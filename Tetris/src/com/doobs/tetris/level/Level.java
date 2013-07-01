package com.doobs.tetris.level;

import java.awt.event.KeyEvent;

import res.bitmaps.Bitmaps;
import res.sound.Sounds;

import com.doobs.java2d.gfx.Screen;
import com.doobs.java2d.input.InputHandler;
import com.doobs.tetris.Tetris;
import com.doobs.tetris.blocks.Block;
import com.doobs.tetris.score.HighScore;
import com.doobs.tetris.score.HighScores;
import com.doobs.tetris.shapes.Shape;
import com.doobs.tetris.shapes.ShapeData;

import static com.doobs.tetris.blocks.Block.AIR;
import static com.doobs.tetris.font.FontRenderer.*;

public class Level {
	public static final int WIDTH = 10, HEIGHT = 20;
	
	private int level;
	
	private int highScoreRanking;
	
	// Statistical variables
	private int[] shapeStats;
	private int linesCleared, top, score;
	
	// Block-related variables
	private Block[][] blocks;
	private Shape currentShape;
	private Shape nextShape;
	private int dropTimer;
	private boolean dropped;
	
	// Line-clearing variables
	private int currentLine;
	private int consecutiveLines;

	// Animation variables
	public static final int NO_ANIMATION = -1, LINE_CLEAR = 0, GAME_OVER = 1;
	private int frameCounter;
	private int animationTimer;
	private int currentAnimation;
	
	
	public Level(int level) {
		reset();
		this.level = level;
		linesCleared = level * 10;
	}
	
	public void reset() {
		level = 0;
		
		highScoreRanking = -1;
		
		shapeStats = new int[7];
		linesCleared = 0;
		top = HighScores.highScores[0].getScore();
		score = 0;
		
		blocks = new Block[WIDTH][HEIGHT];
		for(int x = 0; x < blocks.length; x++) {
			for(int y = 0; y < blocks[0].length; y++) {
				setBlock(x, y, 0);
			}
		}
		
		currentShape = generateRandomShape();
		currentShape.trySpawn(getRotation(currentShape));
		nextShape = generateRandomShape();
		
		currentLine = 0;
		frameCounter = 0;
		animationTimer = 0;
		consecutiveLines = 0;
		currentAnimation = -1;
	}
	
	public void tick(InputHandler input) {
		if(!currentShape.getActive() && currentAnimation == NO_ANIMATION) {
			checkForGameOver();
			checkForFullLines();
		}
		
		// An if statement to allow the game to be paused for animations
		if(currentAnimation == NO_ANIMATION) {
			if(!currentShape.getActive()) {
				if(input.keys[KeyEvent.VK_SPACE]) Shape.preventDrop = true;
				nextShape();
			}
			
			if(!input.keys[KeyEvent.VK_SPACE]) Shape.preventDrop = false;
			
			dropped = false;
			
			if(dropTimer++ >= 60 - level * 5) {
				currentShape.setYD(-1);
				dropTimer = 0;
				dropped = true;
			}
			
			currentShape.tick(input);
		} else if(currentAnimation == LINE_CLEAR) {
			handleFullLines();
		} else if(currentAnimation == GAME_OVER) {
			handleGameOver();
			if(frameCounter == HEIGHT)
				 determineRanking();
		}
	}
	
	public void determineRanking() {
		HighScore[] highScores = HighScores.highScores;
		int ranking = -1;
		if(score > highScores[2].getScore()) {
			for(int i = highScores.length - 1; i >= 0; i--) {
				if(score > highScores[i].getScore()) ranking = i;
			}
		}
		highScoreRanking = ranking;
	}
	
	public void checkForGameOver() {
		int[][] currentRotation = currentShape.getCurrentRotation();
		for(int y = 0; y < Shape.SIZE; y++) {
			for(int x = 0; x < Shape.SIZE; x++) {
				if(currentRotation[x][y] != Block.AIR && (y + currentShape.getY()) >= HEIGHT) {
					currentAnimation = GAME_OVER;
					return;
				}
			}
		}
	}
	
	public void checkForFullLines() {
		currentLine = findNextFullLine();
		if(currentLine != -1) 
			currentAnimation = LINE_CLEAR;
	}
	
	public void handleGameOver() {
		if(++animationTimer >= 5) {
			animationTimer = 0;
			if(++frameCounter > HEIGHT) {
				frameCounter = HEIGHT;
			}
		}
	}
	
