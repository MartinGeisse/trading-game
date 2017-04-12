package name.martingeisse.trading_game.game.market;

import name.martingeisse.trading_game.game.GameLogicException;

/**
 *
 */
public class NotEnoughMoneyException extends GameLogicException {

	public NotEnoughMoneyException() {
		super("not enough money");
	}

}
