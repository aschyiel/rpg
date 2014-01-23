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

  /**
  * Returns a set of edges departing from a given vertex.
  */
  public Set<Edge<T>> getEdgesFrom( Vertex<T> from )
  {
    final Set<Vertex<T> edges = new HashSet<Vertex<T>>();
    for ( Edge<T> edge : edges )
    {
      if ( from == edge.from )
      {
        edges.add( edge );
      }
    }
    return edges;
  }

}
