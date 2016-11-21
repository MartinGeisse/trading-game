package name.martingeisse.trading_game.gui.item;

import name.martingeisse.trading_game.game.item.ItemType;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

/**
 *
 */
public final class ItemIcons {

	// prevent instantiation
	private ItemIcons() {
	}

	/**
	 * Gets a resource reference for the item icon for the specified item type.
	 *
	 * @param itemType
	 * @return
	 */
	public static ResourceReference get(ItemType itemType) {
		return new PackageResourceReference(ItemIcons.class, itemType.name().toLowerCase() + ".png");
	}

}
