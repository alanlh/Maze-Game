import java.awt.Color;
import java.util.ArrayList;

public class Character {

	private MazeGame game;
	private Room currentRoom;
	
	private Point position;
	private double faceAngleDeg = 90;
	private double moveSpeed = 0;
	private double turnRate = 0; // CCW is positive
	
	final double CHARACTER_RADIUS = 5;
	final Color CHARACTER_COLOR = Color.ORANGE;
	
	private final double MAX_MOVE_SPEED;
	private final double MAX_TURN_SPEED;
	
	private final double MOVE_ACCELERATION;
	private final double TURN_ACCELERATION;
	
	private final double MOVE_ACCELERATION_TIME;
	private final double TURN_ACCELERATION_TIME;

	private boolean turningLeft = false;
	private boolean turningRight = false;
	private boolean movingForward = false;
	private boolean movingBackwards = false;
	
	private ArrayList<Items> itemInventory = new ArrayList<>();
	private final int MAX_INVENTORY_SIZE = 10;
	
	private double hunger = 0;
	private double health = 1;
	private double warmth = 1;
	
	private double currentEating = 0;
	private double currentFirstAid = 0;
	private final double FOOD_VALUE = 1;
	
	private Items inHand;
	int candleCount;
	int aidCount;
	int foodCount;
	int stickCount;

	private final Color characterColor = Color.orange;
	
	private boolean startingPointSet = false;
	
	public Character(Room startRoom, MazeGame game) {
		currentRoom = startRoom;
		this.game = game;
		
		MAX_MOVE_SPEED = 25 * game.UPDATE_FREQUENCY;
		MAX_TURN_SPEED = 240 * game.UPDATE_FREQUENCY;
		
		MOVE_ACCELERATION_TIME = 0.8;
		TURN_ACCELERATION_TIME = 0.4;
		
		MOVE_ACCELERATION = MAX_MOVE_SPEED * game.UPDATE_FREQUENCY / MOVE_ACCELERATION_TIME;
		TURN_ACCELERATION = MAX_TURN_SPEED * game.UPDATE_FREQUENCY / TURN_ACCELERATION_TIME;
				
		position = new Point(currentRoom.ROOM_WIDTH / 2, currentRoom.ROOM_HEIGHT / 2);
	}
	
	public Items currentItem() {
		return inHand;
	}
	
	public void updateCharacter() {
		updateMovement();
		updateHealth();
	}
	
	void setStartingPoint(double width, double height) {
//		if (!startingPointSet) {
//			this.x = width / 2;
//			this.y = height / 2;
//			
//			startingPointSet = true;
//		}
	}
	
