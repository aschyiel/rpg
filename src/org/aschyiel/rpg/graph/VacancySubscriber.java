package org.aschyiel.rpg.graph;

import org.aschyiel.rpg.graph.ChessBoard.Square;

/**
* A pub-sub thing for empty-squares. 
*/
public interface VacancySubscriber
{ 
  void onVacancy( Square sq ); 
}
