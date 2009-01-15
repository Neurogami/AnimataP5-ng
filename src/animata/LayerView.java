package animata;

import java.util.ArrayList;

import processing.core.PApplet;
import animata.model.Layer;

public class LayerView extends ViewBase {

	private final Layer layer;
	private MeshView mesh;
	private SkeletonView skeleton;
	private ArrayList<LayerView> layers;

	public LayerView(Layer layer, PApplet applet) {
		super(applet);
		this.layer = layer;
		if(layer.mesh != null) mesh = new MeshView(applet,layer);
		if(layer.skeleton != null) skeleton = new SkeletonView(applet,layer.skeleton);
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
		drawMesh();
		drawSkeleton();
		applet.popMatrix();

		drawChildLayers();
		applet.popMatrix();
	}
	private void drawChildLayers() {
		for (LayerView layerView : layers) {
			layerView.draw();
		}

	}

	private void drawMesh() {
		if(mesh!= null) mesh.draw();
	}
	private void drawSkeleton() {

	}


	// this is like the calcTransformationMatrix method from the original, but called during draw
	private void doTransformation() {
		applet.scale(layer.scale,layer.scale,1);
		applet.translate(layer.x, layer.y,layer.z);
	}

}
