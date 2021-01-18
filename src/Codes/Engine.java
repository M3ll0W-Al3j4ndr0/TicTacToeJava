import java.util.ArrayList;

public class Engine implements Subject{
	private enum BoxState{ NEUTRAL, XPLAYER, OPLAYER}
	private BoxState[] board;
	private boolean xTurn,
			aWinner;
	private BoxState winnerPlayer;
	private int numOfTurns,
			lastPosition;
	private ArrayList<Observer> observers;

	public Engine(){
		observers = new ArrayList<Observer>();
		board = new BoxState[9];
		xTurn = true;
		aWinner = false;
		numOfTurns = 0;
		lastPosition = 0;

		for(int i = 0; i < 9; i++){
			board[i] = BoxState.NEUTRAL;
		}
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

		xTurn = !xTurn;
	}

	public int getUpdatedPosition(){
		return lastPosition;
	}

	//In development..
	public void checkForWinner(){
		checkRows();
		checkColumns();
		checkDiagonals();
		if(aWinner){
			System.out.println("A winner is announced");
		}
	}

	private void checkRows(){
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

			aWinner = true;
			winnerPlayer = board[i];
			break;
		}
	}

	private void checkColumns(){
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

			aWinner = true;
			winnerPlayer = board[i];
			break;
		}
	}

	private void checkDiagonals(){
		if(board[4] != BoxState.NEUTRAL){
			if(((board[0] == board[8]) && (board[0] == board[4]))
			|| ((board[2] == board[6]) && (board[2] == board[4]))){
				aWinner = true;
				winnerPlayer = board[4];
			}
		}
	}

	//Test method
	public void printBoard(){
		for(BoxState state: board){
			System.out.println(state);
		}
	}
}
