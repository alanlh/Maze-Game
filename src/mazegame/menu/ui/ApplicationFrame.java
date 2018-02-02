package mazegame.menu.ui;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import mazegame.game.io.GameStatus;
import mazegame.game.logic.MazeGame;
import mazegame.game.ui.GamePanel;
import mazegame.menu.logic.Launcher;

// This is the code for the interface of the game, does not include the game itself.
/**
 * Creates a new window. 
 * @author Alan Hu
 *
 */
public class ApplicationFrame extends JFrame {
	
	MainMenuPanel mainMenu;
	TopBarMenuPanel topBarMenu;
	GamePanel gamePanel;
	
	final int DEFAULT_WINDOW_WIDTH = 800; // Width of window, here default as 800.
	final int DEFAULT_WINDOW_HEIGHT = 800; // Height of window, here default as 800.

	/**
	 * Creates the window, sets basic layout for everything.
	 */
	public ApplicationFrame(String title) {		
		super(title);
		// Window Properties
		setVisible(true);
		setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		setLocation(400,100);
		setResizable(true);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		mainMenu = new MainMenuPanel();
		
		add(mainMenu);
	}
		
	/**
	 * Helper method. Used when player presses "Start Game" Button. 
	 * Changes the panel to the game panel.
	 */
	void displayGame() {
		topBarMenu = new TopBarMenuPanel();
		gamePanel = new GamePanel();
		GameStatus gameStatus = new GameStatus();
		gamePanel.setGameStatus(gameStatus, new MazeGame(gamePanel, gameStatus));
						
		mainMenu.setVisible(false);
		add(gamePanel, BorderLayout.CENTER);
		add(topBarMenu, BorderLayout.NORTH);
		
		remove(mainMenu);
		gamePanel.setVisible(true);
		topBarMenu.setVisible(true);
	}
	
	/**
	 * Called when user presses the "Exit Game" Button
	 * TODO: Investigate if there's a cleaner way of doing this. 
	 * TODO: Clean up
	 */
	void exitGame() {
		System.exit(0);
	}
	
	/**
	 * After uses presses start game, the screen is divided into 2 components: A toolbar at the top, and the game panel at the bottom.
	 * Called when user presses the BackToMenu button in the toolbar. Stops the game. Ideally, should save everything. 
	 * 
	 * TODO: Instead of simply calling gameField.running = false, create a method that cleanly finishes all processes. 
	 */
	void backToMenu() {
		// gamePanel.deactivate();
		
		gamePanel.setVisible(false);
		topBarMenu.setVisible(false);
		add(mainMenu, BorderLayout.CENTER);
		remove(gamePanel);
		remove(topBarMenu);
		mainMenu.setVisible(true);
	}	
}
