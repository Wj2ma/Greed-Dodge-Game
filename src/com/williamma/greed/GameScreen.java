package com.williamma.greed;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;

import com.williamma.framework.Game;
import com.williamma.framework.Graphics;
import com.williamma.framework.Image;
import com.williamma.framework.Input.TouchEvent;
import com.williamma.framework.Screen;

public class GameScreen extends Screen {

	enum Gamestate {
		Paused, Resumed
	}

	Gamestate state = Gamestate.Resumed;

	private Typeface titleFont, boldFont;
	private Paint paint2;

	public static final int EASY = 2, MEDIUM = 4, HARD = 6, INSANE = 8;
	private int difficulty;
	private static You you;
	private double sensitivity;

	private int speed, score = 0, curX = 240, curY = 400, powerups;
	private Gun gun;
	private ArrayList<Ball> balls = new ArrayList<Ball>();
	private Coin coin;
	private Animation[] coinAnim;
	private Animation currAnim;
	private int dRotation = 0; // rotating diamond
	private Image youImage;
	private Image[] explosion;
	private boolean mute;

	private int coinType; // Type of coin
	private double[] chances = new double[4]; // odds of getting this coin
	private int decreasedChance; // odds of getting a powerdown
	private int chance; // how many coins before powerup comes
	private Powerup powerup; // the powerup
	private int superCoinValue; // how much the super coin is worth
	private boolean silenced = false; // checks if gun is silenced
	private int[] bombX = new int[2], bombY = new int[2]; // bomb coordinates
	private int radius; // how big explosion is
	private int[] durations; // how long each powerup will last
	private int coinCount = 0; // how many coins gained
	private int[] coinCounts = new int[5];
	private int bonus; // how much bonus coins earned evry 10 coins
	private int bonusMultiplier; // how much increments
	private long bonusLabel = 0; // show the bonus label
	private long gameTime;

	// Variables for achievements
	private int[] achievements = new int[45];
	private int[][] coinCountT = new int[5][5];
	private int[] powerupT = new int[3], scoreT = new int[3];
	private long pickupTimer = System.currentTimeMillis(), ballLife;
	private boolean[] collectedPowerups = new boolean[10],
			gots = new boolean[15];
	private int silenceCoins = 0, activePowerdowns = 0, powerdownCoins = 0;
	private boolean pinkCoin = false, powerDown = false, fingerTouch = false,
			catchem = false;

	private ArrayList<Integer> achievePopup = new ArrayList<Integer>();
	private int bannerY = 800;
	private long achieveTime;
	boolean starter = false;

	private int[] bombed = new int[2];
	private ArrayList<Long> speedrun = new ArrayList<Long>();
	private ArrayList<Integer> collection = new ArrayList<Integer>();

	private SharedPreferences saves;
	private SharedPreferences.Editor editor;
	private long timer[];
	private long powerupDuration, pauseTime, currTime;
	private boolean paused = false;

