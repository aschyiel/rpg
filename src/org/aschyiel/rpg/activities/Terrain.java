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
import org.aschyiel.rpg.GameObject;
import org.aschyiel.rpg.GameObjectFactory;
import org.aschyiel.rpg.Resorcerer;
import org.aschyiel.rpg.graph.ChessBoard;
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
    implements IOnSceneTouchListener
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
        ScreenOrientation.LANDSCAPE_FIXED,
        new RatioResolutionPolicy( display.getWidth(), display.getHeight() ),
        cam );
  }

  /**
  * A private reference to the resource manager for this activity instance.
  * Careful not to confuse this with Android's "res".
  */
  private Resorcerer rez;

  @Override
  public void onCreateResources( OnCreateResourcesCallback callback )
      throws Exception
  {
    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath( "gfx/" );
    rez = new Resorcerer( this );
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
    Level lvl = getLevelInfo();

    // Needed for translating board vs. canvas space.
    rowHeight   = CAMERA_HEIGHT / lvl.getBoardRows();
    columnWidth = CAMERA_WIDTH  / lvl.getBoardColumns();

    board = new ChessBoard( lvl.getBoardRows(), lvl.getBoardColumns() );
    if ( DEV )
    {
      board.showSquares( columnWidth, rowHeight, rez, scene );
    }

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
      scene.attachChild( unit.getSprite() );
      int m = details.getColumn();
      int n = details.getRow();
      board.placeUnit( unit, n, m );
      unit.setPosition( asCoords( m, n ) );
    }
  }

  /**
  * Translate boards-squares (rows and columns) into AndEngine pixels.
  */
  private Coords asCoords( int m, int n )
  {
    return new Coords( n * columnWidth, m * rowHeight );
  }
  private Integer rowHeight;
  private Integer columnWidth;

}