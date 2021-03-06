package mazegame.game.logic;
// See this link:
// https://stackoverflow.com/questions/12021249/adding-jpanel-from-another-class-to-jpanel-in-jframe
// http://www.java-gaming.org/index.php?topic=25864.0

import mazegame.game.io.GameLoop;
import mazegame.game.io.GameStatus;
import mazegame.game.ui.GamePanel;


/**
 * @author Alan Hu
 *
 * Class acting as gateway between multiple parts of the game. Sets 
 */
public class MazeGame {

	GameStatus gameStatus;
	
	// includes:
	Maze map; // Maze maze: includes an array of rooms, along with testing to make sure there is a valid path.
	Character player; // Character person
	
	GamePanel panel; // In order to reference it and tell what to draw. 
			
	private static final int MAX_ACTIONS_PER_UPDATE = 10;
	// Ideally should be executable within the update time
	// Probably not needed in this game.
			
	/**
	 * Sets up by calling constructors on each part of the game. 
	 */
	public MazeGame(GameStatus gameStatus) {		
		this.gameStatus = gameStatus;
	}
	
	/**
	 * Called when the introduction has finished. 
	 */
	public void initialize() {
		map = new Maze();
		
		player = new Character(this, gameStatus, map.getStartingRoom());
		gameStatus.playerReady(true);
		gameStatus.setCharacterRoom(map.getStartingRoom());
	}
		
	/**
	 * Updates map and player. 
	 */
	public void update() {
		player.update();
		map.update();
	}
		
	
	public Character getCharacter() {
		return player;
	}
	
	public void displayWinMessage() {
		System.out.println("You have found your way out. Congrats.");
	}
	
}
