
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
	
	
	private int x;
	private int y;
	private Room room;
	private State state;
	
	public Items (Room room, int x, int y, State itemState) {
		this.room = room;
		this.x = x;
		this.y = y;
		this.state = itemState;
	}

	abstract ItemTypes getType();
	abstract String getName();
	abstract State getState();
	
	abstract void draw();
	
}
