package mazegame.game.logic;

public abstract class Items {

	public enum Type {
		FOOD, // Foods decrease the player's hunger over a short period of time, which helps increase overall hp in the long run
		AID, // First aid items immediately increase player's health.
		STICK, // A thick line on the ground, which can be used to point 
		CANDLE, // Lit by player and then placed on the ground. Lasts a finite amount of time. In future, there should be several types of candles
	}
	
	public enum State {
		HOLDING,
		ROOM,
		INVENTORY
	}
	
	private String name;
	private Point position;	
	private double orientation;
	private Room room;
	private Type type;
	private State state;
	
	public Items (Room room, Point position) {
		this.room = room;
		this.position = position;
		this.state = State.HOLDING;
		this.orientation = 360 * Math.random();
	}
	
	String getName() {
		return name;
	}
	
	public Type getType() {
		return type;
	}
	State getState() {
		return state;
	}
	
	abstract void draw();
	
}
