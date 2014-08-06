package org.aschyiel.rpg.level;

// TODO Mountains, Forest, Flats.
public enum LandType
{
  PLAINS(  "PLAINS" )    // Normal
  , DESERT(  "DESERT" )  // Faster
  ;

  private final String name;
  
  LandType( String name )
  {
    this.name = name;
  }

  @Override
  public String toString()
  {
    return name;
  }
}
