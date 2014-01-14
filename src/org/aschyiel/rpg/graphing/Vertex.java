package org.aschyiel.rpg.graphing;

/**
* A single vertex represents a single abstract location in the game
* where things can go.
*
* As a collection, they may form edges within a graph.
*
* References:
* http://java.dzone.com/articles/introduction-generics-java-%E2%80%93-0
*/
public class Vertex<T>
{
  public Vertex( T place )
  {
    this.place = place;
  }

  /**
  * The "physical" location that is being represented by
  * this "abstract" location. Usually an in-game terrain-tile.
  */
  protected final T place;

  /**
  * Returns the actual "physical" point of reference that
  * we're supposed to represent.
  * Again, usually this is going to be a terrain-tile.
  */
  public T getActualLocation()
  {
    return place;
  }

  /**
  * Semantics.
  */
  public T getValue()
  {
    return getActualLocation();
  }
}
