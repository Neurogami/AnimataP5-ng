import rwmidi.*;

import oscP5.*;
import netP5.*;

import processing.opengl.*;

import animata.*;
AnimataPlayback playback;
PImage viola;
void setup() {
  size(950,614,OPENGL);
  hint(ENABLE_OPENGL_4X_SMOOTH);
  playback = new AnimataPlayback(this);
  playback.loadSet("set.xml");
}

void draw() {
  background(255);
  playback.draw();
}
