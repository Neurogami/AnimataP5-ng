package animata;

import animata.model.Layer;
import processing.core.PApplet;
import processing.xml.XMLElement;

public class AnimataPlayback {
	public static final float timeDivision = 42f;
	public static float gravity = 0;
	private final PApplet applet;
	private Layer root;
	private LayerView layersView;

	public AnimataPlayback(PApplet applet){
		this.applet = applet;
		this.applet.hint(PApplet.ENABLE_OPENGL_2X_SMOOTH);
		root = new Layer();
	}
	public void addScene(String xml){
		addScene(xml, root);
	}
	public void addScene(String xml, Layer parent){
		XMLElement element= new XMLElement(applet, xml);
		parent.addLayers(element.getChildren("layer"));
		layersView = new LayerView(root, applet);
	}
	public void draw(){
		root.simulate();
		applet.textureMode(PApplet.NORMAL);
		applet.noStroke();
		applet.translate(-200, 0,0);
		layersView.draw();
	}
}