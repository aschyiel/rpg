package org.aschyiel.rpg.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aschyiel.rpg.Coords;
import org.aschyiel.rpg.IGameObject;
import org.aschyiel.rpg.PowerChords;
import org.aschyiel.rpg.graph.ChessBoard.Square;
import org.aschyiel.rpg.level.UnitType;

import android.util.Log;

/**
* Girl-friend always knows where she's going! haha
*/
public class GirlFriend implements Navigator, VacancySubscriber
{
  private final ChessBoard matrix;
  private final PowerChords sona; 

  /**
  * Keeps track of who is going where at any given time.
  */
  private final Map<IGameObject, NavPath> enroute;

  /**
  * Keeps track of who is in the middle of animating between squares.
  */
  private final Set<IGameObject> busyAnimating;

  private final static String TAG = "[RPG:Dr. GirlFriend]";

  public GirlFriend( ChessBoard matrix, PowerChords sona )
  {
    this.matrix = matrix;
    this.sona   = sona;
    enroute = new HashMap<IGameObject, NavPath>();
    busyAnimating = new HashSet<IGameObject>();
    matrix.subscribeToVacancies( this );
  }

  @Override
  public void guide( final IGameObject unit, final Square dst )
  {
    final Square src = matrix.findSquare( unit );
    final List<Step> steps = findPath( src, dst, unit.getUnitType() );
    final NavPath navi;
    if ( isEnroute( unit ) )
    {
      // Allow last-minute re-routing if it's already trying to go somewhere;
      // Indirectly resumes the guide directive (protects against animation canceling).
      navi = enroute.get( unit );
      navi.swapIn( steps );    // GOTCHA: Changes to be picked-up via guide.
      if ( !busyAnimating.contains( unit ) )
      {
        navi.cb.callback();
      }
    }
    else
    {
      // Otherwise kick-start a new animation-path.
      navi = new NavPath( unit, steps );
      enroute.put( unit, navi );
      guide( navi );
    }
  }

  @Override
  public void onVacancy( final Square sq )
  {
    final LinkedList<NavCallback> callbacks = vacancyListeners.get( sq.name ); 
    if ( null == callbacks )
    {
      return;
    }
    final LinkedList<NavCallback> unsatisfied = new LinkedList<NavCallback>(); 
    while ( callbacks.size() > 0 )
    {
      NavCallback cb = callbacks.pop();
      if ( !cb.callback() )
      {
        unsatisfied.push( cb );
      }
    }
    callbacks.addAll( unsatisfied );
  }

  private Map<String, LinkedList<NavCallback>> vacancyListeners = new HashMap<String, LinkedList<NavCallback>>(); 

  private void addVacancyListener( final Square sq, final NavCallback cb )
  {
    if ( null == vacancyListeners.get( sq.name ) )
    {
      vacancyListeners.put( sq.name, new LinkedList<NavCallback>() );
    }
    vacancyListeners.get( sq.name ).add( cb );
  }

  /**
  * Returns true if something is *already* going somewhere.
  * Meaning we'll have to alter it's current route.
  */
  private boolean isEnroute( IGameObject unit )
  {
    return null != enroute.get( unit );
  }

  /**
  * Recursively guide our game-object along it's path;
  * Asynchronous.
  */
  private void guide( final NavPath navi )
  {
    final GirlFriend gf = this;
    if ( navi.isAtDestination() )
    {
      enroute.put( navi.unit, null );
      return;
    }

    final Step step = navi.getCurrentStep();
    final NavCallback cb = navi.cb = new NavCallback()
        {
          @Override
          public boolean callback()
          {
            final boolean isStillBlocked = null != navi.getCurrentStep() &&
              navi.getCurrentStep().to.isOccupado();
            if ( isStillBlocked )
            {
              return false;
            }
            gf.guide( navi );
            return true;
          }
        };

    // Game-objects can get in the way of one another.
    // In which case we politely wait our turn.
    final boolean isCockBlocked = step.to.isOccupado();
    if ( isCockBlocked )
    {
      addVacancyListener( step.to, cb );
    }
    else
    {
      navi.incrementStep();
      gf.matrix.placeUnit( navi.unit, step.to );    //..Instantly double-park.
      animate( navi.unit, step.from, step.to, cb );
    }
  }
  
  private void animate( final IGameObject unit,
                        final Square from,
                        final Square to,
                        final NavCallback cb )
  {
    final GirlFriend gf = this;
    unit.animate(
        sona.asCoords( from.row, from.column ),
        sona.asCoords( to  .row, to  .column ),

        //..before..
        new NavCallback()
            {
              @Override
              public boolean callback()
              {
                // GOTCHA: units instantly "live" in their next-slot while moving.
                //   - Simplifies the same game-object moving back to it's previous square.
                //   - Simplifies chaining unit movement "trains".
                //   - Implies that you can "dodge" effects INSTANTLY by moving.
                gf.matrix.removeUnit( from );
                gf.busyAnimating.add( unit );
                return true;
              }
            },

        //..after..
        new NavCallback()
            {
              @Override
              public boolean callback()
              {
                gf.busyAnimating.remove( unit );
                cb.callback();
                return true;
              }
            });
  }

  private List<Step> findPath( final Square src, final Square dst, final UnitType unitType )
  {
    List<Step> path = new ArrayList<Step>();
    DefaultPathFinder.getInstance().findPath( path, src, dst, unitType );
    return path;
  }
}
