package org.aschyiel.rpg;

import android.content.res.AssetManager;

import java.util.HashMap;
import java.util.Map;

import org.andengine.entity.scene.background.IBackground;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.aschyiel.rpg.activities.Terrain; 
import org.aschyiel.rpg.level.BackgroundType;

/**
* The resource management solution of good and NOT evil.
*
* Instantiation synonymous with BaseGameActivity#onCreateResources.
*/
public class Resorcerer
{
  private TextureManager tex;
  private VertexBufferObjectManager buffy;
  private AssetManager missy;

  /** A reference to the parent game. */
  private Terrain tera;

  /** An OP mechwarrior. */
  private BitmapTextureAtlas atlas;

  /**
   * Named backgrounds - ie. the "grass-background".
   */
  private final Map<String, IBackground> backgrounds;

  /**
  * Named tile texture-regions (within the texture-atlas).
  */
  private final Map<String, TiledTextureRegion> textures;

  public Resorcerer( Terrain tera )
  {
    // Assign references.
    this.tera = tera;
    tex       = tera.getTextureManager();
    buffy     = tera.getVertexBufferObjectManager();
    missy     = tera.getAssets();
    
    // Build-up backgrounds.
    backgrounds = new HashMap<String, IBackground>();
    backgrounds.put( "grass", bg( "gfx/background_grass.png" ) );

    // Build-up tile-textures.
    textures = new HashMap<String, TiledTextureRegion>();
    setupTextureAtlas();
  }

  public TiledTextureRegion getUnitTexture( GameObject it )
  {
    // TODO: Allow unit differentiation based on the owner (ie. blue tanks
    //   for player1, red tanks for player2, etc.).
    return getTexture( it.getUnitType().toString() );
  }

  public TiledTextureRegion getTexture( String name )
  {
    return textures.get( name );
  }

  public IBackground getBackground( BackgroundType bg )
  {
    return backgrounds.get( bg.name );
  }

  public VertexBufferObjectManager getVertexBufferObjectManager()
  {
    return buffy;
  }

  /**
  * Make our atlas big enough to hold all of our textures.
  */
  private void setupTextureAtlas()
  {
    atlas = new BitmapTextureAtlas( tex, 1024, 1024 );
    textures.put( "tank",         tile( "tank1.png",        3, 4, 128 ) );    // 96x128
    textures.put( "terrain_tile", tile( "terrain_tile.png", 1, 3,  32 ) );    // 32x96
    textures.put( "nav_point",    tile( "nav_point.png",    1, 1,  48 ) );    // 48x48
    atlas.load();    //..last!..
  }

  /**
  * Short-hand for making a tile-region within the atlas.
  */
  private TiledTextureRegion tile( String img, int w, int h, int idx )
  {
    int x = 0;
    int y = atlasIndex;
    TiledTextureRegion tile = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
        atlas, tera, img,
        x, y, w, h );
    atlasIndex += idx;
    return tile;
  }
  private int atlasIndex = 0;

  /**
   * Makes a repeating background of sorts. 
   */
  private IBackground bg( String path )
  { 
    return new RepeatingSpriteBackground(
        Terrain.CAMERA_WIDTH,
        Terrain.CAMERA_HEIGHT,
        tex,
        AssetBitmapTextureAtlasSource.create( missy, path ),
        buffy );
  }
}
