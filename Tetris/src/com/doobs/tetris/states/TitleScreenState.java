package com.doobs.tetris.states;

import res.bitmaps.Bitmaps;

import com.doobs.java2d.gfx.Screen;
import com.doobs.java2d.input.InputHandler;
import com.doobs.tetris.InputUtil;
import com.doobs.tetris.Tetris;

public class TitleScreenState extends GameState {
	public static final int ID = 1;
	
	public TitleScreenState(Tetris tetris) {
		super(tetris);
	}

	public void tick(InputHandler input) {
		if(InputUtil.escapePressed()) {
			tetris.exit();
		}
		if(InputUtil.enterPressed()) {
			tetris.changeState(MusicSelectionScreenState.ID, true);
		}
	}
	
	public void render(Screen screen) {
		screen.draw(Bitmaps.titleScreen, 0, 0);
	}
}
