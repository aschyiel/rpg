package org.aschyiel.rpg.graph;

import java.util.List;

import org.aschyiel.rpg.IGameObject; 
import org.aschyiel.rpg.graph.Navigator.NavCallback;

/**
* A simple way of keeping track of both the game-object,
* and the current step in the navigation steps.
*/
public class NavPath
{
  protected final IGameObject unit;

  protected List<Step> steps;

  /**
   * The current step index.
   */
  private int idx = 0;

  protected NavCallback cb;

  public NavPath( final IGameObject unit, final List<Step> steps )
  {
    this.unit  = unit;
    this.steps = steps;
  }
  
  protected void incrementStep()
  {
    idx++;
  }

  /**
  * Returns true if we're done with all of our steps.
  */
  protected boolean isAtDestination()
  {
    return idx > steps.size() - 1;
  }

  protected Step getCurrentStep()
  {
    return ( idx < steps.size() )?
        steps.get( idx ) : null;
  }

  /**
  * A mechanic for re-routing;
  * Essentially tacks-on new steps to our existing steps.
  */
  protected void swapIn( final List<Step> detour )
  {
    idx = 0;
    steps = detour;
  }
}