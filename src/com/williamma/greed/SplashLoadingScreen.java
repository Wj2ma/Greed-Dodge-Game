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

import com.williamma.framework.Graphics;
import com.williamma.framework.Input.TouchEvent;
import com.williamma.framework.Screen;

public class SplashLoadingScreen extends Screen {

	private Paint paint = new Paint();
	private int x = -120, y = 400, part = 0, inc = 0, fade = 0;
	private Animation anim;
	private Typeface titleFont, logoFont;

	private Paint paint2;
	private Typeface boldFont;
	
	private ArrayList<Integer> achievePopup = new ArrayList<Integer>();
	private int bannerY = 800;
	private long achieveTime;
	boolean starter = false;
	
	public SplashLoadingScreen(SampleGame game) {
		super(game);
		anim = new Animation();
		anim.addFrame(Assets.genesis1, 150);
		anim.addFrame(Assets.genesis2, 150);
		anim.addFrame(Assets.genesis3, 150);
		anim.addFrame(Assets.genesis4, 150);
		anim.addFrame(Assets.genesis5, 150);
		anim.addFrame(Assets.genesis6, 150);
		anim.addFrame(Assets.genesis7, 150);
		anim.addFrame(Assets.genesis6, 150);
		anim.addFrame(Assets.genesis5, 150);
		anim.addFrame(Assets.genesis4, 150);
		anim.addFrame(Assets.genesis3, 150);
		anim.addFrame(Assets.genesis2, 150);
		logoFont = Typeface.createFromAsset(game.getAssets(),
				"fonts/showcardGothic.ttf");
		titleFont = Typeface.createFromAsset(game.getAssets(),
				"fonts/titleFont.ttf");
		boldFont = Typeface.create((String) null, Typeface.BOLD);
		paint2 = new Paint();
		paint2.setTypeface(boldFont);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		for (TouchEvent e : touchEvents)
			if (e.type == TouchEvent.TOUCH_UP) {
				game.setScreen(new LoadingScreen((SampleGame)game, titleFont));
				break;
			}

		if (part == 0) {
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			part++;
		}

		else if (part == 1) {
			if (x < 600)
				x += 30;
			else {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				part++;
			}
		}

		else if (part == 2) {
			if (x > -120)
				x -= 30;
			else {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				part++;
				x = 240;
				y = -120;
			}
		}

		else if (part == 3) {
			if (y < 400) {
				y += 20;
				if (y <= 100)
					inc = 5;
				else if (y <= 150)
					inc = 10;
				else if (y <= 200)
					inc = 15;
				else if (y <= 250)
					inc = 20;
				else if (y <= 300)
					inc = 25;
				else
					inc = 30;

				try {
					Thread.sleep(inc);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				part++;
				inc = 0;
			}
		} else {
			anim.update(15);
			if (fade < 254)
				fade += 2;
			else {
				fade = 255;
				inc++;
				if (inc == 100)
					game.setScreen(new LoadingScreen((SampleGame)game, titleFont));
			}
		}
		
		if (((SampleGame) game).getAchievements(44) == 0 && ((SampleGame) game).isOpened()) {
			((SampleGame) game).setAchievements(1,44);
			SharedPreferences saves = PreferenceManager.getDefaultSharedPreferences((SampleGame)game);
			SharedPreferences.Editor editor = saves.edit();
			editor.putInt("Achievements44", 1);
			editor.commit();
			achievePopup.add(44);
		}
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawARGB(255, 0, 0, 0);
		g.drawImage(anim.getImage(), x - 120, y - 120);

		if (part == 4) {
			paint.setColor(Color.argb(fade, 255, 255, 255));
			paint.setTypeface(logoFont);
			paint.setTextSize(50f);
			g.drawString("Genesis", 137, 600, paint);
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

	}
}