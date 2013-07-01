package com.doobs.tetris.states;

import java.awt.event.KeyEvent;

import res.bitmaps.Bitmaps;
import res.sound.Sounds;

import com.doobs.java2d.gfx.Screen;
import com.doobs.java2d.input.InputHandler;
import com.doobs.tetris.InputUtil;
import com.doobs.tetris.Tetris;

public class MusicSelectionScreenState extends GameState {
	public static final int ID = 2;
	private int blinkTimer;
	private boolean leftSelected;
	
	public MusicSelectionScreenState(Tetris tetris) {
		super(tetris);
		blinkTimer = 0;
		leftSelected = true; 
	}
	
	public void tick(InputHandler input) {
		if(++blinkTimer >= 5) blinkTimer = 0;
		
		if(InputUtil.escapePressed()) {
			tetris.changeState(TitleScreenState.ID, true);
		}
		
		int selectedSong = tetris.getSelectedSong();
		int lastSelectedSong = selectedSong;
		boolean updatedSong = false;
		
		if(InputUtil.upPressed()) {
			if(leftSelected) {
				if(selectedSong != 0) {
					tetris.setSelectedSong(--selectedSong);
					updatedSong = true;
					Sounds.selectionChange.play();
				}
			} else if(!Tetris.sfx) {
				Tetris.sfx = true;
				Sounds.selectionChange.play();
			}
		}
		
		if(InputUtil.downPressed()) {
			if(leftSelected) {
				if(selectedSong != 3) {
					tetris.setSelectedSong(++selectedSong);
					updatedSong = true;
					Sounds.selectionChange.play();
				}	
			} else if(Tetris.sfx){
				Tetris.sfx = false;
				Sounds.selectionChange.play();
			}
		}
		
		if(input.keys[KeyEvent.VK_LEFT]) {
			leftSelected = true;
		}
		else if(input.keys[KeyEvent.VK_RIGHT]) {
			leftSelected = false;
		}
		
		if(InputUtil.enterPressed()) {
			Sounds.songs[lastSelectedSong].stop();
			tetris.changeState(LevelSelectionScreenState.ID, true);
		}
		
		if(updatedSong) {
			Sounds.songs[lastSelectedSong].stop();
			if(selectedSong != 3)
				Sounds.songs[selectedSong].loop();
		}
	}
	
	public void render(Screen screen) {
		screen.draw(Bitmaps.musicSelectionScreen, 0, 0);
		
		if(!leftSelected || blinkTimer != 0) {
			screen.draw(Bitmaps.pointer, 39, 97 + tetris.getSelectedSong() * 16);
			screen.drawReverse(Bitmaps.pointer, 111, 97 + tetris.getSelectedSong() * 16);
		}
		
		if(leftSelected || blinkTimer != 0) {
			screen.draw(Bitmaps.pointer, 155, Tetris.sfx ? 104 : 136);
			screen.drawReverse(Bitmaps.pointer, 190, Tetris.sfx ? 104 : 136);
		}
	}
}
