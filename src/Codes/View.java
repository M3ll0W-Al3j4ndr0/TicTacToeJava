import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class View implements Observer{
	private JFrame frame;
	private GridLayout layout;
	private JPanel boardPanel,
			overAllPanel,
			menuPanel,
			innerMenuPanel,
			innerButtonPanel,
			innerLabelPanel;
	private JButton[] buttons;
	private JButton vsCpuButton,
			twoPlayerButton,
			exitButton,
			retryButton,
			exitButton2;
	private ButtonListener buttonListener;
	private JMenuBar menuBar;
	private ImageIcon xImage,
		oImage;
	private boolean xTurn = true;
	private Controller controller;
	private JLabel menuLabel,
			testLabel;
	private GridBagConstraints labelConstraints,
					vsCpuButtonConstraints,
					exitButtonConstraints,
					twoPlayerButtonConstraints;
	private JLayeredPane layeredPanel;
	private Engine model;
	private final String MENU = "Menu panel",
				TWOPLAYER = "Two player panel";

	public View(){
		frame = new JFrame("Tic Tac Toe");
		setUpBoard();
		setUpMenu();

		overAllPanel = new JPanel();
		overAllPanel.setLayout(new CardLayout());

		layeredPanel = new JLayeredPane();
		testLabel = new JLabel("Congratulations!!");
		innerMenuPanel = new JPanel();

		innerMenuPanel.setOpaque(true);
		innerMenuPanel.setBounds(100, 200, 300, 100);
		innerMenuPanel.setLayout(new BorderLayout());
		
		//testLabel.setBackground(Color.BLUE);
		testLabel.setOpaque(true);
		//testLabel.setBounds(100, 200, 300, 100);
		//testLabel.setBounds(0, 0, 500, 500);
		testLabel.setFont(new Font("Calibri", Font.PLAIN, 20));

		innerLabelPanel = new JPanel();
		innerLabelPanel.add(testLabel);

		innerMenuPanel.add(innerLabelPanel);


		innerButtonPanel = new JPanel();
		retryButton = new JButton("Retry");
		exitButton2 = new JButton("Exit");
		exitButton2.addActionListener(new ExitButtonListener());

		innerButtonPanel.add(retryButton);
		innerButtonPanel.add(exitButton2);
		
		innerMenuPanel.add(innerButtonPanel, BorderLayout.SOUTH);

		layeredPanel.add(boardPanel, Integer.valueOf(1));
		layeredPanel.add(innerMenuPanel, Integer.valueOf(0));
		
		overAllPanel.add(menuPanel, MENU);
		//overAllPanel.add(boardPanel, TWOPLAYER);
		overAllPanel.add(layeredPanel, TWOPLAYER);

		//frame.add(boardPanel);
		//frame.add(menuPanel);
		frame.add(overAllPanel);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	public void showEndingMenu(){
		layeredPanel.setLayer(innerMenuPanel, Integer.valueOf(2));		
		for(JButton button: buttons){
			button.setEnabled(false);
		}
	}


	public void update(){
		//CardLayout cli = (CardLayout)(overAllPanel.getLayout());	
		//cli.show(overAllPanel, MENU);

		int position = model.getUpdatedPosition();
		buttons[position].setText("");
		buttons[position].setIcon(model.isXTurn()? xImage: oImage);
		buttons[position].setDisabledIcon(model.isXTurn()? xImage: oImage);
		buttons[position].removeActionListener(buttonListener);
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
		//xImage = getImage("x.png");
		xImage = getImage("src/Resources/x.png");
		//oImage = getImage("o.png");
		oImage = getImage("src/Resources/o.png");

		JMenu help = new JMenu("Help");
		menuBar.add(help);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(500, 550);
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
		boardPanel.setBounds(0, 0, 500, 500);

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

			/*
			button.setIcon(xTurn? xImage: oImage);

			xTurn = !xTurn;

			button.setText("");
			button.removeActionListener(buttonListener);
			*/
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
