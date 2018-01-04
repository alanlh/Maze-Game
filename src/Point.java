
public class Point {
	
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
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

	public void invertHorizontal() {
		x = (x + Room.ROOM_WIDTH) % Room.ROOM_WIDTH;
	}
	
	public void invertVertical() {
		y = (y + Room.ROOM_HEIGHT) % Room.ROOM_HEIGHT;
	}
	
}
