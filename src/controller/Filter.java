package controller;

import model.ImageModel;
import model.Image;
import model.ImageStorage;
import model.Pixel;
import view.ImageProcessorView;

/**
 * The command for applying a filter command to an image.
 */
public class Filter extends AImageProcessorCommand {
  private final String operation;

  /**
   * A constructor that just takes in the specific operation desired.
   *
   * @param operation the operation that is desired to apply
   */
  public Filter(String operation) {
    super();
    this.operation = operation;
  }

  /**
   * Takes in the relevant arguments for being able to filter an image.
   *
   * @param operation the specific type of operation the user wants
   * @param originalImage the name of the original image to be operated on
   * @param nameOfNewImage the name of the new image to be created
   * @param maskedImage the name of the masked image, could be empty
   * @param images the storage of processed images so far
   * @param view the view that transmits output to the user
   * @param messageToUser a message to be shown to the user based on input
   */
  public Filter(String operation, String originalImage, String nameOfNewImage, String maskedImage,
                ImageStorage images, ImageProcessorView view, String messageToUser) {
    super(originalImage, nameOfNewImage, maskedImage, images, view, messageToUser);
    this.operation = operation;
  }

  @Override
  public ImageModel makeModelChange(ImageModel model) {
    switch (this.operation) {
      case "blur":
        double[][] blurFilter = new double[3][3];
        blurFilter[0][0] = 0.0625;
        blurFilter[0][1] = 0.125;
        blurFilter[0][2] = 0.0625;
        blurFilter[1][0] = 0.125;
        blurFilter[1][1] = 0.25;
        blurFilter[1][2] = 0.125;
        blurFilter[2][0] = 0.0625;
        blurFilter[2][1] = 0.125;
        blurFilter[2][2] = 0.0625;
        return this.applyFilter(blurFilter, model);
      case "sharpen":
        double[][] sharpenFilter = new double[5][5];
        sharpenFilter[0][0] = -0.125;
        sharpenFilter[0][1] = -0.125;
        sharpenFilter[0][2] = -0.125;
        sharpenFilter[0][3] = -0.125;
        sharpenFilter[0][4] = -0.125;
        sharpenFilter[1][0] = -0.125;
        sharpenFilter[1][1] = 0.25;
        sharpenFilter[1][2] = 0.25;
        sharpenFilter[1][3] = 0.25;
        sharpenFilter[1][4] = -0.125;
        sharpenFilter[2][0] = -0.125;
        sharpenFilter[2][1] = 0.25;
        sharpenFilter[2][2] = 1;
        sharpenFilter[2][3] = 0.25;
        sharpenFilter[2][4] = -0.125;
        sharpenFilter[3][0] = -0.125;
        sharpenFilter[3][1] = 0.25;
        sharpenFilter[3][2] = 0.25;
        sharpenFilter[3][3] = 0.25;
        sharpenFilter[3][4] = -0.125;
        sharpenFilter[4][0] = -0.125;
        sharpenFilter[4][1] = -0.125;
        sharpenFilter[4][2] = -0.125;
        sharpenFilter[4][3] = -0.125;
        sharpenFilter[4][4] = -0.125;
        return this.applyFilter(sharpenFilter, model);
      default:
        throw new IllegalArgumentException("Invalid type of filter");
    }
  }

  /**
   * Applies a filter to the pixels of the original image in order to create a new one.
   *
   * @param filter the specific filter to be applied to the model
   * @param model the model to be operated on
   * @return a new image model with updated pixels
   */
  private ImageModel applyFilter(double[][] filter, ImageModel model) {
    Pixel[][] newContents = new Pixel[model.getHeight()][model.getWidth()];
    for (int i = 0; i < model.getHeight(); i++) {
      for (int j = 0; j < model.getWidth(); j++) {
        int red = 0;
        int green = 0;
        int blue = 0;
        for (int x = -(filter.length / 2); x < (filter.length / 2) + 1; x++) {
          for (int y = -(filter.length / 2); y < (filter.length / 2) + 1; y++) {
            try {
              int addRed = (int) (model.getPixel(i + x, j + y).getRed() *
                      filter[x + (filter.length / 2)][y + (filter.length / 2)]);
              int addGreen = (int) (model.getPixel(i + x, j + y).getGreen() *
                      filter[x + (filter.length / 2)][y + (filter.length / 2)]);
              int addBlue = (int) (model.getPixel(i + x, j + y).getBlue() *
                      filter[x + (filter.length / 2)][y + (filter.length / 2)]);
              red = red + addRed;
              green = green + addGreen;
              blue = blue + addBlue;
            }
            catch (IllegalArgumentException e) {
              // this represents when a specific cell in the kernel is outside the image boundaries
            }
          }
        }
        newContents[i][j] = new Pixel(this.colorBounds(red, model.getMaxValue()),
                this.colorBounds(green, model.getMaxValue()),
                this.colorBounds(blue, model.getMaxValue()));
      }
    }
    return new Image(newContents, model.getFilepath());
  }
}