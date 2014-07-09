package org.aschyiel.rpg;

import org.aschyiel.rpg.graph.ChessBoard.Square;
import org.aschyiel.rpg.graph.GirlFriend.NavCallback;
import org.aschyiel.rpg.graph.Navigator;
import org.aschyiel.rpg.level.UnitType;

/**
* This interface is all about the game-play aspect to a game-object.
*/
public interface IGameObject
{
  void setHealth( int hp );
  void setMovementSpeed( int ms );
  void setAttackDamage( int ad );
  UnitType getUnitType();
  Integer getId();
  void setPosition( Coords koords );
  void applyAction( Square sq, IGameObject them, Navigator gf );
  boolean isMobile();
  void animate( Coords from, Coords to, NavCallback cb1, NavCallback cb2 );
}
