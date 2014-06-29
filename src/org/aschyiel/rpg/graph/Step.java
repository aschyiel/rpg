package org.aschyiel.rpg.graph;

import java.util.List;

import org.aschyiel.rpg.graph.ChessBoard.Square;

import android.util.Log;

/**
* A record of neighboring squares as an edge. 
*/
public final class Step
{ 
  protected final Square to;
  protected final Square from; 
  
  public Step( final Square a, final Square b )
  {
    from = a;
    to   = b;
  }

  /**
  * For debugging paths.
  */
  protected static void print( List<Step> li )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "Path as steps: \n" );

    int i = 1;
    for ( Step step : li )
    {
      sb.append( i );
      sb.append( ". Move from " );
      sb.append( step.from );
      sb.append( " to " );
      sb.append( step.to );
      sb.append( ".\n" );
      i++;
    }

    Log.d( "[RPG:Step]", sb.toString() );
  }
}
