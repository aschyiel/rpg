package org.aschyiel.rpg;

import org.aschyiel.rpg.activities.Terrain;

/**
* The game-object manufacturing plant.
*/
public class GameObjectFactory
{
  //
  // Statics.
  //

  private static final Coordinates DEFAULT_COORDS = new Coordinates( 0, 0 );

  public GameObjectFactory( Terrain terrain, Resorcerer resorcerer )
  {
    this.terrain    = terrain;
    this.resorcerer = resorcerer;
  }

  private Resorcerer resorcerer;
  private Terrain    terrain;

  /**
  * Make something - to be called AFTER all resources have been loaded.
  */
  public IGameObject make( GameObjectType type )
  {
    switch( type )
    {
      case TANK:
        return makeTank();
      case TERRAIN_TILE:
        return makeTerrainTile();
      case NAVIGATIONAL_POINT:
        return makeNavigationalPoint();
      default:
        throw new Error( "Oops! Unknown game-object type." );
    }
  }

  //---------------------------------
  //
  // Private Methods.
  //
  //---------------------------------

  /**
  * Returns a basic tank.
  */
  private IGameObject makeTank()
  {
    final BaseGameObject tank = new BaseGameObject();
    tank.setTerrain( terrain );
    tank.setCoords( DEFAULT_COORDS );
    tank.setSprite( resorcerer.getTankSprite() );

    // TODO: Set HP, attack, speed, range, other stats, etc.

    return (IGameObject) tank;
  }

  /**
  * Returns a abstract game-object representing a single tile within the terrian view.
  */
  private IGameObject makeTerrainTile()
  {
    final IGameObject tile = new BaseGameObject();
    tile.setCoords( DEFAULT_COORDS );
    tile.setSprite( resorcerer.getTerrainTileSprite() );
    return tile;
  }

  /**
  * Creates an item representing a single nav-point;
  * A collection of nav-points are used to represent a navigational-path.
  */
  private IGameObject makeNavigationalPoint()
  {
    final IGameObject navPoint = new BaseGameObject();
    navPoint.setSprite( resorcerer.getNavigationalPointSprite() );
    navPoint.getSprite().setVisible( false );    // Hidden by default.
    return navPoint;
  }

}