package com.doobs.tetris.states;

import res.bitmaps.Bitmaps;
import res.sound.Sounds;

import com.doobs.java2d.Game2D;
import com.doobs.java2d.gfx.Screen;
import com.doobs.java2d.input.InputHandler;
import com.doobs.tetris.InputUtil;
import com.doobs.tetris.Tetris;
import com.doobs.tetris.debug.LevelDebugger;
import com.doobs.tetris.level.Level;
import com.doobs.tetris.score.HighScore;

public class PlayingScreenState extends GameState {
	public static final int ID = 4;
	
	private Level level;
	private LevelDebugger levelDebugger;
	
	public PlayingScreenState(Tetris tetris) {
		this(tetris, 0);
	}
	
	public PlayingScreenState(Tetris tetris, int difficultyLevel) {
		super(tetris);
		level = new Level(difficultyLevel);
		levelDebugger = new LevelDebugger(tetris, level);
	}
	
	public void tick(InputHandler input) {
		boolean stateChanged = false;
		if(InputUtil.enterPressed() || InputUtil.escapePressed() && level.getHighScoreRanking() != -1) {
			HighScoreScreenState state = ((HighScoreScreenState)tetris.getGameStates()[HighScoreScreenState.ID]);
			state.setRanking(level.getHighScoreRanking());
			state.setHighScore(new HighScore("", level.getScore(), level.getLevel()));
			tetris.changeState(HighScoreScreenState.ID, true);
			Sounds.songs[3].play();
			stateChanged = true;
		} 
		if(InputUtil.escapePressed() && level.getHighScoreRanking() == -1) {
			tetris.changeState(LevelSelectionScreenState.ID, true);
			stateChanged = true;
		}
		if(stateChanged) {
			if(tetris.getSelectedSong() < 3 && !Sounds.songs[tetris.getSelectedSong()].isStopped())
				Sounds.songs[tetris.getSelectedSong()].stop();
			level.reset();
		}
		
		handlePause(input);
		level.tick(input);
		if(level.getCurrentAnimation() == Level.GAME_OVER && !Sounds.songs[tetris.getSelectedSong()].isStopped())
			Sounds.songs[tetris.getSelectedSong()].stop();
		if(Tetris.debug) levelDebugger.tick(input);
	}
	
	public void tickPaused(InputHandler input) {
		handlePause(input);
	}
	
	public void handlePause(InputHandler input) {
		if(InputUtil.pausePressed()) {
			Game2D game = tetris.getGame2D();
			if(game.getPaused()) game.unpause();
			else {
				game.pause();
				if(Tetris.sfx) Sounds.pause.play();
			}
		}
	}
	
	public void render(Screen screen) {
		screen.draw(Bitmaps.gameScreen, 0, 0);
		level.render(screen);
	}
	
	public void renderPaused(Screen screen) {
		screen.draw(Bitmaps.pauseScreen, 0, 0);
	}
	
	// Getters and Setters
	public Level getLevel() {
		return level;
	}
}
