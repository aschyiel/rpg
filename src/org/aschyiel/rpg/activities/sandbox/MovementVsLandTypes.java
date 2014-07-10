package org.aschyiel.rpg.activities.sandbox;

import org.aschyiel.rpg.activities.Terrain;
import org.aschyiel.rpg.level.*;

public class MovementVsLandTypes extends Terrain
{
  @Override
  protected Level getLevelInfo()
  {
    Level lvl = new Level( 8, 8 );
    lvl.getUnits().add( new LevelDetail( UnitType.TANK, 0, 0, Player.ONE ) );
    lvl.getUnits().add( new LevelDetail( UnitType.TANK, 1, 0, Player.ONE ) );
    lvl.getUnits().add( new LevelDetail( UnitType.TANK, 2, 0, Player.ONE ) );
    lvl.getUnits().add( new LevelDetail( UnitType.TANK, 3, 0, Player.ONE ) );
    lvl.getUnits().add( new LevelDetail( UnitType.TANK, 4, 0, Player.ONE ) );
    lvl.getUnits().add( new LevelDetail( UnitType.TANK, 5, 0, Player.ONE ) );
    lvl.getUnits().add( new LevelDetail( UnitType.TANK, 6, 0, Player.ONE ) );
    lvl.getUnits().add( new LevelDetail( UnitType.TANK, 7, 0, Player.ONE ) );
    return lvl;
  }
}
