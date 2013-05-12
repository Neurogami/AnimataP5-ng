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

        private String location;
        private float x;
        private float y;
        private float scale;
        private PImage image;

        public Texture(XML child, String folder) {
            location = folder + File.separator + child.getString("location");
            x = child.getFloat("x");
            y = child.getFloat("y");
            scale = child.getFloat("scale");
        }

        public PImage getImage(PApplet applet) {
            if (image == null) {
                image = applet.loadImage(location);
            }
            return image;
        }
    }
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
        alpha = element.getFloat("alpha", 255);
        scale = element.getFloat("scale", 1);
        visible = element.getInt("vis") == 1;
    }

    private void setupLayerContents(XML element, String folder) {
        System.err.println("setupLayerContents has folder '" + folder + "' and element " + element );
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
        System.err.println("addLayer has folder '" + folder + "' and element " + element );
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
