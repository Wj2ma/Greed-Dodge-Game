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

public class AchievementsScreen extends Screen {

	private Typeface titleFont, boldFont;
	private Paint paint, paint2, paint3;
	private int page = 1, purpColour;
	private int[][] achievements = new int[9][5], fullUnlocked = new int[9][5];
	private boolean[] pressed = new boolean[2];

	private ArrayList<Integer> achievePopup = new ArrayList<Integer>();
	private int bannerY = 800;
	private long achieveTime;
	boolean starter = false;

	// Thresholds
	private int[][][] thresholds = new int[9][5][5];
	private int[][] coinCountT = new int[5][5], scoreT = new int[4][3],
			current = new int[4][5];
	private int[] powerupT = new int[3], earnerT = new int[4],
			deathT = new int[4], timeT = new int[3], coinT = new int[3];

	public AchievementsScreen(Game game, Typeface font) {
		super(game);
		titleFont = font;
		boldFont = Typeface.create((String) null, Typeface.BOLD);
		paint = new Paint();
		paint.setTypeface(titleFont);
		paint2 = new Paint();
		paint2.setTypeface(boldFont);
		paint3 = new Paint();
		paint3.setTypeface(boldFont);
		purpColour = Color.argb(255, 132, 112, 255);
		int k = 0;
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 5; j++)
				achievements[i][j] = ((SampleGame) game).getAchievements(k++);

		// Max levels for each achievement
		for (int i = 0; i < 5; i++)
			fullUnlocked[0][i] = 5;
		fullUnlocked[1][0] = 4;
		for (int i = 1; i < 5; i++)
			fullUnlocked[1][i] = 3;
		fullUnlocked[2][0] = 4;
		fullUnlocked[2][1] = 3;
		fullUnlocked[2][2] = 3;
		fullUnlocked[2][3] = 4;
		fullUnlocked[2][4] = 3;
		fullUnlocked[3][0] = 3;
		for (int i = 1; i < 5; i++)
			fullUnlocked[3][i] = 1;
		for (int i = 4; i < 9; i++)
			for (int j = 0; j < 5; j++)
				fullUnlocked[i][j] = 1;

		// Set threshold values for achievements
		thresholds[0][0][0] = 1000;
		thresholds[0][0][1] = 3000;
		thresholds[0][0][2] = 5000;
		thresholds[0][0][3] = 7500;
		thresholds[0][0][4] = 10000;
		thresholds[0][1][0] = 500;
		thresholds[0][1][1] = 1000;
		thresholds[0][1][2] = 2000;
		thresholds[0][1][3] = 3500;
		thresholds[0][1][4] = 5000;
		thresholds[0][2][0] = 250;
		thresholds[0][2][1] = 500;
		thresholds[0][2][2] = 750;
		thresholds[0][2][3] = 1000;
		thresholds[0][2][4] = 1500;
		thresholds[0][3][0] = 100;
		thresholds[0][3][1] = 200;
		thresholds[0][3][2] = 300;
		thresholds[0][3][3] = 500;
		thresholds[0][3][4] = 1000;
		thresholds[0][4][0] = 50;
		thresholds[0][4][1] = 100;
		thresholds[0][4][2] = 200;
		thresholds[0][4][3] = 300;
		thresholds[0][4][4] = 500;
		thresholds[1][0][0] = 1000;
		thresholds[1][0][1] = 10000;
		thresholds[1][0][2] = 100000;
		thresholds[1][0][3] = 250000;
		thresholds[1][1][0] = 300;
		thresholds[1][1][1] = 500;
		thresholds[1][1][2] = 700;
		thresholds[1][2][0] = 200;
		thresholds[1][2][1] = 400;
		thresholds[1][2][2] = 600;
		thresholds[1][3][0] = 100;
		thresholds[1][3][1] = 300;
		thresholds[1][3][2] = 500;
		thresholds[1][4][0] = 100;
		thresholds[1][4][1] = 250;
		thresholds[1][4][2] = 400;
		thresholds[2][0][0] = 1;
		thresholds[2][0][1] = 10;
		thresholds[2][0][2] = 100;
		thresholds[2][0][3] = 1000;
		thresholds[2][1][0] = 1;
		thresholds[2][1][1] = 3;
		thresholds[2][1][2] = 5;
		thresholds[2][2][0] = 100;
		thresholds[2][2][1] = 800;
		thresholds[2][2][2] = 1500;
		thresholds[2][3][0] = 1;
		thresholds[2][3][1] = 10;
		thresholds[2][3][2] = 50;
		thresholds[2][3][3] = 95;
		thresholds[2][4][0] = 1;
		thresholds[2][4][1] = 10;
		thresholds[2][4][2] = 25;
		thresholds[3][0][0] = 1000;
		thresholds[3][0][1] = 10000;
		thresholds[3][0][2] = 30000;

