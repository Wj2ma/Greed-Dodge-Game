package com.williamma.greed;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.williamma.framework.Graphics;
import com.williamma.framework.Graphics.ImageFormat;
import com.williamma.framework.Image;
import com.williamma.framework.Screen;
import com.williamma.framework.implementation.AndroidGame;

public class SampleGame extends AndroidGame {

	private double sensitivity;
	private int skin, powerupChance, bombRadius, pickupRadius, lifetimeCoins,
			totalCoins, bonusMultiplier, decreasedChance, deaths,
			superCoinValue, totalPowerups, playTime, upgrades, buySkins;
	private int[] powerupDurations = new int[8], highscores = new int[4],
			mostCoins = new int[4], coinCounts = new int[5],
			death = new int[4], achievements = new int[45], mostBalls = new int[4];
	private boolean[] unlockedSkins = new boolean[26];
	private boolean muted;
	private double[] coinChances = new double[4], averageScore = new double[4],
			averageCoins = new double[4];
	private Image[] skins = new Image[26];

	@Override
	public Screen getInitScreen() {
		Graphics g = this.getGraphics();
		Assets.genesis1 = g.newImage("Genesis1.png", ImageFormat.RGB565);
		Assets.genesis2 = g.newImage("Genesis2.png", ImageFormat.RGB565);
		Assets.genesis3 = g.newImage("Genesis3.png", ImageFormat.RGB565);
		Assets.genesis4 = g.newImage("Genesis4.png", ImageFormat.RGB565);
		Assets.genesis5 = g.newImage("Genesis5.png", ImageFormat.RGB565);
		Assets.genesis6 = g.newImage("Genesis6.png", ImageFormat.RGB565);
		Assets.genesis7 = g.newImage("Genesis7.png", ImageFormat.RGB565);
		unlockedSkins[13] = true;
		return new SplashLoadingScreen(this);
	}

