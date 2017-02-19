package name.martingeisse.trading_game.tools.init;

import com.google.inject.Injector;
import name.martingeisse.trading_game.game.definition.GameConstants;
import name.martingeisse.trading_game.game.definition.GameDefinition;
import name.martingeisse.trading_game.game.generate.SpaceStationPlacement;
import name.martingeisse.trading_game.game.generate.StarNaming;
import name.martingeisse.trading_game.game.generate.StarPlacement;
import name.martingeisse.trading_game.game.item.ImmutableItemStacks;
import name.martingeisse.trading_game.game.space.InitialSpaceObjectsFactory;
import name.martingeisse.trading_game.platform.application.CommandLineApplicationBootstrapper;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 */
public class SpaceInitMain {

	/**
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		Injector injector = CommandLineApplicationBootstrapper.bootstrap();
		GameDefinition gameDefinition = injector.getInstance(GameDefinition.class);
		InitialSpaceObjectsFactory initialSpaceObjectsFactory = injector.getInstance(InitialSpaceObjectsFactory.class);
		StarPlacement starPlacement = new StarPlacement();
		{
			long miningCapacity = 1000 * GameConstants.BASE_MINING_SPEED;
			ImmutableItemStacks asteroidYieldPerTick = ImmutableItemStacks.from(gameDefinition.getRedPixelItemType(), 5);
			for (Pair<Long, Long> starPosition : starPlacement) {
				String name = StarNaming.compute();
				long x = starPosition.getLeft();
				long y = starPosition.getRight();
				initialSpaceObjectsFactory.createAsteroid(name, x, y, asteroidYieldPerTick, miningCapacity);
			}
		}
		{
			int numberOfSpaceStations = starPlacement.getNumberOfStars() / 10 + 2;
			int namingCounter = 1;
			for (Pair<Long, Long> spaceStationPosition : SpaceStationPlacement.compute(starPlacement, numberOfSpaceStations, 3000, 6000)) {
				String name = "space station " + namingCounter;
				long x = spaceStationPosition.getLeft();
				long y = spaceStationPosition.getRight();
				initialSpaceObjectsFactory.createSpaceStation(name, x, y);
				namingCounter++;
			}
		}
	}

}