	public GameScreen(Game game, Typeface font, int d) {
		super(game);
		titleFont = font;
		boldFont = Typeface.create((String) null, Typeface.BOLD);
		paint2 = new Paint();
		paint2.setTypeface(boldFont);
		saves = PreferenceManager
				.getDefaultSharedPreferences((SampleGame) game);
		editor = saves.edit();

		sensitivity = ((SampleGame) game).getSensitivity();

		timer = new long[10];
		durations = new int[8];
		for (int i = 0; i < 8; i++)
			durations[i] = ((SampleGame) game).getPowerupDurations(i);

		coinAnim = new Animation[5];
		coinAnim[0] = new Animation();
		coinAnim[0].addFrame(Assets.coin1, 100);
		coinAnim[0].addFrame(Assets.coin2, 100);
		coinAnim[0].addFrame(Assets.coin3, 100);
		coinAnim[0].addFrame(Assets.coin2, 100);
		coinAnim[1] = new Animation();
		coinAnim[1].addFrame(Assets.coin4, 100);
		coinAnim[1].addFrame(Assets.coin5, 100);
		coinAnim[1].addFrame(Assets.coin3, 100);
		coinAnim[1].addFrame(Assets.coin5, 100);
		coinAnim[2] = new Animation();
		coinAnim[2].addFrame(Assets.coin6, 100);
		coinAnim[2].addFrame(Assets.coin7, 100);
		coinAnim[2].addFrame(Assets.coin3, 100);
		coinAnim[2].addFrame(Assets.coin7, 100);
		coinAnim[3] = new Animation();
		coinAnim[3].addFrame(Assets.coin8, 100);
		coinAnim[3].addFrame(Assets.coin9, 100);
		coinAnim[3].addFrame(Assets.coin3, 100);
		coinAnim[3].addFrame(Assets.coin9, 100);
		coinAnim[4] = new Animation();
		coinAnim[4].addFrame(Assets.coin10, 100);
		coinAnim[4].addFrame(Assets.coin11, 100);
		coinAnim[4].addFrame(Assets.coin3, 100);
		coinAnim[4].addFrame(Assets.coin11, 100);

		randomize();
		currAnim = coinAnim[coinType];

		you = new You();
		you.setPickupRadius(((SampleGame) game).getPickupRadius());
		youImage = ((SampleGame) game).getSkin();
		if (youImage == ((SampleGame) game).getSkins(25))
			you.setDiamond(true);

		gun = new Gun(50, 0);
		coin = new Coin(10 + (int) (Math.random() * 460),
				30 + (int) (Math.random() * 560), you);
		powerup = new Powerup(24 + (int) (Math.random() * 436),
				44 + (int) (Math.random() * 532), you);

		for (int i = 0; i < 5; i++)
			coinCounts[i] = ((SampleGame) game).getCoinCount(i);
		powerups = ((SampleGame) game).getTotalPowerups();

		bonusMultiplier = ((SampleGame) game).getBonusMultiplier();
		bonus = bonusMultiplier;
		radius = ((SampleGame) game).getBombRadius();
		chance = ((SampleGame) game).getPowerupChance();
		for (int i = 0; i < 4; i++)
			chances[i] = ((SampleGame) game).getCoinChances(i);
		decreasedChance = ((SampleGame) game).getDecreasedChance();
		superCoinValue = ((SampleGame) game).getSuperCoinValue();

		explosion = new Image[4];
		double inc = .5;
		for (int i = 0; i < 4; i++) {
			explosion[i] = Assets.explode.resize((int) (radius * inc),
					(int) (radius * inc));
			inc += .5;
		}

		speed = d;
		if (d == EASY)
			difficulty = 0;
		else if (d == MEDIUM)
			difficulty = 1;
		else if (d == HARD)
			difficulty = 2;
		else
			difficulty = 3;

		for (int i = 0; i < 45; i++)
			achievements[i] = ((SampleGame) game).getAchievements(i);
		mute = ((SampleGame) game).isMuted();

		coinCountT[0][0] = 1000;
		coinCountT[0][1] = 3000;
		coinCountT[0][2] = 5000;
		coinCountT[0][3] = 7500;
		coinCountT[0][4] = 10000;
		coinCountT[1][0] = 500;
		coinCountT[1][1] = 1000;
		coinCountT[1][2] = 2000;
		coinCountT[1][3] = 3500;
		coinCountT[1][4] = 5000;
		coinCountT[2][0] = 250;
		coinCountT[2][1] = 500;
		coinCountT[2][2] = 750;
		coinCountT[2][3] = 1000;
		coinCountT[2][4] = 1500;
		coinCountT[3][0] = 100;
		coinCountT[3][1] = 200;
		coinCountT[3][2] = 300;
		coinCountT[3][3] = 500;
		coinCountT[3][4] = 1000;
		coinCountT[4][0] = 50;
		coinCountT[4][1] = 100;
		coinCountT[4][2] = 200;
		coinCountT[4][3] = 300;
		coinCountT[4][4] = 500;
		powerupT[0] = 100;
		powerupT[1] = 800;
		powerupT[2] = 1500;
		if (difficulty == 0) {
			scoreT[0] = 300;
			scoreT[1] = 500;
			scoreT[2] = 700;
		} else if (difficulty == 1) {
			scoreT[0] = 200;
			scoreT[1] = 400;
			scoreT[2] = 600;
		} else if (difficulty == 2) {
			scoreT[0] = 100;
			scoreT[1] = 300;
			scoreT[2] = 500;
		} else {
			scoreT[0] = 100;
			scoreT[1] = 250;
			scoreT[2] = 400;
		}

		if (achievements[36] == 0) {
			editor.putBoolean("Used Skin" + ((SampleGame) game).getSkinNum(),
					true);
			editor.commit();
			boolean achieve = true;
			for (int i = 0; i < 26; i++) {
				if (!saves.getBoolean("Used Skin" + i, false)) {
					achieve = false;
					break;
				}
			}

			if (achievements[37] == 0 && ((SampleGame) game).getSkinNum() == 14) {
				achievements[37] = 1;
				((SampleGame) game).setAchievements(1, 37);
				editor.putInt("Achievements37", 1);
				editor.commit();
				achievePopup.add(37);
			}

			if (achieve) {
				achievements[36] = 1;
				((SampleGame) game).setAchievements(1, 36);
				editor.putInt("Achievements36", 1);
				editor.commit();
				achievePopup.add(36);
			}
		}

		// If pokeball skin
		if (achievements[40] == 0 && ((SampleGame) game).getSkinNum() == 24) {
			catchem = true;
			for (int i = 0; i < 15; i++)
				gots[i] = saves.getBoolean("Caught" + i, false);
		}

		for (int i = 0; i < 10; i++)
			collectedPowerups[i] = saves.getBoolean("Collected Powerups" + i,
					false);

		gameTime = System.currentTimeMillis();
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		if (state == Gamestate.Resumed)
			updateRunning(touchEvents);
		else
			updatePause(touchEvents);

	}

