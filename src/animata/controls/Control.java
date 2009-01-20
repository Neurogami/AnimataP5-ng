package animata.controls;

import processing.xml.XMLElement;
import rwmidi.MidiInput;
import animata.Controller;

public class Control {

	protected int channel;
	protected final Controller controller = Controller.getInstance();

	public Control(XMLElement element, MidiInput in) {
		if (in == null) System.out.println("error, MIDI Input Device not supplied!");
		channel = element.getIntAttribute("channel", element.getParent().getIntAttribute("channel")) - 1;
	}
}
