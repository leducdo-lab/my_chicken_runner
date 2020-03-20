package my_chicken_runner;

import java.awt.Point;

public class Missle extends Point{

	private static final long serialVersionUID = 1L;

	
	public Missle(int x, int y) {
		this.setLocation(new Point(x, y));
	}
	
	public int getXVelocity() {
		return Constrants.MISSLE_X_VELOCITY;
	}
	
	public int getMissleLength() {
		return Constrants.MISSLE_LENGTH;
	}
	
	public void moveRight() {
		this.setLocation(this.getX() + Constrants.MISSLE_X_VELOCITY, this.getY());
	}

}
