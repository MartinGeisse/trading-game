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

import java.util.Random;

/**
 *
 */
public class SpaceInitMain {

	private static final String ASTEROID_CODE_CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ0123456789";
	private static final int ASTEROID_CODE_CHARACTERS_LENGTH = ASTEROID_CODE_CHARACTERS.length();

	private static final Random random = new Random();
	private static Injector injector;
	private static GameDefinition gameDefinition;
	private static InitialSpaceObjectsFactory initialSpaceObjectsFactory;

	/**
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		init(CommandLineApplicationBootstrapper.bootstrap());
	}

	public static void init(Injector injector) throws Exception {
		SpaceInitMain.injector = injector;
		gameDefinition = injector.getInstance(GameDefinition.class);
		initialSpaceObjectsFactory = injector.getInstance(InitialSpaceObjectsFactory.class);
		for (Pair<Long, Long> starPosition : new StarPlacement()) {
			generateStarSystem(starPosition.getLeft(), starPosition.getRight());
		}
	}


	private static void generateStarSystem(long starX, long starY) {
		String starName = StarNaming.compute();
		initialSpaceObjectsFactory.createStar(starName, starX, starY);
		double distance = 100_000;
		int numberOfPlanets = random.nextInt(10);
		int planetCounter = 0;
		for (int i = 0; i < numberOfPlanets; i++) {
			int typeKey = random.nextInt(100);
			if (typeKey < 50) {

				// planet
				planetCounter++;
				String planetName = starName + ' ' + planetCounter;
				double angle = random.nextDouble() * 2 * Math.PI;
				long planetX = (long)(starX + Math.cos(angle) * distance);
				long planetY = (long)(starY + Math.sin(angle) * distance);
				initialSpaceObjectsFactory.createPlanet(planetName, planetX, planetY);

			} else if (typeKey < 90) {

				// partial asteroid belt
				double baseAngle = random.nextDouble() * 2 * Math.PI;
				int numberOfAsteroids = random.nextInt(100) + 10;
				double angularSpan = random.nextDouble() * (2 * Math.PI - 0.1) + 0.1;
				double radialSpan = random.nextDouble() * 20_000;
				for (int j = 0; j< numberOfAsteroids; j++) {
					double angle = baseAngle + random.nextDouble() * angularSpan;
					double asteroidDistance = distance + random.nextDouble() * radialSpan;
					long asteroidX = (long)(starX + Math.cos(angle) * asteroidDistance);
					long asteroidY = (long)(starY + Math.sin(angle) * asteroidDistance);
					ImmutableItemStacks asteroidYieldPerTick = ImmutableItemStacks.from(gameDefinition.getRedPixelItemType(), 5);
					long miningCapacity = 1000 * GameConstants.BASE_MINING_SPEED;
					initialSpaceObjectsFactory.createAsteroid(starName + " A-" + generateAsteroidCode(), asteroidX, asteroidY, asteroidYieldPerTick, miningCapacity);
				}

			} else {

				// TODO complete asteroid belt

			}
			distance += 40_000 + random.nextInt(50_000);
		}
	}

	private static String generateAsteroidCode() {
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<5; i++) {
			builder.append(ASTEROID_CODE_CHARACTERS.charAt(random.nextInt(ASTEROID_CODE_CHARACTERS_LENGTH)));
		}
		return builder.toString();
	}



//	public static void init(Injector injector) {
//		{
//			int numberOfSpaceStations = starPlacement.getNumberOfStars() / 10 + 2;
//			int namingCounter = 1;
//			for (Pair<Long, Long> spaceStationPosition : SpaceStationPlacement.compute(starPlacement, numberOfSpaceStations, 3000, 6000)) {
//				String name = "space station " + namingCounter;
//				long x = spaceStationPosition.getLeft();
//				long y = spaceStationPosition.getRight();
//				initialSpaceObjectsFactory.createSpaceStation(name, x, y);
//				namingCounter++;
//			}
//		}
//	}

}
