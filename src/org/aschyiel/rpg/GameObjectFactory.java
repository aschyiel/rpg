package org.aschyiel.rpg;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.aschyiel.rpg.level.Player;
import org.aschyiel.rpg.level.UnitType;

/**
* The thing that creates a bunch of stuff,
* over and over like a manufacturing plant.
*/
public class GameObjectFactory
{
  private final Resorcerer rez;
  private final VertexBufferObjectManager buffy;

  private int nextId = 1;

  public GameObjectFactory( Resorcerer rez )
  {
    this.rez = rez;
    this.buffy = rez.getVertexBufferObjectManager();
  }

  /**
  * Make a new game-object of a given type, and label it under the given owner.
  */
  public GameObject makeUnit( UnitType unitType, Player owner )
  {
    GameObject unit = new GameObject( unitType, owner, nextId++ );
    switch( unitType )
    {
      case TANK:
        asTank( unit );
        break;
      default:
        throw new RuntimeException( "Invalid unit type." );
    }
    unit.getSprite().setSize( targetWidth, targetHeight );
    return unit;
  }

  private void asTank( GameObject it )
  {
    it.setHealth(         100 );
    it.setMovementSpeed(  100 );
    it.setAttackDamage(   100 );
    it.setSprite( new Sprite( 0, 0, rez.getUnitTexture( it ), buffy ) );
  }

  /**
  * Specify the end-goal sprite size so that it fills it's square.
  */
  public void setTargetSize( int w, int h )
  {
    targetWidth  = (float) w;
    targetHeight = (float) h;
  }
  private float targetWidth;
  private float targetHeight;

}
