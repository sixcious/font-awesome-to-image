/*
 * Copyright (c) 2014 Roy Six
 * Font Awesome by Dave Gandy - http://fontawesome.io
 */

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <code>FontAwesome</code> converts Font Awesome icons to image files, such as in PNG or GIF format.
 * <p/>
 * Arguments are expected in two formats.
 * <p/>
 * Regular Icon (4 Arguments): [size] [color] [padding] [format]
 * <br/>
 * Stacked Icon (7 Arguments): [size] [color] [padding] [format] [sicon] [ssize] [scolor]
 * <p/>
 * Examples: <code>java FontAwesome 48 0 png</code> or <code>java FontAwesome 32 ffffff png square 64 0</code>
 * <p/>
 * Note: the colors should be entered in hex format like <code>ff69b4</code> (pink) or <code>0</code> (black)
 *
 * @author  Roy Six
 * @version 1.0
 */
public class FontAwesome {

    int size; float padding; Font ffont; float fsize; Color fcolor; Font sfont; float ssize; Color scolor; String sicon; String format;

    /**
     * Main method.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        validateArgs(args);
        System.out.format( "\n%30s", "Initializing font... ");
        final Font font = initFont();
        System.out.print("Done!\n");
        System.out.format("%30s", "Initializing icons... ");
        final Map<String, Character> icons = initIcons();
        System.out.print("Done!\n");
        System.out.format("%30s", "Initializing properties... ");
        final FontAwesome properties = initProperties(font, icons, args);
        System.out.print("Done!\n");
        System.out.format("%30s", "Saving " + icons.size() + " images... ");
        buildAndSaveImages(icons, properties);
        System.out.print("Done!\n");
    }

    /**
     * Validates the command line arguments.
     *
     * @param args command line arguments
     */
    public static void validateArgs(final String[] args) {
        if (args.length != 4 && args.length != 7) {
            System.out.print("\n\tFor regular icons, please enter 4 arguments:\n");
            System.out.print("\tjava FontAwesome [size] [color] [padding] [format]\n");
            System.out.print("\tex: \"java FontAwesome 48 0 1/8 png\"\n\n");
            System.out.print("\tFor stacked icons, please enter 7 arguments:\n");
            System.out.print("\tjava FontAwesome [size] [color] [padding] [format] [sicon] [ssize] [scolor]\n");
            System.out.print("\tex: \"java FontAwesome 32 ffffff 1/8 png square 64 0\"\n");
            System.exit(0);
        }
    }

    /**
     * Initializes the Font Awesome {@link Font} via the provided TTF file.
     *
     * @return Font Awesome {@link Font}
     */
    public static Font initFont() {
        Font font = null;
        try (final FileInputStream fis = new FileInputStream("fonts/fontawesome-webfont.ttf")) {
            font = Font.createFont(Font.TRUETYPE_FONT, fis);
        } catch (final IOException | FontFormatException e) {
            System.err.print(e.getMessage() + "\n");
            System.exit(1);
        }
        return font;
    }

