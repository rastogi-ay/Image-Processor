package controller;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import javax.imageio.ImageIO;
import model.Pixel;

/**
 * This class contains utility methods to read PPM or non PPM files and simply return its contents.
 */
public class ImageUtil {

  /**
   * Read an image file in the PPM format and return a 2D array of pixels representing the image.
   *
   * @param filename the path of the file
   * @return a 2D array of pixels with each pixel containing colors from the PPM file
   * @throws IllegalArgumentException if the file isn't found, the PPM does not start with P3, or
   *      if the width, height, or maximum value are non-positive integers
   */
  public static Pixel[][] readPPM(String filename) throws IllegalArgumentException {
    Scanner sc;

    try {
      sc = new Scanner(new FileInputStream(filename));
    }
    catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File " + filename + " not found!");
    }

    StringBuilder builder = new StringBuilder();
    // read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
        // technically gets rid of the '# Created by GIMP version 2.10.20 PNM plug-in' line
      }
    }

    // now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    if (width <= 0) {
      throw new IllegalArgumentException("Provided width is a non-positive integer");
    }
    int height = sc.nextInt();
    if (height <= 0) {
      throw new IllegalArgumentException("Provided height is a non-positive integer");
    }
    int maxValue = sc.nextInt();
    if (maxValue <= 0) {
      throw new IllegalArgumentException("Provided maximum value of a color is a non-positive"
              + " integer");
    }

    Pixel[][] image = new Pixel[height][width];
    for (int i = 0; i < image.length; i++) {
      for (int j = 0; j < image[i].length; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        if (r < 0 || r > maxValue || g < 0 || g > maxValue || b < 0 || b > maxValue) {
          throw new IllegalArgumentException("Invalid component value, either negative or higher" +
                  " than the maximum value: " + maxValue);
        }
        image[i][j] = new Pixel(r, g, b);
      }
    }

    return image;
  }

  /**
   * Read an image file in a non PPM format and return a 2D array of pixels representing the image.
   *
   * @param filename the path of the file
   * @return a 2D array of pixels with each pixel containing colors from the non PPM file
   */
  public static Pixel[][] readNonPPM(String filename) {
    try {
      FileInputStream file = new FileInputStream(filename);
      // checks if the file exists at all
    }
    catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File " + filename + " not found!");
    }
    Pixel[][] image = null;
    try {
      BufferedImage originalImage = ImageIO.read(new File(filename));
      image = new Pixel[originalImage.getHeight()][originalImage.getWidth()];
      for (int i = 0; i < originalImage.getHeight(); i++) {
        for (int j = 0; j < originalImage.getWidth(); j++) {
          Color color = new Color(originalImage.getRGB(j, i));
          image[i][j] = new Pixel(color.getRed(), color.getGreen(), color.getBlue());
        }
      }
    }
    catch (IOException e) {
      // do nothing, (return a null image)
      // a proper error message will be thrown in the classes that call this
      // if I write an error message here, a similar message will render twice
    }
    return image;
  }
}