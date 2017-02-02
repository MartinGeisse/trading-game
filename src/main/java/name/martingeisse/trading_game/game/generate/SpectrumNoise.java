package name.martingeisse.trading_game.game.generate;

/**
 * Generates multiple levels of Perlin noise at different frequencies.
 * <p>
 * Sample this noise from 0...1 along both axes to avoid largest-scale structures from becoming visible. Outside that
 * range, it will be visible that the largest structure exists at a scale of around 1.0 (this being the most coarse
 * level of Perlin noise).
 */
public final class SpectrumNoise {

	private final PerlinNoise[] noiseGenerators;
	private final double amplifier;

	public SpectrumNoise(int octaves, double amplifier) {
		noiseGenerators = new PerlinNoise[octaves];
		for (int i = 0; i < noiseGenerators.length; i++) {
			noiseGenerators[i] = new PerlinNoise(i);
		}
		this.amplifier = amplifier;
	}

	/**
	 * Gets the noise value at the specified position, roughly in the value range -1..+1.
	 */
	public double get(double x, double y) {
		double value = 0.0;
		for (PerlinNoise noise : noiseGenerators) {
			value += noise.computeNoise(x, y);
			x *= 2.0;
			y *= 2.0;
		}
		return value * amplifier / noiseGenerators.length;
	}

	/**
	 * Gets the noise value at the specified position, clamped to the value range -1..+1.
	 */
	public double getClamped(double x, double y) {
		double value = get(x, y);
		return (value < -1.0 ? -1.0 : value > 1.0 ? 1.0 : value);
	}

}
