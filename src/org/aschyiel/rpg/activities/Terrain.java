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

import org.aschyiel.rpg.Resorcerer;

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

  //-----------------------------------
  //
  // Private Variables.
  //
  //-----------------------------------

  private ClickDetector clickDetector;
  private Camera cam;

  //-----------------------------------
  //
  // Overriden Methods
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

  @Override
  public void onCreateResources( OnCreateResourcesCallback callback )
      throws Exception
  {
    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath( "gfx/" );
    rez = new Resorcerer( this );
    callback.onCreateResourcesFinished();
  }
  
  @Override
  public void onCreateScene( OnCreateSceneCallback callback )
      throws Exception
  {
    // TODO: Add listeners.
    final Scene scene = new Scene();
    callback.onCreateSceneFinished( scene );
  }

  @Override
  public void onPopulateScene( Scene scene, OnPopulateSceneCallback callback )
      throws Exception
  {
    // TOOD:
    //   1. Populate units.
    //   2. Connect tiles.
    //   3. Render tiles based on level design.
    scene.setBackground( rez.backgrounds.get( "grass" ) );
    callback.onPopulateSceneFinished();
  }

  @Override
  public boolean onSceneTouchEvent( Scene pScene, TouchEvent pSceneTouchEvent )
  {
    // TODO Auto-generated method stub
    return false;
  }

}