import animata.*;
import processing.opengl.*;

AnimataP5 armA,armB;

int poigneeBX = 10;
int poigneeBY = 500;

void setup() {
  size(400,400,OPENGL);
  frameRate(25);
  armA = new AnimataP5(this,"arm.nmt");
  armB = new AnimataP5(this,"arm.nmt");
}

void draw() {
  background(0,0,120);
  armB.moveJointX("poignee", poigneeBX); // set x-coordinate of joint "poignee". 'poignee' means 'handle' in French.
  armB.moveJointY("poignee", poigneeBY); // set y-coordinate of joint "poignee". 
  armB.setBoneTempo("coude",.5); // set tempo of bone "coude" to .5  "coude" means "elbow" in French.

  armA.setBoneRange("coude",.2,.6); // set the range of bone "coude" between .2 and .6
  armA.setLayerAlpha("arm",120); // set the alpha value of the layer "arm" to 120 (0=transparent/255=opaque)
  armA.setLayerScale("arm",.6); // set the scale of the layer "arm" to .6 (in this case, default is 0.5)
  armA.setLayerPos("arm",0,70); // set the position of layer "arm" to (0,70)

  armB.draw(10,30); // draw scene translated of (10,30)
  armA.draw(10,30); // draw scene translated of (10,30)
}

void mouseDragged(){
  poigneeBX =  mouseX;
  poigneeBY =  mouseY;
}

/*

   Some background info, based on http://animata.kibu.hu/tutorials.html and personal experience

   Animata is a tool that allows you to create, edit, and render 2D animations.

   A typical process goes something like this:

   - Import an image into a new project
   - Assign a set of vertices covering the part of image you wish to animate.  
   For example, you might have a drawing of an arm and hand, with a transparent background
   - Making connecting lines among the vertices such that you end up defining a mesh 
   covering the part of the image you wish to animate.

   You then "attach" this mesh to the image; once attached, moving the mesh will distort the image
   as if it were rendered on some stretchy rubber surface.  BE CAREFUL!  Animata has no "undo", so if
   you accidentally move something you will have to figure out how to move it back.

   - Add some "joints". These are vertices to which you connect "bones".  Then associate the initial 
   points with one or another bone.  This will constrain their movement, such that when you move something 
   along a joint the image moves with their associated bones.  Sections of your image will expand and 
   contract in those areas where there is a movable joint.  

   - You can add some implied tension lines between joints; these can be animated in order to create bouncy, 
   springy movement.  

   Things can get move complicated than this, with the addition of multiple images and layers, but essentially
   this is how things work.  What the tutorial videos do not show is what happens when you save an 
   Animata project.  

   You careful work gets written to XML file with the extension ".nmt". 

   It can be a bit more complex than this depending on how your project is devised, but the key
   thing is that the points, vertices, joints, bones, and their associations are stored in an XML
   file. (By the way, all images used in your project have to be in the same directory as the nmt file.
   It's just an assumption Animata makes when you save your project, even if you try setting some 
   other path.)

   It is this nmt file (plus all associated image files) that gets loaded by AnimataP5.

   You don't need to have Animata available in order to use AnimataP5.  You can (and perhaps should) use
   Animata to generate the project files, but after that you can manipulate them as you see fit and use
   them with AnimataP5.  

   You can also plausibly create the nmt file by hand or using some other tool.

   Hand-crafting seems daunting, but editing an existing project file can be fairly easy.  At the very least
   you can swap out the images used, so long as any new image maps well to the defined mesh and bones.


   This example allows you to change the location of the "poignee" joint on arm B by clicking and dragging the 
   cursor.  You'll notice that the hand does not simply follow the cursor; it is constrained by the attached
   bones and joints, with the shoulder location fixed.  In other words, you cannot just drag the arm around.


   After adding the code for mouseDragged() I wondered why the shoulder stayed fixed.



 */

