package name.martingeisse.trading_game.game.player;

import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipment;
import name.martingeisse.trading_game.game.space.PlayerShip;

import java.util.Map;

/**
 *
 */
public interface PlayerDataLink {

	public long getId();
	public String getName();
	public void setName(String name);
	public String getLoginToken();
	public void setLoginToken(String loginToken);
	public String getEmailAddress();
	public void setEmailAddress(String emailAddress);
	public PlayerShip getShip();
	public ActionQueue getActionQueue();
	public PlayerShipEquipment getEquipment();
	public void replacePlayerAttributes(Map<PlayerAttributeKey, ?> newAttributes);
	public Object getAttribute(PlayerAttributeKey key);

}
