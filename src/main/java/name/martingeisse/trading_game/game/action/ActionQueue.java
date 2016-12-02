package name.martingeisse.trading_game.game.action;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class ActionQueue extends ArrayList<ActionQueue.Entry> {

	public void enqueue(PlayerAction action) {
		enqueue(1, action);
	}

	public void enqueue(int repetitions, PlayerAction action) {
		if (repetitions > 0) {
			add(new Entry(repetitions, action));
		}
	}

	public PlayerAction dequeue() {
		if (isEmpty()) {
			return null;
		}
		Entry entry = get(0);
		if (entry.getRepetitions() == 1) {
			remove(0);
		} else {
			entry.setRepetitions(entry.getRepetitions() - 1);
		}
		return entry.getAction();
	}

	public static final class Entry {

		private int repetitions;
		private final PlayerAction action;

		public Entry(int repetitions, PlayerAction action) {
			this.repetitions = repetitions;
			this.action = action;
		}

		/**
		 * Getter method.
		 *
		 * @return the repetitions
		 */
		public int getRepetitions() {
			return repetitions;
		}

		/**
		 * Setter method.
		 *
		 * @param repetitions the repetitions
		 */
		public void setRepetitions(int repetitions) {
			this.repetitions = repetitions;
		}

		/**
		 * Getter method.
		 *
		 * @return the action
		 */
		public PlayerAction getAction() {
			return action;
		}

		@Override
		public String toString() {
			if (repetitions == 1) {
				return action.toString();
			} else {
				return action.toString() + " (" + repetitions + "x)";
			}
		}
	}

}