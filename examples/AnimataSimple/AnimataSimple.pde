import animata.*;
import processing.opengl.*;

AnimataP5 armA,armB;

void setup() {
  size(400,400,OPENGL);
  frameRate(25);
  armA = new AnimataP5(this,"arm.nmt");
  armB = new AnimataP5(this,"arm.nmt");
}

void draw() {
  background(0,0,120);
  armB.moveJointX("poignee",10); // set x-coordinate of joint "poignee" to 10
  armB.moveJointY("poignee",500); // set y-coordinate of joint "poignee" to 500
  armB.setBoneTempo("coude",.5); // set tempo of bone "coude" to .5
  armA.setBoneRange("coude",.2,.6); // set the range of bone "coude" between .2 and .6
  armA.setLayerAlpha("arm",120); // set the alpha value of the layer "arm" to 120 (0=transparent/255=opaque)
  armA.setLayerScale("arm",.6); // set the scale of the layer "arm" to .6 (in this case, default is 0.5)
  armA.setLayerPos("arm",0,70); // set the position of layer "arm" to (0,70)
  armB.draw(10,30); // draw scene translated of (10,30)
  armA.draw(10,30); // draw scene translated of (10,30)
}

