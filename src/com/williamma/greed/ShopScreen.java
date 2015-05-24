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
import com.williamma.framework.Image;
import com.williamma.framework.Input.TouchEvent;
import com.williamma.framework.Screen;

public class ShopScreen extends Screen {

	enum State {
		Normal, Confirm, Insufficient, Bought
	}

	private State state = State.Normal;

	private Typeface titleFont, boldFont;
	private Paint paint, paint2, paint3;
	private int totalCoins, page = 1, currentPurchase, row;
	private boolean[] pressed = new boolean[7],
			unlockedSkins = new boolean[26];
	private int[] prices1 = { 100, 200, 300, 500, 1000 }, prices2 = { 50, 100,
			200, 300, 500, 750, 1000, 1500, 2000, 3000 }, prices3 = { 100, 200,
			400, 800, 1200, 2000, 3000, 4000, 5000, 6000 }, prices4 = { 500,
			750, 1000, 1500, 2000, 3000, 4000, 5000, 7500, 10000 }, enhancerT = new int[4], skinT = new int[3];
	private String[][] pagePrice;
	private int coinChance, pickupRadius, coinMultiplier, powerupSpawn,
			powerdownSpawn, bombRadius, superCoinValue, shieldDuration,
			silenceDuration, slowBallDuration, smallBallDuration,
			shrinkYouDuration, growYouDuration, growBallDuration,
			fastBallDuration, dRotation = 0, upgrades, buySkins;
	private Image[] skins = new Image[26];
	private boolean change = false;
	SharedPreferences saves;

	private ArrayList<Integer> achievePopup = new ArrayList<Integer>();
	private int bannerY = 800;
	private long achieveTime;
	boolean starter = false;
	
