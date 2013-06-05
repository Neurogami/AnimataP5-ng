import oscP5.*;
import netP5.*;

OscP5 oscP5;

String configFile      = "config.txt";
String clientOSCAddress = "0.0.0.0";
String localOSCAddress = "0.0.0.0";
int    clientOSCPort    = 8081;
int    localOSCPort    = 8000;

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
  println("***************************** DEBUG: load data ... ******************************"); // DEBUG

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

  print("### received an osc message.");
  print(" addrpattern: "+oscMsg.addrPattern());
  println(" typetag: "+oscMsg.typetag());


  int realm    = 1;
  int action   = 2;
  int area     = 2;
  int layer    = 3;
  int joint    = 4;
  int property = 4;

  String[] parts = split(oscMsg.addrPattern(), "/");

  // Done mostly to suggest a way to process messages meant for different aspects of the sketch
  // For example, if you want an OSC message to tell the sketch to quit.
  String reAnimata = "animata";
  String reSketch = "sketch";

  if (parts[realm].equals(reAnimata) ) {

    println("Animata message. Action is " + parts[action] + "; parts[area] = " + parts[area]);

    //   /animata/move/<layer-name>/<joint-name> i,i
    if (parts[action].equals("move") ) {
      println("Move joint '" + parts[joint] + "' ...");

      Float x = parseFloat( (oscMsg.arguments()[0]).toString() );
      Float y = parseFloat( (oscMsg.arguments()[1]).toString() );

      println("new location: " + x + ", " + y);
      moveLayerJoint(parts[layer], parts[joint],   x, y);
    }

    //    /animata/layer/<layername>/image  s
    if ( parts[area].equals("layer") ) {
      String layerName = parts[layer];
      if (parts[property].equals("image") ) {
        println("Load an image into'" + layerName  + "' ...");
        String s =  (oscMsg.arguments()[0]).toString();
        setTextureImage(s, layerName);
      }
    }
  } 


  // The OSC stuff Animata itself would handle

  //    To move a joint, x and y are float values:	 /joint name x y
  if ( parts[realm].equals("joint") ){
    String jointName = (oscMsg.arguments()[0]).toString();
    Float x = parseFloat( (oscMsg.arguments()[1]).toString() );
    Float y = parseFloat( (oscMsg.arguments()[2]).toString() );
    animataOscJoint(jointName, x, y);
  }

  //  Control the length of a bone, value is a float between 0 and 2:	 /anibone name value
  if ( parts[realm].equals("anibone") ){
    String boneName = (oscMsg.arguments()[0]).toString();
    Float length = parseFloat( (oscMsg.arguments()[1]).toString() );
    animataOscAnibone(boneName, length);
  }

  //  Switch on and off a layer, on_off is 0 or 1:	 /layervis name on_off
  if ( parts[realm].equals("layervis") ){
    String layerName = (oscMsg.arguments()[0]).toString();
    int state = parseInt( (oscMsg.arguments()[1]).toString() );
    animataOscLayervis(layerName, state);
  }

  //  Set the transparency of the layer, value is a float between 0 and 1:	/layeralpha name value
  if ( parts[realm].equals("layeralpha") ){
    String layerName = (oscMsg.arguments()[0]).toString();
    Float value = parseFloat( (oscMsg.arguments()[1]).toString() );
    animataOscLayeralpha(layerName, value);
  }

  //    Moving a layer, x and y are the position coordinates as float values:	/layerpos name x y
  if ( parts[realm].equals("layerpos") ){
    String layerName = (oscMsg.arguments()[0]).toString();
    Float x = parseFloat( (oscMsg.arguments()[1]).toString() );
    Float y = parseFloat( (oscMsg.arguments()[2]).toString() );
    animataOscLayerpos(layerName, x, y);
  }

  //  Moving a layer, x and y are ofsets from the current x and y :	/layerdeltapos name x y
  if ( parts[realm].equals("layerdeltapos") ){
    String layerName = (oscMsg.arguments()[0]).toString();
    Float x = parseFloat( (oscMsg.arguments()[1]).toString() );
    Float y = parseFloat( (oscMsg.arguments()[2]).toString() );
    animataOscLayerDeltapos(layerName, x, y);
  }
}


