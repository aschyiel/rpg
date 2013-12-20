package org.aschyiel.rpg;

/**
 * Coordinates organize positional information relative to a view. 
 * 
 * You'd use coordinates to calculate a given tile, and back again, etc.
 */
public class Coordinates
{ 
  //-----------------------------------
  //
  // Private Variables
  //
  //-----------------------------------

  /**
   * Where we are horizontally, in AndEngine scene-x coordinates.
   */
  private float x;

  /**
   * Where we are vertically, in AndEngine scene-y coordinates.
   */
  private float y;
 
  //-----------------------------------
  //
  // Constructor(s)
  // 
  //-----------------------------------

  /**
   * Simultaneously create and set the positional information for this thing.
   * That is now where it exists.
   */
  public Coordinates( final float x, final float y )
  {
    this.x = x;
    this.y = y;
  }
  
  //-----------------------------------
  //
  // Public Methods.
  //
  //-----------------------------------

  /** Returns the read-only x-coordinate property. */
  public float getX()
  {
    return x;
  }

  /** Returns the read-only y-coordinate property. */
  public float getY()
  {
    return y;
  }

  @Override
  public String toString()
  {
    return "[ x:"+ x +", y:"+ y +" ]";
  }
}
