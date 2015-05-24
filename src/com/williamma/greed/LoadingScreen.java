package com.williamma.greed;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;

import com.williamma.framework.Graphics;
import com.williamma.framework.Image;
import com.williamma.framework.Graphics.ImageFormat;
import com.williamma.framework.implementation.AndroidFastRenderView;
import com.williamma.framework.Screen;

public class LoadingScreen extends Screen {

	private Typeface titleFont;

	public LoadingScreen(SampleGame game, Typeface font) {

		super(game);
		titleFont = font;
		Graphics g = game.getGraphics();
		// animation pics
		Assets.coin1 = g.newImage("coin1.png", ImageFormat.RGB565);
		Assets.coin2 = g.newImage("coin2.png", ImageFormat.RGB565);
		Assets.coin3 = g.newImage("coin3.png", ImageFormat.RGB565);
		Assets.coin4 = g.newImage("coin4.png", ImageFormat.RGB565);
		Assets.coin5 = g.newImage("coin5.png", ImageFormat.RGB565);
		Assets.coin6 = g.newImage("coin6.png", ImageFormat.RGB565);
		Assets.coin7 = g.newImage("coin7.png", ImageFormat.RGB565);
		Assets.coin8 = g.newImage("coin8.png", ImageFormat.RGB565);
		Assets.coin9 = g.newImage("coin9.png", ImageFormat.RGB565);
		Assets.coin10 = g.newImage("coin10.png", ImageFormat.RGB565);
		Assets.coin11 = g.newImage("coin11.png", ImageFormat.RGB565);

		Assets.coin = game.getAudio().createSound("coinSound.wav");
		Assets.explosion = game.getAudio().createSound("bombSound.mp3");
		Assets.death = game.getAudio().createSound("hitSound.mp3");
		Assets.money = game.getAudio().createSound("moneySound.mp3");
		Assets.powerup = game.getAudio().createSound("powerupSound.mp3");
		Assets.powerdown = game.getAudio().createSound("powerdownSound.mp3");

		// skins
		Assets.electricBall = g
				.newImage("electricBall.png", ImageFormat.RGB565);
		Assets.minionBall = g.newImage("minion.png", ImageFormat.RGB565);
		Assets.superBall = g.newImage("superBall.png", ImageFormat.RGB565);
		Assets.batBall = g.newImage("batBall.png", ImageFormat.RGB565);
		Assets.fireBall = g.newImage("fireball.png", ImageFormat.RGB565);
		Assets.slimeBall = g.newImage("slimeBall.png", ImageFormat.RGB565);
		Assets.loveBall = g.newImage("loveBall.png", ImageFormat.RGB565);
		Assets.moneyBall = g.newImage("moneyBall.png", ImageFormat.RGB565);
		Assets.happyBall = g.newImage("happyBall.png", ImageFormat.RGB565);
		Assets.superHappyBall = g.newImage("superHappyBall.png",
				ImageFormat.RGB565);
		Assets.ninjaBall = g.newImage("ninjaBall.png", ImageFormat.RGB565);
		Assets.skullBall = g.newImage("skullBall.png", ImageFormat.RGB565);
		Assets.sadBall = g.newImage("sadBall.png", ImageFormat.RGB565);
		Assets.blueBall = g.newImage("blueBall.png", ImageFormat.RGB565);
		Assets.blackBall = g.newImage("blackBall.png", ImageFormat.RGB565);
		Assets.magentaBall = g.newImage("magentaBall.png", ImageFormat.RGB565);
		Assets.greenBall = g.newImage("greenBall.png", ImageFormat.RGB565);
		Assets.cyanBall = g.newImage("cyanBall.png", ImageFormat.RGB565);
		Assets.yellowBall = g.newImage("yellowBall.png", ImageFormat.RGB565);
		Assets.cookieBall = g.newImage("cookieBall.png", ImageFormat.RGB565);
		Assets.richBall = g.newImage("richBall.png", ImageFormat.RGB565);
		Assets.marineBall = g.newImage("marineBall.png", ImageFormat.RGB565);
		Assets.zergBall = g.newImage("zergBall.png", ImageFormat.RGB565);
		Assets.zealotBall = g.newImage("zealotBall.png", ImageFormat.RGB565);
		Assets.pokeBall = g.newImage("pokeBall.png", ImageFormat.RGB565);
		Assets.diamondBall = g.newImage("diamondBall.png", ImageFormat.RGB565);

		// powerups
		Assets.fastBall = g.newImage("fastball.png", ImageFormat.RGB565);
		Assets.slowBall = g.newImage("slowball.png", ImageFormat.RGB565);
		Assets.shrink = g.newImage("shrink.png", ImageFormat.RGB565);
		Assets.grow = g.newImage("grow.png", ImageFormat.RGB565);
		Assets.growBall = g.newImage("growball.png", ImageFormat.RGB565);
		Assets.shrinkBall = g.newImage("shrinkball.png", ImageFormat.RGB565);
		Assets.shield = g.newImage("shield.png", ImageFormat.RGB565);
		Assets.silence = g.newImage("silence.png", ImageFormat.RGB565);
		Assets.superCoin = g.newImage("superCoin.png", ImageFormat.RGB565);
		Assets.superCoin2 = g.newImage("superCoin2.png", ImageFormat.RGB565);
		Assets.superCoin3 = g.newImage("superCoin3.png", ImageFormat.RGB565);
		Assets.superCoin4 = g.newImage("superCoin4.png", ImageFormat.RGB565);
		Assets.bomb = g.newImage("bomb.png", ImageFormat.RGB565);
		Assets.explode = g.newImage("explosion.png", ImageFormat.RGB565);

		// achievements
		Assets.achievements = new Image[9][5][5];
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++)
				Assets.achievements[0][i][j] = g.newImage(
						"achievements0" + i + j + ".png", ImageFormat.RGB565)
						.resize(80, 80);
		for (int i = 0; i < 4; i++)
			Assets.achievements[1][0][i] = g.newImage(
					"achievements10" + i + ".png", ImageFormat.RGB565).resize(
					80, 80);
		for (int i = 1; i < 5; i++)
			for (int j = 0; j < 3; j++)
				Assets.achievements[1][i][j] = g.newImage(
						"achievements1" + i + j + ".png", ImageFormat.RGB565)
						.resize(80, 80);
		for (int i = 0; i < 4; i++) {
			Assets.achievements[2][0][i] = g.newImage(
					"achievements20" + i + ".png", ImageFormat.RGB565).resize(
					80, 80);
			Assets.achievements[2][3][i] = g.newImage(
					"achievements23" + i + ".png", ImageFormat.RGB565).resize(
					80, 80);
		}
		for (int i = 0; i < 3; i++) {
			Assets.achievements[2][1][i] = g.newImage(
					"achievements21" + i + ".png", ImageFormat.RGB565).resize(
					80, 80);
			Assets.achievements[2][2][i] = g.newImage(
					"achievements22" + i + ".png", ImageFormat.RGB565).resize(
					80, 80);
			Assets.achievements[2][4][i] = g.newImage(
					"achievements24" + i + ".png", ImageFormat.RGB565).resize(
					80, 80);
			Assets.achievements[3][0][i] = g.newImage(
					"achievements30" + i + ".png", ImageFormat.RGB565).resize(
					80, 80);
		}
		for (int i = 1; i < 5; i++)
			Assets.achievements[3][i][0] = g.newImage(
					"achievements3" + i + "0.png", ImageFormat.RGB565).resize(
					80, 80);
		for (int i = 4; i < 9; i++)
			for (int j = 0; j < 5; j++)
				Assets.achievements[i][j][0] = g.newImage(
						"achievements" + i + j + "0.png", ImageFormat.RGB565)
						.resize(80, 80);