	public void handleFullLines() {
		if(++animationTimer >= 5){
			animationTimer = 0;
			blocks[blocks.length / 2 - frameCounter - 1][currentLine].setID(AIR);
			blocks[blocks.length / 2 + frameCounter][currentLine].setID(AIR);
			if(++frameCounter >= 5) {
				frameCounter = 0;
				shiftBlocksDown(currentLine, 1);
				consecutiveLines++;
				linesCleared++;
				level = linesCleared / 10;
				if(level > 11) level = 11;
				
				if(Tetris.sfx) {
					if(consecutiveLines == 4)
						Sounds.fourLineClear.play();
					else
						Sounds.lineClear.play();
				}
				currentLine = findNextFullLine();
				if(currentLine == -1) {
					currentAnimation = NO_ANIMATION;
					calculateLineClearScore();
				}
			}
		}
	}
	
	public int findNextFullLine() {
		for(int i = 0; i < HEIGHT; i++) {
			if(isFullLine(i)) return i;
		}
		return -1;
	}
	
	public boolean isFullLine(int lineNumber) {
		boolean fullLine = true;
		for(int x = 0; x < WIDTH; x++) {
			if(blocks[x][lineNumber].getID() == 0)
				fullLine = false;
		}
		return fullLine;
	}
	
	public void calculateLineClearScore() {
		switch(consecutiveLines) {
		case 1: score += 40 * (level + 1); break;
		case 2: score += 100 * (level + 1); break;
		case 3: score += 300 * (level + 1); break;
		case 4: score += 1200 * (level + 1); break;
		default: ;
		}
		
		consecutiveLines = 0;
	}
	
	
	public void render(Screen screen) {
		int blockWidth = Bitmaps.blocks[0][0].getWidth();
		int blockHeight = Bitmaps.blocks[0][0].getHeight();
		for(int x = 0; x < blocks.length; x++) {
			for(int y = 0; y < blocks[0].length; y++) {
				byte id = blocks[x][y].getID();
				if(id > 0 && id <= Bitmaps.blocks.length) {
					/*screen.draw(Bitmaps.blockSprites[blocks[x][y].getID() - 1][0], 96 + x * (blockWidth + 1), 
							192 - y * (blockHeight + 1));*/
					screen.drawColoredExceptFor(Bitmaps.blocks[id - 1][0], getLevelColorByID(id), 0xFFFFFFFF,
							96 + x * (blockWidth + 1), 192 - y * (blockHeight + 1));
				}
			}
		}
		
		if(currentAnimation == GAME_OVER) {
			for(int i = 0; i < frameCounter; i++) {
				screen.fillRect(LevelColors.colors[level % 10][0], 96, 40 + i * (Block.SIZE + 1), 79, 2);
				screen.fillRect(0xFFFFFFFF, 96, 42 + i * (Block.SIZE + 1), 79, 3);
				screen.fillRect(LevelColors.colors[level % 10][1], 96, 45 + i * (Block.SIZE + 1), 79, 2);
			}
		}
		
		// Statistical rendering
		renderNextShape(nextShape, screen);
		renderShapeStats(screen);
		drawFixedDigitNumber(linesCleared, 0xFFFFFFFF, 3, 153, 16, screen);
		drawFixedDigitNumber(top, 0xFFFFFFF, 6, 192, 33, screen);
		drawFixedDigitNumber(score, 0xFFFFFFFF, 6, 192, 58, screen);
		drawFixedDigitNumber(level, 0xFFFFFFFF, 2, 208, 168, screen);
	}
	
	// For use in rendering shapes to the "Next Shape" box
	public void renderNextShape(Shape shape, Screen screen) {
		int separation = Bitmaps.blocks[0][0].getWidth() + 1;
		int xOffset = 0, yOffset = 0;
		for(int x = 0; x < Shape.SIZE; x++) {
			for(int y = 0; y < Shape.SIZE; y++) {
				byte blockID = (byte) shape.getRotationData()[0][x][Shape.SIZE - 1 - y];
				if(blockID != 0) {
					switch(shape.getID()) {
					case 0:
						xOffset = 4;
						yOffset = 2;
						break;
					case 1:
						xOffset = 1;
						yOffset = 0;
						break;
					case 2:
						xOffset = 0;
						yOffset = 0;
						break;
					case 3:
						xOffset = 0;
						yOffset = 4;
						break;
					case 4:
						xOffset = 4;
						yOffset = 5;
						break;
					case 5:
						xOffset = -1;
						yOffset = 7;
						break;
					case 6:
						xOffset = -4;
						yOffset = 5;
						break;
					default: ;
					}
					screen.drawColoredExceptFor(Bitmaps.blocks[blockID - 1][0], getLevelColorByID(blockID), 
							0xFFFFFFFF, 192 + x * separation + xOffset, 108 + y * separation + yOffset);
				}
			}
		}
	}
	
