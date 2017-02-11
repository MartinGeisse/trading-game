package name.martingeisse.trading_game.game;

import name.martingeisse.trading_game.game.space.SpaceObject;

/**
 *
 */
public interface GameListener {

	public void onDynamicSpaceObjectsChanged();

	public void onSpaceObjectPropertiesChanged(SpaceObject spaceObject);

}
