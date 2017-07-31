package name.martingeisse.trading_game.peripherals.payment.folding_at_home;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.player.PlayerRepository;

/**
 * Implements "payment" via FaH.
 */
@Singleton
public class FoldingAtHomePaymentService {

	private final PlayerRepository playerRepository;
	private final FoldingAtHomeApiService foldingAtHomeApiService;

	@Inject
	public FoldingAtHomePaymentService(PlayerRepository playerRepository, FoldingAtHomeApiService foldingAtHomeApiService) {
		this.playerRepository = playerRepository;
		this.foldingAtHomeApiService = foldingAtHomeApiService;
	}

	public void updatePlayTimeCredits() {
		playerRepository.forEachPlayer(player -> {
			String userHash = player.getFoldingUserHash();
			if (userHash != null) {
				String userName = "SpaceTrading-" + userHash;
				UserScore userScore = foldingAtHomeApiService.getUserScore(userName);
				player.updatePlayTimeCredits(userScore.getCredits());
			}
		});
	}

}
