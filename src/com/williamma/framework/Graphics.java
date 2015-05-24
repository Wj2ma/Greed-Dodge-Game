package com.williamma.framework;


import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

public interface Graphics {
	public static enum ImageFormat {
		ARGB8888, ARGB4444, RGB565
	}

	public Image newImage(String fileName, ImageFormat format);

	public void clearScreen(int color);

	public void drawLine(int x, int y, int x2, int y2, int color);

	public void drawLine(int x, int y, int x2, int y2, int color, float strokeWidth);
	
	public void drawRect(int x, int y, int width, int height, int color);
	
	public void drawHollowRect(int x, int y, int width, int height, int color, float strokeWidth);

	public void drawImage(Image image, int x, int y, int srcX, int srcY,
			int srcWidth, int srcHeight);

	public void drawImage(Image Image, int x, int y);

	void drawString(String text, int x, int y, Paint paint);

	public int getWidth();

	public int getHeight();

	public void drawARGB(int i, int j, int k, int l);
	
	public void drawCircle(int cx, int cy, int radius, int color);
	
	public void drawHollowCircle(int cx, int cy, int radius, int color, float strokeWidth);
	
	public void drawArc(RectF oval, int startAngle, int sweepAngle, boolean useCenter, int color);
	
	public void drawHollowArc(RectF oval, int startAngle, int sweepAngle,
			boolean useCenter, int color, float strokeWidth);
	
	public void rotate(int degrees, int px, int py);
	
	public void save();
	
	public void restore();
}
