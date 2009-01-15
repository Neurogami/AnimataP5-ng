import processing.opengl.*;

import animata.*;
AnimataPlayback playback;
PImage viola;
void setup() {
  size(400,400,OPENGL);
  hint(ENABLE_OPENGL_4X_SMOOTH);
  playback = new AnimataPlayback(this);
  playback.addScene("violaplayer.nmt");
}

void draw() {
  background(0);
  playback.draw();
}
