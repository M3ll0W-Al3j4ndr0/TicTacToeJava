import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class View implements Observer{
	private JFrame frame;
	private GridLayout layout;
	private JPanel boardPanel,
			overAllPanel,
			menuPanel;
	private JButton[] buttons;
	private JButton vsCpuButton,
			twoPlayerButton,
			exitButton;
	private ButtonListener buttonListener;
	private JMenuBar menuBar;
	private ImageIcon xImage,
		oImage;
	private boolean xTurn = true;
	private Controller controller;
	private JLabel menuLabel;
	private GridBagConstraints labelConstraints,
					vsCpuButtonConstraints,
					exitButtonConstraints,
					twoPlayerButtonConstraints;
	private Engine model;
	private final String MENU = "Menu panel",
				TWOPLAYER = "Two player panel";

	public View(){
		frame = new JFrame("Tic Tac Toe");
		setUpBoard();
		setUpMenu();

		overAllPanel = new JPanel();
		overAllPanel.setLayout(new CardLayout());
		
		overAllPanel.add(menuPanel, MENU);
		overAllPanel.add(boardPanel, TWOPLAYER);

		//frame.add(boardPanel);
		//frame.add(menuPanel);
		frame.add(overAllPanel);
		frame.setVisible(true);
	}

	public void update(){
		CardLayout cli = (CardLayout)(overAllPanel.getLayout());	
		cli.show(overAllPanel, MENU);
	}

	public void setModel(Engine model){
		this.model = model;
		((Subject)model).registerObserver(this);
	}

	private void setUpMenu(){
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridBagLayout());

		menuLabel = new JLabel("Select Mode:");
		menuLabel.setFont(new Font("Calibri", Font.PLAIN, 40));

		labelConstraints = new GridBagConstraints();
		labelConstraints.gridx = 0;
		labelConstraints.gridy = 0;
		labelConstraints.gridwidth = 3;
		labelConstraints.insets = new Insets(0, 0, 100, 0);

		menuPanel.add(menuLabel, labelConstraints);

		vsCpuButton = new JButton("vs CPU");
		vsCpuButtonConstraints = new GridBagConstraints();
		
		vsCpuButtonConstraints.gridx = 0;
		vsCpuButtonConstraints.gridy = 1;
		
		menuPanel.add(vsCpuButton, vsCpuButtonConstraints);

		exitButton = new JButton("Exit");
		exitButton.addActionListener(new ExitButtonListener());
		exitButtonConstraints = new GridBagConstraints();

		exitButtonConstraints.gridx = 1;
		exitButtonConstraints.gridy = 2;
	
		menuPanel.add(exitButton, exitButtonConstraints);


		twoPlayerButton = new JButton("2 Players");
		twoPlayerButton.addActionListener(new TwoPlayerButtonListener());
		twoPlayerButtonConstraints = new GridBagConstraints();

		twoPlayerButtonConstraints.gridx = 2;
		twoPlayerButtonConstraints.gridy = 1;

		menuPanel.add(twoPlayerButton, twoPlayerButtonConstraints);

		//menuPanel.setBackground(Color.ORANGE);
	}

	private void setUpBoard(){
		layout = new GridLayout(3, 3);
		boardPanel = new JPanel();
		menuBar = new JMenuBar();
		//System.setProperty("apple.laf.useScreenMenuBar", "true");
		xImage = getImage("x.png");
		oImage = getImage("o.png");

		JMenu help = new JMenu("Help");
		menuBar.add(help);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(500, 500);
		frame.setJMenuBar(menuBar);

		buttons = new JButton[9];
		buttonListener = new ButtonListener();
		boardPanel.setLayout(layout);

		for(int i = 0; i < 9; i++){
			buttons[i] = new JButton("Click here!!");
			buttons[i].addActionListener(buttonListener);
			boardPanel.add(buttons[i]);
		}

		boardPanel.setBackground(Color.BLACK);

	}

	public void setController(Controller controller){
		this.controller = controller;
	}

	private ImageIcon getImage(String file){
		ImageIcon temp = new ImageIcon(file);
		Image temp2 = temp.getImage();
		Image temp3 = temp2.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		return new ImageIcon(temp3);
	}

	private class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JButton button = (JButton)e.getSource();

			for(int i = 0; i < 9; i++){
				if(button == buttons[i]){
					System.out.println("Button: " + i);
					controller.playerEnteredPosition(i);
					break;
				}
			}

			button.setIcon(xTurn? xImage: oImage);

			xTurn = !xTurn;

			button.setText("");
			button.removeActionListener(buttonListener);
		}
	}

	private class ExitButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			System.exit(0);
		}
	}

	private class TwoPlayerButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			CardLayout cl = (CardLayout)(overAllPanel.getLayout());
			cl.show(overAllPanel, TWOPLAYER);
		}
	}
}
