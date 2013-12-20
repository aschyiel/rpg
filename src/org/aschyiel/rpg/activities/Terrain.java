package org.aschyiel.rpg.activities;

import java.util.ArrayList;
import java.util.Collection;

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
import org.aschyiel.rpg.Resorcerer;
import org.aschyiel.rpg.GameObjectFactory;
import org.aschyiel.rpg.GameObjectType;

/**
 * Written against AndEngine GLES2 
 * The terrain-view part of the game.
 *
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

    plant = new GameObjectFactory( resorcerer );
    final IGameObject tank = plant.make( GameObjectType.TANK );
    tank.setCoords( new Coordinates( 50, 50 ) );
    scene.attachChild( tank.getSprite() );    // TODO: Abstract via controller.

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
    setupTiles();
    
    callback.onPopulateSceneFinished();
  }

  /**
   * Our chess board.
   */
  private Collection<TerrainTile> tiles = new ArrayList<TerrainTile>();

  
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
        handleSceneClick( event.getX(), event.getY() );
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
  public void handleSceneClick( final float pSceneX,
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

Log.d( TAG, "targetUnit:"+ targetUnit );
Log.d( TAG, "it:"+ it );
Log.d( TAG, "tile:"+ tile );
Log.d( TAG, "here:"+ here );

    //
    // Apply the appropriate implied action based on what we've clicked on.
    //
    if ( null != it )
    { 
      // TODO: Abstract this to allow different actions like "Healing" other units. -uly, 191213.
      boolean isMoving = null == targetUnit;
      if ( isMoving )
      {
        it.move( here );
      }
      else if ( null != targetUnit )
      {
        it.attack( targetUnit );
      }
    }

    //
    // Set the currently selected thing in preparation of
    // the next click-handler iteration.
    //

    currentlySelectedUnit = ( null == targetUnit )?
                            null       : ( it != targetUnit && targetUnit.isBelongToUs() )?
                            targetUnit : null;
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
