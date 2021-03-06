package org.aschyiel.rpg.level;

/**
* Describes the valid unit-types that can be placed within a level.
*/
public enum UnitType
{
  TANK( "TANK" );

  private final String name;

  UnitType( String name )
  {
    this.name = name;
  }

  @Override
  public String toString()
  {
    return name;
  }
}
