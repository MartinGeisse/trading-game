package name.martingeisse.trading_game.game.market;

import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.item.NotEnoughItemsException;
import name.martingeisse.trading_game.game.item.ObjectWithInventory;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.game.space.SpaceStation;

/**
 * TODO for now, escrow works by just removing the items from the game altogether. This is wrong because those items
 * don't count towards the maximum cargo mass of their location.
 */
@Singleton
public class EscrowService {

	public void putMoneyInEscrow(Player owner, long amount) throws NotEnoughMoneyException {
		owner.subtractMoney(amount);
	}

	public void putItemsInEscrow(SpaceObject location, Player owner, ItemType itemType, int quantity) throws NotEnoughItemsException {
		checkLocationValidForEscrow(location);
		Inventory inventory = ((ObjectWithInventory) location).getInventory();
		inventory.remove(owner.getId(), itemType, quantity);
	}

	public void removeMoneyFromEscrow(Player owner, long amount) {
		owner.addMoney(amount);
	}

	public void removeItemsFromEscrow(SpaceObject location, Player owner, ItemType itemType, int quantity) {
		// for now, buying from the store puts items directly into the player's ship. These items never were in escrow.
		// So we disable the check for now. That's okay because this is a double check -- at a player's ship, there
		// cannot be a market order other than shop-sells orders, so trying to remove an item from escrow there
		// is impossible anyway.
		// checkLocationValidForEscrow(location);
		Inventory inventory = ((ObjectWithInventory) location).getInventory();
		inventory.add(owner.getId(), itemType, quantity);
	}

	private void checkLocationValidForEscrow(SpaceObject location) {
		if (!(location instanceof SpaceStation)) {
			throw new IllegalArgumentException("invalid location to put items in escrow: " + location);
		}
	}

}
