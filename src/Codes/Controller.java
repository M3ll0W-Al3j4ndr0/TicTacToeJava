public class Controller{
	private View viewer;
	private Engine model;

	public Controller(View viewer, Engine model){
		this.viewer = viewer;
		this.model = model;	

		viewer.setController(this);
		viewer.setModel(model);
	}

	public void playerEnteredPosition(int position){
		model.markPosition(position);
		//model.checkForWinner();

		if(model.haveAWinner()){
			//System.exit(0);
			//model.notifyObservers();
			viewer.showEndingMenu();
		}
	}
}
