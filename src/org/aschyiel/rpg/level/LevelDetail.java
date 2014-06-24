package org.aschyiel.rpg.level;

/**
* Describes minute details within a level --- immutable.
*/
public final class LevelDetail
{
  private final UnitType unitType;
  private final Player owner;
  
  /**
  * The tile/square position within the board/terrain.
  */
  private final int row;
  private final int column;

  public LevelDetail( UnitType unitType, int row, int column, Player owner )
  {
    this.unitType = unitType;
    this.row      = row;
    this.column   = column;
    this.owner    = owner;
  }

  public int getRow()
  {
    return row;
  }

  public int getColumn()
  {
    return column;
  }

  public UnitType getUnitType()
  {
    return unitType;
  }
  
  public Player getOwner()
  {
	return owner;
  }
}
