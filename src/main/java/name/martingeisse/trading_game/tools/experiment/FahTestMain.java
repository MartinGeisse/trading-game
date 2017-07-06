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

	public static void main(String[] args) {
		Injector injector = CommandLineApplicationBootstrapper.bootstrap();

		TeamScore teamScore = injector.getInstance(FoldingAtHomeApiService.class).getTeamScore();
		System.out.println("teamScore: " + teamScore.getCredits() + " (" + teamScore.getWorkUnits() + " WUs) -- rank " + teamScore.getRank());

		UserScore userScore = injector.getInstance(FoldingAtHomeApiService.class).getUserScore("SpaceTrading-1e1bb463cab93f38c2bac4b2c806764f98ed1d26ebc51faab2b");
		System.out.println("userScore: " + userScore.getCredits() + " (" + userScore.getWorkUnits() + " WUs)");

	}

}
