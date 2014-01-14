package org.aschyiel.rpg.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.os.Bundle; 
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.Display;
import android.view.MotionEvent;

import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import org.aschyiel.rpg.Coordinates;
import org.aschyiel.rpg.IGameObject;
import org.aschyiel.rpg.Movement;
import org.aschyiel.rpg.Resorcerer;
import org.aschyiel.rpg.GameObjectFactory;
import org.aschyiel.rpg.GameObjectType;
import org.aschyiel.rpg.graphing.Edge;
import org.aschyiel.rpg.graphing.Graph;
import org.aschyiel.rpg.graphing.SimpleMovementStrategy;
import org.aschyiel.rpg.graphing.Vertex;

/**
 * Written against AndEngine GLES2 
 * The terrain-view part of the game.
 * Singleton.
 */
public class Terrain
    extends BaseGameActivity
    implements IOnSceneTouchListener
{
  
  //-----------------------------------
  //
  // Constants.
  //
  //-----------------------------------

  /** For printing to console. */
  private static final String TAG = "[Terrain]";

  public static final int CAMERA_WIDTH  = 720;
  public static final int CAMERA_HEIGHT = 480;

  //-----------------------------------
  //
  // Private Variables.
  //
  //-----------------------------------

  private ClickDetector clickDetector;
  
  private Camera camera;
 
  /**
   * The currently user-selected game-object.
   */
  private IGameObject currentlySelectedUnit;

  //-----------------------------------
  //
  // Overriden Methods
  //
  //----------------------------------- 

  @Override
  public EngineOptions onCreateEngineOptions()
  {
    camera = new Camera( 0, 0, CAMERA_WIDTH, CAMERA_HEIGHT );
    
    final Display display = getWindowManager().getDefaultDisplay();
    int displayWidth      = display.getWidth();
    int displayHeight     = display.getHeight();
    
    EngineOptions engineOptions = new EngineOptions( true,
        ScreenOrientation.LANDSCAPE_SENSOR,
        new RatioResolutionPolicy( displayWidth, displayHeight ),
        camera );
    return engineOptions;
  }

  /** A private reference to the resource manager for this activity instance. */
  private Resorcerer resorcerer;

  @Override
  public void onCreateResources( OnCreateResourcesCallback callback )
      throws Exception
  {
    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath( "gfx/" );

    resorcerer = new Resorcerer()
        .setGameActivity(              this )
        .setTextureManager(            getTextureManager() )
        .setAssets(                    getAssets() )
        .setVertexBufferObjectManager( getVertexBufferObjectManager() );
    resorcerer.onCreateResources();

    callback.onCreateResourcesFinished();
  }

  private GameObjectFactory plant;
  
  @Override
  public void onCreateScene( OnCreateSceneCallback callback )
      throws Exception
  {
    final Scene scene = new Scene();
    scene.setBackground( resorcerer.grassBackground );

    plant = new GameObjectFactory( this, resorcerer );
    setupTiles( scene );
    setupGraphs();
    setupNavPointPool( scene );

    //
    // Temporarily setup a tank for testing purposes.
    //

    final IGameObject tank = plant.make( GameObjectType.TANK );
    tank.setCoords( new Coordinates( 0, 0 ) );
    scene.attachChild( tank.getSprite() );    // TODO: Abstract via controller.
    tiles.get( 0 ).occupant = tank;


    //
    // Add listeners.
    //

    scene.setOnSceneTouchListener( this );

    //
    // Finally, execute the callback.
    //

    callback.onCreateSceneFinished( scene );
  }


  @Override
  public void onPopulateScene( Scene scene, OnPopulateSceneCallback callback )
      throws Exception
  {
    callback.onPopulateSceneFinished();
  }

  /**
  * The maximum number of navigation points we're ever gonna need.
  */
  private static final int MAX_NAV_PTS =
      TerrainTile.DEFAULT_MAX_ROWS * TerrainTile.DEFAULT_MAX_COLUMNS;

  /**
  * Populate our pool of nav. points - used in representing unit movements.
  * To be called at init-time.
  */
  private void setupNavPointPool( Scene scene )
  {
    int i = MAX_NAV_PTS;
    while ( 0 < i-- )
    {
      IGameObject nav = plant.make( GameObjectType.NAVIGATIONAL_POINT );
      scene.attachChild( nav.getSprite() );
      _navPointsPool.add( nav );
    }
  }

  /**
   * Our chess board.
   */
  private List<TerrainTile> tiles = new ArrayList<TerrainTile>();

  /**
   * Setup our chess board pieces;
   * Each tile is assigned an id indicating it's index.
   * Probably as an array.
   */
  private void setupTiles( Scene scene )
  {
    final int width   = TerrainTile.WIDTH;
    final int height  = TerrainTile.HEIGHT;
    final int rows    = TerrainTile.DEFAULT_MAX_ROWS;
    final int columns = TerrainTile.DEFAULT_MAX_COLUMNS;

    // The id is synonimous with the array index, I guess.
    int idx      = 0;
    // TODO: Different levels will have different tile sizes? -uly, 191213.

    for ( int row = 0; row < rows; row++ )
    {
      for ( int column = 0; column < columns; column++ )
      {
        // Snap the coordinates to the tile.
        int x = width * ( column % columns );
        int y = height * row ;
        TerrainTile tile = new TerrainTile( idx, x, y, row, column );
        scene.attachChild( tile.gameObject.getSprite() );
        tiles.add( tile );
        idx++;
      }
    }

    //
    // 2nd loop to hook-up all of the neighbor-references.
    //

    TerrainTile tile = null;
    while ( 0 < idx-- )
    {
      tile = tiles.get( idx );
      tile.top    = ( 0 == tile.row )?                  null : tiles.get( idx - rows );
      tile.bottom = ( ( rows - 1 ) == tile.row )?       null : tiles.get( idx + rows );
      tile.left   = ( 0 == tile.column )?               null : tiles.get( idx - 1 );
      tile.right  = ( ( columns - 1 ) == tile.column )? null : tiles.get( idx + 1 );
    }
  }

  /**
  * Organize the available movement paths based on the tiles.
  */
  private void setupGraphs()
  {
    setupSimpleGraph();
  }

  private Graph<TerrainTile> _simpleGraph;

  /**
  * Setup the simplest movement graph of left, right, up, down,
  * by one space at a time.
  */
  private void setupSimpleGraph()
  {
    final Set<Vertex <TerrainTile>> vertices = new HashSet<Vertex <TerrainTile>>();
    final HashMap<TerrainTile, Vertex <TerrainTile>> verticesByTile = new HashMap<TerrainTile, Vertex <TerrainTile>>();
    for ( TerrainTile tile : tiles )
    {
      final Vertex<TerrainTile> vertex = new Vertex<TerrainTile>( tile );
      vertices.add( vertex );
      verticesByTile.put( tile, vertex );
    }

    final Set<Edge<TerrainTile>> edges = new HashSet<Edge <TerrainTile>>();
    for ( TerrainTile tile : tiles )
    {
      // TODO: javaScript closures would be nice here... -uly, 130114
      final Vertex<TerrainTile> from = verticesByTile.get( tile );
      if ( null != tile.top )
      {
        edges.add( new Edge<TerrainTile>( from, verticesByTile.get( tile.top ) ) );
      }
      if ( null != tile.bottom )
      {
        edges.add( new Edge<TerrainTile>( from, verticesByTile.get( tile.bottom ) ) );
      }
      if ( null != tile.left )
      {
        edges.add( new Edge<TerrainTile>( from, verticesByTile.get( tile.left ) ) );
      }
      if ( null != tile.right )
      {
        edges.add( new Edge<TerrainTile>( from, verticesByTile.get( tile.right ) ) );
      }
    }

    _simpleGraph = new Graph<TerrainTile>( vertices, edges );
  }

  /**
   * Look up a terrain-tile based on the in-game coordinates. 
   */
  private TerrainTile getTile( Coordinates coords )
  {
    final Coordinates ohSnap = coords.getSnappedCoordinates();
    final int perCol = TerrainTile.DEFAULT_MAX_COLUMNS;
    final int col = (int) ohSnap.getX() / TerrainTile.WIDTH;
    final int row = (int) ohSnap.getY() / TerrainTile.HEIGHT;
    final int idx = ( row * perCol ) + col;
    final TerrainTile tile = tiles.get( idx );
    Log.d( TAG, "getTile:"+ tile );
    if ( null == tile )
    {
      throw new Error( "Something is fundamentally wrong here - "+
                       "we failed to find a tile by coordinate!" );
    }
    return tile;
  }

  /**
   * Required by IOnSceneTouchListener.
   * The user touched the view, do something about it.
   * 
   * Returns true to represent the event was handled.
   */
  @Override
  public boolean onSceneTouchEvent( final Scene scene, final TouchEvent event )
  {
    switch( event.getAction() )
    {
      case MotionEvent.ACTION_DOWN:
        return false;
      case MotionEvent.ACTION_UP:
        handleActionUp( event.getX(), event.getY() );
        return true;
      case MotionEvent.ACTION_MOVE:
        return false;
      default:
        return false;
    } 
  }
  
  /**
   * The user clicked somewhere within the view,
   * figure out which game-object they clicked on,
   * and apply the appropriate actions from therein.
   */
  public void handleActionUp( final float pSceneX,
                                final float pSceneY )
  {
    final Coordinates here = new Coordinates( pSceneX, pSceneY );
    final TerrainTile tile = getTile( here );

    final IGameObject it = currentlySelectedUnit;    // shorthand.

    // The thing we're applying out click against, if any.
    // ie. Targeting another tank to attack.
    final IGameObject targetUnit = ( null == tile )?
        null : ( it == tile.occupant )?
        null : tile.occupant;

    Log.d( TAG, "targetUnit:"+ targetUnit +", \n"+
                "it:"+         it         +", \n"+
                "tile:"+       tile       +", \n"+
                "here:"+       here       +"." );

    //
    // Apply the appropriate implied action based on what we've clicked on.
    //
    if ( null != it )
    { 
      // TODO: Abstract this to allow different actions like "Healing" other units. -uly, 191213.
      boolean isMoving = null == targetUnit;
      if ( isMoving )
      {
        it.move( here.getSnappedCoordinates() );
      }
      else if ( null != targetUnit )
      {
        it.attack( targetUnit );
      }
    }

    //
    // Set focus to the "next" thing, if appropriate.
    // ie. When attacking, keep our selection on the current tank, etc.
    //

    currentlySelectedUnit = ( null == targetUnit )?
                            currentlySelectedUnit : ( it != targetUnit && targetUnit.isBelongToUs() )?
                            targetUnit : currentlySelectedUnit;
    if ( null != currentlySelectedUnit )
    {
      setFocus( currentlySelectedUnit );
    }
  }

  /**
  * Keep track of the previously UI "focused" unit;
  * This is only slightly different than the currently-selected unit.
  */
  private IGameObject previousFocus;

  /**
  * Set the next thing we're gonna focus;
  * Only allow one thing at a time.
  */
  private void setFocus( IGameObject it )
  {
    if ( null != previousFocus && previousFocus != it )
    {
      previousFocus.unfocus();
    }
    if ( it != previousFocus )
    {
      it.focus();
    }
    previousFocus = it;
  }


  /**
  * Our HUD navigational points representing a unit's movement path.
  */
  private List<IGameObject> _visibleNavPoints = new ArrayList<IGameObject>();

  /**
  * A pool of available nav. points.
  */
  private List<IGameObject> _navPointsPool = new ArrayList<IGameObject>();

  /**
  * Hide the navigation-path is currently being displayed if any.
  */
  public void hideNavigation()
  {
    for ( IGameObject nav : _visibleNavPoints )
    {
      nav.getSprite().setVisible( false );
    }
    _visibleNavPoints.clear();
  }

  /**
  * Display the navigation-path (usually for a unit) to the user.
  */
  public void showNavigation( List<Movement> moves )
  {
    IGameObject nav = null;
    int i = 0;

    for ( Movement move : moves )
    {
      nav = _navPointsPool.get( i++ );
      // TODO Set the type of navigation-point to render here - per instance.
      nav.setCoords( move.to );
      nav.getSprite().setVisible( true );
      _visibleNavPoints.add( nav );
    }

    // GOTCHA: set the entry point (a closure would be nice here, huh?).
    nav = _navPointsPool.get( i++ );
    // TODO Set the type of navigation-point to render here - per instance.
    nav.setCoords( moves.get( 0 ).from );
    nav.getSprite().setVisible( true );
    _visibleNavPoints.add( nav );
  }

  /**
  * Calculate the movement path from point A to point B;
  * aka a graphing problem.
  */
  public List<Movement> calculatePath( Coordinates from,
      Coordinates to )
  {
    // TODO: Accept movement rules/strategies.
    //   ie. for knights that move in an L,
    //   ie2. flying vehicles can probably move most places.
    final Graph<TerrainTile> graph = getSimpleGraph();
    final Vertex<TerrainTile> origin      = graph.getVertex( getTile( from ) );
    final Vertex<TerrainTile> destination = graph.getVertex( getTile( to ) );
    return SimpleMovementStrategy.<TerrainTile>calculatePath( origin, destination, graph );
  }

  private Graph<TerrainTile> getSimpleGraph()
  {
    return _simpleGraph;
  }

  //-----------------------------------
  //
  // Public Inner Classes
  //
  //-----------------------------------

  /**
   * A unit of spatial representation; a chunk of the terrain view.
   * Think of it as a single square on a chess board.
   *
   * Terrain-tiles are NOT to be rendered.  Things can be rendered where
   * terrain-tiles go, but the tiles themselves are to remain abstract.
   */
  public class TerrainTile
  {
    /**
     * The tile number corresponding to the tile array index.
     */
    public int id;

    /**
     * The "exact" location of this piece of tile.
     * Useful for quickly placing units exactly within it's proper bounds.
     */
    public Coordinates coords;

    /** The sprite representation for our tile. */
    public IGameObject gameObject; 

    /**
    * Neighboring tile references.
    */
    public TerrainTile left   = null;
    public TerrainTile right  = null;
    public TerrainTile top    = null;
    public TerrainTile bottom = null;

    /**
    * Matrix positional information.
    */
    public int row    = -1;
    public int column = -1;

    TerrainTile( int id, float x, float y, int row, int column )
    {
      this.id = id;
      this.coords = new Coordinates( x, y );
      this.gameObject = plant.make( GameObjectType.TERRAIN_TILE );
      this.gameObject.setCoords( this.coords );
      this.row    = row;
      this.column = column;
    }

    @Override
    public String toString()
    {
      return "[Tile id:"+ id +", coords:"+ coords +"]";
    }

    /**
     * The current occupant for a tile.
     * There can only be a single thing standing on a tile at a time;
     * 
     * Again with the chess analogy, only a single pawn an occupy
     * a single square at a time.
     */
    public IGameObject occupant; 

    // Should provide about 48x48 on 720x480.
    public static final int DEFAULT_MAX_COLUMNS =  15;
    public static final int DEFAULT_MAX_ROWS    =  10;

    /** A terrain-tile width in (probably pixels?) scene coordinate units. */
    public static final int WIDTH  = CAMERA_WIDTH / DEFAULT_MAX_COLUMNS;

    /** A terrain-tile width in (probably pixels?) scene units. */
    public static final int HEIGHT = CAMERA_HEIGHT / DEFAULT_MAX_ROWS;

  }

  //-----------------------------------
  //
  // Private Inner Classes
  //
  //----------------------------------- 
}
