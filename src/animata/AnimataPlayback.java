package animata;

import processing.core.PApplet;

public class AnimataPlayback {
	private final PApplet applet;
	private LayerView root;

	public AnimataPlayback(PApplet applet){
		this.applet = applet;
		this.applet.registerDraw(this);
		root = new LayerView(applet);
	}
	public void addScene(String xml){
		addScene(xml, root);
	}
	public void addScene(String xml, LayerView parent){

	}
	public void draw(){

	}
}