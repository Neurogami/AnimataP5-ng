package animata;

import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class Animator extends Observable implements Observer {


	private static Engine engine;
	float targetValue;
	float initialValue;
	float difference;
	float frameIncrement;
	public float currentValue = 0;
	float frame = 0;
	boolean isComplete = false;

	public Animator(float startValue, Observer target) {
		engine.addObserver(this);
		initialValue = currentValue = startValue;
		addObserver(target);
	}

	public void set(float value, int _frames) {
		targetValue = value;
		initialValue = currentValue;
		difference = (value - initialValue);
		frameIncrement = 1 / (float) _frames;
		isComplete = false;
		frame = 0;
		frame += frameIncrement;
	}

	public float getValue() {
		return currentValue;
	}

	public void update(Observable o, Object arg) {
		// Just gonna assume it's always nextFrame.
		if (frame >= 1 || frame == 0) return;
		frame += frameIncrement;
		currentValue = initialValue + (difference * easeOutQuad(frame));
		changed();
		if (frame >= 1) complete();
	}

	void changed() {
		setChanged();
		notifyObservers(NEXT_FRAME);
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void complete() {
		currentValue = targetValue;
		isComplete = true;
		notifyObservers("complete");
	}

	public void stop() {
		frame = 1;
		// engine.deleteObserver(this);
	}

	float easeOutQuad(float t) {
		return -1 * (t /= 1) * (t - 2);
	}

	public static void init(PApplet applet) {
		engine = new Engine(applet);

	};
	public static final String NEXT_FRAME = "nextFrame";
	public static class Engine extends Observable{

		public Engine(PApplet applet) {
			applet.registerDraw(this);
		}
		public void draw() {
			setChanged();
		    notifyObservers(NEXT_FRAME);
		}

	}

}