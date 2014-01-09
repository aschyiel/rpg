package org.aschyiel.rpg.graphing;

/**
* A graph is a network of abstract in-game locations via edges and vertices.
*
* @see http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
*/
public class Graph<T>
{
  protected final Set<Edge <T>>   edges;
  protected final Set<Vertex <T>> vertices;

  public Graph( Set<Vertex <T>> vertices, Set<Edge <T>> edges )
  {
    this.edges    = edges;
    this.vertices = vertices;
  }
}
