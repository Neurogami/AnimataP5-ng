package animata.model;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.data.XML;
import animata.AnimataP5;
import animata.model.Mesh.Vertex;

public class Skeleton {

    private class AttachedVertex {

        private static final float BONE_MINIMAL_WEIGHT = 0.1f;
        private Vertex vertex;
        private float d;
        private float w;
        private float ca;
        private float sa;
        private float weight;
        private final Bone bone;
        private float dst;

        private AttachedVertex(XML element, Bone bone) {
            this.bone = bone;
            assignAttributes(element);
            setInitialConditions();
        }

        private void setInitialConditions() {
            float x0 = bone.j0.x;
            float y0 = bone.j0.y;
            float x1 = bone.j1.x;
            float y1 = bone.j1.y;
            float alpha = PApplet.atan2(y1 - y0, x1 - x0);
            float dx = (x1 - x0);
            float dy = (y1 - y0);
            float x = x0 + dx * 0.5f;
            float y = y0 + dy * 0.5f;
            float vx = vertex.x;
            float vy = vertex.y;
            float vd = PApplet.sqrt((x - vx) * (x - vx) + (y - vy) * (y - vy));
            dst = vd;
            float vdnorm = vd / (bone.radius * bone.size * .5f);
            if (vdnorm >= 1) {
                weight = BONE_MINIMAL_WEIGHT;
            } else {
                weight = PApplet.pow(1.0f - vdnorm, 1.0f / bone.falloff);
            }
            float a = PApplet.atan2(vy - y, vx - x) - alpha;
            sa = vd * (PApplet.sin(a));
            ca = vd * (PApplet.cos(a));
        }

        private void assignAttributes(XML element) {
            vertex = mesh.vertices[element.getInt("id")];
            d = element.getFloat("d");
            w = element.getFloat("w");
            ca = element.getFloat("ca");
            sa = element.getFloat("sa");
        }
    }

    public class Bone {

        public Joint j0;
        public Joint j1;
        private float scale;
        public float maxScale;
        public float minScale;
        public float tempo;
        private float time;
        private AttachedVertex[] attachedVertices;
        public String name;
        private float stiffness;
        public float size;
        private float radius;
        private float falloff;

        public Bone(XML element) {
            assignAttributes(element);
            setInitialConditions();
            if (element.getChild("attached") != null) {
                addVertices(element.getChildren("attached/vertex"));
            }
        }

        private void setInitialConditions() {
            falloff = 1.0f;
        }

        private void assignAttributes(XML element) {
            name = element.getString("name", "");
            j0 = joints[element.getInt("j0")];
            j1 = joints[element.getInt("j1")];
            stiffness = element.getFloat("stiffness");
            scale = element.getFloat("lm");
            maxScale = element.getFloat("lmmax");
            minScale = element.getFloat("lmmin");
            tempo = element.getFloat("tempo");
            time = element.getFloat("time");
            size = element.getFloat("size");
            radius = element.getFloat("radius");
        }

        private void addVertices(XML[] children) {
            attachedVertices = new AttachedVertex[children.length];
            for (int i = 0; i < children.length; i++) {
                XML element = children[i];
                AttachedVertex attachedVertex = new AttachedVertex(element, this);
                attachedVertices[i] = attachedVertex;
            }

        }

        private void simulate() {
            if (tempo > 0) {
                time += tempo / AnimataP5.timeDivision;	// FIXME
                animateScale(0.5f + PApplet.sin(time) * 0.5f);
            }
            float dx = (j1.x - j0.x);
            float dy = (j1.y - j0.y);
            float dCurrent = PApplet.sqrt(dx * dx + dy * dy);
            dx /= dCurrent;
            dy /= dCurrent;
            float m = ((size * scale) - dCurrent) * stiffness;
            if (!j0.fixed) {
                j0.x -= m * dx;
                j0.y -= m * dy;
            }
            if (!j1.fixed) {
                j1.x += m * dx;
                j1.y += m * dy;
            }
        }

        private void animateScale(float t) {
            scale = minScale + ((maxScale - minScale) * t);
        }

        private void translateVertices() {
            float x0 = j0.x;
            float y0 = j0.y;
            float x1 = j1.x;
            float y1 = j1.y;
            float dx = (x1 - x0);
            float dy = (y1 - y0);
            float x = x0 + dx * 0.5f;
            float y = y0 + dy * 0.5f;
            float dCurrent = PApplet.sqrt(dx * dx + dy * dy);
            dx /= dCurrent;
            dy /= dCurrent;

            if (attachedVertices == null) {
                return;
            }
            for (AttachedVertex v : attachedVertices) {
                float vx = v.vertex.x;
                float vy = v.vertex.y;
                float tx = x + (dx * v.ca - dy * v.sa);
                float ty = y + (dx * v.sa + dy * v.ca);
                vx += (tx - vx) * v.weight;
                vy += (ty - vy) * v.weight;
                v.vertex.x = vx;
                v.vertex.y = vy;
            }
        }
    }

    public class Joint {

        public float x;
        public float y;
        public boolean fixed;
        private boolean selected;
        public String name;

        public Joint(XML element) {
            name = element.getString("name", "");
            x = element.getFloat("x");
            y = element.getFloat("y");
            fixed = element.getInt("fixed") == 1;
            selected = element.getInt("selected") == 1;
        }

        public void simulate() {
            if (!fixed) {
                y += AnimataP5.gravity;
            }
        }
    }
    private Joint[] joints;
    private Bone[] bones;
    private final Mesh mesh;
    public ArrayList<Bone> allBones = new ArrayList<Bone>();
    public ArrayList<Joint> allJoints = new ArrayList<Joint>();

    public Skeleton(XML child, Mesh mesh) {
        this.mesh = mesh;
        addJoints(child.getChildren("joints/joint"));
        addBones(child.getChildren("bones/bone"));
    }

    private void addBones(XML[] children) {
        bones = new Bone[children.length];
        for (int i = 0; i < children.length; i++) {
            XML element = children[i];
            Bone bone = new Bone(element);
            bones[i] = bone;
            allBones.add(bone);
        }
    }

    private void addJoints(XML[] children) {
        joints = new Joint[children.length];
        for (int i = 0; i < children.length; i++) {
            XML element = children[i];
            Joint joint = new Joint(element);
            joints[i] = joint;
            allJoints.add(joint);
        }
    }

    public void simulate(int times) {
        for (int i = 0; i < times; i++) {
            for (Joint joint : joints) {
                joint.simulate();
            }
            for (Bone bone : bones) {
                bone.simulate();
                bone.translateVertices();
            }
        }
    }
}
