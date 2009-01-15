package animata;

import java.util.ArrayList;

import oscP5.OscArgument;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;
import animata.model.*;
import animata.model.Skeleton.Bone;

public class Controller {

	private OscP5 oscP5;
	private final PApplet applet;
	private final AnimataPlayback animataPlayback;

	public Controller(PApplet applet, Layer root, AnimataPlayback animataPlayback) {
		this.applet = applet;
		this.animataPlayback = animataPlayback;
		oscP5 = new OscP5(this, 7110);// 12000);

		// NetAddress netAddress = new NetAddress(NetInfo.getHostAddress(),
		// 7110);
	}

	void oscEvent(OscMessage message) {
		boolean result = run(message);
		if (!result) {
			System.out.println("failed to respond to message " + message.addrPattern());
			message.print();
		}
	}

	/*
	 * "/bonetempo") == 0 "/joint") == 0) "/layervis") == 0) "/layeralpha") ==
	 * "/layerpos") == 0) "/layerdeltapos") "/cameradeltazoom"
	 * "/cameradeltapan")
	 */
	private boolean run(OscMessage message) {
		String type = message.addrPattern();
		if (type.equals("/anibone")) return animateBone(message);
		if (type.equals("/bonetempo")) return setBoneTempo(message);
		if (type.equals("/joint")) return setJoint(message);
		if (type.equals("/layervis")) return setLayerVisibility(message);
		if (type.equals("/layerpos")) return setLayerPosition(message);
		if (type.equals("/cameradeltazoom")) return cameraDeltaZoom(message);
		if (type.equals("/cameradeltapan")) return cameraDeltaPan(message);
		System.out.println("no handler defined for type " + type);
		return false;
	}

	private boolean cameraDeltaPan(OscMessage message) {
		return false;
	}

	private boolean cameraDeltaZoom(OscMessage message) {
		float delta  = (Float) message.arguments()[0];
		animataPlayback.setMoveCameraZ(delta);
		return true;
	}

	private boolean setLayerPosition(OscMessage message) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean setLayerVisibility(OscMessage message) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean setJoint(OscMessage message) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean setBoneTempo(OscMessage message) {
		// name, value
		ArrayList<Bone> bones = Skeleton.findBones((String) message.arguments()[0]);
		for (Bone bone : bones) {
			bone.setTempo((Float) message.arguments()[1]);
		}
		return true;
	}

	private boolean animateBone(OscMessage message) {
		ArrayList<Bone> bones = Skeleton.findBones((String) message.arguments()[0]);
		for (Bone bone : bones) {
			bone.setScale((Float) message.arguments()[1]);
		}
		return true;
	}

}
