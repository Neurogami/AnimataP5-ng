package animata;

import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;
import netP5.NetAddress;
import netP5.NetInfo;

public class Animata {

	public static NetAddress net;
	public static OscP5 oscP5;

	public static void startOSC(PApplet applet) {
		net = new NetAddress(NetInfo.getHostAddress(), 7110);
		oscP5 = new OscP5(applet, 12000);
		PApplet.println("Sending Animata stuff to " + net.address());
	}


	private static boolean checkInitialised() {
		if (net == null) {
			//PApplet.println("need to call init() with valid settings on Animata");
			return false;
		}
		return true;
	}

	public static void zoomCamera(Float delta) {
		if (!checkInitialised()) return;
		OscMessage message = new OscMessage("/cameradeltazoom");
		message.add(delta);
		oscP5.send(message, net);
		System.out.println("message:" + message + " to  " + net);
	}

	public static void panLayer(Float deltaX) {
		if (!checkInitialised()) return;
		OscMessage message = new OscMessage("/cameradeltapan");
		message.add(deltaX);
		message.add(0.0f);
		oscP5.send(message, net);
		System.out.println("message:" + message + " to  " + net);
	}

	public static void setBone(String name, Float n) {
		if (!checkInitialised()) return;
		OscMessage message = new OscMessage("/anibone");
		message.add(name);
		message.add((float) n);
		PApplet.println("trying to send to " + net.address() + " = " + n + " to bone " + name);
		oscP5.send(message, net);
	}

	public static void setAlpha(String layer, float value) {
		if(!checkInitialised()) return;
		OscMessage message = new OscMessage("/layeralpha");
		message.add(layer);
		message.add(value);
		oscP5.send(message, net);
	}

	public static void setBoneTempo(String bone, Float tempo) {
		if(!checkInitialised()) return;
		OscMessage message = new OscMessage("/bonetempo");
		message.add(bone);
		message.add((float) tempo);
		PApplet.println("trying to send to " + net.address() + " = " + tempo + " to bone " + bone);
		oscP5.send(message, net);
	}

}
