package name.martingeisse.trading_game.game.item;

/**
 * Provides a serialized representation of item types as strings.
 *
 * NOTE: Various pieces of code currently depend on the serialized version of an item type being unambiguous. This is
 * the case for the current serializer.
 */
public interface ItemTypeSerializer {

	public String serializeItemType(ItemType itemType);

	public ItemType deserializeItemType(String serializedItemType);

}
