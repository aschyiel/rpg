package org.aschyiel.rpg;

import org.andengine.entity.sprite.Sprite;
import org.aschyiel.rpg.level.Player;
import org.aschyiel.rpg.level.UnitType;

/**
* Well, um, everything is a game-object at the end of the day.
*/
public class GameObject
{
  private final Player owner;
  private final UnitType unitType;

  public GameObject( UnitType unitType, Player owner )
  {
    this.unitType = unitType;
    this.owner    = owner;
  }

  public void setHealth(int i)
  {
    // TODO Auto-generated method stub
    
  }

  public void setMovementSpeed(int i)
  {
    // TODO Auto-generated method stub
    
  }

  public void setAttackDamage(int i)
  {
    // TODO Auto-generated method stub
    
  }

  public void setSprite(Sprite sprite)
  {
    // TODO Auto-generated method stub
    
  }

  public Object getUnitType()
  {
    return unitType;
  }

  public Integer getId()
  {
    // TODO Auto-generated method stub
    return null;
  }

  
}
