package test;

import animata.AnimataPlayback;
import processing.core.PApplet;
import processing.opengl.*;
public class AddScene extends PApplet{

	AnimataPlayback playback;
	public void setup() {
	  size(400,400, OPENGL);
	  playback = new AnimataPlayback(this);
	  playback.addScene("violaplayer.nmt");
	}

	public void draw() {
		
	  //background(0);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "--bgcolor=#c0c0c0", "AddScene" });
	}

}
