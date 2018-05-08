package mazegame.game.ui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import mazegame.game.io.GameStatus;
import mazegame.game.io.Actions;
import mazegame.game.logic.Character;
import mazegame.game.logic.Point;
import mazegame.game.logic.Room;
import mazegame.game.logic.items.Items;

public class MapPanel extends JPanel {
	
	private GameStatus gameStatus;
		
	private final double WALL_BOUNDARY_PROPORTION = 0.9;
	private final double DOORWAY_PROPORTION = 0.2;
		
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
		addMouseListener();
	}
	
	/**
	 * Adds a mouse listener for clicking. 
	 */
	private void addMouseListener() {
		this.addMouseListener(new MouseAdapter() {			
			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				
				Point coord = convertToMapCoordinates(x, y);

				System.out.println("Click: " + coord.getX() + " " + coord.getY());
				Character player = gameStatus.getGameReference().getCharacter();

				if (SwingUtilities.isLeftMouseButton(e)) {
					player.pickUpItem(coord);
				} else if (SwingUtilities.isRightMouseButton(e)) {
					// Same as before
					// TODO: player._____
				}
			}
		});
	}

	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawRoom(g);
		
		// Interpolation accessed here to have a consistent value for each drawing. Possibly faster? I don't know.
		double interpolation = gameStatus.getInterpolation();
		
		drawCharacter(g, interpolation);
		repaint();
	}
	
	/**
	 * Draws the room the character is currently in. 
	 * 
	 * @param graphics default graphics object
	 */
	private void drawRoom(Graphics graphics) {
		if (gameStatus == null || !gameStatus.getRoomInitialized()) {
			return;
		}
		
		int wallMinDimension = computeMinDimension();
				
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
		
		if (gameStatus.getCharacterRoom().getEastWallBlocked()) {
			graphics.drawLine(rightBoundary, topBoundary, rightBoundary, bottomBoundary);
		} else {
			graphics.drawLine(rightBoundary, topBoundary, rightBoundary, topEntrancePoint);
			graphics.drawLine(rightBoundary, bottomBoundary, rightBoundary, bottomEntrancePoint);
		}
		
		if (gameStatus.getCharacterRoom().getWestWallBlocked()) {
			graphics.drawLine(leftBoundary, topBoundary, leftBoundary, bottomBoundary);
		} else {
			graphics.drawLine(leftBoundary, topBoundary, leftBoundary, topEntrancePoint);
			graphics.drawLine(leftBoundary, bottomBoundary, leftBoundary, bottomEntrancePoint);
		}

		if (gameStatus.getCharacterRoom().getNorthWallBlocked()) {
			graphics.drawLine(leftBoundary, topBoundary, rightBoundary, topBoundary);
		} else {
			graphics.drawLine(leftBoundary, topBoundary, leftEntrancePoint, topBoundary);
			graphics.drawLine(rightBoundary, topBoundary, rightEntrancePoint, topBoundary);
		}

		if (gameStatus.getCharacterRoom().getSouthWallBlocked()) {
			graphics.drawLine(leftBoundary, bottomBoundary, rightBoundary, bottomBoundary);
		} else {
			graphics.drawLine(leftBoundary, bottomBoundary, leftEntrancePoint, bottomBoundary);
			graphics.drawLine(rightBoundary, bottomBoundary, rightEntrancePoint, bottomBoundary);
		}
		
		for (Items item : gameStatus.getCharacterRoom().getItems()) {
			Point itemLoc = item.getPosition();
			double itemTopLeftX = convertToPanelWidth(itemLoc.getX() - item.getRadius(), wallMinDimension);
			double itemTopLeftY = convertToPanelHeight(itemLoc.getY() - item.getRadius(), wallMinDimension);
			
			item.draw(graphics, itemTopLeftX, itemTopLeftY);
		}
	}
	
	/**
	 * Draws a player character, a circle with two eyes for now
	 * 
	 * TODO: Figure out how to import a PNG or other image
	 *  
	 * @param default graphics object
	 * @param interpolation to decide where to draw it specifically. 
	 */
	private void drawCharacter(Graphics graphics, double interpolation){				
		if (gameStatus == null || !gameStatus.getPlayerReady()) {
			return;
		}
		
		int minDimension = computeMinDimension();
		
		double facingAngleRad = (gameStatus.getPlayerAngle()) * Math.PI / 180;
		
		// Computing player location
		// TODO: Convert to returning Point
		double playerCenterPointX = convertToPanelWidth(gameStatus.getPlayerX(interpolation), minDimension);
		double playerCenterPointY = convertToPanelHeight(gameStatus.getPlayerY(interpolation), minDimension);
						
		double playerRadius = gameStatus.getPlayerSize() * minDimension;
		playerRadius /= (getWidth() < getHeight()) ? gameStatus.getCharacterRoom().getRoomWidth() 
						: gameStatus.getCharacterRoom().getRoomHeight();
		
		// Draws character
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
	
	/**
	 * Converts the room coordinates into x coordinate of where to draw on screen.
	 * 
	 * TODO: Turn this to accept and return a Point Object
	 * TODO: Figure out if should call computeMinDimension() instead of passing in minDimension
	 * @param mapCoordinate
	 * @param minDimension
	 * @return
	 */
	private double convertToPanelWidth(double mapCoordinate, double minDimension) {		
		return leftBoundary + mapCoordinate * minDimension / gameStatus.getCharacterRoom().getRoomWidth();
	}
	
	/**
	 * Same as above but for height
	 * 
	 * @param mapCoordinate
	 * @param minDimension
	 * @return
	 */
	private double convertToPanelHeight(double mapCoordinate, double minDimension) {		
		return topBoundary + mapCoordinate * minDimension / gameStatus.getCharacterRoom().getRoomHeight();
	}
	
	private Point convertToMapCoordinates(int x, int y) {
		int minDimension = computeMinDimension();
		double xRoom = (x - leftBoundary) * gameStatus.getCharacterRoom().getRoomWidth() / minDimension;
		double yRoom = (y - topBoundary) * gameStatus.getCharacterRoom().getRoomHeight() / minDimension;
		return new Point(xRoom, yRoom);
	}
	
	/**
	 * Computes the pixel length of the room, which varies based off of the window size
	 * 
	 * @return the pixel dimension of the room
	 */
	private int computeMinDimension() {
		return (int) (WALL_BOUNDARY_PROPORTION * Math.min(this.getWidth(),  this.getHeight()));
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
