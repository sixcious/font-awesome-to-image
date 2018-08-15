/*
 * Copyright (c) 2014 Roy Six
 * Font Awesome by Dave Gandy - https://fontawesome.com
 */

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.awt.AlphaComposite;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * {@code FontAwesome} converts Font Awesome icons to PNG image files.
 * <p/>
 * For usage and examples, see the accompanying README.MD file.
 *
 * @author  Roy Six
 * @version 1.0
 */
public class FontAwesome {

    private static final String ICONS_JSON_PATH = "metadata/icons.json";
    private static final String BRANDS_FONT_PATH = "otfs/Font Awesome 5 Brands-Regular-400.otf";
    private static final String REGULAR_FONT_PATH = "otfs/Font Awesome 5 Free-Regular-400.otf";
    private static final String SOLID_FONT_PATH = "otfs/Font Awesome 5 Free-Solid-900.otf";

    private static class Properties {
        List<String> icons;
        List<String> styles;
        int size;
        float padding;
        //    private static Font ffont;
        float fsize;
        Color fcolor; // Front variables
        Font sfont;
        float ssize;
        Color scolor;
        String sicon;  // Stacked variables
        boolean transparent;
        Color bgcolor;
    }

    private static class Icon {
        String name;
        List<String> styles;
        Character unicode;
    }

    /**
     * Main method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        validateArgs(args);

        System.out.format("\n%30s", "Initializing properties... ");
        Properties properties = initProperties(args);
        System.out.print("Done!\n");

        System.out.format( "%30s", "Initializing fonts... ");
        Map<String, Font> fonts = initFonts(properties);
        System.out.print("Done!\n");

        System.out.format("%30s", "Initializing icons... ");
        List<Icon> icons = initIcons(properties);
        System.out.print("Done!\n");

        System.out.format("%30s", "Saving " + icons.size() + " images... ");
        buildAndSaveImages(properties, fonts, icons);
        System.out.print("Done!\n");
    }

    /**
     * Validates the command line arguments.
     *
     * @param args the command line arguments
     */
    private static void validateArgs(String[] args) {
        if (args.length != 5 && args.length != 6 && args.length != 8) {
            System.out.print("\n\tFor regular icons, enter 5 arguments:\n");
            System.out.print("\tjava FontAwesome [icons] [styles] [size] [color] [padding]\n");
            System.out.print("\tex: \"java FontAwesome all all 128 ff0000 1/8\"\n\n");
            System.out.print("\tFor transparent icons, enter 6 arguments:\n");
            System.out.print("\tjava FontAwesome [icons] [styles] [size] transparent [padding] [bgcolor]\n");
            System.out.print("\tex: \"java FontAwesome plus-circle,minus-circle 128 transparent 1/8 ffffff\"\n\n");
            System.out.print("\tFor stacked icons, enter 8 arguments:\n");
            System.out.print("\tjava FontAwesome [icons] [styles] [size] [color] [padding] [sicon] [ssize] [scolor]\n");
            System.out.print("\tex: \"java FontAwesome all 128 ffffff 0 png square 256 000000\"\n\n");
            System.out.print("\tNote: [icons] and [styles] can be \"all\" or the individual icon/style names separated by commas e.g. \"plus-circle,minus-circle\" or \"regular,solid\"\n");
            System.exit(0);
        }
    }

