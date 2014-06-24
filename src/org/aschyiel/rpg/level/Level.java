package org.aschyiel.rpg.level;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
* Describes a single level within the game;
* To be serialized.
*/
public class Level
{

  private final List<LevelDetail> units;

  public Level()
  {
    units = new ArrayList<LevelDetail>();
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
}
