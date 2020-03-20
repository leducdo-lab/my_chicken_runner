package my_chicken_runner;

public class HealthBar {

	private float health;
	
	public HealthBar() {
		this.health = Constrants.HEALTH_MAX;
	}
	
	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}
	
	public void addHealth() {
		if(this.health < 100) {
			this.health += 1;
		}
	}
	
	public void removeHealth() {
		if(this.health > 0) {
			this.health -= 1;
		}
	}
	
	public int  getX() {
		return Constrants.HEALTHBAR_X + Constrants.HEALTHBAR_WIDTH - (int)(this.health / Constrants.HEALTH_MAX * Constrants.HEALTHBAR_WIDTH);
	}

	public float getHealthPercentage() {
		return this.health / Constrants.HEALTH_MAX;
	}
}
