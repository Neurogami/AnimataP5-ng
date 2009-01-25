package animata.controls;

import processing.xml.XMLElement;
import rwmidi.MidiInput;
import animata.NoteParser;
import animata.NoteParser.BadNoteFormatException;

public class BoneTempoKeyRanges extends Control {
	private Integer low;
	private int bonecount;
	private float tempo;
	private BoneTempoKeys[] ranges;
	private NoteBone[] triggers;
	private String boneRoot;
	private int step;
	public BoneTempoKeyRanges(XMLElement element, MidiInput in) {
		super(element, in);
		low = NoteParser.getNote(element.getStringAttribute("low", "1"));
		channel = element.getIntAttribute("channel", 16) - 1;
		step = element.getIntAttribute("step");
		boneRoot = element.getStringAttribute("bone");
		tempo = element.getFloatAttribute("tempo", 1);
		bonecount = element.getIntAttribute("bonecount",1);
		addRanges();
	}

	private void addRanges() {
		ranges = new BoneTempoKeys[bonecount];
		triggers = new NoteBone[bonecount * step];
		int nextNote = low;
		for (int i = 0; i < bonecount; i++) {
			ranges[i] = new BoneTempoKeys(in, nextNote, (nextNote+step)-1, boneRoot+i, tempo, channel);
			for (int j = 0; j < step; j++) {
				triggers[i + j] = new NoteBone(in,channel,"trigger"+boneRoot+i, 0f, 1f, nextNote + j);
			}
			nextNote+= step;
		}
	}
}
