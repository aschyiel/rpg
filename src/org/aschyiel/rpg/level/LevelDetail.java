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

  private final LandType landType;

  //
  // Mututally exclusive constructors.
  //

  /**
  * When describing tile land-types.
  */
  public LevelDetail( LandType landType, int row, int column )
  {
    this.landType = landType;
    this.unitType = null;
    this.owner    = null;
    this.row      = row;
    this.column   = column;
  }

  /**
  * When describing a unit (spawn) position.
  */
  public LevelDetail( UnitType unitType, int row, int column, Player owner )
  {
    this.landType = null;
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

  public LandType getLandType()
  {
    return landType;
  }
}
