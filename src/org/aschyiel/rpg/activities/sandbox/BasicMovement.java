package org.aschyiel.rpg.activities.sandbox;

import org.aschyiel.rpg.activities.Terrain;
import org.aschyiel.rpg.level.Level;
import org.aschyiel.rpg.level.LevelDetail;
import org.aschyiel.rpg.level.Player;
import org.aschyiel.rpg.level.UnitType;

public class BasicMovement extends Terrain
{

  @Override
  protected Level getLevelInfo()
  {
    Level lvl = new Level();
    lvl.getUnits().add( new LevelDetail( UnitType.TANK, 0, 0, Player.ONE ) );
    lvl.getUnits().add( new LevelDetail( UnitType.TANK, 2, 3, Player.CPU ) );
    return lvl;
  }

}
