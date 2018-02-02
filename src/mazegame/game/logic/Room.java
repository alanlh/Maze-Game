package mazegame.game.logic;
import java.util.ArrayList;
import java.util.Random;

public class Room {
	
	static Random random = new Random();
	
	private final int roomColumn;
	private final int roomRow;
	
	private boolean isBorderRoom;
	
	final double ROOM_WIDTH = 100;
	final double ROOM_HEIGHT = 100;
	final double ENTRANCE_WIDTH = 20;
	
	final Point NORTH_WEST_CORNER = new Point(0, 0);
	final Point NORTH_EAST_CORNER = new Point(ROOM_WIDTH, 0);
	final Point SOUTH_WEST_CORNER = new Point(0, ROOM_HEIGHT);
	final Point SOUTH_EAST_CORNER = new Point(ROOM_WIDTH, ROOM_HEIGHT);
	
	final Point NORTH_LEFT_ENTRANCE = new Point((ROOM_WIDTH - ENTRANCE_WIDTH) / 2 , 0);
	final Point NORTH_RIGHT_ENTRANCE = new Point((ROOM_WIDTH + ENTRANCE_WIDTH) / 2 , 0);
	final Point SOUTH_LEFT_ENTRANCE = new Point((ROOM_WIDTH - ENTRANCE_WIDTH) / 2 , ROOM_HEIGHT);
	final Point SOUTH_RIGHT_ENTRANCE = new Point((ROOM_WIDTH + ENTRANCE_WIDTH) / 2 , ROOM_HEIGHT);
	final Point WEST_TOP_ENTRANCE = new Point(0, (ROOM_HEIGHT - ENTRANCE_WIDTH) / 2 );
	final Point WEST_BOTTOM_ENTRANCE = new Point(0, (ROOM_HEIGHT + ENTRANCE_WIDTH) / 2 );
	final Point EAST_TOP_ENTRANCE = new Point(ROOM_WIDTH, (ROOM_HEIGHT - ENTRANCE_WIDTH) / 2 );
	final Point EAST_BOTTOM_ENTRANCE = new Point(ROOM_WIDTH, (ROOM_HEIGHT + ENTRANCE_WIDTH) / 2 );

	private Maze mapContainer;
	private boolean northWall, eastWall, southWall, westWall;
	private Room northRoom, eastRoom, southRoom, westRoom;
			
	ArrayList<Items> itemsInRoom;
	
	int numberVistedTimes;
	
	int distanceFromExit;
	
	// --- Variables only for maze generation --- //

	public boolean visited;
	
	private static final double VISIT_VISITED_ROOM_CHANCE = 0.15;
		
	// constructor method; if blocked, does not do anything
	public Room(int xCoordinate, int yCoordinate, Maze mapContainer) {
		this.roomColumn = xCoordinate;
		this.roomRow = yCoordinate;
		this.mapContainer = mapContainer;
		
		northWall = true; 
		eastWall = true;
		southWall = true;
		westWall = true;		
		
		visited = false;
		
		itemsInRoom = generateInitialItems();
	}
	
	public void initialize() {
		northRoom = mapContainer.getRoom(roomColumn,  roomRow - 1);
		eastRoom = mapContainer.getRoom(roomColumn + 1,  roomRow);
		southRoom = mapContainer.getRoom(roomColumn,  roomRow + 1);
		westRoom = mapContainer.getRoom(roomColumn - 1,  roomRow);
	}
	
	// TODO:
	private ArrayList<Items> generateInitialItems() {
		
		return null;
	}
	
	public int getX() {
		return roomColumn;
	}
	
	public int getY() {
		return roomRow;
	}
	
	public boolean isBorderRoom() {
		return isBorderRoom;
	}
	
	public boolean getEastWallBlocked() {
		return eastWall;
	}

	public boolean getWestWallBlocked() {
		return westWall;
	}
	
	public boolean getNorthWallBlocked() {
		return northWall;
	}
	
	public boolean getSouthWallBlocked() {
		return southWall;
	}
	
	public Room getNorthRoom() {
		return northRoom;
	}
	
	public Room getEastRoom() {
		return eastRoom;
	}
	
	public Room getSouthRoom() {
		return southRoom;
	}
	
