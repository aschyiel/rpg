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
  private final List<LevelDetail> lands;
  private final int boardRows;
  private final int boardColumns;

  public Level()
  {
    this( 4, 5 );
  }
  public Level( int rows, int columns )
  {
    units = new ArrayList<LevelDetail>();
    lands = new ArrayList<LevelDetail>();

    // TODO: Don't hardcode.
    boardRows    = rows;
    boardColumns = columns;
  }

  //
  // TODO: Remove these list-getters vs. serialization.
  //

  public List<LevelDetail> getUnits()
  {
    return units;
  }

  public List<LevelDetail> getLands()
  {
    return lands;
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
  public ListIterator<LevelDetail> getLandsIterator()
  {
    return lands.listIterator();
  }
}
