package com.doobs.tetris.states;

import java.awt.event.KeyEvent;

import res.bitmaps.Bitmaps;

import com.doobs.java2d.gfx.Screen;
import com.doobs.java2d.input.InputHandler;
import com.doobs.tetris.Tetris;

public class CreditScreenState extends GameState {
	public static final int ID = 0;
	
	private final int SCREEN_TIME = 180;
	private int timer;
	
	public CreditScreenState(Tetris tetris) {
		super(tetris);
		timer = SCREEN_TIME;
	}

	public void tick(InputHandler input) {
		if(input.keys[KeyEvent.VK_ESCAPE]) {
			tetris.exit();
		}
		if(--timer <= 0)
			tetris.changeState(TitleScreenState.ID, false);
	}
	
	public void render(Screen screen) {
		screen.draw(Bitmaps.creditScreen, 0, 0);
	}
}
