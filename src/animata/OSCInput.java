package animata;

import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;
import animata.model.Layer;

public class OSCInput {
	private Controller controller;

	public OSCInput(PApplet applet, Layer root, Controller controller, int port) {
		this.controller = controller;
		new OscP5(this, port);

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
		float delta  = (Float) message.arguments()[0];
		controller.cameraDeltaPan(delta);
		return true;
	}

	private boolean cameraDeltaZoom(OscMessage message) {
		float delta  = (Float) message.arguments()[0];
		controller.cameraDeltaZoom(delta);
		return true;
	}
	private boolean setLayerPosition(OscMessage message) {
		return false;
	}

	private boolean setLayerVisibility(OscMessage message) {
		return false;
	}

	private boolean setJoint(OscMessage message) {
		return false;
	}

	private boolean setBoneTempo(OscMessage message) {
		// name, value
		controller.setBoneTempo((String)message.arguments()[0], (Float) message.arguments()[1]);
		return true;
	}

	private boolean animateBone(OscMessage message) {
		controller.animateBone((String) message.arguments()[0], (Float) message.arguments()[1]);
		return true;
	}
}
