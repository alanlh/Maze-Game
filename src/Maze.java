import java.util.ArrayList;

public class Maze {
	
	// An array of 50 by 50 rooms.
	
	final int MAZE_SIZE = 50;
	
	Room[][] chambers;
			
	private Room startRoom;
	
	public Maze() {
		
		chambers = new Room[MAZE_SIZE][MAZE_SIZE];
		
		for (int i = 0; i < MAZE_SIZE; i ++)
		{
			for (int j = 0; j < MAZE_SIZE; j ++)
			{
				chambers[i][j] = new Room(i, j, this);
			}
		}
		for (int i = 0; i < MAZE_SIZE; i ++)
		{
			for (int j = 0; j < MAZE_SIZE; j ++)
			{
				chambers[i][j].initialize();
			}
		}
		
		startRoom = chambers[MAZE_SIZE/2][MAZE_SIZE/2];
		
		startRoom.generateMaze(MAZE_SIZE);
	}
	
	public Room getRoom(int x, int y) {
		return (x >= 0 && y >= 0 && x <= MAZE_SIZE - 1 && y <= MAZE_SIZE - 1) ? chambers[x][y] : null;
	}

	public Room getStartingRoom() {
		return startRoom;
	}
	
}
