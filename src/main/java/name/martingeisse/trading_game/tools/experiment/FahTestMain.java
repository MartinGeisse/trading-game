package name.martingeisse.trading_game.tools.experiment;

import com.google.inject.Injector;
import name.martingeisse.trading_game.peripherals.payment.folding_at_home.FoldingAtHomeApiService;
import name.martingeisse.trading_game.peripherals.payment.folding_at_home.TeamScore;
import name.martingeisse.trading_game.peripherals.payment.folding_at_home.UserScore;
import name.martingeisse.trading_game.platform.application.CommandLineApplicationBootstrapper;

/**
 *
 */
public class FahTestMain {

	private static final String[] usernames = {
		"SpaceTrading-5e8360a16120a1c3ba3c52a923bdbe45f634f884a35731ac65a",
		"SpaceTrading-d9cdf8453ce1a8d748cdfc4b30ec3bfd08d5b252f70209f2174",
		"SpaceTrading-1e1bb463cab93f38c2bac4b2c806764f98ed1d26ebc51faab2b",
	};

	public static void main(String[] args) {
		Injector injector = CommandLineApplicationBootstrapper.bootstrap();

		TeamScore teamScore = injector.getInstance(FoldingAtHomeApiService.class).getTeamScore();
		System.out.println("teamScore: " + teamScore.getCredits() + " (" + teamScore.getWorkUnits() + " WUs) -- rank " + teamScore.getRank());

		for (String username : usernames) {
			UserScore userScore = injector.getInstance(FoldingAtHomeApiService.class).getUserScore(username);
			System.out.println(username + ": " + userScore.getCredits() + " (" + userScore.getWorkUnits() + " WUs)");
		}

	}

}
