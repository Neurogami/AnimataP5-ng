import rwmidi.*;

import traer.physics.*;

import app.models.*;
import mf.*;
import app.helpers.*;
import mf.geom.*;

import oscP5.*;
import netP5.*;

import processing.opengl.*;

import animata.*;
AnimataPlayback playback;
FallingLeavesScene leaves;
PImage viola;
MidiInput in;
void setup() {
  size(950,614,OPENGL);
  frameRate(25);
  hint(ENABLE_OPENGL_4X_SMOOTH);
  playback = new AnimataPlayback(this);
  playback.addScene("forest.nmt");
  leaves = new FallingLeavesScene(this);
  in = RWMidi.getInputDevice("IAC Bus 1 <MIn:0> Apple Computer, Inc.").createInput(this);
}

void draw() {
  background(255);
  playback.draw();
  pushMatrix();
  translate(-width/2,-height/2,50);
  try{
    leaves.draw();
  }catch(Exception e){}
  popMatrix();
}
public void noteOnReceived(rwmidi.Note n) {
  float x = 700 + (300 * int(n.getPitch())/60.0);
  leaves.addLeaf(x);
}

