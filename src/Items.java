
public abstract class Items {

	private int x;
	private int y;
	private Room room;
	
	public Items (Room room, int x, int y) {
		this.room = room;
		this.x = x;
		this.y = y;
	}
	
	abstract void draw();
	
}
