package animata;

import processing.core.PApplet;
import processing.xml.XMLElement;

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
		XMLElement element= new XMLElement(applet, xml);
		parent.addLayers(element.getChildren("layer"));
	}
	public void draw(){

	}
}