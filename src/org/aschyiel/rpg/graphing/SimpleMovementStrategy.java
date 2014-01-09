package org.aschyiel.rpg.graphing;

import java.util.List;

import org.aschyiel.rpg.Coordinates;
import org.aschyiel.rpg.Movement;

/**
* The most basic movement strategy, which is, any direction,
* one square at a time.
*
* Does NOT wrap around borders.
* Does NOT go diagonal, just left/right/up/down.
* Does NOT go through blocked paths.
*/
public class SimpleMovementStrategy implements IMovementStrategy
{
  @Override
  public List<Movement> calculatePath(
      Coordinates origin,
      Coordinates destination )
  {
    // TODO Auto-generated method stub
    return null;
  }
}
