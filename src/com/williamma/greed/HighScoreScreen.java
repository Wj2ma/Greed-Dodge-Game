package com.williamma.greed;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.preference.PreferenceManager;
import android.util.Log;

import com.williamma.framework.Game;
import com.williamma.framework.Graphics;
import com.williamma.framework.Input.TouchEvent;
import com.williamma.framework.Screen;

public class HighScoreScreen extends Screen {

	enum Gamestate {
		Normal, Reset
	}

	private Gamestate state = Gamestate.Normal;

	private Typeface titleFont, boldFont;
	private Paint paint, paint2;
	private int[] highscores = new int[4], highscores2 = new int[4],
			coinCounts = new int[5], mostBalls = new int[4];
	private SharedPreferences saves;
	private int lifetimeCoins, totalPowerups, totalDeaths, hours, minutes,
			seconds, page = 1;
	private double[] averageScore = new double[4],
			averageCoins = new double[4];
	private double averageTime, averagePowerups;
	private Animation[] coinAnim;
	private DecimalFormat format = new DecimalFormat("0.00"), format2 = new DecimalFormat("0.0");
	private int startX, startY;
	
	private ArrayList<Integer> achievePopup = new ArrayList<Integer>();
	private int bannerY = 800;
	private long achieveTime;
	boolean starter = false;

