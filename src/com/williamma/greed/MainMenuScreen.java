package com.williamma.greed;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;

import com.williamma.framework.Game;
import com.williamma.framework.Graphics;
import com.williamma.framework.Input.TouchEvent;
import com.williamma.framework.Screen;

public class MainMenuScreen extends Screen{

	private Typeface titleFont, boldFont;
	private Paint paint, paint2;
	private int eX, eY;
	SharedPreferences saves;
	SharedPreferences.Editor editor;
	private boolean easterEgg, easterEgg1 = false;
	private int clicks = 0;
	private long eggTime;
	boolean s = false;
	
	private ArrayList<Integer> achievePopup = new ArrayList<Integer>();
	private int bannerY = 800;
	private long achieveTime;
	boolean starter = false;
	
	public MainMenuScreen(Game game, Typeface font) {
		super(game);
		titleFont = font;
		boldFont = Typeface.create((String) null, Typeface.BOLD);
		paint = new Paint();
		paint.setTypeface(titleFont);
		paint2 = new Paint();
		paint2.setTypeface(boldFont);
		
		saves = PreferenceManager.getDefaultSharedPreferences(((SampleGame) game));
		editor = saves.edit();
		easterEgg = saves.getBoolean("Easter Egg1", false);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent e = touchEvents.get(i);
			if (e.type == TouchEvent.TOUCH_DOWN) {
					eX = e.x;
					eY = e.y;
			} else if (e.type == TouchEvent.TOUCH_UP) {
				if (inBounds(e, 85, 175, 310, 68) && inBounds(eX, eY, 85, 175, 310, 68))
					game.setScreen(new DifficultyScreen(game, titleFont));
				else if (inBounds(e, 85, 270, 310, 68) && inBounds(eX, eY, 85, 270, 310, 68))
					game.setScreen(new ShopScreen(game, titleFont));
				else if (inBounds(e, 85, 365, 310, 68) && inBounds(eX, eY, 85, 365, 310, 68))
					game.setScreen(new InstructionsScreen(game, titleFont));
				else if (inBounds(e, 85, 460, 310, 68) && inBounds(eX, eY, 85, 460, 310, 68))
					game.setScreen(new OptionsScreen(game, titleFont));
				else if (inBounds(e, 85, 555, 310, 68) && inBounds(eX, eY, 85, 555, 310, 68))
					game.setScreen(new AchievementsScreen(game, titleFont));
				else if (inBounds(e, 85, 650, 310, 68) && inBounds(eX, eY, 85, 650, 310, 68))
					game.setScreen(new HighScoreScreen(game, titleFont));
				else if (inBounds(e, 240,750,160,20) && inBounds(eX, eY, 240,750,160,20)) {
					clicks++;
					if (clicks >= 7 && !easterEgg) {
						easterEgg = true;
						easterEgg1 = true;
						((SampleGame) game).setTotalCoins(((SampleGame) game).getTotalCoins() + 1000);
						editor.putInt("Total Coins", ((SampleGame) game).getTotalCoins());
						editor.putBoolean("Easter Egg1", true);
						editor.commit();
					}
				}
			}
		}
		
		if (((SampleGame) game).getAchievements(44) == 0 && ((SampleGame) game).isOpened()) {
			((SampleGame) game).setAchievements(1,44);
			editor.putInt("Achievements44", 1);
			editor.commit();
			achievePopup.add(44);
		}
	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}
	
	private boolean inBounds(int ex,int ey, int x, int y, int width,
			int height) {
		if (ex > x && ex < x + width - 1 && ey > y
				&& ey < y + height - 1)
			return true;
		else
			return false;
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		// Title
		g.drawARGB(255, 255, 255, 255);
		paint.setTextSize(150f);
		paint.setColor(Color.BLACK);
		g.drawString("Greed", 50, 140, paint);
		// Buttons
		g.drawRect(85, 175, 310, 70, Color.BLACK);
		g.drawRect(85, 270, 310, 70, Color.BLACK);
		g.drawRect(85, 365, 310, 70, Color.BLACK);
		g.drawRect(85, 460, 310, 70, Color.BLACK);
		g.drawRect(85, 555, 310, 70, Color.BLACK);
		g.drawRect(85, 650, 310, 70, Color.BLACK);
		// Strings
		paint2.setTextSize(30f);
		paint2.setColor(Color.BLACK);
		paint2.setTextAlign(Align.LEFT);
		g.drawString("Created By: William Ma", 83, 770, paint2);
		paint2.setColor(Color.WHITE);
		paint2.setTextSize(45f);
		g.drawString("Start", 185, 225, paint2);
		g.drawString("Shop", 185, 320, paint2);
		g.drawString("Instructions", 118, 415, paint2);
		g.drawString("Options", 160, 510, paint2);
		g.drawString("Achievements", 100, 605, paint2);
		g.drawString("Stats", 185, 700, paint2);
		
		//Achievements
		if (achievePopup.size() > 0) {
			if (bannerY > 665 && !starter)
				bannerY -= 5;
			else if (!starter && bannerY == 665) {
				achieveTime = System.currentTimeMillis();
				starter = true;
			}

			g.drawRect(55, bannerY, 370, 70, Color.GREEN);
			g.drawHollowRect(65, bannerY + 10, 350, 50, Color.WHITE, 5);
			paint2.setTextSize(25f);
			paint2.setColor(Color.BLACK);
			paint2.setTextAlign(Align.CENTER);
			String label = ((SampleGame) game).getAchieveTitle(
					achievePopup.get(0), ((SampleGame) game).getAchievements(achievePopup.get(0)));
			Rect bounds = new Rect();
			paint2.getTextBounds(label, 0, label.length(), bounds);
			g.drawString(label, 240, bannerY + 35
					+ (bounds.bottom - bounds.top) / 2, paint2);
			if (achieveTime != 0 && System.currentTimeMillis() - achieveTime > 4000) {
				bannerY += 5;
				if (bannerY == 800) {
					achieveTime = 0;
					starter = false;
					achievePopup.remove(0);
				}
			}
		}
		
		//Easter Egg
		if (easterEgg1) {
			if (!s) {
				eggTime = System.currentTimeMillis();
				s = true;
			}
			
			g.drawRect(45, 655, 390, 70, Color.argb(255, 132, 112, 255));
			g.drawHollowRect(55, 665, 370, 50, Color.WHITE, 5);
			paint2.setTextSize(25f);
			paint2.setColor(Color.BLACK);
			paint2.setTextAlign(Align.CENTER);
			String label = "Hidden Treasure: +1000 Coins!";
			Rect bounds = new Rect();
			paint2.getTextBounds(label, 0, label.length(), bounds);
			g.drawString(label, 240, 690 + (bounds.bottom - bounds.top) / 2, paint2);
			
			if (System.currentTimeMillis() - eggTime > 4000)
				easterEgg1 = false;
		}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {
		android.os.Process.killProcess(android.os.Process.myPid());

	}
}
