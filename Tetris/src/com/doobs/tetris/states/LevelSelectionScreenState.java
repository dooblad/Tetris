package com.doobs.tetris.states;

import res.bitmaps.Bitmaps;
import res.sound.Sounds;

import com.doobs.java2d.gfx.Screen;
import com.doobs.java2d.input.InputHandler;
import com.doobs.tetris.InputUtil;
import com.doobs.tetris.Tetris;

import static com.doobs.tetris.score.HighScores.highScores;
import static com.doobs.tetris.font.FontRenderer.*;

public class LevelSelectionScreenState extends GameState {
	public static final int ID = 3;
	
	private int selectedLevel;
	private int blinkTimer;
	
	public LevelSelectionScreenState(Tetris tetris) {
		super(tetris);
		selectedLevel = 0;
		blinkTimer = 0;
	}

	public void tick(InputHandler input) {
		if(++blinkTimer >= 4) {
			blinkTimer = 0;
		}
		
		if(InputUtil.escapePressed()) {
			tetris.changeState(MusicSelectionScreenState.ID, true);
		}
		
		if(InputUtil.upPressed() && selectedLevel > 4) {
			selectedLevel -= 5;
		} else if(InputUtil.downPressed() && selectedLevel < 5) {
			selectedLevel += 5;
		}
		
		if(InputUtil.leftPressed() && selectedLevel > 0) {
			selectedLevel -= 1;
		} else if(InputUtil.rightPressed() && selectedLevel < 9) {
			selectedLevel += 1;
		}
		
		if(InputUtil.enterPressed()) {
			((PlayingScreenState) tetris.getGameStates()[PlayingScreenState.ID]).getLevel().setLevel(selectedLevel);
			tetris.changeState(PlayingScreenState.ID, true);
			if(tetris.getSelectedSong() < 3)
				Sounds.songs[tetris.getSelectedSong()].loop();
		}
	}
	
	public void render(Screen screen) {
		screen.draw(Bitmaps.levelSelectionScreen, 0, 0);
		
		if(blinkTimer != 0)
			screen.fillRect(0xFFFEAC4E, 53 + 16 * (selectedLevel % 5), 
				76 + 17 * (int) ((double) selectedLevel / 5), 14, 15);
		
		for(int i = 0; i <= 4; i++) {
			drawStringColored(i + "", 0xFFFF0000, 56 + i * 16, 80, screen);
		}
		for(int i = 5; i <= 9; i++) {
			drawStringColored(i + "", 0xFFFF0000, 56 + (i - 5) * 16, 97, screen);
		}
		for(int i = 0; i < 3; i++) {
			int yo = i * 16;
			drawString(highScores[i].getName(), 71, 152 + yo, screen);
			drawFixedDigitNumber(highScores[i].getScore(), 0xFFFFFFFF, 6, 128, 152 + yo, screen);
			drawFixedDigitNumber(highScores[i].getLevel(), 0xFFFFFFFF, 2, 184, 152 + yo, screen);
		}
	}
}
