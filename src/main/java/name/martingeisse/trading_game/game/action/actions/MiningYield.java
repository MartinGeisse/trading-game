package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.item.ImmutableItemStacks;

/**
 *
 */
public final class MiningYield {

	private final ImmutableItemStacks items;
	private final boolean depleted;
	private final boolean cargoExhausted;

	public MiningYield(ImmutableItemStacks items, boolean depleted, boolean cargoExhausted) {
		this.items = items;
		this.depleted = depleted;
		this.cargoExhausted = cargoExhausted;
	}

	/**
	 * Getter method.
	 *
	 * @return the items
	 */
	public ImmutableItemStacks getItems() {
		return items;
	}

	/**
	 * Getter method.
	 *
	 * @return the depleted
	 */
	public boolean isDepleted() {
		return depleted;
	}

	/**
	 * Getter method.
	 *
	 * @return the cargoExhausted
	 */
	public boolean isCargoExhausted() {
		return cargoExhausted;
	}

}
