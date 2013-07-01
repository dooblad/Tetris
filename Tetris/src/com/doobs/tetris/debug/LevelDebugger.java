package com.doobs.tetris.debug;

import java.awt.event.KeyEvent;

import com.doobs.java2d.input.InputHandler;
import com.doobs.tetris.InputUtil;
import com.doobs.tetris.Tetris;
import com.doobs.tetris.level.Level;

public class LevelDebugger {
	private Tetris tetris;
	private Level level;
	
	private int id;
	
	public LevelDebugger(Tetris tetris, Level level) {
		this.tetris = tetris;
		this.level = level;
		id = 1;
	}
	
	public void tick(InputHandler input) {
		if(input.keys[KeyEvent.VK_0]) id = 1;
		else if(input.keys[KeyEvent.VK_1]) id = 2;
		else if(input.keys[KeyEvent.VK_2]) id = 3;
		else if(input.keys[KeyEvent.VK_3]) id = 4;
		
		int currentLevel = level.getLevel();
		boolean levelChanged = false;
		if(InputUtil.commaPressed() && currentLevel > 0) {
			level.setLevel(--currentLevel);
			levelChanged = true;
		} else if(InputUtil.periodPressed() && currentLevel < 99) {
			level.setLevel(++currentLevel);
			levelChanged = true;
		}
		
		if(levelChanged)
			level.setLinesCleared(currentLevel * 10);
		
		// Convert window mouse coordinates into coordinates that can interact with the level's block grid
		int mouseGridX = (input.getMouseX() / Tetris.SCALE - 94) / 8;
		int mouseGridY = 19 - (input.getMouseY() / Tetris.SCALE - 40) / 8;
		
		if(input.isLeftMousePressed())
			level.setBlock(mouseGridX, mouseGridY, id);
		if(input.isRightMousePressed())
			level.setBlock(mouseGridX, mouseGridY, 0);
	}
	
	public Tetris getTetris() {
		return tetris;
	}
}
