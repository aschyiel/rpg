package org.aschyiel.rpg.graphing;

import java.util.List;

import org.aschyiel.rpg.Coordinates;
import org.aschyiel.rpg.Movement;

/**
* Movement strategies provide different ways of calculating
* an in-game path from point A to point B. 
*/
public interface IMovementStrategy<T>
{
  /**
  * Calculate the movement path from point A to point B;
  * aka a graphing problem.
  */
  public static <T> List<Movement> calculatePath(
      Vertex<T> origin,
      Vertex<T> destination,
      Graph<T>  graph );
}
