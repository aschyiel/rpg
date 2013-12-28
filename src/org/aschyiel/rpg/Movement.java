package org.aschyiel.rpg;

import java.util.List;

import org.andengine.entity.modifier.PathModifier.Path;
import org.aschyiel.rpg.Coordinates;

/**
* Organizes legal game-object movements.
*/
public class Movement
{
  /**
  * Valid directions we can animate our game-objects.
  */
  public enum Direction
  { GOING_LEFT
  , GOING_RIGHT
  , GOING_UP
  , GOING_DOWN
  }

  /** Where we're supposed to move from. */
  public Coordinates from;

  /** Where we're supposed to move to. */
  public Coordinates to;

  /** The directional representation of our movement. */
  public Direction direction;

  /** @constructor */
  public Movement( Coordinates from, Coordinates to, Direction direction )
  {
    this.from      = from;
    this.to        = to;
    this.direction = direction;
  }

  /**
  * A utility for converting a set of movements
  * into a AndEngine friendly path-set-thingy.
  */
  public static Path asPath( final List<Movement> moves )
  {
    final Path path = new Path( moves.size() );
    for ( Movement move : moves )
    {
      path.to( move.to.getX(), move.to.getY() );
    }
    return path;
  }
}