package animata.model;

import processing.xml.XMLElement;

public class Mesh {
	public class Face {
		private Vertex v0;
		private Vertex v1;
		private Vertex v2;

		public Face(XMLElement element) {
			v0 = vertices[element.getIntAttribute("v0")];
			v1 = vertices[element.getIntAttribute("v1")];
			v2 = vertices[element.getIntAttribute("v2")];
		}
	}

	public class Vertex {

		private float x;
		private float y;
		private float u;
		private float v;
		private int selected;
		public Vertex(XMLElement element) {
			x = element.getFloatAttribute("x");
			y = element.getFloatAttribute("y");
			u = element.getFloatAttribute("u");
			v = element.getFloatAttribute("v");
			selected = element.getIntAttribute("selected");
		}
	}

	protected Vertex[] vertices;
	protected Face[] faces;

	public Mesh(XMLElement child) {
		addVertices(child.getChildren("vertices/vertex"));
		addFaces(child.getChildren("faces/face"));
	}

	private void addVertices(XMLElement[] children) {
		vertices = new Vertex[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			Vertex vertex = new Vertex(element);
			vertices[i] = vertex;
		}
	}

	private void addFaces(XMLElement[] children) {
		faces = new Face[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			Face face = new Face(element);
			faces[i] = face;
		}
	}


}
