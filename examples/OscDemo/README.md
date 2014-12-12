# OSC Demo #

The standard Animata offers built-in OSC (Open Sound Control) support.  It uses a preset IP address and port, and responds to  a fixed set of address patterns.

AnimataP5-ng, on the other hand, because it is a Processing library, allows you to hook in ay OSC handling you like.

This example is to show how to have AnimataP5-ng handle the same OSC messages as standard animata, plus a few more.

It also shows a way to have your Animata scene handle "not quite right" messages.

There are many OSC clients, and they provide varying degrees of customization.  Many will only send messages using a fixed set of address patterns.  For those you have to write your code to recognize and handle.

Others allow complete control over the OSC being sent; you can have those send use the standard Animata address patterns.

But there are some clients that fall in the middle.  The allow you to define the address pattern but you can only send values provided by a fixed set of controls or widgets.  

TouchOSC is a good example of this.

TouchOSC is $5 app for both Android and iOS.  It's quite good. It used to be free for Android but that version was limited.  The current version is now on par with the iOS version. This means you can now use the (free) TocuhOSC layout editor with Android.

The editor is good.  You have a variety of control widgets you can add to your layouts.  You can use the default TouchOSc address patterns or defie your own.  

So far so good.  But you cannot have the OSC include any default values.  If, for example, you set up fader to control layer visibilty you have no way to pass along a layer name.  This is a problem.

However, there's a workaround.  Every standard Animata OSC message has a name as the first argument.  Knowing this you can convert TouchOSC messages into  usable Animata messages if the TouchOSC address pattern includes the name as part of of the address pattern.

So, for example, TouchOSC sends this:

    /joint/head f f

and the Processing code converts it to

    /joint head f f 

(where "f" are floating point values).

Lkewise 

    /layervis/name f

becomes

    /layervis name f


There is a helper function called `convertFromTouchOSC` that will handle this.

This example uses a configuration file, `config.txt`, that holds name:value pairs.

In additon to some OSC address and port values you can include an option called `convertTouchOSC`.

If this has any value then all OSC messages recieved by the demo are run through the converter.

If you are **not** using TouchOSC or someother client that is using this pattern hack then comment out or remove this option.



