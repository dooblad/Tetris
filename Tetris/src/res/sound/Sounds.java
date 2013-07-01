package res.sound;

import com.doobs.tetris.sound.Sound;

public class Sounds {
	public static Sound[] songs;
	
	public static Sound selectionChange, stateChange, shapeDrop, shapeMove, shapeRotate, pause, lineClear, fourLineClear, gameOver;
	
	public static void init() {
		songs = new Sound[] {
			new Sound(Sounds.class.getResourceAsStream("song1.ogg")),
			new Sound(Sounds.class.getResourceAsStream("song2.ogg")),
			new Sound(Sounds.class.getResourceAsStream("song3.ogg")),
			new Sound(Sounds.class.getResourceAsStream("highScoreSong.ogg"))
		};
		selectionChange = new Sound(Sounds.class.getResourceAsStream("sfx/selectionChange.ogg"));
		stateChange = new Sound(Sounds.class.getResourceAsStream("sfx/stateChange.ogg"));
		shapeDrop = new Sound(Sounds.class.getResourceAsStream("sfx/shapeDrop.ogg"));
		shapeMove = new Sound(Sounds.class.getResourceAsStream("sfx/shapeMove.ogg"));
		shapeRotate = new Sound(Sounds.class.getResourceAsStream("sfx/shaperotate.ogg"));
		pause = new Sound(Sounds.class.getResourceAsStream("sfx/pause.ogg"));
		lineClear = new Sound(Sounds.class.getResourceAsStream("sfx/lineClear.ogg"));
		fourLineClear = new Sound(Sounds.class.getResourceAsStream("sfx/fourLineClear.ogg"));
		gameOver = new Sound(Sounds.class.getResourceAsStream("sfx/gameOver.ogg"));
	}
}
