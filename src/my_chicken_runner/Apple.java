package my_chicken_runner;

import java.awt.Point;
import java.util.Random;

public class Apple extends Point{
	
	private static final long serialVersionUID = 1L;
	
	private boolean eaten;
	private float alpha;

	public Apple() {	
		this.alpha = 1.0f;
		int startY = Constrants.APPLE_START_Y;
		if(new Random().nextInt(3) + 1 == 3) {
			startY = Constrants.APPLE_START_Y_2;
		}
		this.setLocation(new Point(Constrants.APPLE_START_X, startY));
	}
	
	public void moveLeft() {
		this.setLocation(this.getX() - Constrants.APPLE_X_VELOCITY, this.getY());
	}
	
	public boolean isEaten() {
		return eaten;
	}
	
	public void setEaten(boolean eaten) {
		this.eaten = eaten;
	}
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public float getAlpha() {
		return alpha;
	}
	
	

}
