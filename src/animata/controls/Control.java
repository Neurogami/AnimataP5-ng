package animata.controls;

import processing.xml.XMLElement;
import rwmidi.MidiInput;
import animata.Controller;

public class Control {

	protected int channel;
	protected final Controller controller = Controller.getInstance();
	protected MidiInput in;

	public Control(XMLElement element, MidiInput in) {
		init(element.getIntAttribute("channel", element.getParent().getIntAttribute("channel")) - 1,in);
	}
	public Control(int channel, MidiInput in){
		init(channel, in);
	}
	private void init(int channel, MidiInput in) {
		if (in == null) System.out.println("error, MIDI Input Device not supplied!");
		this.channel = channel;
		this.in = in;
		in.plug(this);
	}
}
