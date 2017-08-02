package name.martingeisse.trading_game.tools.init;

import com.google.inject.Injector;
import name.martingeisse.trading_game.platform.application.CommandLineApplicationBootstrapper;

/**
 * Executes all initializers. The weird name puts this on top of the list in the file navigator.
 */
public class AaaMain {

	/**
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		Injector injector = CommandLineApplicationBootstrapper.bootstrap();
		DatabaseStructureInitMain.init(injector);
		SpaceInitMain.init(injector);
		GlobalMarketOrderInitMain.init(injector);
	}

}
