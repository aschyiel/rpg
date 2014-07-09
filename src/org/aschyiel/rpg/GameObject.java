package org.aschyiel.rpg;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;
import org.aschyiel.rpg.graph.ChessBoard.Square;
import org.aschyiel.rpg.graph.GirlFriend.NavCallback;
import org.aschyiel.rpg.graph.Navigator;
import org.aschyiel.rpg.level.Player;
import org.aschyiel.rpg.level.UnitType;

/**
* Well, um, everything is a game-object at the end of the day.
*/
public class GameObject extends AnimatedSprite implements IFullGameObject
{
  private final Player owner;
  private final UnitType unitType;
  private final int id;

  /** For Testing purposes... */
  public GameObject( UnitType unitType )
  {
    this( unitType, null, 1337, null, null );
  }

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
  public void applyAction( Square sq, IGameObject them, Navigator gf )
  {
    if ( null != them )
    {
      // TODO: Interact with other units (attack, heal, etc.).
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

  @Override
  public void animate( final Coords from,
                       final Coords to,
                       final NavCallback cb1,
                       final NavCallback cb2 )
  {
    // TODO: Imply direction by changing which animation frames we're gonna use.
    registerEntityModifier( new MoveModifier(
        getMoveSpeedDuration(),
        from.getX(),
        to  .getX(),
        from.getY(),
        to  .getY(),
        new IEntityModifier.IEntityModifierListener()
            {
              @Override
              public void onModifierStarted( IModifier<IEntity> pModifier, IEntity pItem )
              {
                final float dx = to.getX() - from.getX();
                final float dy = to.getY() - from.getY();
                Orientation orianna =   ( dx < 0 )?
                    Orientation.LEFT  : ( dx > 0 )?
                    Orientation.RIGHT : ( dy < 0 )?
                    Orientation.UP    : Orientation.DOWN;
                setOrientation( orianna );
                cb1.callback();
              }
              
              @Override
              public void onModifierFinished( IModifier<IEntity> pModifier, IEntity pItem )
              {
                cb2.callback();
              }
            }
        ));
  }

  /**
  * Returns the animation-duration based on the move-speed for this unit.
  * @return The AndEngine (float) duration in seconds.
  */
  private float getMoveSpeedDuration()
  {
    // TODO: Don't hardcode.
    // TODO: How can we apply move-speed modifiers based on the current land-type? etc.
    //   ie. You should move slower through water.
    return 0.5f;
  }

  /**
  * Change the tile animation to reflect
  * the directional orientation of our game-object.
  */
  public void setOrientation( Orientation orianna )
  {
    int firstTileIndex = -1;
    int lastTileIndex  = -1;
    switch ( orianna )
    {
      // NOTE: Follows convetion implied by player.png.
      //   @see AndEngine PathModifierExample.
      case LEFT:
        firstTileIndex =  9;
        lastTileIndex  = 11;
        break;
      case RIGHT:
        firstTileIndex =  3;
        lastTileIndex  =  5;
        break;
      case UP:
        firstTileIndex =  0;
        lastTileIndex  =  2;
        break;
      case DOWN:
        firstTileIndex =  6;
        lastTileIndex  =  8;
        break;
      default:
        throw new RuntimeException( "Unknown Animation-orientation." );
    }
    final long[] frameDurations = new long[]{ 200, 200, 200 };
    final boolean loop = true;
    animate( frameDurations, firstTileIndex, lastTileIndex, loop );
  }

  public enum Orientation
  {
    LEFT, RIGHT, UP, DOWN
  }

}
