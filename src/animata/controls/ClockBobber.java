package animata.controls;

import java.util.Observable;
import java.util.Observer;

import animata.Animator;
import animata.Clock;
import animata.Controller;

public class ClockBobber implements Observer {

	private Animator animator;

	public ClockBobber(Clock clock) {
		System.out.println("new clock bobber");
		clock.addObserver(this);
		animator = new Animator(0,this);
	}

	public void update(Observable o, Object arg) {
		if(arg == Clock.CROTCHET){
			animator.currentValue = 0;
			animator.set((float)Math.PI,30);
		}
		if(o == animator) updateAnimation();
	}

	private void updateAnimation() {
		Controller.getInstance().animateBone("crotchet", 1 - (float)Math.sin(animator.currentValue));
	}

}
