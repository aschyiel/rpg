package org.aschyiel.rpg.graphing;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
* A graph is a network of abstract in-game locations via edges and vertices.
*
* @see http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
*/
public class Graph<T>
{
  protected final Set<Edge <T>>   edges;
  protected final Set<Vertex <T>> vertices;

  protected final Map<T, Vertex<T>> verticesByValue =
      new HashMap<T, Vertex<T>>();

  public Graph( Set<Vertex <T>> vertices, Set<Edge <T>> edges )
  {
    this.edges    = edges;
    this.vertices = vertices;

    setupVerticesForLookup();
  }

  /**
  * Organize our vertices, so we can lookup stuff later.
  */
  private void setupVerticesForLookup()
  {
    for ( Vertex<T> vertex : vertices )
    {
      verticesByValue.put( vertex.getValue(), vertex );
    }
  }

  /**
  * Fetch a vertex wrapping the given reference.
  */
  public Vertex<T> getVertex( T it )
  {
    return verticesByValue.get( it );
  }
}
