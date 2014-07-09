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
  * Keeps track of who is idle while en-route.
  */
  private final Set<IGameObject> blocked;

  private final static String TAG = "[RPG:Dr. GirlFriend]";

  public GirlFriend( ChessBoard matrix, PowerChords sona )
  {
    this.matrix = matrix;
    this.sona   = sona;
    enroute = new HashMap<IGameObject, NavPath>();
    blocked = new HashSet<IGameObject>();
    matrix.subscribeToVacancies( this );
  }

  @Override
  public void guide( final IGameObject unit, final Square dst )
  {
    final Square src = matrix.findSquare( unit );
    final List<Step> steps = findPath( src, dst, unit.getUnitType() );
    final NavPath navi;
    if ( isBusy( unit ) )
    {
      // Allow last-minute re-routing if it's already trying to go somewhere.
      navi = enroute.get( unit );
      navi.swapIn( steps );    // GOTCHA: Changes to be picked-up via guide.
      if ( isBlocked( unit ) )
      {
        navi.cb.callback();    // Indirectly resume the guide directive.
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
    LinkedList<NavCallback> callbacks = vacancyListeners.get( sq.name ); 
    while ( null != callbacks && callbacks.size() > 0 )
    {
      callbacks.pop().callback();
    }
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
  * Returns true if something is currently busy going somewhere.
  * Meaning we'll have to alter it's current route.
  */
  private boolean isBusy( IGameObject unit )
  {
    return null != enroute.get( unit );
  }

  /**
  * Returns true when a game-object is idle
  * and waiting for it's next square to be vacant so that
  * it can resume it's animation.
  */
  private boolean isBlocked( IGameObject unit )
  {
    return blocked.contains( unit );
  }

  /**
  * Recursively guide our game-object along it's path;
  * Asynchronous.
  */
  private void guide( final NavPath navi )
  {
    Log.v( TAG, "gf#guide -- step:" + navi.getCurrentStep() );
    final GirlFriend gf = this;
    if ( navi.isAtDestination() )
    {
      enroute.put( navi.unit, null );
      return;
    }

    final Step step = navi.getCurrentStep();
    final NavCallback cb = navi.cb = new NavCallback()
        {
          // GOTCHA: Written as a single-use callback
          //   to protect against animation-canceling.
          @Override
          public void callback()
          {
            if ( !spent )
            {
              if ( gf.blocked.contains( navi.unit ) )
              {
                gf.blocked.remove( navi.unit );
              }
              gf.guide( navi );
            }
            spent = true;
          }
          private boolean spent = false;
        };

    // Game-objects can get in the way of one another.
    // In which case we politely wait our turn.
    final boolean isCockBlocked = step.to.isOccupado();
    if ( isCockBlocked )
    {
      addVacancyListener( step.to, cb );
      blocked.add( navi.unit );
    }
    else
    {
      navi.incrementStep();
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
              public void callback()
              {
                // GOTCHA: units live in two places at once while moving.
                gf.matrix.placeUnit( unit, to );
              }
            },

        //..after..
        new NavCallback()
            {
              @Override
              public void callback()
              {
                gf.matrix.removeUnit( from );
                cb.callback();
              }
            });
  }

  private List<Step> findPath( final Square src, final Square dst, final UnitType unitType )
  {
    List<Step> path = new ArrayList<Step>();
    DefaultPathFinder.getInstance().findPath( path, src, dst, unitType );
    return path;
  }

  private void debug( NavPath navi )
  {
    Log.d( TAG, "nav-path:" + navi +", unit: "+ navi.unit );
    for ( Step it : navi.steps )
    {
      Log.d( TAG, "sq: "+ it.to +", occupant status:"+ it.to.isOccupado() );
    }
  }
}
