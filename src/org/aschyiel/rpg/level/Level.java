package org.aschyiel.rpg.level;

import java.util.ArrayList;
import java.util.List;

/**
* Describes a single level within the game;
* To be serialized.
*/
public class Level
{

  // TODO: This needs to be marked as private, after I write the serialization part.
  public final List<LevelDetail> units;

  public Level()
  {
    units = new ArrayList<LevelDetail>();
  }
}
