font-awesome-to-image
=====================

Generates Font Awesome icon images

This is a [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html) command-line program that generates image files (like png or gif) using the insanely *awesome* [Font Awesome](http://fortawesome.github.io/Font-Awesome) icon library.

### Set Up
<img src="https://raw.githubusercontent.com/roysix/font-awesome-to-image/master/Set%20Up.png" alt="" width="50%"/>

1. Download [Font Awesome](http://fortawesome.github.io/Font-Awesome) and unzip it
2. Place [`FontAwesome.java`](https://raw.githubusercontent.com/roysix/font-awesome-to-image/master/FontAwesome.java) in the `font-awesome-x.x.x` folder that has the `css` and `fonts` sub-folders
3. Open up a command prompt or terminal shell, navigate to the `font-awesome-x.x.x` folder and run the command `javac FontAwesome.java` to compile

### How To Use

##### Regular Icons
<img src="https://raw.githubusercontent.com/roysix/font-awesome-to-image/master/Regular%20Icons.png" alt="Regular Icons" width="50%"/>

* Run `java FontAwesome [size] [color] [format]`
* Example `java FontAwesome 48 0 png`
* *Generates 48px black png icon images*

##### Stacked Icons
<img src="https://raw.githubusercontent.com/roysix/font-awesome-to-image/master/Stacked%20Icons.png" alt="Stacked Icons" width="50%"/>

* Run `java FontAwesome [size] [color] [format] [sicon] [ssize] [scolor]`
* Example `java FontAwesome 24 ffffff png square 48 0`
* *Generates images that have a 24px white icon stacked on top of a 48px black square*

> *Tip: Use bigger image sizes for better quality when stacking icons
<br/>
Please enter colors in hex format like `ff69b4` (pink) or `0` (black)
<br/>
Images will be saved in a newly made `images` folder*
