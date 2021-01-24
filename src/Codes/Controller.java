//package hisasaga.alejandro;

public class Controller{
	private View viewer;
	private Engine model;

	public Controller(View viewer, Engine model){
		this.viewer = viewer;
		this.model = model;	

		viewer.setController(this);
		viewer.setModel(model);
	}

	public void reset(){
		model.reset();
		viewer.reset();
	}

	public void playerEnteredPosition(int position){
		model.markPosition(position);

		if(model.haveAWinner() || model.getNumOfTurns() == 5){
			viewer.showEndingMenu();
		}
	}

	public void cpuMode(boolean enable){
		model.setCPUMode(enable);
	}

	public void toMenu(){
		viewer.showMainMenu();
		model.reset();
	}
}
