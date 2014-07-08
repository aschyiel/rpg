package org.aschyiel.rpg;

/**
* An immutable object - records a pixel location (x,y) within the AndEngine space.
*/
public final class Coords
{
  private final float x;
  private final float y;

  public Coords( final int x, final int y )
  {
    this( (float) x, (float) y );
  }
  public Coords( final float x, final float y )
  {
    this.x = x;
    this.y = y;
  }

  public float getX()
  {
    return x;
  }

  public float getY()
  {
    return y;
  }

  @Override
  public String toString()
  {
    return "{ x: "+ x +", y: "+ y +" }";
  }
}
