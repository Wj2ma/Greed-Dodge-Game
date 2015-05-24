package com.williamma.greed;

public class You {

	private int x = 240, y = 400, size = 32, pickupRadius, diamondSize = 42;
	private boolean invincible = false, diamondSkin = false, shielded = false;

	public boolean isInvincible() {
		return invincible;
	}

	public void setInvincible(boolean invincible) {
		this.invincible = invincible;
	}
	
	public boolean isDiamond() {
		return diamondSkin;
	}
	
	public void setDiamond(boolean d) {
		diamondSkin = d;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setSize(int s) {
		if (diamondSkin) {
			if (s == 15)
				diamondSize = 21;
			else if (s == 50)
				diamondSize = 70;
			else
				diamondSize = 42;
		}
		
		size = s;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getDiamondSize() {
		return diamondSize;
	}
	
	public void setPickupRadius(int r) {
		pickupRadius = r;
	}
	
	public int getPickupRadius() {
		return pickupRadius;
	}
	
	public void setShielded(boolean s) {
		shielded = s;
	}
	
	public boolean isShielded() {
		return shielded;
	}
	
	public void update(int newX, int newY)
	{
		x += newX;
		y += newY;
		
		if (x < size/2)
			x = size/2;
		else if (x > 480-size/2)
			x = 480-size/2;

		if (y>600-size/2)
			y = 600-size/2;
		else if (y<size/2)
			y = size/2;
	}
}
