//package hisasaga.alejandro;
import javax.swing.UIManager;

public class Driver{
	public static void main(String[] args){
		try{
			UIManager.setLookAndFeel(
				UIManager.getCrossPlatformLookAndFeelClassName());

		}
		catch (Exception e){
			System.out.println("Failed to set LaF");
		}

		Engine ticTacToe = new Engine();
		View viewer = new View(new Messages());
		Controller controller = new Controller(viewer, ticTacToe);
	}
}
