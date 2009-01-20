package animata.model;

import java.io.File;
import java.util.ArrayList;

import javax.vecmath.Matrix3d;

import animata.ViewBase;

import processing.core.PApplet;
import processing.core.PImage;
import processing.xml.XMLElement;

public class Layer{
	public class Texture {
		private String location;
		public float x;
		public float y;
		public float scale;
		public PImage image;

		public Texture(XMLElement child, String folder) {
			location = folder + File.separator + child.getStringAttribute("location");
			x = child.getFloatAttribute("x");
			y = child.getFloatAttribute("y");
			scale = child.getFloatAttribute("scale");
		}

		public PImage getImage(PApplet applet) {
			if(image==null) image = applet.loadImage(location);
			return image;
		}
	}

	public ArrayList<Layer>layers = new ArrayList<Layer>();
	public Texture texture;
	public Mesh mesh;
	public Skeleton skeleton;
	public String name = "root";
	public float x = 0;
	public float y = 0;
	public float z = 0;
	public float alpha = 1;
	public float scale = 1;
	public boolean visible = true;

	public Layer() {
	}

	public Layer(XMLElement element, String folder) {
		setupAttributes(element);
		addChildLayersIfPresent(element, folder);
	}

	private void addChildLayersIfPresent(XMLElement element, String folder) {
		XMLElement[] innerLayers = element.getChildren("layer");
		if(innerLayers.length > 0){
			addLayers(innerLayers,folder);
		}else{
			setupLayerContents(element,folder);
		}
	}

	private void setupAttributes(XMLElement element) {
		name = element.getStringAttribute("name");
		x = element.getFloatAttribute("x");
		y = element.getFloatAttribute("y");
		z = - element.getFloatAttribute("z");
		alpha = element.getFloatAttribute("alpha");
		scale = element.getFloatAttribute("scale");
		visible = element.getIntAttribute("vis") == 1;
	}

	private void setupLayerContents(XMLElement element, String folder) {
		texture = new Texture(element.getChild("texture"), folder);
		mesh = new Mesh(element.getChild("mesh"));
		XMLElement skeletonElement = element.getChild("skeleton");
		if(skeletonElement == null) return;
		skeleton = new Skeleton(skeletonElement, mesh);
	}

	public void addLayers(XMLElement[] children, String folder) {
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			layers.add(new Layer(element,folder));
		}
	}

	public void simulate() {
		if(skeleton != null) skeleton.simulate(40);
		for (Layer layer : layers) {
			layer.simulate();
		}

	}


}
