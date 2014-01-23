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
* - http://www.youtube.com/watch?v=rSH3bXexxoM
*/
public class SimpleMovementStrategy<T> implements IMovementStrategy<T>
{
  @Override
  public static <T> List<Movement> calculatePath(
      Vertex<T> origin,
      Vertex<T> destination,
      Graph<T>  graph )
  {
    return dijkstra( origin, destination, graph );
  }

  /**
  * The Dijkstra algorithm for finding the shortest path, as opposed to say A*.
  *
  * Although thorough, what sucks about it is that it
  * solves too much - which might not be such a hot idea on an Android device.
  *
  * @private
  * @see http://en.wikipedia.org/wiki/Dijkstra's_algorithm
  */
  private static <T> List<Movement> dijkstra(
      Vertex<T> origin,
      Vertex<T> destination,
      Graph<T>  graph )
  {
    final Map<Vertex<T>, Integer>   distance = makeDistances( origin, graph.vertices );
    final Map<Vertex<T>, Vertex<T>> parent  = new HashMap<Vertex<T>, Vertex<T>>();

    final Set<Vertex<T>> settled   = new HashSet<Vertex<T>>();
    final Set<Vertex<T>> unsettled = new HashSet<Vertex<T>>( graph.vertices );

    while ( !unsettled.isEmpty() )
    {
      Vertex<T> v = getMinimumDistanceNode( distance );    //..current node..
      unsettled.remove( v );

      if ( INFINITY == distance.get( v ) )
      {
        // "All remaining vertices are inaccessible from source".
        break;
      }

      for ( Edge<T> neighboringEdge : graph.getEdgesFrom( v ) )
      {
        final Vertex<T> w    = neighboringEdge.to;    //..a good neighbor..
        final Integer weight = neighboringEdge.weight;
        final Integer sum    = weight + distance.get( v );
        if ( distance.get( w ) > sum )
        {
          distance.put( w, sum );
          parent.put( w, v );
        }
      }

      
      
    }

  }

  /** Setup the distance map. */
  private static <T> Map<Vertex<T>, Integer> makeDistances( Vertex<T> origin, Set<Vertex<T>> vertices )
  {
    for ( Vertex<T> vertex : vertices )
    {
      distance.put( vertex, INFINITY );
    }
    distance.put( origin, 0 );
    return distance;
  }


  /** An invalid distance value. */
  private static final INFINITY = Integer.MAX_VALUE;

  /**
  * Returns the current minimum distance node within the given map.
  */
  private static Vertex<T> getMinimumDistanceNode( Map<Vertex<T>, Integer> distances )
  {
    // GOTCHA: Assumes the origin starts with a value of zero.
    int d = INFINITY;
    Vertex<T> node = null;
    for ( Vertex<T> key : distances.keySet() )
    {
      int val = distances.get( key );
      if ( val < d ) {
        d    = val;
        node = key;
      }
    }
    return node;
  }

}
