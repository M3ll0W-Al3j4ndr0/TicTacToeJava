//package hisasaga.alejandro;

public class Driver{
	public static void main(String[] args){
		Engine ticTacToe = new Engine();
		View viewer = new View(new MessagesFactory());
		Controller controller = new Controller(viewer, ticTacToe);
	}
}
