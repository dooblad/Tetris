package com.doobs.tetris.score;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.doobs.tetris.Util;

public class HighScores {
	private static final String DEFAULT_HIGH_SCORE_URL = "data/default.txt", HIGH_SCORE_URL = "data/highScores.txt";
	private static final int NUM_OF_HIGH_SCORES = 3;
	
	public static HighScore[] highScores;
	
	public static void init() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(HIGH_SCORE_URL)));
			String line;
			int counter = 0;
			highScores = new HighScore[NUM_OF_HIGH_SCORES];
			String[] temp;
			while((line = reader.readLine()) != null || counter < highScores.length) {
				temp = line.split(":");
				highScores[counter] = new HighScore(temp[0], Integer.valueOf(temp[1]), Integer.valueOf(temp[2]));
				counter++;
			}
			reader.close();
		} catch(IOException e) {
			loadDefaultHighScores();
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void insertHighScore(HighScore newScore, int ranking) {
		if(ranking < highScores.length && ranking >= 0) {
			HighScore[] temp = new HighScore[highScores.length];
			Util.copyArray(highScores, temp);
			boolean inserted = false;
			for(int i = 0; i < highScores.length; i++) {
				if(inserted) {
					highScores[i] = temp[i - 1];
				}
				
				if(ranking == i) {
					highScores[i] = newScore;
					inserted = true;
				}
			}
		}
	}
	
	public static void overwriteHighScores() {
		File file = new File(HIGH_SCORE_URL);
		BufferedWriter writer = null;
		try {
			file.createNewFile();
			file.setWritable(true);
			writer = new BufferedWriter(new FileWriter(file));
			for(int i = 0; i < highScores.length; i++) {
				writer.write(highScores[i].getName() + ":" + highScores[i].getScore() + ":" + highScores[i].getLevel());
				writer.newLine();
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadDefaultHighScores() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(DEFAULT_HIGH_SCORE_URL)));
			String line;
			int counter = 0;
			String[] temp;
			highScores = new HighScore[NUM_OF_HIGH_SCORES];
			while((line = reader.readLine()) != null || counter < NUM_OF_HIGH_SCORES) {
				temp = line.split(":");
				highScores[counter] = new HighScore(temp[0], Integer.valueOf(temp[1]), Integer.valueOf(temp[2]));
				counter++;
			}
			reader.close();
			overwriteHighScores();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		highScores = null;
	}
	
	public static void printHighScores() {
		for(int i = 0; i < highScores.length; i++) {
			highScores[i].print();
		}
	}
}
