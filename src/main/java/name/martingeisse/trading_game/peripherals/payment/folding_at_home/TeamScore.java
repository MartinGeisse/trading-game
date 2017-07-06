package name.martingeisse.trading_game.peripherals.payment.folding_at_home;

/**
 *
 */
public final class TeamScore {

	private final int workUnits;
	private final int credits;
	private final int rank;

	public TeamScore(int workUnits, int credits, int rank) {
		this.workUnits = workUnits;
		this.credits = credits;
		this.rank = rank;
	}

	public int getWorkUnits() {
		return workUnits;
	}

	public int getCredits() {
		return credits;
	}

	public int getRank() {
		return rank;
	}

}
