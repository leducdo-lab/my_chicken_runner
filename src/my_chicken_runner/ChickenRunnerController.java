package my_chicken_runner;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class ChickenRunnerController {
	
	private final ChickenRunnerView view;
	private final ChickenRunnerModel model;
	
	private boolean inGame;
	
	//Note: Is running two swing timers the best solution for two independent events
	// (moving the background, moving chicken, moving apple, spawning apple etc...)
	public ChickenRunnerController(ChickenRunnerView view, ChickenRunnerModel model) {
		inGame = true;
		this.view = view;
		this.model = model;
		
		//Set location of second background image to be on right side of window.
		this.model.getBgPoint2().x = this.view.getBg2().getWidth(null);
		this.view.addPanel(new Panel());
		
		this.model.setTimer(new Timer(Constrants.MAIN_TIME_DELAY, new MainTimer()));
		this.view.addKeyListener(new ChickenKeyListener());
		
		this.model.setAppleTimer(new Timer(Constrants.APPLE_SPAWN_DELAY, new AppleSpawner()));
		this.model.getAppleTimer().start();
		
		this.model.setEnemyTimer(new Timer(Constrants.ENEMY_SPAWN_DELAY, new EnemySpawner()));
		this.model.getEnemyTimer().start();
		
		inGame = true;
		this.model.getTimer().start();
		
	}
	
	public class Panel extends JPanel{
		
		private static final long serialVersionUID = 1L;

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(inGame) {
				g.drawImage(view.getBg1(), model.getBgPoint1().x, model.getBgPoint1().y, view.getBg1().getWidth(null), view.getBg1().getHeight(null), null);
				g.drawImage(view.getBg2(), model.getBgPoint2().x, model.getBgPoint2().y, view.getBg2().getWidth(null), view.getBg2().getHeight(null), null);
				
				drawHealthBar(g);
				
				drawApple(g);
				
				drawEnemy(g);
				
				g.drawImage(view.getCurrChickenImage(), model.getChicken().getLocation().x, model.getChicken().getLocation().y, view.getChickenImage().getWidth(null), view.getChickenImage().getHeight(null), null);
				
				for(Missle missle : model.getMissles()) {
					g.drawImage(view.getMissleImage(), missle.getLocation().x, missle.getLocation().y, missle.getMissleLength(), missle.getMissleLength(), null);
				}
				
				drawScore(g);
			}else {
				Font font = new Font("Helvetica", Font.BOLD, 20);
				g.setColor(Color.BLACK);
		        g.setFont(font);
		        g.drawString("Game Over!", 350, Constrants.FRAME_HEIGHT / 2);
		        g.drawString("Score: "+model.getScore(), 370, Constrants.FRAME_HEIGHT / 2 +30);
			}
		}
		
		private void drawHealthBar(Graphics g) {
			// Draw Red Health Bar
			g.setColor(Constrants.RED_COLOR);
			g.fillRect(Constrants.HEALTHBAR_X, Constrants.HEALTHBAR_Y, Constrants.HEALTHBAR_WIDTH, Constrants.HEALTHBAR_HEIGHT);
			
			// Draw Green Health Bar Over Top
			g.setColor(Constrants.GREEN_COLOR);
			g.fillRect(model.getHealthBar().getX(), Constrants.HEALTHBAR_Y, (int) (model.getHealthBar().getHealthPercentage() * Constrants.HEALTHBAR_WIDTH), Constrants.HEALTHBAR_HEIGHT);
			
			g.setFont(new Font("Monospaced", Font.BOLD, Constrants.HEALTHBAR_FONT_SIZE));
			
			long healthPercentage = (long) (model.getHealthBar().getHealthPercentage() * 100);
			
			if(healthPercentage < 50 && healthPercentage > 0) {
				g.setColor(Constrants.RED_COLOR);
			}else if(healthPercentage <= 0) {
				g.setColor(Constrants.RED_COLOR);
				inGame = false;
			}
			
			String str = healthPercentage +"%";
			int strLength = g.getFontMetrics().stringWidth(str);
			g.drawString(str, Constrants.HEALTHBAR_PERCENT_X - strLength, Constrants.HEALTHBAR_PERCENT_Y);
		}
		
		private void drawScore(Graphics g) {
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			
			g.setFont(new Font("Monospaced", Font.BOLD, Constrants.SCORE_FONT_SIZE));
			g.setColor(Constrants.GREEN_COLOR);
			g.drawString("Score: "+model.getScore(), Constrants.SCORE_X, Constrants.SCORE_Y);
		}
		
		private void drawApple(Graphics g) {
			for(Apple apple : model.getApples()) {
				((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, apple.getAlpha()));
				g.drawImage(view.getAppleImage(), apple.getLocation().x, apple.getLocation().y, view.getAppleImage().getWidth(null), view.getAppleImage().getHeight(null), null);
			}
			((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
		
		private void drawEnemy(Graphics g) {
			for(Enemy enemy : model.getEnemies()) {
				int shrinkFactor = Constrants.ENEMY_SIZE_FACTOR * (Constrants.ENEMY_HEALTH_MAX - enemy.getHealth());
				g.drawImage(view.getCurrEnemyImage(), enemy.getLocation().x , enemy.getLocation().y + (shrinkFactor), view.getEnemyImage().getWidth(null) - shrinkFactor, view.getEnemyImage().getHeight(null) - shrinkFactor, null);			}
		}
	}
	
	public class MainTimer implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			inGame();
			moveBackground();
			moveChicken();
			moveApple();
			moveMissle();
			moveEnemy();
			checkChickenEnemyCollision();
			view.repaint();
		}
		
		private void inGame() {
			if(!inGame) {
				model.getAppleTimer().stop();
				model.getEnemyTimer().stop();
				model.getTimer().stop();
			}
		}
		
	}
	
	public class ChickenKeyListener implements KeyListener{

		//If you hold down a key. execute once [Pause] then execute repeatedly.
		//This boolean prevents repeating.
		
		boolean alreadyPressed = false;
		
		
		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			int keyCode = e.getKeyCode();
			
			switch (keyCode) {
			case KeyEvent.VK_UP:
				model.getChicken().setMoveUp(true);
				break;
			case KeyEvent.VK_LEFT:
				model.getChicken().setMoveLeft(true);
				break;
			case KeyEvent.VK_RIGHT:
				model.getChicken().setMoveRight(true);
			case KeyEvent.VK_SPACE:
				if(!alreadyPressed) {
					fireMissle();
				}
				alreadyPressed = true;
				break;
			default:
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			int keyCode = e.getKeyCode();
			
			switch (keyCode) {
			//Move Up is handled in Chicken Class.
			case KeyEvent.VK_LEFT:
				model.getChicken().setMoveLeft(false);
				break;
			case KeyEvent.VK_RIGHT:
				model.getChicken().setMoveRight(false);
				break;
			case KeyEvent.VK_SPACE:
				alreadyPressed = false;
				break;
			default:
				break;
			}
		}
		
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub	
		}

	}
	
	class AppleSpawner implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(new Random().nextInt(3) + 1 == 3) {
				model.getApples().add(new Apple());
			}
		}
		
	}
	
	class EnemySpawner implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			model.getEnemies().add(new Enemy());
		}
		
	}
	
	private void moveBackground() {
		model.getBgPoint1().x -= Constrants.BG_DX;
		model.getBgPoint2().x -= Constrants.BG_DX;
		
		if(model.getBgPoint1().x <= view.getBg1().getWidth(null) * - 1) {
			model.getBgPoint1().x = view.getBg1().getWidth(null);
		}else if (model.getBgPoint2().x <= view.getBg2().getWidth(null) * - 1) {
			model.getBgPoint2().x = view.getBg2().getWidth(null);
		}
		
	}
	
	private void moveChicken() {
		if(model.getChicken().isMoveLeft()) {
			//Is the user attempting to move the chicken outside of the left boundary?
			if(model.getChicken().getLocation().x <= 0) {
				model.getChicken().setMoveLeft(false);
				return;
			}
			
			if(model.getChicken().getLocation().x % 3 == 0) {
				view.switchCurrChickenImage();
			}
			
			model.getChicken().moveLeft();
		}else if(model.getChicken().isMoveRight()) {
			
			if(model.getChicken().getLocation().x >= Constrants.FRAME_WIDTH - view.getChickenImage().getWidth(null)) {
				model.getChicken().setMoveRight(false);
				return;
			}
			
			if(model.getChicken().getLocation().x % 3 == 0) {
				view.switchCurrChickenImage();
			}
			
			model.getChicken().moveRight();
		}
		
		if(model.getChicken().isMoveUp()) {
			model.getChicken().moveUp();
		}
	}
	
	private void moveApple() {
		int index = 0;
		for(Apple apple : model.getApples()) {
			apple.moveLeft();
			checkAppleChickenCollision();
			
			if(apple.isEaten()) {
				apple.setLocation(apple.getX(), apple.getY() - Constrants.APPLE_EATEN_DY);;
				apple.setAlpha(apple.getAlpha() - Constrants.APPLE_ALPHA_INCREMENT);
				if(apple.getAlpha() < 0) {
					model.getApples().remove(index);
				}
			}
			
			index++;
		}
		
		if(!model.getApples().isEmpty()) {
			if(model.getApples().get(0).getLocation().x < -1 * view.getAppleImage().getWidth(null)) {
				model.getApples().remove(0);
			}
		}
	}
	
	private void checkAppleChickenCollision() {
		int index = 0;
		
		for(Apple apple : model.getApples()) {
			int appleChickenDistanceX = Math.abs((model.getChicken().getLocation().x - apple.getLocation().x));
			int appleChickenDistanceY = Math.abs((model.getChicken().getLocation().y - apple.getLocation().y));
			int appleWidth = view.getAppleImage().getWidth(null);
			int appleHeight = view.getAppleImage().getHeight(null);
			
			if(appleChickenDistanceY <= appleHeight && appleChickenDistanceX < appleWidth) {
				if(!apple.isEaten()) {
					model.addAppleScore();
				}
				model.getApples().get(index).setEaten(true);
			}
			
			index++;
		}
	}
	
	private void checkMissleEnemyCollision(Enemy enemy, int j) {
		int i = 0;
		for(Missle missle : model.getMissles()) {
			int enemyMissleDistanceX = Math.abs(missle.getLocation().x - enemy.getLocation().x);
			int enemyMissleDistanceY = Math.abs(missle.getLocation().y - enemy.getLocation().y);
			int missleWidth = view.getMissleImage().getWidth(null);
			int enemyHeight = view.getMissleImage().getHeight(null);
			
			if(enemyMissleDistanceX <= missleWidth / 2 && enemyMissleDistanceY <= enemyHeight) {
				enemy.setHealth(enemy.getHealth() - 1);
				model.getMissles().remove(i);
				
				if(enemy.getHealth() <= 0) {
					model.getEnemies().remove(j);
				}
			}
			
			i++;
		}
	}
	
	private void checkChickenEnemyCollision() {
		
		for(Enemy enemy : model.getEnemies()) {
			int enemyChickenDistanceX = Math.abs(model.getChicken().getLocation().x - enemy.getLocation().x);
			int enemyChickenDistanceY = Math.abs(model.getChicken().getLocation().y - enemy.getLocation().y);
			int enemyWidth = view.getEnemyImage().getWidth(null);
			int enemyHeight = view.getEnemyImage().getHeight(null);
			
			if(enemyChickenDistanceX < enemyWidth && enemyChickenDistanceY <= enemyHeight) {
				if(model.getChicken().getVelY() < 0) {
					enemy.setJumpedOn(true);
				}
				
				if(!enemy.isJumpedOn()) {
					model.getHealthBar().removeHealth();
				}
			}
		}
	}
	
	private void moveMissle() {
		
		for(Missle missle : model.getMissles()) {
			missle.moveRight();
		}
		
		if(!model.getMissles().isEmpty()) {
			if(model.getMissles().get(0).getLocation().x > Constrants.FRAME_WIDTH) {
				model.getMissles().remove(0);
			}
		}
	}
	
	private void fireMissle() {
		int x = model.getChicken().getLocation().x + view.getChickenImage().getWidth(null);
		int y = model.getChicken().getLocation().y + Constrants.MISSLE_Y_OFFSET;
		
		model.getMissles().add(new Missle(x, y));
	}
	
	private void moveEnemy() {
		int index = 0;
		
		for(Enemy enemy : model.getEnemies()) {
			enemy.moveLeft();
			
			checkMissleEnemyCollision(enemy, index);
			
			if(enemy.isJumpedOn()) {
				enemy.setLocation(enemy.getX(), enemy.getY() + Constrants.ENEMY_JUMPED_ON_DY);
				if(enemy.getLocation().y > Constrants.FRAME_HEIGHT) {
					model.getEnemies().remove(index);
				}
			}
			
			index++;
		}
		
		if(!model.getEnemies().isEmpty()) {
			if(model.getEnemies().get(0).getLocation().x % 20 == 0) {
				view.switchCurrEnemyImage();
			}
			
			if(model.getEnemies().get(0).getLocation().x < -1 * view.getEnemyImage().getWidth(null)) {
				model.getEnemies().remove(0);
			}
		}
	}

}
