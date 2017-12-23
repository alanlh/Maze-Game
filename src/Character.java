import java.awt.Color;
import java.util.ArrayList;

public class Character {

	private MazeGame game;
	private Room currentRoom;
	private double x = 0;
	private double y = 0;
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
				
		x = currentRoom.ROOM_WIDTH / 2;
		y = currentRoom.ROOM_HEIGHT / 2;
	}
	
	public Items currentItem() {
		return inHand;
	}
	
	public void updateCharacter() {
		updateMovement();
		updateHealth();
	}
	
	void setStartingPoint(double width, double height) {
		if (!startingPointSet) {
			this.x = width / 2;
			this.y = height / 2;
			
			startingPointSet = true;
		}
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
		
		x -= moveSpeed * Math.cos(faceAngleDeg * Math.PI / 180);
		y -= moveSpeed * Math.sin(faceAngleDeg * Math.PI / 180);
		
		if (x < CHARACTER_RADIUS) {
			x = CHARACTER_RADIUS;
		}
		if (x > currentRoom.ROOM_WIDTH - CHARACTER_RADIUS) {
			x = currentRoom.ROOM_WIDTH - CHARACTER_RADIUS;
		}
		if (y < CHARACTER_RADIUS) {
			y = CHARACTER_RADIUS;
		}
		if (y > currentRoom.ROOM_HEIGHT - CHARACTER_RADIUS) {
			y = currentRoom.ROOM_HEIGHT - CHARACTER_RADIUS;
		}
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
	
	double getX() {
		return x;
	}
	
	double getY() {
		return y;
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
