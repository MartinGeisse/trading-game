package name.martingeisse.trading_game;

import com.google.inject.Injector;
import name.martingeisse.trading_game.game.player.PlayerAttributeKey;
import name.martingeisse.trading_game.game.player.PlayerRepository;
import name.martingeisse.trading_game.platform.application.CommandLineApplicationBootstrapper;

/**
 *
 */
public class ExperimentMain {

	/**
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		Injector injector = CommandLineApplicationBootstrapper.bootstrap();
		PlayerRepository playerRepository = injector.getInstance(PlayerRepository.class);
		playerRepository.getPlayerById(1).updateAttributes();
		System.out.println("* " + playerRepository.getPlayerById(1).getAttribute(PlayerAttributeKey.SHIP_MOVEMENT_SPEED));
	}

}