	private void updateMovement() {
		if (movingForward == movingBackwards) {			
			moveSpeed = (moveSpeed > 0) ? Math.max(moveSpeed - MOVE_ACCELERATION, 0) : Math.min(moveSpeed + MOVE_ACCELERATION, 0);
		} else if (movingForward) {
			moveSpeed = Math.min(moveSpeed + MOVE_ACCELERATION, MAX_MOVE_SPEED);
		} else if (movingBackwards) {
			moveSpeed = Math.max(moveSpeed - MOVE_ACCELERATION, -1 * MAX_MOVE_SPEED);
		}
		
		if (turningLeft == turningRight) {			
			turnRate = (turnRate > 0) ? Math.max(turnRate - TURN_ACCELERATION, 0) : Math.min(turnRate + TURN_ACCELERATION, 0);
		} else if (turningRight) {
			turnRate = Math.min(turnRate + TURN_ACCELERATION, MAX_TURN_SPEED);
		} else if (turningLeft) {
			turnRate = Math.max(turnRate - TURN_ACCELERATION, -1 * MAX_TURN_SPEED);
		}
		
		faceAngleDeg = (faceAngleDeg + turnRate) % 360;
		
		position.incrementPoint(- moveSpeed * Math.cos(faceAngleDeg * Math.PI / 180), - moveSpeed * Math.sin(faceAngleDeg * Math.PI / 180));
		
		// If the Character is too close to the left/west wall. 
		if (position.getX() < CHARACTER_RADIUS) {
			double westTopDistance = position.distanceFrom(currentRoom.WEST_TOP_ENTRANCE);
			double westBottomDistance = position.distanceFrom(currentRoom.WEST_BOTTOM_ENTRANCE);
			if (currentRoom.getWestWallBlocked() 
					|| position.getY() > currentRoom.WEST_BOTTOM_ENTRANCE.getY()
					|| position.getY() < currentRoom.WEST_TOP_ENTRANCE.getY()) {
				position.incrementPoint(CHARACTER_RADIUS - position.getX(), 0);
				
			} else if (westTopDistance < CHARACTER_RADIUS) {
				double dislocateRatio = (CHARACTER_RADIUS - westTopDistance) / westTopDistance;
				position.incrementPoint(dislocateRatio * (position.getX() - currentRoom.WEST_TOP_ENTRANCE.getX()), 
						dislocateRatio * (position.getY() - currentRoom.WEST_TOP_ENTRANCE.getY()));
				
			} else if (westBottomDistance < CHARACTER_RADIUS) {
				double dislocateRatio = (CHARACTER_RADIUS - westBottomDistance) / westBottomDistance;
				position.incrementPoint(dislocateRatio * (position.getX() - currentRoom.WEST_BOTTOM_ENTRANCE.getX()), 
						dislocateRatio * (position.getY() - currentRoom.WEST_BOTTOM_ENTRANCE.getY()));
				
			} else if (position.getX() < 0) {
				currentRoom = currentRoom.getWestRoom();
				game.mainMap.setRoom(currentRoom);
				position.invertHorizontal();
				System.out.println(currentRoom.getX() + " " + currentRoom.getY() + " " + currentRoom.distanceFromExit);
			}
		}
		// If the Character is too close to the right/east wall. 
		if (position.getX() > currentRoom.ROOM_WIDTH - CHARACTER_RADIUS) {
			double eastTopDistance = position.distanceFrom(currentRoom.EAST_TOP_ENTRANCE);
			double eastBottomDistance = position.distanceFrom(currentRoom.EAST_BOTTOM_ENTRANCE);
			if (currentRoom.getEastWallBlocked()
					|| position.getY() > currentRoom.EAST_BOTTOM_ENTRANCE.getY()
					|| position.getY() < currentRoom.EAST_TOP_ENTRANCE.getY()) {
				position.incrementPoint(currentRoom.ROOM_WIDTH - position.getX() - CHARACTER_RADIUS, 0);
				
			} else if (eastTopDistance < CHARACTER_RADIUS) {
				double dislocateRatio = (CHARACTER_RADIUS - eastTopDistance) / eastTopDistance;
				position.incrementPoint(dislocateRatio * (position.getX() - currentRoom.EAST_TOP_ENTRANCE.getX()), 
						dislocateRatio * (position.getY() - currentRoom.WEST_TOP_ENTRANCE.getY()));
				
			} else if (eastBottomDistance < CHARACTER_RADIUS) {
				double dislocateRatio = (CHARACTER_RADIUS - eastBottomDistance) / eastBottomDistance;
				position.incrementPoint(dislocateRatio * (position.getX() - currentRoom.EAST_BOTTOM_ENTRANCE.getX()), 
						dislocateRatio * (position.getY() - currentRoom.EAST_BOTTOM_ENTRANCE.getY()));
				
			} else if (position.getX() > currentRoom.ROOM_WIDTH) {
				currentRoom = currentRoom.getEastRoom();
				game.mainMap.setRoom(currentRoom);
				position.invertHorizontal();
				System.out.println(currentRoom.getX() + " " + currentRoom.getY() + " " + currentRoom.distanceFromExit);
			}
		}
		// If the Character is too close to the north/top wall. 
		if (position.getY() < CHARACTER_RADIUS) {
			double northLeftDistance = position.distanceFrom(currentRoom.NORTH_LEFT_ENTRANCE);
			double northRightDistance = position.distanceFrom(currentRoom.NORTH_RIGHT_ENTRANCE);
			if (currentRoom.getNorthWallBlocked()
					|| position.getX() > currentRoom.NORTH_RIGHT_ENTRANCE.getX()
					|| position.getX() < currentRoom.NORTH_LEFT_ENTRANCE.getX()) {
				position.incrementPoint(0, CHARACTER_RADIUS - position.getY());
				
			} else if (northLeftDistance < CHARACTER_RADIUS) {
				double dislocateRatio = (CHARACTER_RADIUS - northLeftDistance) / northLeftDistance;
				position.incrementPoint(dislocateRatio * (position.getX() - currentRoom.NORTH_LEFT_ENTRANCE.getX()), 
						dislocateRatio * (position.getY() - currentRoom.NORTH_LEFT_ENTRANCE.getY()));
			} else if (northRightDistance < CHARACTER_RADIUS) {
				double dislocateRatio = (CHARACTER_RADIUS - northRightDistance) / northRightDistance;
				position.incrementPoint(dislocateRatio * (position.getX() - currentRoom.NORTH_RIGHT_ENTRANCE.getX()), 
						dislocateRatio * (position.getY() - currentRoom.NORTH_RIGHT_ENTRANCE.getY()));
			} else if (position.getY() < 0) {
				currentRoom = currentRoom.getNorthRoom();
				game.mainMap.setRoom(currentRoom);
				position.invertVertical();
				System.out.println(currentRoom.getX() + " " + currentRoom.getY() + " " + currentRoom.distanceFromExit);
			}
		}
		// If the Character is too close to the bottom/south wall. 
		if (position.getY() > currentRoom.ROOM_HEIGHT - CHARACTER_RADIUS) {
			double southLeftDistance = position.distanceFrom(currentRoom.SOUTH_LEFT_ENTRANCE);
			double southRightDistance = position.distanceFrom(currentRoom.SOUTH_RIGHT_ENTRANCE);
			if (currentRoom.getSouthWallBlocked()
					|| position.getX() > currentRoom.SOUTH_RIGHT_ENTRANCE.getX()
					|| position.getX() < currentRoom.SOUTH_LEFT_ENTRANCE.getX()) {
				position.incrementPoint(0, currentRoom.ROOM_HEIGHT - position.getY() - CHARACTER_RADIUS);
				
			} else if (southLeftDistance < CHARACTER_RADIUS) {
				double dislocateRatio = (CHARACTER_RADIUS - southLeftDistance) / southLeftDistance;
				position.incrementPoint(dislocateRatio * (position.getX() - currentRoom.SOUTH_LEFT_ENTRANCE.getX()), 
						dislocateRatio * (position.getY() - currentRoom.SOUTH_LEFT_ENTRANCE.getY()));
			} else if (southRightDistance < CHARACTER_RADIUS) {
				double dislocateRatio = (CHARACTER_RADIUS - southRightDistance) / southRightDistance;
				position.incrementPoint(dislocateRatio * (position.getX() - currentRoom.SOUTH_RIGHT_ENTRANCE.getX()), 
						dislocateRatio * (position.getY() - currentRoom.SOUTH_RIGHT_ENTRANCE.getY()));
			} else if (position.getY() > currentRoom.ROOM_HEIGHT) {
				currentRoom = currentRoom.getSouthRoom();
				game.mainMap.setRoom(currentRoom);
				position.invertVertical();
				System.out.println(currentRoom.getX() + " " + currentRoom.getY() + " " + currentRoom.distanceFromExit);
			}
		}
		
		// If current room is border room, then win!
	}
	
