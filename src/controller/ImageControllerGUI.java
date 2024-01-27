package controller;

import java.awt.image.BufferedImage;

/**
 * Represents the controller for the graphical user interface, makes relevant changes to the model
 * as well as renders messages for the view.
 */
public interface ImageControllerGUI {

  /**
   * Loads an image into the graphical user interface using the filepath from the user's device.
   *
   * @param filePath the path to the file that the user wants to load
   */
  void loadImage(String filePath);

  /**
   * Saves the current image from the graphical user interface using a filepath that the user wants.
   *
   * @param filePath the path to the file that the user wants to save the image as
   */
  void saveImage(String filePath);

  /**
   * Represents when the user wants to change the image that is currently on the graphical user
   * interface.
   *
   * @param command the relevant command for changing the image itself
   */
  void updateImage(ImageProcessorCommand command);

  /**
   * Returns an image for the view to update itself with.
   *
   * @return the image for the user to see
   */
  BufferedImage renderImage();

  /**
   * Returns a histogram for the view to update itself with.
   *
   * @return the histogram for the user to see
   */
  BufferedImage renderHistogram();
}
