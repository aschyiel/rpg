package org.aschyiel.rpg.graph;

import java.util.List;

import org.aschyiel.rpg.GameObject; 

public class NavPath
{
  protected final GameObject unit;
  protected final List<Step> steps;
  
  /**
   * The current step index.
   */
  private int idx = 0;

  public NavPath( final GameObject unit, final List<Step> steps )
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
    return idx > steps.size();
  }

  protected Step getCurrentStep()
  {
    return steps.get( idx );
  }
}