    /**
     * Initializes the Font Awesome Icon {@link Map}, a mapping of {@link String} icon name keys to {@link Character}
     * unicode values via the provided CSS file.
     *
     * @return Font Awesome Icon {@link Map}
     */
    public static Map<String, Character> initIcons() {
        final Map<String, Character> icons = new HashMap<>(1000);
        try {
            final List<String> lines = Files.readAllLines(Paths.get("css/font-awesome.css"), StandardCharsets.UTF_8);
            final Pattern pvalue = Pattern.compile("(?<=\\\\).*(?=\")"); // (?<=\).*(?=")
            final Pattern pkey = Pattern.compile("(?<=.fa-).*(?=:before)"); // (?<=.fa-).*(?=:before)
            for (int i = 0; i < lines.size(); i++) { // Check each line if it has a unicode value
                if (lines.get(i).contains("content: \"\\f")) {
                    final Matcher mvalue = pvalue.matcher(lines.get(i));
                    if (mvalue.find()) {
                        final Character value = toUnicode(mvalue.group());
                        for (int j = i - 1; j >= 0; j--) { // Check previous lines for the keys to this unicode value
                            final Matcher mkey = pkey.matcher(lines.get(j));
                            if (mkey.find()) {
                                icons.put(mkey.group(), value);
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        } catch (final IOException e) {
            System.err.print(e.getMessage() + "\n");
            System.exit(1);
        }
        return icons;
    }

    /**
     * Initializes the {@link FontAwesome} properties via the provided program arguments. See {@link #main(String[])}
     * for the expected arguments format. Note: the font sizes are reduced by 1/8th for some padding.
     *
     * @param font  Font Awesome {@link Font}
     * @param icons Font Awesome Icon {@link Map}
     * @param args  command line arguments
     * @return      {@link FontAwesome} properties
     */
    public static FontAwesome initProperties(final Font font, final Map<String, Character> icons, final String[] args) {
        final FontAwesome properties = new FontAwesome();
        properties.fsize = args.length > 0 ? Integer.parseInt(args[0]) : 48;
        properties.fcolor = args.length > 1 ? new Color(Integer.parseInt(args[1], 16)) : new Color(0);
        properties.padding = args.length > 2 && args[2].contains("/") ? Float.parseFloat(args[2].split("/")[0]) / Float.parseFloat(args[2].split("/")[1]) : 0;
        properties.format = args.length > 3 ? args[3] : "png";
        properties.sicon = args.length > 4 ? icons.get(args[4]).toString() : null;
        properties.ssize = args.length > 5 ? Integer.parseInt(args[5]) : 0;
        properties.scolor = args.length > 6 ? new Color(Integer.parseInt(args[6], 16)) : null;
        properties.size = properties.fsize > properties.ssize ? (int) properties.fsize : (int) properties.ssize;
        properties.ffont = font.deriveFont(properties.fsize - properties.fsize * properties.padding);
        properties.sfont = properties.sicon != null ? font.deriveFont(properties.ssize - properties.ssize * properties.padding) : null;
        return properties;
    }

    /**
     * Builds and saves all images in the icons map based on the {@link FontAwesome} properties in the images folder.
     *
     * @param icons      Font Awesome Icon {@link Map}
     * @param properties {@link FontAwesome} properties
     */
    public static void buildAndSaveImages(final Map<String, Character> icons, final FontAwesome properties) {
        final File images = new File("images");
        if (!images.exists()) { // Create the images folder if it doesn't already exit
            images.mkdir();
        }
        for (final Map.Entry<String, Character> entry : icons.entrySet()) {
            saveImage(buildImage(properties.size, properties.sfont, properties.sicon, properties.scolor, properties.ffont,
                                 entry.getValue().toString(), properties.fcolor), "images/" + entry.getKey(), properties.format);
        }
    }

    /**
     * Builds an {@link BufferedImage} with the drawn icon graphic based on the {@link Font}, size, and color.
     *
     * @param size   encapsulating image size
     * @param sfont  stacked {@link Font}
     * @param sicon  stacked icon unicode value
     * @param scolor stacked {@link Color}
     * @param ffont  front {@link Font}
     * @param ficon  front icon unicode value
     * @param fcolor front {@link Color}
     * @return       {@link BufferedImage} containing the drawn icon graphic
     */
    public static BufferedImage buildImage(final int size,
                                           final Font sfont, final String sicon, final Color scolor,
                                           final Font ffont, final String ficon, final Color fcolor) {
        final BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); // AA
        if (sfont != null) { // Then draw stacked icon first
            graphics.setFont(sfont);
            graphics.setColor(scolor);
            final Point spoint = calcDrawPoint(sfont, sicon, size, graphics);
            graphics.drawString(sicon, spoint.x, spoint.y);
        }
        graphics.setFont(ffont);
        graphics.setColor(fcolor);
        final Point fpoint = calcDrawPoint(ffont, ficon, size, graphics);
        graphics.drawString(ficon, fpoint.x, fpoint.y);
        graphics.dispose();
        return image;
    }

    /**
     * Saves the generated {@link BufferedImage} to the hard drive in the images folder.
     *
     * @param image      {@link BufferedImage} containing the drawn icon graphic
     * @param fileName   file name including path, but not including the file extension e.g. "square" or "images/square"
     * @param formatName format name (file extension) to use e.g. "png" or "gif"
     */
    public static void saveImage(final BufferedImage image, final String fileName, final String formatName) {
        try {
            ImageIO.write(image, formatName, new File(fileName + "." + formatName));
        } catch (final IOException e) {
            System.err.print(e.getMessage() + "\n");
            System.exit(1);
        }
    }

    /**
     * Converts a unicode {@link String} to a properly formatted unicode {@link Character}.
     *
     * @param str {@link String} input
     * @return    {@link Character} in unicode format
     */
    public static Character toUnicode(final String str) {
        return (char) Integer.parseInt(str, 16);
    }

    /**
     * Calculates the {@link Point} to draw this icon with this {@link Graphics2D}. This will be the baseline lower-left
     * point (X,Y) coordinates and will be centered. The calculation is done using the FontMetrics and GlyphVector
     * rectangle bounds.
     * <p/>
     * The centering technique used is based off of Exploding Pixels's "Drawing Text About Its Visual Center."
     *
     * @param font     {@link Font} to use (differs only in size)
     * @param icon     icon unicode value to draw
     * @param size     encapsulating image size of the bounding box
     * @param graphics {@link Graphics2D} from the {@link BufferedImage}
     * @return         {@link Point} containing the x and y coordinates needed to draw the icon centered
     * @see            <a href="http://explodingpixels.wordpress.com/2009/01/29/drawing-text-about-its-visual-center/">
     *                 Exploding Pixels Drawing Text About Its Visual Center</a>
     */
    public static Point calcDrawPoint(final Font font, final String icon, final int size, final Graphics2D graphics) {
        final int center = size / 2; // Center X and Center Y are the same
        final Rectangle stringBounds = graphics.getFontMetrics().getStringBounds(icon, graphics).getBounds();
        final Rectangle visualBounds = font.createGlyphVector(graphics.getFontRenderContext(), icon).getVisualBounds().getBounds();
        return new Point(center - stringBounds.width / 2, center - visualBounds.height / 2 - visualBounds.y);
    }

}
