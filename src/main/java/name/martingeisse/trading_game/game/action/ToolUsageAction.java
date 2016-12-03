package name.martingeisse.trading_game.game.action;

import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.item.FixedInventory;
import name.martingeisse.trading_game.game.item.NotEnoughItemsException;

/**
 * This is a base class for non-crafting actions that require one or more tools but no input materials. No items are
 * listed as expected output since the output usually varies.
 * <p>
 * This class does NOT support subclasses which reserve additional action items! Do not try this, or items will be lost.
 */
public class ToolUsageAction extends PlayerAction {

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
	public boolean onStart() {
		try {
			getPlayer().reserveActionItems(tools);
			return true;
		} catch (NotEnoughItemsException e) {
			return false;
		}
	}

	@Override
	public void onCancel() {
		getPlayer().putBackActionItems();
	}

	@Override
	public void onFinish() {
		getPlayer().putBackActionItems();
	}

}
