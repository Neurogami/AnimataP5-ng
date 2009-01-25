package animata;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import animata.controls.CameraPosition;

public class RandomCameraPanner implements Observer{

	private static RandomCameraPanner instance;

	public RandomCameraPanner(Clock clock, AnimataPlayback animataPlayback) {
		clock.addObserver(this);
		instance = this;
	}

	public void update(Observable o, Object arg) {
		if(arg == Clock.BAR) moveToRandomPosition();

	}

	private void moveToRandomPosition() {
		if(Math.random() >.125) return;
		definitelyMoveCameraToRandomPosition();
	}

	public void definitelyMoveCameraToRandomPosition() {
		ArrayList<CameraPosition> positions = CameraPosition.getCurrentSongPositions();
		int index = (int)(Math.random() * positions.size());
		Controller.getInstance().moveCameraTo(positions.get(index), 100);
	}
	public static void moveNow(){
		instance.definitelyMoveCameraToRandomPosition();
	}
}
