package org.aschyiel.rpg.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
  
  private final static String TAG = "[RPG:Dr. GirlFriend]";

  public GirlFriend( ChessBoard matrix, PowerChords sona )
  {
    this.matrix = matrix;
    this.sona   = sona;
    matrix.subscribeToVacancies( this );
  }

  @Override
  public void guide( IGameObject unit, Square dst )
  {
    Square src = matrix.findSquare( unit );
    List<Step> steps = findPath( src, dst, unit.getUnitType() );
    Step.print( steps );
    guide( new NavPath( unit, steps ) );
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
  * Recursively guide our game-object along it's path;
  * Asynchronous.
  */
  private void guide( final NavPath navi )
  {
    Log.v( TAG, "gf#guide -- step:" + navi.getCurrentStep() );
    final GirlFriend gf = this;
    if ( navi.isAtDestination() )
    {
      return;
    }

    final Step step = navi.getCurrentStep();
    final NavCallback cb = new NavCallback()
        {
          @Override
          public void callback()
          {
            gf.guide( navi );
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

  public interface NavCallback
  {
    void callback();
  }

}
