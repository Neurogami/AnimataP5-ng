package animata;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
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

  public Layer getLayer(String layerName) {

    Layer l = null;
    // This is not correctly checking sub-layers of layers, etc.
    // It needs to recurse, and it needs to know when return a value.
    for (Layer ll : layer.layers) {
      if (ll.name.equals(layerName)) {
        return ll;
      } else {
        l = ll.getLayer(layerName);
      }
    }
    return l;
  }

 public void setNewMeshImage(PImage layerImage, String layerName ) {
    for (Layer llayer : layer.layers) {
      llayer.setNewTextureImage(applet, layerImage, layerName);
    }
  }


  public void setNewMeshImage(String imageName, String layerName ) {
    for (Layer llayer : layer.layers) {
      llayer.setNewTextureImage(applet, imageName, layerName);
    }
  }

  private void addChildLayers(ArrayList<Layer> layers) {
    this.layers = new ArrayList<LayerView>();
    for (Layer llayer : layers) {
      this.layers.add(new LayerView(llayer, applet));
    }
  }

  public void draw(float x, float y) {

    x = x+layer.x();
    y = y+layer.y();

    applet.pushMatrix();
    doTransformation(x, y);
    if (mesh != null) {
      mesh.draw();
    }
    drawChildLayers(x, y); // This used to come AFTER popMatrix, but it seems that the parent layer needs to pass on it's own scaling and such
    applet.popMatrix();

  }

  private void drawChildLayers(float x, float y) {
    for (LayerView layerView : layers) {
      if (layerView.layer.visible() ) { 
        // Each call to a layer draw does a scale.
        // However, there's no parent scale being passed on
        // That seems to be a bug. I C Animata, if you scale the owner layer
        // then the child layers are scaled as well.
        layerView.draw(x, y);
      }
    }
  }

  private void doTransformation(float _x, float _y) {
    //applet.translate(layer.x + _x, layer.y + _y, layer.z);
    // This, with the changes in draw, seems to work better
    applet.translate( _x, _y, layer.z);
    applet.scale(layer.scale, layer.scale, 1);
  }
}