	public void setMovement(AvailableActions direction, boolean isMoving) {
		switch (direction) {
		case FORWARD: 
			movingForward = isMoving;
			break;
		case BACK:
			movingBackwards = isMoving;
			break;
		case RIGHT:
			turningRight = isMoving;
			break;
		case LEFT:
			turningLeft = isMoving;
			break;
		default:
			break;
		}
	}
	
	private void updateHealth() {
		// Current eating 
	}
	
	public void setHealth(AvailableActions aidType) {
		/**
		 * This should eventually be replaced with creating a bar with an icon of each type of food,
		 * So that the player can select the specific type.
		 * This means that each food type should have different properties.
		 * However, this implementation should assume that food types are different. 
		 */
		if (aidType == AvailableActions.CONSUME_FOOD) {
			if (foodCount > 0) {
				currentEating += FOOD_VALUE;
			} else {
				// TODO: Display message to player
			}
		} else if (aidType == AvailableActions.APPLY_FIRST_AID) {
			if (inventoryContains(Items.ItemTypes.AID)) {
				
			} else {
				
			}
		}
		
	}
	
	private boolean inventoryContains(Items.ItemTypes itemType) {
		for(Items item : itemInventory) {
			if (item.getType() == itemType) {
				return true;
			}
		}
		return false;
	}
	
	private int getFoodCount() {
		return foodCount;
	}
	
	private int getAidCount() {
		return aidCount;
	}
	
	private int getStickCount() {
		return stickCount;
	}
	
	private int getCandleCount() {
		return candleCount;
	}
	
	Point getPosition() {
		return position;
	}
	
	double getFaceAngleDeg() {
		return faceAngleDeg;
	}
	
	double getMoveSpeed() {
		return moveSpeed;
	}
	
	double getTurnRate() {
		return turnRate;
	}
}
