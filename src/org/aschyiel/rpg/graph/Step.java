package org.aschyiel.rpg.graph;

import org.aschyiel.rpg.graph.ChessBoard.Square;

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
}
