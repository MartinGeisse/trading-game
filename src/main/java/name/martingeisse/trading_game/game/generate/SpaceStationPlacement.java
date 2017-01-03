package name.martingeisse.trading_game.game.generate;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.space.SpaceObject;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 */
public final class SpaceStationPlacement implements Iterable<Pair<Long, Long>> {

	private final List<Pair<Long, Long>> spaceStations = new ArrayList<>();

	private SpaceStationPlacement(ImmutableList<? extends SpaceObject> anchors, int numberOfSpaceStations, long minimumDistance, long maximumDistance) {
		double deltaDistance = maximumDistance - minimumDistance;
		Random random = new Random();
		for (int i=0; i<numberOfSpaceStations; i++) {
			SpaceObject anchor = anchors.get(random.nextInt(anchors.size()));
			double angle = 2 * Math.PI * random.nextDouble();
			double distance = minimumDistance + deltaDistance * random.nextDouble();
			long dx = (long)(distance * Math.cos(angle));
			long dy = (long)(distance * Math.sin(angle));
			Pair<Long, Long> spaceStation = Pair.of(anchor.getX() + dx, anchor.getY() + dy);
			spaceStations.add(spaceStation);
		}
	}

	@Override
	public Iterator<Pair<Long, Long>> iterator() {
		return spaceStations.iterator();
	}

	/**
	 *
	 */
	public static Iterable<Pair<Long, Long>> compute(ImmutableList<? extends SpaceObject> anchors, int numberOfSpaceStations, long minimumDistance, long maximumDistance) {
		return new SpaceStationPlacement(anchors, numberOfSpaceStations, minimumDistance, maximumDistance);
	}

}
