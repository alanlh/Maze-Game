package mazegame.game.logic;
public class Point {
	
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(Point other) {
		this.x = other.x;
		this.y = other.y;
	}
	
	public void setPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void incrementPoint(double changeX, double changeY) {
		this.x += changeX;
		this.y += changeY;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double distanceFrom(Point other) {
		return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
	}

	public void invertHorizontal(Room newRoom) {
		x = (x + newRoom.ROOM_WIDTH) % newRoom.ROOM_WIDTH;
	}
	
	public void invertVertical(Room newRoom) {
		y = (y + newRoom.ROOM_HEIGHT) % newRoom.ROOM_HEIGHT;
	}
	
}
