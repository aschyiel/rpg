package org.aschyiel.rpg.graph;

import java.util.List;

import org.aschyiel.rpg.graph.ChessBoard.Square;
import org.aschyiel.rpg.level.UnitType;

public interface PathFinder
{ 
  void findPath( final List<Step> path, final Square src, final Square dst, final UnitType unitType );
}