	public ShopScreen(Game game, Typeface font) {
		super(game);
		titleFont = font;
		boldFont = Typeface.create((String) null, Typeface.BOLD);
		paint = new Paint();
		paint.setTypeface(titleFont);
		paint2 = new Paint();
		paint2.setTypeface(boldFont);
		paint3 = new Paint();
		paint3.setTypeface(boldFont);
		totalCoins = ((SampleGame) game).getTotalCoins();
		saves = PreferenceManager
				.getDefaultSharedPreferences((SampleGame) game);

		// LOAD ALL LEVELS
		coinChance = (int)Math.round(((((SampleGame) game).getCoinChances(0)*60) - 9));
		pickupRadius = (((SampleGame) game).getPickupRadius() - 10) / 3;
		coinMultiplier = ((SampleGame) game).getBonusMultiplier();
		powerupSpawn = Math.abs(((SampleGame) game).getPowerupChance() - 15);
		powerdownSpawn = Math
				.abs((((SampleGame) game).getDecreasedChance() - 30) / 4);
		bombRadius = (((SampleGame) game).getBombRadius() - 50) / 10;
		superCoinValue = (((SampleGame) game).getSuperCoinValue() - 10) / 4;
		shieldDuration = (((SampleGame) game).getPowerupDurations(3) - 1500) / 300;
		silenceDuration = (((SampleGame) game).getPowerupDurations(2) - 5000) / 1000;
		slowBallDuration = (((SampleGame) game).getPowerupDurations(4) - 5000) / 1000;
		smallBallDuration = (((SampleGame) game).getPowerupDurations(1) - 5000) / 1000;
		shrinkYouDuration = (((SampleGame) game).getPowerupDurations(0) - 5000) / 1000;
		growYouDuration = Math
				.abs((((SampleGame) game).getPowerupDurations(6) - 10000) / 1000);
		growBallDuration = Math
				.abs((((SampleGame) game).getPowerupDurations(7) - 10000) / 1000);
		fastBallDuration = Math
				.abs((((SampleGame) game).getPowerupDurations(5) - 10000) / 1000);
		for (int i = 0; i < 26; i++) {
			unlockedSkins[i] = ((SampleGame) game).isUnlocked(i);
			skins[i] = ((SampleGame) game).getSkins(i).resize(60, 60);
		}
		
		upgrades = ((SampleGame) game).getUpgrades();
		buySkins = ((SampleGame) game).getBuySkins();
		enhancerT[0] = 1;
		enhancerT[1] = 10;
		enhancerT[2] = 50;
		enhancerT[3] = 95;
		skinT[0] = 1;
		skinT[1] = 10;
		skinT[2] = 25;
		
		pagePrice = new String[8][5];
		if (coinChance != 10)
			pagePrice[0][0] = Integer.toString(prices3[coinChance]);
		else
			pagePrice[0][0] = "MAX";
		if (pickupRadius != 10)
			pagePrice[0][1] = Integer.toString(prices2[pickupRadius]);
		else
			pagePrice[0][1] = "MAX";
		if (coinMultiplier != 10)
			pagePrice[0][2] = Integer.toString(prices4[coinMultiplier]);
		else
			pagePrice[0][2] = "MAX";
		if (powerupSpawn != 10)
			pagePrice[0][3] = Integer.toString(prices3[powerupSpawn]);
		else
			pagePrice[0][3] = "MAX";
		if (powerdownSpawn != 5)
			pagePrice[0][4] = Integer.toString(prices1[powerdownSpawn]);
		else
			pagePrice[0][4] = "MAX";
		if (bombRadius != 5)
			pagePrice[1][0] = Integer.toString(prices1[bombRadius]);
		else
			pagePrice[1][0] = "MAX";
		if (superCoinValue != 5)
			pagePrice[1][1] = Integer.toString(prices1[superCoinValue]);
		else
			pagePrice[1][1] = "MAX";
		if (shieldDuration != 5)
			pagePrice[1][2] = Integer.toString(prices1[shieldDuration]);
		else
			pagePrice[1][2] = "MAX";
		if (silenceDuration != 5)
			pagePrice[1][3] = Integer.toString(prices1[silenceDuration]);
		else
			pagePrice[1][3] = "MAX";
		if (slowBallDuration != 5)
			pagePrice[1][4] = Integer.toString(prices1[slowBallDuration]);
		else
			pagePrice[1][4] = "MAX";
		if (smallBallDuration != 5)
			pagePrice[2][0] = Integer.toString(prices1[smallBallDuration]);
		else
			pagePrice[2][0] = "MAX";
		if (shrinkYouDuration != 5)
			pagePrice[2][1] = Integer.toString(prices1[shrinkYouDuration]);
		else
			pagePrice[2][1] = "MAX";
		if (growYouDuration != 5)
			pagePrice[2][2] = Integer.toString(prices1[growYouDuration]);
		else
			pagePrice[2][2] = "MAX";
		if (growBallDuration != 5)
			pagePrice[2][3] = Integer.toString(prices1[growBallDuration]);
		else
			pagePrice[2][3] = "MAX";
		if (fastBallDuration != 5)
			pagePrice[2][4] = Integer.toString(prices1[fastBallDuration]);
		else
			pagePrice[2][4] = "MAX";
		int j = 0;
		for (int i = 18; i >= 14; i--) {
			if (!unlockedSkins[i])
				pagePrice[3][j] = "200";
			else
				pagePrice[3][j] = "Bought";
			j++;
		}
		pagePrice[4][0] = getString(8);
		pagePrice[4][1] = getString(11);
		pagePrice[4][2] = getString(6);
		pagePrice[4][3] = getString(19);
		pagePrice[4][4] = getString(12);
		pagePrice[5][0] = getString(9);
		pagePrice[5][1] = getString(4);
		pagePrice[5][2] = getString(24);
		pagePrice[5][3] = getString(0);
		pagePrice[5][4] = getString(10);
		pagePrice[6][0] = getString(5);
		pagePrice[6][1] = getString(1);
		pagePrice[6][2] = getString(3);
		pagePrice[6][3] = getString(2);
		pagePrice[6][4] = getString(21);
		pagePrice[7][0] = getString(22);
		pagePrice[7][1] = getString(23);
		pagePrice[7][2] = getString(20);
		pagePrice[7][3] = getString(7);
		pagePrice[7][4] = getString(25);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent e = touchEvents.get(i);
			if (state == State.Normal) {
				if (e.type == TouchEvent.TOUCH_DOWN) {
					if (inBounds(e, 364, 215, 100, 50))
						pressed[0] = true;
					else if (inBounds(e, 364, 315, 100, 50))
						pressed[1] = true;
					else if (inBounds(e, 364, 415, 100, 50))
						pressed[2] = true;
					else if (inBounds(e, 364, 515, 100, 50))
						pressed[3] = true;
					else if (inBounds(e, 364, 615, 100, 50))
						pressed[4] = true;
					else if (inBounds(e, 5, 720, 150, 50))
						pressed[5] = true;
					else if (inBounds(e, 325, 720, 150, 50))
						pressed[6] = true;
				} else if (e.type == TouchEvent.TOUCH_UP) {
					if (inBounds(e, 364, 215, 100, 50) && pressed[0]) {
						if (pagePrice[page - 1][0].equals("MAX")
								|| pagePrice[page - 1][0].equals("Bought"))
							state = State.Bought;
						else if (Integer.parseInt(pagePrice[page - 1][0]) > totalCoins)
							state = State.Insufficient;
						else {
							currentPurchase = Integer
									.parseInt(pagePrice[page - 1][0]);
							row = 0;
							state = State.Confirm;
						}
					} else if (inBounds(e, 364, 315, 100, 50) && pressed[1]) {
						if (pagePrice[page - 1][1].equals("MAX")
								|| pagePrice[page - 1][1].equals("Bought"))
							state = State.Bought;
						else if (Integer.parseInt(pagePrice[page - 1][1]) > totalCoins)
							state = State.Insufficient;
						else {
							currentPurchase = Integer
									.parseInt(pagePrice[page - 1][1]);
							row = 1;
							state = State.Confirm;
						}
					} else if (inBounds(e, 364, 415, 100, 50) && pressed[2]) {
						if (pagePrice[page - 1][2].equals("MAX")
								|| pagePrice[page - 1][2].equals("Bought"))
							state = State.Bought;
						else if (Integer.parseInt(pagePrice[page - 1][2]) > totalCoins)
							state = State.Insufficient;
						else {
							currentPurchase = Integer
									.parseInt(pagePrice[page - 1][2]);
							row = 2;
							state = State.Confirm;
						}
					} else if (inBounds(e, 364, 515, 100, 50) && pressed[3]) {
						if (pagePrice[page - 1][3].equals("MAX")
								|| pagePrice[page - 1][3].equals("Bought"))
							state = State.Bought;
						else if (Integer.parseInt(pagePrice[page - 1][3]) > totalCoins)
							state = State.Insufficient;
						else {
							currentPurchase = Integer
									.parseInt(pagePrice[page - 1][3]);
							row = 3;
							state = State.Confirm;
						}
					} else if (inBounds(e, 364, 615, 100, 50) && pressed[4]) {
						if (pagePrice[page - 1][4].equals("MAX")
								|| pagePrice[page - 1][4].equals("Bought"))
							state = State.Bought;
						else if (Integer.parseInt(pagePrice[page - 1][4]) > totalCoins)
							state = State.Insufficient;
						else {
							currentPurchase = Integer
									.parseInt(pagePrice[page - 1][4]);
							row = 4;
							state = State.Confirm;
						}
					} else if (page > 1 && inBounds(e, 5, 720, 150, 50)
							&& pressed[5])
						page--;
					else if (page < 8 && inBounds(e, 325, 720, 150, 50)
							&& pressed[6])
						page++;
					else
						for (int j = 0; j < 7; j++)
							pressed[j] = false;
				}
			} else if (state == State.Confirm) {
				if (e.type == TouchEvent.TOUCH_DOWN) {
					if (inBounds(e, 80, 415, 100, 40))
						pressed[0] = true;
					else if (inBounds(e, 300, 415, 100, 40))
						pressed[1] = true;
				} else if (e.type == TouchEvent.TOUCH_UP) {
					if (pressed[0] && inBounds(e, 80, 415, 100, 40)) {
						totalCoins -= currentPurchase;
						buy();
						state = State.Normal;
					} else if (pressed[1] && inBounds(e, 300, 415, 100, 40))
						state = State.Normal;
					else
						for (int j = 0; j < 2; j++)
							pressed[j] = false;
				}
			} else {
				if (e.type == TouchEvent.TOUCH_DOWN) {
					if (inBounds(e, 190, 415, 100, 40))
						pressed[0] = true;
				} else if (e.type == TouchEvent.TOUCH_UP) {
					if (pressed[0] && inBounds(e, 190, 415, 100, 40))
						state = State.Normal;
					else
						pressed[0] = false;
				}
			}
		}

