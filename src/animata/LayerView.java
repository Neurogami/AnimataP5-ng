package animata;

import java.util.ArrayList;

import processing.core.PApplet;
import animata.model.Layer;
import animata.model.Skeleton.Bone;
import animata.model.Skeleton.Joint;

public class LayerView extends ViewBase {

	private final Layer layer;
	private MeshView mesh;
	private ArrayList<LayerView> layers;

	public LayerView(Layer layer, PApplet applet) {
		super(applet);
		this.layer = layer;
		if(layer.mesh != null) mesh = new MeshView(applet,layer);
		addChildLayers(layer.layers);
	}

	private void addChildLayers(ArrayList<Layer> layers) {
		this.layers = new ArrayList<LayerView>();
		for (Layer layer : layers) {
			this.layers.add(new LayerView(layer, applet));
		}
	}

	public void draw() {
		if(!layer.visible) return;
		applet.pushMatrix();
		doTransformation();
		applet.pushMatrix();
		if(mesh!= null) mesh.draw();
		if(AnimataPlayback.debugging()) drawDebugStuff();
		drawChildLayers();
		applet.popMatrix();

		applet.popMatrix();
	}
	private void drawDebugStuff() {
		if(layer.skeleton == null) return;
		for(Joint joint : layer.skeleton.joints){
			applet.fill(0xFFFFFF00);
			applet.ellipse(joint.x, joint.y, 5, 5);
		}
		applet.stroke(0x99FF00FF);
		applet.strokeWeight(5);
		for(Bone bone : layer.skeleton.bones){
			applet.line(bone.j1.x, bone.j1.y, bone.j0.x, bone.j0.y);
		}
	}

	private void drawChildLayers() {
		for (LayerView layerView : layers) {
			layerView.draw();
		}
	}


	// this is like the calcTransformationMatrix method from the original, but called during draw
	private void doTransformation() {
		applet.translate(layer.x, layer.y,layer.z);
		applet.scale(layer.scale,layer.scale,1);
	}

}
