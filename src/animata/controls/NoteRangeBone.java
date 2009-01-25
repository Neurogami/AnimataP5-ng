package animata.controls;

import java.util.Observable;
import java.util.Observer;

import animata.Animator;
import animata.Controller;
import animata.NoteParser;
import animata.NoteParser.BadNoteFormatException;
import processing.xml.XMLElement;
import rwmidi.MidiInput;
import rwmidi.Note;

public class NoteRangeBone extends Control implements Observer {

	private String bone;
	private float range;
	private int low;
	private int high;
	private Animator animator;

	public NoteRangeBone(XMLElement element, MidiInput in) {
		super(element, in);
		low = NoteParser.getNote(element.getStringAttribute("low", "1"));
		high = NoteParser.getNote(element.getStringAttribute("high", "100"));

		bone = element.getStringAttribute("bone");
		range = (float) high - low;
		animator = new animata.Animator(low, this);
	}

	public void noteOnReceived(Note n) {
		if (n.getChannel() != channel) return;
		int pitch = n.getPitch();
		if (pitch < low) return;
		if (pitch > high) return;
		float length = 1f - ((float) ((pitch - low)) / range);
		animator.set(length, 4);
	}

	public void update(Observable o, Object arg) {
		Controller.getInstance().animateBone(bone, animator.currentValue);

	}
}
