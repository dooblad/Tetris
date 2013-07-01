package res.bitmaps;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.doobs.java2d.gfx.Bitmap;
import com.doobs.java2d.gfx.BitmapLoader;

public class Bitmaps {
	private static BitmapLoader loader;
	
	public static Bitmap creditScreen, titleScreen, musicSelectionScreen, 
		levelSelectionScreen, gameScreen, pauseScreen, highScoreScreen;

	public static Bitmap[][] blocks, miniBlocks;
	
	public static Bitmap pointer;
	
	public static Bitmap[][] fontSheet;
	
	public static void init() {
		creditScreen = loader.loadBitmap(getImage("screens/creditScreen.png"));
		titleScreen = loader.loadBitmap(getImage("screens/titleScreen.png"));
		musicSelectionScreen = loader.loadBitmap(getImage("screens/musicSelectionScreen.png"));
		levelSelectionScreen = loader.loadBitmap(getImage("screens/levelSelectionScreen.png"));
		gameScreen = loader.loadBitmap(getImage("screens/gameScreen.png"));
		pauseScreen = loader.loadBitmap(getImage("screens/pauseScreen.png"));
		highScoreScreen = loader.loadBitmap(getImage("screens/highScoreScreen.png"));

		blocks = loader.loadTileSheet(getImage("blocks.png"), 4, 1);
		miniBlocks = loader.loadTileSheet(getImage("miniBlocks.png"), 4, 1);
		
		pointer = loader.loadBitmap(getImage("pointer.png"));
		
		fontSheet = loader.loadTileSheet(getImage("fontSheet.png"), 37, 1);
	}
	
	private static BufferedImage getImage(String URL) {
		try {
			return ImageIO.read((Bitmaps.class.getResource(URL)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setBitmapLoader(BitmapLoader bitmapLoader) {
		loader = bitmapLoader;
	}
}
