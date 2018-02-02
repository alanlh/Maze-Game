package mazegame.game.ui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import mazegame.game.io.GameStatus;
import mazegame.game.logic.Character;
import mazegame.game.logic.Room;

public class MapPanel extends JPanel {
	
	private GameStatus gameStatus;
		
	private final double WALL_BOUNDARY_PROPORTION = 0.9;
	private final double DOORWAY_PROPORTION = 0.2;
	private final int DOOR_SIZE = 150;
		
	private int leftBoundary;
	private int rightBoundary;
	private int leftEntrancePoint;
	private int rightEntrancePoint;
	
	private int topBoundary;
	private int bottomBoundary;
	private int topEntrancePoint;
	private int bottomEntrancePoint;
	
	public MapPanel() {
		this.setVisible(true);
	}
	
	void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}
	
	void initialize() {
		this.setVisible(true);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int minDimension = drawRoom(g);
		
		// Interpolation accessed here to have a consistent value for each drawing. Possibly faster? I don't know.
		double interpolation = gameStatus.getInterpolation();
		
		drawCharacter(g, interpolation, minDimension);
		repaint();
	}
	
//	void displayGame(double interpolation) {
//		
//		// https://stackoverflow.com/questions/10966821/appropriate-way-to-draw-to-a-jpanel-for-2d-game-development
//				
//		drawRoom(graphics);
//		drawCharacter(backBufferGraphics, interpolation);		
//	}

	private int drawRoom(Graphics graphics) {
		if (gameStatus == null || !gameStatus.getRoomInitialized()) {
			return 0;
		}
		
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
		
		if (gameStatus.getEastWallBlocked()) {
			graphics.drawLine(rightBoundary, topBoundary, rightBoundary, bottomBoundary);
		} else {
			graphics.drawLine(rightBoundary, topBoundary, rightBoundary, topEntrancePoint);
			graphics.drawLine(rightBoundary, bottomBoundary, rightBoundary, bottomEntrancePoint);
		}
		
		if (gameStatus.getWestWallBlocked()) {
			graphics.drawLine(leftBoundary, topBoundary, leftBoundary, bottomBoundary);
		} else {
			graphics.drawLine(leftBoundary, topBoundary, leftBoundary, topEntrancePoint);
			graphics.drawLine(leftBoundary, bottomBoundary, leftBoundary, bottomEntrancePoint);
		}

		if (gameStatus.getNorthWallBlocked()) {
			graphics.drawLine(leftBoundary, topBoundary, rightBoundary, topBoundary);
		} else {
			graphics.drawLine(leftBoundary, topBoundary, leftEntrancePoint, topBoundary);
			graphics.drawLine(rightBoundary, topBoundary, rightEntrancePoint, topBoundary);
		}

		if (gameStatus.getSouthWallBlocked()) {
			graphics.drawLine(leftBoundary, bottomBoundary, rightBoundary, bottomBoundary);
		} else {
			graphics.drawLine(leftBoundary, bottomBoundary, leftEntrancePoint, bottomBoundary);
			graphics.drawLine(rightBoundary, bottomBoundary, rightEntrancePoint, bottomBoundary);
		}
		return wallMinDimension;
	}
	
	/**
	 * Draws a player character, a circle with two eyes. 
	 *  
	 * @param graphics
	 * @param interpolation to decide where to draw it specifically. 
	 */
	private void drawCharacter(Graphics graphics, double interpolation, int minDimension){				
		if (gameStatus == null || !gameStatus.getPlayerReady()) {
			return;
		}
		
		double facingAngleRad = (gameStatus.getPlayerAngle()) * Math.PI / 180;
				
		double playerCenterPointX = convertToPanelWidth(gameStatus.getPlayerX(interpolation), minDimension);
		double playerCenterPointY = convertToPanelHeight(gameStatus.getPlayerY(interpolation), minDimension);
						
		double playerRadius = gameStatus.getPlayerSize() * minDimension;
		playerRadius /= (getWidth() < getHeight()) ? gameStatus.getRoomWidth() : gameStatus.getRoomHeight();
		
		graphics.setColor(gameStatus.getPlayerColor());
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
		return leftBoundary + mapCoordinate * minDimension / gameStatus.getRoomWidth();
	}
	
	private double convertToPanelHeight(double mapCoordinate, double minDimension) {		
		return topBoundary + mapCoordinate * minDimension / gameStatus.getRoomHeight();
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
