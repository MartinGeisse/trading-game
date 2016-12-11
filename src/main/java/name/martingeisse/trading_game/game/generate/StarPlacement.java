package name.martingeisse.trading_game.game.generate;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Places stars while keeping some distance constraints.
 *
 * TODO: the net suggest poisson disc sampling with the density parameter determined using perlin noise
 */
public final class StarPlacement implements Iterable<Pair<Long, Long>> {

	private final List<Pair<Long, Long>> stars = new ArrayList<>();

	private StarPlacement(long minimumDistance, long maximumDistance, int breadth, long galaxyRadius) {
		Pair<Long, Long> seed = Pair.of(0L, 0L);
		stars.add(seed);
		List<Pair<Long, Long>> processingList = new ArrayList<>();
		processingList.add(seed);
		Random random = new Random();
		double deltaDistance = maximumDistance - minimumDistance;
		while (!processingList.isEmpty()) {
			Pair<Long, Long> star = processingList.remove(random.nextInt(processingList.size()));
			if (star == null) {
				break;
			}
			if (!starsCloserThan(seed, star, galaxyRadius)) {
				continue;
			}
			breadthLoop: for (int i=0; i<breadth; i++) {
				double angle = 2 * Math.PI * random.nextDouble();
				double distance = minimumDistance + deltaDistance * random.nextDouble();
				long dx = (long)(distance * Math.cos(angle));
				long dy = (long)(distance * Math.sin(angle));
				Pair<Long, Long> neighbor = Pair.of(star.getLeft() + dx, star.getRight() + dy);
				for (Pair<Long, Long> potentialBlocker : stars) {
					if (starsCloserThan(neighbor, potentialBlocker, minimumDistance)) {
						continue breadthLoop;
					}
				}
				stars.add(neighbor);
				processingList.add(neighbor);
			}
		}
	}

	private static boolean starsCloserThan(Pair<Long, Long> star1, Pair<Long, Long> star2, long minimumDistance) {
		long dx = star1.getLeft() - star2.getLeft();
		long dy = star1.getRight() - star2.getRight();
		// only compute the squares if the bounding-box check fails, since otherwise they may overflow
		if (dx > minimumDistance || dx < -minimumDistance || dy > minimumDistance || dy < -minimumDistance) {
			return false;
		}
		return dx * dx + dy * dy < minimumDistance * minimumDistance;
	}

	@Override
	public Iterator<Pair<Long, Long>> iterator() {
		return stars.iterator();
	}

	/**
	 *
	 */
	public static Iterable<Pair<Long, Long>> compute(long minimumDistance, long maximumDistance, int breadth, long galaxyRadius) {
		return new StarPlacement(minimumDistance, maximumDistance, breadth, galaxyRadius);
	}

}