package model;

import java.util.Objects;

/**
 * Represents a singular pixel of an image. Has red, green, and blue values. All of its methods are
 * public as they should be able to be accessed for all image functionality since pixels are the
 * most fundamental unit for an image.
 */
public class Pixel {
  private int red;
  private int green;
  private int blue;

  /**
   * Takes in RGB values to represent color.
   *
   * @param red the red component
   * @param green the green component
   * @param blue the blue component
   */
  public Pixel(int red, int green, int blue) {
    // SHOULD HAVE CHECKS IN HERE TO ENSURE THAT THE RGB VALUES MAKE SENSE
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  /**
   * Returns the red component for this pixel.
   *
   * @return the red component, as an integer
   */
  public int getRed() {
    int r = this.red;
    return r;
  }

  /**
   * Returns the green component for this pixel.
   *
   * @return the green component, as an integer
   */
  public int getGreen() {
    int g = this.green;
    return g;
  }

  /**
   * Returns the blue component for this pixel.
   *
   * @return the blue component, as an integer
   */
  public int getBlue() {
    int b = this.blue;
    return b;
  }

  /**
   * Returns the luma for a given pixel.
   *
   * @return the luma, as an integer, which is the weighted average of the RGB values
   */
  public int getLuma() {
    int red = (int) (.2126 * this.getRed());
    int green = (int) (.7152 * this.getGreen());
    int blue = (int) (.0722 * this.getBlue());
    return red + green + blue;
  }

  /**
   * Returns the value for a given pixel.
   *
   * @return the value, as an integer, which is the maximum value of the RGB components
   */
  public int getValue() {
    int max = this.getRed();
    if (this.getGreen() > max) {
      max = this.getGreen();
    }
    if (this.getBlue() > max) {
      max = this.getBlue();
    }
    return max;
  }

  /**
   * Returns the intensity for a given pixel.
   *
   * @return the intensity, as an integer, which is the average of the RGB values
   */
  public int getIntensity() {
    int sum = this.getRed() + this.getGreen() + this.getBlue();
    return sum / 3;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Pixel)) {
      return false;
    }
    Pixel that = (Pixel) o;
    return this.getRed() == that.getRed() && this.getGreen() == that.getGreen()
            && this.getBlue() == that.getBlue();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.red, this.green, this.blue);
  }
}
