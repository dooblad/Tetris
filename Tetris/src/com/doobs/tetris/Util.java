package com.doobs.tetris;

public class Util {
	public static void copyArray(Object[] original, Object[] duplicate) {
		for(int i = 0; i < original.length; i++) {
			duplicate[i] = original[i];
		}
	}
}
