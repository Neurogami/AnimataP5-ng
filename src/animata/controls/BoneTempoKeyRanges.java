package animata.controls;

import processing.xml.XMLElement;
import rwmidi.MidiInput;
import animata.NoteParser;
import animata.NoteParser.BadNoteFormatException;

public class BoneTempoKeyRanges extends Control {
	private Integer low;
	private Integer high;
	private int bonecount;
	private float tempo;
	private BoneTempoKeys[] ranges;
	private String boneRoot;
	public BoneTempoKeyRanges(XMLElement element, MidiInput in) {
		super(element, in);
		try {
			low = NoteParser.getNote(element.getStringAttribute("low", "1"));
			high = NoteParser.getNote(element.getStringAttribute("high", "100"));
		} catch (BadNoteFormatException e) {
			System.out.println(e.getMessage());
		}
		channel = element.getIntAttribute("channel", 16) - 1;
		boneRoot = element.getStringAttribute("bone");
		tempo = element.getFloatAttribute("tempo", 1);
		bonecount = element.getIntAttribute("bonecount",1);
		addRanges();
	}

	private void addRanges() {
		ranges = new BoneTempoKeys[bonecount];
		float step = (((float)high)-((float)low))/((float)bonecount);
		for (int i = 0; i < bonecount; i++) {
			int rangeLow = low + (int)(i*step);
			System.out.println("added range low=" + rangeLow + " step was " + step);
			ranges[i] = new BoneTempoKeys(in, rangeLow, rangeLow + (int)step, boneRoot+i, tempo, channel);
		}
	}
}
