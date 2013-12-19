package org.aschyiel.rpg.activities;

import java.util.Collection;

import android.os.Bundle; 
import android.app.Activity;
import android.view.Menu;
import android.view.Display;

import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.aschyiel.rpg.Coordinates;
import org.aschyiel.rpg.IGameObject;

/**
 * Written against AndEngine GLES2 
 * The terrain-view part of the game.
 *
 */
public class Terrain
    extends BaseGameActivity
    implements IClickDetectorListener
{

  //-----------------------------------
  //
  // Constants.
  //
  //-----------------------------------

  private static final int CAMERA_WIDTH  = 720;
  private static final int CAMERA_HEIGHT = 480;

  //-----------------------------------
  //
  // Private Variables.
  //
  //-----------------------------------

  private ClickDetector clickDetector;
  
  private Camera camera;

  private RepeatingSpriteBackground grassBackground;

  private BitmapTextureAtlas bitmapTextureAtlas;
  private TiledTextureRegion tankTextureRegion;
 
  /**
   * The currently user-selected game-object.
   */
  private IGameObject selected;

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

  @Override
  public void onCreateResources( OnCreateResourcesCallback callback )
      throws Exception
  {
    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath( "gfx/" );

    grassBackground = new RepeatingSpriteBackground(
        CAMERA_WIDTH, CAMERA_HEIGHT, getTextureManager(),
        AssetBitmapTextureAtlasSource.create( getAssets(), "gfx/background_grass.png"),
        getVertexBufferObjectManager() );

    bitmapTextureAtlas = new BitmapTextureAtlas( getTextureManager(), 128, 128 );
    tankTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
        bitmapTextureAtlas, this, "tank1.png", 0, 0, 3, 4 );
    bitmapTextureAtlas.load(); 

    callback.onCreateResourcesFinished();
  }

  @Override
  public void onCreateScene( OnCreateSceneCallback callback )
      throws Exception
  {
    final Scene scene = new Scene();
    scene.setBackground( grassBackground );

    //
    // Add game objects.
    //

    final AnimatedSprite tank = new AnimatedSprite( 10, 10, 48, 64,
        tankTextureRegion, getVertexBufferObjectManager() );
    scene.attachChild( tank );

    //
    // Add listeners.
    //
    clickDetector = new ClickDetector( this ); 

    //
    // Finally, execute the callback.
    //

    callback.onCreateSceneFinished( scene );
  }


  @Override
  public void onPopulateScene( Scene scene, OnPopulateSceneCallback callback )
      throws Exception
  {
    setupTiles();
    
    callback.onPopulateSceneFinished();
  }

  /**
   * Our chess board.
   */
  private Collection<TerrainTile> tiles;
  
  /**
   * Look up a terrain-tile based on the in-game coordinates. 
   */
  private TerrainTile getTile( Coordinates coords )
  {
    final int l = TerrainTile.SIZE;
    float x = coords.getX(); 
    float y = coords.getY();
    x -= x % l;    // Snap them to the tile coordinates.
    y -= y % l;
    
    Coordinates them;
    for ( TerrainTile tile : tiles )
    {
      them = tile.coords;
      if ( them.getX()     >= x &&
           them.getX() + l <= x &&
           them.getY()     <= y &&
           them.getY() + l <= y )
      {
        // If our coordinates takes place within a tile, then that's the one.
        return tile;
      }
    }
    return null;
  }
  
  /**
   * Required by IClickDetectorListener
   * 
   * The user clicked somewhere within the view,
   * figure out which game-object they clicked on,
   * and apply the appropriate actions from therein.
   */
  @Override 
  public void onClick( final ClickDetector pClickDetector,
                       final int pPointerID,
                       final float pSceneX,
                       final float pSceneY )
  {
    final Coordinates here = new Coordinates( pSceneX, pSceneY );
    final TerrainTile tile = getTile( here );
    final IGameObject gameObject = ( null == tile )? null : tile.occupant;
    
    // Select new targets.
    
    // Double-check that it's a legal user-move.
    
    // Apply queued action to new target, if any.



    //
    // Set the currently selected thing in preparation of
    // the next click-handler iteration.
    //

    selected = ( null == gameObject )? null : gameObject;
  }

  /**
   * Setup our chess board pieces;
   * Each tile is assigned an id indicating it's index.
   * Probably as an array.
   */
  private void setupTiles()
  {
    int l       = TerrainTile.SIZE;
    int max     = TerrainTile.DEFAULT_MAX_TILES;
    int rows    = TerrainTile.DEFAULT_MAX_ROWS;
    int columns = TerrainTile.DEFAULT_MAX_COLUMNS;
    // TODO: Different levels will have different tile sizes. -uly, 191213.
        
    for ( int idx = 0; idx < max; idx++ )
    {
      // Snap the coordinates to the tile.
      int x = l * ( idx % columns );
      int y = l * ( idx % rows );
      tiles.add( new TerrainTile( idx, x, y ) );
    }
  }
  
  //-----------------------------------
  //
  // Private Inner Classes
  //
  //-----------------------------------

  /**
   * A unit of spatial representation; a chunk of the terrain view.
   * Think of it as a single square on a chess board.
   *
   * Terrain-tiles are NOT to be rendered.  Things can be rendered where
   * terrain-tiles go, but the tiles themselves are to remain abstract.
   */
  private class TerrainTile
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

    /**
     * The width/height of a tile in coordinate-space units.
     */
    public static final int SIZE = 32;

    TerrainTile( int id, float x, float y )
    {
      this.id = id;
      this.coords = new Coordinates( x, y );
    }

    /**
     * The current occupant for a tile.
     * There can only be a single thing standing on a tile at a time;
     * 
     * Again with the chess analogy, only a single pawn an occupy
     * a single square at a time.
     */
    public IGameObject occupant; 
   
    /** The default board size, modeled after (you guessed it) a chess board. */
    public static final int DEFAULT_MAX_TILES   = 64;
    public static final int DEFAULT_MAX_COLUMNS =  8;
    public static final int DEFAULT_MAX_ROWS    =  8;
  }
  
}
