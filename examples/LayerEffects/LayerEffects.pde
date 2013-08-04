import animata.*;

import animata.model.Skeleton.*;
import animata.model.Layer;

import processing.opengl.*;

import java.util.Iterator;
import java.util.Map;

import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Arrays;



int animationLoop    = 1;
int ANIMATE_ON_COUNT = 3;

int animationCounter = 0;
int maxAnimationLoop = 5;

HashMap sceneTable = new HashMap();
HashMap mainCharacterScenes = new HashMap();

String mainSceneName = "Landscape";

AnimataP5 mainScene;

// Adding this, and having it return true, makes your sketch fullscree, no window borders.
// It's set to false for demo purposes
boolean sketchFullScreen() {
  //  return true;
  return false;
}


int minTone1 = 40;
int maxTone1 = 52;

int buildings1X = 0;
int buildings2X  = 0;
int buildingsInc = 2;



//-----------------------------------------------------------
void setup() {
  size(1200, 480, OPENGL);
  frameRate(60);
  loadScene(mainSceneName);
  buildings2X  = width;

  mainScene = (AnimataP5) sceneTable.get(mainSceneName);
//  loadData();
}


//-----------------------------------------------------------
void draw() {
  background(0);

  shiftBuildings();
  mainScene.draw(0,0);
}

void shiftBuildings() {
  buildings1X -= buildingsInc;
  buildings2X -= buildingsInc;
  
  if (buildings1X < (width *-1) ) {
    buildings1X = width-(buildingsInc*1);
  }

  if (buildings2X < (width *-1) ) {
    buildings2X = width - (buildingsInc);
  }


  float x1 = buildings1X * 1.0;
  float x2 = buildings2X * 1.0;

  moveLayerAbsolute(mainSceneName, "buildings1", x1, 0.0);
  moveLayerAbsolute(mainSceneName, "buildings2", x2, 0.0);

  
}



void loadScene(String sceneName ) {  
  AnimataP5 ap5 = new AnimataP5(this, sceneName + ".nmt");
  ap5.renderPriority(sceneTable.size());
  sceneTable.put(sceneName, ap5);
}


void moveLayerAbsolute(String sceneName, String layerName, float x, float y) {
  AnimataP5 ap5 = (AnimataP5) sceneTable.get(sceneName);
  Layer l = ap5.getLayer(layerName);
  l.setPosition(x, y);

}
