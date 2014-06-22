package org.aschyiel.rpg;

import android.content.res.AssetManager;

import java.util.HashMap;
import java.util.Map;

import org.andengine.entity.scene.background.IBackground;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import org.aschyiel.rpg.activities.Terrain; 

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
  private Terrain tera;
  
  /**
   * Named backgrounds - ie. the "grass-background".
   */
  public Map<String, IBackground> backgrounds;

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
    
  }
  
  /**
   * Makes a repeating background of sorts. 
   */
  private IBackground bg( String path )
  { 
    return new RepeatingSpriteBackground(
        tera.CAMERA_WIDTH,
        tera.CAMERA_HEIGHT,
        tex,
        AssetBitmapTextureAtlasSource.create( missy, path ),
        buffy );
  }
}
