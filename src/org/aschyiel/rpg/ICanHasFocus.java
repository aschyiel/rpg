package org.aschyiel.rpg;

public interface ICanHasFocus
{ 
  // GOTCHA: Needed for EasyMock vs. IGameObject.
  void setFocus( Focus focus );
}
