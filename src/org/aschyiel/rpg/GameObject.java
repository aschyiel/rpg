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
  private final int id;

  private Sprite sprite;

  public GameObject( UnitType unitType, Player owner, int id )
  {
    this.unitType = unitType;
    this.owner    = owner;
    this.id       = id;
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

  public void setSprite( Sprite sprite )
  {
    this.sprite = sprite;
  }

  public Object getUnitType()
  {
    return unitType;
  }

  public Integer getId()
  {
    return id;
  }

  /**
  * Immediately update the position of a unit --- skips movement animation.
  * Used when loading level, spawning, etc.
  */
  public void setPosition( Coords koords )
  {
    sprite.setPosition( koords.getX(), koords.getY() );
  }


}
