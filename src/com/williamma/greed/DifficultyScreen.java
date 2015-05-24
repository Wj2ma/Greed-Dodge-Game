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

public class DifficultyScreen extends Screen {

	private Typeface titleFont, boldFont;
	private Paint paint, paint2;
	private boolean[] pressed = new boolean[4];

	private ArrayList<Integer> achievePopup = new ArrayList<Integer>();
	private int bannerY = 800;
	private long achieveTime;
	boolean starter = false;
	
	public DifficultyScreen(Game game, Typeface font) {
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
		Graphics g = game.getGraphics();
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent e = touchEvents.get(i);
			if (e.type == TouchEvent.TOUCH_DOWN) {
				if (inBounds(e, 85, 270, 310, 75))
					pressed[0] = true;
				else if (inBounds(e, 85, 395, 310, 75))
					pressed[1] = true;
				else if (inBounds(e, 85, 520, 310, 75))
					pressed[2] = true;
				else if (inBounds(e, 85, 645, 310, 75))
					pressed[3] = true;
			} else if (e.type == TouchEvent.TOUCH_UP) {
				if (inBounds(e, 85, 270, 310, 75) && pressed[0]) {
					game.setScreen(new GameScreen(game, titleFont,
							GameScreen.EASY));
				} else if (inBounds(e, 85, 395, 310, 75) && pressed[1]) {
					game.setScreen(new GameScreen(game, titleFont,
							GameScreen.MEDIUM));
				} else if (inBounds(e, 85, 520, 310, 75) && pressed[2]) {
					game.setScreen(new GameScreen(game, titleFont,
							GameScreen.HARD));
				} else if (inBounds(e, 85, 645, 310, 75) && pressed[3]) {
					game.setScreen(new GameScreen(game, titleFont,
							GameScreen.INSANE));
				} else
					for (int j = 0; j < 4; j++)
						pressed[j] = false;
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

		g.drawRect(85, 270, 310, 75, Color.BLACK);
		g.drawRect(85, 395, 310, 75, Color.BLACK);
		g.drawRect(85, 520, 310, 75, Color.BLACK);
		g.drawRect(85, 645, 310, 75, Color.BLACK);
		paint.setTextSize(80f);
		g.drawString("Choose a", 82, 100, paint);
		g.drawString("Difficulty", 78, 200, paint);
		paint2.setTextSize(50f);
		paint2.setColor(Color.WHITE);
		paint2.setTextAlign(Align.LEFT);
		g.drawString("Easy", 187, 325, paint2);
		g.drawString("Medium", 148, 450, paint2);
		g.drawString("Hard", 185, 575, paint2);
		g.drawString("Insane", 167, 700, paint2);
		
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
