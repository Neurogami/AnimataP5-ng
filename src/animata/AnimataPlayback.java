package animata;

import oscP5.OscMessage;
import animata.model.Layer;
import processing.core.PApplet;
import processing.xml.XMLElement;

public class AnimataPlayback {
	public static final float timeDivision = 42f;
	public static float gravity = 0;
	private final PApplet applet;
	private Layer root;
	private LayerView layersView;
	private Controller controller;

	public AnimataPlayback(PApplet applet){
		this.applet = applet;
		this.applet.hint(PApplet.ENABLE_OPENGL_2X_SMOOTH);
		root = new Layer();
		controller = new Controller(applet,root);
		float cameraZ = 1500;//(applet.height/2.0f) / PApplet.tan(PApplet.PI*60.0f / 360.0f);
		applet.camera(applet.width/2.0f, applet.height/2.0f, cameraZ, applet.width/2.0f, applet.height/2.0f, 0f, 0f, 1f, 0f);
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
		applet.translate(applet.width/2, applet.height/2);
		layersView.draw();
	}

}