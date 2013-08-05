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

float minZoom = 0.02;
float maxZoom = 10.0;
float zoomInc = 0.04;
float zoomFactor = 0.03;

float currentZoom = minZoom;
int zoomImageWidth  = 1100; //400;
int zoomImageHeight = 480; //400;

float zoomAlpha = 1.0;

int buildings1X = 0;
int buildings2X  = 0;
int buildingsInc = 2;

String dupeSceneQualifier = "02";

float mainZoom  = maxZoom/2.0;
float dupeZoom  = minZoom;


HashMap sceneTable = new HashMap();
HashMap mainCharacterScenes = new HashMap();

String mainSceneName = "Landscape";

String dupeSceneName = mainSceneName + dupeSceneQualifier;
AnimataP5 mainScene;
AnimataP5 dupeScene;

// Adding this, and having it return true, makes your sketch fullscree, no window borders.
// It's set to false for demo purposes
boolean sketchFullScreen() {
  //  return true;
  return false;
}




//-----------------------------------------------------------
void setup() {
  size(1200, 480, OPENGL);
  frameRate(60);
  loadScene(mainSceneName);
  loadSceneDupe(mainSceneName, dupeSceneQualifier);
  buildings2X  = width;

  mainScene = (AnimataP5) sceneTable.get(mainSceneName);
  dupeScene = (AnimataP5) sceneTable.get(dupeSceneName);

  setLayerVisibility(dupeSceneName, "buildings1" , 0);
  setLayerVisibility(dupeSceneName, "buildings2" , 0);

  
  //  loadData();
}


//-----------------------------------------------------------
void draw() {
  background(0);
  shiftBuildings();
  mainZoom = scaleZoom(mainSceneName, mainZoom );
  dupeZoom = scaleZoom(dupeSceneName, dupeZoom);
  mainScene.draw(0,0);
  dupeScene.draw(0,0);
}


/*


   Here's the thing: If you zoom a layer, it will expand out from it's top-left corner.

   In this example, simply increasing the scale value made the image appear to fly down and across
   the screen.

   There are a few things you need to do to control placement when scaling.

   First, you need to control for position.  In this example the zoom image should
   be in the center of the screen.


   In order to do this you need to know the dimensions of the layer, or, really
   the dimensions of the visble texture.  In this example the png is 1200x500 but
   the texture mapping only covers the visible part, and that is what matters
   when placing the layer.

   Except that's not true.  Anaimata seems to move numbers around.

   If you add an image to a layer and move it to the upper left corner Animata
   gives it a negative x and then offsets that with large mesh coordinates.

   So, if you have the image in uper corner in the nmt file you still need to
   use the height and width of the layer, not the visible image


   Once placed, if scaled the drawing still occurs relative to the
   layer's xy.  

   Suppose you want to center an image (i.e layer)

   We need to get the screen width, find the midpoint, get the
   layer width, half that, and subtract the half-layer-width from 
   half-screen-width

   lx = sw/2 - lw/2

   y should work the same

   ly = sh/2 - lh/2


   This works more or less. In this example we still get some
   drift, though.

   The reason seems to be size differences.

   This sketch is set as 1200x500, and the images used are that size as well,
   but to get the zoom image centered those values had to be tweaked.

   If you are playing around with scaling and placement see if your layer size
   values are appropriate


 */
float scaleZoom(String sceneName, float currentZoom) {

  float zoomAlpha = getLayerAlpha(sceneName, "zoom");

  zoomAlpha -= 0.002;
  if (currentZoom > maxZoom) {   
    currentZoom = minZoom; 
    zoomAlpha = 0.8;
  }

  setLayerAlpha(sceneName, "zoom", zoomAlpha);


  float currLayerWidth  = zoomImageWidth  * currentZoom;
  float currLayerHeight = zoomImageHeight * currentZoom;

  // For some reason, the image is drifting to the left
  float lx = width/2.0 - currLayerWidth/2.0;
  float ly = height/2.0 - currLayerHeight/2.0;


  moveLayerAbsolute(sceneName, "zoom", lx, ly);
  setLayerScale(sceneName, "zoom",  currentZoom );
  currentZoom  += currentZoom*zoomFactor;
  return currentZoom;
}


/***************************************************/
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


void loadSceneDupe(String sceneName , String qualifier) {  
  AnimataP5 ap5 = new AnimataP5(this, sceneName + ".nmt");
  ap5.renderPriority(sceneTable.size());
  sceneTable.put(sceneName+qualifier, ap5);
}

void moveLayerAbsolute(String sceneName, String layerName, float x, float y) {
  AnimataP5 ap5 = (AnimataP5) sceneTable.get(sceneName);
  Layer l = ap5.getLayer(layerName);
  l.setPosition(x, y);

}


void setLayerScale(String sceneName, String layerName, float value) {
  AnimataP5 ap5 = (AnimataP5) sceneTable.get(sceneName);
  Layer l = ap5.getLayer(layerName);
  l.setLayerScale(layerName, value );

}

float getLayerAlpha(String sceneName, String layerName) {
  AnimataP5 ap5 = (AnimataP5) sceneTable.get(sceneName);
  Layer l = ap5.getLayer(layerName);
  return l.alpha;
}


void setLayerAlpha(String sceneName, String layerName, float value) {
  println("setLayerAlpha " + layerName + " :  " + value);

  AnimataP5 ap5 = (AnimataP5) sceneTable.get(sceneName);
  println("Have ap5 = " + ap5);

  Layer l = ap5.getLayer(layerName);
  println("Have layer = " + l );
  // P5 tint uses a int from 0 to 255, so we need to map this
  // HOWEVER: If AP5 is just reading in the XML and uses the alpha value there, then
  // it has to account for the mapping. So if we are using this to set the object propery 
  // we should assume that AP5 is convert the float percentage to 0-255
  // l.setLayerAlpha(layerName, map(value, 0,1, 0,255) );
  l.setLayerAlpha(layerName, value );

}


void setLayerVisibility(String sceneName, String layerName, int state) {
  // println("setLayerVisibility " + layerName + " :  " + state);
  AnimataP5 ap5 = (AnimataP5) sceneTable.get(sceneName);
  Layer l = ap5.getLayer(layerName);
  l.setVisibility(state == 1);
}
