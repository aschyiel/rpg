package org.aschyiel.rpg.graphing;

/**
* An edge connects two verticies within a graph.
*
* Edges within our game, are directed, and weighted.
* For undirected edges, simply create two edges
* from both "sides" of the vertices.
*/
public class Edge<T>
{
  /**
  * The default edge weight.
  */
  protected static final int DEFAULT_WEIGHT = 10;

  public Edge( Vertex<T> from, Vertex<T> to )
  {
    this( from, to, DEFAULT_WEIGHT );
  }

  public Edge( Vertex<T> from, Vertex<T> to, int weight )
  {
    this.from   = from;
    this.to     = to;
    this.weight = weight;
  }

  //
  // We're all adults here...
  //

  protected final Vertex<T> from;
  protected final Vertex<T> to;
  protected final int weight;
}
