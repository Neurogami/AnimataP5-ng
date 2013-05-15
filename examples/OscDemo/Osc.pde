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
  int area   = 2;
  int layer = 3;
  int imageRecipient = 3;
  int joint = 4;
  int property = 4;

  String[] parts = split(oscMsg.addrPattern(), "/");
  println("parts[realm]: '" + parts[realm]  + "'" );

  String[] res;

  // Done mostly to suggest a way to process messages meant for different aspects of the sketch
  String reAnimata = "animata";
  String reSketch = "sketch";

  if (parts[realm].equals(reAnimata) ) {
    println("Animata message. Action is " + parts[action] + "; parts[area] = " + parts[area]);

    //  If we got this:   /animata/move/<layer-name>/<joint-name> i,i
    if (parts[action].equals("move") ) {
      println("Move joint '" + parts[joint] + "' ...");

      Float x = parseFloat( (oscMsg.arguments()[0]).toString() );
      Float y = parseFloat( (oscMsg.arguments()[1]).toString() );

      println("new location: " + x + ", " + y);
      moveLayerJoint(parts[layer], parts[joint],   x, y);
    }

        //  If we got this:   /animata/layer/<layername>/image  s
      
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
//  Moving a layer, x and y control the speed that the layer is moving:	/layerdeltapos name x y
  if ( parts[realm].equals("layerdeltapos") ){
  
  
  }
}

/*
 

   Notes on OSC address pattern design

   In some ways it feels nice and clean to have this:

     /animata/layer/<layername>/image s
 
     to update an image

    But that has two issues.  One, it does not fit well with /<realm>/<action>/... and, two,
    the client needs to be able to construct the address pattern string using the layer name.

    Is it harder in some langages to do this?  Under what considtions would it be a problem to
    have the client have to construct the pattern string?

   It's trivial in Ruby or JavaScript or Java.  Is it harder in Haskell?

   Assuming the actual invocation from the client is a given, what pattern structure makes sense?

   Should the action come before the specification of what area of the realm is being involved?

   Shouldn't any action segements come last?


       /animata/layer/image/update <layerName> <imageName>

       /animata/layer/<layerName>/image/update  <imageName>
       
       /animata/layer/<layerName>/image  <imageName>

       Is it reasonale to assume that if you can set something you cna also fetch the value?
       
       If that is true then should the addrpatt always make explicit when something is begin set or fetched?

 
       Renoise, for example, has some OSC commands for setting things that do not have a matching fetching command.

       BMP, for example.

       Renoise will reject a message (I think) if the addrpatt and arg count do not fit; is there anything
       in OSC that precludes having two differetn behaviors based on the number or type of args?


       In other words, can you have

          /animata/layer/<layerName>/image  <imageName>

      to set the image, and

          /animata/layer/<layerName>/image  

      to get the image?


      We can do this in Processing; does it meet the OSC spec?


      `OSC Message Dispatching and Pattern Matching
      
      When an OSC server receives an OSC Message, it must invoke the appropriate 
      OSC Methods in its OSC Address Space based on the OSC Message's OSC Address 
      Pattern. 
      
      This process is called dispatching the OSC Message to the OSC Methods 
      that match its OSC Address Pattern. 
      
      All the matching OSC Methods are invoked with the same argument data, namely, the OSC Arguments 
      in the OSC Message.`

Is this just about pattern matching?

There's also this:

      `An OSC Server's OSC Methods are arranged in a tree strcuture called an OSC Address Space. 
      The leaves of this tree are the OSC Methods and the branch nodes are called OSC Containers. 
      An OSC Server's OSC Address Space can be dynamic; that is, its contents and shape can change 
      over time.`


   `The syntax of OSC Addresses was chosen to match the syntax of URLs. (OSC Address Examples)`


   Assume that a  server that behaves differently  based on number and type of args is OK

   If we want a more hierarchacal pattern, then perhaps:

   <realm>/<itemOfInterest>/<itemIdentifier>/<propertyOrAction>

   Some examples:

     /animata/layer/layer_001/image s
     /animata/joint/head/move f,f
     /animata/scene/load s


  This really depends on having a full picture of how a scene can be structured

  It might also be good to follow whatever OCS Animata itself supports, and build on it.

    http://mansteri.com/2008/12/animata-osc/
    http://usinganimata.wikia.com/wiki/Animata_OSC_Commands


   However, it seems that the naming of the patterns is not ideal. They seem very targeted.



    To move a joint, x and y are float values:	 /joint name x y
    Control the length of a bone, value is a float between 0 and 2:	 /anibone name value
    Switch on and off a layer, on_off is 0 or 1:	 /layervis name on_off
    Set the transparency of the layer, value is a float between 0 and 1:	/layeralpha name value

    The next two messages require the svn version:
    Moving a layer, x and y are the position coordinates as float values:	/layerpos name x y
    Moving a layer, x and y control the speed that the layer is moving:	/layerdeltapos name x y




 */

