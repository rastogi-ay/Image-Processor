package controller;

import model.ImageModel;
import model.ImageStorage;
import view.ImageProcessorView;

/**
 * Creates a masked image off of a different image, used to apply operations to only certain parts
 * of an image.
 */
public class Mask extends AImageProcessorCommand {

  /**
   * A constructor that doesn't require any arguments, used for calling its methods.
   */
  public Mask() {
    super();
  }

  /**
   * Takes in the relevant arguments for being able to apply a mask an image.
   *
   * @param originalImage the name of the original image to be operated on
   * @param nameOfNewImage the name of the new image to be created
   * @param images the storage of processed images so far
   * @param view the view that transmits output to the user
   * @param messageToUser a message to be shown to the user based on input
   */
  public Mask(String originalImage, String nameOfNewImage, ImageStorage images,
              ImageProcessorView view, String messageToUser) {
    super(originalImage, nameOfNewImage, images, view, messageToUser);
  }

  @Override
  public ImageModel makeModelChange(ImageModel model) {
    return new ColorTransformation("greyscale").makeModelChange(model);
  }
}
