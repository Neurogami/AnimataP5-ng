package animata.model;

import processing.xml.XMLElement;

public class Mesh {

    public class Face {

        public Vertex[] vertices = new Vertex[3];

        public Face(XMLElement element, Vertex[] vertices) {
            this.vertices[0] = vertices[element.getIntAttribute("v0")];
            this.vertices[1] = vertices[element.getIntAttribute("v1")];
            this.vertices[2] = vertices[element.getIntAttribute("v2")];
        }
    }

    public class Vertex {

        public float x;
        public float y;
        public float u;
        public float v;
        public int selected;

        public Vertex(XMLElement element) {
            x = element.getFloatAttribute("x");
            y = element.getFloatAttribute("y");
            u = element.getFloatAttribute("u");
            v = element.getFloatAttribute("v");
            selected = element.getIntAttribute("selected");
        }
    }
    protected Vertex[] vertices;
    public Face[] faces;

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
            Face face = new Face(element, vertices);
            faces[i] = face;
        }
    }
}
