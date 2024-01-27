package controller;

import java.io.IOException;
import model.ImageModel;
import model.Image;
import model.ImageStorage;
import model.Pixel;
import view.ImageProcessorView;

/**
 * The command for brightening or darkening an image.
 */
public class BrightDark extends AImageProcessorCommand {
  private final String value;

  /**
   * Takes in the value that the user wants to brighten or darken the image by, as well as the view
   * for rendering error messages.
   *
   * @param value the value to brighten or darken an image by
   * @param view the view for rendering messages
   */
  public BrightDark(String value, ImageProcessorView view) {
    super(view);
    this.value = value;
  }

  /**
   * Takes in the relevant arguments for being able to brighten or darken an image.
   *
   * @param value the value by which the user wants to brighten or darken an image
   * @param originalImage the name of the original image to be operated on
   * @param nameOfNewImage the name of the new image to be created
   * @param maskedImage the name of the masked image, could be empty
   * @param images the storage of processed images so far
   * @param view the view that transmits output to the user
   * @param messageToUser a message to be shown to the user based on input
   */
  public BrightDark(String value, String originalImage, String nameOfNewImage, String maskedImage,
                    ImageStorage images, ImageProcessorView view, String messageToUser) {
    super(originalImage, nameOfNewImage, maskedImage, images, view, messageToUser);
    this.value = value;
  }

  @Override
  public ImageModel makeModelChange(ImageModel model) {
    try {
      int value = Integer.parseInt(this.value);
      Pixel[][] newContents = new Pixel[model.getHeight()][model.getWidth()];
      for (int i = 0; i < model.getHeight(); i++) {
        for (int j = 0; j < model.getWidth(); j++) {
          Pixel curr = model.getPixel(i, j);
          int red = curr.getRed() + value;
          int blue = curr.getBlue() + value;
          int green = curr.getGreen() + value;
          Pixel newPixel = new Pixel(this.colorBounds(red, model.getMaxValue()),
                  this.colorBounds(green, model.getMaxValue()),
                  this.colorBounds(blue, model.getMaxValue()));
          newContents[i][j] = newPixel;
        }
      }
      return new Image(newContents, model.getFilepath());
    }
    catch (NumberFormatException e) {
      try {
        this.view.renderMessage("Specified value must be an integer");
        return model;
      }
      catch (IOException e2) {
        throw new IllegalStateException("'Specified value must be an integer'"
                + " couldn't be rendered");
      }
    }
  }
}
