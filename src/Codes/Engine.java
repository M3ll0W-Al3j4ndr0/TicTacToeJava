import java.util.ArrayList;
import java.util.Random;

public class Engine implements Subject{
	private enum BoxState{ NEUTRAL, XPLAYER, OPLAYER}
	private BoxState[] board;
	private boolean xTurn,
			aWinner,
			cpuMode;
	private BoxState winnerPlayer;
	private int numOfTurns,
			lastPosition;
	private ArrayList<Observer> observers;
	private Random randomGenerator;

	public Engine(){
		observers = new ArrayList<Observer>();
		randomGenerator = new Random();
		cpuMode = false;
		initialize();
	}

	public void setCPUMode(boolean cpuMode){
		this.cpuMode = cpuMode;
	}

	public void reset(){
		initialize();
	}

	public void registerObserver(Observer observer){
		observers.add(observer);	
	}

	public void removeObserver(Observer observer){
		int i = observers.indexOf(observer);

		if(i >= 0){
			observers.remove(i);	
		}
	}

	public void notifyObservers(){
		for(Observer observer: observers){
			observer.update();
		}
	}

	public boolean isXTurn(){
		return xTurn;	
	}

	public int getNumOfTurns(){
		return numOfTurns;
	}

	public boolean haveAWinner(){
		return aWinner;	
	}

	public boolean xPlayerWon(){
		return winnerPlayer == BoxState.XPLAYER;
	}

	public void markPosition(int position){
		board[position] = xTurn? BoxState.XPLAYER
					: BoxState.OPLAYER;
		lastPosition = position;
		notifyObservers();
		checkForWinner();

		if(xTurn){
			numOfTurns++;
		}

		xTurn = !xTurn;

		if(!xTurn && cpuMode && numOfTurns < 5){
			doCPUTurn();	
		}
	}

	public void doCPUTurn(){
		ArrayList<Integer> emptyPositions = getEmptyPositions(board);
		int size = emptyPositions.size();

		int randomPosition = randomGenerator.nextInt(size);

		markPosition(emptyPositions.get(randomPosition));
	}

	public ArrayList<Integer> getEmptyPositions(BoxState[] board){
		ArrayList<Integer> emptyPositions = new ArrayList<Integer>();

		for(int i = 0; i < 9; i++){
			if(board[i] == BoxState.NEUTRAL){
				emptyPositions.add(i);
			}
		}	

		return emptyPositions;
	}

	public int getUpdatedPosition(){
		return lastPosition;
	}

	public void checkForWinner(){
		aWinner = checkRows() || checkColumns() || checkDiagonals();
	}

	private void initialize(){
		board = new BoxState[9];
		xTurn = true;
		aWinner = false;
		numOfTurns = 0;
		lastPosition = 0;

		for(int i = 0; i < 9; i++){
			board[i] = BoxState.NEUTRAL;
		}

	}

	private boolean checkRows(){
		for(int i = 0; i < 9; i += 3){
			if(board[i] == BoxState.NEUTRAL){
				continue;
			}		
			if(board[i] != board[i+1]){
				continue;
			}
			if(board[i] != board[i+2]){
				continue;
			}

			winnerPlayer = board[i];
			return true;
		}
		
		return false;
	}

	private boolean checkColumns(){
		for(int i = 0; i < 3; i++){
			if(board[i] == BoxState.NEUTRAL){
				continue;
			}		
			if(board[i] != board[i+3]){
				continue;
			}
			if(board[i] != board[i+6]){
				continue;
			}

			winnerPlayer = board[i];
			return true;
		}

		return false;
	}

	private boolean checkDiagonals(){
		if(board[4] != BoxState.NEUTRAL){
			if(((board[0] == board[8]) && (board[0] == board[4]))
			|| ((board[2] == board[6]) && (board[2] == board[4]))){
				winnerPlayer = board[4];
				return true;
			}
		}
		return false;
	}
}
