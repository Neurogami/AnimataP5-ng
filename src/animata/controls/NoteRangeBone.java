package animata.controls;

import animata.Controller;
import animata.NoteParser;
import animata.NoteParser.BadNoteFormatException;
import processing.xml.XMLElement;
import rwmidi.MidiInput;
import rwmidi.Note;

public class NoteRangeBone extends Control {

	private String bone;
	private float range;
	private int low;
	private int high;

	public NoteRangeBone(XMLElement element, MidiInput in) {
		super(element, in);
		try {
			low = NoteParser.getNote(element.getStringAttribute("low", "1"));
			high = NoteParser.getNote(element.getStringAttribute("high", "100"));
		} catch (BadNoteFormatException e) {
			System.out.println(e.getMessage());
		}
		bone = element.getStringAttribute("bone");
		range = (float) high - low;
	}
	public void noteOnReceived(Note n){
		if(n.getChannel() != channel) return;
		int pitch = n.getPitch();
		if(pitch < low ) return;
		if(pitch > high) return;
		float length = 1f - ((float)((pitch - low)) / range);
		Controller.getInstance().animateBone(bone,  length);
	}
}
