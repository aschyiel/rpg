package org.aschyiel.rpg.level;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
* Describes a single level within the game;
* To be serialized.
* Supposed to be read-only to the outside world.
*/
public class Level
{

  private final List<LevelDetail> units;
  private final int boardRows;
  private final int boardColumns;

  public Level()
  {
    units = new ArrayList<LevelDetail>();

    // TODO: Don't hardcode.
    boardRows    = 4;
    boardColumns = 5;
  }

  // TODO: This needs to be removed, after I write the serialization part.
  public List<LevelDetail> getUnits()
  {
    return units;
  }

  public ListIterator<LevelDetail> getUnitsIterator()
  {
    return units.listIterator();
  }

  public int getBoardRows()
  {
    return boardRows;
  }

  public int getBoardColumns()
  {
    return boardColumns;
  }
}
