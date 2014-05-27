font-awesome-to-image
=====================

Generates Font Awesome icon images

This is a [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html) command-line program that generates image files (like png or gif) using the insanely *awesome* [Font Awesome](http://fortawesome.github.io/Font-Awesome) icon library.

### Set Up
<img src="https://raw.githubusercontent.com/roysix/font-awesome-to-image/master/Set%20Up.png" alt="" width="50%"/>

1. Download [Font Awesome](http://fortawesome.github.io/Font-Awesome) and unzip it
2. Place [`FontAwesome.java`](https://raw.githubusercontent.com/roysix/font-awesome-to-image/master/FontAwesome.java) in the `font-awesome-x.x.x` folder
3. Open up a command prompt or terminal shell and run the command `javac FontAwesome.java` to compile

### How To Use

##### Regular Icons
<img src="https://raw.githubusercontent.com/roysix/font-awesome-to-image/master/Regular%20Icons.png" alt="Regular Icons" width="50%"/>

* Run `java FontAwesome [size] [color] [padding] [format]`
* Example `java FontAwesome 48 0 1/8 png`
* *Generates 48px black png icon images with a padding of 1/8 the size (48 * 1/8 = 6px)*

##### Stacked Icons
<img src="https://raw.githubusercontent.com/roysix/font-awesome-to-image/master/Stacked%20Icons.png" alt="Stacked Icons" width="50%"/>

* Run `java FontAwesome [size] [color] [padding] [format] [sicon] [ssize] [scolor]`
* Example `java FontAwesome 24 ffffff 0 png square 48 0`
* *Generates images that have a 24px white icon stacked on top of a 48px black square with no padding*

> #### Tips!
> *Use bigger image sizes for better quality when stacking icons
<br/>
Enter colors in [hex triplet](http://en.wikipedia.org/wiki/Web_colors#Hex_triplet) format like `ff69b4` (pink) or `0` (black)
<br/>
Enter padding as a fraction of the image size like `1/16` or `0` for none (`1/8` recommended)
<br/>
Add more padding if a particularly wide or tall icon is not able to fit in the desired image size
<br/>
Images will be saved in a newly made `images` folder*
