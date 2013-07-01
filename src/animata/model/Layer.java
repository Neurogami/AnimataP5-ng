package animata.model;

import java.io.File;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.XML;

import animata.model.Skeleton.Joint;
import animata.model.Skeleton.Bone;

public class Layer {

  public class Texture {

    public ArrayList spriteImages = null;
    public int spriteUpdateOnFrameCount = 1;

    private int spriteIndex = 0;
    private String location;
    private float x;
    private float y;
    private float scale;
    private PImage image;

    private XML textureElement;
    private String folder;
    private int drawFrameCount = 0;

    public Texture(XML child, String _folder) {
      textureElement = child;
      folder = _folder;
      location = folder + File.separator + textureElement.getString("location");
      x = textureElement.getFloat("x");
      y = textureElement.getFloat("y");
      scale = textureElement.getFloat("scale");
    }



    // We use this to grab the current sprite image, if that is in use
    //
    public void updateCurrentImage() {
      if (spriteImages == null ){
        return;
      }

      drawFrameCount++;
      drawFrameCount %= spriteUpdateOnFrameCount;

      if (drawFrameCount == 0 ) {
        spriteIndex++;
        spriteIndex %= spriteImages.size();
      }

      image = (PImage) spriteImages.get(spriteIndex);

    }


    public PImage getImage(PApplet applet) {
      if (image == null) {
        image = applet.loadImage(location);
      }
      return image;
    }

    public float x() {
      return x;
    }

    public float y() {
      return y;
    }

    public float scale() {
      return scale;
    }

    public void loadImage(PApplet applet, String imageName) {
      location = folder + File.separator + imageName;
      image = applet.loadImage(location);
    }

    public void setImage(PApplet applet, PImage _image) {
      image = _image;
    }

  } // End of Texture class

  public ArrayList<Layer> layers = new ArrayList<Layer>();
  public Texture texture;
  public Mesh mesh;
  private Skeleton skeleton;
  public String name = "root";
  public float x = 0;
  public float y = 0;
  public float z = 0;
  public float alpha = 1;
  public float scale = 1;
  public boolean visible = true;

  public Layer() {
  }

  public Layer(XML element, String folder) {
    setupAttributes(element);
    addChildLayersIfPresent(element, folder);
  }


  public void setNewTexturePImage(PApplet applet, PImage image, String layerName) {
    System.err.println(":: Layer#setNewMeshPImage. My name = " + name + "; looking for " + layerName ); // DEBUGGERY
    if (name.equals(layerName) ) {
      if (texture != null ) { 
        System.err.println(":: Setting texture ! LayerView#setNewMeshPImage"); // DEBUGGERY
        texture.setImage(applet, image);
      } 
    }else {
      for (Layer ll :layers) { ll.setNewTexturePImage(applet, image, layerName); }
    }
  }


  public void setNewTextureImage(PApplet applet, String imageName, String layerName) {
    if (name.equals(layerName) ) {
      if (texture != null ) { 
        texture.loadImage(applet, imageName);
      } else {
        // System.err.println("Layer#setNewTextureImage: texture is null!"  );
      }
    }
  }

  public boolean visible() {
    return visible;
  }

  public void setVisibility(boolean b) {
    visible = b;
  }


  public void setPosition(float _x, float _y) {
    x = _x;
    y = _y;
  }

  public  String name(){
    return name;
  }

  public float x() {
    return x;
  }

  public float y() {
    return y;
  }

  public float z(){
    return z;
  }

  public float scale() {
    return scale();
  }


  public void x(float f){
    x = f;
  }

  public void y(float f) {
    y = f;
  }

  public void z(float f){
    z = f;
  }

  public void scale(float f) {
    scale = f;
  }

  private void addChildLayersIfPresent(XML element, String folder) {
    XML[] innerLayers = element.getChildren("layer");
    if (innerLayers.length > 0) {
      addLayers(innerLayers, folder);
    } else {
      setupLayerContents(element, folder);
    }
  }

  private void setupAttributes(XML element) {
    name = element.getString("name","null");

    x = element.getFloat("x");
    y = element.getFloat("y");
    z = -element.getFloat("z");
    alpha = element.getFloat("alpha", 1);
    scale = element.getFloat("scale", 1);
    visible = element.getInt("vis") == 1;
  }

  private void setupLayerContents(XML element, String folder) {
    texture = new Texture(element.getChild("texture"), folder);
    mesh = new Mesh(element.getChild("mesh"));
    XML skeletonElement = element.getChild("skeleton");
    if (skeletonElement == null) {
      return;
    }
    skeleton = new Skeleton(skeletonElement, mesh);
  }

  private void addLayers(XML[] children, String folder) {
    for (int i = 0; i < children.length; i++) {
      XML element = children[i];
      addLayer(folder, element);
    }
  }

  public Layer addLayer(String folder, XML element) {
    Layer layer = new Layer(element, folder);
    layers.add(layer);
    return layer;
  }

