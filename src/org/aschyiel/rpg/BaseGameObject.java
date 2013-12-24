package org.aschyiel.rpg;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.Sprite;
import org.aschyiel.rpg.IGameObject;

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

  //
  // Public Methods required by the IGameObject interface.
  //

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

  public void move( Coordinates destination )
  {
    // Set a course to move to our destination.
    // Periodically update our coordinates as we go.
    // Stop moving if we get blocked for some reason.

    // ask for calculated path of queued tile movements.
    // clear the previous moveQueue if any.
    // update our UI to show that we're moving.
    // - hide previous navigation points if any.
    //   game.hideNav();
    // - show navigation path with nav. points.
    //   game.showNav( moveQueue );

  }

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
    // TODO Auto-generated method stub
    
  }

  @Override
  public void focus()
  {
    // TODO Auto-generated method stub
    
  }

  //--------------------------------
  //
  // Private Methods
  //
  //--------------------------------
}
