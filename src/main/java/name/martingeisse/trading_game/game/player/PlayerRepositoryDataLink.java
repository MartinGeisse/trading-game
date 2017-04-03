package name.martingeisse.trading_game.game.player;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.postgres_entities.PlayerRow;

import java.util.function.Consumer;

/**
 *
 */
public interface PlayerRepositoryDataLink {

	public Player createPlayer(PlayerRow data);
	public ImmutableList<Player> getAllPlayers();
	public ImmutableList<String> getLoginTokensByEmailAddress(String emailAddress);
	public Player getPlayerById(long id);
	public Player getPlayerByName(String name);
	public Player getPlayerByShipId(long shipId);
	public Player getPlayerByLoginToken(String loginToken);
	public boolean isRenamePossible(long id, String newName);
	public void forEachPlayer(PostgresConnection connection, Consumer<Player> body);

}
