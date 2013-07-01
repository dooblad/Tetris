package com.doobs.tetris.score;

public class HighScore {
	private String name;
	private int score, level;
	
	public HighScore(String name, int score, int level) {
		this.name = name;
		this.score = score;
		this.level = level;
	}
	
	public void print() {
		System.out.println(name + " got the high score of " + score + " at the level " + level + "!");
	}
	
	// Getters and setters
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
}
