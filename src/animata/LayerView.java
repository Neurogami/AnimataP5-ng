package animata;

import java.util.ArrayList;

import processing.core.PApplet;
import animata.model.Layer;

public class LayerView extends ViewBase {

	public final Layer layer;
	private MeshView mesh;
	public ArrayList<LayerView> layers;

	public LayerView(Layer layer, PApplet applet) {
		super(applet);
		this.layer = layer;
		if(layer.mesh != null) mesh = new MeshView(applet,layer);
		addChildLayers(layer.layers);
	}

	private void addChildLayers(ArrayList<Layer> layers) {
		this.layers = new ArrayList<LayerView>();
		for (Layer llayer : layers) {
			this.layers.add(new LayerView(llayer, applet));
		}
	}


	public void draw(float x,float y) {
		applet.pushMatrix();
		doTransformation(x,y);
		if(mesh!= null) mesh.draw();
        applet.popMatrix();
		drawChildLayers(x,y);
	}

	private void drawChildLayers(float x,float y) {
		for (LayerView layerView : layers) {
			layerView.draw(x,y);
		}
	}

	// this is like the calcTransformationMatrix method from the original, but called during draw
	private void doTransformation(float x,float y) {
		applet.translate(x,y,layer.z);
		applet.scale(layer.scale,layer.scale,1);
	}
}
