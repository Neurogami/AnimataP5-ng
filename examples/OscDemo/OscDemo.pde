import animata.*;

import animata.model.Skeleton.*;

import processing.opengl.*;

import java.util.Iterator;
import java.util.Map;


int SCREEN_WIDTH  = 500;
int SCREEN_HEIGHT = 600;

AnimataP5 Doll;

HashMap jointTable = new HashMap();

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

//-----------------------------------------------------------
// These values were taken from the original nmt file
void prepJointLocations() {
  jointTable.put("head",   new Point( 46.20,  88.6) );
  jointTable.put("r_hand", new Point(- 21.0, 243.0) );
  jointTable.put("l_hand", new Point( 184.0, 198.0) );
  jointTable.put("r_foot", new Point( -21.0, 419.0) );
  jointTable.put("l_foot", new Point( 218.0, 391.0) );
}

//-----------------------------------------------------------
void setup() {
  size(SCREEN_WIDTH, SCREEN_HEIGHT, OPENGL);
  Doll = new AnimataP5(this,"jgb-doll.nmt");
  prepJointLocations();
  loadData();
  setupOsc();
  
}

//-----------------------------------------------------------
void draw() {
  background(0);
  setJointPositions();
  Doll.draw(10,10); 
}



/* ****************** Some Observations *************************


#  Managing joint movement with OSC #

The original Animata program has built-in OSC support.

AnimataP5 does not (so far!) have the same.


However, it's easy to add using the OscP5 library

http://www.sojamo.de/libraries/oscp5/

To learn more about OSC, see http://osc.justthebestparts.com

It has code samples and explains using OscP5.

In a nutshell:

- Import OscP5
- Create a server (the thing that listens for OSC messages)
- Add an OSC message event handler method that will examine incoming messages
and so something useful

"Something useful" here means manipulating the Animata scene, but since you have 
full control of your P5 sketch your OSC can do much more.


In order to manipulate any part of an Animata object you need a way to locate it.

The best way is to use a name.  Animata does not require you to name things unless 
you want to apply OSC messages (because that is how Animata will find the correct 
joint).

It is probably a good idea to use names whenever possible when creating your Animata
scenes to make it easier to reference things later on should you want to.

OSCeleton (https://github.com/Sensebloom/OSCeleton) is a tool that sends OSC messages
based on data received from an XBox Kinect. (See http://kinect.justthebestparts.com
for some more about hacking around with the Kinect.)

It assumes you are using a Kinect to drive the application.  It has an option to
send OSC message that play mice with Animata, and includes an example Animata scene.

If you are using OSC with Animata your OSC messages will have to conform to the 
Animata address pattern; this is what OSCeleton does.  Actually, Animata allows you 
to pick the names for your joints and such but OSCeleton has these names built-in 
so you need to follow them.

With AnimataP5 and Processing you are free to name things as you like and craft
your own OSC address patterns, but this example is going to use a variation
on the OSCeleton demo so the joint names will follow the OSCeleton convention.

Those names are pretty sensible if you are targeting a human-like figure, as in
this example.

This is the joint data found in the scene nmt file:


<joint name="head" x="46.208652" y="88.680550" fixed="0" selected="1" osc="0" />
<joint name="neck" x="59.999622" y="132.255859" fixed="0" selected="0" osc="0" />
<joint name="l_shoulder" x="96.170837" y="157.626373" fixed="0" selected="0" osc="0" />
<joint name="torso" x="77.048012" y="208.374023" fixed="0" selected="0" osc="0" />
<joint name="r_shoulder" x="50.621754" y="176.083939" fixed="0" selected="0" osc="0" />
<joint name="l_hip" x="114.119019" y="233.785370" fixed="0" selected="0" osc="0" />
<joint name="r_hip" x="71.246582" y="256.189697" fixed="0" selected="0" osc="0" />
<joint name="r_knee" x="-0.426601" y="314.908997" fixed="0" selected="0" osc="0" />
<joint name="l_knee" x="160.805069" y="299.034149" fixed="0" selected="0" osc="0" />
<joint name="r_foot" x="-21.736025" y="419.784698" fixed="0" selected="0" osc="0" />
<joint name="l_foot" x="218.231354" y="392.024414" fixed="0" selected="0" osc="0" />
<joint name="l_elbow" x="139.947693" y="180.178192" fixed="0" selected="0" osc="0" />
<joint name="l_hand" x="184.611359" y="198.042953" fixed="0" selected="0" osc="0" />
<joint name="r_elbow" x="14.897085" y="211.421524" fixed="0" selected="0" osc="0" />
<joint name="r_hand" x="-21.449156" y="242.805420" fixed="0" selected="0" osc="0" />

In order to manipulate any of these the OSC message needs to specify a joint name, the type
of manipulate (move, set the fixed property, etc.) and any data needed to follow through.

One way to think about OSC address patterns is as a hierarchy of items or behavior.

While this example only manipulates Animata joints you can easily imagine having it do other
things in response to OSC, such as loading a new scene or playing some music.

The example also has but one figure (or skeleton, as Animata might put it), but adding another 
would be simple so the address patterns will take into account multiple skeletons.

Some OSC-aware programs, when they allow management of multiple similar items (such as audio
    tracks in a music program) will use numbers in the address pattern, like this:

/song/track/2/mute i 

/song/track/3/vol f


OscP5 only catches the receipt of a message.  Figuring out the details of that message happens in 
your own code.  You can use pattern matching, string splitting, etc.,  to determine the scope 
or intent of a message and then do additional message parsing from there. 

There's a catch, though: Animata does not allow naming of skeletons.  There's nothing in
the Animata GUI to set the name of a skeleton.  I tried manually adding a name to the XML, but
it was removed when I opened the project and saved it back.

Indeed, if you look at the AnimataP5 code you'll see that joints are found by scanning 
through layers; the Skeleton class has no "name" property.

This raises some possibilities.  One is to give everything in a scene a unique name. That
has the virtue of working right away. It has the downside of being forced to use what are
likely to become clunky names prone to errors.  For example, if instead of naming the right-hand
joint "r_hand" you had to call it "figure_1_r_hand", and do the same for all the other figure
joints, there's a really good chance of a misspelling or other typographical error.

Another option is to specify layer and joint name; joint names would then only have to be unique
for each layer, and you could partition complex scenes into multiple layers. 

That's how the jazz_anim.mnt example (included when you install Animata) does it.

So that suggests something like this:

/animata/move/<layer-name>/<joint-name> i,i

or possibly

/animata/layer/<layer-name>/joint/<joint-name>/move  i,i


Picking an address pattern scheme can get tricky. If you've already figured out all the possible 
things you are going to ever do then you might possibly get it right the first time.

Mostly, though, it is an matter of some reasonable consideration of what to expect plus
things you learn after actual use.


This example uses the former.  It makes it easier to split up messages based on the main
area of concern (Animata, or audio, or some other possible aspect of the sketch) and then
narrows down to what action to take. The message then provides any additional data in order
to take action.


Likewise, setting the "fixed" property can be done with this:

/animata/layer/<layer-name>/joint/<joint-name>/setfixed  i

Is this ideal?  I don't know yet.  Part of of me thinks boolean properties should be
controlled with distinct messages (e.g. ../fix and ../unfix, or something like that).

Another part of me sees that as too complicated, that's it's better for the client to
have a single address pattern that takes the new value. If that value is coming from
some device then then client software can just pass it on.

What's actually best will depend on the specifics of the program, the expectations 
of the user, and any peripherals.

To play with these example you'll need an OSC client that can send the appropriate OSC
address patterns.  I suggest using osc-repl: https://github.com/Neurogami/osc-repl

It's a Ruby program that runs in a terminal.  You can configure the server IP address and 
port, and then use the REPL to send custom OSC messages.

*/

