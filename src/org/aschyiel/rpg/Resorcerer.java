package org.aschyiel.rpg;

import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.aschyiel.rpg.activities.Terrain;

import android.content.res.AssetManager;

/**
* The RPG Resource Manager organizes textures and things needed by
* game-object as they get created by the game-object factory.
*
* Think of it as a simple hash-map full of goodies.
*
* A pseudo singleton for simplicity (for now at least).
*/
public class Resorcerer
{
  //---------------------------------
  //
  // Private Variables.
  //
  //---------------------------------

  private VertexBufferObjectManager vertexBufferObjectManager;

  private TextureManager textureManager;

  private BitmapTextureAtlas atlas;

  private BaseGameActivity gameActivity;

  private AssetManager assets;

  //---------------------------------
  //
  // Chainable property setters.
  //
  //---------------------------------

  public Resorcerer setVertexBufferObjectManager( VertexBufferObjectManager it )
  {
    vertexBufferObjectManager = it;
    return this;
  }

  public Resorcerer setTextureManager( TextureManager it )
  {
    textureManager = it;
    return this;
  }

  public Resorcerer setAssets( AssetManager it )
  {
    assets = it;
    return this;
  }

  public Resorcerer setGameActivity( BaseGameActivity it )
  {
    gameActivity = it;
    return this;
  }

  //---------------------------------
  //
  // Public properties by name - we don't need no stinking getters.
  //
  //---------------------------------

  public TiledTextureRegion tankTextureRegion;

  public RepeatingSpriteBackground grassBackground;


  /**
  * Setup our graphics, etc.
  * To be called by the parent game-activity when it's onCreateResources happens.
  */
  public void onCreateResources()
  { 
    grassBackground = new RepeatingSpriteBackground(
        Terrain.CAMERA_WIDTH,
        Terrain.CAMERA_HEIGHT,
        textureManager,
        AssetBitmapTextureAtlasSource.create( assets, "gfx/background_grass.png" ),
        vertexBufferObjectManager );

    atlas = new BitmapTextureAtlas( textureManager, 128, 128 );

        tankTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
            atlas, gameActivity, "tank1.png", 0, 0, 3, 4 ); 

    // Finally, load all our stuff.
    atlas.load();
  }

  /**
   * Returns a new tank-sprite.
   */
  public AnimatedSprite getTankSprite()
  {
    return new AnimatedSprite( 10, 10, 48, 64,
       tankTextureRegion, vertexBufferObjectManager );
   
  }

  


}