package controller;

import model.ImageModel;
import model.Image;
import model.ImageStorage;
import model.Pixel;
import view.ImageProcessorView;

/**
 * The command for vertically flipping an image.
 */
public class VerticalFlip extends AImageProcessorCommand {

  /**
   * Simply vertically flips an image model when this is utilized. No other arguments need to be
   * passed in since code that calls this doesn't have a use for those arguments.
   */
  public VerticalFlip() {
    super();
  }

  /**
   * Takes in the relevant arguments for being able to vertically flip an image.
   *
   * @param originalImage the name of the original image to be operated on
   * @param nameOfNewImage the name of the new image to be created
   * @param images the storage of processed images so far
   * @param view the view that transmits output to the user
   * @param messageToUser a message to be shown to the user based on input
   */
  public VerticalFlip(String originalImage, String nameOfNewImage, ImageStorage images,
                      ImageProcessorView view, String messageToUser) {
    super(originalImage, nameOfNewImage, images, view, messageToUser);
  }

  @Override
  public ImageModel makeModelChange(ImageModel model) {
    Pixel[][] newContents = new Pixel[model.getHeight()][model.getWidth()];
    for (int i = 0; i < model.getHeight(); i++) {
      for (int j = 0; j < model.getWidth(); j++) {
        Pixel newPixel = model.getPixel(model.getHeight() - 1 - i, j);
        newContents[i][j] = newPixel;
      }
    }
    return new Image(newContents, model.getFilepath());
  }
}
