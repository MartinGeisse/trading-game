package name.martingeisse.trading_game.game.item;

import name.martingeisse.trading_game.game.GameLogicException;

/**
 * This exception type gets thrown when trying to remove items from an inventory that aren't there.
 */
public final class NotEnoughItemsException extends GameLogicException {

	public NotEnoughItemsException() {
		super("not enough items");
	}

}
