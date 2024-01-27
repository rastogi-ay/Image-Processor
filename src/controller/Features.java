package controller;

import model.ImageModel;

/**
 * Represents the higher-level user requests a user can perform. Mainly used to call other methods.
 */
public interface Features {

  /**
   * Loads an image into the graphical user interface.
   *
   * @param command the Load command
   * @param model an image with no data, but that is changed through this method
   * @return the loaded image in a model form
   */
  ImageModel loadImage(ImageProcessorCommand command, ImageModel model);

  /**
   * Saves the current image with a specified filepath from the user.
   *
   * @param saveCommand the specific Save command, used for making new files
   */
  void saveImage(ImageProcessorCommand saveCommand);

  /**
   * Changes the image data based on a passed-in command, and returns the new image.
   *
   * @param command the command to change the image data
   * @param model the image model to be changed
   * @return the new model image
   */
  ImageModel changeImage(ImageProcessorCommand command, ImageModel model);
}
