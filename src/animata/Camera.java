package animata;

import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class Camera implements Observer {
	private static final int CAMERA_DISTANCE = 500;
	public class CameraShaker implements Observer {
		private static final float X_RANGE = (float) Math.PI * 5;
		private Animator animator;
		public float y = 0;
		public float x = 0;

		CameraShaker(){
			this.animator = new Animator(0,this);
		}

		public void update(Observable o, Object arg) {
			float v = sinWithDecay(animator.currentValue);
			this.y  = v;
			updateWithShake();
		}


		private float sinWithDecay(float v) {
			return (float) Math.sin(v) * 1/(v/X_RANGE);
		}

		public void shake() {
			animator.currentValue = 0;
			animator.set(X_RANGE, 20);
		}
	}
	// TODO: find a good standard Java vector class! This is shocking now.
	public float x;
	public float y;
	public float z;
	public float targetX;
	public float targetY;
	public float targetZ;
	private Animator animator;
	private float endX;
	private float endY;
	private float endZ;
	private float startX;
	private float startY;
	private float startZ;
	private CameraShaker shaker;



	public Camera(PApplet applet) {
		x =  applet.width/2.0f;
		y = applet.height/2.0f;
		z = CAMERA_DISTANCE;//(applet.height/2.0f) / PApplet.tan(PApplet.PI*60.0f / 360.0f);
		targetX = applet.width/2.0f;
		targetY = applet.height/2.0f;
		targetZ = 0f;
		shaker = new CameraShaker();
		animator = new Animator(0,this);
	}
	private void adjustCameraForShake() {
		y += shaker.y;

	}
	public void zoomBy(float delta) {
		z+=delta;
		targetZ += delta;

	}
	public void panXBy(float delta) {
		x += delta;
		targetX += delta;
	}

	public void panYBy(float delta) {
		y += delta;
		targetY += delta;
	}
	public void update(Observable o, Object arg) {
		updateFromAnimator();
		updateWithShake();
	}
	private void updateWithShake() {
		if(animator.currentValue != 1 && animator.currentValue != 0 ) return;
		y += shaker.y;
		targetY += shaker.y;

	}
	private void updateFromAnimator() {
		float v = animator.currentValue;
		x = startX + (endX-startX) * v;
		y = startY + (endY-startY) * v;
		z = startZ + (endZ-startZ) * v;
		targetX = x;
		targetY = y;
		targetZ = z - CAMERA_DISTANCE;
	}
	public void moveTo(float x, float y, float z, int frames){
		startX = this.x;
		startY = this.y;
		startZ = this.z;
		endX = x;
		endY = y;
		endZ = z;
		animator.currentValue = 0;
		animator.set(1, frames);
	}
	@Override
	public String toString() {
		return super.toString() + "<camera x=\"" + x +"\" y=\"" + y +"\" z=\"" + z +"\" />";
	}
	public void shake() {
		shaker.shake();

	}
}