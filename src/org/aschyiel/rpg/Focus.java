package org.aschyiel.rpg;

import org.andengine.entity.sprite.TiledSprite;
import org.aschyiel.rpg.level.Player;

/**
* The "focus" represents the current selection within the game.
* It's context sensitive to the target ( ie. friendly vs. neutral vs.
* enemy targets are to be represented differently ).
*/
public class Focus extends TiledSprite
{
  /**
  * Who we're focusing on behalf of.
  */
  private final Player perspective;

  public Focus( Resorcerer rez, Player perspective, int w, int h )
  {
    super( 0, 0, rez.getTexture( "focus" ), rez.getVertexBufferObjectManager() );
    setTarget( null );
    this.perspective = perspective;
    setSize( (float) w, (float) h );
  }
  
  public void setTarget( GameObject unit )
  {
    // Massive todo.

    // if same player or same team, friendly.
    // if neautral, say neutral.
    // else enemy.
    setAlpha( ( null == unit )? 0 : 1 );
  }
}
