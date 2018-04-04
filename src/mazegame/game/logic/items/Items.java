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
	
	public Point position;	
	private double orientation;
	private Room room;
	private State state;
	
	private String name;
	
	// private IMAGE icon;
	
	public Items (Room room, Point position) {
		this.room = room;
		this.position = position;
		this.state = State.ROOM;
		this.orientation = 360 * Math.random();
	}
	
	public String getName() {
		return name;
	}
	
	public abstract Type getType();
	
	public State getState() {
		return state;
	}
	
	public abstract void onUse();
	
	public abstract void draw(Graphics g);
}
