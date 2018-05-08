package mazegame.game.logic.items;

import java.awt.Graphics;

import mazegame.game.logic.Point;
import mazegame.game.logic.Room;

public abstract class Items {

	public enum Type {
		FOOD, // Foods decrease the player's hunger over a short period of time, which helps increase overall hp in the long run
		HEAL, // First aid items immediately increase player's health.
		STICK, // A thick line on the ground, which can be used to point 
		CANDLE, // Lit by player and then placed on the ground. Lasts a finite amount of time. In future, there should be several types of candles
	}
	
	public enum State {
		HOLDING,
		ROOM,
		INVENTORY
	}
	
	private Point position;	
	private double orientation;
	protected double radius;
	private Room room;
	private State state;
	
	private String name;
	
	// private IMAGE icon;
	
	public Items (Room room, Point position) {
		this.room = room;
		this.position = position;
		this.state = State.ROOM;
		this.orientation = 360 * Math.random();
		this.radius = 5;
	}
	
	public void setPosition(Point newPosition) {
		position = newPosition;
	}
	
	public Point getPosition() {
		return position;
	}
	
	/**
	 * Sets the orientation in degrees.
	 * 0 is pointing right, increasing clockwise
	 * 
	 * @param newOrientation value between 0 and 360
	 */
	public void setOrientation(double newOrientation) {
		orientation = newOrientation;
	}
	
	public double getOrientation() {
		return orientation;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract Type getType();
	
	public void setState(State newState) {
		state = newState;
	}
	
	public State getState() {
		return state;
	}
	
	public abstract void onUse();
	
	public abstract void draw(Graphics g, double itemTopLeftX, double itemTopLeftY);

}
