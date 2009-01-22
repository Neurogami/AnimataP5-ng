package animata.controls;

import java.util.Observable;
import java.util.Observer;

import processing.xml.XMLElement;
import rwmidi.MidiInput;
import rwmidi.Note;
import animata.Animator;
import animata.NoteParser;
import animata.NoteParser.BadNoteFormatException;

public class NoteBone extends Control implements Observer {

	private String bone;
	private Integer note;

	private float on;
	private float off;
	private Animator animator;

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
		animator = new animata.Animator(off,this);
		System.out.println("Created notebone for bone " + bone + " note="  +note);
	}
	public NoteBone(MidiInput in, int channel, String bone, float on, float off, int note){
		super(channel, in);
		this.on = on;
		this.off = off;
		this.note = note;
		this.bone = bone;
		// TODO: can I unrepeat this without a silly small method?
		animator = new animata.Animator(off,this);
	}

	public void noteOnReceived(Note n){
		if(n.getChannel() != channel) return;
		if(n.getPitch() != note) return;
		animator.set(on,3);
	}
	public void noteOffReceived(Note n){
		if(n.getChannel() != channel) return;
		if(n.getPitch() != note) return;
		animator.set(off,10);
	}
	public void update(Observable o, Object arg) {
		controller.animateBone(bone, animator.currentValue);
	}

}
