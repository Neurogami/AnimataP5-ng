package animata;

import processing.core.PApplet;
import animata.model.Layer;
import animata.model.Mesh.Face;
import animata.model.Mesh.Vertex;

public class MeshView {

    private final Layer layer;
    protected final PApplet applet;

    public MeshView(PApplet applet, Layer layer) {
        this.applet = applet;
        this.layer = layer;
    }

    public void draw() {
        applet.noStroke();
        drawFaces(layer.mesh.faces);
    }

    private void drawFaces(Face[] faces) {
        for (int i = 0; i < faces.length; i++) {
            Face face = faces[i];
            drawFace(face.vertices);
        }
    }

    private void drawFace(Vertex[] vertices) {
        applet.beginShape();
        applet.texture(layer.texture.getImage(applet));
        applet.tint(255, 255 - layer.alpha);
        for (int i = 0; i < vertices.length; i++) {
            Vertex vertex = vertices[i];
            applet.vertex(vertex.x, vertex.y, vertex.u, vertex.v);
        }
        applet.endShape();
    }
}
