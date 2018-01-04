import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class MainMap extends JPanel {
	
	private Character player;
	private Room currentRoom;
	
	private final double WALL_BOUNDARY_PROPORTION = 0.9;
	private final double DOORWAY_PROPORTION = 0.2;
	private final int DOOR_SIZE = 150;
	
	Graphics graphics;
	BufferedImage backBuffer;
	Graphics backBufferGraphics;
	
	volatile double interpolation = 0;
	
	private int leftBoundary;
	private int rightBoundary;
	private int leftEntrancePoint;
	private int rightEntrancePoint;
	
	private int topBoundary;
	private int bottomBoundary;
	private int topEntrancePoint;
	private int bottomEntrancePoint;
	
	public MainMap() {
		this.setVisible(true);
		player = null; 
	}
	
	void setInterpolation(double interpolation) {
		this.interpolation = interpolation;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int minDimension = drawRoom(g);
		if (player != null) {
			drawCharacter(g, interpolation, minDimension);
		}
	}
		
	void setCharacter(Character player) {
		this.player = player;
	}
	
	void setRoom(Room room) {
		this.currentRoom = room;
	}
	
//	void displayGame(double interpolation) {
//		
//		// https://stackoverflow.com/questions/10966821/appropriate-way-to-draw-to-a-jpanel-for-2d-game-development
//				
//		drawRoom(graphics);
//		drawCharacter(backBufferGraphics, interpolation);		
//	}

	private int drawRoom(Graphics graphics) {
		
		int wallMinDimension = (int) (WALL_BOUNDARY_PROPORTION * Math.min(this.getWidth(),  this.getHeight()));
				
		leftBoundary = (int) ((this.getWidth() - wallMinDimension) / 2);
		rightBoundary = (int) ((this.getWidth() + wallMinDimension) / 2);
		leftEntrancePoint = (int) ((this.getWidth() - wallMinDimension * DOORWAY_PROPORTION) / 2);
		rightEntrancePoint = (int) ((this.getWidth() + wallMinDimension * DOORWAY_PROPORTION) / 2);
		
		topBoundary = (int) ((this.getHeight() - wallMinDimension) / 2);
		bottomBoundary = (int) ((this.getHeight() + wallMinDimension) / 2);
		topEntrancePoint = (int) ((this.getHeight() - wallMinDimension * DOORWAY_PROPORTION) / 2);
		bottomEntrancePoint = (int) ((this.getHeight() + wallMinDimension * DOORWAY_PROPORTION) / 2);
		
		// Draws the approximate shape below:
		//    ____  ____
		//   |          | 
		//   |          |
		//
		//   |          |
		//   |____  ____|
		//
		
		if (currentRoom.getEastWallBlocked()) {
			graphics.drawLine(rightBoundary, topBoundary, rightBoundary, bottomBoundary);
		} else {
			graphics.drawLine(rightBoundary, topBoundary, rightBoundary, topEntrancePoint);
			graphics.drawLine(rightBoundary, bottomBoundary, rightBoundary, bottomEntrancePoint);
		}
		
		if (currentRoom.getWestWallBlocked()) {
			graphics.drawLine(leftBoundary, topBoundary, leftBoundary, bottomBoundary);
		} else {
			graphics.drawLine(leftBoundary, topBoundary, leftBoundary, topEntrancePoint);
			graphics.drawLine(leftBoundary, bottomBoundary, leftBoundary, bottomEntrancePoint);
		}

		if (currentRoom.getNorthWallBlocked()) {
			graphics.drawLine(leftBoundary, topBoundary, rightBoundary, topBoundary);
		} else {
			graphics.drawLine(leftBoundary, topBoundary, leftEntrancePoint, topBoundary);
			graphics.drawLine(rightBoundary, topBoundary, rightEntrancePoint, topBoundary);
		}

		if (currentRoom.getSouthWallBlocked()) {
			graphics.drawLine(leftBoundary, bottomBoundary, rightBoundary, bottomBoundary);
		} else {
			graphics.drawLine(leftBoundary, bottomBoundary, leftEntrancePoint, bottomBoundary);
			graphics.drawLine(rightBoundary, bottomBoundary, rightEntrancePoint, bottomBoundary);
		}
		return wallMinDimension;
	}
	
	/**
	 * Draws a player character, an orange circle with two eyes. 
	 * 
	 * TODO: Move to Character class?
	 * 
	 * @param graphics
	 * @param interpolation to decide where to draw it specifically. 
	 */
	private void drawCharacter(Graphics graphics, double interpolation, int minDimension) {		
										
		double facingAngleRad = (player.getFaceAngleDeg() + interpolation * player.getTurnRate()) * Math.PI / 180;
				
		double playerCenterPointX = convertToPanelWidth((player.getPosition().getX() 
				- interpolation * player.getMoveSpeed() * Math.cos(facingAngleRad)), minDimension);
		double playerCenterPointY = convertToPanelHeight((player.getPosition().getY()
				- interpolation * player.getMoveSpeed() * Math.sin(facingAngleRad)), minDimension);
				
		double playerRadius = player.CHARACTER_RADIUS * minDimension;
		playerRadius /= (getWidth() < getHeight()) ? currentRoom.ROOM_WIDTH : currentRoom.ROOM_HEIGHT;
//		System.out.println(playerRadius);
		
		graphics.setColor(player.CHARACTER_COLOR);
		graphics.fillOval((int) (playerCenterPointX - playerRadius), 
				(int) (playerCenterPointY - playerRadius), 
				(int) (2 * playerRadius), 
				(int) (2 * playerRadius));
		
		graphics.setColor(Color.BLACK);
		graphics.fillOval((int) (playerCenterPointX - playerRadius * Math.cos(facingAngleRad + Math.PI / 4) * 2 / 3- 2), 
				(int) (playerCenterPointY - playerRadius * Math.sin(facingAngleRad + Math.PI / 4) * 2 / 3 - 2), 
				4, 
				4);
		graphics.fillOval((int) (playerCenterPointX - playerRadius * Math.cos(facingAngleRad - Math.PI / 4) * 2 / 3 - 2), 
				(int) (playerCenterPointY - playerRadius * Math.sin(facingAngleRad - Math.PI / 4) * 2 / 3 - 2), 
				4, 
				4);
	}
	
	private double convertToPanelWidth(double mapCoordinate, double minDimension) {		
		return leftBoundary + mapCoordinate * minDimension / currentRoom.ROOM_WIDTH;
	}
	
	private double convertToPanelHeight(double mapCoordinate, double minDimension) {		
		return topBoundary + mapCoordinate * minDimension / currentRoom.ROOM_HEIGHT;
	}
	
	int getLeftBoundary() {
		return leftBoundary;
	}

	int getRightBoundary() {
		return rightBoundary;
	}

	int getTopBoundary() {
		return topBoundary;
	}

	int getBottomBoundary() {
		return bottomBoundary;
	}

	int getLeftEntrancePoint() {
		return leftEntrancePoint;
	}

	int getRightEntrancePoint() {
		return rightEntrancePoint;
	}

	int getBottomEntrancePoint() {
		return bottomEntrancePoint;
	}
	
	int getTopEntrancePoint() {
		return topEntrancePoint;
	}
}
