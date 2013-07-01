package com.doobs.tetris;

import java.awt.event.KeyEvent;

import com.doobs.java2d.input.InputHandler;

// This class is to prevent the continued pressing of a certain key so the game can detect a key press only once
public class InputUtil {
	private static InputHandler input;
	public static boolean up, down, left, right, rotateLeft, rotateRight, escape, enter, pause, comma, period;
	
	public static void init(InputHandler input) {
		InputUtil.input = input;
		up = false;
		down = false;
		left = false;
		right = false;
		rotateLeft = false;
		rotateRight = false;
		escape = false;
		enter = false;
		pause = false;
	}
	
	public static void tick() {
		up = input.keys[KeyEvent.VK_UP];
		down = input.keys[KeyEvent.VK_DOWN];
		left = input.keys[KeyEvent.VK_LEFT];
		right = input.keys[KeyEvent.VK_RIGHT];
		rotateLeft = input.keys[KeyEvent.VK_Q];
		rotateRight = input.keys[KeyEvent.VK_E];
		escape = input.keys[KeyEvent.VK_ESCAPE];
		enter = input.keys[KeyEvent.VK_ENTER];
		pause = input.keys[KeyEvent.VK_P];
		comma = input.keys[KeyEvent.VK_COMMA];
		period = input.keys[KeyEvent.VK_PERIOD];
	}
	
	public static boolean upPressed() {
		return input.keys[KeyEvent.VK_UP] && !up;
	}
	
	public static boolean downPressed() {
		return input.keys[KeyEvent.VK_DOWN] && !down;
	}
	
	public static boolean leftPressed() {
		return input.keys[KeyEvent.VK_LEFT] && !left;
	}
	
	public static boolean rightPressed() {
		return input.keys[KeyEvent.VK_RIGHT] && !right;
	}
	
	public static boolean rotateLeftPressed() {
		return input.keys[KeyEvent.VK_Q] && !rotateLeft;
	}
	
	public static boolean rotateRightPressed() {
		return input.keys[KeyEvent.VK_E] && !rotateRight;
	}
	
	public static boolean escapePressed() {
		return input.keys[KeyEvent.VK_ESCAPE] && !escape;
	}
	
	public static boolean enterPressed() {
		return input.keys[KeyEvent.VK_ENTER] && !enter;
	}
	
	public static boolean pausePressed() {
		return input.keys[KeyEvent.VK_P] && !pause;
	}
	
	public static boolean commaPressed() {
		return input.keys[KeyEvent.VK_COMMA] && !comma;
	}
	
	public static boolean periodPressed() {
		return input.keys[KeyEvent.VK_PERIOD] && !period;
	}
}
