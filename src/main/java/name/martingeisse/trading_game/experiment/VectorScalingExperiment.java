package name.martingeisse.trading_game.experiment;

/**
 * Gegeben sei ein 2d-Vektor (x, y) und eine gewünschte Länge s. Finde einen Vektor (x2, y2) in derselben Richtung wie
 * (x, y) mit der Länge s. (Anwendung: Flug von startPosition zu zielPosition; der Differenzvektor ist (x, y), die
 * Länge s ist die Fluggeschwindigkeit pro game-step).
 *
 * x, y, s sind als long gegeben. Vorsicht vor Overflow und Rundungsfehlern; wenn möglich reine Integer-Arithmetik.
 * Insbesondere kann der Einheitsvektor in (x, y)-Richtung Nachkommastellen enthalten, die durch die Multiplikation
 * mit s zu nennenswerten Beiträgen werden!
 */
public class VectorScalingExperiment {

	private static final long x = 1_000_000_000_000_000L;
	private static final long y = 5_000_000_000_000_000L;
	private static final long s = 1_000_000_000_000_000L;
	private static long x2, y2;

	public static void main(String[] args) {
		useDoubleOnly();
		System.out.println("result: " + x2 + ", " + y2);
	}

	// Ergebnisse sehen gut aus und für diesen Fall passt double besser als long!
	public static void useDoubleOnly() {
		double x = VectorScalingExperiment.x;
		double y = VectorScalingExperiment.y;
		double norm = Math.sqrt(x * x + y * y);
		double factor = s / norm;
		x2 = Math.round(x * factor);
		y2 = Math.round(y * factor);
	}

	public static void useLongOnly() {
		// x2, y2 sind im Bereich -s, -s bis s, s.
	}

}
