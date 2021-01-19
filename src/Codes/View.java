import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class View implements Observer{
	private JFrame frame;
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
	private Controller controller;
	private JLabel menuLabel,
			resultLabel;
	private GridBagConstraints labelConstraints,
					vsCpuButtonConstraints,
					exitButtonConstraints,
					twoPlayerButtonConstraints;
	private JLayeredPane layeredPanel;
	private Engine model;
	private final String MENU = "Menu panel",
				BOARD = "Board panel";

	public View(){
		xImage = getImage("src/Resources/x.png");
		oImage = getImage("src/Resources/o.png");

		frame = setUpFrame();
		frame.setVisible(true);
	}

	public void reset(){
		layeredPanel.setLayer(innerMenuPanel, Integer.valueOf(0));
		boardPanel.removeAll();
		boardPanel = addBoardPanelComponents(boardPanel);
		boardPanel.validate();
	}

	public void setController(Controller controller){
		this.controller = controller;
	}

	public void setModel(Engine model){
		this.model = model;
		((Subject)model).registerObserver(this);
	}

	public void update(){
		int position = model.getUpdatedPosition();
		buttons[position].setText("");
		buttons[position].setIcon(model.isXTurn()? xImage: oImage);
		buttons[position].setDisabledIcon(model.isXTurn()? xImage: oImage);
		buttons[position].removeActionListener(buttonListener);
	}

	public void showEndingMenu(){
		String message = model.haveAWinner()? 
					model.xPlayerWon()? "O you suck!"
							: "Nice one O!"
					: "It's a Tie!!";
		resultLabel.setText(message);
		layeredPanel.setLayer(innerMenuPanel, Integer.valueOf(2));	

		for(JButton button: buttons){
			button.setEnabled(false);
		}
	}

	private JFrame setUpFrame(){
		frame = setUpFrameDetails();
		frame = addFrameComponents(frame);

		return frame;
	}

	private JFrame setUpFrameDetails(){
		frame = new JFrame("Tic Tac Toe");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(500, 550);

		return frame;
	}

	private JFrame addFrameComponents(JFrame frame){
		menuBar = setUpMenuBar();
		overAllPanel = setUpOverAllPanel();

		frame.setJMenuBar(menuBar);
		frame.add(overAllPanel);

		return frame;
	}

	private ImageIcon getImage(String file){
		ImageIcon temp = new ImageIcon(file);
		Image temp2 = temp.getImage();
		Image temp3 = temp2.getScaledInstance(80, 80, Image.SCALE_SMOOTH);

		return new ImageIcon(temp3);
	}

	private JMenuBar setUpMenuBar(){
		menuBar = new JMenuBar();
		menuBar = addUpMenuBarComponents(menuBar);

		return menuBar;
	}

	private JMenuBar addUpMenuBarComponents(JMenuBar menuBar){
		JMenu help = new JMenu("Help");
		menuBar.add(help);

		return menuBar;
	}

	private JPanel setUpOverAllPanel(){
		overAllPanel = setUpOverAllPanelDetails();
		
		overAllPanel = addOverAllPanelComponents(overAllPanel);

		return overAllPanel;
	}

	private JPanel setUpOverAllPanelDetails(){
		overAllPanel = new JPanel();
		overAllPanel.setLayout(new CardLayout());

		return overAllPanel;
	}

	private JPanel addOverAllPanelComponents(JPanel overAllPanel){
		menuPanel = setUpMenuPanel();
		layeredPanel = setUpLayeredPanel();
	
		overAllPanel.add(menuPanel, MENU);
		overAllPanel.add(layeredPanel, BOARD);

		return overAllPanel;
	}

	private JPanel setUpMenuPanel(){
		menuPanel = setUpMenuPanelDetails();

		menuPanel = addMenuPanelComponents(menuPanel);

		return menuPanel;
	}

	private JPanel setUpMenuPanelDetails(){
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridBagLayout());

		return menuPanel;
	}

	private JPanel addMenuPanelComponents(JPanel menuPanel){
		menuLabel = setUpMenuLabel();
		labelConstraints = setUpLabelConstraints();

		vsCpuButton = new JButton("vs CPU");
		vsCpuButton.addActionListener(new VsCpuButtonListener());
		vsCpuButtonConstraints = setUpVsCpuConstraints();

		exitButton = setUpExitButton();
		exitButtonConstraints = setUpExitButtonConstraints();
	
		twoPlayerButton = setUpTwoPlayerButton();
		twoPlayerButtonConstraints = setUpTwoPlayerButtonConstraints();

		menuPanel.add(menuLabel, labelConstraints);
		menuPanel.add(vsCpuButton, vsCpuButtonConstraints);
		menuPanel.add(exitButton, exitButtonConstraints);
		menuPanel.add(twoPlayerButton, twoPlayerButtonConstraints);

		return menuPanel;
	}

	private JLabel setUpMenuLabel(){
		menuLabel = new JLabel("Select Mode:");
		menuLabel.setFont(new Font("Calibri", Font.PLAIN, 40));

		return menuLabel;
	}

	private GridBagConstraints setUpLabelConstraints(){
		labelConstraints = new GridBagConstraints();
		labelConstraints.gridx = 0;
		labelConstraints.gridy = 0;
		labelConstraints.gridwidth = 3;
		labelConstraints.insets = new Insets(0, 0, 100, 0);

		return labelConstraints;
	}

	private GridBagConstraints setUpVsCpuConstraints(){
		vsCpuButtonConstraints = new GridBagConstraints();
		vsCpuButtonConstraints.gridx = 0;
		vsCpuButtonConstraints.gridy = 1;

		return vsCpuButtonConstraints;
	}

	private JButton setUpExitButton(){
		exitButton = new JButton("Exit");
		exitButton.addActionListener(new ExitButtonListener());

		return exitButton;
	}

	private GridBagConstraints setUpExitButtonConstraints(){
		exitButtonConstraints = new GridBagConstraints();
		exitButtonConstraints.gridx = 1;
		exitButtonConstraints.gridy = 2;

		return exitButtonConstraints;
	}

	private JButton setUpTwoPlayerButton(){
		twoPlayerButton = new JButton("2 Players");
		twoPlayerButton.addActionListener(new TwoPlayerButtonListener());

		return twoPlayerButton;
	}

	private GridBagConstraints setUpTwoPlayerButtonConstraints(){
		twoPlayerButtonConstraints = new GridBagConstraints();
		twoPlayerButtonConstraints.gridx = 2;
		twoPlayerButtonConstraints.gridy = 1;

		return twoPlayerButtonConstraints;
	}

	private JLayeredPane setUpLayeredPanel(){
		layeredPanel = new JLayeredPane();

		layeredPanel = addLayeredPanelComponents(layeredPanel);

		return layeredPanel;
	}

	private JLayeredPane addLayeredPanelComponents(JLayeredPane layeredPanel){
		boardPanel = setUpBoardPanel();
		innerMenuPanel = setUpInnerMenuPanel();
	
		layeredPanel.add(boardPanel, Integer.valueOf(1));
		layeredPanel.add(innerMenuPanel, Integer.valueOf(0));

		return layeredPanel;
	}

	private JPanel setUpBoardPanel(){
		boardPanel = setUpBoardPanelDetails();

		boardPanel = addBoardPanelComponents(boardPanel);

		return boardPanel;
	}

	private JPanel setUpBoardPanelDetails(){
		boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(3, 3));
		boardPanel.setBackground(Color.BLACK);
		boardPanel.setBounds(0, 0, 500, 500);

		return boardPanel;
	}

	private JPanel addBoardPanelComponents(JPanel boardPanel){
		buttons = setUpButtons();

		boardPanel = addButtonsToBoardPanel(boardPanel, buttons);

		return boardPanel;
	}

	private JButton[] setUpButtons(){
		buttons = new JButton[9];
		buttonListener = new ButtonListener();

		for(int i = 0; i < 9; i++){
			buttons[i] = setUpButton(buttonListener);
		}

		return buttons;
	}

	private JButton setUpButton(ButtonListener buttonListener){
		JButton button = new JButton("Click here!!");
		button.addActionListener(buttonListener);

		return button;
	}

	private JPanel addButtonsToBoardPanel(JPanel boardPanel, JButton[] buttons){
		for(JButton button: buttons){
			boardPanel.add(button);
		}
		
		return boardPanel;
	}

	private JPanel setUpInnerMenuPanel(){
		innerMenuPanel = setUpInnerMenuPanelDetails();
		innerMenuPanel = addInnerMenuPanelComponents(innerMenuPanel);
		
		return innerMenuPanel;
	}

	private JPanel setUpInnerMenuPanelDetails(){
		innerMenuPanel = new JPanel();
		innerMenuPanel.setOpaque(true);
		innerMenuPanel.setBounds(100, 200, 300, 100);
		innerMenuPanel.setLayout(new BorderLayout());

		return innerMenuPanel;
	}

	private JPanel addInnerMenuPanelComponents(JPanel innerMenuPanel){
		innerLabelPanel = setUpInnerLabelPanel();
		innerButtonPanel = setUpInnerButtonPanel();

		innerMenuPanel.add(innerLabelPanel);
		innerMenuPanel.add(innerButtonPanel, BorderLayout.SOUTH);

		return innerMenuPanel;
	}

	private JPanel setUpInnerLabelPanel(){
		innerLabelPanel = new JPanel();

		resultLabel = setUpResultLabel();

		innerLabelPanel.add(resultLabel);

		return innerLabelPanel;
	}
	
	private JLabel setUpResultLabel(){
		resultLabel = new JLabel("Congratulations!!");
		resultLabel.setOpaque(true);
		resultLabel.setFont(new Font("Calibri", Font.PLAIN, 20));

		return resultLabel;
	}

	private JPanel setUpInnerButtonPanel(){
		innerButtonPanel = new JPanel();

		innerButtonPanel = addInnerButtonPanelComponents(innerButtonPanel);

		return innerButtonPanel;
	}

	private JPanel addInnerButtonPanelComponents(JPanel innerButtonPanel){
		retryButton = setUpRetryButton();
		exitButton2 = setUpExitButton2();

		innerButtonPanel.add(retryButton);
		innerButtonPanel.add(exitButton2);

		return innerButtonPanel;
	}

	private JButton setUpRetryButton(){
		retryButton = new JButton("Retry");
		retryButton.addActionListener(new RetryButtonListener());
		return retryButton;
	}

	private JButton setUpExitButton2(){
		exitButton2 = new JButton("Exit");
		exitButton2.addActionListener(new ExitButtonListener());

		return exitButton2;
	}

	private class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JButton button = (JButton)e.getSource();

			for(int i = 0; i < 9; i++){
				if(button == buttons[i]){
					controller.playerEnteredPosition(i);
					break;
				}
			}
		}
	}

	private class ExitButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			System.exit(0);
		}
	}

	private class TwoPlayerButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			controller.cpuMode(false);	
			CardLayout cl = (CardLayout)(overAllPanel.getLayout());
			cl.show(overAllPanel, BOARD);
		}
	}
		
	private class RetryButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			controller.reset();	
		}
	}

	private class VsCpuButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			controller.cpuMode(true);	
			CardLayout cl = (CardLayout)(overAllPanel.getLayout());
			cl.show(overAllPanel, BOARD);
		}
	}
}