//-----------------------------------------------------------

// For demo purpose the code only cares about a few joints: head, r_hand, l_hand, l_foot, and r_foot
//  To help manage joints and coordinates a HashMap is used. The name of each joint maps
//  to an array holding the x and y position values.

void setJointPositions() {
  Iterator i = jointTable.entrySet().iterator(); 
  Point coords;
  String name;
  Joint j;
  while (i.hasNext()) {
    Map.Entry me = (Map.Entry)i.next();
    coords = (Point) me.getValue();
    name = me.getKey().toString();
    // Useful if you want to track joint locations
    // j = Doll.getJoint(name);  
    // println("Joint '" + name + " is at x, y " + j.x() + ", " + j.y() );
    Doll.moveJointX(name, coords.x() ); 
    Doll.moveJointY(name, coords.y() ); 
  }
}



void moveLayerJoint(String layerName, String jointName, float x, float y) {
  println("moveLayerJoint: " + jointName + ", to  " + x + ", " + y );

  // We don't yet implement finding joints based on layer names
  // Since the `draw` method renders the joints based on the jointTable we need
  // to update values there.
  jointTable.put(jointName, new Point(x, y) );
}


void setTextureImage(String imageName, String layerName) {
  println("setTextureImage: " + imageName  + " for " + layerName );
  Doll.setNewMeshImage( imageName,  layerName); 

}
