package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.manufacturing.Blueprint;
import name.martingeisse.trading_game.game.item.ImmutableItemStack;
import name.martingeisse.trading_game.game.item.ImmutableItemStacks;
import name.martingeisse.trading_game.game.item.NotEnoughItemsException;
import name.martingeisse.trading_game.game.player.Player;

/**
 * An action backed by a {@link Blueprint}.
 * <p>
 * TODO serialization
 */
public final class ManufacturingAction extends FixedEffortAction {

	private final Player player;
	private final Blueprint blueprint;
	private final String customName;

	public ManufacturingAction(Player player, Blueprint blueprint) {
		this(player, blueprint, null);
	}

	public ManufacturingAction(Player player, Blueprint blueprint, String customName) {
		this.player = player;
		this.blueprint = blueprint;
		this.customName = customName;
	}

	@Override
	public Action getPrerequisite() {
		return null;
	}

	@Override
	public int getTotalRequiredProgressPoints() {
		return blueprint.getRequiredProgressPoints();
	}

	@Override
	protected boolean onStart() {
		try {
			player.getInventory().removeBillOfMaterials(player.getId(), blueprint.getBillOfMaterials());
			return true;
		} catch (NotEnoughItemsException e) {
			return false;
		}
	}

	@Override
	protected void onCancel() {
		player.getInventory().add(player.getId(), blueprint.getBillOfMaterials());
	}

	@Override
	protected boolean onFinish() {
		// TODO check inventory space
		player.getInventory().add(player.getId(), blueprint.getYield());
		return true;
	}

	@Override
	public String getName() {
		return (customName == null ? getDefaultText(blueprint) : customName);
	}

	@Override
	public String getGlyphiconName() {
		return "wrench";
	}

	/**
	 * Builds the default name for a manufacturing action with the specified blueprint.
	 */
	public static String getDefaultText(Blueprint blueprint) {
		ImmutableItemStacks products = blueprint.getYield();
		if (products.getStacks().size() == 1) {
			ImmutableItemStack stack = products.getStacks().get(0);
			if (stack.getSize() == 1) {
				return "craft " + stack.getItemType();
			} else {
				return "craft " + stack.getSize() + ' ' + stack.getItemType() + 's';
			}
		} else {
			return blueprint.toString();
		}
	}

}
