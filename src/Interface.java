import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

// This is the code for the interface of the game, does not include the game itself.
/**
 * Creates a new window. 
 * @author Alan Hu
 *
 */
public class Interface extends JFrame {
	// Creates the window
	JFrame window = new JFrame ("The Maze Game");
	int windowWidth = 800; // Width of window, here default as 600.
	int windowHeight = 800; // Height of window, here default as 600.
	
	// Panel for the entire screen
	JPanel screen = new JPanel();
	
	// Game opens into mainMenu
	JPanel mainMenu = new JPanel();
	
	// Buttons in mainMenu
	JLabel mazeTitle = new JLabel ("Maze", JLabel.CENTER);
	JButton startNewGame = new JButton("Start New Game");
	JButton exitGame = new JButton("Exit");
	
	// Within the game, there is a Menu at top, and main game at bottom
	
	MazeGame gameField;
		
	JPanel gameMenu = new JPanel();
	// Buttons in in-game menu
	JButton goToMenu = new JButton("Back to Menu");
	
	/**
	 * Creates the window, sets basic layout for everything.
	 */
	public Interface() {
		// Window Properties
		window.setSize(windowWidth, windowHeight);
		window.setLocation(400,100);
		window.setResizable(true);
		window.setLayout(new BorderLayout());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Panels
		// ------ mainMenu layout ------ //
		// mainMenu uses boxlayout
		mainMenu.setLayout(new BoxLayout(mainMenu, BoxLayout.Y_AXIS));
		mainMenu.setBackground(Color.DARK_GRAY);
		
		// Adds expandable/retractable space at top
		mainMenu.add(Box.createVerticalGlue());
		mainMenu.add(Box.createVerticalGlue());
		
		// Font is the best way to increase size
		mainMenu.add(mazeTitle);
		mazeTitle.setAlignmentX(CENTER_ALIGNMENT);
		mazeTitle.setForeground(new Color(255, 255, 255));
		mazeTitle.setFont(new Font("Sans Serif", Font.BOLD, 96));
		
		// Bigger space: is there a way to customize glue?
		mainMenu.add(Box.createVerticalGlue());
		mainMenu.add(Box.createVerticalGlue());
		mainMenu.add(Box.createVerticalGlue());

		// Add start new game button
		mainMenu.add(startNewGame);
		startNewGame.setAlignmentX(CENTER_ALIGNMENT);
		startNewGame.setPreferredSize(new Dimension(200,50));
		startNewGame.setFont(new Font("Sans Serif", Font.BOLD, 24));
		
		// Fixed (non expandable) space between the two buttons
		mainMenu.add(Box.createRigidArea(new Dimension(0,50)));
		
		// Add exit game button
		mainMenu.add(exitGame);
		exitGame.setAlignmentX(CENTER_ALIGNMENT);
		exitGame.setPreferredSize(new Dimension(20,50));
		exitGame.setFont(new Font("Sans Serif", Font.BOLD, 24));
		
		//Makes sure there space at the bottom, because glue.
		mainMenu.add(Box.createVerticalGlue());
		mainMenu.add(Box.createVerticalGlue());

		// ------  gameMenu layout ----- //
		
		// Initialized, but does not appear here. 
		gameMenu.setLayout(new BoxLayout(gameMenu, BoxLayout.X_AXIS));
		gameMenu.add(Box.createHorizontalGlue());
		
		gameMenu.add(goToMenu);
		gameMenu.setBackground(Color.BLACK);
		
		goToMenu.setFont(new Font ("Sans Serif", Font.PLAIN, 24));		
		
		// ------   gameField layout ------ //
		
		// The actual game panel is created in MazeGame class.
				
		// ---- Adds action items to buttons ---- //
		exitGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent click) {
				exitGame();
			}
		});
		startNewGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startNewGame();	
			}
		});
		goToMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				backToMenu();
			}
		});
		
		// window.add(mainMenu, BorderLayout.CENTER);
		window.add(mainMenu);
		// Add this statement to end to make everything show up? 
		// window.pack();
		window.setVisible(true);
	}
	
	/**
	 * Helper method. Used when player presses "Start Game" Button. 
	 * Changes the panel to the game panel.
	 */
	private void startNewGame() {
		gameField = new MazeGame();
		
		mainMenu.setVisible(false);
		window.add(gameField, BorderLayout.CENTER);
		
		window.add(gameMenu, BorderLayout.NORTH);
		window.remove(mainMenu);
		gameField.setVisible(true);
		gameMenu.setVisible(true);
		
		// gameField.prepareMap();
	}
	
	/**
	 * Called when user presses the "Exit Game" Button
	 * TODO: Investigate if there's a cleaner way of doing this. 
	 */
	private void exitGame() {
		System.exit(0);
	}
	
	/**
	 * After uses presses start game, the screen is divided into 2 components: A toolbar at the top, and the game panel at the bottom.
	 * Called when user presses the BackToMenu button in the toolbar. Stops the game. Ideally, should save everything. 
	 * 
	 * TODO: Instead of simply calling gameField.running = false, create a method that cleanly finishes all processes. 
	 */
	private void backToMenu() {
		gameField.setVisible(false);
		gameMenu.setVisible(false);
		window.add(mainMenu, BorderLayout.CENTER);
		window.remove(gameField);
		window.remove(gameMenu);
		mainMenu.setVisible(true);
		
		gameField.running = false;
	}	
}