		// current scores
		for (int i = 0; i < 5; i++)
			current[0][i] = ((SampleGame) game).getCoinCount(i);
		current[1][0] = ((SampleGame) game).getLifetimeCoins();
		for (int i = 1; i < 5; i++)
			current[1][i] = ((SampleGame) game).getHighscores(i - 1);
		current[2][0] = ((SampleGame) game).getDeaths();
		current[2][1] = ((SampleGame) game).getPlayTime() / 3600;
		current[2][2] = ((SampleGame) game).getTotalPowerups();
		current[2][3] = ((SampleGame) game).getUpgrades();
		current[2][4] = ((SampleGame) game).getBuySkins();
		current[3][0] = ((SampleGame) game).getTotalCoins();
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent e = touchEvents.get(i);
			if (e.type == TouchEvent.TOUCH_DOWN) {
				if (inBounds(e, 5, 720, 150, 50))
					pressed[0] = true;
				else if (inBounds(e, 325, 720, 150, 50))
					pressed[1] = true;
			} else if (e.type == TouchEvent.TOUCH_UP) {
				if (page > 1 && pressed[0] && inBounds(e, 5, 720, 150, 50))
					page--;
				else if (page < 9 && pressed[1]
						&& inBounds(e, 325, 720, 150, 50))
					page++;
				else
					for (int j = 0; j < 2; j++)
						pressed[j] = false;
			}
		}

		if (achievements[8][4] == 0 && ((SampleGame) game).isOpened()) {
			((SampleGame) game).setAchievements(1, 44);
			SharedPreferences saves = PreferenceManager
					.getDefaultSharedPreferences((SampleGame) game);
			SharedPreferences.Editor editor = saves.edit();
			editor.putInt("Achievements44", 1);
			editor.commit();
			achievePopup.add(44);
			achievements[8][4] = 1;
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
		g.drawARGB(255, 255, 255, 255);

		// title
		paint.setTextSize(76);
		paint.setColor(Color.BLACK);
		g.drawString("Achievements", 20, 80, paint);

		// Next previous and page part
		if (page == 1)
			g.drawRect(5, 720, 150, 50, Color.GRAY);
		else
			g.drawRect(5, 720, 150, 50, Color.BLACK);
		if (page == 9)
			g.drawRect(325, 720, 150, 50, Color.GRAY);
		else
			g.drawRect(325, 720, 150, 50, Color.BLACK);

		paint2.setTextAlign(Align.LEFT);
		paint2.setColor(Color.WHITE);
		paint2.setTextSize(30f);

		g.drawString("Previous", 17, 755, paint2);
		g.drawString("Next", 365, 755, paint2);
		paint2.setColor(Color.BLACK);
		g.drawString("Page " + page + "/9", 180, 755, paint2);

		// Colour the achievements green/yellow/red
		int j = 0;
		for (int i = 110; i <= 554; i += 111) {
			if (achievements[page - 1][j] == 0)
				g.drawRect(0, i, 480, 111, Color.RED);
			else if (achievements[page - 1][j] < fullUnlocked[page - 1][j])
				g.drawRect(0, i, 480, 111, Color.YELLOW);
			else
				g.drawRect(0, i, 480, 111, Color.GREEN);
			j++;
		}

		// Images
		j = 0;
		for (int i = 125; i < 665; i += 111) {
			if (achievements[page - 1][j] > 0)
				g.drawImage(
						Assets.achievements[page - 1][j][achievements[page - 1][j] - 1],
						12, i);
			else
				g.drawRect(12, i, 80, 80, Color.LTGRAY);
			j++;
		}

		// outlines
		g.drawLine(2, 110, 2, 665, Color.BLACK, 5);
		g.drawLine(478, 110, 478, 665, Color.BLACK, 5);
		for (int i = 110; i <= 665; i += 111)
			g.drawLine(0, i, 480, i, Color.BLACK, 5);
		for (int i = 125; i < 665; i += 111)
			g.drawHollowRect(12, i, 80, 80, Color.BLACK, 3);

		// Individual achievements
		paint3.setColor(Color.BLACK);
		paint3.setTextSize(20f);
		paint3.setFlags(Paint.UNDERLINE_TEXT_FLAG);
		paint2.setColor(Color.BLACK);
		// Achievement titles
		j = 0;
		for (int i = 135; i <= 579; i += 111) {
			g.drawString(((SampleGame) game).getAchieveTitle(
					(page - 1) * 5 + j, achievements[page - 1][j]), 108, i,
					paint3);
			j++;
		}

		if (page <= 3) {
			// Descriptions
			paint2.setTextSize(18f);
			if (page == 1) {
				if (achievements[0][0] == fullUnlocked[0][0])
					g.drawString("Collect "
							+ thresholds[0][0][achievements[0][0] - 1]
							+ " yellow coins", 108, 160, paint2);
				else
					g.drawString("Next: Collect "
							+ thresholds[0][0][achievements[0][0]]
							+ " yellow coins", 108, 160, paint2);
				if (achievements[0][1] == fullUnlocked[0][1])
					g.drawString("Collect "
							+ thresholds[0][1][achievements[0][1] - 1]
							+ " cyan coins", 108, 271, paint2);
				else
					g.drawString("Next: Collect "
							+ thresholds[0][1][achievements[0][1]]
							+ " cyan coins", 108, 271, paint2);
				if (achievements[0][2] == fullUnlocked[0][2])
					g.drawString("Collect "
							+ thresholds[0][2][achievements[0][2] - 1]
							+ " blue coins", 108, 382, paint2);
				else
					g.drawString("Next: Collect "
							+ thresholds[0][2][achievements[0][2]]
							+ " blue coins", 108, 382, paint2);
				if (achievements[0][3] == fullUnlocked[0][3])
					g.drawString("Collect "
							+ thresholds[0][3][achievements[0][3] - 1]
							+ " green coins", 108, 493, paint2);
				else
					g.drawString("Next: Collect "
							+ thresholds[0][3][achievements[0][3]]
							+ " green coins", 108, 493, paint2);
				if (achievements[0][4] == fullUnlocked[0][4])
					g.drawString("Collect "
							+ thresholds[0][4][achievements[0][4] - 1]
							+ " pink coins", 108, 604, paint2);
				else
					g.drawString("Next: Collect "
							+ thresholds[0][4][achievements[0][4]]
							+ " pink coins", 108, 604, paint2);
			} else if (page == 2) {
				if (achievements[1][0] == fullUnlocked[1][0])
					g.drawString("Collect a lifetime of "
							+ thresholds[1][0][achievements[1][0] - 1]
							+ " coins", 108, 160, paint2);
				else
					g.drawString("Collect a lifetime of "
							+ thresholds[1][0][achievements[1][0]] + " coins",
							108, 160, paint2);
				if (achievements[1][1] == fullUnlocked[1][1])
					g.drawString("Get "
							+ thresholds[1][1][achievements[1][1] - 1]
							+ " total coins in an easy game", 108, 271, paint2);
				else
					g.drawString("Next: Get "
							+ thresholds[1][1][achievements[1][1]]
							+ " total coins in an easy game", 108, 271, paint2);
				if (achievements[1][2] == fullUnlocked[1][2])
					g.drawString("Get "
							+ thresholds[1][2][achievements[1][2] - 1]
							+ " total coins in a medium game", 108, 382, paint2);
				else
					g.drawString("Next: Get "
							+ thresholds[1][2][achievements[1][2]]
							+ " total coins in a medium game", 108, 382, paint2);
				if (achievements[1][3] == fullUnlocked[1][3])
					g.drawString("Get "
							+ thresholds[1][3][achievements[1][3] - 1]
							+ " total coins in a hard game", 108, 493, paint2);
				else
					g.drawString("Next: Get "
							+ thresholds[1][3][achievements[1][3]]
							+ " total coins in a hard game", 108, 493, paint2);
				if (achievements[1][4] == fullUnlocked[1][4])
					g.drawString("Get "
							+ thresholds[1][4][achievements[1][4] - 1]
							+ " total coins in an insane game", 108, 604,
							paint2);
				else
					g.drawString("Next: Get "
							+ thresholds[1][4][achievements[1][4]]
							+ " total coins in an insane game", 108, 604,
							paint2);
			} else {
				if (achievements[2][0] == 0)
					g.drawString("Next: Die once", 108, 160, paint2);
				else if (achievements[2][0] == fullUnlocked[2][0])
					g.drawString("Die a total of "
							+ thresholds[2][0][achievements[2][0] - 1]
							+ " times", 108, 160, paint2);
				else
					g.drawString("Next: Die a total of "
							+ thresholds[2][0][achievements[2][0]] + " times",
							108, 160, paint2);
				if (achievements[2][1] == 0)
					g.drawString("Next: Play this game for an hour", 108, 271,
							paint2);
				else if (achievements[2][1] == fullUnlocked[2][1])
					g.drawString("Play this game for "
							+ thresholds[2][1][achievements[2][1] - 1]
							+ " hours", 108, 271, paint2);
				else
					g.drawString("Next: Play this game for "
							+ thresholds[2][1][achievements[2][1]] + " hours",
							108, 271, paint2);
				if (achievements[2][2] == fullUnlocked[2][2])
					g.drawString("Collect a lifetime of "
							+ thresholds[2][2][achievements[2][2] - 1]
							+ " powerups", 108, 382, paint2);
				else
					g.drawString("Next: Collect a lifetime of "
							+ thresholds[2][2][achievements[2][2]]
							+ " powerups", 108, 382, paint2);
				if (achievements[2][3] == 0)
					g.drawString("Next: Buy an upgrade", 108, 493, paint2);
				else if (achievements[2][3] == fullUnlocked[2][3])
					g.drawString("Buy "
							+ thresholds[2][3][achievements[2][3] - 1]
							+ " upgrades", 108, 493, paint2);
				else
					g.drawString("Next: Buy "
							+ thresholds[2][3][achievements[2][3]]
							+ " upgrades", 108, 493, paint2);
				if (achievements[2][4] == 0)
					g.drawString("Next: Buy a skin", 108, 604, paint2);
				else if (achievements[2][4] == fullUnlocked[2][4])
					g.drawString("Buy "
							+ thresholds[2][4][achievements[2][4] - 1]
							+ " skins", 108, 604, paint2);
				else
					g.drawString("Next: Buy "
							+ thresholds[2][4][achievements[2][4]] + " skins",
							108, 604, paint2);
			}

			// Bars and progress
			paint2.setTextSize(20f);
			j = 0;
			for (int i = 170; i < 665; i += 111) {
				g.drawRect(110, i, 350, 20, Color.WHITE);
				if (achievements[page - 1][j] == fullUnlocked[page - 1][j])
					g.drawRect(110,i,350,20,purpColour);
				else
					g.drawRect(
							110,
							i,
							(int) Math
									.round((current[page - 1][j] * 1.0 / thresholds[page - 1][j][achievements[page - 1][j]]) * 350),
							20, purpColour);
				g.drawHollowRect(110, i, 350, 20, Color.BLACK, 3);
				j++;
			}

			j = 0;
			for (int i = 212; i < 665; i += 111) {
				if (achievements[page - 1][j] == fullUnlocked[page - 1][j])
					g.drawString("MAX", 108, i, paint2);
				else
					g.drawString(
							current[page - 1][j]
									+ "/"
									+ thresholds[page - 1][j][achievements[page - 1][j]],
							108, i, paint2);
				j++;
			}
		} else if (page == 4) {
			// Descriptions
			paint2.setTextSize(18f);
			if (achievements[3][0] == fullUnlocked[3][0])
				g.drawString("Have " + thresholds[3][0][achievements[3][0] - 1]
						+ " coins in your hand", 108, 160, paint2);
			else
				g.drawString("Next: Have "
						+ thresholds[3][0][achievements[3][0]]
						+ " coins in your hand", 108, 160, paint2);
			paint2.setTextSize(20f);
			g.drawString("Collect your first coin", 108, 275, paint2);
			g.drawString("Cover your character with your finger", 108, 386,
					paint2);
			g.drawString("Play a game without touching the", 108, 497, paint2);
			g.drawString("screen", 108, 526, paint2);
			g.drawString("Do not collect a coin for 20 seconds", 108, 608,
					paint2);

			// Draw bars and progress
			g.drawRect(110, 170, 350, 20, Color.WHITE);
			if (achievements[3][0] == fullUnlocked[3][0]) {
				g.drawString("MAX", 108, 212, paint2);
				g.drawRect(110, 170, 350, 20, purpColour);
			} else {
				g.drawString(current[3][0] + "/"
						+ thresholds[3][0][achievements[3][0]], 108, 212,
						paint2);
				g.drawRect(
						110,
						170,
						(int) Math
								.round((current[3][0] * 1.0 / thresholds[3][0][achievements[3][0]]) * 350),
						20, purpColour);
			}

			g.drawHollowRect(110, 170, 350, 20, Color.BLACK, 3);

		} else {
			paint2.setTextSize(20f);
			if (page == 5) {
				g.drawString("Collect a powerup of each type", 108, 164, paint2);
				g.drawString("Collect 10 powerups in a single game", 108, 275,
						paint2);
				g.drawString("Have a coin spawn at the same ", 108, 386, paint2);
				g.drawString("position as the previous coin", 108, 415, paint2);
				g.drawString("Grab 10 coins in 4 seconds on Insane", 108, 497,
						paint2);
				g.drawString("difficulty", 108, 526, paint2);
				g.drawString("Collect 2 pink coins in a row", 108, 608, paint2);
			} else if (page == 6) {
				g.drawString("Change your skin", 108, 164, paint2);
				g.drawString("Buy the sad skin", 108, 275, paint2);
				g.drawString("Buy all Starcraft skins", 108, 386, paint2);
				g.drawString("Buy the moneybag skin", 108, 497, paint2);
				g.drawString("screen", 108, 526, paint2);
				g.drawString("Buy the diamond skin", 108, 608, paint2);
			} else if (page == 7) {
				g.drawString("Collect 20 coins when the silence", 108, 164,
						paint2);
				g.drawString("powerup is active", 108, 193, paint2);
				g.drawString("Blow up 10 balls at once with a bomb", 108, 275,
						paint2);
				g.drawString("Go through a cannonball with shield", 108, 386,
						paint2);
				g.drawString("activated", 108, 415, paint2);
				g.drawString("Collect 10 coins while a bad powerup is", 108,
						497, paint2);
				g.drawString("active", 108, 526, paint2);
				g.drawString("Pickup 2 bad powerups in a row", 108, 608, paint2);
			} else if (page == 8) {
				g.drawString("Play a one second game", 108, 164, paint2);
				g.drawString("Play a game with every skin at least", 108, 275,
						paint2);
				g.drawString("once", 108, 304, paint2);
				g.drawString("Play a game with the black skin", 108, 386,
						paint2);
				g.drawString("Get sniped by the cannon", 108, 497, paint2);
				g.drawString("", 108, 526, paint2);
				g.drawString("Max out an upgrade", 108, 608, paint2);
			} else {
				g.drawString("Collect every coin and powerup while", 108, 164,
						paint2);
				g.drawString("using the pokeball", 108, 193, paint2);
				g.drawString("Change the sensitivity", 108, 275, paint2);
				g.drawString("Mute the sound", 108, 386, paint2);
				paint2.setTextSize(18f);
				g.drawString("In an easy, medium, hard, and insane game,", 108,
						493, paint2);
				g.drawString("have at least 50,40,30,20 cannonballs", 108, 518,
						paint2);
				g.drawString("present respectively", 108, 543, paint2);
				paint2.setTextSize(20f);
				g.drawString("Open an advertisement", 108, 608, paint2);
			}
		}

		// Achievements
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
					achievePopup.get(0),
					((SampleGame) game).getAchievements(achievePopup.get(0)));
			Rect bounds = new Rect();
			paint2.getTextBounds(label, 0, label.length(), bounds);
			g.drawString(label, 240, bannerY + 35
					+ (bounds.bottom - bounds.top) / 2, paint2);
			if (achieveTime != 0
					&& System.currentTimeMillis() - achieveTime > 4000) {
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