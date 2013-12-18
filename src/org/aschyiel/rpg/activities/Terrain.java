package org.aschyiel.rpg.activities;

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
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

/**
 * Written against AndEngine GLES2 
 * The terrain-view part of the game.
 *
 */
public class Terrain
    extends BaseGameActivity
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

  private Camera camera;

  private RepeatingSpriteBackground grassBackground;

  private BitmapTextureAtlas bitmapTextureAtlas;
  private TiledTextureRegion playerTextureRegion;

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
    playerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
        bitmapTextureAtlas, this, "player.png", 0, 0, 3, 4 );
    bitmapTextureAtlas.load();
    
    callback.onCreateResourcesFinished();
  }


  @Override
  public void onCreateScene( OnCreateSceneCallback callback )
      throws Exception
  {
    final Scene scene = new Scene();
    scene.setBackground( grassBackground );
    
    callback.onCreateSceneFinished( scene );
  }


  @Override
  public void onPopulateScene( Scene scene, OnPopulateSceneCallback callback )
      throws Exception
  {
    callback.onPopulateSceneFinished();
  }
    
}