		// Set skins
		game.setSkins(Assets.electricBall, 0);
		game.setSkins(Assets.minionBall, 1);
		game.setSkins(Assets.superBall, 2);
		game.setSkins(Assets.batBall, 3);
		game.setSkins(Assets.fireBall, 4);
		game.setSkins(Assets.slimeBall, 5);
		game.setSkins(Assets.loveBall, 6);
		game.setSkins(Assets.moneyBall, 7);
		game.setSkins(Assets.happyBall, 8);
		game.setSkins(Assets.superHappyBall, 9);
		game.setSkins(Assets.ninjaBall, 10);
		game.setSkins(Assets.skullBall, 11);
		game.setSkins(Assets.sadBall, 12);
		game.setSkins(Assets.greenBall, 13);
		game.setSkins(Assets.blackBall, 14);
		game.setSkins(Assets.magentaBall, 15);
		game.setSkins(Assets.cyanBall, 16);
		game.setSkins(Assets.blueBall, 17);
		game.setSkins(Assets.yellowBall, 18);
		game.setSkins(Assets.cookieBall, 19);
		game.setSkins(Assets.richBall, 20);
		game.setSkins(Assets.marineBall, 21);
		game.setSkins(Assets.zergBall, 22);
		game.setSkins(Assets.zealotBall, 23);
		game.setSkins(Assets.pokeBall, 24);
		game.setSkins(Assets.diamondBall, 25);

		// LOAD ALL SAVES
		game.load();

		AndroidFastRenderView renderView = game.getRenderView();
		game.setScaleY((float) 800.0 / renderView.getHeight());
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		game.setScreen(new MainMenuScreen(game, titleFont));
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawARGB(255, 0, 0, 0);
		Paint paint = new Paint();
		paint.setTextSize(100f);
		paint.setColor(Color.WHITE);
		paint.setTypeface(Typeface.create((String) null, Typeface.BOLD));
		g.drawString("Loading", 60, 400, paint);
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

	}
}