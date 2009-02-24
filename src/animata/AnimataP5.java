package animata;

import java.io.File;
import processing.core.PApplet;
import processing.xml.XMLElement;
import animata.model.Layer;

public class AnimataP5 {

	public static final float timeDivision = 42f;
	public static float gravity = 0;
	public  PApplet applet;
	public Layer root;
	public LayerView layersView;

	public AnimataP5(PApplet applet, String nmtFile){
		setup(applet);
		addScene(nmtFile);
	}

	private void setup(PApplet applet) {
		this.applet = applet;
		root = new Layer();
	}
    
	private void addScene(String xml){
		String folder = new File(xml).getParent();
		if(folder == null) folder = ".";
		XMLElement element = new XMLElement(applet, xml);
        layersView = new LayerView(root.addLayer(folder, element),applet);
	}

	public void draw(float x,float y){
		root.simulate();
		applet.textureMode(PApplet.NORMAL);
		applet.noStroke();
		applet.fill(0);
		layersView.draw(x,y);
	}

    public void moveJointY(String name, float value) {
        root.moveJointY(name, value);
	}

    public void moveJointX(String name, float value) {
        root.moveJointX(name, value);
	}

    public void setLayerAlpha(String name, float value) {
        if(value>255) value=255;
        if(value<0) value=0;
        root.setLayerAlpha(name, value);
	}

    public void setLayerPos(String name, float x,float y) {
        root.setLayerPos(name, x,y);
	}

    public void setBoneTempo(String name, float value) {
        root.setBoneTempo(name, value);
	}

    public void setBoneRange(String name, float min,float max) {
        root.setBoneRange(name, min, max);
	}
}