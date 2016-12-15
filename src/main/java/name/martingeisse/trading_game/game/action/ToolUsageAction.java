package name.martingeisse.trading_game.game.action;

import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.item.FixedInventory;
import name.martingeisse.trading_game.game.item.NotEnoughItemsException;

/**
 * This is a base class for non-crafting actions that require one or more tools but no input materials. No items are
 * listed as expected output since the output usually varies.
 * <p>
 * This class does NOT support subclasses which reserve additional action items! Do not try this, or items will be lost.
 *
 * TODO remove. In the future, there won't be tools but machines; those are required to be installed at manufacturing
 * sites instead of being present in the player's inventory.
 */
public abstract class ToolUsageAction extends PlayerAction {

	private final FixedInventory tools;

	public ToolUsageAction(Player player, int requiredProgressPoints, FixedInventory tools) {
		super(player, requiredProgressPoints);
		this.tools = tools;
	}

	/**
	 * Getter method.
	 *
	 * @return the tools
	 */
	public final FixedInventory getTools() {
		return tools;
	}

	@Override
	public final boolean onStart() {
		try {
			getPlayer().reserveActionItems(tools);
			onStartToolUsage();
			return true;
		} catch (NotEnoughItemsException e) {
			return false;
		}
	}

	protected void onStartToolUsage() {
	}

	@Override
	public final void onCancel() {
		getPlayer().putBackActionItems();
		onCancelToolUsage();
	}

	protected void onCancelToolUsage() {
	}

	@Override
	public final void onFinish() {
		getPlayer().putBackActionItems();
		onFinishToolUsage();
	}

	protected abstract void onFinishToolUsage();


}
