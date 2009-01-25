package animata;

import java.util.Hashtable;

import processing.core.PApplet;
import processing.xml.XMLElement;

public class NoteParser {


	private static Hashtable<String, Integer> notes = new Hashtable<String, Integer>();
	static {
		notes.put("C",  0);
		notes.put("C#", 1);
		notes.put("D",  2);
		notes.put("D#", 3);
		notes.put("E",  4);
		notes.put("F",  5);
		notes.put("F#", 6);
		notes.put("G",  7);
		notes.put("G#", 8);
		notes.put("A",  9);
		notes.put("A#", 10);
		notes.put("B",  11);
	}
	public static Integer getNote(XMLElement xml) throws BadNoteFormatException{
		String noteName = xml.getStringAttribute("note");
		return getNote(noteName);
	}

	public static Integer getNote(String noteName){
		try {
			return Integer.parseInt(noteName);

		} catch (NumberFormatException e) {}

		try {
			return convertStringToNoteNumber(noteName);
		} catch (BadNoteFormatException e) {
			e.printStackTrace();
		}
		System.out.println("Note xml - error parsing " + noteName);
		return null;
	}
	/*
	 * C0 = 0
	 * C1 = 12
	 * C2 = 24 etc...
	 * some regexp testing data:
	 *  <stop channel="13" note="C#-2"/>
		<stop channel="13" note="A-2"/>
		<stop channel="13" note="F-1"/>
		<stop channel="13" note="C#2"/>
		<stop channel="13" note="C#2"/>
		<stop channel="13" note="A2"/>
		<stop channel="13" note="F3"/>
		<stop channel="13" note="C#4"/>
		<stop channel="13" note="A4"/>
	 */
	private static Integer convertStringToNoteNumber(String string) throws BadNoteFormatException{
		try {
			String[] matches = PApplet.match(string,"([^\\d-]*)(.*)");
			Integer octave = Integer.parseInt( matches[2]);
			int base = 12 * (octave + 2);
			String id = matches[1].toUpperCase();
			Integer offset =  notes.get( id) ;
			return base + offset;

		} catch (Exception e) {
			throw new BadNoteFormatException("Couldn't convert " +  string + " to a MIDI note number");
		}
	}
	private static String stringAt(String string, int index) {
		return String.valueOf(string.charAt(index));
	}
	public static class BadNoteFormatException extends Exception {
		public BadNoteFormatException(String string) {
			super(string);
		}
	}
}
