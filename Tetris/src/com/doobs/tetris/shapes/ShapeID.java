package com.doobs.tetris.shapes;

public enum ShapeID {
	I (0),
	J (1),
	L (2),
	O (3),
	S (4),
	T (5),
	Z (6);
	
	private byte id;
	
	ShapeID(int id) {
		this.id = (byte) id;
	}
	
	byte getID() {
		return id;
	}
}