	private void updateRunning(List<TouchEvent> touchEvents) {

		for (TouchEvent i : touchEvents) {
			if (i.type == TouchEvent.TOUCH_DOWN) {
				curX = i.x;
				curY = i.y;
				if (achievements[17] == 0)
					if (Math.abs(i.x - you.getX()) < 32
							&& Math.abs(i.y - you.getY()) < 32) {
						achievements[17] = 1;
						((SampleGame) game).setAchievements(1, 17);
						editor.putInt("Achievements17", 1);
						editor.commit();
						achievePopup.add(17);
					}
				fingerTouch = true;
			}
			if (i.type == TouchEvent.TOUCH_DRAGGED) {
				you.update((int) Math.round((i.x - curX) * sensitivity),
						(int) Math.round((i.y - curY) * sensitivity));
				curX = i.x;
				curY = i.y;
			}

		}

		// Checks for all powerup timeouts
		long currTime = System.currentTimeMillis();
		if ((timer[1] != 0 && currTime - timer[1] > durations[0])
				|| (timer[7] != 0 && currTime - timer[7] > durations[6])) {
			you.setSize(30);
			youImage = ((SampleGame) game).getSkin();
			if (timer[7] != 0)
				activePowerdowns--;
			if (activePowerdowns == 0)
				powerdownCoins = 0;
		}
		if ((timer[2] != 0 && currTime - timer[2] > durations[1])
				|| (timer[8] != 0 && currTime - timer[8] > durations[7])) {
			for (Ball b : balls)
				b.setSize(20);
			if (timer[8] != 0)
				activePowerdowns--;
			if (activePowerdowns == 0)
				powerdownCoins = 0;
		}
		if (timer[3] != 0 && currTime - timer[3] > durations[2]) {
			silenced = false;
			silenceCoins = 0;
		}
		if (timer[4] != 0 && currTime - timer[4] > durations[3])
			you.setInvincible(false);
		if ((timer[5] != 0 && currTime - timer[5] > durations[4])
				|| (timer[6] != 0 && currTime - timer[6] > durations[5])) {
			for (Ball b : balls)
				b.setSpeed(b.getDefaultSpeed());
			if (timer[6] != 0)
				activePowerdowns--;
			if (activePowerdowns == 0)
				powerdownCoins = 0;
		}
		if (currTime - powerupDuration > 7000)
			powerup.setSpawned(false);

		// Achievement for 10 balls in 3.5 seconds
		if (achievements[23] == 0) {
			while (true) {
				if (speedrun.size() > 0) {
					if (currTime - speedrun.get(0) > 4000) {
						speedrun.remove(0);
						collection.remove(0);
					} else
						break;
				} else
					break;
			}
		}

		if (achievements[32] == 0 && you.isShielded()) {
			achievements[32] = 1;
			((SampleGame) game).setAchievements(1, 32);
			editor.putInt("Achievements32", 1);
			editor.commit();
			achievePopup.add(32);
		}

		if (achievements[30] == 0 && silenceCoins >= 20) {
			achievements[30] = 1;
			((SampleGame) game).setAchievements(1, 30);
			editor.putInt("Achievements30", 1);
			editor.commit();
			achievePopup.add(30);
		}

		for (int i = 0; i < balls.size(); i++) {
			balls.get(i).update();
			if (balls.get(i).isHit()) {
				if (!mute)
					Assets.death.play(1f);

				boolean sniped = false;
				if (achievements[38] == 0 && i == balls.size() - 1
						&& currTime - ballLife < 500) {
					achievements[38] = 1;
					((SampleGame) game).setAchievements(1, 38);
					editor.putInt("Achievements38", 1);
					sniped = true;
				}

				boolean highscore1 = false;
				if (score > ((SampleGame) game).getHighscores(difficulty)) {
					((SampleGame) game).setHighscores(score, difficulty);
					editor.putInt("score" + difficulty, score);
					highscore1 = true;
				}
				if (coinCount > ((SampleGame) game).getMostCoins(difficulty)) {
					((SampleGame) game).setMostCoins(coinCount, difficulty);
					editor.putInt("Most Coins" + difficulty, coinCount);
					highscore1 = true;
				}
				int deaths = ((SampleGame) game).getDeaths();
				int newDeaths = deaths + 1;
				int d = ((SampleGame) game).getDeaths(difficulty);
				int d2 = d + 1;
				((SampleGame) game).setDeaths(d2, difficulty);
				editor.putInt("Deaths" + difficulty, d2);
				((SampleGame) game)
						.setAverageScore(
								(((SampleGame) game)
										.getAverageScore(difficulty) * d + score)
										/ d2, difficulty);
				editor.putFloat("Average Score" + difficulty,
						(float) ((SampleGame) game).getAverageScore(difficulty));
				((SampleGame) game)
						.setAverageCoins(
								(((SampleGame) game)
										.getAverageCoins(difficulty) * d + coinCount)
										/ d2, difficulty);
				editor.putFloat("Average Coins" + difficulty,
						(float) ((SampleGame) game).getAverageCoins(difficulty));
				int coins = score * (difficulty + 1);
				((SampleGame) game).setTotalCoins(((SampleGame) game)
						.getTotalCoins() + coins);
				((SampleGame) game).setLifetimeCoins(((SampleGame) game)
						.getLifetimeCoins() + coins);
				editor.putInt("Total Coins",
						((SampleGame) game).getTotalCoins());
				editor.putInt("Lifetime Coins",
						((SampleGame) game).getLifetimeCoins());
				for (int j = 0; j < 5; j++) {
					((SampleGame) game).setCoinCount(coinCounts[j], j);
					editor.putInt("Coin Count" + j,
							((SampleGame) game).getCoinCount(j));
				}
				((SampleGame) game).setTotalPowerups(powerups);
				editor.putInt("Total Powerups",
						((SampleGame) game).getTotalPowerups());
				((SampleGame) game).setDeaths(newDeaths);
				((SampleGame) game)
						.setPlayTime(((SampleGame) game).getPlayTime()
								+ (int) Math
										.round((((currTime - gameTime) * 1.0) / 1000)));
				editor.putInt("Deaths", newDeaths);
				editor.putInt("Playtime", ((SampleGame) game).getPlayTime());
				if (balls.size() > ((SampleGame) game).getMostBalls(difficulty)) {
					((SampleGame) game).setMostBalls(balls.size(), difficulty);
					editor.putInt("Most Balls" + difficulty, balls.size());
					highscore1 = true;
				}

				boolean quick = false;
				if (achievements[35] == 0 && currTime - gameTime <= 1000) {
					achievements[35] = 1;
					((SampleGame) game).setAchievements(1, 35);
					editor.putInt("Achievements35", 1);
					quick = true;
				}
				boolean what = false;
				if (achievements[18] == 0 && !fingerTouch) {
					achievements[18] = 1;
					((SampleGame) game).setAchievements(1, 18);
					editor.putInt("Achievements18", 1);
					what = true;
				}

				editor.commit();
				nullify();
				game.setScreen(new DeadScreen(game, titleFont, score, speed,
						highscore1, what, quick, sniped));
				return;
			}
		}

		gun.update();
		if (you.isDiamond()) {
			dRotation++;
			if (dRotation == 361)
				dRotation = 1;
		}

		animate();
		// Powerups
		if (powerup.isSpawned()) {
			powerup.update();
			if (powerup.isHit()) {
				powerups++;
				if (achievements[12] < 3) {
					if (powerups > powerupT[achievements[12]]) {
						achievements[12]++;
						((SampleGame) game).setAchievements(achievements[12],
								12);
						editor.putInt("Achievements12", achievements[12]);
						editor.commit();
						achievePopup.add(12);
					}
				}

				if (achievements[21] == 0) {
					if (powerups - ((SampleGame) game).getTotalPowerups() >= 10) {
						achievements[21] = 1;
						((SampleGame) game).setAchievements(1, 21);
						editor.putInt("Achievements21", 1);
						editor.commit();
						achievePopup.add(21);
					}
				}

				switch (powerup.getType()) {
				case 0:
					if (!mute)
						Assets.explosion.play(1f);
					if (timer[0] == 0) {
						timer[0] = currTime;
						bombed[0] = 0;
						bombX[0] = powerup.getX();
						bombY[0] = powerup.getY();
						explode(0);
					} else {
						timer[9] = currTime;
						bombed[1] = 0;
						bombX[1] = powerup.getX();
						bombY[1] = powerup.getY();
						explode(1);
					}
					if (!collectedPowerups[0]) {
						collectedPowerups[0] = true;
						editor.putBoolean("Collected Powerups0", true);
						editor.commit();
					}
					powerDown = false;

					if (catchem && !gots[0])
						setAchieve40(0);
					break;
				case 1:
					if (!mute)
						Assets.powerup.play(1f);
					you.setSize(15);
					if (!you.isDiamond())
						youImage = ((SampleGame) game).getSkin().resize(15, 15);
					else
						youImage = ((SampleGame) game).getSkin().resize(21, 21);
					timer[1] = currTime;
					timer[7] = 0;
					if (!collectedPowerups[1]) {
						collectedPowerups[1] = true;
						editor.putBoolean("Collected Powerups1", true);
						editor.commit();
					}
					powerDown = false;
					activePowerdowns = 0;

					if (catchem && !gots[1])
						setAchieve40(1);
					break;
				case 2:
					if (!mute)
						Assets.powerup.play(1f);
					for (Ball b : balls)
						b.setSize(10);
					timer[2] = currTime;
					timer[8] = 0;
					if (!collectedPowerups[2]) {
						collectedPowerups[2] = true;
						editor.putBoolean("Collected Powerups2", true);
						editor.commit();
					}
					powerDown = false;
					activePowerdowns = 0;

					if (catchem && !gots[2])
						setAchieve40(2);
					break;
				case 3:
					if (!mute)
						Assets.powerup.play(1f);
					silenced = true;
					powerup.setSilenced(true);
					timer[3] = currTime;
					if (!collectedPowerups[3]) {
						collectedPowerups[3] = true;
						editor.putBoolean("Collected Powerups3", true);
						editor.commit();
					}
					powerDown = false;

					if (catchem && !gots[3])
						setAchieve40(3);
					break;
				case 4:
					if (!mute)
						Assets.powerup.play(1f);
					you.setInvincible(true);
					timer[4] = currTime;
					if (!collectedPowerups[4]) {
						collectedPowerups[4] = true;
						editor.putBoolean("Collected Powerups4", true);
						editor.commit();
					}
					powerDown = false;
					activePowerdowns = 0;

					if (catchem && !gots[4])
						setAchieve40(4);
					break;
				case 5:
					if (!mute)
						Assets.powerup.play(1f);
					for (Ball b : balls)
						b.setSpeed(b.getDefaultSpeed() - 2);
					timer[5] = currTime;
					timer[6] = 0;
					if (!collectedPowerups[5]) {
						collectedPowerups[5] = true;
						editor.putBoolean("Collected Powerups5", true);
						editor.commit();
					}
					powerDown = false;
					activePowerdowns = 0;

					if (catchem && !gots[5])
						setAchieve40(5);
					break;
				case 6:
					if (!mute)
						Assets.powerdown.play(1f);
					for (Ball b : balls)
						b.setSpeed(b.getDefaultSpeed() + 2);
					timer[6] = currTime;
					timer[5] = 0;
					if (!collectedPowerups[6]) {
						collectedPowerups[6] = true;
						editor.putBoolean("Collected Powerups6", true);
						editor.commit();
					}
					activePowerdowns++;
					if (achievements[34] == 0 && powerDown) {
						achievements[34] = 1;
						((SampleGame) game).setAchievements(1, 34);
						editor.putInt("Achievements34", 1);
						editor.commit();
						achievePopup.add(34);
					}
					powerDown = true;

					if (catchem && !gots[6])
						setAchieve40(6);
					break;
				case 7:
					if (!mute)
						Assets.powerdown.play(1f);
					you.setSize(50);
					if (!you.isDiamond())
						youImage = ((SampleGame) game).getSkin().resize(50, 50);
					else
						youImage = ((SampleGame) game).getSkin().resize(70, 70);
					timer[7] = currTime;
					timer[1] = 0;
					if (!collectedPowerups[7]) {
						collectedPowerups[7] = true;
						editor.putBoolean("Collected Powerups7", true);
						editor.commit();
					}
					activePowerdowns++;
					if (achievements[34] == 0 && powerDown) {
						achievements[34] = 1;
						((SampleGame) game).setAchievements(1, 34);
						editor.putInt("Achievements34", 1);
						editor.commit();
						achievePopup.add(34);
					}
					powerDown = true;

					if (catchem && !gots[7])
						setAchieve40(7);
					break;
				case 8:
					if (!mute)
						Assets.powerdown.play(1f);
					for (Ball b : balls)
						b.setSize(30);
					timer[8] = currTime;
					timer[2] = 0;
					if (!collectedPowerups[8]) {
						collectedPowerups[8] = true;
						editor.putBoolean("Collected Powerups8", true);
						editor.commit();
					}
					activePowerdowns++;
					if (achievements[34] == 0 && powerDown) {
						achievements[34] = 1;
						((SampleGame) game).setAchievements(1, 34);
						editor.putInt("Achievements34", 1);
						editor.commit();
						achievePopup.add(34);
					}
					powerDown = true;

					if (catchem && !gots[8])
						setAchieve40(8);
					break;
				case 9:
					if (!mute)
						Assets.money.play(1f);
					score += superCoinValue;
					if (!collectedPowerups[9]) {
						collectedPowerups[9] = true;
						editor.putBoolean("Collected Powerups9", true);
						editor.commit();
					}
					powerDown = false;

					if (catchem && !gots[9])
						setAchieve40(9);
					break;
				}
				if (achievements[20] == 0 && collectedPowerups[0]
						&& collectedPowerups[1] && collectedPowerups[2]
						&& collectedPowerups[3] && collectedPowerups[4]
						&& collectedPowerups[5] && collectedPowerups[6]
						&& collectedPowerups[7] && collectedPowerups[8]
						&& collectedPowerups[9]) {
					achievements[20] = 1;
					((SampleGame) game).setAchievements(1, 20);
					editor.putInt("Achievements20", 1);
					editor.commit();
					achievePopup.add(20);
				}
				powerup.setHit(false);
			}
		}

		coin.update();
		if (coin.isHit()) {
			if (achievements[23] == 0 && difficulty == 3) {
				for (int i = 0; i < collection.size(); i++) {
					collection.set(i, collection.get(i) + 1);
					if (collection.get(i) >= 10) {
						achievements[23] = 1;
						((SampleGame) game).setAchievements(1, 23);
						editor.putInt("Achievements23", 1);
						editor.commit();
						achievePopup.add(23);
					}
				}
				speedrun.add(System.currentTimeMillis());
				collection.add(1);
			}

			if (achievements[16] == 0) {
				achievements[16] = 1;
				((SampleGame) game).setAchievements(1, 16);
				editor.putInt("Achievements16", 1);
				editor.commit();
				achievePopup.add(16);
			}

			if (achievements[22] == 0) {
				if (Math.abs(coin.getPrevX() - coin.getX()) < 15
						&& Math.abs(coin.getPrevY() - coin.getY()) < 15) {
					achievements[22] = 1;
					((SampleGame) game).setAchievements(1, 22);
					editor.putInt("Achievements22", 1);
					editor.commit();
					achievePopup.add(22);
				}
			}

			if (achievements[19] == 0)
				pickupTimer = currTime;

			if (silenced)
				silenceCoins++;

			if (achievements[33] == 0 && activePowerdowns > 0) {
				powerdownCoins++;
				if (powerdownCoins >= 10) {
					achievements[33] = 1;
					((SampleGame) game).setAchievements(1, 33);
					editor.putInt("Achievements33", 1);
					editor.commit();
					achievePopup.add(33);
				}
			}

			if (!mute)
				Assets.coin.play(0.85f);

			if (catchem && !gots[coinType + 10])
				setAchieve40(coinType + 10);

			switch (coinType) {
			case 4:
				score += 10;
				if (achievements[24] == 0 && pinkCoin) {
					achievements[24] = 1;
					((SampleGame) game).setAchievements(1, 24);
					editor.putInt("Achievements24", 1);
					editor.commit();
					achievePopup.add(24);
				}
				pinkCoin = true;
				break;
			case 3:
				score += 2;
			case 2:
				score++;
			case 1:
				score++;
			case 0:
				score++;
				pinkCoin = false;
			}
			coinCount++;
			coinCounts[coinType]++;

			if (achievements[coinType] < 5) {
				if (coinCounts[coinType] > coinCountT[coinType][achievements[coinType]]) {
					achievements[coinType]++;
					((SampleGame) game).setAchievements(achievements[coinType],
							coinType);
					editor.putInt("Achievements" + coinType,
							achievements[coinType]);
					editor.commit();
					achievePopup.add(coinType);
				}
			}

			coin.setHit(false);
			randomize();
			currAnim = coinAnim[coinType];
			createBall();
			if (coinCount % chance == 0) {
				createPowerup();
			}
			if (coinCount % 10 == 0) {
				score += bonus * (difficulty + 1);
				bonus += bonusMultiplier;
				bonusLabel = System.currentTimeMillis();
			}
		} else if (achievements[19] == 0 && currTime - pickupTimer > 20000) {
			achievements[19] = 1;
			((SampleGame) game).setAchievements(1, 19);
			editor.putInt("Achievements19", 1);
			editor.commit();
			achievePopup.add(19);
		}

		if (achievements[44] == 0 && ((SampleGame) game).isOpened()) {
			achievements[44] = 1;
			((SampleGame) game).setAchievements(1, 44);
			editor.putInt("Achievements44", 1);
			editor.commit();
			achievePopup.add(44);
		}
		
		int part = difficulty + 6;
		if (achievements[part] < 3 && score >= scoreT[achievements[part]]) {
			achievements[difficulty+6]++;
			((SampleGame) game).setAchievements(achievements[part], part);
			editor.putInt("Achievements" + part, achievements[part]);
			editor.commit();
			achievePopup.add(part);
		}
	}

