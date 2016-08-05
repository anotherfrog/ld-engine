package uk.co.nobber.minild;

public class Main {

	public static void main(String... args) {
		int width = 1280;
		int height = 720;
		int scale = 2;
		
		new MiniLD(width / scale, height / scale, scale, "MiniLD 69: Colonization").start(60, false);
	}
}
