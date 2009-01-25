package animata.controls;

import animata.Controller;
import animata.NoteParser;
import processing.xml.XMLElement;
import rwmidi.MidiInput;
import rwmidi.Note;

public class CameraShake extends Control {

	private Integer note;

	public CameraShake(XMLElement element, MidiInput in) {
		super(element,in);
		note = NoteParser.getNote(element.getStringAttribute("note"));
	}
	public void noteOnReceived(Note n){
		if(n.getChannel()!=channel) return;
		if(n.getPitch()!=note) return;
		Controller.getInstance().shakeCamera();
	}
}
