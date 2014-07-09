package org.aschyiel.rpg.graph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.aschyiel.rpg.graph.ChessBoard.Square;
import org.aschyiel.rpg.level.UnitType;

import android.util.Log;

/**
* Look for an un-blocked path from point A to point B.
*
* @see http://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
*/
public class DefaultPathFinder implements PathFinder
{
  private static final DefaultPathFinder INSTANCE = new DefaultPathFinder();
  
  private DefaultPathFinder()
  {
    //..
  }

  public static DefaultPathFinder getInstance()
  {
    return INSTANCE;
  }
 
  private static final String TAG = "[RPG:DefaultPathFinder]";

  @Override
  public void findPath( final List<Step> path, final Square src, final Square dst, final UnitType unitType )
  {
    final Set<String> visited = new HashSet<String>();
    visited.add( src.name );

    final PriorityQueue<Square> q = new PriorityQueue<Square>( anticipateWorst( src, dst ), getComparator( dst ) );
    q.add( src );

    // Keep track of the best distances per vertex (so far).
    final Map<String, Distance> xi = new HashMap<String, Distance>();
    xi.put( src.name, new Distance( null, 0 ) );

    //
    // Explore via BFS.
    //

    while ( !q.isEmpty() )
    {
      Square sq = q.poll();
      if ( sq == dst )
      {
        break;
      }

      // Checkout neighboring edges (if any).
      _findPath( xi, q, visited, sq, sq.up  ,  unitType );
      _findPath( xi, q, visited, sq, sq.down,  unitType );
      _findPath( xi, q, visited, sq, sq.left,  unitType );
      _findPath( xi, q, visited, sq, sq.right, unitType );
    }

    //
    // Consolidate results.
    //

    // If we're not blocked-off.
    if ( null != xi.get( dst.name ) )
    {
      // Put it all together in reverse (so that it's forwards again).
      // ie. we're given D -> C -> B -> A.
      LinkedList<Square> stack = new LinkedList<Square>();
      Square it = dst;
      while ( null != it )
      {
        stack.push( it );
        it = xi.get( it.name ).via;
      }

      // Record pairs as discrete steps.
      // ie. [A, B] -> [B, C] -> [C, D].
      it = stack.pop();
      while ( null != it && null != stack.peek() )
      {
        path.add( new Step( it, stack.peek() ) );
        it = stack.pop();
      }
    }
  }

  private void _findPath( final Map<String, Distance> xi,
                          final PriorityQueue<Square> q,
                          final Set<String> visited,
                          final Square from,
                          final Square neighbor,
                          final UnitType unitType )
  {
    // Skip places we've already been.
    if ( null == neighbor || visited.contains( neighbor.name ) )
    {
      return;
    }

    visited.add( neighbor.name );

    // Ignore in-accessible places.
    if ( neighbor.isInaccessible( unitType ) )
    {
      return;
    }

    Distance curr = xi.get( neighbor.name );
    Distance here = xi.get( from.name     );
    int cost = getEdgeCost( from, neighbor ) + here.cost;
    if ( null == curr || curr.cost > cost )
    {
      xi.put( neighbor.name, new Distance( from, cost ) );
    }
    q.add( neighbor );
  }

  /**
  * Returns the edge-cost when traveling between adjacent vertices.
  *
  * This will be at a minimum of 1.
  * However, for different land-types, etc. the cost will increase.
  *
  * For example, traveling up-hill should be deemed more expensive
  * than traveling down-hill, etc.
  */
  private static int getEdgeCost( Square a, Square b )
  {
    // TODO: Don't hard-code.
    return 1;
  }
 
  /**
  * Makes a comparator that prioritizes squares (vertices)
  * based off of the shortest-line distance to the end-goal.
  */
  private static Comparator<Square> getComparator( final Square dst )
  {
    return new Comparator<Square>()
        {
          @Override
          public int compare( final Square a, final Square b )
          {
            // Calculate a rough delta-distance for each against the final destination.
            final int da = Math.abs( a.row - dst.row ) + Math.abs( a.column - dst.column );
            final int db = Math.abs( b.row - dst.row ) + Math.abs( b.column - dst.column );
            return ( da == db )?  0 :
                   ( da  < db )? -1 : +1;
          }
        };
  }

  /**
  * Returns how many vertices we plan on visiting/queueing-up at worst-case.
  */
  private static int anticipateWorst( final Square src, final Square dst )
  {
    int dx = Math.max( 1, Math.abs( src.row    - dst.row    ) );
    int dy = Math.max( 1, Math.abs( src.column - dst.column ) );
    return  dx * dy;
  }

  /**
  * Keeps record of a distance/cost to an individual vertex from the origin.
  */
  protected class Distance
  {
    private final Square via;
    private final int cost;
    public Distance( final Square sq, final int cost )
    {
      this.via  = sq;
      this.cost = cost;
    }

    @Override
    public String toString()
    {
      return "{ cost:"+ cost +", via:"+ via.name  +" }";
    }
  }

}
