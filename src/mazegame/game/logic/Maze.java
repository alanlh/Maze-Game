package mazegame.game.logic;
import java.util.ArrayList;

public class Maze {
	
	// An array of 50 by 50 rooms.
	
	final int MAZE_SIZE = 10;
	
	Room[][] chambers = new Room[MAZE_SIZE][MAZE_SIZE];
			
	private Room startRoom;
	
	public Maze() {		
		for (int i = 0; i < MAZE_SIZE; i ++) {
			for (int j = 0; j < MAZE_SIZE; j ++) {
				chambers[i][j] = new Room(i, j, this);
			}
		}
		for (int i = 0; i < MAZE_SIZE; i ++) {
			for (int j = 0; j < MAZE_SIZE; j ++) {
				chambers[i][j].initialize();
			}
		}
		
		startRoom = chambers[MAZE_SIZE/2][MAZE_SIZE/2];
		
		startRoom.generateMaze(MAZE_SIZE);
	}
	
	/**
	 * Returns the rooms at the requested location. Positive x is to the right, positive y is to bottom
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @return The room at the location. If the coordinates are invalid, 
	 */
	public Room getRoom(int x, int y) {
		return (x >= 0 && y >= 0 && x <= MAZE_SIZE - 1 && y <= MAZE_SIZE - 1) ? chambers[x][y] : null;
	}

	public Room getStartingRoom() {
		return startRoom;
	}
	
	public void update() {
		for (int i = 0; i < MAZE_SIZE; i ++) {
			for (int j = 0; j < MAZE_SIZE; j ++) {
				chambers[i][j].update();
			}
		}
	}
	
}
