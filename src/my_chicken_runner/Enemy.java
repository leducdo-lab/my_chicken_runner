package my_chicken_runner;

import java.awt.Point;

public class Enemy extends Point{

	private static final long serialVersionUID = 1L;

	private int health;
	private boolean jumpedOn;
	
	public Enemy() {
		this.setLocation(new Point(Constrants.ENEMY_START_X, Constrants.ENEMY_START_Y));
		this.health = Constrants.ENEMY_HEALTH_MAX;
	}
	
	public void moveLeft() {
		this.setLocation(this.getX() - Constrants.ENEMY_X_VELOCITY, this.getY());
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public boolean isJumpedOn() {
		return jumpedOn;
	}

	public void setJumpedOn(boolean jumpedOn) {
		this.jumpedOn = jumpedOn;
	}
	
}
