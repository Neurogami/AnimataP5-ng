// These methods should be handling OSC requests.
// The main OSC code (where oscEvent is defined) 
// figures out what to do and with what data and routes it off to
// methods here.
//
// This code assumes that all AnimataP5 instances are stored in a HashMap
// and keyed by scene name; the scene name is Animata nmt file minus the
// extension (".nmt") part.
//  
//  


void moveLayerJoint(String sceneName, String layerName, String jointName, float x, float y) {
  // println("moveLayerJoint: " + jointName + ", to  " + x + ", " + y );
  AnimataP5 ap5 = (AnimataP5) sceneTable.get(sceneName);
  ap5.moveJointOnLayer(jointName, layerName, x, y); 
}

void moveJoint(String sceneName,  String jointName, float x, float y) {
  // println("moveJoint: " + jointName + ", to  " + x + ", " + y );
  AnimataP5 ap5 = (AnimataP5) sceneTable.get(sceneName);
  ap5.moveJoint(jointName, x, y); 
}

void setTextureImage(String sceneName,  String layerName, String imageName) {
  // println("setTextureImage: " + imageName  + " for " + layerName );
  AnimataP5 ap5 = (AnimataP5) sceneTable.get(sceneName);
  ap5.setNewMeshImage( imageName,  layerName); 
}


void setTexturePImage(String sceneName,  String layerName, PImage image) {
  AnimataP5 ap5 = (AnimataP5) sceneTable.get(sceneName);
  ap5.setNewMeshImage( image,  layerName); 
}

/* 
   Ideally, the code or the OSC message wold not need to know too much. OTOH, the OSC now needs to know
   the scene name.  So, some options:

   /animata/<scene-name>/orientation <right, left, forward>

BUT: We are using different scenes for different directions.  Or, more broadly, we are
allow this AP5 app to toggle active character stuff, so it is not really about direction
but about what scene should be assigned to the main character variable.

Where does this logic live?  In the Leap controller? What sort of API should it
expect? Not every program is going to have a notion of `main character`, and even this
app might have several characters that need swapping.

What might be nice is to have yet another sort of scene-struct thing, where you can define  a meta-scene
as a collection of scenes, only one begin active at a time.  So the OSC directs messages to a meta-scene,
and may tell it some detail (`../orientation/left`), and the meta-scene itself then decides how to
handle that (perhaps by swapping out the the active sub-scene).

For now we can just hack it and have the OSC decide to send main character orientation messages.

 */

// We assume for now that there is one main scene and that we can infer the new scene name from
// the old scene name.
// Or, we assume that for this we are expecting a partial scene name and the orientation
void setSceneOrientation( String partialSceneName, String orientation ) {
  String sceneName = partialSceneName + orientation;
  println("new main scene name: '" + sceneName + "'");

  // This makes all layers invisible.  WTF?
  setLayerVisibility(sceneName,          mainCharacterLayer, 1);
  setLayerVisibility(mainCharacterScene, mainCharacterLayer, 0);

  // So this is a hack.  
  setLayerVisibility(sceneName,          mainCharacterLayer, 1);

  // Still have some odd flickering ...

  mainCharacterScene = sceneName;
}



// Assumes the given scene name is the base part of the nmt file with the project XML
// e.g. "foo" => "foo.nmt"
//
void loadScene(String sceneName ) {  
  AnimataP5 ap5 = new AnimataP5(this, sceneName + ".nmt");
  ap5.renderPriority(sceneTable.size());
  // println("\tLoad scene: '" +  sceneName + "'");
  sceneTable.put(sceneName, ap5);
}


//    Moving a layer, x and y are the position coordinates as float values:	/layerpos name x y
void moveLayerAbsolute(String sceneName, String layerName, float x, float y) {
  // println("moveLayerAbsolute " + sceneName + "::" + layerName + " to  " + x + ", " + y);  
  AnimataP5 ap5 = (AnimataP5) sceneTable.get(sceneName);
  // println("Have ap5 = " + ap5);
  Layer l = ap5.getLayer(layerName);
  l.setPosition(x, y);

}

void moveLayerRelative(String sceneName, String layerName, float x, float y) {
  // println("moveLayerRelative " + sceneName + "::" + layerName + " to  " + x + ", " + y);  
  AnimataP5 ap5 = (AnimataP5) sceneTable.get(sceneName);
  // println("Have ap5 = " + ap5);
  Layer l = ap5.getLayer(layerName);
  l.setPosition(l.x() + x, l.y() + y);
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

void setLayerScale(String sceneName, String layerName, float value) {
  // println("setLayerScale " + layerName + " :  " + value);
  AnimataP5 ap5 = (AnimataP5) sceneTable.get(sceneName);
  Layer l = ap5.getLayer(layerName);
  // P5 tint uses a int from 0 to 255, so we need to map this
  // HOWEVER: If AP5 is just reading in the XML and uses the sclae value there, then
  // it has to account for the mapping. So if we are using this to set the object propery 
  // we should assume that AP5 is convert the float percentage to 0-255
  // l.setLayerAlpha(layerName, map(value, 0,1, 0,255) );
  l.setLayerScale(layerName, value );

}


void setLayerVisibility(String sceneName, String layerName, int state) {
  // println("setLayerVisibility " + layerName + " :  " + state);
  AnimataP5 ap5 = (AnimataP5) sceneTable.get(sceneName);
  Layer l = ap5.getLayer(layerName);
  l.setVisibility(state == 1);
}


//  Control the length of a bone, value is a float between 0 and 2:	 /anibone name value
void setBoneLength(String sceneName, String boneName, float length){
  // println("setBoneLength " + boneName + " :  " + length);
  AnimataP5 ap5 = (AnimataP5) sceneTable.get(sceneName);
  Bone b = ap5.getBone(boneName);
  b.setSize(length);
}



//  Control the length of a bone, value is a float between 0 and 2:	 /anibone name value
void setBoneScale(String sceneName, String boneName, float scale){
  // println("setBoneScale " + boneName + " :  " + scale);
  AnimataP5 ap5 = (AnimataP5) sceneTable.get(sceneName);
  Bone b = ap5.getBone(boneName);
  b.setScale(scale);
}

