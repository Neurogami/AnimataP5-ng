package animata;

import java.io.File;

import processing.core.PApplet;
import processing.xml.XMLElement;
import rwmidi.MidiInput;
import rwmidi.RWMidi;
import animata.controls.ClockBobber;
import animata.model.Layer;

public class AnimataPlayback {

	public static final float timeDivision = 42f;
	public static float gravity = 0;
	private static boolean debug;
	private  PApplet applet;
	private Layer root;
	private LayerView layersView;
	private Controller controller;
	public Camera camera;
	private MidiInput in;
	public static Clock clock;

	public AnimataPlayback(PApplet applet, String nmtFile){
		setup(applet);
		addScene(nmtFile);
	}
	public AnimataPlayback(PApplet applet){
		setup(applet);
	}
	private void setup(PApplet applet) {
		this.applet = applet;
		Animator.init(applet);
		camera = new Camera(applet);
		this.applet.hint(PApplet.ENABLE_OPENGL_2X_SMOOTH);
		root = new Layer();
		in = RWMidi.getInputDevice("IAC Bus 1 <MIn:0> Apple Computer, Inc.").createInput();
		clock = new Clock("IAC Bus 2");
		new ClockBobber(clock);
		new RandomCameraPanner(clock, this);
		Controller.init(applet,root,this);
		controller = Controller.getInstance();
		new GrimoniumInput(applet,root,controller,7111);
	}
	public void initOSC(int port){
		new OSCInput(applet,root,controller,port);
	}
	/*
	 * defaults to port 7110
	 */
	public void initOSC(){
		new OSCInput(applet,root,controller,7110);
	}


	public void loadSet(String xml){

		XMLElement scene = new XMLElement(applet,xml);
		XMLElement[] layers = scene.getChildren("layer");
		for (int i = 0; i < layers.length; i++) {
			XMLElement element = layers[i];
			Layer layer = addScene(element.getStringAttribute("nmt"));
			layer.visible = true;
			layer.alpha = 1;
			new Scene(element,in,applet,layer);
		}
		layersView = new LayerView(root, applet);
	}

	public Layer addScene(String xml){
		return addScene(xml, root);
	}
	public Layer addScene(String xml, Layer parent){
		String folder = new File(xml).getParent();
		if(folder == null) folder = ".";
		XMLElement element = new XMLElement(applet, xml);
		return parent.addLayer(folder, element);//element.getChildren("layer"), folder);

	}
	public void draw(){
		applet.camera(camera.x, camera.y, camera.z, camera.targetX, camera.targetY,camera.targetZ, 0f, 1f, 0f);
		root.simulate();
		applet.textureMode(PApplet.NORMAL);
		applet.noStroke();
		applet.fill(0,0);
		applet.translate(applet.width/2, applet.height/2);
		layersView.draw();
	}
	public void zoomCamera(float delta) {
		camera.zoomBy(-delta);
	}
	public void panCameraX(float delta) {
		camera.panXBy(delta);
	}
	public void panCameraY(float delta){
		camera.panYBy(delta);
	}
	public void debug() {
		debug = true;

	}
	public static boolean debugging() {
		return debug;
	}

}