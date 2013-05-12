# AnimataP5 #


Forked from zeni/animatap5 with the goal of updating it to work with Processing 2.



## Building ##

If this repo does not already contain a compiled, working jar file then you need to build that yourself.



## Installation ##

When installing a library in Processing you need to place the folder in the `libraries` directory of your `sketchbook` folder.


    <sketchbook-path>/libraries/AnimataP5


The library folder needs it's own subdirectory named `library` that holds `AnimataP5.jar`


    sketchbook-path>/libraries/AnimataP5/library/AnimataP5.jar


Examples go in an `examples` directory


    sketchbook-path>/libraries/AnimataP5/examples





### Original README ###

A library for processing (http://processing.org) to render/control Animata (http://animata.kibu.hu) scenes.

Usage:

    // --------------------------------------------------------------------------------------------
    AnimataP5 playback;
    void setup(){
         // The nmt file must be in your data folder with the related images
       playback = new AnimataP5(this,"myscene.nmt");
    }
    void draw(){
      playback.draw(0,0); // draw scene translated of (0,0)
      playback.moveJointX("myjoint",100); // set x-coordinate of "myjoint" to 100
      playback.moveJointY("myjoint",10); // set y-coordinate of "myjoint" to 10
      playback.setBoneTempo("mybone",.5); // set tempo of "mybone" to .5
      playback.setBoneRange("mybone",.2,.6); // set the range of "mybone" between .2 and .6
      playback.setLayerAlpha("mylayer",120); // set the alpha value of "mylayer" to 120 (0=transparent/255=opaque)
      playback.setLayerScale("mylayer",1.5); // set the scale of "mylayer" to 1.5
      playback.setLayerPos("mylayer",66,99); // set the position of "mylayer" to (66,99)
    }
    // --------------------------------------------------------------------------------------------
    You can have several instances of the same Animata scene (same file) and control each joint/bone/layer individually.
    To compile you need the Processing library (core.jar).
    Examples to come soon.
    // --------------------------------------------------------------------------------------------

