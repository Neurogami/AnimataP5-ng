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


int animationLoop  = 1;
int ANIMATE_ON_COUNT = 3;

int animationCounter = 0;
int maxAnimationLoop = 5;

HashMap sceneTable = new HashMap();
HashMap mainCharacterScenes = new HashMap();

String mainCharacterLayer = "main_head";
String baseMainSceneName = "sprite_";

String leftFacingScene = baseMainSceneName + "left";
String rightFacingScene = baseMainSceneName + "right";
String frontFacingScene = baseMainSceneName + "front";

String mainCharacterScene = rightFacingScene;


//-----------------------------------------------------------
// Use this for setting joint locations in a joint map
class Point {
  float x, y;

  Point( float _x, float _y) {
    x = _x;
    y = _y;
  }

  float x() { return x; }
  float y() { return y; }
}


boolean sketchFullScreen() {
  return true;
}


//-----------------------------------------------------------
void setup() {
float initialScaling = 1.8;
// The displayWidth and displayHeight variables contain the width and 
// height of the screen where the sketch was launched. So to run a 
// sketch that uses the full width and height of the screen, use the following:
// size(displayWidth, displayHeight);

  size(displayWidth, displayHeight, OPENGL);
   println("w x h: " + displayWidth + " x " + displayHeight);
 
  loadScene("scene-backdrop");
  loadScene(leftFacingScene );
  loadScene(rightFacingScene );
  loadScene(frontFacingScene );

  if (displayHeight > 1100 ) {
    setLayerScale("scene-backdrop",  "main_layer", 1.9 ); }
   else {
   initialScaling = displayHeight/630; 
   setLayerScale("scene-backdrop",  "main_layer", 1.6 );
 }
  String sublayer = "sub000";

  setLayerScale(leftFacingScene,  mainCharacterLayer, initialScaling );
  setLayerScale(rightFacingScene, mainCharacterLayer, initialScaling );
  setLayerScale(frontFacingScene, mainCharacterLayer, initialScaling );

  setLayerVisibility(leftFacingScene,  mainCharacterLayer, 0);
  setLayerVisibility(rightFacingScene, mainCharacterLayer, 0);
  setLayerVisibility(frontFacingScene, mainCharacterLayer, 0);

  setLayerVisibility(mainCharacterScene, mainCharacterLayer, 1);
 
  loadData();
  setupOsc();

  
// The idea is to map an orientation, such as left, right, or front, to a scene.
// When we get an OSC message indicating that the orientation has change we want to 
// update the name eof the scne stored in `mainCharacterScene` and toggle visibilty on 
// the main layer of the inactive scenes so that we only see one character scene.
    // sceneTable.put(sceneName, ap5);
  /*


     A plan:  Have three sets of images, all with the same image count.  

     Have the names be derivable from the scene name.

     Each actor has three images sets, for facing left, right, and front.

     A value is set via OSC indicating the active set; the iamge cycling then loads the 
     atching iamges.  If we use a string indicating direction (l,r, f) and use that in the 
     image names then image cycling can build the correct image name string.


     Do this for two actors.  

     Then need OSC to control l and r mouth,  and some frnt-facing effects based on fingers.

     Perhaps if all five fingers deteted than face fornt and scale layer by 2.

     Need to know if we can tell if a hand has the palm paralelt to the Leap (i.e. it is not
     in hand-pupper mode, where the palm is curved and facing left or right.


     If We can tell when a hand is left or right, can we mimici shadow puppets? If 2 fingers.
     just move mouth.  If three or more then the bottom two fingers move mouth and remaing move other things.





   */

// We need a way to group scene images.  Is this something that should be built in to
// an AP5 instance?  Add an HashMap field to the base class so that when you have
//  a reference to a scene you alos have the set of images?
  
  AnimataP5 main = (AnimataP5) sceneTable.get(leftFacingScene);
  ArrayList sprites = new ArrayList();
  sprites.add(loadImage(baseMainSceneName + "left_000.png"));
  sprites.add(loadImage(baseMainSceneName + "left_001.png"));
  sprites.add(loadImage(baseMainSceneName + "left_002.png"));
  sprites.add(loadImage(baseMainSceneName + "left_003.png"));
  sprites.add(loadImage(baseMainSceneName + "left_004.png"));
  sprites.add(loadImage(baseMainSceneName + "left_005.png"));
  main.setLayerSpriteImages("main_head", sprites, ANIMATE_ON_COUNT);

  main = (AnimataP5) sceneTable.get(rightFacingScene);
  sprites = new ArrayList();
  sprites.add(loadImage(baseMainSceneName + "right_000.png"));
  sprites.add(loadImage(baseMainSceneName + "right_001.png"));
  sprites.add(loadImage(baseMainSceneName + "right_002.png"));
  sprites.add(loadImage(baseMainSceneName + "right_003.png"));
  sprites.add(loadImage(baseMainSceneName + "right_004.png"));
  sprites.add(loadImage(baseMainSceneName + "right_005.png"));
  main.setLayerSpriteImages("main_head", sprites, ANIMATE_ON_COUNT);

  main = (AnimataP5) sceneTable.get(frontFacingScene);
  sprites = new ArrayList();
  sprites.add(loadImage(baseMainSceneName + "front_000.png"));
  sprites.add(loadImage(baseMainSceneName + "front_001.png"));
  sprites.add(loadImage(baseMainSceneName + "front_002.png"));
  sprites.add(loadImage(baseMainSceneName + "front_003.png"));
  sprites.add(loadImage(baseMainSceneName + "front_004.png"));
  sprites.add(loadImage(baseMainSceneName + "front_005.png"));
  main.setLayerSpriteImages("main_head", sprites, ANIMATE_ON_COUNT);

}

//-----------------------------------------------------------
void draw() {
  background(0);
  List<AnimataP5> ap5s = new ArrayList<AnimataP5>();
  // println("mainCharacterScene: '" + mainCharacterScene + "'");
  AnimataP5 main = (AnimataP5) sceneTable.get(mainCharacterScene);

  animationLoop ++;

  animationLoop %= ANIMATE_ON_COUNT;

//  if (animationLoop == 0 ) {
 //   // println("animationLoop == 0, animationCounter = " + animationCounter );
 //   animationCounter++;  
 //   animationCounter %= maxAnimationLoop;
  //  setTexturePImage(mainCharacterScene,  mainCharacterLayer, (PImage) main.sceneImages.get(animationCounter ) );	
  //}

  Iterator i = sceneTable.entrySet().iterator(); 

  while (i.hasNext()) {
    Map.Entry me = (Map.Entry)i.next();
    ap5s.add((AnimataP5) me.getValue());
  }

  Collections.sort(ap5s);
  for(AnimataP5 a: ap5s ){
    a.draw(0,0);
  }

}

