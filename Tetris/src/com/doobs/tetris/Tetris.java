package com.doobs.tetris;

import java.awt.event.KeyEvent;

import res.bitmaps.Bitmaps;
import res.sound.Sounds;

import com.doobs.java2d.Game2D;
import com.doobs.java2d.GameLoop;
import com.doobs.java2d.gfx.Screen;
import com.doobs.java2d.input.InputHandler;
import com.doobs.tetris.debug.StateDebugger;
import com.doobs.tetris.score.HighScores;
import com.doobs.tetris.states.CreditScreenState;
import com.doobs.tetris.states.GameState;
import com.doobs.tetris.states.HighScoreScreenState;
import com.doobs.tetris.states.LevelSelectionScreenState;
import com.doobs.tetris.states.MusicSelectionScreenState;
import com.doobs.tetris.states.PlayingScreenState;
import com.doobs.tetris.states.TitleScreenState;

public class Tetris extends GameLoop{
	public static final int WIDTH = 256, HEIGHT = 224;
	public static final int SCALE = 2;
	private static final String NAME = "Tetris";
	private static final boolean RENDER_FPS = false;
	private static final boolean VSYNC = true;
	
	public static boolean sfx = false;
	
	public static boolean debug = true;
	
	private int selectedSong;
	
	private int resetCounter;
	
	private Game2D game;
	
	private GameState[] gameStates;
	private GameState currentState;
	private StateDebugger stateDebugger;
		
	public Tetris() {
		selectedSong = 0;
		resetCounter = 0;
		game = new Game2D(WIDTH, HEIGHT, SCALE, NAME, this);
		Bitmaps.setBitmapLoader(game.getBitmapLoader());
		game.getBitmapLoader().setIgnoredColors(0xFF00FF);
		Bitmaps.init();
		Sounds.init();
		HighScores.init();
		game.setRenderFPS(RENDER_FPS);
		game.setVSync(VSYNC);
		InputUtil.init(game.getInputHandler());
		gameStates = new GameState[] {new CreditScreenState(this), new TitleScreenState(this), new MusicSelectionScreenState(this), 
				new LevelSelectionScreenState(this), new PlayingScreenState(this), new HighScoreScreenState(this)};
		changeState(CreditScreenState.ID, false);
		stateDebugger = new StateDebugger(this);
		game.start();
	}
	
	public void tick(InputHandler input) {
		if(input.keys[KeyEvent.VK_F5]) {
			resetCounter++;
			if(resetCounter >= 60)
				HighScores.loadDefaultHighScores();
		} else
			resetCounter = 0;
		
		if(debug) stateDebugger.tick(input);
		currentState.tick(input);
		InputUtil.tick();
	}
	
	public void tickPaused(InputHandler input) {
		currentState.tickPaused(input);
		InputUtil.tick();
	}
	
	public void render(Screen screen) {
		currentState.render(screen);
	}
	
	public void renderPaused(Screen screen) {
		currentState.renderPaused(screen);
	}
	
	public static void main(String[] args) {
		if(args.length != 0 && args[0].equals("-debug")) debug = true;
		new Tetris();
	}
	
	public void changeState(int stateID, boolean sound) {
		if(sound) Sounds.stateChange.play();
		currentState = gameStates[stateID];
	}
	
	public void exit() {
		System.exit(0);
	}
	
	// Getters and setters
	public Game2D getGame2D() {
		return game;
	}
	
	public int getSelectedSong() {
		return selectedSong;
	}
	
	public void setSelectedSong(int selectedSong) {
		this.selectedSong = selectedSong;
	}
	
	public GameState[] getGameStates() {
		return gameStates;
	}
	
	public GameState getCurrentState() {
		return currentState;
	}
}
