package org.aschyiel.rpg;

import org.andengine.entity.sprite.Sprite;

/**
* The game-object interface.
* A game-object organizes it's sprite animations, positional information
* such as it's current tile, etc.
*/
public interface IGameObject
{
  /**
  * The game-coordinates for an game-object
  * represents where it lives within the view.
  *
  * ie. Figure out the current tile for a game-object based
  *     on it's current coordinates.
  */
  public void getCoords( Coordinates coords ); 
  public void setCoords( Coordinates coords ); 

  /**
  * Tell the game-object to move itself to a new destination;
  * Or try to move as close as it can to the specified area, accordingly.
  * 
  * ie.  Animated game-objects will update their correct display as they go.
  * ie2. Tanks will follow the best tile-path within the terrain view (such as
  *      avoiding road-blocks).
  */
  public void move( Coordinates destination ); 

  /**
  * Set the GUI representation for this thing. 
  */
  public void setSprite( Sprite sprite );

  /**
  * Returns true if this unit belongs to our player.
  */
  public boolean isBelongToUs();

  /**
  * Tell this unit to attack another unit;
  * This may imply that our unit moves to the other unit first,
  * before attacking it based on it's available range, etc.
  */
  public void attack( IGameObject targetUnit );
}
