package animata;

import java.io.File;
import java.io.IOException;
import processing.core.PApplet;
import processing.data.XML;
import animata.model.Layer;

import animata.model.Skeleton.Joint;
import animata.model.Skeleton.Bone;

public class AnimataP5 {

  public static final float timeDivision = 42f;
  public static float gravity = 0;
  private PApplet applet;
  public Layer root;
  public LayerView layersView;

  public AnimataP5(PApplet applet, String nmtFileName) {
    setup(applet);
    addScene(nmtFileName);
  }

  private void setup(PApplet applet) {
    this.applet = applet;
    root = new Layer();
  }

  private void addScene(String xmlFileName) {
    String folder = new File(xmlFileName).getParent();
    
    if (folder == null) { folder = applet.sketchPath + "/data";   }

    try {
      XML element = new XML(new File(folder + "/" + xmlFileName));
      layersView = new LayerView(root.addLayer(folder, element), applet);
    } 

    catch(IOException ioe) {
      System.err.println("IO exception opening file '" + xmlFileName + "': " + ioe );
    }
    catch(org.xml.sax.SAXException saxe) {
      System.err.println("SAX exception opening file '" + xmlFileName + "': " + saxe );
    }
    catch(javax.xml.parsers.ParserConfigurationException pce ) {
      System.err.println("Parser configuration exception opening file '" + xmlFileName + "': " + pce  );
    }
  }

  public void draw(float x, float y) {
    root.simulate();
    applet.textureMode(PApplet.NORMAL);
    applet.noStroke();
    layersView.draw(x, y);
  }

  public Joint getJoint(String name) {
    return root.getJoint(name);
  }

  public Layer getLayer(String name) {
        System.err.println("AnimataP5#getLayer:  Look for " + name + ". root name = " + root.name  );  // DEBUGGERY
    if (root.name.equals(name) ) {
     return root;
    }

    return layersView.getLayer(name);
  }

  public Bone getBone(String name) {
  System.err.println("AnimataP5#getBone: " + name );  // DEBUGGERY
    return root.getBone(name);
  }


  public void setNewMeshImage(String imageName, String layerName) {
   layersView.setNewMeshImage(imageName, layerName);
  }

  public void moveJointY(String name, float value) {
    root.moveJointY(name, value);
  }

  public void moveJointX(String name, float value) {
    root.moveJointX(name, value);
  }

  public void moveJoint(String name, float x,  float y) {
//    System.err.println("moveJoint: " + name + " -> " + x + ", " + y);  // DEBUGGERY
    root.moveJoint(name, x, y);
  }

  public void toggleJointFixed(String name) {
    root.toggleJointFixed(name);
  }

  public void setJointFixed(String name, boolean b) {
    root.setJointFixed(name, b);
  }

  public void setLayerAlpha(String name, float value) {
    if (value > 255) {
      value = 255;
    }
    if (value < 0) {
      value = 0;
    }
    root.setLayerAlpha(name, value);
  }

  public void setLayerScale(String name, float value) {
    root.setLayerScale(name, value);
  }

  public void setLayerPos(String name, float x, float y) {
    root.setLayerPos(name, x, y);
  }

  public void setBoneTempo(String name, float value) {
    // add constraint on values
    root.setBoneTempo(name, value);
  }

  public void setBoneRange(String name, float min, float max) {
    // add constraint on values
    root.setBoneRange(name, min, max);
  }
}
