package animata;

import java.util.ArrayList;

import processing.core.PApplet;
import animata.model.Layer;

public class LayerView {

  protected final PApplet applet;
  private final Layer layer;
  private MeshView mesh;
  private ArrayList<LayerView> layers;

  public LayerView(Layer layer, PApplet applet) {
    this.applet = applet;
    this.layer = layer;
    if (layer.mesh != null) {
      mesh = new MeshView(applet, layer);
    }
    addChildLayers(layer.layers);
  }

  // This seems to work OK under simple conditions but the
  // lack of any way to target a layer is a Bad Idea.
  public void setNewMeshImage(String imageName) {
    System.err.println("LayerView#setNewMeshImage: " + imageName );
    if (mesh != null ) { 
      mesh.setNewImageName(imageName);
    } else {
      System.err.println("LayerView#setNewMeshImage: mesh is null!"  );
      for (Layer llayer : layer.layers) {
        llayer.setNewTextureImage(applet, imageName);
      }
    }
  }

  private void addChildLayers(ArrayList<Layer> layers) {
    this.layers = new ArrayList<LayerView>();
    for (Layer llayer : layers) {
      this.layers.add(new LayerView(llayer, applet));
    }
  }

  public void draw(float x, float y) {
    applet.pushMatrix();
    doTransformation(x, y);
    if (mesh != null) {
      mesh.draw();
    }
    applet.popMatrix();
    drawChildLayers(x, y);
  }

  private void drawChildLayers(float x, float y) {
    for (LayerView layerView : layers) {
      layerView.draw(x, y);
    }
  }

  private void doTransformation(float _x, float _y) {
    applet.translate(layer.x + _x, layer.y + _y, layer.z);
    applet.scale(layer.scale, layer.scale, 1);
  }
}
