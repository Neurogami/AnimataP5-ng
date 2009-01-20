package animata.controls;

import processing.xml.XMLElement;
import rwmidi.MidiInput;
import rwmidi.Note;
import animata.NoteParser;
import animata.NoteParser.BadNoteFormatException;

public class NoteBone extends Control {

	private String bone;
	private Integer note;

	private float on;
	private float off;

	public NoteBone(XMLElement element, MidiInput in) {
		super(element, in);
		bone = element.getStringAttribute("bone");
		on = element.getFloatAttribute("on",0);
		off = element.getFloatAttribute("off",1f);
		try {
			note = NoteParser.getNote(element.getStringAttribute("note"));
		} catch (BadNoteFormatException e) {
			e.printStackTrace();
		}

		in.plug(this);
		System.out.println("Created notebone for bone " + bone + " note="  +note);
	}
	public void noteOnReceived(Note n){
		if(n.getChannel() != channel) return;
		if(n.getPitch() != note) return;
		controller.animateBone(bone, on);
	}
	public void noteOffReceived(Note n){
		if(n.getChannel() != channel) return;
		if(n.getPitch() != note) return;
		controller.animateBone(bone, off);
	}

}
