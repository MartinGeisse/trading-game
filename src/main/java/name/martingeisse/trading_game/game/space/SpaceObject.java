package name.martingeisse.trading_game.game.space;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.actions.MoveToPositionAction;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.QSpaceObjectBaseDataRow;

/**
 *
 */
public abstract class SpaceObject implements PositionProvider {

	public static final String DEFAULT_NAME = "unnamed";

	private PostgresService postgresService;
	private long id;
	private String name = DEFAULT_NAME;
	private long x;
	private long y;

	/**
	 * Setter method.
	 *
	 * @param postgresService the postgresService
	 */
	@Inject
	public void internalSetPostgresService(PostgresService postgresService) {
		this.postgresService = postgresService;
	}

	/**
	 * Getter method.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setter method.
	 *
	 * @param id the id
	 */
	void internalSetId(long id) {
		this.id = id;
	}

	/**
	 * Getter method.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method.
	 *
	 * @param name the name
	 */
	void internalSetName(String name) {
		this.name = (name == null ? DEFAULT_NAME : name);
	}

	/**
	 * Getter method.
	 *
	 * @return the x
	 */
	@Override
	public long getX() {
		return x;
	}

	/**
	 * Setter method.
	 *
	 * @param x the x
	 */
	void internalSetX(long x) {
		this.x = x;
	}

	/**
	 * Getter method.
	 *
	 * @return the y
	 */
	@Override
	public long getY() {
		return y;
	}

	/**
	 * Setter method.
	 *
	 * @param y the y
	 */
	void internalSetY(long y) {
		this.y = y;
	}

	/**
	 * Changes the name of this object.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QSpaceObjectBaseDataRow qbd = QSpaceObjectBaseDataRow.SpaceObjectBaseData;
			connection.update(qbd).set(qbd.name, name).execute();
		}
		internalSetName(name);
	}

	/**
	 * Changes the position of this object.
	 *
	 * @param x the new x coordinate
	 * @param y the new y coordinate
	 */
	public void setPosition(long x, long y) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QSpaceObjectBaseDataRow qbd = QSpaceObjectBaseDataRow.SpaceObjectBaseData;
			connection.update(qbd).set(qbd.x, x).set(qbd.y, y).execute();
		}
		internalSetX(x);
		internalSetY(y);
	}

	/**
	 * Called once every second to advance game logic. Whether this method is supported can be checked by
	 * {@link SpaceObjectType#getTypesThatSupportTick()}.
	 */
	public void tick(PostgresConnection connection) {
		throw new UnsupportedOperationException("tick() not supported");
	}

	/**
	 *
	 */
	public ImmutableList<Action> getActionsFor(Player player) {
		return ImmutableList.of(getMoveToPositionActionFor(player));
	}

	/**
	 * @param player
	 * @return
	 */
	public final Action getMoveToPositionActionFor(Player player) {
		return new MoveToPositionAction(player.getShip(), x, y, player::getShipMovementSpeed);
	}

}
