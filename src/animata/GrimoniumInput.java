package animata;

import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;
import animata.model.Layer;

public class GrimoniumInput {
	private Controller controller;

	public GrimoniumInput(PApplet applet, Layer root, Controller controller, int port) {
		this.controller = controller;
		new OscP5(this, port);

	}
	public void oscEvent(OscMessage message) {
		boolean result = run(message);
		if (!result) {
			System.out.println("failed to respond to message " + message.addrPattern());
			message.print();
		}
	}
	private boolean run(OscMessage message) {
		String type = message.addrPattern();
		if(type.equals("/visualschanged")) return visualsChanged(message);
		return false;
	}
	private boolean visualsChanged(OscMessage message) {
		controller.currentSong = (String) message.arguments()[0];
		RandomCameraPanner.moveNow();
		return true;
	}
}
