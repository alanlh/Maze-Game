
public abstract class Items {

	public enum ItemTypes {
		FOOD,
		AID,
		STICK,
		CANDLE,
	}
	
	public enum State {
		HOLDING,
		ROOM,
		INVENTORY
	}
	
	
	private Point position;
	private Room room;
	private State state;
	
	public Items (Room room, Point position, State itemState) {
		this.room = room;
		this.position = position;
		this.state = itemState;
	}

	abstract ItemTypes getType();
	abstract String getName();
	abstract State getState();
	
	abstract void draw();
	
}
