package com.williamma.greed;

import com.williamma.framework.Image;

public class Powerup {

	private int x, y, type;
	private You you;
	private boolean hit = false, spawned = false;
	private Image[] powerups;
	private Image currImage;
	private Animation superCoin;
	private boolean silenced;

	public Powerup(int startX, int startY, You me) {
		powerups = new Image[9];
		powerups[0] = Assets.bomb;
		powerups[1] = Assets.shrink;
		powerups[2] = Assets.shrinkBall;
		powerups[3] = Assets.silence;
		powerups[4] = Assets.shield;
		powerups[5] = Assets.slowBall;
		powerups[6] = Assets.fastBall;
		powerups[7] = Assets.grow;
		powerups[8] = Assets.growBall;
		
		superCoin = new Animation();
		superCoin.addFrame(Assets.superCoin, 75);
		superCoin.addFrame(Assets.superCoin2, 75);
		superCoin.addFrame(Assets.superCoin3, 75);
		superCoin.addFrame(Assets.superCoin4, 75);
		superCoin.addFrame(Assets.superCoin3, 75);
		superCoin.addFrame(Assets.superCoin2, 75);
		
		x = startX;
		y = startY;
		you = me;
	}

	public void update() {
		checkCollision();
		if (type == 9)
			animate();
	}
	
	public Image getCurrImage() {
		return currImage;
	}

	public void setCurrImage(Image currImage) {
		this.currImage = currImage;
	}

	public void setSilenced(boolean s) {
		silenced = s;
	}
	
	private void animate() {
		superCoin.update(10);
		currImage = superCoin.getImage();
	}

	public void checkCollision() {
		if (spawned) {
			double distance = Math.sqrt(Math.pow(x - you.getX(), 2)
					+ Math.pow(y - you.getY(), 2));
			if (distance < .85 * (you.getSize() / 2 + 24)) {
				hit = true;
				spawned = false;
			}	
		}
	}

	public boolean isSpawned() {
		return spawned;
	}

	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getType() {
		return type;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void newPowerdown() {
		type = 6 + (int)(Math.random()*3);
		currImage = powerups[type];
	}
	
	public void newPowerup() {
		do {
			type = (int)(Math.random()*7);
		} while (type == 3 && silenced);
			
		if (type == 6) {
			type = 9;
			currImage = superCoin.getImage();
		}
		else
			currImage = powerups[type];
		
		
	}

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean newH) {
		hit = newH;
	}

}
