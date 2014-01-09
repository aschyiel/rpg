package org.aschyiel.rpg.graphing;

import java.util.List;

import org.aschyiel.rpg.Coordinates;
import org.aschyiel.rpg.Movement;

/**
* The most basic movement strategy, which is, any direction,
* one square at a time.
*
* Edges are considered to be undirected.
* The edge cost is considered to be equal, everywhere (again, we're a ghost).
*
* Does NOT wrap around borders.
* Does NOT go diagonal, just left/right/up/down.
* Does go through blocked paths - like a ghost, etc.
*
* References:
* - http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
* - Chapter 6, Skiena.
* - Chapter 15.4, Shortest Path, Skiena.
*/
public class NaiveMovementStrategy implements IMovementStrategy
{
  @Override
  public List<Movement> calculatePath(
      Vertex origin,
      Vertex destination,
      Graph  graph )
  {
    // TODO Auto-generated method stub
    return null;
  }
}
