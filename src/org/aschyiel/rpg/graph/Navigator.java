package org.aschyiel.rpg.graph;

import org.aschyiel.rpg.IGameObject;
import org.aschyiel.rpg.graph.ChessBoard.Square;

/**
* The google-maps for our game.  Promises to take care of
* the game-object as it moves about the world. 
* 
* Prompting it to show the approrpiate animation-frames, etc.
*/
public interface Navigator
{ 
  /**
  * Guide the given unit from it's current position to the target destination.
  */
  void guide( IGameObject unit, Square destination );

  // QQ javaScript Closures!
  public interface NavCallback
  {
    /**
    * An anonymous closure - returns true meaning conditions have been satisfied.
    */
    boolean callback();
  }
}
