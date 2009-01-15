package animata;

import processing.core.PApplet;
import processing.xml.XMLElement;
import animata.model.Layer;

public class AnimataPlayback {
	public class Camera {
		public float x;
		public float y;
		public float z;
		public float targetX;
		public float targetY;
		public float targetZ;
		public Camera(PApplet applet) {
			x =  applet.width/2.0f;
			y = applet.height/2.0f;
			z = 1500;//(applet.height/2.0f) / PApplet.tan(PApplet.PI*60.0f / 360.0f);
			targetX = applet.width/2.0f;
			targetY = applet.height/2.0f;
			targetZ = 0f;
		}
		public void zoomBy(float delta) {
			z+=delta;
			targetZ += delta;

		}

	}
	public static final float timeDivision = 42f;
	public static float gravity = 0;
	private final PApplet applet;
	private Layer root;
	private LayerView layersView;
	private Controller controller;
	private Camera camera;

	public AnimataPlayback(PApplet applet){
		this.applet = applet;
		camera = new Camera(applet);
		this.applet.hint(PApplet.ENABLE_OPENGL_2X_SMOOTH);
		root = new Layer();
		controller = new Controller(applet,root,this);

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
		applet.camera(camera.x, camera.y, camera.z, camera.targetX, camera.targetY,camera.targetZ, 0f, 1f, 0f);
		root.simulate();
		applet.textureMode(PApplet.NORMAL);
		applet.noStroke();
		applet.fill(0,0);
		applet.translate(applet.width/2, applet.height/2);
		layersView.draw();
	}
	public void setMoveCameraZ(float delta) {
		camera.zoomBy(delta);

	}

}