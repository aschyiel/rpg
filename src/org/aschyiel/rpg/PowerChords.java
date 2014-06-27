package org.aschyiel.rpg;

/**
* Basically a simple contract for translating the abstract chess-board space
* into AndEngine x,y-coordinates. 
*/
public interface PowerChords
{ 
  /**
  * Convert zero-indexed matrix space into the world of floats.
  * 
  * @param m The row.
  * @param n The column.
  */
  Coords asCoords( int m, int n ); 
}
