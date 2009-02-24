package animata;

import java.io.File;

import processing.core.PApplet;
import processing.xml.XMLElement;
import animata.model.Layer;
import java.util.ArrayList;

public class AnimataP5 {

	public static final float timeDivision = 42f;
	public static float gravity = 0;
	private static boolean debug;
	public  PApplet applet;
	public Layer root;
	public LayerView layersView;
    public ArrayList<Layer>allLayers = new ArrayList<Layer>();

	public AnimataP5(PApplet applet, String nmtFile){
		setup(applet);
		addScene(nmtFile);
	}

	private void setup(PApplet applet) {
		this.applet = applet;
		root = new Layer();
	}
    
	public void addScene(String xml){
		String folder = new File(xml).getParent();
		if(folder == null) folder = ".";
		XMLElement element = new XMLElement(applet, xml);
        layersView = new LayerView(root.addLayer(folder, element),applet);
	}

	public void draw(float x,float y){
		root.simulate();
		applet.textureMode(PApplet.NORMAL);
		applet.noStroke();
		applet.fill(0,0);
		layersView.draw(x,y);
	}

    public void moveJointY(String name, float value) {
        root.moveJointY(name, value);
	}

    public void moveJointX(String name, float value) {
        root.moveJointX(name, value);
	}

    public void setBoneTempo(String name, float value) {
        root.setBoneTempo(name, value);
	}

    public void setBoneRange(String name, float min,float max) {
        root.setBoneRange(name, min, max);
	}
}