	public HighScoreScreen(Game game, Typeface font) {
		super(game);
		titleFont = font;
		boldFont = Typeface.create((String) null, Typeface.BOLD);
		paint = new Paint();
		paint.setTypeface(titleFont);
		paint2 = new Paint();
		paint2.setTypeface(boldFont);

		coinAnim = new Animation[5];
		coinAnim[0] = new Animation();
		coinAnim[0].addFrame(Assets.coin1.resize(50, 50), 100);
		coinAnim[0].addFrame(Assets.coin2.resize(50, 50), 100);
		coinAnim[0].addFrame(Assets.coin3.resize(50, 50), 100);
		coinAnim[0].addFrame(Assets.coin2.resize(50, 50), 100);
		coinAnim[1] = new Animation();
		coinAnim[1].addFrame(Assets.coin4.resize(50, 50), 100);
		coinAnim[1].addFrame(Assets.coin5.resize(50, 50), 100);
		coinAnim[1].addFrame(Assets.coin3.resize(50, 50), 100);
		coinAnim[1].addFrame(Assets.coin5.resize(50, 50), 100);
		coinAnim[2] = new Animation();
		coinAnim[2].addFrame(Assets.coin6.resize(50, 50), 100);
		coinAnim[2].addFrame(Assets.coin7.resize(50, 50), 100);
		coinAnim[2].addFrame(Assets.coin3.resize(50, 50), 100);
		coinAnim[2].addFrame(Assets.coin7.resize(50, 50), 100);
		coinAnim[3] = new Animation();
		coinAnim[3].addFrame(Assets.coin8.resize(50, 50), 100);
		coinAnim[3].addFrame(Assets.coin9.resize(50, 50), 100);
		coinAnim[3].addFrame(Assets.coin3.resize(50, 50), 100);
		coinAnim[3].addFrame(Assets.coin9.resize(50, 50), 100);
		coinAnim[4] = new Animation();
		coinAnim[4].addFrame(Assets.coin10.resize(50, 50), 100);
		coinAnim[4].addFrame(Assets.coin11.resize(50, 50), 100);
		coinAnim[4].addFrame(Assets.coin3.resize(50, 50), 100);
		coinAnim[4].addFrame(Assets.coin11.resize(50, 50), 100);

		// get saves
		saves = PreferenceManager
				.getDefaultSharedPreferences((SampleGame) game);
		load();
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		int len = touchEvents.size();
		if (state == Gamestate.Normal) {
			for (int i = 0; i < len; i++) {
				TouchEvent e = touchEvents.get(i);
				if (e.type == TouchEvent.TOUCH_DOWN) {
					startX = e.x;
					startY = e.y;
				} else if (e.type == TouchEvent.TOUCH_UP) {
					if (inBounds(e, 140, 700, 200, 75) && inBounds(startX, startY, 140, 700, 200, 75))
						state = Gamestate.Reset;
					else if (page > 1 && inBounds(startX, startY, 5, 630, 150, 50)
							&& inBounds(e, 5, 630, 150, 50))
						page--;
					else if (page < 4 && inBounds(startX, startY, 325, 630, 150, 50)
							&& inBounds(e, 325, 630, 150, 50))
						page++;
				}
			}
			for (int i = 0; i < 5; i++)
				coinAnim[i].update(10);
		} else
			for (int i = 0; i < len; i++) {
				TouchEvent e = touchEvents.get(i);
				if (e.type == TouchEvent.TOUCH_DOWN) {
					startX = e.x;
					startY = e.y;
				} else if (e.type == TouchEvent.TOUCH_UP) {
					if (inBounds(e, 80, 580, 120, 75) && inBounds(startX, startY, 80, 580, 120, 75)) {
						resetScores();
						state = Gamestate.Normal;
					} else if (inBounds(e, 280, 580, 120, 75) && inBounds(startX, startY, 280, 580, 120, 75))
						state = Gamestate.Normal;
				}
			}
		if (((SampleGame) game).getAchievements(44) == 0 && ((SampleGame) game).isOpened()) {
			((SampleGame) game).setAchievements(1,44);
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

		if (state == Gamestate.Normal) {
			g.drawARGB(255, 255, 255, 255);

			g.drawRect(140, 700, 200, 75, Color.BLACK);
			paint.setTextSize(100f);
			g.drawString("Stats", 130, 100, paint);

			if (page == 1)
				g.drawRect(5, 630, 150, 50, Color.GRAY);
			else
				g.drawRect(5, 630, 150, 50, Color.BLACK);
			if (page == 4)
				g.drawRect(325, 630, 150, 50, Color.GRAY);
			else
				g.drawRect(325, 630, 150, 50, Color.BLACK);

			paint2.setColor(Color.WHITE);
			paint2.setTextSize(30f);
			paint2.setTextAlign(Align.LEFT);
			g.drawString("Previous", 17, 665, paint2);
			g.drawString("Next", 365, 665, paint2);
			paint2.setColor(Color.BLACK);
			g.drawString("Page " + page + "/4", 180, 665, paint2);

			paint2.setTextSize(50f);
			paint2.setColor(Color.WHITE);
			g.drawString("Reset", 175, 755, paint2);
			paint2.setTextSize(25);
			paint2.setColor(Color.BLACK);

			if (page == 1) {
				g.drawString("Difficulty:", 20, 180, paint2);
				g.drawString("Total Coins:", 150, 180, paint2);
				g.drawString("Most Coins", 315, 180, paint2);
				g.drawString("Collected:", 315, 215, paint2);
				g.drawString("Easy: ", 20, 260, paint2);
				g.drawString("Medium:", 20, 305, paint2);
				g.drawString("Hard:", 20, 350, paint2);
				g.drawString("Insane:", 20, 395, paint2);
				int j = 0;
				for (int i = 260; i <= 395; i += 45) {
					g.drawString(Integer.toString(highscores[j]), 150, i,
							paint2);
					g.drawString(Integer.toString(highscores2[j++]), 315, i,
							paint2);
				}

				g.drawString("Lifetime Coins Earned: " + lifetimeCoins, 20,
						445, paint2);
				g.drawString("Lifetime Powerups Collected: " + totalPowerups,
						20, 495, paint2);
				g.drawString("Total Deaths: " + totalDeaths, 20, 545, paint2);
				g.drawString("Total Gametime: " + hours + "h  " + minutes
						+ "m  " + seconds + "s", 20, 595, paint2);
				g.drawHollowRect(2, 140, 478, 270, Color.BLACK, 5);
				g.drawHollowRect(2, 410, 478, 50, Color.BLACK, 5);
				for (int i = 460; i <= 560; i += 50)
					g.drawHollowRect(2, i, 478, 50, Color.BLACK, 5);
			} else if (page == 2) {
				g.drawString("Coin Collection:", 20, 180, paint2);
				int j = 0;
				for (int i = 199; i <= 551; i += 88)
					g.drawImage(coinAnim[j++].getImage(), 20, i);
				g.drawString("Yellow Coins: " + coinCounts[0], 95, 233, paint2);
				g.drawString("Cyan Coins: " + coinCounts[1], 95, 321, paint2);
				g.drawString("Blue Coins: " + coinCounts[2], 95, 409, paint2);
				g.drawString("Green Coins: " + coinCounts[3], 95, 497, paint2);
				g.drawString("Pink Coins: " + coinCounts[4], 95, 585, paint2);
				g.drawHollowRect(2, 140, 478, 475, Color.BLACK, 5);
			} else if (page == 3 ){
				g.drawString("Difficulty:", 20, 180, paint2);
				g.drawString("Average", 150, 180, paint2);
				g.drawString("Score:", 150, 215, paint2);
				g.drawString("Average Coins", 300, 180, paint2);
				g.drawString("Collected:", 300, 215, paint2);
				g.drawString("Easy: ", 20, 260, paint2);
				g.drawString("Medium:", 20, 305, paint2);
				g.drawString("Hard:", 20, 350, paint2);
				g.drawString("Insane:", 20, 395, paint2);
				int j = 0;
				for (int i = 260; i <= 395; i += 45) {
					g.drawString(format.format(averageScore[j]), 150, i,
							paint2);
					g.drawString(format.format(averageCoins[j++]), 300, i,
							paint2);
				}
				g.drawHollowRect(2, 140, 478, 270, Color.BLACK, 5);
				g.drawString("Average Time per Game: " + format2.format(averageTime) + "s", 20, 445, paint2);
				g.drawHollowRect(2, 410, 478, 50, Color.BLACK, 5);
				g.drawString("Average Powerups per Game: " + format2.format(averagePowerups), 20, 495, paint2);
				g.drawHollowRect(2,460,478,50,Color.BLACK, 5);
			} else if (page == 4) {
				g.drawString("Difficulty:", 20, 180, paint2);
				g.drawString("Most Cannonballs", 190, 180, paint2);
				g.drawString("Present", 245, 215, paint2);
				g.drawString("Easy: ", 20, 260, paint2);
				g.drawString("Medium:", 20, 305, paint2);
				g.drawString("Hard:", 20, 350, paint2);
				g.drawString("Insane:", 20, 395, paint2);
				int j = 0;
				for (int i = 260; i <= 395; i += 45)
					g.drawString(Integer.toString(mostBalls[j++]), 270, i, paint2);
				g.drawHollowRect(2, 140, 478, 270, Color.BLACK, 5);
			}

		} else {
			g.drawARGB(255, 0, 0, 0);
			paint2.setTextSize(60f);
			paint2.setColor(Color.WHITE);
			g.drawString("Are you sure you", 20, 100, paint2);
			g.drawString("want to reset?", 53, 200, paint2);
			paint2.setColor(Color.RED);
			paint2.setTextSize(40f);
			g.drawString("WARNING: Resetting will", 20, 300, paint2);
			g.drawString("erase all saves including", 20, 350, paint2);
			g.drawString("purchased items at the", 20, 400, paint2);
			g.drawString("shop and achievements.", 20, 450, paint2);
			g.drawRect(80, 580, 120, 75, Color.YELLOW);
			g.drawRect(280, 580, 120, 75, Color.YELLOW);
			paint2.setTextSize(50f);
			paint2.setColor(Color.BLACK);
			g.drawString("Yes", 100, 635, paint2);
			g.drawString("No", 308, 635, paint2);
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
		if (state == Gamestate.Normal)
			game.setScreen(new MainMenuScreen(game, titleFont));
		else
			state = Gamestate.Normal;
	}

	private void load() {
		for (int i = 0; i < 4; i++) {
			highscores[i] = ((SampleGame) game).getHighscores(i);
			highscores2[i] = ((SampleGame) game).getMostCoins(i);
			averageScore[i] = ((SampleGame) game).getAverageScore(i);
			averageCoins[i] = ((SampleGame) game).getAverageCoins(i);
			mostBalls[i] = ((SampleGame) game).getMostBalls(i);
		}
		lifetimeCoins = ((SampleGame) game).getLifetimeCoins();
		totalPowerups = ((SampleGame) game).getTotalPowerups();
		totalDeaths = ((SampleGame) game).getDeaths();
		int playTime = ((SampleGame) game).getPlayTime();
		averageTime = playTime*1.0/totalDeaths;
		averagePowerups = ((SampleGame) game).getTotalPowerups()*1.0/totalDeaths;
		seconds = playTime % 60;
		playTime = (playTime - seconds) / 60;
		if (playTime > 0) {
			minutes = playTime % 60;
			hours = (playTime - minutes) / 60;
			if (hours < 0)
				hours = 0;
		} else {
			minutes = 0;
			hours = 0;
		}
		for (int i = 0; i < 5; i++)
			coinCounts[i] = ((SampleGame) game).getCoinCount(i);
		
	}

	private void resetScores() {
		SharedPreferences.Editor editor = saves.edit();
		editor.clear();
		editor.putFloat("Sensitivity",
				(float) ((SampleGame) game).getSensitivity());
		editor.putBoolean("Mute", ((SampleGame) game).isMuted());
		editor.commit();
		((SampleGame) game).load();
		load();
	}
}