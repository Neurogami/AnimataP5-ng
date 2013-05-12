package animata.model;

import processing.data.XML;

public class Mesh {

    public class Face {

        public Vertex[] vertices = new Vertex[3];

        public Face(XML element, Vertex[] vertices) {
            this.vertices[0] = vertices[element.getInt("v0")];
            this.vertices[1] = vertices[element.getInt("v1")];
            this.vertices[2] = vertices[element.getInt("v2")];
        }
    }

    public class Vertex {

        public float x;
        public float y;
        public float u;
        public float v;
        public int selected;

        public Vertex(XML element) {
            x = element.getFloat("x");
            y = element.getFloat("y");
            u = element.getFloat("u");
            v = element.getFloat("v");
            selected = element.getInt("selected");
        }
    }
    protected Vertex[] vertices;
    public Face[] faces;

    public Mesh(XML child) {
        addVertices(child.getChildren("vertices/vertex"));
        addFaces(child.getChildren("faces/face"));
    }

    private void addVertices(XML[] children) {
        vertices = new Vertex[children.length];
        for (int i = 0; i < children.length; i++) {
            XML element = children[i];
            Vertex vertex = new Vertex(element);
            vertices[i] = vertex;
        }
    }

    private void addFaces(XML[] children) {
        faces = new Face[children.length];
        for (int i = 0; i < children.length; i++) {
            XML element = children[i];
            Face face = new Face(element, vertices);
            faces[i] = face;
        }
    }
}
