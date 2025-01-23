package view;

public class StateRunning implements StateViewCommand {

	ViewCommand view;

	public StateRunning(ViewCommand view) {

		this.view = view;

		this.view.getInitChoice().setEnabled(false);
		this.view.getStepChoice().setEnabled(false);
		this.view.getPlayChoice().setEnabled(false);
		this.view.getPauseChoice().setEnabled(true);

	}

	@Override
	public void clickRestart() {

	}

	@Override
	public void clickStep() {

	}

	@Override
	public void clickPlay() {

	}

	@Override
	public void clickPause() {
		this.view.setState(new StateWaiting(this.view));

	}

}