	@Override
	public void onBackPressed() {
		getCurrentScreen().backButton();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public double getSensitivity() {
		return sensitivity;
	}

	public void setSensitivity(double s) {
		sensitivity = s;
	}

	public void setSkin(int i) {
		skin = i;
	}

	public Image getSkin() {
		return skins[skin];
	}

	public int getSkinNum() {
		return skin;
	}

	public int getPowerupChance() {
		return powerupChance;
	}

	public int getBombRadius() {
		return bombRadius;
	}

	public int getPickupRadius() {
		return pickupRadius;
	}

	public int getLifetimeCoins() {
		return lifetimeCoins;
	}

	public int getTotalCoins() {
		return totalCoins;
	}

	public int getPlayTime() {
		return playTime;
	}

	public int getBonusMultiplier() {
		return bonusMultiplier;
	}

	public int getHighscores(int i) {
		return highscores[i];
	}

	public int getMostCoins(int i) {
		return mostCoins[i];
	}

	public int getDecreasedChance() {
		return decreasedChance;
	}

	public int getPowerupDurations(int i) {
		return powerupDurations[i];
	}

	public double getCoinChances(int i) {
		return coinChances[i];
	}

	public int getCoinCount(int i) {
		return coinCounts[i];
	}

	public Image getSkins(int i) {
		return skins[i];
	}

	public int getMostBalls(int i) {
		return mostBalls[i];
	}
	
	public boolean isUnlocked(int i) {
		return unlockedSkins[i];
	}

	public boolean isMuted() {
		return muted;
	}

	public int getUpgrades() {
		return upgrades;
	}
	
	public int getBuySkins() {
		return buySkins;
	}
	
	public String getAchieveTitle(int i, int j) {
		switch(i) {
		case 0: 
			switch (j) {
			case 1: 
				return "Yummy Gold! I";
			case 2: 
				return "Yummy Gold! II";
			case 3: 
				return "Yummy Gold! III";
			case 4: 
				return "Yummy Gold! IV";
			case 5:
				return "Yummy Gold! V";
			default:
				return "Yummy Gold!";
			}
		case 1:
			switch (j) {
			case 1: 
				return "OoO Specials! I";
			case 2: 
				return "OoO Specials! II";
			case 3: 
				return "OoO Specials! III";
			case 4: 
				return "OoO Specials! IV";
			case 5:
				return "OoO Specials! V";
			default:
				return "OoO Specials!";
			}
		case 2:
			switch (j) {
			case 1: 
				return "SOo mAnY RaReS! I";
			case 2: 
				return "SOo mAnY RaReS! II";
			case 3: 
				return "SOo mAnY RaReS! III";
			case 4: 
				return "SOo mAnY RaReS! IV";
			case 5:
				return "SOo mAnY RaReS! V";
			default:
				return "SOo mAnY RaReS!";
			}
		case 3:
			switch (j) {
			case 1: 
				return "Lucky Guy! I";
			case 2: 
				return "Lucky Guy! II";
			case 3: 
				return "Lucky Guy! III";
			case 4: 
				return "Lucky Guy! IV";
			case 5:
				return "Lucky Guy! V";
			default:
				return "Lucky Guy!";
			}
		case 4:
			switch (j) {
			case 1: 
				return "Precious Little Coins I";
			case 2: 
				return "Precious Little Coins II";
			case 3: 
				return "Precious Little Coins III";
			case 4: 
				return "Precious Little Coins IV";
			case 5:
				return "Precious Little Coins V";
			default:
				return "Precious Little Coins";
			}
		case 5:
			switch(j) {
			case 1:
				return "Amateur Earner!";
			case 2:
				return "Professional Earner!";
			case 3:
				return "Adept Earner!";
			case 4:
				return "Masterful Earner!";
			default:
				return "Amateur Earner!";
			}
		case 6:
			switch (j) {
			case 1:
				return "Amateur Collector I";
			case 2:
				return "Amateur Collector II";
			case 3:
				return "Amateur Collector III";
			default:
				return "Amateur Collector";
			}
		case 7:
			switch (j) {
			case 1:
				return "Professional Collector I";
			case 2:
				return "Professional Collector II";
			case 3:
				return "Professional Collector III";
			default:
				return "Professional Collector";
			}
		case 8:
			switch (j) {
			case 1:
				return "Hardcore Collector I";
			case 2:
				return "Hardcore Collector II";
			case 3:
				return "Hardcore Collector III";
			default:
				return "Hardcore Collector";
			}
		case 9:
			switch (j) {
			case 1:
				return "Veteran Collector I";
			case 2:
				return "Veteran Collector II";
			case 3:
				return "Veteran Collector III";
			default:
				return "Veteran Collector";
			}
		case 10:
			switch(j) {
			case 1:
				return "Medic!";
			case 2:
				return "Graveyard";
			case 3:
				return "Annihilation";
			case 4:
				return "Genocide";
			default:
				return "Medic!";
			}
		case 11:
			switch(j) {
			case 1:
				return "Fun!";
			case 2:
				return "Obsessed!";
			case 3:
				return "Addicted!";
			default:
				return "Fun!";
			}
		case 12:
			switch (j) {
			case 1:
				return "Om Nom Powerups";
			case 2:
				return "Keep them Comin";
			case 3:
				return "Too Much Powerups!";
			default:
				return "Om Nom Powerups";
			}
		case 13:
			switch (j) {
			case 1:
				return "Enhancer I";
			case 2:
				return "Enhancer II";
			case 3:
				return "Enhancer III";
			case 4:
				return "Enhancer IV";
			default:
				return "Enhancer";
			}
		case 14:
			switch (j) {
			case 1:
				return "Skin Collector I";
			case 2:
				return "Skin Collector II";
			case 3:
				return "Skin Collector III";
			default: 
				return "Skin Collector";
			}
		case 15:
			switch (j) {
			case 1:
				return "Big Bank!";
			case 2:
				return "Rich!";
			case 3:
				return "Hoarder!";
			default:
				return "Big Bank!";
			}
		case 16:
			return "The Greed Begins...";
		case 17:
			return "I Can't See the Ball!";
		case 18:
			return "What...";
		case 19:
			return "Slow and Steady";
		case 20:
			return "Multicultural";
		case 21:
			return "Power Me Up!";
		case 22:
			return "Lucky Me!";
		case 23:
			return "Speedster";
		case 24:
			return "Lottery Winner!";
		case 25:
			return "Customizer!";
		case 26:
			return "What a Waste";
		case 27:
			return "Starcraft Fan";
		case 28:
			return "Big Spender!";
		case 29:
			return "King!";
		case 30:
			return "Ninja!";
		case 31:
			return "BOOM!";
		case 32:
			return "Can't Touch This!";
		case 33:
			return "Nothing Will Slow me Down!";
		case 34:
			return "Unlucky!";
		case 35:
			return "That Was Quick";
		case 36:
			return "Diversity";
		case 37:
			return "Camouflage!";
		case 38:
			return "Sniped!";
		case 39:
			return "Maxed Out!";
		case 40:
			return "Gotta Catch 'Em All!";
		case 41:
			return "Precision is Key";
		case 42:
			return "Terrible Sound Effects!";
		case 43:
			return "Pro Dodger!";
		case 44:
			return "Stupid Ads";
		}
		return "";
	}
	
	public int getDeaths() {
		return deaths;
	}

	public int getDeaths(int i) {
		return death[i];
	}

	public int getSuperCoinValue() {
		return superCoinValue;
	}

	public int getTotalPowerups() {
		return totalPowerups;
	}

	public double getAverageScore(int i) {
		return averageScore[i];
	}

	public double getAverageCoins(int i) {
		return averageCoins[i];
	}

	public int getAchievements(int i) {
		return achievements[i];
	}
	
	public void setPowerupChance(int powerupChance) {
		this.powerupChance = powerupChance;
	}

	public void setBombRadius(int bombRadius) {
		this.bombRadius = bombRadius;
	}

	public void setPickupRadius(int pickupRadius) {
		this.pickupRadius = pickupRadius;
	}

	public void setLifetimeCoins(int lifetimeCoins) {
		this.lifetimeCoins = lifetimeCoins;
	}

	public void setTotalCoins(int totalCoins) {
		this.totalCoins = totalCoins;
	}

	public void setPlayTime(int playTime) {
		this.playTime = playTime;
	}

	public void setBonusMultiplier(int bonusMultiplier) {
		this.bonusMultiplier = bonusMultiplier;
	}

	public void setHighscores(int h, int i) {
		highscores[i] = h;
	}

	public void setMostCoins(int m, int i) {
		mostCoins[i] = m;
	}

	public void setPowerupDurations(int duration, int i) {
		this.powerupDurations[i] = duration;
	}

	public void setCoinChances(double chance, int i) {
		this.coinChances[i] = chance;
	}

	public void setCoinCount(int c, int i) {
		coinCounts[i] = c;
	}

	public void setDecreasedChance(int c) {
		decreasedChance = c;
	}

	public void setUnlocked(boolean u, int i) {
		unlockedSkins[i] = u;
	}

	public void setDeaths(int d) {
		deaths = d;
	}

	public void setDeaths(int d, int i) {
		death[i] = d;
	}

	public void setSuperCoinValue(int v) {
		superCoinValue = v;
	}

	public void setSkins(Image s, int i) {
		skins[i] = s;
	}

	public void setTotalPowerups(int p) {
		totalPowerups = p;
	}

	public void setMuted(boolean m) {
		muted = m;
	}

	public void setAverageScore(double a, int i) {
		averageScore[i] = a;
	}

	public void setAverageCoins(double a, int i) {
		averageCoins[i] = a;
	}
	
	public void setAchievements(int a, int i) {
		achievements[i] = a;
	}
	
	public void setMostBalls(int m, int i) {
		mostBalls[i] = m;
	}
	
	public void setUpgrades(int u) {
		upgrades = u;
	}
	
	public void setBuySkins(int s) {
		buySkins = s;
	}
	
	public void load() {
		SharedPreferences saves = PreferenceManager
				.getDefaultSharedPreferences(this);
		setSensitivity(saves.getFloat("Sensitivity", 1));
		for (int i = 0; i < 4; i++) {
			setHighscores(saves.getInt("score" + i, 0), i);
			setMostCoins(saves.getInt("Most Coins" + i, 0), i);
			setAverageScore(saves.getFloat("Average Score" + i, 0.0f), i);
			setAverageCoins(saves.getFloat("Average Coins" + i, 0.0f), i);
			setMostBalls(saves.getInt("Most Balls" + i, 0), i);
			setDeaths(saves.getInt("Deaths" + i, 0), i);
		}
		setTotalCoins(saves.getInt("Total Coins", 0));
		setLifetimeCoins(saves.getInt("Lifetime Coins", 0));
		setTotalPowerups(saves.getInt("Total Powerups", 0));
		setSkin(saves.getInt("Skin", 13));
		setPowerupChance(saves.getInt("Powerup Chance", 15));
		setBombRadius(saves.getInt("Bomb Radius", 50));
		setPickupRadius(saves.getInt("Pickup Radius", 10));
		setBonusMultiplier(saves.getInt("Bonus Multiplier", 0));
		setDecreasedChance(saves.getInt("Decreased Chance", 30));
		setDeaths(saves.getInt("Deaths", 0));
		setPlayTime(saves.getInt("Playtime", 0));
		for (int i = 0; i < 5; i++) {
			if (i != 3)
				setPowerupDurations(saves.getInt("Powerup Duration" + i, 5000),
						i);
			else
				setPowerupDurations(saves.getInt("Powerup Duration" + i, 1500),
						i);
			setCoinCount(saves.getInt("Coin Count" + i, 0), i);
		}
		for (int i = 5; i < 8; i++)
			setPowerupDurations(saves.getInt("Powerup Duration" + i, 10000), i);
		setSuperCoinValue(saves.getInt("Super Coin Value", 10));
		setCoinChances(saves.getFloat("Coin Chance0", 9.0f / 60), 0);
		setCoinChances(saves.getFloat("Coin Chance1", 1.0f / 30), 1);
		setCoinChances(saves.getFloat("Coin Chance2", 1.0f / 50), 2);
		setCoinChances(saves.getFloat("Coin Chance3", 1.0f / 70), 3);
		for (int i = 0; i < 26; i++)
			if (i != 13)
				setUnlocked(saves.getBoolean("Skin" + i, false), i);
		setMuted(saves.getBoolean("Mute", false));
		
		for (int i = 0; i < 45; i++)
			setAchievements(saves.getInt("Achievements" + i, 0), i);
		setUpgrades(saves.getInt("Upgrades", 0));
		setBuySkins(saves.getInt("Buy Skins", 0));
	}
}