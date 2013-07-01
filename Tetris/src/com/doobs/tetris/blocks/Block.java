package com.doobs.tetris.blocks;

public class Block {
	public static final byte AIR = 0, RED_OUTLINE = 1, RED_FILL = 2, YELLOW_FILL = 3, RED_OUTLINE_SOLID = 4;
	public static final int SIZE = 7;
	
	private byte ID;
	
	public Block() {
		this((byte) 0);
	}
	public Block(byte ID) {
		this.ID = ID;
	}
	
	// Getters and setters
	public byte getID() {
		return ID;
	}
	public void setID(byte ID) {
		this.ID = ID;
	}
}
