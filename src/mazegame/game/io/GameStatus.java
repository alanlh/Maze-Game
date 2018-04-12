package mazegame.game.io;

import mazegame.game.logic.Room;
import mazegame.game.logic.items.Items;

import java.awt.Color;
import java.util.LinkedList;

import mazegame.game.logic.Character;
import mazegame.game.logic.MazeGame;

public class GameStatus {
	// Class containing variables required to draw the game. Should mostly be volatile
	
	// Room: doors, items, 
	// Character: x, y, face direction, speed, (rotation speed), items.

	// --- VARIABLES RELATED TO THE OVERALL GAME --- //
		
	public GameStatus() {
		game = new MazeGame(this);
	}
	
	private MazeGame game;
	public MazeGame getGameReference() {
		return game;
	}
		
	private boolean running = false;
	public void changeRunning(boolean isRunning) {
		running = isRunning;
	}	
	public boolean getRunning() {
		return running;
	}
	
	private volatile double interpolation = 0;
	public void setInterpolation(double newInterpolation) {
		interpolation = newInterpolation;
	}
	public double getInterpolation() {
		return interpolation;
	}
	
	private volatile boolean win = false;
	public void setWin(boolean hasWon) {
		win = hasWon;
	}
	public boolean getWin() {
		return win;
	}
	
	// --- VARIABLES RELATED TO THE ROOM --- //
	
	public volatile boolean roomInitialized = false;
	public void setRoomInitialized(boolean roomInitialized) {
		this.roomInitialized = roomInitialized;
	}
	public boolean getRoomInitialized() {
		return roomInitialized;
	}
	
	private volatile boolean eastWallBlocked = false;
	private volatile boolean westWallBlocked = false;
	private volatile boolean northWallBlocked = false;
	private volatile boolean southWallBlocked = false;
	
	private volatile double roomWidth = 0;
	private volatile double roomHeight = 0;
	public void setCharacterRoom(Room newRoom) {
		setRoomInitialized(true);
		eastWallBlocked = newRoom.getEastWallBlocked();
		westWallBlocked = newRoom.getWestWallBlocked();
		northWallBlocked = newRoom.getNorthWallBlocked();
		southWallBlocked = newRoom.getSouthWallBlocked();
		
		roomWidth = newRoom.getRoomWidth();
		roomHeight = newRoom.getRoomHeight();
	}
	
	public boolean getEastWallBlocked() {
		return eastWallBlocked;
	}
	public boolean getWestWallBlocked() {
		return westWallBlocked;
	}
	public boolean getNorthWallBlocked() {
		return northWallBlocked;
	}
	public boolean getSouthWallBlocked() {
		return southWallBlocked;
	}
	public double getRoomWidth() {
		return roomWidth;
	}
	public double getRoomHeight() {
		return roomHeight;
	}

	
	// --- VARIABLES RELATED TO THE PLAYER CHARACTER --- //
	
	private volatile boolean playerReady = false;
	public void playerReady(boolean playerExists) {
		this.playerReady = playerExists;
	}
	public boolean getPlayerReady() {
		return playerReady;
	}

	// Player position information
	private volatile double playerX;
	private volatile double playerY;
	private volatile double playerSpeed; // Speed is only used for interpolation purposes. Turn rate not included. 
	private volatile double playerFaceAngle;
	public void updateCharacterPosition(double x,
										double y,
										double speed,
										double angle) {
		playerX = x;
		playerY = y;
		playerSpeed = speed;
		playerFaceAngle = angle;
	}
	public double getPlayerX(double interpolation) {
		return playerX - playerSpeed * interpolation * Math.cos(playerFaceAngle * Math.PI / 180);
	}
	public double getPlayerY(double interpolation) {
		return playerY - playerSpeed * interpolation * Math.sin(playerFaceAngle * Math.PI / 180);
	}
	public double getPlayerAngle() {
		return playerFaceAngle;
	}
	
	// TODO: Determine if should be volatile?
	private boolean movingForward = false;
	private boolean movingBackwards = false;
	private boolean turningRight = false;
	private boolean turningLeft = false;
	/**
	 * Sets updates the moving direction of the player character.
	 * Does not affect the position of the character
	 * Character class gets values from here 
	 * 
	 * @param direction: 	A enum describing which direction the player is moving
	 * 						Depends on which key was pressed.
	 * @param isMoving: 	Whether to start or stop moving in that direction.
	 * 						If key was pressed, true. If released, false.
	 */
	public void setMovement(Actions direction, boolean isMoving) {
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

	public boolean getMovingForward() {
		return movingForward;
	}
	public boolean getMovingBackwards() {
		return movingBackwards;
	}
	public boolean getTurningRight() {
		return turningRight;
	}
	public boolean getTurningLeft() {
		return turningLeft;
	}
	
	// Lesser used attributes
	// TODO: Figure out how to setup a listener. 
	private volatile Color playerColor = Color.RED;
	private volatile double playerSize = 0;
	private volatile int playerInventorySize = 10;
	public void setPlayerAttributes(Color playerColor,
									double playerSize,
									int playerInventorySize) {
		this.playerColor = playerColor;
		this.playerSize = playerSize;
		this.playerInventorySize = playerInventorySize;
	}
	public Color getPlayerColor() {
		return playerColor;
	}
	public double getPlayerSize() {
		return playerSize;
	}
	public int getInventorySize() {
		return playerInventorySize;
	}
	
	// --- VARIABLES RELATED TO ITEMS --- //

	volatile LinkedList<Items> currentPlayerInventory;
	volatile boolean changedSinceLastRefresh = true;
	public void setPlayerInventory(LinkedList<Items> inventoryPointer) {
		currentPlayerInventory = inventoryPointer;
		changedSinceLastRefresh = true;
	}
	
	public boolean getItemsChanged() {
		return changedSinceLastRefresh;
	}
	
	// Called by ItemsPanel to alert when an icon has been clicked
	public void alertItemClicked(int index) {
		
	}
	
	private Items getPlayerItem(int index) {
		if (currentPlayerInventory == null) {
			return null;
		}
		return currentPlayerInventory.get(index);
	}
	
}
