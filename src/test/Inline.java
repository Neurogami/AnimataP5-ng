package test;

import animata.AnimataPlayback;
import processing.core.PApplet;
import processing.opengl.*;
public class Inline extends PApplet{

	AnimataPlayback playback;
	public void setup() {
	  size(400,400, OPENGL);
	  playback = new AnimataPlayback(this,"violaplayer.nmt");
	}

	public void draw() {
	  background(0);
	  playback.draw();
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "--bgcolor=#c0c0c0", "AddScene" });
	}

}
