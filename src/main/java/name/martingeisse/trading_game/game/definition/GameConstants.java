package name.martingeisse.trading_game.game.definition;

/**
 * This class defines constants that are too universal to be in the {@link GameDefinition}. The idea is that these
 * constants cannot simply be changed, despite being fields of this class, since their value has inspired certain
 * ways to deal with data. For example, the BASE_MINING_SPEED was selected the way it is to avoid rounding issues.
 */
public final class GameConstants {

	/**
	 * The amount of rock mined from asteroids per second. This is the base value before upgrades and skills have been applied.
	 */
	public static final long BASE_MINING_SPEED = 10;

	/**
	 * The number of seconds of play time earned for each Folding@Home credit.
	 */
	public static final long PLAY_TIME_SECONDS_PER_FAH_CREDIT = 2500;

}
