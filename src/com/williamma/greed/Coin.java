package com.williamma.greed;

public class Coin {

	private int x, y, prevX, prevY;
	private boolean hit = false;
	private You you;

	public Coin(int startX, int startY, You me) {
		x = startX;
		y = startY;
		you = me;
	}

	public void update() {
		checkCollision();
	}
	
	public void checkCollision() {
		double distance = Math.sqrt(Math.pow(x-you.getX(),2) + Math.pow(y-you.getY(),2));
		if (distance < .85*(you.getPickupRadius() + you.getSize()/2))
		{
			hit = true;
			prevX = x;
			prevY = y;
			x = 10+(int)(Math.random()*460);
			y = 30+(int)(Math.random()*560);
		}
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getPrevX() {
		return prevX;
	}
	
	public int getPrevY() {
		return prevY;
	}
	
	public boolean isHit() {
		return hit;
	}
	
	public void setHit(boolean newH) {
		hit = newH;
	}
	
	public void setX(int newX) {
		x = newX;
	}
	
	public void setY(int newY) {
		y = newY;
	}
}
