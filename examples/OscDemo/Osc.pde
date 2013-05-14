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
      //  println("DEBUG lines["+i+"] = " + lines[i] ); // DEBUG
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
  //  println(" arguments: "+oscMsg.arguments()[0].toString());


  int realm  = 1;
  int action = 2;
  int layer = 3;
  int imageRecipient = 3;
  int joint = 4;
  int image = 4;

  String[] parts = split(oscMsg.addrPattern(), "/");
  println("parts[realm]: '" + parts[realm]  + "'" );

  String[] res;

  // Done mostly to suggest a way to process messages meant for different aspects of the sketch
  String reAnimata = "animata";
  String reSketch = "sketch";

  if (parts[realm].equals(reAnimata) ) {
    println("Animata message. Action is " + parts[action]);

    //  If we got this:   /animata/move/<layer-name>/<joint-name> i,i
    if (parts[action].equals("move") ) {
      println("Move joint '" + parts[joint] + "' ...");

      Float x = parseFloat( (oscMsg.arguments()[0]).toString() );
      Float y = parseFloat( (oscMsg.arguments()[1]).toString() );

      println("new location: " + x + ", " + y);
      moveLayerJoint(parts[layer], parts[joint],   x, y);
    }

        //  If we got this:   /animata/image/texture  s
      
    if (parts[action].equals("image") ) {
      println("Load an image into'" + parts[imageRecipient] + "' ...");

      String s =  (oscMsg.arguments()[0]).toString();

      setTextureImage(s);
    }
  } 
}


