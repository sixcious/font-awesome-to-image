# Font Awesome To Image
<img src="https://raw.githubusercontent.com/sixcious/font-awesome-to-image/main/screenshots/program-intro.png" alt="Program Intro" width="75%"/>

Generates Font Awesome icon images in Java. Tested to work with Font Awesome 6 to generate over 1K+ images (including aliases).

This is a simple Java command-line program that generates high quality image files (like png or gif) using the insanely *awesome* [Font Awesome](http://fontawesome.io) icon library.
If you don't have Java, you can download it from the [OpenJDK](https://jdk.java.net/) website.

## Set Up
<img src="https://raw.githubusercontent.com/sixcious/font-awesome-to-image/main/screenshots/setup.png" alt="Set Up" width="50%"/>

1. Download [Font Awesome Free for the Desktop](https://fontawesome.com/download) and unzip it
2. Place [`FontAwesome.java`](https://raw.githubusercontent.com/sixcious/font-awesome-to-image/main/FontAwesome.java) in the `fontawesome-free-x.x.x-desktop` folder
3. Open up a command prompt or terminal shell, navigate to the folder, and run the command `javac FontAwesome.java` to compile

## How To Use
You can create regular icons, transparent icons, or stacked icons.

### Regular Icons
<img src="https://raw.githubusercontent.com/sixcious/font-awesome-to-image/main/screenshots/regular-icons.png" alt="Regular Icons" width="50%"/>

* Run `java FontAwesome [styles] [icons] [size] [color] [padding]`
* Example `java FontAwesome all all 128 FF0000 1/8`
* *Generates 128px red (FF0000) png icon images with a padding of 1/8 the size (48 * 1/8 = 6px)*

### Transparent Icons
<img src="https://raw.githubusercontent.com/sixcious/font-awesome-to-image/main/screenshots/regular-icons.png" alt="Regular Icons" width="50%"/>

* Run `java FontAwesome [styles] [icons] [size] transparent [padding] [bgcolor]`
* Example `java FontAwesome all all 128 transparent 1/8 FFFFFF`
* *Generates 128px transparent png icon images with a background color of white (FFFFFF) and a padding of 1/8 the size (48 * 1/8 = 6px)*

### Stacked Icons (In Progress for Version 6...)
<img src="https://raw.githubusercontent.com/sixcious/font-awesome-to-image/main/screenshots/stacked-icons.png" alt="Stacked Icons" width="50%"/>

* Run `java FontAwesome [size] [color] [padding] [format] [sicon] [ssize] [scolor]`
* Example `java FontAwesome 24 ffffff 0 png square 48 0`
* *Generates images that have a 24px white icon stacked on top of a 48px black square with no padding*

## Options
- [styles] - Can be `all`, `brands`, `regular`, `solid`, `light`, or any combination of the four delimited by commas e.g. `brands,regular`
- [icons] - Can be `all` or a list of icon names delimited by commas e.g. `plus-circle,minus-circle`
- [color] - Can be a [hex triplet](http://en.wikipedia.org/wiki/Web_colors#Hex_triplet) format like `FF69B4` (pink) or `0` (black) or `transparent`

## Tips
- Use bigger image sizes for better quality when stacking icons
- Enter colors in [hex triplet](http://en.wikipedia.org/wiki/Web_colors#Hex_triplet) format like `ff69b4` (pink) or `0` (black)
- Enter padding as a fraction of the image size like `1/16` or `0` for none (`1/8` recommended)
- Add more padding if a particularly wide or tall icon is not able to fit in the desired image size
- Images will be saved in a newly made `images` folder

## Older Versions (FontAwesome 4 and Black Tie)
If you still need to generate icons for older versions, you can find them in the [past commits](https://github.com/sixcious/font-awesome-to-image/tree/d88ec10bbcf768fcee7c04786a44a21cc0013330), but please note that these versions are no longer officially supported.

## License
MIT

## Copyright
Font Awesome To Image  
Copyright © 2020 Roy Six
