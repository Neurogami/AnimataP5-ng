package animata.controls;

import processing.xml.XMLElement;
import rwmidi.MidiInput;

public class ControlFactory {

	public static Control createControl(XMLElement element, MidiInput in) {
		String name = element.getName();
		if(name.equals("notebone")) return new NoteBone(element, in);
//		if(name.equals("faderbone")) return new FaderBone(element);
//		if(name.equals("freqbone")) return new FreqBone(element);
		if(name.equals("noterangebone")) return new NoteRangeBone(element,in);
		if(name.equals("bonetempokeys")) return new BoneTempoKeys(element,in);
		if(name.equals("bonetempokeyranges")) return new BoneTempoKeyRanges(element, in);
		if(name.equals("camera")) return new CameraPosition(element, in);
		if(name.equals("camerashake")) return new CameraShake(element,in);
		return new Control(element, in);

	}

}
