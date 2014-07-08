package org.aschyiel.rpg.graph;

import org.aschyiel.rpg.IFullGameObject;
import org.aschyiel.rpg.graph.ChessBoard.Square;

public interface OnSquareClickHandler
{
  void onSquareClicked( Square sq, IFullGameObject target );
}
