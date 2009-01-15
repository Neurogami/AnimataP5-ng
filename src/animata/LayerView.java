package animata;

import java.util.ArrayList;

import animata.model.Mesh;
import animata.model.Skeleton;

import processing.core.PApplet;
import processing.xml.XMLElement;

public class LayerView extends ViewBase{



	public class Texture {

		private String location;
		private float x;
		private float y;
		private float scale;

		public Texture(XMLElement child) {
			location = child.getStringAttribute("location");
			x = child.getFloatAttribute("x");
			y = child.getFloatAttribute("y");
			scale = child.getFloatAttribute("scale");

		}

	}


	private ArrayList<LayerView>layers = new ArrayList<LayerView>();
	private Texture texture;
	private Mesh mesh;
	private Skeleton skeleton;
	private String name;
	private float x;
	private float y;
	private float z;
	private float alpha;
	private float scale;
	private boolean visible;

	public LayerView(PApplet applet) {
		super(applet);
	}

	public LayerView(PApplet applet, XMLElement element) {
		super(applet);

		setupAttributes(element);

		XMLElement[] innerLayers = element.getChildren("layer");
		if(innerLayers.length > 0){
			addLayers(innerLayers);
		}else{
			setupLayerContents(element);
		}

	}

	private void setupAttributes(XMLElement element) {
		name = element.getStringAttribute("name");
		x = element.getFloatAttribute("x");
		y = element.getFloatAttribute("y");
		z = element.getFloatAttribute("z");
		alpha = element.getFloatAttribute("alpha");
		scale = element.getFloatAttribute("scale");
		visible = element.getIntAttribute("vis") == 1;

	}

	private void setupLayerContents(XMLElement element) {

		texture = new Texture(element.getChild("texture"));
		mesh = new Mesh(element.getChild("mesh"));
		skeleton = new Skeleton(element.getChild("skeleton"), mesh);
	}


	public void addLayers(XMLElement[] children) {
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			layers.add(new LayerView(applet, element));
		}

	}

}
