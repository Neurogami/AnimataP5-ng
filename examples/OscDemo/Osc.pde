import oscP5.*;
import netP5.*;

OscP5 oscP5;

String configFile      = "config.txt";
Config config;
String clientOSCAddress = "0.0.0.0";
String localOSCAddress = "0.0.0.0";
int    clientOSCPort    = 8081;
int    localOSCPort    = 7013;

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

  config = new Config(configFile);
  config.load();
  clientOSCAddress = config.value("clientOSCAddress");
  localOSCAddress  = config.value("localOSCAddress"); 
  clientOSCPort    = config.intValue("clientOSCPort");
  localOSCPort     = config.intValue("localOSCPort"); 

}


//-----------------------------------------------------------
void oscEvent(OscMessage oscMsg) {

  /* print the address pattern and the typetag of the received OscMessage */

  if (config.value("convertTouchOSC") != null) {
    oscMsg = convertFromTouchOSC(oscMsg);
  }

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


// Some OSC clients do not ive you the option to send "fixed" arguments.
// At best you can define a custom address pattern.
//
// A good example of this is TouchOSC, hence the name for this method and config option.
//
// TocuOSC allows you to define your own screens using assorted widgets.  The widgets can send
// 1 or 2 arguments, typiccally floating point; the widgets are variations on binary switches or
// gradient-value sliders.
//
// You can define the address pattern to be, for example, '/joint' but there is no way to
// hard-code the first parameter to be a joint name.  All you send as the arguments are the values
// returned by the widget.
//
// To get around this, we assume that the address patern in the client has the joint, layer, or bone
// name appended as the last segment.  In other wordds, since we cannot have the client
// send "/joint head 100.0 344.8" we have it send  "/joint/head 100.0 344.8" 
// This method then splits out that name and uses it to create a new OSC message; this is the message
// that is then actually executed.
//
// It gets a bit more complicated.  When the there are two arguments after the name value they
// refer to screen location.  Animata expects them to be absolute values. However, many (most?)
// OSC clients are going to be sending a float in the range of 0.0 to 1.0.  
//
// This function makes some assumptions about what "role" values are playing, and assumes that if 
// an OSC message is meant to specify a location then the float value is a percentage of a screen
// dimension.  For example, you indicate the middle of screen with 0.5 0.5, and the bottom right corner
// as 1.0 1.0
//
// This really only works for OSC messages that follow the standard Animata OSC mesage set
//   
// See http://usinganimata.wikia.com/wiki/Animata_OSC_Commands
OscMessage convertFromTouchOSC(OscMessage oscMsg) {
  String patt = oscMsg.addrPattern();
  String[] parts = splitTokens(patt, "/");
  String[] lessOne = shorten(parts);
  String name =  parts[parts.length - 1];
  String newAddrPattern = "/" + join(lessOne , "/");
  OscMessage m = new OscMessage(newAddrPattern);
  m.add(name);

  if (oscMsg.arguments().length == 1) {
    if (newAddrPattern.equals("/layervis" ) )  {
      m.add(parseInt( (oscMsg.arguments()[0]).toString() ));
    } else {
      m.add(parseFloat( (oscMsg.arguments()[0]).toString() ));
    }
  } else { // We assume there are 2, since the standard OSC messages have either 1 or 2 args,
    // All default 2-arg OSC messages are assumed to refer to screen coordinates.
    m.add(  map( parseFloat( (oscMsg.arguments()[1]).toString()), 0.0, 1.0,  0.0,  height ) ); 
    m.add(  map( parseFloat( (oscMsg.arguments()[0]).toString()), 0.0, 1.0,  width, 0.0   ) );
  }
  return m;
}
