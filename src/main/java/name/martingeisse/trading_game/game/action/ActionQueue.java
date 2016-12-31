package name.martingeisse.trading_game.game.action;

import java.util.ArrayList;

/**
 *
 */
public final class ActionQueue extends ArrayList<Action> {

	public ActionExecution startNext() {
		if (isEmpty()) {
			return null;
		}
		Action action = get(0);
		Action prerequisite = action.getPrerequisite();
		if (prerequisite != null) {
			return new PrerequisiteActionExecutionDecorator(prerequisite.startExecution());
		}
		remove(0);
		return action.startExecution();
	}

}