    /**
     * Initializes the FontAwesome properties via the provided program arguments.
     *
     * @param args  the command line arguments
     */
    private static Properties initProperties(String[] args) {
        Properties properties = new Properties();
        properties.icons = Arrays.asList(args[0].split(","));
        properties.styles = Arrays.asList(args[1].split(","));
//        properties.ficons = args.length > 0 ? Arrays.asList(args[0].split(",")) : null;
        properties.fsize = args.length > 2 ? Integer.parseInt(args[2]) : 48;
        properties.fcolor = args.length > 3 ? "transparent".equals(args[3]) ? new Color(0x0000000, true) : new Color(Integer.parseInt(args[3], 16)) : new Color(0);
        properties.transparent = args.length > 3 && "transparent".equals(args[3]);
        properties.padding = args.length > 4 && args[4].contains("/") ? Float.parseFloat(args[4].split("/")[0]) / Float.parseFloat(args[4].split("/")[1]) : 0;
        // properties.padding = args.length > 4 ? Float.parseFloat(args[4]) : 0;
       properties.bgcolor = args.length > 5 && properties.transparent ? new Color(Integer.parseInt(args[5], 16)) : null;
        //sicon = args.length > 5 && !transparent ? icons.get(args[5]).toString() : null;
        properties.sicon = null;
        properties.ssize = args.length > 6 ? Integer.parseInt(args[6]) : 0;
        properties.scolor = args.length > 7 ? new Color(Integer.parseInt(args[7], 16)) : null;
        properties.size = properties.fsize > properties.ssize ? (int) properties.fsize : (int) properties.ssize;
//        properties.ffont = font.deriveFont(properties.fsize - properties.fsize * properties.padding);
//        properties.sfont = properties.sicon != null ? font.deriveFont(properties.ssize - properties.ssize * properties.padding) : null;
        return properties;
    }

