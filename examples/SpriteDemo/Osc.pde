import oscP5.*;
import netP5.*;

OscP5 oscP5;

String configFile = "config.txt";

String clientOSCAddress = "0.0.0.0";
String localOSCAddress  = "0.0.0.0";

int clientOSCPort    = 9000;
int localOSCPort     = 8000;

//-----------------------------------------------------------
void setupOsc()  {
  loadData();
  oscP5 = new OscP5(this, localOSCAddress, localOSCPort);

  println(" Using OSC config values:");
  println(" localOSCAddress:\t" + localOSCAddress);
  println(" localOSCPort:\t" + localOSCPort);
}


//-----------------------------------------------------------
void loadData(){
  println("***************************** DEBUGGERY: load data ... ******************************"); // DEBUG

  String defaultValues[] = split("127.0.0.1\n127.0.0.1\n8081\n8001", "\n");

  try {
    String lines[] = loadStrings(configFile);

    for (int i=0; i < lines.length; i++) {
      defaultValues[i] =  lines[i]; 
    }
  } catch(Exception e) {
    println("Error loading data from '" + configFile + "'");
    e.printStackTrace();
  }

  int i = 0;

  clientOSCAddress = trim(defaultValues[i]);          i++;
  localOSCAddress  = trim(defaultValues[i]);          i++;
  clientOSCPort    = int(trim(defaultValues[i]));     i++;
  localOSCPort     = int(trim(defaultValues[i]));     i++;

}


//-----------------------------------------------------------
void oscEvent(OscMessage oscMsg) {

  /* print the address pattern and the typetag of the received OscMessage */

  print("\t\t\t### received an osc message.");
  print(" addrpattern: "+oscMsg.addrPattern());
  println(" typetag: "+oscMsg.typetag());
  //  println(" arguments: "+oscMsg.arguments()[0].toString());


  //  Assume this:
  //      /animata/<sceneName>/[itemType]/<itemName>/[actionOrQuery] (args ...)


  int realm     = 1;
  int sceneName = 2;
  int itemType  = 3;
  int itemID    = 4;

  String[] parts = split(oscMsg.addrPattern(), "/");
  // println("parts[realm]: '" + parts[realm]  + "'" );

  String[] res;

  // Done mostly to suggest a way to process messages meant for different aspects of the sketch
  String reAnimata = "animata";
  String reSketch = "sketch";

  println(oscMsg.addrPattern());

  if (parts[realm].equals(reAnimata) ) {
    println("Animata message. Scene name " + parts[sceneName] + "parts[itemType] = '" + parts[itemType] + "'" );

    //  If we got this:   /animata/<sceneName>/layer/<layer-name>/<some actioj or query>
    if (parts[itemType].equals("layer") ) {
      println("Act on layer '" + parts[itemID] + "'" );
      handleLayerAction( parts[sceneName], parts[itemID], subset(parts, itemID+1), oscMsg.arguments() );
    }

    if (parts[itemType].equals("joint") ) {
      println("Act on joint '" + parts[itemID] + "'" );
      handleJointAction( parts[sceneName], parts[itemID], subset(parts, itemID+1), oscMsg.arguments() );
    }

    if (parts[itemType].equals("bone") ) {
      println("@  @ @ @ A ct on bone '" + parts[itemID] + "'" );
      handleBoneAction( parts[sceneName], parts[itemID], subset(parts, itemID+1), oscMsg.arguments() );
    }
  
    if (parts[itemType].equals("load") ) {
      println("Load scene '" + parts[sceneName] + "'" );
      loadScene( parts[sceneName] );
    }
// /animata/<sceneName>/orientation 
    if (parts[itemType].equals("orientation") ) {
      println("Set scene orientation for '" + parts[sceneName] + "' to '" +parts[itemID] + "'" );
      setSceneOrientation( parts[sceneName] , parts[itemID] );
    }
  } 

}


// How much do we want to assume?  We need the scene and layer name.
// That probably is in the message address.  But if we change how that is ordered
// then code that exepctec it would break.
// Ideally we apps just the needed data as generially as possible.
void handleLayerAction(String sceneName, String layerName, String[] messageTail, Object[] args ) {
  println("**** handleLayerAction for '" +  sceneName + "', layer '" + layerName + "'" );

  String action = messageTail[0];
  println("\taction  = " + action  );
  if (action.equals("move") ) {
    Float x = parseFloat( (args[0]).toString() );
    Float y = parseFloat( (args[1]).toString() );
    moveLayerAbsolute(sceneName, layerName, x, y);              
  }

  if (action.equals("alpha") ) {
    println("---- alpha message ");
    Float f = parseFloat( (args[0]).toString() );
    setLayerAlpha(sceneName, layerName, f);
  }


  if (action.equals("scale") ) {
    Float f = parseFloat( (args[0]).toString() );
    setLayerScale(sceneName, layerName, f);
  }


  if (action.equals("visibilty") ) {
    int i = parseInt( (args[0]).toString() );
    setLayerVisibility(sceneName, layerName, i);
  }

  if (action.equals("image") ) {
    String imageName = args[0].toString();
    setTextureImage(sceneName, layerName, imageName);
  }

  //     Float y = parseFloat( (args[1]).toString() );

  //   println("new location: " + x + ", " + y);
  // moveLayerJoint(parts[layer], parts[joint],   x, y);
}

void handleJointAction(String sceneName, String jointName, String[] messageTail, Object[] args ) {
  println("**** handleJointAction for '" +  sceneName + "', jointName '" + jointName + "'" );

  String action = messageTail[0];

  println("action  = " + action  );
  if (action.equals("move") ) {
    Float x = parseFloat( (args[0]).toString() );
    Float y = parseFloat( (args[1]).toString() );
    moveJoint(sceneName, jointName, x, y);              
  }


}

void handleBoneAction(String sceneName, String boneName, String[] messageTail, Object[] args ) {
  println("* * *   handleBoneAction for '" +  sceneName + "', boneName '" + boneName + "' * * * * * " );

  String action = messageTail[0];

  println("handleBoneAction action  = '" + action  + "'" );

  if (action.equals("scale") ) {
    Float scale = parseFloat( (args[0]).toString() );
    println("Call setBoneScale .... ");
    setBoneScale(sceneName, boneName, scale);
  } else if (action.equals("length") ) {
    Float length = parseFloat( (args[0]).toString() );
    println("Call setBoneLength .... ");
    setBoneLength(sceneName, boneName, length);
  } else {

    println("Have no handler for action " + action );
}

  println("End of handleBoneAction ");

}
