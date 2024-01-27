package controller;

import model.ImageModel;
import model.Image;
import model.ImageStorage;
import model.Pixel;
import view.ImageProcessorView;

/**
 * The command for applying a component command to an image.
 */
public class Component extends AImageProcessorCommand {
  private final String operation;

  /**
   * A constructor that just takes in the specific operation desired.
   *
   * @param operation the operation that is desired to apply
   */
  public Component(String operation) {
    super();
    this.operation = operation;
  }

  /**
   * Takes in the relevant arguments for being able to apply a component an image.
   *
   * @param operation the specific type of operation the user wants
   * @param originalImage the name of the original image to be operated on
   * @param nameOfNewImage the name of the new image to be created
   * @param maskedImage the name of the masked image, could be empty
   * @param images the storage of processed images so far
   * @param view the view that transmits output to the user
   * @param messageToUser a message to be shown to the user based on input
   */
  public Component(String operation, String originalImage, String nameOfNewImage, String maskedImage,
                   ImageStorage images, ImageProcessorView view, String messageToUser) {
    super(originalImage, nameOfNewImage, maskedImage, images, view, messageToUser);
    this.operation = operation;
  }

  @Override
  public ImageModel makeModelChange(ImageModel model) {
    Pixel[][] newContents = new Pixel[model.getHeight()][model.getWidth()];
    for (int i = 0; i < model.getHeight(); i++) {
      for (int j = 0; j < model.getWidth(); j++) {
        Pixel curr = model.getPixel(i, j);
        Pixel grey;
        switch (this.operation) {
          case "red-component":
            grey = new Pixel(curr.getRed(), curr.getRed(), curr.getRed());
            break;
          case "green-component":
            grey = new Pixel(curr.getGreen(), curr.getGreen(), curr.getGreen());
            break;
          case "blue-component":
            grey = new Pixel(curr.getBlue(), curr.getBlue(), curr.getBlue());
            break;
          case "luma-component":
            grey = new Pixel(curr.getLuma(), curr.getLuma(), curr.getLuma());
            break;
          case "value-component":
            grey = new Pixel(curr.getValue(), curr.getValue(), curr.getValue());
            break;
          case "intensity-component":
            grey = new Pixel(curr.getIntensity(), curr.getIntensity(), curr.getIntensity());
            break;
          default:
            throw new IllegalArgumentException("Invalid kind of component");
        }
        newContents[i][j] = grey;
      }
    }
    return new Image(newContents, model.getFilepath());
  }
}
