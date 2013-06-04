package animata;

import processing.core.PApplet;
import processing.core.PImage;

import animata.model.Layer;
import animata.model.Mesh.Face;
import animata.model.Mesh.Vertex;

public class MeshView {

  private final Layer layer;
  protected final PApplet applet;
  private String newImageName = null;

  public MeshView(PApplet applet, Layer layer) {
    this.applet = applet;
    this.layer = layer;
  }

  public void draw() {
    applet.noStroke();
    drawFaces(layer.mesh.faces);
  }


  public void setNewImageName(String imageName) {
    if (this.applet != null ) {
      this.layer.texture.loadImage(this.applet, imageName);
    } else {
      System.err.println("MeshView#setNewImageName: " + imageName + ": this.applet  is null!" );
    }
  }

  private void drawFaces(Face[] faces) {
    layer.texture.updateCurrentImage(); 
    
    for (int i = 0; i < faces.length; i++) {
      Face face = faces[i];
      drawFace(face.vertices);
    }
  }

  private void drawFace(Vertex[] vertices) {

    applet.pushMatrix();
    applet.scale(layer.texture.scale() );
    applet.translate(layer.texture.x(), layer.texture.y() );   
    applet.beginShape();
    applet.texture(layer.texture.getImage(applet));

    // This works, techincally, but it is updating the frame count on each face,
    // so the iamge changes before the whole texture is rendered.
//    applet.texture(layer.texture.getCurrentImage(applet));
   // Animata store alpha as a float. 
   // Note that an alpha value of 1 means it is visible. A value of 0 means transparent.
    applet.tint(255, 255 - applet.map(layer.alpha, 0,1, 255, 0)  );
    for (int i = 0; i < vertices.length; i++) {
      Vertex vertex = vertices[i];
      applet.vertex(vertex.x, vertex.y, vertex.u, vertex.v);
    }
    applet.endShape();
    applet.popMatrix();
  }
}
