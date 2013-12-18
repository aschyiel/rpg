package org.aschyiel.rpg;

import android.os.Bundle; 
import android.app.Activity;
import android.view.Menu;

import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

public class MainActivity extends BaseGameActivity {

  //-----------------------------------
  //
  // Constants.
  //
  //-----------------------------------

  private static final int CAMERA_WIDTH  = 800;
  private static final int CAMERA_HEIGHT = 400;

  //-----------------------------------
  //
  // Private Variables.
  //
  //-----------------------------------

  private Camera camera;
  
  //-----------------------------------
  //
  // Public Methods
  //
  //-----------------------------------

  @Override
  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


  @Override
  public EngineOptions onCreateEngineOptions() {
    camera = new Camera( 0, 0, CAMERA_WIDTH, CAMERA_HEIGHT );
    EngineOptions engineOptions = new EngineOptions( true,
        ScreenOrientation.LANDSCAPE_FIXED,
        new RatioResolutionPolicy( CAMERA_WIDTH, CAMERA_HEIGHT ),
        camera );
    return engineOptions;
  }

  @Override
  public void onCreateResources(
      OnCreateResourcesCallback pOnCreateResourcesCallback)
      throws Exception {
    // TODO Auto-generated method stub
  	
  }


  @Override
  public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
      throws Exception {
    // TODO Auto-generated method stub
  	
  }


  @Override
  public void onPopulateScene(Scene pScene,
      OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
    // TODO Auto-generated method stub
  	
  }
    
}
