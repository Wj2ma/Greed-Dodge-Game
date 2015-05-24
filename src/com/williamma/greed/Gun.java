package com.williamma.greed;

public class Gun {

	private int x, y, theta;
	private boolean backwards = false, backwards2 = false;

	public Gun(int startX, int startY) {
		x = startX;
		y = startY;
		theta = 1;
	}

	public void update() {
		if (x == 50)
			backwards = false;
		else if (x == 380)
			backwards = true;
		if (backwards)
			x -= 1;
		else
			x += 1;

		if (theta == 80)
			backwards2 = true;
		if (theta == -80)
			backwards2 = false;
		if (backwards2)
			theta--;
		else
			theta++;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getTheta() {
		return theta;
	}
}
