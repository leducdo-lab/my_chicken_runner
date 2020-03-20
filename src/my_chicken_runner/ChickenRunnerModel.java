package my_chicken_runner;

import java.awt.Point;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.Timer;

public class ChickenRunnerModel {
	
	private int score;
	
	private Timer timer, appleTimer, enemyTimer;
	private Point bgPoint1, bgPoint2;
	private Chicken chicken;
	private CopyOnWriteArrayList<Missle> missles;
	private CopyOnWriteArrayList<Enemy> enemies;
	private CopyOnWriteArrayList<Apple> apples;
	private HealthBar healthBar;
	

	public ChickenRunnerModel() {
		this.score = 0;
		this.timer = new Timer(0, null);
		this.appleTimer = new Timer(0, null);
		this.enemyTimer = new Timer(0, null);
		this.bgPoint1 = new Point( 0, 0);
		this.bgPoint2 = new Point(0, 0);
		this.chicken = new Chicken();
		this.missles = new CopyOnWriteArrayList<Missle>();
		this.enemies = new CopyOnWriteArrayList<Enemy>();
		this.apples = new CopyOnWriteArrayList<Apple>();
		this.healthBar = new HealthBar();
	}


	public Timer getTimer() {
		return this.timer;
	}


	public void setTimer(Timer timer) {
		this.timer = timer;
	}


	public Timer getAppleTimer() {
		return this.appleTimer;
	}


	public void setAppleTimer(Timer appleTimer) {
		this.appleTimer = appleTimer;
	}


	public Timer getEnemyTimer() {
		return this.enemyTimer;
	}


	public void setEnemyTimer(Timer enemyTimer) {
		this.enemyTimer = enemyTimer;
	}
	
	public Point getBgPoint1() {
		return this.bgPoint1;
	}
	
	public Point getBgPoint2() {
		return this.bgPoint2;
	}
	
	public Chicken getChicken() {
		return this.chicken;
	}
	
	public CopyOnWriteArrayList<Apple> getApples() {
		return this.apples;
	}
	
	public CopyOnWriteArrayList<Enemy> getEnemies() {
		return this.enemies;
	}
	
	public CopyOnWriteArrayList<Missle> getMissles() {
		return this.missles;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public void addAppleScore() {
		this.score += Constrants.APPLE_SCORE_VALUE;
	}
	
	public HealthBar getHealthBar() {
		return this.healthBar;
	}

}
