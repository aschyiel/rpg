package org.aschyiel.rpg;

import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.util.modifier.IModifier;

import org.aschyiel.rpg.IGameObject;
import org.aschyiel.rpg.Movement;
import org.aschyiel.rpg.Movement.Direction;
import org.aschyiel.rpg.activities.Terrain;

/**
* The base game object class.
*/
public class BaseGameObject
    implements IGameObject
{
  //
  // Private variables.
  //

  private Sprite _sprite;
  private Coordinates _coords;
  private Terrain _terrain;

  //
  // Public Methods required by the IGameObject interface.
  //

  /** A way of passing forward our parent activity. */
  public void setTerrain( Terrain terrain )
  {
    _terrain = terrain;
  }

  /** Instantly update a game-object's position - for spawn-time use only. */
  public void setCoords( Coordinates coords )
  {
    if ( null != _sprite )
    {
      _sprite.setPosition( coords.getX(), coords.getY() );
    }
    _coords = coords;
  }

  public Coordinates getCoords()
  {
    return _coords;
  }

  /**
  * The movement queue for this game-object.
  */
  private List<Movement> _moves;    // TODO: Provide a size.

  public void move( Coordinates destination )
  {
    // Clear the previous moveQueue if any.
    cancelPreviousMoves();

    // Ask for calculated path of queued tile movements.
    _moves = _terrain.calculatePath( _coords, destination );

    // Update our UI to show that we're moving.
    rollOut( _moves );
    focus();
  }

  /**
  * Set a course to move to our destination.
  * Periodically update our coordinates as we go.
  * Stop moving if we get blocked for some reason.
  */
  private void cancelPreviousMoves()
  {
    if ( null != _moveHandler )
    { 
      _sprite.unregisterEntityModifier( _moveHandler );
    }
  }

  private PathModifier _moveHandler;
  
  /**
  * Setup our AndEngine sprite movement queue/callbacks.
  */
  private void rollOut( final List<Movement> moves )
  {
    // Closure variables.
    final BaseGameObject gameObject = this;
    final Sprite         sprite     = getSprite();

    final float speed = DEFAULT_SPEED;

    _moveHandler = new PathModifier( speed, Movement.asPath( moves ),
        new IEntityModifierListener()
        {
          @Override
          public void onModifierFinished( IModifier<IEntity> modifier, IEntity item )
          {
            // TODO: Clear movement queue.
          }

          @Override
          public void onModifierStarted( IModifier<IEntity> modifier, IEntity item )
          {
            //..
          } 
        }
      , new IPathModifierListener()
        {
          @Override
          public void onPathStarted( PathModifier modifier, IEntity item )
          {
            // TODO Auto-generated method stub
            
          }

          @Override
          public void onPathWaypointStarted( PathModifier modifier,
                                             IEntity item,
                                             int waypointIndex )
          {
            // TODO Auto-generated method stub
            final Movement move = moves.get( waypointIndex );
            switch ( move.direction )
            {
              // TODO: Handle projectiles which may go diagonal.
              case GOING_LEFT:
                gameObject.animateMovingLeft();
                break;
              case GOING_RIGHT:
                gameObject.animateMovingRight();
                break;
              case GOING_UP:
                gameObject.animateMovingUp();
                break;
              case GOING_DOWN:
                gameObject.animateMovingDown();
                break;
              default:
                throw new Error( "Unsupported movement direction!" );
            }
          }

          @Override
          public void onPathWaypointFinished( PathModifier modifier,
                                              IEntity item,
                                              int waypointIndex )
          {
            // TODO Auto-generated method stub
            
          }

          @Override
          public void onPathFinished( PathModifier modifier, IEntity item )
          {
            // TODO Auto-generated method stub
            
          }
        });
    sprite.registerEntityModifier( _moveHandler );
  }

  //
  // Various sprite animation methods.
  //

  public void animateMovingLeft()
  {
    //..
  }

  public void animateMovingRight()
  {
    //..
  }

  public void animateMovingUp()
  {
    //..
  }

  public void animateMovingDown()
  {
    //..
  }

  /**
  * The default movement speed or duration.
  */
  public static final float DEFAULT_SPEED = 200;

  public Sprite getSprite()
  {
    return _sprite;
  }
  public void setSprite( Sprite sprite )
  {
    _sprite = sprite;
  }

  /**
  * Returns true if this unit belongs to our player.
  */
  public boolean isBelongToUs()
  {
    return true;    // TODO
  }

  /**
  * Tell this unit to attack another unit;
  * This may imply that our unit moves to the other unit first,
  * before attacking it based on it's available range, etc.
  */
  public void attack( IGameObject targetUnit )
  {
    // TODO

    // Move until we're in range.
    // Animate our attack.
    // Attempt to deal damage to the target.
  }

  @Override
  public void unfocus()
  {
    _terrain.hideNavigation();
  }

  @Override
  public void focus()
  {
    _terrain.showNavigation( _moves );
  }

  //--------------------------------
  //
  // Private Methods
  //
  //--------------------------------
}
