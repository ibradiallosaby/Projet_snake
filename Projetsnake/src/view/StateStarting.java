package view;

public class StateStarting implements StateViewCommand {

	ViewCommand view;

	public StateStarting(ViewCommand view) {

		this.view = view;

		this.view.getInitChoice().setEnabled(false);
		this.view.getStepChoice().setEnabled(true);
		this.view.getPlayChoice().setEnabled(true);
		this.view.getPauseChoice().setEnabled(false);

	}

	@Override
	public void clickRestart() {
		System.out.println("Already Initialized");
	}

	@Override
	public void clickStep() {

		this.view.setState(new StateWaiting(this.view));

	}

	@Override
	public void clickPlay() {

		this.view.setState(new StateRunning(this.view));

	}

	@Override
	public void clickPause() {
		System.out.println("Game not launched");
	}

}
