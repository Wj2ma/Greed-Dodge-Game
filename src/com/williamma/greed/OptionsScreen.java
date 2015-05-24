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

public class OptionsScreen extends Screen {

	private Typeface titleFont, boldFont;
	private Paint paint, paint2;
	private boolean sensSelect = false, muted;
	private Image[] skins = new Image[26];
	private boolean[] unlocked = new boolean[26], pressed = new boolean[27];
	private int sensX, selectX, selectY, dRotation = 0, skinNum;

	private SharedPreferences saves;
	private SharedPreferences.Editor editor;

	private ArrayList<Integer> achievePopup = new ArrayList<Integer>();
	private int bannerY = 800;
	private long achieveTime;
	private boolean starter = false, changed = false;

	public OptionsScreen(Game game, Typeface font) {
		super(game);
		titleFont = font;
		boldFont = Typeface.create((String) null, Typeface.BOLD);
		paint = new Paint();
		paint.setTypeface(titleFont);
		paint2 = new Paint();
		paint2.setTypeface(boldFont);
		sensX = (int) ((((SampleGame) game).getSensitivity() - 1) * 100) + 40;
		saves = PreferenceManager
				.getDefaultSharedPreferences(((SampleGame) game));
		editor = saves.edit();

		for (int i = 0; i < 26; i++)
			unlocked[i] = ((SampleGame) game).isUnlocked(i);
		muted = ((SampleGame) game).isMuted();

		skins[13] = Assets.greenBall.resize(60, 60);
		skins[18] = Assets.yellowBall.resize(60, 60);
		skins[17] = Assets.blueBall.resize(60, 60);
		skins[16] = Assets.cyanBall.resize(60, 60);
		skins[15] = Assets.magentaBall.resize(60, 60);
		skins[14] = Assets.blackBall.resize(60, 60);
		skins[8] = Assets.happyBall.resize(60, 60);
		skins[11] = Assets.skullBall.resize(60, 60);
		skins[6] = Assets.loveBall.resize(60, 60);
		skins[19] = Assets.cookieBall.resize(60, 60);
		skins[12] = Assets.sadBall.resize(60, 60);
		skins[9] = Assets.superHappyBall.resize(60, 60);
		skins[4] = Assets.fireBall.resize(60, 60);
		skins[24] = Assets.pokeBall.resize(60, 60);
		skins[0] = Assets.electricBall.resize(60, 60);
		skins[10] = Assets.ninjaBall.resize(60, 60);
		skins[5] = Assets.slimeBall.resize(60, 60);
		skins[1] = Assets.minionBall.resize(60, 60);
		skins[3] = Assets.batBall.resize(60, 60);
		skins[2] = Assets.superBall.resize(60, 60);
		skins[21] = Assets.marineBall.resize(60, 60);
		skins[22] = Assets.zergBall.resize(60, 60);
		skins[23] = Assets.zealotBall.resize(60, 60);
		skins[20] = Assets.richBall.resize(60, 60);
		skins[7] = Assets.moneyBall.resize(60, 60);
		skins[25] = Assets.diamondBall.resize(60, 60);

		skinNum = ((SampleGame) game).getSkinNum();
		if (skinNum == 13) {
			selectX = 0;
			selectY = 300;
		} else if (skinNum == 18) {
			selectX = 80;
			selectY = 300;
		} else if (skinNum == 17) {
			selectX = 160;
			selectY = 300;
		} else if (skinNum == 16) {
			selectX = 240;
			selectY = 300;
		} else if (skinNum == 15) {
			selectX = 320;
			selectY = 300;
		} else if (skinNum == 14) {
			selectX = 400;
			selectY = 300;
		} else if (skinNum == 8) {
			selectX = 0;
			selectY = 380;
		} else if (skinNum == 11) {
			selectX = 80;
			selectY = 380;
		} else if (skinNum == 6) {
			selectX = 160;
			selectY = 380;
		} else if (skinNum == 19) {
			selectX = 240;
			selectY = 380;
		} else if (skinNum == 12) {
			selectX = 320;
			selectY = 380;
		} else if (skinNum == 9) {
			selectX = 400;
			selectY = 380;
		} else if (skinNum == 4) {
			selectX = 0;
			selectY = 460;
		} else if (skinNum == 24) {
			selectX = 80;
			selectY = 460;
		} else if (skinNum == 0) {
			selectX = 160;
			selectY = 460;
		} else if (skinNum == 10) {
			selectX = 240;
			selectY = 460;
		} else if (skinNum == 5) {
			selectX = 320;
			selectY = 460;
		} else if (skinNum == 1) {
			selectX = 400;
			selectY = 460;
		} else if (skinNum == 3) {
			selectX = 0;
			selectY = 540;
		} else if (skinNum == 2) {
			selectX = 80;
			selectY = 540;
		} else if (skinNum == 21) {
			selectX = 160;
			selectY = 540;
		} else if (skinNum == 22) {
			selectX = 240;
			selectY = 540;
		} else if (skinNum == 23) {
			selectX = 320;
			selectY = 540;
		} else if (skinNum == 20) {
			selectX = 400;
			selectY = 540;
		} else if (skinNum == 7) {
			selectX = 0;
			selectY = 620;
		} else if (skinNum == 25) {
			selectX = 80;
			selectY = 620;
		}
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for (TouchEvent i : touchEvents) {
			if (i.type == TouchEvent.TOUCH_DOWN) {
				if (inBounds(i, 0, 180, 480, 60))
					sensSelect = true;
				else if (inBounds(i, 0, 300, 80, 80))
					pressed[0] = true;
				else if (inBounds(i, 80, 300, 80, 80))
					pressed[1] = true;
				else if (inBounds(i, 160, 300, 80, 80))
					pressed[2] = true;
				else if (inBounds(i, 240, 300, 80, 80))
					pressed[3] = true;
				else if (inBounds(i, 320, 300, 80, 80))
					pressed[4] = true;
				else if (inBounds(i, 400, 300, 80, 80))
					pressed[5] = true;
				else if (inBounds(i, 0, 380, 80, 80))
					pressed[6] = true;
				else if (inBounds(i, 80, 380, 80, 80))
					pressed[7] = true;
				else if (inBounds(i, 160, 380, 80, 80))
					pressed[8] = true;
				else if (inBounds(i, 240, 380, 80, 80))
					pressed[9] = true;
				else if (inBounds(i, 320, 380, 80, 80))
					pressed[10] = true;
				else if (inBounds(i, 400, 380, 80, 80))
					pressed[11] = true;
				else if (inBounds(i, 0, 460, 80, 80))
					pressed[12] = true;
				else if (inBounds(i, 80, 460, 80, 80))
					pressed[13] = true;
				else if (inBounds(i, 160, 460, 80, 80))
					pressed[14] = true;
				else if (inBounds(i, 240, 460, 80, 80))
					pressed[15] = true;
				else if (inBounds(i, 320, 460, 80, 80))
					pressed[16] = true;
				else if (inBounds(i, 400, 460, 80, 80))
					pressed[17] = true;
				else if (inBounds(i, 0, 540, 80, 80))
					pressed[18] = true;
				else if (inBounds(i, 80, 540, 80, 80))
					pressed[19] = true;
				else if (inBounds(i, 160, 540, 80, 80))
					pressed[20] = true;
				else if (inBounds(i, 240, 540, 80, 80))
					pressed[21] = true;
				else if (inBounds(i, 320, 540, 80, 80))
					pressed[22] = true;
				else if (inBounds(i, 400, 540, 80, 80))
					pressed[23] = true;
				else if (inBounds(i, 0, 620, 80, 80))
					pressed[24] = true;
				else if (inBounds(i, 80, 620, 80, 80))
					pressed[25] = true;
				else if (inBounds(i, 250, 740, 20, 20))
					pressed[26] = true;
			} else if (i.type == TouchEvent.TOUCH_DRAGGED) {
				if (sensSelect) {
					sensX = (int) (Math.round((i.x - 40) / 10.0) * 10) + 40;
					if (sensX < 40)
						sensX = 40;
					else if (sensX > 440)
						sensX = 440;
					
					if (((SampleGame) game).getAchievements(41) == 0) {
						((SampleGame) game).setAchievements(1,41);
						editor.putInt("Achievements41", 1);
						editor.commit();
						achievePopup.add(41);
					}
				}
			} else if (i.type == TouchEvent.TOUCH_UP) {
				if (sensSelect) {
					sensSelect = false;
					((SampleGame) game)
							.setSensitivity(((sensX - 40) / 100.0 + 1));
					editor.putFloat("Sensitivity",
							(float) ((SampleGame) game).getSensitivity());
					editor.commit();
				} else if (pressed[0] && inBounds(i, 0, 300, 80, 80)) {
					if (unlocked[13] && skinNum != 13) {
						selectX = 0;
						selectY = 300;
						((SampleGame) game).setSkin(13);
						editor.putInt("Skin", 13);
						editor.commit();
						changed = true;
					}
				} else if (pressed[1] && inBounds(i, 80, 300, 80, 80)) {
					if (unlocked[18] && skinNum != 18) {
						selectX = 80;
						selectY = 300;
						((SampleGame) game).setSkin(18);
						editor.putInt("Skin", 18);
						editor.commit();
						changed = true;
					}
				} else if (pressed[2] && inBounds(i, 160, 300, 80, 80)) {
					if (unlocked[17] && skinNum != 17) {
						selectX = 160;
						selectY = 300;
						((SampleGame) game).setSkin(17);
						editor.putInt("Skin", 17);
						editor.commit();
						changed = true;
					}
				} else if (pressed[3] && inBounds(i, 240, 300, 80, 80)) {
					if (unlocked[16] && skinNum != 16) {
						selectX = 240;
						selectY = 300;
						((SampleGame) game).setSkin(16);
						editor.putInt("Skin", 16);
						editor.commit();
						changed = true;
					}
				} else if (pressed[4] && inBounds(i, 320, 300, 80, 80)) {
					if (unlocked[15] && skinNum != 15) {
						selectX = 320;
						selectY = 300;
						((SampleGame) game).setSkin(15);
						editor.putInt("Skin", 15);
						editor.commit();
						changed = true;
					}
				} else if (pressed[5] && inBounds(i, 400, 300, 80, 80)) {
					if (unlocked[14] && skinNum != 14) {
						selectX = 400;
						selectY = 300;
						((SampleGame) game).setSkin(14);
						editor.putInt("Skin", 14);
						editor.commit();
						changed = true;
					}
				} else if (pressed[6] && inBounds(i, 0, 380, 80, 80)) {
					if (unlocked[8] && skinNum != 8) {
						selectX = 0;
						selectY = 380;
						((SampleGame) game).setSkin(8);
						editor.putInt("Skin", 8);
						editor.commit();
						changed = true;
					}
				} else if (pressed[7] && inBounds(i, 80, 380, 80, 80)) {
					if (unlocked[11] && skinNum != 11) {
						selectX = 80;
						selectY = 380;
						((SampleGame) game).setSkin(11);
						editor.putInt("Skin", 11);
						editor.commit();
						changed = true;
					}
				} else if (pressed[8] && inBounds(i, 160, 380, 80, 80)) {
					if (unlocked[6] && skinNum != 6) {
						selectX = 160;
						selectY = 380;
						((SampleGame) game).setSkin(6);
						editor.putInt("Skin", 6);
						editor.commit();
						changed = true;
					}
				} else if (pressed[9] && inBounds(i, 240, 380, 80, 80)) {
					if (unlocked[19] && skinNum != 19) {
						selectX = 240;
						selectY = 380;
						((SampleGame) game).setSkin(19);
						editor.putInt("Skin", 19);
						editor.commit();
						changed = true;
					}
				} else if (pressed[10] && inBounds(i, 320, 380, 80, 80)) {
					if (unlocked[12] && skinNum != 12) {
						selectX = 320;
						selectY = 380;
						((SampleGame) game).setSkin(12);
						editor.putInt("Skin", 12);
						editor.commit();
						changed = true;
					}
				} else if (pressed[11] && inBounds(i, 400, 380, 80, 80)) {
					if (unlocked[9] && skinNum != 9) {
						selectX = 400;
						selectY = 380;
						((SampleGame) game).setSkin(9);
						editor.putInt("Skin", 9);
						editor.commit();
						changed = true;
					}
				} else if (pressed[12] && inBounds(i, 0, 460, 80, 80)) {
					if (unlocked[4] && skinNum != 4) {
						selectX = 0;
						selectY = 460;
						((SampleGame) game).setSkin(4);
						editor.putInt("Skin", 4);
						editor.commit();
						changed = true;
					}
				} else if (pressed[13] && inBounds(i, 80, 460, 80, 80)) {
					if (unlocked[24] && skinNum != 24) {
						selectX = 80;
						selectY = 460;
						((SampleGame) game).setSkin(24);
						editor.putInt("Skin", 24);
						editor.commit();
						changed = true;
					}
				} else if (pressed[14] && inBounds(i, 160, 460, 80, 80)) {
					if (unlocked[0] && skinNum != 0) {
						selectX = 160;
						selectY = 460;
						((SampleGame) game).setSkin(0);
						editor.putInt("Skin", 0);
						editor.commit();
						changed = true;
					}
				} else if (pressed[15] && inBounds(i, 240, 460, 80, 80)) {
					if (unlocked[10] && skinNum != 10) {
						selectX = 240;
						selectY = 460;
						((SampleGame) game).setSkin(10);
						editor.putInt("Skin", 10);
						editor.commit();
						changed = true;
					}
				} else if (pressed[16] && inBounds(i, 320, 460, 80, 80)) {
					if (unlocked[5] && skinNum != 5) {
						selectX = 320;
						selectY = 460;
						((SampleGame) game).setSkin(5);
						editor.putInt("Skin", 5);
						editor.commit();
						changed = true;
					}
				} else if (pressed[17] && inBounds(i, 400, 460, 80, 80)) {
					if (unlocked[1] && skinNum != 1) {
						selectX = 400;
						selectY = 460;
						((SampleGame) game).setSkin(1);
						editor.putInt("Skin", 1);
						editor.commit();
						changed = true;
					}
				} else if (pressed[18] && inBounds(i, 0, 540, 80, 80)) {
					if (unlocked[3] && skinNum != 3) {
						selectX = 0;
						selectY = 540;
						((SampleGame) game).setSkin(3);
						editor.putInt("Skin", 3);
						editor.commit();
						changed = true;
					}
				} else if (pressed[19] && inBounds(i, 80, 540, 80, 80)) {
					if (unlocked[2] && skinNum != 2) {
						selectX = 80;
						selectY = 540;
						((SampleGame) game).setSkin(2);
						editor.putInt("Skin", 2);
						editor.commit();
						changed = true;
					}
				} else if (pressed[20] && inBounds(i, 160, 540, 80, 80)) {
					if (unlocked[21] && skinNum != 21) {
						selectX = 160;
						selectY = 540;
						((SampleGame) game).setSkin(21);
						editor.putInt("Skin", 21);
						editor.commit();
						changed = true;
					}
				} else if (pressed[21] && inBounds(i, 240, 540, 80, 80)) {
					if (unlocked[22] && skinNum != 22) {
						selectX = 240;
						selectY = 540;
						((SampleGame) game).setSkin(22);
						editor.putInt("Skin", 22);
						editor.commit();
						changed = true;
					}
				} else if (pressed[22] && inBounds(i, 320, 540, 80, 80)) {
					if (unlocked[23] && skinNum != 23) {
						selectX = 320;
						selectY = 540;
						((SampleGame) game).setSkin(23);
						editor.putInt("Skin", 23);
						editor.commit();
						changed = true;
					}
				} else if (pressed[23] && inBounds(i, 400, 540, 80, 80)) {
					if (unlocked[20] && skinNum != 20) {
						selectX = 400;
						selectY = 540;
						((SampleGame) game).setSkin(20);
						editor.putInt("Skin", 20);
						editor.commit();
						changed = true;
					}
				} else if (pressed[24] && inBounds(i, 0, 620, 80, 80)) {
					if (unlocked[7] && skinNum != 7) {
						selectX = 0;
						selectY = 620;
						((SampleGame) game).setSkin(7);
						editor.putInt("Skin", 7);
						editor.commit();
						changed = true;
					}
				} else if (pressed[25] && inBounds(i, 80, 620, 80, 80)) {
					if (unlocked[25] && skinNum != 25) {
						selectX = 80;
						selectY = 620;
						((SampleGame) game).setSkin(25);
						editor.putInt("Skin", 25);
						editor.commit();
						changed = true;
					}
				} else if (pressed[26] && inBounds(i, 250, 740, 20, 20)) {
					if (muted)
						muted = false;
					else {
						muted = true;
						if (((SampleGame) game).getAchievements(42) == 0) {
							((SampleGame) game).setAchievements(1,42);
							editor.putInt("Achievements42", 1);
							editor.commit();
							achievePopup.add(42);
						}
					}
					((SampleGame) game).setMuted(muted);
					editor.putBoolean("Mute", muted);
					editor.commit();
				} else {
					for (int j = 0; j < 27; j++)
						pressed[j] = false;
				}
			}
		}
		dRotation++;
		if (dRotation == 361)
			dRotation = 1;

		if (((SampleGame) game).getAchievements(44) == 0
				&& ((SampleGame) game).isOpened()) {
			((SampleGame) game).setAchievements(1, 44);
			editor.putInt("Achievements44", 1);
			editor.commit();
			achievePopup.add(44);
		}

		if (((SampleGame) game).getAchievements(25) == 0 && changed) {
			((SampleGame) game).setAchievements(1, 25);
			editor.putInt("Achievements25", 1);
			editor.commit();
			achievePopup.add(25);
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
		paint.setTextSize(100f);
		paint.setColor(Color.BLACK);
		g.drawString("Options", 75, 90, paint);
		g.drawRect(40, 200, 400, 20, Color.LTGRAY);
		for (int i = 40; i <= 440; i += 100)
			g.drawLine(i, 195, i, 225, Color.DKGRAY, 3);
		for (int i = 90; i < 440; i += 50)
			g.drawLine(i, 200, i, 220, Color.DKGRAY, 2);
		g.drawLine(sensX, 190, sensX, 230, Color.BLACK, 10);
		paint2.setColor(Color.BLACK);
		paint2.setTextSize(35f);
		paint2.setTextAlign(Align.LEFT);
		g.drawString("Sensitivity: " + ((sensX - 40) / 100.0 + 1), 10, 160,
				paint2);
		g.drawString("Choose a Skin:", 10, 275, paint2);

		int color = Color.argb(255, 139, 69, 19);
		for (int i = 300; i <= 700; i += 80)
			g.drawLine(0, i, 480, i, color, 5);
		for (int i = 80; i < 480; i += 80)
			g.drawLine(i, 300, i, 700, color, 5);
		g.drawLine(2, 300, 2, 700, color, 5);
		g.drawLine(478, 300, 478, 700, color, 5);
		if (selectX == 0)
			g.drawHollowRect(2, selectY, 78, 80, Color.GREEN, 5);
		else if (selectX == 400)
			g.drawHollowRect(400, selectY, 78, 80, Color.GREEN, 5);
		else
			g.drawHollowRect(selectX, selectY, 80, 80, Color.GREEN, 5);

		// draw all images in the spots
		g.drawImage(skins[13], 10, 310);
		if (unlocked[18])
			g.drawImage(skins[18], 90, 310);
		else
			g.drawCircle(120, 340, 30, Color.DKGRAY);
		if (unlocked[17])
			g.drawImage(skins[17], 170, 310);
		else
			g.drawCircle(200, 340, 30, Color.DKGRAY);
		if (unlocked[16])
			g.drawImage(skins[16], 250, 310);
		else
			g.drawCircle(280, 340, 30, Color.DKGRAY);
		if (unlocked[15])
			g.drawImage(skins[15], 330, 310);
		else
			g.drawCircle(360, 340, 30, Color.DKGRAY);
		if (unlocked[14])
			g.drawImage(skins[14], 410, 310);
		else
			g.drawCircle(440, 340, 30, Color.DKGRAY);
		if (unlocked[8])
			g.drawImage(skins[8], 10, 390);
		else
			g.drawCircle(40, 420, 30, Color.DKGRAY);
		if (unlocked[11])
			g.drawImage(skins[11], 90, 390);
		else
			g.drawCircle(120, 420, 30, Color.DKGRAY);
		if (unlocked[6])
			g.drawImage(skins[6], 170, 390);
		else
			g.drawCircle(200, 420, 30, Color.DKGRAY);
		if (unlocked[19])
			g.drawImage(skins[19], 250, 390);
		else
			g.drawCircle(280, 420, 30, Color.DKGRAY);
		if (unlocked[12])
			g.drawImage(skins[12], 330, 390);
		else
			g.drawCircle(360, 420, 30, Color.DKGRAY);
		if (unlocked[9])
			g.drawImage(skins[9], 410, 390);
		else
			g.drawCircle(440, 420, 30, Color.DKGRAY);
		if (unlocked[4])
			g.drawImage(skins[4], 10, 470);
		else
			g.drawCircle(40, 500, 30, Color.DKGRAY);
		if (unlocked[24])
			g.drawImage(skins[24], 90, 470);
		else
			g.drawCircle(120, 500, 30, Color.DKGRAY);
		if (unlocked[0])
			g.drawImage(skins[0], 170, 470);
		else
			g.drawCircle(200, 500, 30, Color.DKGRAY);
		if (unlocked[10])
			g.drawImage(skins[10], 250, 470);
		else
			g.drawCircle(280, 500, 30, Color.DKGRAY);
		if (unlocked[5])
			g.drawImage(skins[5], 330, 470);
		else
			g.drawCircle(360, 500, 30, Color.DKGRAY);
		if (unlocked[1])
			g.drawImage(skins[1], 410, 470);
		else
			g.drawCircle(440, 500, 30, Color.DKGRAY);
		if (unlocked[3])
			g.drawImage(skins[3], 10, 550);
		else
			g.drawCircle(40, 580, 30, Color.DKGRAY);
		if (unlocked[2])
			g.drawImage(skins[2], 90, 550);
		else
			g.drawCircle(120, 580, 30, Color.DKGRAY);
		if (unlocked[21])
			g.drawImage(skins[21], 170, 550);
		else
			g.drawCircle(200, 580, 30, Color.DKGRAY);
		if (unlocked[22])
			g.drawImage(skins[22], 250, 550);
		else
			g.drawCircle(280, 580, 30, Color.DKGRAY);
		if (unlocked[23])
			g.drawImage(skins[23], 330, 550);
		else
			g.drawCircle(360, 580, 30, Color.DKGRAY);
		if (unlocked[20])
			g.drawImage(skins[20], 410, 550);
		else
			g.drawCircle(440, 580, 30, Color.DKGRAY);
		if (unlocked[7])
			g.drawImage(skins[7], 10, 630);
		else
			g.drawCircle(40, 660, 30, Color.DKGRAY);
		if (unlocked[25]) {
			g.save();
			g.rotate(dRotation, 120, 660);
			g.drawImage(skins[25], 90, 630);
			g.restore();
		} else
			g.drawCircle(120, 660, 30, Color.DKGRAY);

		g.drawString("Mute Sound:", 20, 760, paint2);
		if (muted) {
			g.drawLine(250, 740, 270, 760, Color.RED, 3);
			g.drawLine(250, 760, 270, 740, Color.RED, 3);
		}
		g.drawHollowRect(250, 740, 20, 20, Color.BLACK, 3);

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