	public Room getWestRoom() {
		return westRoom;
	}
	
	public double getRoomWidth() {
		return ROOM_WIDTH;
	}
	
	public double getRoomHeight() {
		return ROOM_HEIGHT;
	}

	/**
	 * Wrapper method for generating a maze. Starts from the starting room and "digs" outwards. 
	 * @param mazeSize
	 */
	public void generateMaze(int mazeSize) {
		distanceFromExit = mazeDigger(mazeSize * mazeSize, mazeSize);
	}
	
	/**
	 * Algorithm for generating the maze. 
	 * 
	 * @param previousDistanceToExit 
	 * @param mazeSize
	 * @return
	 */
	private int mazeDigger(int previousDistanceToExit, int mazeSize) {
		this.visited = true;
		this.distanceFromExit = previousDistanceToExit;
		if (roomColumn == 0 || roomRow == 0 || roomColumn == mazeSize - 1 || roomRow == mazeSize - 1) {
			distanceFromExit = 0;
			northWall = false;
			eastWall = false;
			westWall = false;
			southWall = false;
			isBorderRoom = true;
			return distanceFromExit;
		}
				
		ArrayList<Integer> directionPermutation = new ArrayList<>();
		directionPermutation.add(0);
		directionPermutation.add(1);
		directionPermutation.add(2);
		directionPermutation.add(3);
		
		for (int i = 0; i < 4; i ++) {
			int randSeed = directionPermutation.remove(random.nextInt(directionPermutation.size()));
			boolean visitVisitedRoom = (random.nextDouble() < VISIT_VISITED_ROOM_CHANCE);
			switch (randSeed) {
				case 0: 
					// Go East
					if (!eastRoom.visited) {
						this.eastWall = false;
						eastRoom.westWall = false;
						distanceFromExit = Math.min(distanceFromExit, eastRoom.mazeDigger(distanceFromExit, mazeSize) + 1);
					} else if (visitVisitedRoom) {
						this.eastWall = false;
						eastRoom.westWall = false;
						if (this.distanceFromExit > eastRoom.distanceFromExit) {
							this.distanceFromExit = eastRoom.distanceFromExit + 1;
						} else {
							eastRoom.distanceFromExit = this.distanceFromExit + 1;
						}
					}
					break;
				case 1:
					// Go West
					if (!westRoom.visited) {
						this.westWall = false;
						westRoom.eastWall = false;					
						distanceFromExit = Math.min(distanceFromExit, westRoom.mazeDigger(distanceFromExit, mazeSize) + 1);
					} else if (visitVisitedRoom) {
						this.westWall = false;
						westRoom.eastWall = false;					
						if (this.distanceFromExit > westRoom.distanceFromExit) {
							this.distanceFromExit = westRoom.distanceFromExit + 1;
						} else {
							westRoom.distanceFromExit = this.distanceFromExit + 1;
						}
					}
					break;
				case 2:
					// Go North
					if (!northRoom.visited) {
						this.northWall = false;
						northRoom.southWall = false;				
						distanceFromExit = Math.min(distanceFromExit, northRoom.mazeDigger(distanceFromExit, mazeSize) + 1);
					} else if (visitVisitedRoom) {
						this.northWall = false;
						northRoom.southWall = false;				
						if (this.distanceFromExit > northRoom.distanceFromExit) {
							this.distanceFromExit = northRoom.distanceFromExit + 1;
						} else {
							northRoom.distanceFromExit = this.distanceFromExit + 1;
						}
					}
					break;
				case 3:
					// Go South
					if(!southRoom.visited) {
						this.southWall = false;
						southRoom.northWall = false;
						distanceFromExit = Math.min(distanceFromExit, southRoom.mazeDigger(distanceFromExit, mazeSize) + 1);
					} else if (visitVisitedRoom) {
						this.southWall = false;
						southRoom.northWall = false;
						if (this.distanceFromExit > southRoom.distanceFromExit) {
							this.distanceFromExit = southRoom.distanceFromExit + 1;
						} else {
							southRoom.distanceFromExit = this.distanceFromExit + 1;
						}
					}
					break;
			}
		}
		return distanceFromExit;
	}

	public void update() {
		
	}
	
}
