import java.util.ArrayList;
import java.util.Random;

public class Room {
	
	Random random = new Random();
	
	private int x;
	private int y;
	
	private Maze mapContainer;
	private boolean northWall, eastWall, southWall, westWall;
	private Room northRoom, eastRoom, southRoom, westRoom;
			
	Items[] itemsInRoom;
	
	int numberVistedTimes;
	
	int distanceFromExit = Integer.MAX_VALUE;
	
	// --- Variables only for maze generation --- //

	public boolean visited;
		
	// constructor method; if blocked, does not do anything
	public Room(int xCoordinate, int yCoordinate, Maze mapContainer) {
		this.x = xCoordinate;
		this.y = yCoordinate;
		this.mapContainer = mapContainer;
		
		northWall = true; 
		eastWall = true;
		southWall = true;
		westWall = true;		
		
		visited = false;
	}
	
	public void initialize() {
		northRoom = mapContainer.getRoom(x,  y + 1);
		eastRoom = mapContainer.getRoom(x + 1,  y);
		southRoom = mapContainer.getRoom(x,  y - 1);
		westRoom = mapContainer.getRoom(x - 1,  y);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void generateMaze(int mazeSize) {
		distanceFromExit = mazeDigger(null, mazeSize);
	}
	
	private int mazeDigger(Room previousRoom, int mazeSize) {
		this.visited = true;
		if (x == 0 || y == 0 || x == mazeSize - 1 || y == mazeSize - 1) {
			distanceFromExit = 1;
			northWall = false;
			eastWall = false;
			westWall = false;
			southWall = false;
			return distanceFromExit;
		}
				
		ArrayList<Integer> directionPermutation = new ArrayList<>();
		directionPermutation.add(0);
		directionPermutation.add(1);
		directionPermutation.add(2);
		directionPermutation.add(3);
		
		for (int i = 0; i < 4; i ++) {
			int randSeed = directionPermutation.remove(random.nextInt(directionPermutation.size()));
			switch (randSeed) {
				case 0: 
					// Go East
					if (!eastRoom.visited) {
						this.eastWall = false;
						eastRoom.westWall = false;
						distanceFromExit = Math.min(distanceFromExit, eastRoom.mazeDigger(this, mazeSize));
					}
					break;
				case 1:
					// Go West
					if (!westRoom.visited) {
						this.westWall = false;
						westRoom.eastWall = false;					
						distanceFromExit = Math.min(distanceFromExit, westRoom.mazeDigger(this, mazeSize));
					}
					break;
				case 2:
					// Go North
					if (!northRoom.visited) {
						this.northWall = false;
						northRoom.southWall = false;				
						distanceFromExit = Math.min(distanceFromExit, northRoom.mazeDigger(this, mazeSize));
					}
					break;
				case 3:
					// Go South
					if(!southRoom.visited) {
						this.southWall = false;
						southRoom.northWall = false;
						distanceFromExit = Math.min(distanceFromExit, southRoom.mazeDigger(this, mazeSize));
					}
					break;
			}
		}
		return distanceFromExit;
	}

}
