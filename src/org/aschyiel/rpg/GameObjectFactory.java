package org.aschyiel.rpg;

/**
* The game-object manufacturing plant.
*/
public class GameObjectFactory
{
  public GameObjectFactory( Resorcerer resorcerer )
  {
    this.resorcerer = resorcerer;
  }

  private Resorcerer resorcerer;

  public IGameObject make( GameObjectType type )
  {
    return null;
  }
}