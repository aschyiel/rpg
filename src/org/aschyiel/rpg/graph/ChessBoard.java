package org.aschyiel.rpg.graph;

import java.util.HashMap;
import java.util.Map;

import org.aschyiel.rpg.GameObject;

/**
* The chess-board is mechanism for keeping tabs on unit spatial orientation.
* Just like a real chessboard, there are only squares; Nothing to do with pixels.
*
* Specific pixel-perfect placement is to be dealt with elsewhere.
*/
public class ChessBoard
{

  /**
  * Our board dimensions in squares.
  * columns are synonymous with width (m).
  * rows are synonymous with height (n).
  */
  private final int rows;
  private final int columns;

  private final Square[][] squares;

  public ChessBoard( int rows, int columns )
  {
    this.rows    = rows;
    this.columns = columns;
    squaresByUnit = new HashMap<Integer, Square>();
    squares = new Square[rows][columns];

    setupVertices();
    setupEdges();
  }

  public void placeUnit( GameObject unit, int row, int col )
  {
    Square sq = squares[row][col];
    sq.occupy( unit );
  }
  
  /** 
   * The corollary of ChessBoard#placeUnit. 
   */
  public GameObject removeUnit( int row, int col )
  {
    return squares[row][col].unoccupy();
  }

  /**
  * A reverse lookup capability --- managed via Square#occupy et al.
  * Uses the GameObject.id as the key.
  */
  private final Map<Integer, Square> squaresByUnit;

  private Square findSquare( GameObject unit )
  {
    return squaresByUnit.get( unit.getId() );
  }

  /**
  * Create each squares (8x8 will be 64, etc.).
  */
  private void setupVertices()
  {
    boolean isColoured = false;    // flip back-and-forth between white vs. black.
    for ( int row = 0; row < rows; row++ )
    {
      for ( int col = 0; col < columns; col++ )
      {
        squares[row][col] = new Square( row, col, isColoured );
        isColoured = !isColoured;
      }
      isColoured = !isColoured;    // Stagger per row (not redundant).
    }
  }

  /**
  * Setup each square to have 4 neighbors max.
  */
  private void setupEdges()
  {
    for ( int row = 0; row < rows; row++ )
    {
      for ( int col = 0; col < columns; col++ )
      {
        Square sq = squares[row][col];
        sq.up    = ( row - 1 < 0           )? null : squares[ row + 1][ col     ];
        sq.left  = ( col - 1 < 0           )? null : squares[ row    ][ col - 1 ];
        sq.down  = ( row + 1 > rows    - 1 )? null : squares[ row - 1][ col     ];
        sq.right = ( col + 1 > columns - 1 )? null : squares[ row    ][ col + 1 ];
      }
    }
  }

  /**
  * An individual place within our board.
  */
  private class Square
  {
    /** ie. "A3" */
    protected final String name;

    protected final int row;
    protected final int column;

    /** Friendly neighbors. */
    protected Square up;
    protected Square down;
    protected Square left;
    protected Square right;

    /**
    * Think alternating white vs. black squares in a chess-board.
    */
    protected final boolean isColoured;

    /** The current occupant of the square (changes). */
    private GameObject occupant = null;

    Square( int row, int column, boolean isColoured )
    {
      name =  String.valueOf( Character.toChars( 65 + column ) ) +
              String.valueOf( row + 1 );
      this.row        = row;
      this.column     = column;
      this.isColoured = isColoured;
    }

    public void occupy( GameObject unit )
    {
      if ( null != occupant )
      {
        throw new RuntimeException( "Don't double-book units onto the same square." );
      }
      if ( null == unit )
      {
        throw new RuntimeException( "NPE - Invalid occupant for square." );
      }
      occupant = unit;
      squaresByUnit.put( unit.getId(), this );
    }

    /**
    * Clear out a square and returns the current occupant, if any.
    */
    public GameObject unoccupy()
    {
      GameObject unit = occupant;
      occupant = null;
      squaresByUnit.put( unit.getId(), null );
      return unit;
    }

    @Override
    public String toString()
    {
      return name;
    }
  }

}
