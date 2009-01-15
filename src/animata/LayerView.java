package animata;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.xml.XMLElement;

public class LayerView extends ViewBase{

	public class Mesh {

		public Mesh(XMLElement child) {
			addVertices(child.getChildren("vertices/vertex"));
			addFaces(child.getChildren("faces/face"));
		}

		private void addVertices(XMLElement[] children) {
			System.out.println("adding " + children.length + " children");

		}

		private void addFaces(XMLElement[] children) {


		}

	}


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

	public LayerView(PApplet applet) {
		super(applet);
	}

	public LayerView(PApplet applet, XMLElement element) {
		super(applet);
		XMLElement[] innerLayers = element.getChildren("layer");
		if(innerLayers.length > 0){
			addLayers(innerLayers);
		}else{
			setupLayer(element);
		}

	}

	private void setupLayer(XMLElement element) {
		texture = new Texture(element.getChild("texture"));
		mesh = new Mesh(element.getChild("mesh"));
	}


	public void addLayers(XMLElement[] children) {
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			layers.add(new LayerView(applet, element));
		}

	}

}