  public void simulate() {
    if (skeleton != null) {
      skeleton.simulate(40);
    }
    for (Layer layer : layers) {
      layer.simulate();
    }
  }

  public Layer getLayer(String name) {
    Layer l = null;

    for (Layer llayer : layers) {
      if (llayer.name.equals(name) ) {
        return llayer;
      }
    }
    return l;
  }

  public Bone getBone(String name) {
    Bone b = null;
    if (skeleton != null ) {
      return skeleton.getBone(name);
    } else {
      for (Layer llayer : layers) {
        b = llayer.getBone(name);
        if (b != null ) {
          return b;
        }
      }
    }
    return b;

  }


  public Joint getJoint(String name) {
    Joint j = null;

    if (skeleton != null) {
      for (Joint joint : skeleton.allJoints) {
        if (joint.name.equals(name)) {
          return joint; 
        }
      }
    }

    for (Layer llayer : layers) {
      j = llayer.getJoint(name);
      if (j != null) {
        return j;
      }
    }

    return j;
  }


  public void moveJointX(String name, float x) {
    if (skeleton != null) {
      for (Joint joint : skeleton.allJoints) {
        if (joint.name.equals(name)) {
          joint.x = x;
        }
      }
    }
    for (Layer llayer : layers) {
      llayer.moveJointX(name, x);
    }
  }

  public void moveJointY(String name, float y) {
    if (skeleton != null) {
      for (Joint joint : skeleton.allJoints) {
        if (joint.name.equals(name)) {
          joint.y = y;
        }
      }
    }
    for (Layer llayer : layers) {
      llayer.moveJointY(name, y);
    }
  }

  public void moveJoint(String name, float x, float y) {
    if (skeleton != null) {
      for (Joint joint : skeleton.allJoints) {
        if (joint.name.equals(name)) {
          joint.x = x;
          joint.y = y;
          return;
        }
      }
    }

    for (Layer llayer : layers) {
      llayer.moveJoint(name, x, y);
    }
  }


  public void moveJointOnLayer(String jointName, String layerName, float x,  float y) {
    if (skeleton != null) {
      if ( name.equals(layerName) ) {
        for (Joint joint : skeleton.allJoints) {
          if (joint.name.equals(jointName)) {
            joint.x = x;
            joint.y = y;
            return;
          }
        }
      }
    }

    for (Layer llayer : layers) {
      llayer.moveJointOnLayer(jointName, layerName, x, y);
    }
  }


  public void toggleJointFixed(String name) {
    if (skeleton != null) {
      for (Joint joint : skeleton.allJoints) {
        if (joint.name.equals(name)) {
          joint.toggleFixed();
        }
      }
    }

    for (Layer llayer : layers) {
      llayer.toggleJointFixed(name);
    }
  }


  public void setJointFixed(String name, boolean b) {
    if (skeleton != null) {
      for (Joint joint : skeleton.allJoints) {
        if (joint.name.equals(name)) {
          joint.setFixed(b);
        }
      }
    }

    for (Layer llayer : layers) {
      llayer.setJointFixed(name, b);
    }
  }

  // A decisoin needs to be made, and used consistently
  // C Animata uses a float, from 0.0 to 1.0, to indicate alpha.
  // P5 uses a value from 0 to 255.
  // The public API for this library needs to be consistent.
  //
  // If it reads in an XML value for alpha it needs to be able to
  // handle alpha asa float.  So, the API should assume a 0-1 float
  // is being used.  Someplace in all this that float must be 
  // mapped to P5's 0-255 range.
  public void setLayerAlpha(String _name, float a) {

    if (this != null) {
      if (this.name.equals(_name)) {
        this.alpha = a;
      }
    }

    for (Layer llayer : layers) {
      llayer.setLayerAlpha(_name, a);
    }
  }

  public void setLayerScale(String _name, float s) {
    if (this != null) {
      if (this.name.equals(_name)) {
        this.scale = s;
      }
    }
    for (Layer llayer : layers) {
      llayer.setLayerScale(_name, s);
    }
  }

  public void setLayerPos(String _name, float _x, float _y) {
    if (this != null) {
      if (this.name.equals(_name)) {
        x = _x;
        y = _y;
      }
    }
    for (Layer llayer : layers) {
      llayer.setLayerPos(_name, _x, _y);
    }
  }

  public void setBoneTempo(String name, float t) {
    if (skeleton != null) {
      for (Bone bone : skeleton.allBones) {
        if (bone.name.equals(name)) {
          bone.tempo = t;
        }
      }
    }
    for (Layer llayer : layers) {
      llayer.setBoneTempo(name, t);
    }
  }

  public void setBoneRange(String name, float min, float max) {
    if (skeleton != null) {
      for (Bone bone : skeleton.allBones) {
        if (bone.name.equals(name)) {
          bone.minScale = min;
          bone.maxScale = max;
        }
      }
    }
    for (Layer llayer : layers) {
      llayer.setBoneRange(name, min, max);
    }
  }

}
