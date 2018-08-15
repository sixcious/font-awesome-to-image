# Font Awesome To Image

Generates Font Awesome icon images. Tested to work with Font Awesome 5, Version 5.2.0 to generate 1K+ images (including aliases).

<img src="https://raw.githubusercontent.com/roysix/font-awesome-to-image/master/screenshots/program-intro.png" alt="Program Intro" width="75%"/>

This is a [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html) command-line program that generates high quality image files (like png or gif) using the insanely *awesome* [Font Awesome](http://fontawesome.io) icon library.

### Set Up
<img src="https://raw.githubusercontent.com/roysix/font-awesome-to-image/master/screenshots/setup.png" alt="Set Up" width="50%"/>

1. Download [Font Awesome Free for the Desktop](https://fontawesome.com/how-to-use/on-the-desktop/setup/getting-started) and unzip it
2. Place [`FontAwesome.java`](https://raw.githubusercontent.com/roysix/font-awesome-to-image/master/FontAwesome.java) in the `font-awesome-free-x.x.x-desktop` folder
3. Open up a command prompt or terminal shell and run the command `javac FontAwesome.java` to compile
Note: For Font Awesome Version 4.7 or Black Tie, please use `FontAwesome4.7/FontAwesome.java` or `BlackTie/BlackTie.java`

### How To Use

You can create regular icons, transparent icons, or stacked icons.

##### Regular Icons
<img src="https://raw.githubusercontent.com/roysix/font-awesome-to-image/master/screenshots/regular-icons.png" alt="Regular Icons" width="50%"/>

* Run `java FontAwesome [styles] [icons] [size] [color] [padding]`
* Example `java FontAwesome all all 128 FF0000 1/8`
* *Generates 128px red (FF0000) png icon images with a padding of 1/8 the size (48 * 1/8 = 6px)*

##### Transparent Icons
<img src="https://raw.githubusercontent.com/roysix/font-awesome-to-image/master/screenshots/regular-icons.png" alt="Regular Icons" width="50%"/>

* Run `java FontAwesome [styles] [icons] [size] transparent [padding] [bgcolor]`
* Example `java FontAwesome all all 128 transparent 1/8 FFFFFF`
* *Generates 128px transparent png icon images with a background color of white (FFFFFF) and a padding of 1/8 the size (48 * 1/8 = 6px)*

##### Stacked Icons (In Progress for Version 5...)
<img src="https://raw.githubusercontent.com/roysix/font-awesome-to-image/master/screenshots/stacked-icons.png" alt="Stacked Icons" width="50%"/>

* Run `java FontAwesome [size] [color] [padding] [format] [sicon] [ssize] [scolor]`
* Example `java FontAwesome 24 ffffff 0 png square 48 0`
* *Generates images that have a 24px white icon stacked on top of a 48px black square with no padding*

#### Legend
- [styles] - Can be `all`, `brands`, `regular`, `solid`, `light`, or any combination of the four delimited by commas e.g. `brands,regular`
- [icons] - Can be `all` or a list of icon names delimited by commas e.g. `plus-circle,minus-circle`
- [color] - Can be a [hex triplet](http://en.wikipedia.org/wiki/Web_colors#Hex_triplet) format like `FF69B4` (pink) or `0` (black) or `transparent`


#### Tips!
> Use bigger image sizes for better quality when stacking icons
> Enter colors in [hex triplet](http://en.wikipedia.org/wiki/Web_colors#Hex_triplet) format like `ff69b4` (pink) or `0` (black)
> Enter padding as a fraction of the image size like `1/16` or `0` for none (`1/8` recommended)
> Add more padding if a particularly wide or tall icon is not able to fit in the desired image size
> Images will be saved in a newly made `images` folder
