package org.aschyiel.rpg.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.aschyiel.rpg.GameObject;
import org.aschyiel.rpg.IFullGameObject;
import org.aschyiel.rpg.IGameObject;
import org.aschyiel.rpg.Resorcerer;
import org.aschyiel.rpg.activities.Terrain;
import org.aschyiel.rpg.level.UnitType;

import android.util.Log;

/**
* The chess-board is mechanism for keeping tabs on unit spatial orientation.
* Just like a real chessboard, there are only squares; Nothing to do with pixels.
*
* Specific pixel-perfect placement is to be dealt with elsewhere.
*/
public class ChessBoard
{

  protected static final String TAG = "[RPG:ChessBoard]";
  /**
  * Our board dimensions in squares.
  * rows    are synonymous with height (m).
  * columns are synonymous with width  (n).
  */
  protected final int rows;
  protected final int columns;

  protected final Square[][] squares;

  public ChessBoard( int rows, int columns )
  {
    this.rows    = rows;
    this.columns = columns;
    squaresByUnit = new HashMap<Integer, Square>();
    squares = new Square[rows][columns];

    setupVertices();
    setupEdges();
  }

  public void placeUnit( final IGameObject unit, int row, int col )
  {
    placeUnit( unit, squares[row][col] );
  }
  public void placeUnit( final IGameObject unit, final Square sq )
  {
    sq.occupy( unit );
  }
  
  /** 
   * The corollary of ChessBoard#placeUnit. 
   */
  public IGameObject removeUnit( int row, int col )
  {
    return removeUnit( squares[row][col] );
  }
  public IGameObject removeUnit( final Square sq )
  {
    final IGameObject unit = sq.unoccupy();
    broadcastVacancy( sq );
    return unit;
  }

  /**
  * Let others know that a previously occupied square is now available.
  * Needed for blocked units that are trying to complete their navigation-steps.
  *
  * @see GirlFriend#guide
  */
  private void broadcastVacancy( final Square sq )
  {
    for ( VacancySubscriber sub : vacancySubscribers )
    {
      sub.onVacancy( sq );
    }
  }

  private List<VacancySubscriber> vacancySubscribers = new ArrayList<VacancySubscriber>();

  public void subscribeToVacancies( final VacancySubscriber sub )
  {
    vacancySubscribers.add( sub );
  }

  /**
  * Setup the touch event listeners accross the board;
  * (Render the squares - for development purposes only).
  */
  public void bindSquares( float w,
                           float h,
                           Resorcerer rez,
                           Scene scn,
                           final OnSquareClickHandler cb )
  {
    for ( int m = 0; m < rows;    m++ )
    for ( int n = 0; n < columns; n++ )
    {
      final Square sq = squares[m][n];
      final Sprite sprite = new Sprite( w * n,
                                        h * m,
                                        rez.getTexture( "terrain_tile" ),
                                        rez.getVertexBufferObjectManager() )
          {
            @Override
            public boolean onAreaTouched( final TouchEvent vente,
                                          final float _,
                                          final float __ )
            {
              if ( !vente.isActionUp() )
              {
                return false;
              }
              cb.onSquareClicked( sq, (IFullGameObject) sq.occupant );
              return true;
            }
          };
      sprite.setAlpha( ( Terrain.DEV )? 0.3f : 0f );
      sprite.setSize( w, h );
      scn.registerTouchArea( sprite );
      scn.attachChild( sprite );
      sq.sprite = sprite;
    }
  }

  /**
  * A reverse lookup capability --- managed via Square#occupy et al.
  * Uses the IGameObject.id as the key.
  */
  private final Map<Integer, Square> squaresByUnit;

  protected Square findSquare( IGameObject unit )
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
        sq.up    = ( row - 1 < 0           )? null : squares[ row - 1][ col     ];
        sq.left  = ( col - 1 < 0           )? null : squares[ row    ][ col - 1 ];
        sq.down  = ( row + 1 > rows    - 1 )? null : squares[ row + 1][ col     ];
        sq.right = ( col + 1 > columns - 1 )? null : squares[ row    ][ col + 1 ];
      }
    }
  }

  /**
  * An individual place within our board.
  */
  public class Square
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

    /** For debugging. */
    protected Sprite sprite;

    /**
    * Think alternating white vs. black squares in a chess-board.
    */
    protected final boolean isColoured;

    /** The current occupant of the square (changes). */
    protected IGameObject occupant = null;

    Square( int row, int column, boolean isColoured )
    {
      name =  String.valueOf( Character.toChars( 65 + column ) ) +
              String.valueOf( row + 1 );
      this.row        = row;
      this.column     = column;
      this.isColoured = isColoured;
    }

    protected void occupy( IGameObject unit )
    {
      if ( isOccupado() )
      {
        throw new RuntimeException( "Don't double-book units onto the same square." );
      }
      if ( null == unit )
      {
        throw new RuntimeException( "NPE - Invalid occupant for square." );
      }
      occupant = unit;
      squaresByUnit.put( unit.getId(), this );

      if ( Terrain.DEV && null != sprite )
      {
        sprite.setAlpha( 1f );
      }
    }

    /**
    * Clear out a square and returns the current occupant, if any.
    */
    protected IGameObject unoccupy()
    {
      IGameObject unit = occupant;
      occupant = null;
      if ( null != unit && this == squaresByUnit.get( unit.getId() ) )
      {
        // GOTCHA: Be specific vs. last-one wins.
        squaresByUnit.put( unit.getId(), null );
      }

      if ( Terrain.DEV && null != sprite )
      {
        sprite.setAlpha( 0.3f );
      }

      return unit;
    }

    /**
    * Returns true if we're NOT vacant.
    */
    protected boolean isOccupado()
    {
      return null != occupant;
    }

    @Override
    public String toString()
    {
      return name;
    }

    /**
    * Returns true if this square is unavailable to the given unit-type.
    * ie. ships can't go on land.
    */
    public boolean isInaccessible( UnitType unitType )
    {
      // Always allow empty-values free reign (for testing).
      if ( null == unitType )
      {
        return false;
      }

      // TODO: Don't hardcode -- base it off of land-types, etc.
      //   Also, we should allow units to "run-over" other unit-types,
      //   based on the occupant, etc.
      return isOccupado();
    }
  }

}
