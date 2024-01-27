package model;

/**
 * Represents the data to be passed into an image processor, and has functionality that an
 * image processor can support.
 */
public interface ImageModel {

  /**
   * Returns the pixel at the specified position.
   *
   * @param row the x-coordinate of the position
   * @param col the y-coordinate of the position
   * @return a Pixel at the position
   * @throws IllegalArgumentException if the coordinates are outside the image boundaries
   */
  Pixel getPixel(int row, int col) throws IllegalArgumentException;

  /**
   * Returns the maximum value for a pixel for a specific image implementation.
   *
   * @return the maximum value, as an integer
   */
  int getMaxValue();

  /**
   * Returns the height for this image.
   *
   * @return the height, as an integer
   */
  int getHeight();

  /**
   * Returns the width for this image.
   *
   * @return the width, as an integer
   */
  int getWidth();

  /**
   * A method that returns the file path for an image.
   *
   * @return the file path, as a String
   */
  String getFilepath();
}
