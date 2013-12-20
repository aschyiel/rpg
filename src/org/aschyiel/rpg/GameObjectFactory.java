package org.aschyiel.rpg;

/**
* The game-object manufacturing plant.
*/
public class GameObjectFactory
{
  //
  // Statics.
  //

  private static final Coordinates DEFAULT_COORDS = new Coordinates( 0, 0 );

  public GameObjectFactory( Resorcerer resorcerer )
  {
    this.resorcerer = resorcerer;
  }

  private Resorcerer resorcerer;

  /**
  * Make something - to be called AFTER all resources have been loaded.
  */
  public IGameObject make( GameObjectType type )
  {
    switch( type )
    {
      case TANK:
        return makeTank();
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
    final IGameObject tank = new BaseGameObject();
    tank.setCoords( DEFAULT_COORDS );
    tank.setSprite( resorcerer.getTankSprite() );

    // TODO: Set HP, attack, speed, range, other stats, etc.

    return tank;
  }

}