		dRotation++;
		if (dRotation == 361)
			dRotation = 1;
		
		if (((SampleGame) game).getAchievements(44) == 0 && ((SampleGame) game).isOpened()) {
			((SampleGame) game).setAchievements(1,44);
			SharedPreferences.Editor editor = saves.edit();
			editor.putInt("Achievements44", 1);
			editor.commit();
			achievePopup.add(44);
		}
	}

	private void buy() {
		SharedPreferences.Editor editor = saves.edit();
		int value;
		switch (page) {
		case 1:
			switch (row) {
			case 0:
				coinChance++;
				if (coinChance == 10) {
					pagePrice[0][0] = "MAX";
					if (((SampleGame) game).getAchievements(39) == 0) {
						((SampleGame) game).setAchievements(1, 39);
						editor.putInt("Achievements39", 1);
						achievePopup.add(39);
					}
				} else
					pagePrice[0][0] = Integer.toString(prices3[coinChance]);
				editor.putFloat("Coin Chance0", (9 + coinChance)/60.0f);
				editor.putFloat("Coin Chance1", 1.0f/(30-(2*coinChance)));
				editor.putFloat("Coin Chance2", 1.0f/(50-(3*coinChance)));
				editor.putFloat("Coin Chance3", 1.0f/(70-(4*coinChance)));
				break;
			case 1:
				pickupRadius++;
				if (pickupRadius == 10) {
					pagePrice[0][1] = "MAX";
					if (((SampleGame) game).getAchievements(39) == 0) {
						((SampleGame) game).setAchievements(1, 39);
						editor.putInt("Achievements39", 1);
						achievePopup.add(39);
					}
				} else
					pagePrice[0][1] = Integer.toString(prices2[pickupRadius]);
				editor.putInt("Pickup Radius", 10 + (3*pickupRadius));
				break;
			case 2:
				coinMultiplier++;
				if (coinMultiplier == 10) {
					pagePrice[0][2] = "MAX";
					if (((SampleGame) game).getAchievements(39) == 0) {
						((SampleGame) game).setAchievements(1, 39);
						editor.putInt("Achievements39", 1);
						achievePopup.add(39);
					}
				} else
					pagePrice[0][2] = Integer.toString(prices4[coinMultiplier]);
				editor.putInt("Bonus Multiplier", coinMultiplier);
				break;
			case 3:
				powerupSpawn++;
				if (powerupSpawn == 10) {
					pagePrice[0][3] = "MAX";
					if (((SampleGame) game).getAchievements(39) == 0) {
						((SampleGame) game).setAchievements(1, 39);
						editor.putInt("Achievements39", 1);
						achievePopup.add(39);
					}
				} else
					pagePrice[0][3] = Integer.toString(prices3[powerupSpawn]);
				editor.putInt("Powerup Chance", 15-powerupSpawn);
				break;
			case 4:
				powerdownSpawn++;
				if (powerdownSpawn == 5) {
					pagePrice[0][4] = "MAX";
					if (((SampleGame) game).getAchievements(39) == 0) {
						((SampleGame) game).setAchievements(1, 39);
						editor.putInt("Achievements39", 1);
						achievePopup.add(39);
					}
				} else
					pagePrice[0][4] = Integer.toString(prices1[powerdownSpawn]);
				editor.putInt("Decreased Chance", 30-(4*powerdownSpawn));
				break;
			}
			upgrades++;
			editor.putInt("Upgrades", upgrades);
			value = ((SampleGame) game).getAchievements(13);
			if (value < 4 && upgrades >= enhancerT[value]) {
				value++;
				((SampleGame) game).setAchievements(value, 13);
				editor.putInt("Achievements13", value);
				achievePopup.add(13);
			}
			break;
		case 2:
			switch (row) {
			case 0:
				bombRadius++;
				if (bombRadius == 5) {
					pagePrice[1][0] = "MAX";
					if (((SampleGame) game).getAchievements(39) == 0) {
						((SampleGame) game).setAchievements(1, 39);
						editor.putInt("Achievements39", 1);
						achievePopup.add(39);
					}
				} else
					pagePrice[1][0] = Integer.toString(prices1[bombRadius]);
				editor.putInt("Bomb Radius", 50 + (10*bombRadius));
				break;
			case 1:
				superCoinValue++;
				if (superCoinValue == 5) {
					pagePrice[1][1] = "MAX";
					if (((SampleGame) game).getAchievements(39) == 0) {
						((SampleGame) game).setAchievements(1, 39);
						editor.putInt("Achievements39", 1);
						achievePopup.add(39);
					}
				} else
					pagePrice[1][1] = Integer.toString(prices1[superCoinValue]);
				editor.putInt("Super Coin Value", 10 + (4*superCoinValue));
				break;
			case 2:
				shieldDuration++;
				if (shieldDuration == 5) {
					pagePrice[1][2] = "MAX";
					if (((SampleGame) game).getAchievements(39) == 0) {
						((SampleGame) game).setAchievements(1, 39);
						editor.putInt("Achievements39", 1);
						achievePopup.add(39);
					}
				} else
					pagePrice[1][2] = Integer.toString(prices1[shieldDuration]);
				editor.putInt("Powerup Duration3", 1500 + (300*shieldDuration));
				break;
			case 3:
				silenceDuration++;
				if (silenceDuration == 5) {
					pagePrice[1][3] = "MAX";
					if (((SampleGame) game).getAchievements(39) == 0) {
						((SampleGame) game).setAchievements(1, 39);
						editor.putInt("Achievements39", 1);
						achievePopup.add(39);
					}
				} else
					pagePrice[1][3] = Integer.toString(prices1[silenceDuration]);
				editor.putInt("Powerup Duration2", 5000 + (1000*silenceDuration));
				break;
			case 4:
				slowBallDuration++;
				if (slowBallDuration == 5) {
					pagePrice[1][4] = "MAX";
					if (((SampleGame) game).getAchievements(39) == 0) {
						((SampleGame) game).setAchievements(1, 39);
						editor.putInt("Achievements39", 1);
						achievePopup.add(39);
					}
				} else
					pagePrice[1][4] = Integer.toString(prices1[slowBallDuration]);
				editor.putInt("Powerup Duration4", 5000 + (1000*slowBallDuration));
				break;
			}
			upgrades++;
			editor.putInt("Upgrades", upgrades);
			value = ((SampleGame) game).getAchievements(13);
			if (value < 4 && upgrades >= enhancerT[value]) {
				value++;
				((SampleGame) game).setAchievements(value, 13);
				editor.putInt("Achievements13", value);
				achievePopup.add(13);
			}
			break;
		case 3:
			switch (row) {
			case 0:
				smallBallDuration++;
				if (smallBallDuration == 5) {
					pagePrice[2][0] = "MAX";
					if (((SampleGame) game).getAchievements(39) == 0) {
						((SampleGame) game).setAchievements(1, 39);
						editor.putInt("Achievements39", 1);
						achievePopup.add(39);
					}
				} else
					pagePrice[2][0] = Integer.toString(prices1[smallBallDuration]);
				editor.putInt("Powerup Duration1", 5000 + (1000*smallBallDuration));
				break;
			case 1:
				shrinkYouDuration++;
				if (shrinkYouDuration == 5) {
					pagePrice[2][1] = "MAX";
					if (((SampleGame) game).getAchievements(39) == 0) {
						((SampleGame) game).setAchievements(1, 39);
						editor.putInt("Achievements39", 1);
						achievePopup.add(39);
					}
				} else
					pagePrice[2][1] = Integer.toString(prices1[shrinkYouDuration]);
				editor.putInt("Powerup Duration0", 5000 + (1000*shrinkYouDuration));
				break;
			case 2:
				growYouDuration++;
				if (growYouDuration == 5) {
					pagePrice[2][2] = "MAX";
					if (((SampleGame) game).getAchievements(39) == 0) {
						((SampleGame) game).setAchievements(1, 39);
						editor.putInt("Achievements39", 1);
						achievePopup.add(39);
					}
				} else
					pagePrice[2][2] = Integer.toString(prices1[growYouDuration]);
				editor.putInt("Powerup Duration6", 10000 - (1000*growYouDuration));
				break;
			case 3:
				growBallDuration++;
				if (growBallDuration == 5) {
					pagePrice[2][3] = "MAX";
					if (((SampleGame) game).getAchievements(39) == 0) {
						((SampleGame) game).setAchievements(1, 39);
						editor.putInt("Achievements39", 1);
						achievePopup.add(39);
					}
				} else
					pagePrice[2][3] = Integer.toString(prices1[growBallDuration]);
				editor.putInt("Powerup Duration7", 10000 - (1000*growBallDuration));
				break;
			case 4:
				fastBallDuration++;
				if (fastBallDuration == 5) {
					pagePrice[2][4] = "MAX";
					if (((SampleGame) game).getAchievements(39) == 0) {
						((SampleGame) game).setAchievements(1, 39);
						editor.putInt("Achievements39", 1);
						achievePopup.add(39);
					}
				} else
					pagePrice[2][4] = Integer.toString(prices1[fastBallDuration]);
				editor.putInt("Powerup Duration5", 10000 - (1000*fastBallDuration));
				break;
			}
			upgrades++;
			editor.putInt("Upgrades", upgrades);
			value = ((SampleGame) game).getAchievements(13);
			if (value < 4 && upgrades >= enhancerT[value]) {
				value++;
				((SampleGame) game).setAchievements(value, 13);
				editor.putInt("Achievements13", value);
				achievePopup.add(13);
			}
			break;
		case 4:
			switch (row) {
			case 0:
				pagePrice[3][0] = "Bought";
				editor.putBoolean("Skin18", true);
				break;
			case 1:
				pagePrice[3][1] = "Bought";
				editor.putBoolean("Skin17", true);
				break;
			case 2:
				pagePrice[3][2] = "Bought";
				editor.putBoolean("Skin16", true);
				break;
			case 3:
				pagePrice[3][3] = "Bought";
				editor.putBoolean("Skin15", true);
				break;
			case 4:
				pagePrice[3][4] = "Bought";
				editor.putBoolean("Skin14", true);
				break;
			}
			buySkins++;
			editor.putInt("Buy Skins", buySkins);
			value = ((SampleGame) game).getAchievements(14);
			if (value < 3 && buySkins >= skinT[value]) {
				value++;
				((SampleGame) game).setAchievements(value, 14);
				editor.putInt("Achievements14", value);
				achievePopup.add(14);
			}
			break;
		case 5:
			switch (row) {
			case 0:
				pagePrice[4][0] = "Bought";
				editor.putBoolean("Skin8", true);
				break;
			case 1:
				pagePrice[4][1] = "Bought";
				editor.putBoolean("Skin11", true);
				break;
			case 2:
				pagePrice[4][2] = "Bought";
				editor.putBoolean("Skin6", true);
				break;
			case 3:
				pagePrice[4][3] = "Bought";
				editor.putBoolean("Skin19", true);
				break;
			case 4:
				pagePrice[4][4] = "Bought";
				editor.putBoolean("Skin12", true);
				((SampleGame) game).setAchievements(1, 26);
				editor.putInt("Achievements26", 1);
				achievePopup.add(26);
				break;
			}
			buySkins++;
			editor.putInt("Buy Skins", buySkins);
			value = ((SampleGame) game).getAchievements(14);
			if (value < 3 && buySkins >= skinT[value]) {
				value++;
				((SampleGame) game).setAchievements(value, 14);
				editor.putInt("Achievements14", value);
				achievePopup.add(14);
			}
			break;
		case 6:
			switch (row) {
			case 0:
				pagePrice[5][0] = "Bought";
				editor.putBoolean("Skin9", true);
				break;
			case 1:
				pagePrice[5][1] = "Bought";
				editor.putBoolean("Skin4", true);
				break;
			case 2:
				pagePrice[5][2] = "Bought";
				editor.putBoolean("Skin24", true);
				break;
			case 3:
				pagePrice[5][3] = "Bought";
				editor.putBoolean("Skin0", true);
				break;
			case 4:
				pagePrice[5][4] = "Bought";
				editor.putBoolean("Skin10", true);
				break;
			}
			buySkins++;
			editor.putInt("Buy Skins", buySkins);
			value = ((SampleGame) game).getAchievements(14);
			if (value < 3 && buySkins >= skinT[value]) {
				value++;
				((SampleGame) game).setAchievements(value, 14);
				editor.putInt("Achievements14", value);
				achievePopup.add(14);
			}
			break;
		case 7:
			switch (row) {
			case 0:
				pagePrice[6][0] = "Bought";
				editor.putBoolean("Skin5", true);
				break;
			case 1:
				pagePrice[6][1] = "Bought";
				editor.putBoolean("Skin1", true);
				break;
			case 2:
				pagePrice[6][2] = "Bought";
				editor.putBoolean("Skin3", true);
				break;
			case 3:
				pagePrice[6][3] = "Bought";
				editor.putBoolean("Skin2", true);
				break;
			case 4:
				pagePrice[6][4] = "Bought";
				editor.putBoolean("Skin21", true);
				if (pagePrice[7][0].equals("Bought") && pagePrice[7][1].equals("Bought")) {
					((SampleGame) game).setAchievements(1, 27);
					editor.putInt("Achievements27", 1);
					achievePopup.add(27);
				}
				break;
			}
			buySkins++;
			editor.putInt("Buy Skins", buySkins);
			value = ((SampleGame) game).getAchievements(14);
			if (value < 3 && buySkins >= skinT[value]) {
				value++;
				((SampleGame) game).setAchievements(value, 14);
				editor.putInt("Achievements14", value);
				achievePopup.add(14);
			}
			break;
		case 8:
			switch (row) {
			case 0:
				pagePrice[7][0] = "Bought";
				editor.putBoolean("Skin22", true);
				if (pagePrice[6][4].equals("Bought") && pagePrice[7][1].equals("Bought")) {
					((SampleGame) game).setAchievements(1, 27);
					editor.putInt("Achievements27", 1);
					achievePopup.add(27);
				}
				break;
			case 1:
				pagePrice[7][1] = "Bought";
				editor.putBoolean("Skin23", true);
				if (pagePrice[7][0].equals("Bought") && pagePrice[6][4].equals("Bought")) {
					((SampleGame) game).setAchievements(1, 27);
					editor.putInt("Achievements27", 1);
					achievePopup.add(27);
				}
				break;
			case 2:
				pagePrice[7][2] = "Bought";
				editor.putBoolean("Skin20", true);
				break;
			case 3:
				pagePrice[7][3] = "Bought";
				editor.putBoolean("Skin7", true);
				((SampleGame) game).setAchievements(1, 28);
				editor.putInt("Achievements28", 1);
				achievePopup.add(28);
				break;
			case 4:
				pagePrice[7][4] = "Bought";
				editor.putBoolean("Skin25", true);
				((SampleGame) game).setAchievements(1, 29);
				editor.putInt("Achievements29", 1);
				achievePopup.add(29);
				break;
			}
			buySkins++;
			editor.putInt("Buy Skins", buySkins);
			value = ((SampleGame) game).getAchievements(14);
			if (value < 3 && buySkins >= skinT[value]) {
				value++;
				((SampleGame) game).setAchievements(value, 14);
				editor.putInt("Achievements14", value);
				achievePopup.add(14);
			}
			break;
		}
		change = true;
		editor.putInt("Total Coins", totalCoins);
		editor.commit();
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
		g.drawARGB(255, 205, 133, 63);
		paint.setTextSize(110f);
		paint.setColor(Color.BLACK);
		g.drawString("Shop", 125, 110, paint);
		paint2.setTextSize(40f);
		paint2.setColor(Color.BLACK);
		paint2.setTextAlign(Align.LEFT);
		g.drawString("Total Coins: " + totalCoins, 5, 180, paint2);
		for (int i = 190; i < 690; i += 100)
			g.drawHollowRect(3, i, 474, 100, Color.YELLOW, 3);
		for (int i = 215; i < 715; i += 100) {
			g.drawRect(364, i, 100, 50, Color.GREEN);
			g.drawHollowRect(364, i, 100, 50, Color.WHITE, 2);
		}

		if (page == 1)
			g.drawRect(5, 720, 150, 50, Color.GRAY);
		else
			g.drawRect(5, 720, 150, 50, Color.BLACK);
		if (page == 8)
			g.drawRect(325, 720, 150, 50, Color.GRAY);
		else
			g.drawRect(325, 720, 150, 50, Color.BLACK);

		paint2.setColor(Color.WHITE);
		paint2.setTextSize(30f);
		for (int i = 250; i < 750; i += 100)
			g.drawString("BUY", 386, i, paint2);
		g.drawString("Previous", 17, 755, paint2);
		g.drawString("Next", 365, 755, paint2);
		g.drawString("Page " + page + "/8", 180, 755, paint2);

		int color = Color.argb(255, 169, 69, 19);
		if (page < 4) {
			paint2.setTextSize(25f);
			paint2.setColor(Color.BLACK);
			paint3.setTextSize(20f);
			paint3.setColor(Color.BLACK);
			if (page == 1) {
				g.drawRect(13, 205, coinChance * 30, 30, Color.YELLOW);
				g.drawRect(13, 305, pickupRadius * 30, 30, Color.YELLOW);
				g.drawRect(13, 405, coinMultiplier * 30, 30, Color.YELLOW);
				g.drawRect(13, 505, powerupSpawn * 30, 30, Color.YELLOW);
				g.drawRect(13, 605, powerdownSpawn * 60, 30, Color.YELLOW);
				g.drawString("Increase chance of getting", 13, 255, paint3);
				g.drawString("better coins", 13, 280, paint3);
				g.drawString("Pick up coins further away", 13, 355, paint3);
				g.drawString("from you", 13, 380, paint3);
				g.drawString("Get more bonus coins", 13, 455, paint3);
				g.drawString("Increase the frequency", 13, 555, paint3);
				g.drawString("powerup spawns", 13, 580, paint3);
				g.drawString("Decrease the frequency of", 13, 655, paint3);
				g.drawString("bad powerup spawns", 13, 680, paint3);
				int j = 0;
				for (int i = 270; i < 770; i += 100) {
					if (pagePrice[0][j].equals("MAX"))
						g.drawString(pagePrice[0][j], 270, i, paint2);
					else
						g.drawString("$" + pagePrice[0][j], 270, i, paint2);
					j++;
				}
			} else if (page == 2) {
				g.drawRect(13, 205, bombRadius * 60, 30, Color.YELLOW);
				g.drawRect(13, 305, superCoinValue * 60, 30, Color.YELLOW);
				g.drawRect(13, 405, shieldDuration * 60, 30, Color.YELLOW);
				g.drawRect(13, 505, silenceDuration * 60, 30, Color.YELLOW);
				g.drawRect(13, 605, slowBallDuration * 60, 30, Color.YELLOW);
				g.drawString("Increase the bomb radius", 13, 255, paint3);
				g.drawString("Increase the super coin", 13, 355, paint3);
				g.drawString("value", 13, 380, paint3);
				g.drawString("Increase shield duration", 13, 455, paint3);
				g.drawString("Increase silence duration", 13, 555, paint3);
				g.drawString("Increase slow ball duration", 13, 655, paint3);
				int j = 0;
				for (int i = 270; i < 770; i += 100) {
					if (pagePrice[1][j].equals("MAX"))
						g.drawString(pagePrice[1][j], 270, i, paint2);
					else
						g.drawString("$" + pagePrice[1][j], 270, i, paint2);
					j++;
				}
			} else if (page == 3) {
				g.drawRect(13, 205, smallBallDuration * 60, 30, Color.YELLOW);
				g.drawRect(13, 305, shrinkYouDuration * 60, 30, Color.YELLOW);
				g.drawRect(13, 405, growYouDuration * 60, 30, Color.YELLOW);
				g.drawRect(13, 505, growBallDuration * 60, 30, Color.YELLOW);
				g.drawRect(13, 605, fastBallDuration * 60, 30, Color.YELLOW);
				g.drawString("Increase small ball duration", 13, 255, paint3);
				g.drawString("Increase shrink duration", 13, 355, paint3);
				g.drawString("Decrease grow duration", 13, 455, paint3);
				g.drawString("Decrease big ball duration", 13, 555, paint3);
				g.drawString("Decrease fast ball duration", 13, 655, paint3);
				int j = 0;
				for (int i = 270; i < 770; i += 100) {
					if (pagePrice[2][j].equals("MAX"))
						g.drawString(pagePrice[2][j], 270, i, paint2);
					else
						g.drawString("$" + pagePrice[2][j], 270, i, paint2);
					j++;
				}
			}

			for (int i = 205; i < 705; i += 100)
				g.drawHollowRect(13, i, 300, 30, color, 3);
			for (int i = 73; i < 254; i += 60)
				g.drawLine(i, 605, i, 635, color, 3);

			if (page == 1) {
				for (int j = 205; j < 605; j += 100)
					for (int i = 43; i < 313; i += 30)
						g.drawLine(i, j, i, j + 30, color, 3);
			} else {
				for (int j = 205; j < 605; j += 100)
					for (int i = 73; i < 254; i += 60)
						g.drawLine(i, j, i, j + 30, color, 3);
			}
		} else if (page < 9) {
			for (int i = 200; i < 700; i += 100) {
				g.drawRect(13, i, 80, 80, Color.WHITE);
				g.drawHollowRect(13, i, 80, 80, color, 3);
			}

			paint2.setTextSize(30f);
			paint2.setColor(Color.BLACK);

			if (page == 4) {
				g.drawImage(skins[18], 23, 210);
				g.drawImage(skins[17], 23, 310);
				g.drawImage(skins[16], 23, 410);
				g.drawImage(skins[15], 23, 510);
				g.drawImage(skins[14], 23, 610);
				g.drawString("Yellow Skin", 103, 247, paint3);
				g.drawString("Blue Skin", 103, 347, paint3);
				g.drawString("Cyan Skin", 103, 447, paint3);
				g.drawString("Magenta Skin", 103, 547, paint3);
				g.drawString("Black Skin", 103, 647, paint3);
				int j = 0;
				for (int i = 250; i < 750; i += 100) {
					if (pagePrice[3][j].equals("Bought"))
						g.drawString(pagePrice[3][j], 250, i, paint2);
					else
						g.drawString("$" + pagePrice[3][j], 250, i, paint2);
					j++;
				}
			} else if (page == 5) {
				g.drawImage(skins[8], 23, 210);
				g.drawImage(skins[11], 23, 310);
				g.drawImage(skins[6], 23, 410);
				g.drawImage(skins[19], 23, 510);
				g.drawImage(skins[12], 23, 610);
				g.drawString("Happy Skin", 103, 247, paint3);
				g.drawString("Skull Skin", 103, 347, paint3);
				g.drawString("Love Skin", 103, 447, paint3);
				g.drawString("Cookie Skin", 103, 547, paint3);
				g.drawString("Sad Skin", 103, 647, paint3);
				int j = 0;
				for (int i = 250; i < 750; i += 100) {
					if (pagePrice[4][j].equals("Bought"))
						g.drawString(pagePrice[4][j], 250, i, paint2);
					else
						g.drawString("$" + pagePrice[4][j], 250, i, paint2);
					j++;
				}
			} else if (page == 6) {
				g.drawImage(skins[9], 23, 210);
				g.drawImage(skins[4], 23, 310);
				g.drawImage(skins[24], 23, 410);
				g.drawImage(skins[0], 23, 510);
				g.drawImage(skins[10], 23, 610);
				g.drawString("Ecstatic Skin", 103, 247, paint3);
				g.drawString("Fire Skin", 103, 347, paint3);
				g.drawString("Pokeball Skin", 103, 447, paint3);
				g.drawString("Shock Skin", 103, 547, paint3);
				g.drawString("Ninja Star Skin", 103, 647, paint3);
				int j = 0;
				for (int i = 250; i < 750; i += 100) {
					if (pagePrice[5][j].equals("Bought"))
						g.drawString(pagePrice[5][j], 250, i, paint2);
					else
						g.drawString("$" + pagePrice[5][j], 250, i, paint2);
					j++;
				}
			} else if (page == 7) {
				g.drawImage(skins[5], 23, 210);
				g.drawImage(skins[1], 23, 310);
				g.drawImage(skins[3], 23, 410);
				g.drawImage(skins[2], 23, 510);
				g.drawImage(skins[21], 23, 610);
				g.drawString("Slime Skin", 103, 247, paint3);
				g.drawString("Minion Skin", 103, 347, paint3);
				g.drawString("Batman Skin", 103, 447, paint3);
				g.drawString("Superman Skin", 103, 547, paint3);
				g.drawString("Marine Skin", 103, 647, paint3);
				int j = 0;
				for (int i = 250; i < 750; i += 100) {
					if (pagePrice[6][j].equals("Bought"))
						g.drawString(pagePrice[6][j], 250, i, paint2);
					else
						g.drawString("$" + pagePrice[6][j], 250, i, paint2);
					j++;
				}
			} else if (page == 8) {
				g.drawImage(skins[22], 23, 210);
				g.drawImage(skins[23], 23, 310);
				g.drawImage(skins[20], 23, 410);
				g.drawImage(skins[7], 23, 510);
				g.save();
				g.rotate(dRotation, 53, 640);
				g.drawImage(skins[25], 23, 610);
				g.restore();
				g.drawString("Zergling Skin", 103, 247, paint3);
				g.drawString("Zealot Skin", 103, 347, paint3);
				g.drawString("Rich Skin", 103, 447, paint3);
				g.drawString("Moneybag Skin", 103, 547, paint3);
				g.drawString("Diamond Skin", 103, 647, paint3);
				int j = 0;
				for (int i = 250; i < 750; i += 100) {
					if (pagePrice[7][j].equals("Bought"))
						g.drawString(pagePrice[7][j], 250, i, paint2);
					else
						g.drawString("$" + pagePrice[7][j], 250, i, paint2);
					j++;
				}
			}
		}

		if (state != State.Normal) {
			g.drawARGB(100, 0, 0, 0);
			g.drawRect(50, 325, 380, 150, Color.LTGRAY);
			g.drawHollowRect(50, 325, 380, 150, color, 5);
			paint2.setColor(Color.RED);
			paint2.setTextSize(30f);
			if (state == State.Confirm) {
				g.drawString("Confirm purchase", 122, 365, paint2);
				if (currentPurchase < 10000)
					g.drawString("for $" + currentPurchase + "?", 173, 395,
							paint2);
				else
					g.drawString("for $" + currentPurchase + "?", 158, 395,
							paint2);
				g.drawRect(80, 415, 100, 40, Color.BLACK);
				g.drawRect(300, 415, 100, 40, Color.BLACK);
				paint2.setColor(Color.WHITE);
				g.drawString("Yes", 105, 445, paint2);
				g.drawString("No", 330, 445, paint2);
			} else {
				if (state == State.Bought) {
					g.drawString("You already purchased", 85, 365, paint2);
					g.drawString("this", 212, 400, paint2);
				} else
					g.drawString("Insufficient coins", 120, 375, paint2);
				g.drawRect(190, 415, 100, 40, Color.BLACK);
				paint2.setColor(Color.WHITE);
				g.drawString("OK", 220, 445, paint2);
			}
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

	private String getString(int i) {
		if (unlockedSkins[i])
			return "Bought";
		else if (i == 8 || i == 6 || i == 11 || i == 19)
			return "500";
		else if (i == 12 || i == 9 || i == 4)
			return "1000";
		else if (i == 24 || i == 0 || i == 10 || i == 5)
			return "2000";
		else if ((i >= 1 && i <= 3) || (i >= 21 && i <= 23))
			return "3000";
		else if (i == 20)
			return "5000";
		else if (i == 7)
			return "10000";
		else
			return "25000";
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
		if (state == State.Normal) {
			if (change)
				((SampleGame) game).load();
			game.setScreen(new MainMenuScreen(game, titleFont));
		}
		else
			state = State.Normal;
	}

}