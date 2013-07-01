package com.doobs.tetris.states;

import com.doobs.java2d.gfx.Screen;
import com.doobs.java2d.input.InputHandler;
import com.doobs.tetris.Tetris;

public class GameState {
	protected Tetris tetris;
	
	public GameState(Tetris tetris) {
		this.tetris = tetris;
	}
	
	public void tick(InputHandler input) {
		
	}
	
	public void tickPaused(InputHandler input) {
		
	}
	
	public void render(Screen screen) {
		
	}
	
	public void renderPaused(Screen screen) {
		
	}
}
