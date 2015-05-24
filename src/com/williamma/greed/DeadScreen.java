package com.williamma.greed;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.preference.PreferenceManager;

import com.williamma.framework.Game;
import com.williamma.framework.Graphics;
import com.williamma.framework.Input.TouchEvent;
import com.williamma.framework.Screen;

public class DeadScreen extends Screen {

	private Typeface titleFont, boldFont;
	private Paint paint, paint2;
	private int score, difficulty;
	private boolean[] pressed = new boolean[2];
	private boolean highscore;
	private int totalCoins, increment = 0, coins;
	private long time;
	
	private ArrayList<Integer> achievePopup = new ArrayList<Integer>();
	private int bannerY = 800;
	private long achieveTime;
	boolean starter = false;
	private int[] earnerT = new int[4], deathT = new int[4], timeT = new int[3], coinT = new int[3];

	public DeadScreen(Game game, Typeface font, int s, int d, boolean h1, boolean w, boolean q, boolean sn) {
		super(game);
		titleFont = font;
		boldFont = Typeface.create((String) null, Typeface.BOLD);
		paint = new Paint();
		paint.setTypeface(titleFont);
		paint2 = new Paint();
		paint2.setTypeface(boldFont);
		score = s;
		difficulty = d;
		highscore = h1;
		coins = ((SampleGame) game).getTotalCoins();
		if (difficulty == GameScreen.EASY)
			totalCoins = s;
		else if (difficulty == GameScreen.MEDIUM)
			totalCoins = 2 * s;
		else if (difficulty == GameScreen.HARD)
			totalCoins = 3 * s;
		else
			totalCoins = 4 * s;
		time = System.currentTimeMillis();
		
		if (w)
			achievePopup.add(18);
		if (q)
			achievePopup.add(35);
		if (sn)
			achievePopup.add(38);
		
		SharedPreferences saves = PreferenceManager.getDefaultSharedPreferences((SampleGame) game);
		SharedPreferences.Editor editor = saves.edit();
		earnerT[0] = 1000;
		earnerT[1] = 10000;
		earnerT[2] = 100000;
		earnerT[3] = 250000;
		deathT[0] = 1;
		deathT[1] = 10;
		deathT[2] = 100;
		deathT[3] = 1000;
		timeT[0] = 3600;
		timeT[1] = 10800;
		timeT[2] = 18000;
		coinT[0] = 1000;
		coinT[1] = 10000;
		coinT[2] = 30000;
		
		int value = ((SampleGame) game).getAchievements(5);
		if (value < 4 && ((SampleGame) game).getLifetimeCoins() >= earnerT[value]) {
			value++;
			((SampleGame) game).setAchievements(value, 5);
			editor.putInt("Achievements5", value);
			editor.commit();
			achievePopup.add(5);
		}
		value = ((SampleGame) game).getAchievements(10);
		if (value < 4 && ((SampleGame) game).getDeaths() >= deathT[value]) {
			value++;
			((SampleGame) game).setAchievements(value, 10);
			editor.putInt("Achievements10", value);
			editor.commit();
			achievePopup.add(10);
		}
		value = ((SampleGame) game).getAchievements(11);
		if (value < 3 && ((SampleGame) game).getPlayTime() >= timeT[value]) {
			value++;
			((SampleGame) game).setAchievements(value, 11);
			editor.putInt("Achievements11", value);
			editor.commit();
			achievePopup.add(11);
		}
		value = ((SampleGame) game).getAchievements(15);
		if (value < 3 && coins >= coinT[value]) {
			value++;
			((SampleGame) game).setAchievements(value, 15);
			editor.putInt("Achievements15", value);
			editor.commit();
			achievePopup.add(15);
		}
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent e = touchEvents.get(i);
			if (e.type == TouchEvent.TOUCH_DOWN) {
				if (inBounds(e, 60, 600, 150, 75))
					pressed[0] = true;
				else if (inBounds(e, 270, 600, 150, 75))
					pressed[1] = true;
			} else if (e.type == TouchEvent.TOUCH_UP) {
				if (inBounds(e, 60, 600, 150, 75) && pressed[0]) {
					game.setScreen(new GameScreen(game, titleFont, difficulty));
				} else if (inBounds(e, 270, 600, 150, 75) && pressed[1]) {
					game.setScreen(new MainMenuScreen(game, titleFont));
				} else {
					for (int j = 0; j < 2; j++)
						pressed[j] = false;
					if (System.currentTimeMillis()-time>600)
						increment = totalCoins;
				}
			}
		}
		if (System.currentTimeMillis()-time>600)
			if (increment < totalCoins)
				increment++;
		
		if (((SampleGame) game).getAchievements(44) == 0 && ((SampleGame) game).isOpened()) {
			((SampleGame) game).setAchievements(1,44);
			SharedPreferences saves = PreferenceManager.getDefaultSharedPreferences((SampleGame)game);
			SharedPreferences.Editor editor = saves.edit();
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

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawARGB(255, 0, 0, 0);

		paint.setTextSize(200f);
		paint.setColor(Color.WHITE);
		g.drawString("GG", 113, 200, paint);
		paint2.setTextSize(50f);
		paint2.setColor(Color.WHITE);
		paint2.setTextAlign(Align.LEFT);
		g.drawString("Final Score: " + score, 103-(15*Integer.toString(score).length()), 300, paint2);
		g.drawString("Total Coins Earned:", 22, 400, paint2);
		paint2.setTextSize(100f);
		g.drawString(Integer.toString(increment), 245-(30*Integer.toString(increment).length()), 500, paint2);
		paint2.setTextSize(45f);
		g.drawRect(60, 600, 150, 75, Color.YELLOW);
		g.drawRect(270, 600, 150, 75, Color.YELLOW);
		paint2.setColor(Color.BLACK);
		g.drawString("Retry", 78, 650, paint2);
		g.drawString("Menu", 288, 650, paint2);
		paint2.setColor(Color.WHITE);
		g.drawString("Total Coins: " + coins, 20, 750, paint2);
		if (highscore) {
			paint2.setTextSize(40f);
			g.drawString("New Highscore!", 95, 560, paint2);
		}
		
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
		game.setScreen(new MainMenuScreen(game, titleFont));
	}
}