    /**
     * Initializes the Font Awesome fonts via the provided TTF/OTF files and properties size and padding.
     *
     * @param properties the properties containing the size and padding
     * @return the fonts
     */
    private static Map<String, Font> initFonts(Properties properties) {
        Map<String, Font> fonts = new HashMap<>();
        Map<String, String> paths = new HashMap<>();
        paths.put("brands", BRANDS_FONT_PATH);
        paths.put("regular", REGULAR_FONT_PATH);
        paths.put("solid", SOLID_FONT_PATH);
        for (Map.Entry<String, String> path : paths.entrySet()) {
            try (FileInputStream fis = new FileInputStream(path.getValue())) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, fis).deriveFont(properties.size + (properties.padding * properties.size));
                fonts.put(path.getKey(), font);
            } catch (IOException | FontFormatException e) {
                System.err.print(e.getMessage() + "\n");
                System.exit(1);
            }
        }
        return fonts;
    }

    /**
     * TODO
     *
     * @param properties
     * @return
     */
    private static List<Icon> initIcons(Properties properties) {
        List<Icon> icons = new ArrayList<>();
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(ICONS_JSON_PATH));
            String json = new String(encoded);
            String script = "Java.asJSONCompatible(" + json + ")";
            ScriptObjectMirror mirror = (ScriptObjectMirror) engine.eval(script);
            for (Map.Entry<String, Object> entry : mirror.entrySet()) {
                if (properties.icons.contains("all") || properties.icons.contains(entry.getKey())) {
                    Icon icon = new Icon();
                    icon.name = entry.getKey();
                    ScriptObjectMirror mirror2 = (ScriptObjectMirror) entry.getValue();
                    for (Map.Entry<String, Object> entry2 : mirror2.entrySet()) {
                        if ("styles".equals(entry2.getKey())) {
                            icon.styles = Arrays.asList(entry2.getValue().toString().replace("[", "").replace("]", "").split(", "));
                        } else if ("unicode".equals(entry2.getKey())) {
                            icon.unicode = (char) Integer.parseInt(entry2.getValue().toString(), 16);
                        }
                    }
                    icons.add(icon);
                }
            }
        } catch (IOException | ScriptException e) {
            System.err.print(e.getMessage() + "\n");
            System.exit(1);
        }
        return icons;
    }

    /**
     * Builds and saves all images in the icons map based on the FontAwesome properties in the images folder.
     *
     * @param icons      the icon map
     */
    private static void buildAndSaveImages(Properties properties, Map<String, Font> fonts, List<Icon> icons) {
        File images = new File("images");
        if (!images.exists()) { // Create the images folder if it doesn't already exit
            images.mkdir();
        }
        for (final String style : fonts.keySet()) {
            File subimages = new File("images/" + style);
            if (!subimages.exists()) {
                subimages.mkdir();
            }
        }
        for (Icon icon : icons) {
            for (String style : icon.styles) {
                BufferedImage image = buildImage(properties.size, properties.sfont, properties.sicon, properties.scolor, fonts.get(style), icon.unicode.toString(), properties.fcolor, properties.transparent, properties.bgcolor);
                saveImage(image, "images/" + style + "/" + icon.name, "png");
            }
        }
    }

    /**
     * Builds a BufferedImage with the drawn icon graphic based on the font, size, and color.
     *
     * @param size   the encapsulating image size
     * @param sfont  the stacked font
     * @param sicon  the stacked icon unicode value
     * @param scolor the stacked color
     * @param ffont  the front font
     * @param ficon  the front icon unicode value
     * @param fcolor the front color
     * @param transparent boolean indicating if transparent
     * @param bgcolor     for transparency, the background color
     * @return       the BufferedImage containing the drawn icon graphic
     */
    private static BufferedImage buildImage(int size,
                                           Font sfont, String sicon, Color scolor,
                                           Font ffont, String ficon, Color fcolor,
                                           boolean transparent, Color bgcolor) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); // AA
        // If transparent, set background color for transparent icons
        if (transparent) {
            graphics.setColor(bgcolor);
            graphics.fillRect(0, 0, size, size);
        }
        // If stacked icon, then draw stacked icon first
        if (sicon != null) {
            graphics.setFont(sfont);
            graphics.setColor(scolor);
            Point spoint = calcDrawPoint(sfont, sicon, size, graphics);
            graphics.drawString(sicon, spoint.x, spoint.y);
        }
        graphics.setFont(ffont);
        graphics.setColor(fcolor);
        if (transparent) {
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 1f));
        }
        Point fpoint = calcDrawPoint(ffont, ficon, size, graphics);
        graphics.drawString(ficon, fpoint.x, fpoint.y);
        graphics.dispose();
        return image;
    }

    /**
     * Saves the generated images to the hard drive in the images folder.
     *
     * @param image      the BufferedImage containing the drawn icon graphic
     * @param fileName   the file name including path, but not including the file extension e.g. "square" or "images/square"
     * @param formatName the format name (file extension) to use e.g. "png" or "gif"
     */
    private static void saveImage(BufferedImage image, String fileName, String formatName) {
        try {
            ImageIO.write(image, formatName, new File(fileName + "." + formatName));
        } catch (IOException e) {
            System.err.print(e.getMessage() + "\n");
            System.exit(1);
        }
    }

    /**
     * Calculates the point to draw this icon with this Graphics2D. This will be the baseline lower-left point (X,Y)
     * coordinates and will be centered. The calculation is done using the FontMetrics and GlyphVector rectangle bounds.
     * <p/>
     * The centering technique used is based off of Exploding Pixels's "Drawing Text About Its Visual Center."
     *
     * @param font     the font to use (differs only in size)
     * @param icon     the icon unicode value to draw
     * @param size     the encapsulating image size of the bounding box
     * @param graphics the Graphics2D from the BufferedImage
     * @return         the point containing the x and y coordinates needed to draw the icon centered
     * @see            <a href="http://explodingpixels.wordpress.com/2009/01/29/drawing-text-about-its-visual-center/">
     *                 Exploding Pixels Drawing Text About Its Visual Center</a>
     */
    private static Point calcDrawPoint(Font font, String icon, int size, Graphics2D graphics) {
        int center = size / 2; // Center X and Center Y are the same
        Rectangle stringBounds = graphics.getFontMetrics().getStringBounds(icon, graphics).getBounds();
        Rectangle visualBounds = font.createGlyphVector(graphics.getFontRenderContext(), icon).getVisualBounds().getBounds();
        return new Point(center - stringBounds.width / 2, center - visualBounds.height / 2 - visualBounds.y);
    }

}