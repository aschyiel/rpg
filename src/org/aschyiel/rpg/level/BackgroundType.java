package org.aschyiel.rpg.level;

public enum BackgroundType
{
  GRASS( "GRASS" );
  
  private final String name;
  
  BackgroundType( String name )
  {
    this.name = name;
  }

  @Override
  public String toString()
  {
    return name;
  }
}
