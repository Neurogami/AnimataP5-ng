package animata;

import java.util.ArrayList;

import processing.core.PApplet;
import animata.controls.CameraPosition;
import animata.model.Layer;
import animata.model.Skeleton;
import animata.model.Skeleton.Bone;

public class Controller {

	private static Controller instance;
	private final AnimataPlayback animataPlayback;
	public String currentSong;

	public Controller(PApplet applet, Layer root, AnimataPlayback animataPlayback) {
		this.animataPlayback = animataPlayback;
	}

	public boolean cameraDeltaPan(float delta) {
		animataPlayback.panCameraX(delta);
		return true;
	}

	public boolean cameraDeltaZoom(float delta) {
		animataPlayback.zoomCamera(delta);
		return true;
	}

	public boolean setLayerPosition(String layer, float x, float y, float z) {
		return false;
	}

	public boolean setLayerVisibility(String layer, boolean visible) {
		return false;
	}

	public boolean setJoint(String joint, float x, float y, float z) {
		return false;
	}

	public boolean setBoneTempo(String name, Float tempo) {
		ArrayList<Bone> bones = Skeleton.findBones(name);
		for (Bone bone : bones) {
			bone.setTempo(tempo);
		}
		// TODO: extract interface for Animata calls
		Animata.setBoneTempo(name, tempo);
		return true;
	}

	public boolean animateBone(String name, float scale) {
		ArrayList<Bone> bones = Skeleton.findBones(name);
		for (Bone bone : bones) {
			bone.setScale(scale);
		}
		// TODO: extract interface for Animata calls
		Animata.setBone(name, scale);
		return true;
	}

	public static Controller getInstance() {
		return instance;
	}

	public static void init(PApplet applet, Layer root, AnimataPlayback animataPlayback) {
		instance = new Controller(applet,root,animataPlayback);

	}

	public void moveCameraTo(CameraPosition p, int frames) {
		animataPlayback.camera.moveTo(p.x, p.y,p.z, frames);

	}

	public void shakeCamera() {
		animataPlayback.camera.shake();

	}

}
