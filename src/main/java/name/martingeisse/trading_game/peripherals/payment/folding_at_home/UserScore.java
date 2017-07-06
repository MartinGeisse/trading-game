package name.martingeisse.trading_game.peripherals.payment.folding_at_home;

/**
 *
 */
public final class UserScore {

	private final int workUnits;
	private final int credits;

	public UserScore(int workUnits, int credits) {
		this.workUnits = workUnits;
		this.credits = credits;
	}

	public int getWorkUnits() {
		return workUnits;
	}

	public int getCredits() {
		return credits;
	}

}
