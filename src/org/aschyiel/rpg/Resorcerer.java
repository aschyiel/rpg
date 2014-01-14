package org.aschyiel.rpg;

import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
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
  private TextureManager            textureManager;
  private BitmapTextureAtlas        atlas;
  private BaseGameActivity          gameActivity;
  private AssetManager              assets;

  // Texture Regions.
  private TiledTextureRegion tankTextureRegion;
  private TiledTextureRegion terrainTileTextureRegion;
  private TiledTextureRegion navPointTextureRegion;

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

  public RepeatingSpriteBackground grassBackground;

  /**
  * Setup our graphics, etc.
  * To be called by the parent game-activity when it's onCreateResources happens.
  */
  public void onCreateResources()
  {
    // GOTCHA: Assumes that "gfx/" is already set as our base-path.

    grassBackground = new RepeatingSpriteBackground(
        Terrain.CAMERA_WIDTH,
        Terrain.CAMERA_HEIGHT,
        textureManager,
        AssetBitmapTextureAtlasSource.create( assets, "gfx/background_grass.png" ),
        vertexBufferObjectManager );

    // Make our atlas big enough to hold all of our textures.
    atlas = new BitmapTextureAtlas( textureManager, 1024, 1024 );

    // 96x128
    int y = 0;    // The current "height" index.
    tankTextureRegion        = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
        atlas, gameActivity, "tank1.png",
        0, y, 3, 4 );
    y += 128;

    // 32x96
    terrainTileTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
        atlas, gameActivity, "terrain_tile.png",
        0, y, 1, 3 );
    y += 32;

    // 48x48
    navPointTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
        atlas, gameActivity, "nav_point.png",
        0, y, 1, 1 );
    y += 48;

    // Finally, load all our stuff.
    atlas.load();
  }

  public Sprite getNavigationalPointSprite()
  {
    return getNavigationalPointSprite( 0, 0 );
  }

  /**
  * Returns a navigation point sprite, which may or may not be new.
  */
  public Sprite getNavigationalPointSprite( float x, float y )
  {
    // TODO support different navigation-point types,
    //   ie. going left, various turnings, etc.
    // TODO pool these. -uly, 221213
    return new AnimatedSprite( x, y, navPointTextureRegion, vertexBufferObjectManager );
  }

  /**
   * Returns a new tank-sprite.
   */
  public Sprite getTankSprite()
  {
    return new AnimatedSprite( 0, 0,
        tankTextureRegion, vertexBufferObjectManager );
  }

  /**
  * Returns a new terrain-tile sprite.
  */
  public Sprite getTerrainTileSprite()
  {
    return new TiledSprite( 0, 0,
        terrainTileTextureRegion, vertexBufferObjectManager );
  }

}