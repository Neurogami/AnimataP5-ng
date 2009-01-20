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
AnimataPlayback tree;
AnimataPlayback strings;
FallingLeavesScene leaves;
PApplet applet = this;
PImage viola;
MidiInput in;
void setup() {
  size(950,614,OPENGL);
  frameRate(25);
  tree = new AnimataPlayback(this,"tree.nmt");
  strings = new AnimataPlayback(this, "strings.nmt");

  leaves = new FallingLeavesScene(this);
  in = RWMidi.getInputDevice("IAC Bus 1 <MIn:0> Apple Computer, Inc.").createInput(this);
}

void draw() {
  background(255);
  tree.draw();
  pushMatrix();
  translate(-width/2,-height/2,50);
  try{
    leaves.draw();
  }catch(Exception e){}
  
  popMatrix();
  strings.draw();
}
public void noteOnReceived(rwmidi.Note n) {
  float x = 700 + (300 * int(n.getPitch())/60.0);
  //leaves.addLeaf(x);
}


