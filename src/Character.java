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
	
	private final double MAX_MOVE_SPEED;
	private final double MAX_TURN_SPEED;
	
	private final double MOVE_ACCELERATION;
	private final double TURN_ACCELERATION;

	private boolean turningLeft = false;
	private boolean turningRight = false;
	private boolean movingForward = false;
	private boolean movingBackwards = false;
	
	private ArrayList<Items> itemInventory = new ArrayList<>();
	private final int MAX_INVENTORY_SIZE = 10;
	
	private double hunger = 0;
	private double health = 1;
	private double warmth = 1;
	
	private Items inHand;
	
	private Color characterColor = Color.orange;	
	
	public Character(Room startRoom, MazeGame game) {
		currentRoom = startRoom;
		this.game = game;
		
		MAX_MOVE_SPEED = 100 * game.UPDATE_RATE;
		MAX_TURN_SPEED = 180 * game.UPDATE_RATE;
		
		MOVE_ACCELERATION = MAX_MOVE_SPEED  * game.UPDATE_RATE / 0.75;
		TURN_ACCELERATION = MAX_TURN_SPEED  * game.UPDATE_RATE / 0.25;
	}
	
	public Items currentItem() {
		return inHand;
	}
	
	public void updateCharacter() {
		updateMovement();
		updateHealth();
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
		} else if (turningLeft) {
			turnRate = Math.min(turnRate + TURN_ACCELERATION, MAX_TURN_SPEED);
		} else if (turningRight) {
			turnRate = Math.max(turnRate - TURN_ACCELERATION, -1 * MAX_TURN_SPEED);
		}
		
		faceAngleDeg = (faceAngleDeg + turnRate) % 360;
		
		x += moveSpeed * Math.cos(faceAngleDeg*Math.PI/180);
		y += moveSpeed * Math.sin(faceAngleDeg*Math.PI/180);
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
		
	}
	
	public void setHealth(AvailableActions aidType) {
		switch (aidType) {
		case CONSUME_FOOD:
			
		}
	}
}
