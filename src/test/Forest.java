package test;

import animata.AnimataPlayback;
import processing.core.PApplet;
import processing.opengl.*;
public class Forest extends PApplet{

	AnimataPlayback playback;
	public void setup() {
	  size(950,614, OPENGL);
	  playback = new AnimataPlayback(this);
	  playback.addScene("forest.nmt");
	}

	public void draw() {
	  background(255);
	  playback.draw();
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "--bgcolor=#c0c0c0", "AddScene" });
	}

}
