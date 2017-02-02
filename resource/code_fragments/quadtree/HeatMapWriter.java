package name.martingeisse.trading_game.game.space.quadtree;

import name.martingeisse.trading_game.game.space.SpaceObject;

import java.util.Collection;

/**
 *
 */
public interface HeatMapWriter {

	public void writeSpaceObjects(Collection<? extends SpaceObject> spaceObjects);


}
