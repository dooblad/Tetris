package com.doobs.tetris.sound;

import java.io.IOException;
import java.io.InputStream;

import org.newdawn.easyogg.OggClip;

public class Sound {
	private OggClip sound;
	
	public Sound(InputStream stream) {
		try {
			sound = new OggClip(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Sound(String URL) {
		try {
			sound = new OggClip(URL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loop() {
		sound.loop();
	}
	
	public void play() {
		if(sound.stopped())
			sound.play();
	}
	
	public void stop() {
		if(!sound.stopped()) {
			sound.stop();
		}
	}
	
	public void resume() {
		sound.resume();
	}
	
	public boolean isStopped() {
		return sound.stopped();
	}
	
	// Getters and setters
	public OggClip getSound() {
		return sound;
	}
}
