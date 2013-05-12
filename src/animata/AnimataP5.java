package animata;

import java.io.File;
import java.io.IOException;
import processing.core.PApplet;
import processing.data.XML;
import animata.model.Layer;

public class AnimataP5 {

    public static final float timeDivision = 42f;
    public static float gravity = 0;
    private PApplet applet;
    public Layer root;
    public LayerView layersView;

    public AnimataP5(PApplet applet, String nmtFile) {
        setup(applet);
        addScene(nmtFile);
    }

    private void setup(PApplet applet) {
        this.applet = applet;
        root = new Layer();
    }

    private void addScene(String xml) {
        String folder = new File(xml).getParent();
        if (folder == null) {
            folder = ".";
        }
        try {
          // XML element = new XML(applet, xml);
          XML element = new XML(xml);
          layersView = new LayerView(root.addLayer(folder, element), applet);
        } 
        
 //       catch(IOException e) {
   //       // ??
     //   }
        catch(javax.xml.parsers.ParserConfigurationException e2) {
        // ??
        }

       // catch(org.xml.sax.SAXException e3) {
       // // ???
        //}
  }

    public void draw(float x, float y) {
        root.simulate();
        applet.textureMode(PApplet.NORMAL);
        applet.noStroke();
        layersView.draw(x, y);
    }

    public void moveJointY(String name, float value) {
        root.moveJointY(name, value);
    }

    public void moveJointX(String name, float value) {
        root.moveJointX(name, value);
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
