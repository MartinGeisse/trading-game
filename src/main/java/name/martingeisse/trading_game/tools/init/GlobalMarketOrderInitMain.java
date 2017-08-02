package name.martingeisse.trading_game.tools.init;

import com.google.common.collect.ImmutableList;
import com.google.inject.Injector;
import name.martingeisse.trading_game.game.definition.GameDefinition;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.market.InitialGlobalMarketOrderFactory;
import name.martingeisse.trading_game.game.market.MarketOrderType;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.player.PlayerFactory;
import name.martingeisse.trading_game.platform.application.CommandLineApplicationBootstrapper;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 *
 */
public class GlobalMarketOrderInitMain {

	/**
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		init(CommandLineApplicationBootstrapper.bootstrap());
	}

	/**
	 * @param injector ...
	 */
	public static void init(Injector injector) throws Exception {

		PlayerFactory playerFactory = injector.getInstance(PlayerFactory.class);
		Player storePlayer = playerFactory.createPlayer();
		storePlayer.renameTo("Store");

		GameDefinition gameDefinition = injector.getInstance(GameDefinition.class);
		InitialGlobalMarketOrderFactory initialGlobalMarketOrderFactory = injector.getInstance(InitialGlobalMarketOrderFactory.class);

		{
			int price = 1000;
			for (ItemType oreItemType : gameDefinition.getOreItemTypes()) {
				initialGlobalMarketOrderFactory.createMarketOrder(storePlayer, MarketOrderType.BUY, oreItemType, price);
				price = price * 5 / 4;
			}
		}

		{
			int price = 100_000;
			for (ImmutableList<ItemType> level : gameDefinition.getUpgradeItemTypes()) {
				for (ItemType equipmentItemType : level) {
					initialGlobalMarketOrderFactory.createMarketOrder(storePlayer, MarketOrderType.SELL, equipmentItemType, price);
				}
				price = price * 2;
			}
		}

	}

}
