package com.doobs.tetris.font;

import res.bitmaps.Bitmaps;

import com.doobs.java2d.gfx.Bitmap;
import com.doobs.java2d.gfx.Screen;

public class FontRenderer {
	public static void drawString(String input, int xo, int yo, Screen screen) {
		drawStringColored(input, 0xFFFFFFFF, xo, yo, screen);
	}
	
	public static void drawStringColored(String input, int color, int xo, int yo, Screen screen) {
		Bitmap[][] fontSheet = Bitmaps.fontSheet;
		for(int i = 0; i < input.length(); i++) {
			if(input.charAt(i) == 32) continue;
			screen.drawColored(fontSheet[parseCharToIndex(input.charAt(i))][0], color, xo + i * (fontSheet[0][0].getWidth() + 1), yo);
		}
	}
	
	public static void drawFixedDigitNumber(int input, int color, int numOfDigits, int xo, int yo, Screen screen) {
		int numLength = String.valueOf(input).length();
				
		String addon = "";
		for(int i = 0; i < numOfDigits - numLength; i++) {
			addon += "0";
		}
		
		drawStringColored(addon + input, color, xo, yo, screen);
	}
	
	public static int parseCharToIndex(char input) {
		int temp = (int) input;
		if(temp >= 65 && temp <= 90)
			return 25 - (90 - input);
		else if(temp >= 97 && temp <= 122)
			return 25 - (122 - input);
		else if(temp >= 48 && temp <= 57)
			return 10 - (57 - input) + 25;
		else if(temp == 45)
			return 36;
		else
			return 0;
	}
	
	public static char parseIndexToChar(int index) {
		if(index >= 0 && index <= 25)
			return (char) (97 + index);
		else if(index > 25 && index < 36)
			return (char) (48 + index - 26);
		else if(index == 36)
			return '-';
		else
			return ' ';
	}
}
