package controller;

import model.ImageModel;

/**
 * Represents a function object that runs a command for an image processor.
 */
public interface ImageProcessorCommand {

  /**
   * The method that is able to run a specific command object and update the view as well. It works
   * in conjunction with the image storage system.
   *
   * @throws IllegalStateException if output cannot be transmitted
   */
  void checkStorage() throws IllegalStateException;

  /**
   * Used for command objects that need to make new image data based on old data. If it needs to,
   * this method will also update the view.
   *
   * @param model the specific model to be operated on for creation of new images
   * @return an image model with new data
   */
  ImageModel makeModelChange(ImageModel model);

  /**
   * Makes a change to an old image to make a new one, with regard to a masked image. The changes
   * will only be applied to the pixels that are black.
   *
   * @param model the data to edit and create a new image out of
   * @return a model with the new image representation
   */
  ImageModel makeModelChangeWithMask(ImageModel model);
}
