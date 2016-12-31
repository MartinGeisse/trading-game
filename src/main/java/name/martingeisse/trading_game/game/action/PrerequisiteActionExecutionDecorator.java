package name.martingeisse.trading_game.game.action;

/**
 *
 */
public class PrerequisiteActionExecutionDecorator implements ActionExecution {

	private final ActionExecution decorated;

	public PrerequisiteActionExecutionDecorator(ActionExecution decorated) {
		this.decorated = decorated;
	}

	@Override
	public String getName() {
		return decorated.getName();
	}

	@Override
	public ProgressSnapshot getProgress() {
		return decorated.getProgress();
	}

	@Override
	public Integer getRemainingTime() {
		return decorated.getRemainingTime();
	}

	@Override
	public void tick() {
		decorated.tick();
	}

	@Override
	public void cancel() {
		decorated.cancel();
	}

	@Override
	public boolean isFinishable() {
		return decorated.isFinishable();
	}

	@Override
	public void finish() {
		decorated.finish();
	}

	@Override
	public boolean isPrerequisite() {
		return true;
	}

}
