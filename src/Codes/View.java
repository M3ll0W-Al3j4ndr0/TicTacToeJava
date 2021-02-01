//package hisasaga.alejandro;

import java.net.URL;
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
			exitButton2,
			toMenuButton;
	private ButtonListener buttonListener;
	private JMenuBar menuBar;
	private JMenu help,
			options,
			language;
	private JMenuItem instructions,
			moreHelp,
			toMenu;
	private JRadioButtonMenuItem englishButton,
			spanishButton,
			vietnameseButton;
	private ButtonGroup languageGroup;
	private ImageIcon xImage,
		oImage,
		faceImage,
		lifeLineImage,
		appIcon;
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
	private Messages messages;
	private MessagesFactory messagesFactory;
	private Color mainMenuButtonColor,
			customBackgroundColor;

	public View(MessagesFactory messagesFactory){
		this.messagesFactory = messagesFactory;
		messages = messagesFactory.create(Language.ENGLISH);
		languageGroup = new ButtonGroup();
		mainMenuButtonColor = new Color(226, 243, 250);
		customBackgroundColor = new Color(224, 224, 224);

		String filePath = "Resources/";
		xImage = getImage(filePath + "x.png");
		oImage = getImage(filePath + "o.png");
		faceImage = getImage(filePath + "face.jpg");
		lifeLineImage = getImage(filePath + "lifeLine.png");
		appIcon = getImage(filePath + "appIcon.png");


		frame = setUpFrame();
		frame.setIconImage(appIcon.getImage());
		frame.setVisible(true);
	}

	public void updateLanguage(){
		menuLabel.setText(messages.selectMode());
		twoPlayerButton.setText(messages.twoPlayers());
		vsCpuButton.setText(messages.vsCPU());
		exitButton.setText(messages.exit());

		options.setText(messages.options());
		toMenu.setText(messages.menu());
		language.setText(messages.language());
		help.setText(messages.help());
		instructions.setText(messages.howToPlay());
		moreHelp.setText(messages.moreHelp());
		retryButton.setText(messages.retry());
		toMenuButton.setText(messages.menu());
		exitButton2.setText(messages.exit());
		setResultMessage();
		
		for(JButton button: buttons){
			if(button.getText() != ""){
				button.setText(messages.clickHere());
			}
		}
	}
	
	public void showBoard(){
		toMenu.setEnabled(true);
		CardLayout cl = (CardLayout)(overAllPanel.getLayout());
		cl.show(overAllPanel, BOARD);

	}

	public void showMainMenu(){
		toMenu.setEnabled(false);
		CardLayout cli = (CardLayout)overAllPanel.getLayout();

		cli.show(overAllPanel, MENU);
		reset();
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
		setResultMessage();
		layeredPanel.setLayer(innerMenuPanel, Integer.valueOf(2));	

		for(JButton button: buttons){
			button.setEnabled(false);
			button.setBackground(Color.GRAY);
		}
	}

	private void setResultMessage(){
		String message =
			 model.haveAWinner()? 
					model.xPlayerWon()? messages.xPlayerWin()
							: messages.oPlayerWin()
				: messages.tieGame();

		resultLabel.setText(message);

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
		URL url = getClass().getResource(file);
		ImageIcon temp = new ImageIcon(url);
		//ImageIcon temp = new ImageIcon(file);
		Image temp2 = temp.getImage();
		Image temp3 = temp2.getScaledInstance(80, 80, Image.SCALE_SMOOTH);

		return new ImageIcon(temp3);
	}

	private JMenuBar setUpMenuBar(){
		menuBar = new JMenuBar();
		menuBar.setBackground(Color.WHITE);

		menuBar = addMenuBarComponents(menuBar);

		return menuBar;
	}

	private JMenuBar addMenuBarComponents(JMenuBar menuBar){
		options = setUpOptions();
		help = setUpHelp();

		menuBar.add(options);
		menuBar.add(help);

		return menuBar;
	}

	private JMenu setUpOptions(){
		options = new JMenu(messages.options());

		options = addOptionsComponents(options);

		return options;
	}

	private JMenu addOptionsComponents(JMenu options){
		toMenu = setUpToMenu();
		language = setUpLanguage();

		options.add(toMenu);
		options.add(language);

		return options;
	}

	private JMenuItem setUpToMenu(){
		toMenu = new JMenuItem(messages.menu());
		toMenu.setEnabled(false);
		toMenu.setBackground(Color.WHITE);
		toMenu.addActionListener(new ToMenuButtonListener());
	
		return toMenu;
	}

	private JMenu setUpLanguage(){
		language = new JMenu(messages.language());
		language.setBackground(Color.WHITE); 
		language.setOpaque(true);

		language = addLanguageComponents(language);

		return language;
	}


	private JMenu addLanguageComponents(JMenu language){
		englishButton = new JRadioButtonMenuItem("English", true);
		englishButton.setBackground(Color.WHITE);
		spanishButton = new JRadioButtonMenuItem("Español");
		spanishButton.setBackground(Color.WHITE);
		vietnameseButton = new JRadioButtonMenuItem("Tiếng Việt");
		vietnameseButton.setBackground(Color.WHITE);

		englishButton.addActionListener(new EnglishListener());
		spanishButton.addActionListener(new SpanishListener());
		vietnameseButton.addActionListener(new VietnameseListener());

		languageGroup.add(englishButton);
		languageGroup.add(spanishButton);
		languageGroup.add(vietnameseButton);

		language.add(englishButton);
		language.add(spanishButton);
		language.add(vietnameseButton);

		return language;
	}

	private JMenu setUpHelp(){
		help = new JMenu(messages.help());
		
		help = addHelpComponents(help);

		return help;
	}

	private JMenu addHelpComponents(JMenu help){
		instructions = setUpInstructions();
		moreHelp = setUpMoreHelp();

		help.add(instructions);
		help.add(moreHelp);

		return help;
	}

	private JMenuItem setUpInstructions(){
		instructions = new JMenuItem(messages.howToPlay());
		instructions.addActionListener(new InstructionsListener());
		instructions.setBackground(Color.WHITE);

		return instructions;	
	}

	private JMenuItem setUpMoreHelp(){
		moreHelp = new JMenuItem(messages.moreHelp());
		moreHelp.addActionListener(new MoreHelpListener());
		moreHelp.setBackground(Color.WHITE);
		return moreHelp;
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
		menuPanel.setBackground(customBackgroundColor); /// <- Testing

		return menuPanel;
	}

	private JPanel addMenuPanelComponents(JPanel menuPanel){
		menuLabel = setUpMenuLabel();
		labelConstraints = setUpLabelConstraints();

		vsCpuButton = setUpVsCpuButton();
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
		menuLabel = new JLabel(messages.selectMode());
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

	private JButton setUpVsCpuButton(){
		vsCpuButton = new JButton(messages.vsCPU());
		vsCpuButton.setBackground(mainMenuButtonColor);
		vsCpuButton.addActionListener(new VsCpuButtonListener());
		vsCpuButton.addMouseListener(new VsCpuButtonMouseListener());

		return vsCpuButton;
	}

	private GridBagConstraints setUpVsCpuConstraints(){
		vsCpuButtonConstraints = new GridBagConstraints();
		vsCpuButtonConstraints.gridx = 0;
		vsCpuButtonConstraints.gridy = 1;

		return vsCpuButtonConstraints;
	}

	private JButton setUpExitButton(){
		exitButton = new JButton(messages.exit());
		exitButton.setBackground(mainMenuButtonColor);
		exitButton.addActionListener(new ExitButtonListener());
		exitButton.addMouseListener(new ExitButtonMouseListener());

		return exitButton;
	}

	private GridBagConstraints setUpExitButtonConstraints(){
		exitButtonConstraints = new GridBagConstraints();
		exitButtonConstraints.gridx = 1;
		exitButtonConstraints.gridy = 2;

		return exitButtonConstraints;
	}

	private JButton setUpTwoPlayerButton(){
		twoPlayerButton = new JButton(messages.twoPlayers());
		twoPlayerButton.setBackground(mainMenuButtonColor);
		twoPlayerButton.addActionListener(new TwoPlayerButtonListener());
		twoPlayerButton.addMouseListener(new TwoPlayerButtonMouseListener());

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
		boardPanel.setLayout(new GridLayout(3, 3, 5, 5));
		boardPanel.setBackground(Color.BLACK);
		boardPanel.setBorder(BorderFactory.createLineBorder(Color.black, 5));
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
		JButton button = new JButton(messages.clickHere());
		button.setBackground(Color.WHITE);
		button.setFocusable(false);
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
		innerMenuPanel.setBackground(customBackgroundColor);
		innerMenuPanel.setBounds(100, 200, 300, 100);
		innerMenuPanel.setBorder(BorderFactory.createLineBorder(Color.black));
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
		innerLabelPanel.setOpaque(false);

		resultLabel = setUpResultLabel();

		innerLabelPanel.add(resultLabel);

		return innerLabelPanel;
	}
	
	private JLabel setUpResultLabel(){
		resultLabel = new JLabel("Congratulations!!");
		resultLabel.setFont(new Font("Calibri", Font.PLAIN, 20));

		return resultLabel;
	}

	private JPanel setUpInnerButtonPanel(){
		innerButtonPanel = new JPanel();
		innerButtonPanel.setOpaque(false);

		innerButtonPanel = addInnerButtonPanelComponents(innerButtonPanel);

		return innerButtonPanel;
	}

	private JPanel addInnerButtonPanelComponents(JPanel innerButtonPanel){
		retryButton = setUpRetryButton();
		toMenuButton = setUpToMenuButton();
		exitButton2 = setUpExitButton2();

		innerButtonPanel.add(retryButton);
		innerButtonPanel.add(toMenuButton);
		innerButtonPanel.add(exitButton2);

		return innerButtonPanel;
	}

	private JButton setUpRetryButton(){
		retryButton = new JButton(messages.retry());
		retryButton.setBackground(mainMenuButtonColor);
		retryButton.addActionListener(new RetryButtonListener());
		retryButton.addMouseListener(new RetryButtonMouseListener());

		return retryButton;
	}

	private JButton setUpToMenuButton(){
		toMenuButton = new JButton(messages.menu());
		toMenuButton.setBackground(mainMenuButtonColor);
		toMenuButton.addActionListener(new ToMenuButtonListener());
		toMenuButton.addMouseListener(new ToMenuButtonMouseListener());

		return toMenuButton;
	}

	private JButton setUpExitButton2(){
		exitButton2 = new JButton(messages.exit());
		exitButton2.setBackground(mainMenuButtonColor);
		exitButton2.addActionListener(new ExitButtonListener());
		exitButton2.addMouseListener(new ExitButtonMouseListener());

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
			controller.gameStart();
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
			controller.gameStart();
		}
	}

	private class ToMenuButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			controller.toMenu();
		}
	}

	private class InstructionsListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JOptionPane.showMessageDialog(frame, 
						messages.instructions(),
						messages.howToPlay(),
						JOptionPane.INFORMATION_MESSAGE,
						faceImage);
		}

	}

	private class MoreHelpListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JOptionPane.showMessageDialog(frame, 
						messages.moreHelpMessage(),
						messages.moreHelp(),
						JOptionPane.INFORMATION_MESSAGE,
						lifeLineImage);
		}

	}

	private class EnglishListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			messages = messagesFactory.create(Language.ENGLISH);
			controller.updateLanguage();
		}
	}

	private class SpanishListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			messages = messagesFactory.create(Language.SPANISH);
			controller.updateLanguage();
		}
	}

	private class VietnameseListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			messages = messagesFactory.create(Language.VIETNAMESE);
			controller.updateLanguage();
		}
	}
	
	private class VsCpuButtonMouseListener extends MouseAdapter{
		public void mouseEntered(MouseEvent e){
			JButton button = (JButton)e.getSource();

			button.setBackground(Color.GREEN);
		}

		public void mouseExited(MouseEvent e){
			JButton button = (JButton)e.getSource();

			button.setBackground(mainMenuButtonColor);
		}
	}

	private class TwoPlayerButtonMouseListener extends MouseAdapter{
		public void mouseEntered(MouseEvent e){
			JButton button = (JButton)e.getSource();

			button.setBackground(new Color(69, 140, 204));
		}

		public void mouseExited(MouseEvent e){
			JButton button = (JButton)e.getSource();

			button.setBackground(mainMenuButtonColor);
		}
	}
	
	private class ExitButtonMouseListener extends MouseAdapter{
		public void mouseEntered(MouseEvent e){
			JButton button = (JButton)e.getSource();

			button.setBackground(new Color(240, 86, 88));
		}

		public void mouseExited(MouseEvent e){
			JButton button = (JButton)e.getSource();

			button.setBackground(mainMenuButtonColor);
		}
	}

	private class RetryButtonMouseListener extends MouseAdapter{
		public void mouseEntered(MouseEvent e){
			JButton button = (JButton)e.getSource();

			button.setBackground(new Color(248, 242, 132));
		}

		public void mouseExited(MouseEvent e){
			JButton button = (JButton)e.getSource();

			button.setBackground(mainMenuButtonColor);
		}
	}

	private class ToMenuButtonMouseListener extends MouseAdapter{
		public void mouseEntered(MouseEvent e){
			JButton button = (JButton)e.getSource();

			button.setBackground(new Color(249, 176, 126));
		}

		public void mouseExited(MouseEvent e){
			JButton button = (JButton)e.getSource();

			button.setBackground(mainMenuButtonColor);
		}
	}
}
