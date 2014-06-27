package org.aschyiel.rpg;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.aschyiel.rpg.graph.ChessBoard.Square;
import org.aschyiel.rpg.graph.Navigator;
import org.aschyiel.rpg.level.Player;
import org.aschyiel.rpg.level.UnitType;

/**
* Well, um, everything is a game-object at the end of the day.
*/
public class GameObject extends TiledSprite
{
  private final Player owner;
  private final UnitType unitType;
  private final int id;

  public GameObject( UnitType unitType,
                     Player owner,
                     int id,
                     TiledTextureRegion texture,
                     VertexBufferObjectManager buffy )
  {
    super( 0, 0, texture, buffy );
    this.unitType = unitType;
    this.owner    = owner;
    this.id       = id;
  }

  public void setHealth(int i)
  {
    // TODO Auto-generated method stub
    
  }

  public void setMovementSpeed(int i)
  {
    // TODO Auto-generated method stub
    
  }

  public void setAttackDamage(int i)
  {
    // TODO Auto-generated method stub
    
  }

  public UnitType getUnitType()
  {
    return unitType;
  }

  public Integer getId()
  {
    return id;
  }

  /**
  * Immediately update the position of a unit --- skips movement animation.
  * Used when loading level, spawning, etc.
  */
  public void setPosition( Coords koords )
  {
    setPosition( koords.getX(), koords.getY() );
  }

  /**
  * Provide a mechanism for following units.
  *
  * @see http://www.andengine.org/forums/gles2/set-a-sprite-to-follow-another-sprite-t9634.html
  */
  @Override
  public void setPosition( final float x, final float y )
  {
    super.setPosition( x, y );
    if ( null != focus )
    {
      focus.setPosition( x, y );
    }
  }

  private Focus focus = null;
  public void setFocus( Focus focus )
  {
    this.focus = focus;
    if ( null != focus )
    {
      focus.setTarget( this );
      focus.setPosition( mX , mY );
    }
  }

  @Override
  public String toString()
  {
    return "{ id:"+ id +", unitType:"+ unitType +", owner:"+ owner +" }";
  }

  /**
   * Apply the appropriate action to said target.
   * For Example:
   * If we've selected a friendly, maybe we're gonna heal them.
   * Or if it's an empty square, maybe we're gonna move there, if we can.
   * Or if it's an enemy, perhaps we're supposed
   * to attack it (involves moving towards, getting into range, etc.).
   * 
   * @param sq
   * @param target (May be null).
   */
  public void applyAction( Square sq, GameObject target, Navigator gf )
  {
    if ( null != target )
    {
      // TODO: Iteract with other units (attack, heal, etc.).
    }
    else if ( isMobile() )
    {
      // Navigate the graph along a path.
      gf.guide( this, sq );
    }
  }

  /**
  * Meaning, our game-object can move about the map.
  */
  private boolean _isMobile = true;
  public boolean isMobile()
  {
    return _isMobile;
  }

}
