# AnimataP5-ng #

A library for [processing](http://processing.org) to render/control [Animata](http://animata.kibu.hu) scenes.

Originally copied from "zeni/animatap5":https://github.com/zeni/animatap5, then updated to work with Processing 2, then assorted bugs and omissions were taken care of, then given freaky magic powers.

`AnimataP5-ng` should now be rendering Animata scenes just as Animata itself does.

However, some features have been added that expand what the Animata renderer can do.

Since a Processing sketch can have any number (more or less) of Animata scenes running at the same time, the AnimataP5 class has an integer `renderPriority` property you can set and use to sort scenes before rendering them.

You can also set a list of PImages to use for a texture in order to have an animated "sprite" effect.

The main class has a method,  `setLayerSpriteImages(String layerName, ArrayList listOfPImages, int spriteUpdateOnFrameCount)`.

It will find the first instance of a layer with that name (so try to use unique layer names), and pass along that list of images to used by that layer's Texture instance.

The `spriteUpdateOnFrameCount` value is used to control how often the texture image changes.  When a layer is drawn, a call is made to `updateCurrentImage()`.  This method tracks a "drawing frame" count. If that count modulo `spriteUpdateOnFrameCount` equals zero, then then next image in the image array is used; the process cycles over the list of images.


### Some previous changes and additions ###

* Added code to allow run-time altering of the Joint `fixed` property.

* Added code for referencing joints by name and reading location values

* Added an example showing the use of OSC to manipulate joints

* Added assorted methods so that a scene can respond to the default OSC messages (as defined in the original Animata)

Note: The OSC handling still has to be done by importing OscP5 and writing code to implement the server processing.

You can choose to handle any OSC messages you want.  An example is included that handles the same OSC messages that the original Animata handled, plus two others.

See the sample applications for usage and some docs about Animata in general.

See [osc.justthebestparts.com](http://osc.justthebestparts.com/) to learn about Open Sound Control.

## Building ##

If this repo does not already contain a compiled working jar file then you need to build that yourself.

You should be able to do this just by running `rake`

That assumes that you have Ruby installed.

You can also look at the file `Rakefile` to see how it invokes `ant` to do the build.

`build.xml` requires a `build.properties` file that defines some user-specific properties.  There is a sample file included; you need to modify this and change the name.  


## Installation ##

When installing a library in Processing you need to place the folder in the `libraries` directory of your `sketchbook` folder.

    <sketchbook-path>/libraries/AnimataP5ng

The library folder needs it's own subdirectory named `library` that holds `AnimataP5ng.jar`

    sketchbook-path>/libraries/AnimataP5/library/AnimataP5ng.jar

Examples go in an `examples` directory

    sketchbook-path>/libraries/AnimataP5ng/examples

The build processing should be giving you an `AnimataP5ng.zip` that bundles up the needed files in the proper folders.  

Copy that file into your sketchbook libraries folder and unzip it.

You should them be able to try out the example sketch: 

   `File -> Examples -> Contributed Libraries -> AnimataP5ng -> AnimataSimple`

If all goes well you should see a smallish screen with two arms, one overlaid on the other, each moving.  

It's kind of creepy, but very cool.

The example also allows you to play with the `fixed` property of the shoulder joints as well as use the mouse to move the location of the wrist joint.


## License ##

Sadly, the original Animata source has been released under the GNU GPL v3, and if this code is based on that code then the GPL extends to this code as well.  

I would much prefer the MIT license, but such is life.  

In any event, thanks to the original devs for their work and for making it available.

The original Animata source can be found at [http://code.google.com/p/animata/](http://code.google.com/p/animata)


