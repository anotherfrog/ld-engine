package uk.co.nobber.engine.util;

import java.util.Iterator;
import java.util.Map;

public class Utils {

	public static float distance(double x1, double y1, double x2, double y2) {
		return (float) (Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2)));
	}

	// Method originally created by Alex J. Nicholson (Twitter: @TechnoCodeFox, Github: TechnoCF)
	public static boolean collision(Rectangle rect1, Rectangle rect2) {
		return (rect1.getX() < rect2.getX() + rect2.getWidth()) && (rect1.getX() + rect1.getWidth() > rect2.getX())
				&& (rect1.getY() < rect2.getY() + rect2.getHeight())
				&& (rect1.getHeight() + rect1.getY() > rect2.getY());
	}

	public static void printMap(Map mp) {
		System.out.println("=======================[START]=======================");
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			it.remove(); // avoids a ConcurrentModificationException
		}
		System.out.println("=======================[END]=======================");
	}

	public static int getHex(int r, int g, int b) {
		if (r > 255) r = 255;
		if (g > 255) g = 255;
		if (b > 255) b = 255;
		
		if (r < 0) r = 0;
		if (g < 0) g = 0;
		if (b < 0) b = 0;
		
		return (0xff << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
	}
	
	public static int getHex(float r, float g, float b) {
		return Utils.getHex((int) (r * 255), (int) (g * 255), (int) (b * 255));
	}
}
