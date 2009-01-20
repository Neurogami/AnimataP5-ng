package animata;

import processing.xml.XMLElement;
import rwmidi.MidiInput;
import animata.controls.Control;
import animata.controls.NoteBone;

public class ControlFactory {

	public static Control createControl(XMLElement element, MidiInput in) {
		String name = element.getName();
		if(name.equals("notebone")) return new NoteBone(element, in);
//		if(name.equals("faderbone")) return new FaderBone(element);
//		if(name.equals("freqbone")) return new FreqBone(element);
//		if(name.equals("noterangebone")) return new NoteRangeBone(element);
//		if(name.equals("bonetempokeys")) return new BoneTempoKeys(element);
//		if(name.equals("bonetempokeyranges")) return new BoneTempoKeyRanges(element);

		return new Control(element, in);

	}

}
