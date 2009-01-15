package animata.model;

import animata.model.Mesh.Vertex;
import processing.xml.XMLElement;

public class Skeleton {

	public class AttachedVertex {

		private Vertex vertex;
		private float d;
		private float w;
		private float ca;
		private float sa;

		public AttachedVertex(XMLElement element) {
			vertex = mesh.vertices[element.getIntAttribute("id")];
			d = element.getFloatAttribute("d");
			w = element.getFloatAttribute("w");
			ca = element.getFloatAttribute("ca");
			sa = element.getFloatAttribute("sa");

		}

	}

	public class Bone {

		private Joint j0;
		private Joint j1;
		private float stiffness;
		private float length;
		private float maxLength;
		private float minLength;
		private float tempo;
		private float radius;
		private boolean selected;
		private float size;
		private float time;
		private AttachedVertex[] attachedVertices;
		private String name;

		public Bone(XMLElement element) {
			name = element.getStringAttribute("name", "");
			j0 = joints[element.getIntAttribute("j0")];
			j1 = joints[element.getIntAttribute("j1")];
			stiffness = element.getFloatAttribute("stiffness");
			length = element.getFloatAttribute("lm");
			maxLength = element.getFloatAttribute("lmmax");
			minLength = element.getFloatAttribute("lmmin");
			tempo = element.getFloatAttribute("tempo");
			time = element.getFloatAttribute("time");
			size = element.getFloatAttribute("size");
			selected = element.getIntAttribute("selected") == 1;
			radius = element.getFloatAttribute("radius");
			if(element.getChild("attached") != null) {
				addVertices(element.getChildren("attached/vertex"));
			}
		}

		private void addVertices(XMLElement[] children) {
			attachedVertices = new AttachedVertex[children.length];
			for (int i = 0; i < children.length; i++) {
				XMLElement element = children[i];
				AttachedVertex attachedVertex = new AttachedVertex(element);
				attachedVertices[i] = attachedVertex;
			}

		}

	}

	public class Joint {

		private float x;
		private float y;
		private boolean fixed;
		private boolean selected;
		private String name;

		public Joint(XMLElement element) {
			name = element.getStringAttribute("name","");
			x = element.getFloatAttribute("x");
			y = element.getFloatAttribute("y");
			fixed = element.getIntAttribute("fixed") == 1;
			selected = element.getIntAttribute("selected") == 1;
		}

	}

	private Joint[] joints;
	private Bone[] bones;
	private final Mesh mesh;

	public Skeleton(XMLElement child, Mesh mesh) {
		this.mesh = mesh;
		addJoints(child.getChildren("joints/joint"));
		addBones(child.getChildren("bones/bone"));
	}

	private void addBones(XMLElement[] children) {
		bones = new Bone[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			Bone bone = new Bone(element);
			bones[i] = bone;
		}
	}

	private void addJoints(XMLElement[] children) {
		joints = new Joint[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			Joint joint =  new Joint(element);
			joints[i] = joint;
		}

	}

}
