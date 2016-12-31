package name.martingeisse.trading_game.game.action;

import java.util.ArrayList;

/**
 *
 */
public final class ActionQueue extends ArrayList<ActionQueue.Entry> {

	public void enqueue(Action action) {
		enqueue(1, action);
	}

	public void enqueue(int repetitions, Action action) {
		if (repetitions > 0) {
			add(new Entry(repetitions, action));
		}
	}

	public ActionExecution startNext() {
		if (isEmpty()) {
			return null;
		}
		Entry entry = get(0);
		Action prerequisite = entry.getAction().getPrerequisite();
		if (prerequisite != null) {
			return new PrerequisiteActionExecutionDecorator(prerequisite.startExecution());
		}
		if (entry.getRepetitions() == 1) {
			remove(0);
		} else {
			entry.setRepetitions(entry.getRepetitions() - 1);
		}
		return entry.getAction().startExecution();
	}

	public static final class Entry {

		private int repetitions;
		private final Action action;

		public Entry(int repetitions, Action action) {
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
		public Action getAction() {
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
