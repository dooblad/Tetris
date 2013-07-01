package com.doobs.tetris.states;

import static com.doobs.tetris.font.FontRenderer.drawFixedDigitNumber;
import static com.doobs.tetris.font.FontRenderer.drawString;
import static com.doobs.tetris.score.HighScores.highScores;
import res.bitmaps.Bitmaps;
import res.sound.Sounds;

import com.doobs.java2d.gfx.Screen;
import com.doobs.java2d.input.InputHandler;
import com.doobs.tetris.InputUtil;
import com.doobs.tetris.Tetris;
import com.doobs.tetris.font.FontRenderer;
import com.doobs.tetris.score.HighScore;
import com.doobs.tetris.score.HighScores;

public class HighScoreScreenState extends GameState {
	public static final int ID = 5;
	
	private HighScore score;
	private int ranking;
	
	private boolean nameEntered;
	private int blinkTimer;
	private int[] name;
	private int currentCharacter;
	private int currentIndex;

	public HighScoreScreenState(Tetris tetris) {
		this(tetris, 0);
	}
	
	public HighScoreScreenState(Tetris tetris, int ranking) {
		super(tetris);
		score = new HighScore("", -1, -1);
		this.ranking = ranking;
		nameEntered = false;
		blinkTimer = 0;
		name = new int[6];
		for(int i = 0; i < name.length; i++) {
			name[i] = Bitmaps.fontSheet.length - 1;
		}
		currentCharacter = Bitmaps.fontSheet.length - 1;
		currentIndex = 0;
	}

	public void tick(InputHandler input) {
		if(InputUtil.enterPressed() && nameEntered) {
			tetris.changeState(LevelSelectionScreenState.ID, true);
			String temp = "";
			for(int i = 0; i < name.length; i++) {
				if(name[i] == 36) temp += " ";
				else temp += FontRenderer.parseIndexToChar(name[i]);
			}
			score.setName(temp);
			HighScores.insertHighScore(score, ranking);
			HighScores.overwriteHighScores();
			Sounds.songs[3].stop();
		}
		
		if(InputUtil.leftPressed() && currentIndex > 0) {
			currentIndex--;
			currentCharacter = name[currentIndex];
		} else if(InputUtil.rightPressed() && currentIndex < 5) {
			currentIndex++;
			currentCharacter = name[currentIndex];
		}
		
		if(InputUtil.upPressed()) {
			if(currentCharacter >= Bitmaps.fontSheet.length) {
				currentCharacter = 0;
				name[currentIndex] = (char) currentCharacter;
			} else
				name[currentIndex] = (char) ++currentCharacter;
			nameEntered = true;
		} else if(InputUtil.downPressed()) {
			if(currentCharacter <= 0) {
				currentCharacter = Bitmaps.fontSheet.length;
				name[currentIndex] = (char) currentCharacter;
			} else
				name[currentIndex] = (char) --currentCharacter;
			nameEntered = true;
		}
		
		if(++blinkTimer >= 4)
			blinkTimer = 0;
	}
	
	public void render(Screen screen) {
		screen.draw(Bitmaps.highScoreScreen, 0, 0);
		
		boolean inserted = false;
		for(int i = 0; i < highScores.length; i++) {
			int yo = i * 16;

			if(i != ranking) {
				int index = i;
				if(inserted) index -= 1;
				drawString(highScores[index].getName(), 71, 152 + yo, screen);
				drawFixedDigitNumber(highScores[index].getScore(), 0xFFFFFFFF, 6, 128, 152 + yo, screen);
				drawFixedDigitNumber(highScores[index].getLevel(), 0xFFFFFFFF, 2, 184, 152 + yo, screen);
			} else {
				drawFixedDigitNumber(score.getScore(), 0xFFFFFFFF, 6, 128, 152 + yo, screen);
				drawFixedDigitNumber(score.getLevel(), 0xFFFFFFFF, 2, 184, 152 + yo, screen);
				inserted = true;
			}
		}
		
		for(int i = 0; i < 6; i++) {
			if(i == currentIndex && blinkTimer != 0) ;
			else if(name[i] != Bitmaps.fontSheet.length)
				screen.draw(Bitmaps.fontSheet[name[i]][0], 71 + i * 8, 152 + ranking * 16);
		}
	}
	
	// Getters and Setters
	public HighScore getHighScore() {
		return score;
	}
	
	public void setHighScore(HighScore highScore) {
		score = highScore;
	}
	
	public int getRanking() {
		return ranking;
	}
	
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
}