	private void setAchieve40(int i) {
		gots[i] = true;
		editor.putBoolean("Caught" + i, true);
		boolean check = true;
		for (int j = 0; j < 15; j++) {
			if (!gots[j]) {
				check = false;
				break;
			}
		}
		if (check) {
			achievements[40] = 1;
			((SampleGame) game).setAchievements(1, 40);
			editor.putInt("Achievements40", 1);
			achievePopup.add(40);
		}
		editor.commit();
	}

	private void explode(int j) {
		for (int i = 0; i < balls.size(); i++)
			try {
				if (balls.get(i).checkCollision(bombX[j], bombY[j], radius)) {
					try {
						balls.remove(i);
					} catch (IndexOutOfBoundsException e) {
					}
					bombed[j]++;
				}
			} catch (IndexOutOfBoundsException e) {
			}
	}

	private void randomize() {
		double randomizer = Math.random();
		if (randomizer < chances[3])
			coinType = 4;
		else if (randomizer < chances[2] + chances[3])
			coinType = 3;
		else if (randomizer < chances[1] + chances[2] + chances[3])
			coinType = 2;
		else if (randomizer < chances[0] + chances[1] + chances[2] + chances[3])
			coinType = 1;
		else
			coinType = 0;
	}

	private void updatePause(List<TouchEvent> touchEvents) {
		for (TouchEvent i : touchEvents)
			if (i.type == TouchEvent.TOUCH_UP)
				resume();
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawARGB(255, 255, 255, 255);
		g.drawRect(0, 600, 480, 200, Color.GRAY);
		paint2.setTextAlign(Align.LEFT);
		// Coin
		g.drawImage(currAnim.getImage(), coin.getX() - 10, coin.getY() - 10);
		// Powerup
		if (powerup.isSpawned())
			g.drawImage(powerup.getCurrImage(), powerup.getX() - 24,
					powerup.getY() - 24);

		// Balls
		for (Ball b : balls)
			g.drawCircle(b.getX(), b.getY(), b.getSize() / 2, Color.BLACK);
		// Gun
		RectF r = new RectF(gun.getX(), gun.getY(), gun.getX() + 50,
				gun.getY() + 50);
		g.drawArc(r, 180, 180, true, Color.argb(255, 139, 69, 19));
		g.drawHollowArc(r, 180, 180, true, Color.BLACK, 1);
		g.save();
		g.rotate(gun.getTheta(), gun.getX() + 25, gun.getY() + 10);
		g.drawRect(gun.getX() + 20, gun.getY() + 10, 10, 30, Color.GRAY);
		if (!silenced)
			g.drawRect(gun.getX() + 20, gun.getY() + 37, 10, 3, Color.BLACK);
		g.drawHollowRect(gun.getX() + 20, gun.getY() + 10, 10, 30, Color.BLACK,
				1);
		g.restore();
		// you
		if (!you.isDiamond())
			g.drawImage(youImage, you.getX() - you.getSize() / 2, you.getY()
					- you.getSize() / 2);
		else {
			g.save();
			g.rotate(dRotation, you.getX(), you.getY());
			g.drawImage(youImage, you.getX() - you.getDiamondSize() / 2,
					you.getY() - you.getDiamondSize() / 2);
			g.restore();
		}

		if (state == Gamestate.Resumed)
			currTime = System.currentTimeMillis();
		if (you.isInvincible()) {
			if (currTime - timer[4] < durations[3] / 4)
				g.drawCircle(you.getX(), you.getY(), you.getSize() / 2 + 7,
						Color.argb(100, 99, 255, 248));
			else if (currTime - timer[4] < 2 * durations[3] / 4)
				g.drawCircle(you.getX(), you.getY(), you.getSize() / 2 + 7,
						Color.argb(67, 99, 255, 248));
			else if (currTime - timer[4] < 3 * durations[3] / 4)
				g.drawCircle(you.getX(), you.getY(), you.getSize() / 2 + 7,
						Color.argb(33, 99, 255, 248));
			else
				g.drawCircle(you.getX(), you.getY(), you.getSize() / 2 + 7,
						Color.argb(15, 99, 255, 248));
		}

		for (int i = 0; i < 10; i += 9) {
			long t = currTime - timer[i];
			if (t < 1000) {
				if (t < 50)
					g.drawImage(explosion[0], bombX[i / 9] - radius / 4,
							bombY[i / 9] - radius / 4);
				else if (t < 100)
					g.drawImage(explosion[1], bombX[i / 9] - radius / 2,
							bombY[i / 9] - radius / 2);
				else if (t < 150)
					g.drawImage(explosion[2], bombX[i / 9] - (3 * radius) / 4,
							bombY[i / 9] - (3 * radius) / 4);
				else
					g.drawImage(explosion[3], bombX[i / 9] - radius,
							bombY[i / 9] - radius);
				if (t > 600 && t < 700)
					g.drawCircle(bombX[i / 9], bombY[i / 9], radius,
							Color.argb(51, 255, 255, 255));
				else if (t > 700 && t < 800)
					g.drawCircle(bombX[i / 9], bombY[i / 9], radius,
							Color.argb(102, 255, 255, 255));
				else if (t > 800 && t < 900)
					g.drawCircle(bombX[i / 9], bombY[i / 9], radius,
							Color.argb(153, 255, 255, 255));
				else if (t > 900)
					g.drawCircle(bombX[i / 9], bombY[i / 9], radius,
							Color.argb(204, 255, 255, 255));
				explode(i / 9);
			} else if (achievements[31] == 0 && bombed[i / 9] >= 10) {
				achievements[31] = 1;
				((SampleGame) game).setAchievements(1, 31);
				editor.putInt("Achievements31", 1);
				editor.commit();
				achievePopup.add(31);
				timer[i] = 0;
				bombed[i / 9] = 0;
			} else {
				timer[i] = 0;
				bombed[i / 9] = 0;
			}
		}

		if (bonusMultiplier != 0 && bonus != bonusMultiplier
				&& currTime - bonusLabel < 3000) {
			paint2.setColor(Color.WHITE);
			paint2.setTextSize(40f);
			g.drawString("Bonus "
					+ ((bonus - bonusMultiplier) * (difficulty + 1))
					+ " coins!!", 95, 700, paint2);
		}

		// Score
		paint2.setColor(Color.BLACK);
		paint2.setTextSize(30f);
		g.drawString(Integer.toString(score), 5, 23, paint2);

		if (state == Gamestate.Paused) {
			g.drawARGB(100, 0, 0, 0);
			paint2.setColor(Color.WHITE);
			paint2.setTextSize(30f);
			g.drawString("GAME PAUSED", 145, 400, paint2);
			g.drawString("Tap to resume", 145, 450, paint2);
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
					achievePopup.get(0), achievements[achievePopup.get(0)]);
			Rect bounds = new Rect();
			paint2.getTextBounds(label, 0, label.length(), bounds);
			g.drawString(label, 240, bannerY + 35
					+ (bounds.bottom - bounds.top) / 2, paint2);
			if (achieveTime != 0 && currTime - achieveTime > 4000) {
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
		state = Gamestate.Paused;
		paused = true;
		pauseTime = System.currentTimeMillis();
	}

	@Override
	public void resume() {
		state = Gamestate.Resumed;
		if (paused) {
			pauseTime = System.currentTimeMillis() - pauseTime;
			for (int i = 0; i < 9; i++)
				if (timer[i] != 0)
					timer[i] += pauseTime;
			powerupDuration += pauseTime;
			bonusLabel += pauseTime;
			gameTime += pauseTime;
			paused = false;
		}
	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {
		pause();
	}

	public void createBall() {
		if (!silenced) {
			int theta = gun.getTheta();
			int dir1, dir2 = 1, slopeX, slopeY;
			int x = gun.getX() + 25, y = gun.getY() + 15;
			double newX, newY;
			double ratio;

			if (theta < 0)
				dir1 = 1;
			else
				dir1 = -1;

			if (theta == 0) {
				ratio = -1.0;
				newX = x;
				newY = y + 50;
			} else {
				ratio = Math.abs(Math.tan(Math.toRadians(90 - theta)));
				newX = Math.sqrt(1600 / (Math.pow(ratio, 2) + 1));
				newY = newX * ratio;
				if (theta > 0)
					newX *= -1;
				newX += x;
				newY += y;
			}

			if (ratio < 0.0) {
				slopeY = 1;
				slopeX = 0;
			} else if (ratio > 1.0) {
				slopeX = 1;
				slopeY = (int) Math.round(ratio);
			} else if (ratio < 1.0) {
				slopeY = (int) Math.round(ratio * 10);
				slopeX = 10;
			} else {
				slopeX = 1;
				slopeY = 1;
			}

			Ball bullet = new Ball((int) Math.round(newX),
					(int) Math.round(newY), slopeX, slopeY, speed, dir1, dir2,
					you);
			long currTime = System.currentTimeMillis();
			if ((timer[2] != 0 && currTime - timer[2] < durations[1])
					|| (timer[8] != 0 && currTime - timer[8] < durations[7]))
				bullet.setSize(balls.get(0).getSize());
			if ((timer[5] != 0 && currTime - timer[5] < durations[4])
					|| (timer[6] != 0 && currTime - timer[6] < durations[5]))
				bullet.setSpeed(balls.get(0).getSpeed());
			balls.add(bullet);
			ballLife = System.currentTimeMillis();

			if (achievements[43] == 0) {
				if (difficulty == 0 && balls.size() >= 60)
					if (((SampleGame) game).getMostBalls(1) >= 40
							&& ((SampleGame) game).getMostBalls(2) >= 30
							&& ((SampleGame) game).getMostBalls(3) >= 20) {
						achievements[43] = 1;
						((SampleGame) game).setAchievements(1, 43);
						editor.putInt("Achievements43", 1);
						editor.commit();
						achievePopup.add(43);
					} else if (difficulty == 1 && balls.size() >= 40)
						if (((SampleGame) game).getMostBalls(0) >= 60
								&& ((SampleGame) game).getMostBalls(2) >= 30
								&& ((SampleGame) game).getMostBalls(3) >= 20) {
							achievements[43] = 1;
							((SampleGame) game).setAchievements(1, 43);
							editor.putInt("Achievements43", 1);
							editor.commit();
							achievePopup.add(43);
						} else if (difficulty == 2 && balls.size() >= 30)
							if (((SampleGame) game).getMostBalls(1) >= 40
									&& ((SampleGame) game).getMostBalls(0) >= 60
									&& ((SampleGame) game).getMostBalls(3) >= 20) {
								achievements[43] = 1;
								((SampleGame) game).setAchievements(1, 43);
								editor.putInt("Achievements43", 1);
								editor.commit();
								achievePopup.add(43);
							} else if (difficulty == 3 && balls.size() >= 20)
								if (((SampleGame) game).getMostBalls(1) >= 40
										&& ((SampleGame) game).getMostBalls(2) >= 30
										&& ((SampleGame) game).getMostBalls(0) >= 60) {
									achievements[43] = 1;
									((SampleGame) game).setAchievements(1, 43);
									editor.putInt("Achievements43", 1);
									editor.commit();
									achievePopup.add(43);
								}
			}
		}
	}

	private void createPowerup() {
		powerup.setX(24 + (int) (Math.random() * 436));
		powerup.setY(44 + (int) (Math.random() * 532));
		powerup.setSpawned(true);
		if (Math.ceil(Math.random() * 100) > decreasedChance)
			powerup.newPowerup();
		else
			powerup.newPowerdown();
		powerupDuration = System.currentTimeMillis();
	}

	private void animate() {
		currAnim.update(10);
	}

	private void nullify() {
		balls.clear();
		coin = null;
		coinAnim = null;
		currAnim = null;
		gun = null;
		you = null;
		powerup = null;
		timer = null;
		durations = null;
		youImage = null;
		chances = null;
		saves = null;
		System.gc();
	}
}