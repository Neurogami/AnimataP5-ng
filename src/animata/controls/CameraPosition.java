package animata.controls;

import java.util.ArrayList;
import java.util.Hashtable;

import animata.Controller;



import processing.xml.XMLElement;
import rwmidi.MidiInput;

public class CameraPosition extends Control {
	static Hashtable<String,ArrayList<CameraPosition>> songs = new Hashtable<String,ArrayList<CameraPosition>>();
	public float x;
	public float y;
	public float z;
	private String song;
	public static ArrayList<CameraPosition> allPositions = new ArrayList<CameraPosition>();

	public CameraPosition(XMLElement element, MidiInput in) {
		super(element, in);
		song = element.getParent().getStringAttribute("id");

		x = element.getFloatAttribute("x");
		y = element.getFloatAttribute("y");
		z = element.getFloatAttribute("z");

		allPositions.add(this);
		addToCorrectArrayList();
	}

	private void addToCorrectArrayList() {
		if(songs.get(song) == null) songs.put(song, new ArrayList<CameraPosition>());
		ArrayList<CameraPosition> list = songs.get(song);
		list.add(this);
	}

	public static ArrayList<CameraPosition> getCurrentSongPositions() {
		return songs.get(Controller.getInstance().currentSong);
	}



}
