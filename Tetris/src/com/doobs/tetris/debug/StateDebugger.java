package com.doobs.tetris.debug;

import java.awt.event.KeyEvent;

import com.doobs.java2d.input.InputHandler;
import com.doobs.tetris.Tetris;

public class StateDebugger {
	private Tetris tetris;
	
	public StateDebugger(Tetris tetris) {
		this.tetris = tetris;
	}
	
	public void tick(InputHandler input) {
		if(input.keys[KeyEvent.VK_CONTROL]) {
			if(input.keys[KeyEvent.VK_0])
				tetris.changeState(0, true);
			else if(input.keys[KeyEvent.VK_1])
				tetris.changeState(1, true);
			else if(input.keys[KeyEvent.VK_2])
				tetris.changeState(2, true);
			else if(input.keys[KeyEvent.VK_3])
				tetris.changeState(3, true);
			else if(input.keys[KeyEvent.VK_4])
				tetris.changeState(4, true);
			else if(input.keys[KeyEvent.VK_5])
				tetris.changeState(5, true);
		}
	}
}
