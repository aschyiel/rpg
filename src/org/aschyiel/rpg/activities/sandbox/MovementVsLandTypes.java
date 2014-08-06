package org.aschyiel.rpg.activities.sandbox;

import org.aschyiel.rpg.activities.Terrain;
import org.aschyiel.rpg.level.*;

/**
* Demonstrates land-types affecting unit movement-speed.
*/
public class MovementVsLandTypes extends Terrain
{
  @Override
  protected Level getLevelInfo()
  {
    int rows = 8;
    int cols = 8;
    Level lvl = new Level( rows, cols );

    //
    // Add a unit at the top of each column.
    //

    for ( int n = 0; n < cols; n++ )
    {
      lvl.getUnits().add( new LevelDetail( UnitType.TANK, 0, n, Player.ONE ) );
    }

    //
    // Each column gets a different land-type.
    //

    for ( int m = 0; m < rows; m++ )
    {
      lvl.getLands().add( new LevelDetail( LandType.DESERT, m, 3 ) );
    }

    return lvl;
  }
}
