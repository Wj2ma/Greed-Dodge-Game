package com.williamma.greed;

public class Ball {

	private int x, y, slopeX, slopeY, speed, defaultSpeed, trackX = 0, trackY = 0, dirX,
			dirY, size = 20;
	private boolean alternate = true, hit = false;
	private long[] timer;
	private You you;

	public Ball(int startX, int startY, int slope1, int slope2, int s, int d1,
			int d2, You me) {
		x = startX;
		y = startY;
		you = me;

		if (slope1 == 0) {
			slopeX = slope1;
			slopeY = slope2;
		} else if (slope2 / slope1 == 0) {
			slopeY = (int) Math.round(((slope2 * 1.0) / slope1) * 10);
			slopeX = 10;
		} else if (Math.ceil(slope2 * 1.0 / slope1) != Math.floor(slope2 * 1.0
				/ slope1)) {
			slopeX = 1;
			slopeY = (int) Math.round(slope2 * 1.0 / slope1);
		} else {
			slopeX = slope1;
			slopeY = slope2;
		}
		speed = s;
		defaultSpeed = s;
		dirX = d1;
		dirY = d2;
	}

	public void update() {
		for (int i = 0; i < speed; i++) {
			if (alternate) {
				alternate = false;
				if (trackX != slopeX) {
					if (x >= 480 - size / 2)
						dirX = -1;
					else if (x <= size / 2)
						dirX = 1;
					x += dirX;
					trackX++;
				}

				else if (trackY != slopeY) {
					if (y >= 600 - size / 2)
						dirY = -1;
					else if (y <= size / 2)
						dirY = 1;
					y += dirY;
					trackY++;
				}

				else {
					trackX = 0;
					trackY = 0;
					i--;
				}
			} else {
				alternate = true;
				if (trackY != slopeY) {
					if (y >= 600 - size / 2)
						dirY = -1;
					else if (y <= size / 2)
						dirY = 1;
					y += dirY;
					trackY++;
				}

				else if (trackY == slopeY && trackX != slopeX) {
					if (x >= 480 - size / 2)
						dirX = -1;
					else if (x <= size / 2)
						dirX = 1;
					x += dirX;
					trackX++;
				}

				else {
					trackX = 0;
					trackY = 0;
					i--;
				}
			}
		}

		checkCollision();
	}

	public void checkCollision() {
			double distance = Math.sqrt(Math.pow(x - you.getX(), 2)
					+ Math.pow(y - you.getY(), 2));
			if (distance < .85 * (size / 2 + you.getSize() / 2)) {
				if (!you.isInvincible())
					hit = true;
				else
					you.setShielded(true);
			}
	}

	public boolean checkCollision(int centerX, int centerY, int radius) {
		double distance = Math.sqrt(Math.pow(x - centerX, 2)
				+ Math.pow(y - centerY, 2));
		if (distance < .85 * (radius + size / 2))
			return true;
		else
			return false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isHit() {
		return hit;
	}

	public void setSize(int s) {
		size = s;
	}

	public int getSize() {
		return size;
	}

	public void setSpeed(int s) {
		if (s > 0)
			speed = s;
		else
			speed = 1;

	}

	public int getSpeed() {
		return speed;
	}
	
	public int getDefaultSpeed() {
		return defaultSpeed;
	}
}
