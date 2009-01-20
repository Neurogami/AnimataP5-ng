package animata;

import processing.xml.XMLElement;
import rwmidi.MidiInput;
import animata.controls.Control;

public class Scene {

	private Control[] controls;
	private MidiInput in;

	public Scene(XMLElement element, MidiInput in) {
		this.in = in;
		addControls(element.getChildren());

	}

	private void addControls(XMLElement[] elements) {
		controls = new Control[elements.length];
		for (int i = 0; i < elements.length; i++) {
			XMLElement element = elements[i];
			Control control = ControlFactory.createControl(element, in);
			controls[i] = control;
		}
	}

}
