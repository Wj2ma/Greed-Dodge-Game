package com.williamma.greed;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.preference.PreferenceManager;

import com.williamma.framework.Game;
import com.williamma.framework.Graphics;
import com.williamma.framework.Input.TouchEvent;
import com.williamma.framework.Screen;

public class InstructionsScreen extends Screen {

	private Typeface titleFont, boldFont;
	private Paint paint, paint2;
	private boolean pressed;

	private ArrayList<Integer> achievePopup = new ArrayList<Integer>();
	private int bannerY = 800;
	private long achieveTime;
	boolean starter = false;
	
	public InstructionsScreen(Game game, Typeface font) {
		super(game);
		titleFont = font;
		boldFont = Typeface.create((String) null, Typeface.BOLD);
		paint = new Paint();
		paint.setTypeface(titleFont);
		paint2 = new Paint();
		paint2.setTypeface(boldFont);
	}

	@Override
	public void update(float deltaTime) {
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
		g.drawARGB(255, 255, 255, 255);

		paint2.setColor(Color.BLACK);
		paint2.setTextSize(30f);
		paint2.setTextAlign(Align.LEFT);
		g.drawString("You just invaded Captain Spark's", 5, 25, paint2);
		g.drawString("treasure place and the only thing", 5, 75, paint2);
		g.drawString("stopping you from grabbing all", 5, 125, paint2);
		g.drawString("his treasure is his automated", 5, 175, paint2);
		g.drawString("cannon! Everytime you steal gold", 5, 225, paint2);
		g.drawString("from the treasure pile, the cannon", 5, 275, paint2);
		g.drawString("fires a cannonball. Avoid all", 5, 325, paint2);
		g.drawString("cannonballs and collect as much", 5, 375, paint2);
		g.drawString("treasure as you can. Good luck!", 5, 425, paint2);
		g.drawImage(Assets.slowBall, 24, 504);
		g.drawImage(Assets.fastBall, 120,504);
		g.drawImage(Assets.shrinkBall, 216,504);
		g.drawImage(Assets.growBall, 312,504);
		g.drawImage(Assets.bomb, 408,504);	
		g.drawImage(Assets.shield,24,652);
		g.drawImage(Assets.silence,120,652);
		g.drawImage(Assets.shrink,216,652);
		g.drawImage(Assets.grow,312,652);
		g.drawImage(Assets.superCoin,408,652);
		paint2.setTextSize(18f);
		g.drawString("Slow Ball:   Fast Ball:   Small Ball:    Big Ball:       Bomb:",15,580,paint2);
		g.drawString("Shield:        Silence:         Shrink:          Grow:     Super Coin:",15,728,paint2);
		paint2.setTextSize(16f);
		g.drawString("Slows         Speeds Up       Shrinks           Grows         Destroys", 24, 610, paint2);
		g.drawString("Bullets          Bullets            Bullets           Bullets         Bullets", 24, 630, paint2);
		g.drawString("Shields         Gun will          Shrinks           Grows        Gives More", 20, 758, paint2);
		g.drawString("  You            Hold Fire           You                 You                Score", 24, 778, paint2);
		paint2.setTextSize(40f);
		paint2.setColor(Color.BLUE);
		g.drawString("Powerups:", 5, 475, paint2);
		
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
