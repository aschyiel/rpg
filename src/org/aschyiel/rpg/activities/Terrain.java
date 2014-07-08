package org.aschyiel.rpg.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
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
import org.aschyiel.rpg.Coords;
import org.aschyiel.rpg.Focus;
import org.aschyiel.rpg.GameObject;
import org.aschyiel.rpg.ICanHasFocus;
import org.aschyiel.rpg.IFullGameObject;
import org.aschyiel.rpg.IGameObject;
import org.aschyiel.rpg.GameObjectFactory;
import org.aschyiel.rpg.IGameObject;
import org.aschyiel.rpg.PowerChords;
import org.aschyiel.rpg.Resorcerer;
import org.aschyiel.rpg.graph.ChessBoard;
import org.aschyiel.rpg.graph.GirlFriend;
import org.aschyiel.rpg.graph.Navigator;
import org.aschyiel.rpg.graph.OnSquareClickHandler;
import org.aschyiel.rpg.graph.ChessBoard.Square;
import org.aschyiel.rpg.level.BackgroundType;
import org.aschyiel.rpg.level.Level;
import org.aschyiel.rpg.level.LevelDetail;
import org.aschyiel.rpg.level.Player;
import org.aschyiel.rpg.level.UnitType;

/**
* A terrain is like a chess-board, full of tiles (squares),
* and units that are placed and move around the board.
*
* Written against AndEngine GLES2.
*/
public class Terrain
    extends BaseGameActivity
    implements IOnSceneTouchListener, OnSquareClickHandler, PowerChords
{
  
  //-----------------------------------
  //
  // Constants.
  //
  //-----------------------------------

  public static final int CAMERA_WIDTH  = 720;
  public static final int CAMERA_HEIGHT = 480;

  /**
  * When enabled, show additional information, sandboxes, etc.
  */
  public static final boolean DEV = true;
  private static final String TAG = "[RPG:Terrain]";

  //-----------------------------------
  //
  // Private Variables.
  //
  //-----------------------------------

  private ClickDetector clickDetector;
  private Camera cam;
  private ChessBoard board;
  private GameObjectFactory plant;

  //-----------------------------------
  //
  // Overridden Methods
  //
  //----------------------------------- 

  @Override
  public EngineOptions onCreateEngineOptions()
  {
    cam = new Camera( 0, 0, CAMERA_WIDTH, CAMERA_HEIGHT );
    final Display display = getWindowManager().getDefaultDisplay();
    return new EngineOptions(
        true,
        ScreenOrientation.LANDSCAPE_SENSOR,
        new RatioResolutionPolicy( display.getWidth(), display.getHeight() ),
        cam );
  }

  /**
  * A private reference to the resource manager for this activity instance.
  * Careful not to confuse this with Android's "res".
  */
  private Resorcerer rez;
  private Terrain tera;
  private Navigator gf;

  @Override
  public void onCreateResources( OnCreateResourcesCallback callback )
      throws Exception
  {
    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath( "gfx/" );
    tera = this;
    rez = new Resorcerer( tera );
    plant = new GameObjectFactory( rez );
    callback.onCreateResourcesFinished();
  }
  
  @Override
  public void onCreateScene( OnCreateSceneCallback callback )
      throws Exception
  {
    // TODO: Add listeners.
    final Scene scene = new Scene();
    scene.setTouchAreaBindingOnActionDownEnabled( true );
    callback.onCreateSceneFinished( scene );
  }

  @Override
  public void onPopulateScene( final Scene scene, OnPopulateSceneCallback callback )
      throws Exception
  {
    Log.w( TAG, "Welcome to the league of Draven!" );
    
    Level lvl = getLevelInfo();

    // Needed for translating board vs. canvas space.
    rowHeight   = CAMERA_HEIGHT / lvl.getBoardRows();
    columnWidth = CAMERA_WIDTH  / lvl.getBoardColumns();

    board = new ChessBoard( lvl.getBoardRows(), lvl.getBoardColumns() );
    gf = new GirlFriend( board, (PowerChords) tera );
    if ( DEV )
    {
      board.bindSquares( columnWidth, rowHeight, rez, scene, (OnSquareClickHandler) tera );
    }

    currentPerspective = Player.ONE;    // FIXME: This should be based on who we're signed in.
    focus = new Focus( rez, currentPerspective, columnWidth, rowHeight );
    scene.attachChild( focus );
    
    // TOOD:
    //   1. Populate units.
    //   2. Connect tiles.
    //   3. Render tiles based on level design.
    scene.setBackground( rez.getBackground( BackgroundType.GRASS ) );

    instantiateUnits( scene, lvl.getUnitsIterator() );

    // TODO:
    //   Need to map squares to pixels inorder to place sprites.
    //   
    
    callback.onPopulateSceneFinished();    //..last..
  }

  @Override
  public boolean onSceneTouchEvent( Scene scene, TouchEvent sceneTouchEvent )
  {
    // TODO Auto-generated method stub
    return false;
  }

  /**
  * The current player we're seeing the game through/as.
  */
  private Player currentPerspective;
  
  /**
  * The thing that goes around highlighting the selection for things.
  */
  private Focus focus;
  
  /**
  * Returns a description on how to setup the next level of terrain,
  * where to place units, etc.
  */
  private Level getLevelInfo()
  {
    // TODO: Check who's playing, what they're using, and serialize the level appropriately...
    Level lvl = new Level();
    lvl.getUnits().add( new LevelDetail( UnitType.TANK, 0, 0, Player.ONE ) );
    lvl.getUnits().add( new LevelDetail( UnitType.TANK, 2, 3, Player.CPU ) );
    return lvl;
  }

  private void instantiateUnits( final Scene scene, ListIterator<LevelDetail> it )
  {
    plant.setTargetSize( columnWidth, rowHeight );
    while ( it.hasNext() )
    {
      LevelDetail details = it.next();
      GameObject unit = plant.makeUnit( details.getUnitType(), details.getOwner() );
      scene.attachChild( unit );
      int m = details.getColumn();
      int n = details.getRow();
      board.placeUnit( unit, m, n );
      unit.setPosition( asCoords( m, n ) );
    }
  }

  /**
  * Translate boards-squares (rows and columns) into AndEngine pixels.
  */
  @Override
  public Coords asCoords( int m, int n )
  {
    return new Coords( n * columnWidth, m * rowHeight );
  }
  private Integer rowHeight;
  private Integer columnWidth;

  /**
  * The user click on a square within the board.
  * If there is a unit occupying that square, apply an action to it.
  * 
  * Valid actions are:
  * - unit selection.
  * - set as the "target" of the currently selected unit.
  * - and/or both of the above.
  *
  * @param sq The target location of the click.
  * @param target May be null.
  */
  @Override
  public void onSquareClicked( Square sq, IFullGameObject target )
  {
    Log.d( TAG, "onSquareClicked: square:"+ sq + ", target:"+ target );
    if ( null != currentlySelectedUnit )
    {
      currentlySelectedUnit.applyAction( sq, target, gf );
    }
    setCurrentlySelectedUnit( target );
  }
  
  private void setCurrentlySelectedUnit( IFullGameObject unit )
  {
    final IFullGameObject prev = currentlySelectedUnit;
    if ( null != prev )
    {
      prev.setFocus( null );
    }

    currentlySelectedUnit = unit;
    if ( null != unit )
    {
      unit.setFocus( focus );
    }
    else
    {
      focus.setTarget( null );
    }
    // TODO: Apply selection entity to track it.
    // TODO: Update the HUD to show context information on the unit.
  }

  private IFullGameObject currentlySelectedUnit = null;

}