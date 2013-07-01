package com.doobs.tetris.shapes;

import java.awt.event.KeyEvent;

import res.sound.Sounds;

import com.doobs.java2d.input.InputHandler;
import com.doobs.tetris.InputUtil;
import com.doobs.tetris.Tetris;
import com.doobs.tetris.level.Level;
import static com.doobs.tetris.blocks.Block.AIR;

public class Shape {
	public static final int SIZE = 4;
	
	private Level level;
	private int x, y, xd, yd;
	private int[][][] rotationData;
	private int rotation;
	private boolean active;
	
	private byte id;
	
	public static int softDropCounter = 0;
	public static boolean preventDrop = false;
	
	public Shape(Level level, int x, int y, int id) {
		this.level = level;
		this.x = x;
		this.y = y;
		xd = 0;
		yd = 0;
		rotationData = findShapeByID((byte) id);
		rotation = 0;
		active = true;
		this.id = (byte) id;
	}
	
	public void tick(InputHandler input) {
		// Check for movement
		boolean moved = false;
		
		if(InputUtil.downPressed()) {
				yd = -1;
				moved = true;
		}
		
		if(InputUtil.leftPressed()) {
			xd = -1;
			moved = true;
		}
		
		if(InputUtil.rightPressed()) {
			xd = 1;
			moved = true;
		}
		
		if(Tetris.sfx && moved) {
			Sounds.shapeMove.play();
		}
		
		if(input.keys[KeyEvent.VK_SPACE] && !preventDrop) {
			softDropCounter++;
			level.setDropTimer(level.getDropTimer() + 30);
		} else {
			softDropCounter = 0;
		}
		
		// Try to move
		if(!tryMove(rotationData[rotation]) && level.getDropped()) {
			deactivate();
		}
				
		// Check for rotation
		boolean rotated = false;
		
		if(InputUtil.rotateLeftPressed()) {
			rotated |= tryRotate(rotationData[rotation], getPreviousRotation(), false);
		}
		
		if(InputUtil.rotateRightPressed()) {
			rotated |= tryRotate(rotationData[rotation], getNextRotation(), true);
		}
		
		if(Tetris.sfx && rotated) {
			Sounds.shapeRotate.play();
		}
	}
	
	public boolean tryMove(int[][] currentRotation) {
		int previousYD = yd;
		
		// Erase the previous blocks
		for(int x = 0; x < currentRotation.length; x++) {
			for(int y = 0; y < currentRotation[0].length; y++) {
				if(currentRotation[x][y] != AIR) {
					level.setBlock(this.x + x, this.y + y, AIR);
				}
			}
		}
		
		// Check if the desired position is free
		for(int x = 0; x < currentRotation.length; x++) {
			for(int y = 0; y < currentRotation[0].length; y++) {
				if(currentRotation[x][y] != AIR) { 
					if(!isFree(this.x + x + xd, this.y + y))
						xd = 0;
					if(!isFree(this.x + x, this.y + y + yd))
						yd = 0;
				}
			}
		}
		
		// Set the new blocks
		for(int x = 0; x < currentRotation.length; x++) {
			for(int y = 0; y < currentRotation[0].length; y++) {
				if(currentRotation[x][y] != AIR) {
					level.setBlock(this.x + x + xd, this.y + y + yd, currentRotation[x][y]);
				}
			}
		}
		
		boolean free = (yd != 0 && previousYD != 0);
		
		// Update position and reset direction
		this.x += xd;
		this.y += yd;
		xd = 0;
		yd = 0;
		
		return free;
	}
	
	public boolean tryRotate(int[][] currentRotation, int[][] newRotation, boolean clockwise) {
		// Erase the previous blocks
		for(int x = 0; x < currentRotation.length; x++) {
			for(int y = 0; y < currentRotation[0].length; y++) {
				if(currentRotation[x][y] != AIR) {
					level.setBlock(this.x + x, this.y + y, AIR);
				}
			}
		}
		
		boolean free = true;
		
		// Check if the desired rotation is free
		for(int x = 0; x < currentRotation.length; x++) {
			for(int y = 0; y < currentRotation[0].length; y++) {
				if(newRotation[x][y] != AIR) {
					if(!level.inBounds(this.x + x, this.y + y) || level.getBlock(this.x + x, this. y + y).getID() != AIR)
						free = false;
				}
			}
		}
		
		// Set the new blocks
		if(free) {
			for(int x = 0; x < currentRotation.length; x++) {
				for(int y = 0; y < currentRotation[0].length; y++) {
					if(newRotation[x][y] != AIR) {
						level.setBlock(this.x + x + xd, this.y + y + yd, currentRotation[x][y]);
					}
				}
			}
			if(clockwise) incrementRotation();
			else decrementRotation();
		} else {
			for(int x = 0; x < currentRotation.length; x++) {
				for(int y = 0; y < currentRotation[0].length; y++) {
					if(currentRotation[x][y] != AIR) {
						level.setBlock(this.x + x + xd, this.y + y + yd, currentRotation[x][y]);
					}
				}
			}
		}
		
		return free;
	}
	
	public boolean trySpawn(int[][] currentRotation) {
		level.addShapeStat(id);
		for(int x = 0; x < currentRotation.length; x++) {
			for(int y = 0; y < currentRotation[0].length; y++) {
				if(currentRotation[x][y] != AIR) 
					level.setBlock(this.x + x, this.y + y, currentRotation[x][y]);
			}
		}
		return true;
	}
	
	public boolean isFree(int x, int y) {
		return level.inBounds(x, y) && level.getBlock(x, y).getID() == AIR;
	}
	
	public int[][] getPreviousRotation() {
		if(rotation - 1 < 0)
			return rotationData[rotationData.length - 1];
		else
			return rotationData[rotation - 1];
			 
	}
	
	public int[][] getNextRotation() {
		if(rotation + 1 >= rotationData.length)
			return rotationData[0];
		else
			return rotationData[rotation + 1];
			 
	}

	public void decrementRotation() {
		if(rotation - 1 < 0)
			rotation = SIZE - 1;
		else rotation--;
	}
	
	public void incrementRotation() {
		if(rotation + 1 >= SIZE)
			rotation = 0;
		else rotation++;
	}
	
	public void deactivate() {
		active = false;
	}
	
	public int[][][] findShapeByID(byte id) {
		return ShapeData.values()[id].getData();
	}
	
	// Getters and setters
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getXD() {
		return xd;
	}
	
	public void setXD(int xd) {
		this.xd = xd;
	}
	
	public int getYD() {
		return yd;
	}
	
	public void setYD(int yd) {
		this.yd = yd;
	}
	
	public int[][][] getRotationData() {
		return rotationData;
	}
	
	public int getRotation() {
		return rotation;
	}
	
	public int[][] getCurrentRotation() {
		return rotationData[rotation];
	}
	
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	
	public boolean getActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public int getID() {
		return (byte) id;
	}
	
	public void setID(int id) {
		this.id = (byte) id;
	}
}