	public void renderShapeStats(Screen screen) {
		for(int i = 0; i < shapeStats.length; i++) {
			drawFixedDigitNumber(shapeStats[i], 0xFFFF0000, 3, 48, 88 + i * 16, screen);
		}
		renderMiniShape(0, 1, 23, 82, screen);
		renderMiniShape(1, 1, 26, 94, screen);
		renderMiniShape(2, 3, 26, 111, screen);
		renderMiniShape(3, 3, 23, 127, screen);
		renderMiniShape(4, 2, 20, 143, screen);
		renderMiniShape(5, 3, 20, 158, screen);
		renderMiniShape(6, 2, 26, 173, screen);
	}
	
	public void renderMiniShape(int id, int rotation, int xo, int yo, Screen screen) {
		ShapeData shapeData = ShapeData.values()[id];
		for(int x = 0; x < Shape.SIZE; x++) {
			for(int y = 0; y < Shape.SIZE; y++) {
				if(shapeData.getData()[rotation][x][Shape.SIZE - y - 1] != Block.AIR)
					screen.drawColoredExceptFor(Bitmaps.miniBlocks[shapeData.getID() - 1][0], 
						getLevelColorByID(shapeData.getID()), 0xFFFFFFFF, xo + x * 6, yo + y * 6);
			}
		}
	}
	
	public int getLevelColorByID(byte id) {
		if(id == Block.RED_OUTLINE)
			return LevelColors.colors[level % 10][1];
		else if(id == Block.RED_FILL)
			return LevelColors.colors[level % 10][1];
		else if(id == Block.YELLOW_FILL)
			return LevelColors.colors[level % 10][0];
		else if(id == Block.RED_OUTLINE_SOLID)
			return LevelColors.colors[level % 10][1];
		else
			return 0xFFFFFFFF;
	}
	
	public void addShapeStat(byte id) {
		shapeStats[id]++;
	}
	
	public void shiftBlocksDown(int startIndex, int downShifts) {
		// Create a buffer to hold the original block data and replace all 
        // blocks with air before shifting down
		Block[][] tempBlocks = new Block[WIDTH][HEIGHT];
		for(int x = 0; x < blocks.length; x++) {
			for(int y = startIndex + 1; y < blocks[0].length; y++) {
				tempBlocks[x][y] = new Block(blocks[x][y].getID());
				blocks[x][y].setID(Block.AIR);
			}
		}
		
		// Shift the blocks down based on the variable downShifts
		for(int x = 0; x < blocks.length; x++) {
			for(int y = startIndex; y < blocks[0].length - downShifts; y++) {
				blocks[x][y] = new Block(tempBlocks[x][y + downShifts].getID());
			}
		}
	}
	
	public void nextShape() {
		currentShape = nextShape; 
		currentShape.trySpawn(getRotation(currentShape));
		nextShape = generateRandomShape();
		score += Shape.softDropCounter;
		if(Tetris.sfx) Sounds.shapeDrop.play();
	}
	
	public Shape generateRandomShape() {
		byte random = (byte) (Math.random() * ShapeData.values().length);
		int spawnHeight = blocks[0].length - 1;
		return new Shape(this, 3, spawnHeight, random);
	}
	
	public int[][] getRotation(Shape shape) {
		return shape.getRotationData()[shape.getRotation()];
	}
	
	// This is a separate method from inArrayBounds to allow blocks to float down from above
	// by removing the upper boundary check
	public boolean inBounds(int x, int y) {
		if(x >= 0 && x < blocks.length && y >= 0) return true;
		else return false;
	}
	
	// A more strict check on the boundaries that prevents out of bounds exceptions
	public boolean inArrayBounds(int x, int y) {
		if(x >= 0 && x < blocks.length && y >= 0 && y < blocks[0].length) return true;
		else return false;
	}
	
	// Getters and setters
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
		linesCleared = level * 10;
	}
	
	public int getHighScoreRanking() {
		return highScoreRanking;
	}
	
	public int getLinesCleared() {
		return linesCleared;
	}
	
	public void setLinesCleared(int linesCleared) {
		this.linesCleared = linesCleared;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public Block getBlock(int x, int y) { 
		if(inArrayBounds(x, y))
			return blocks[x][y];
		else
			return new Block((byte) AIR);
	}
	
	public void setBlock(int x, int y, int id) {
		if(inArrayBounds(x, y))
			blocks[x][y] = new Block((byte) id);
		else
			return;
	}
	
	public int getDropTimer() {
		return dropTimer;
	}
	
	public void setDropTimer(int dropTimer) {
		this.dropTimer = dropTimer;
	}
	
	public boolean getDropped() {
		return dropped;
	}
	
	public int getCurrentAnimation() {
		return currentAnimation;
	}
}
