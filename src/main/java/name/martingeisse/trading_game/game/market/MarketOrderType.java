package name.martingeisse.trading_game.game.market;

/**
 *
 */
public enum MarketOrderType {

	BUY {
		@Override
		public MarketOrderType getOpposite() {
			return SELL;
		}
	},

	SELL {
		@Override
		public MarketOrderType getOpposite() {
			return BUY;
		}
	};

	public abstract MarketOrderType getOpposite();

}
