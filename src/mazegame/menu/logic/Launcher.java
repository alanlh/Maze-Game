package mazegame.menu.logic;
/*
 * Maze Game
 * Alan Hu
 *  
 */

import javax.swing.JFrame;

import mazegame.game.io.GameStatus;
import mazegame.game.logic.MazeGame;
import mazegame.game.ui.GamePanel;
import mazegame.menu.ui.ApplicationFrame;

/**
 * @author Alan Hu
 *
 */
public class Launcher {

	private static final String GAME_NAME = "THE MAZE GAME";
	
	public static void main(String[] args) {				
		new ApplicationFrame(GAME_NAME);
	}
}
