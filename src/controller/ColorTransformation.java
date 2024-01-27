package controller;

import model.ImageModel;
import model.Image;
import model.ImageStorage;
import model.Pixel;
import view.ImageProcessorView;

/**
 * The command for applying a color transformation an image.
 */
public class ColorTransformation extends AImageProcessorCommand {
  private final String operation;

  /**
   * A constructor that just takes in the specific operation desired.
   *
   * @param operation the operation that is desired to apply
   */
  public ColorTransformation(String operation) {
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
  public ColorTransformation(String operation, String originalImage, String nameOfNewImage, String maskedImage,
                             ImageStorage images, ImageProcessorView view, String messageToUser) {
    super(originalImage, nameOfNewImage, maskedImage, images, view, messageToUser);
    this.operation = operation;
  }

  @Override
  public ImageModel makeModelChange(ImageModel model) {
    switch (this.operation) {
      case "greyscale":
        double[][] greyScale = new double[3][3];
        greyScale[0][0] = 0.2126;
        greyScale[1][0] = 0.2126;
        greyScale[2][0] = 0.2126;
        greyScale[0][1] = 0.7152;
        greyScale[1][1] = 0.7152;
        greyScale[2][1] = 0.7152;
        greyScale[0][2] = 0.0722;
        greyScale[1][2] = 0.0722;
        greyScale[2][2] = 0.0722;
        return this.applyFilter(greyScale, model);
      case "sepia":
        double[][] sepia = new double[3][3];
        sepia[0][0] = 0.393;
        sepia[1][0] = 0.349;
        sepia[2][0] = 0.272;
        sepia[0][1] = 0.769;
        sepia[1][1] = 0.686;
        sepia[2][1] = 0.534;
        sepia[0][2] = 0.189;
        sepia[1][2] = 0.168;
        sepia[2][2] = 0.131;
        return this.applyFilter(sepia, model);
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
    for (int i = 0 ; i < model.getHeight(); i++) {
      for (int j = 0; j < model.getWidth(); j++) {
        Pixel curr = model.getPixel(i, j);
        int redRed = (int) (curr.getRed() * filter[0][0]);
        int redGreen = (int) (curr.getGreen() * filter[0][1]);
        int redBlue = (int) (curr.getBlue() * filter[0][2]);
        int greenRed = (int) (curr.getRed() * filter[1][0]);
        int greenGreen = (int) (curr.getGreen() * filter[1][1]);
        int greenBlue = (int) (curr.getBlue() * filter[1][2]);
        int blueRed = (int) (curr.getRed() * filter[2][0]);
        int blueGreen = (int) (curr.getGreen() * filter[2][1]);
        int blueBlue = (int) (curr.getBlue() * filter[2][2]);

        int newRed = this.colorBounds(redRed + redGreen + redBlue, model.getMaxValue());
        int newGreen = this.colorBounds(greenRed + greenGreen + greenBlue, model.getMaxValue());
        int newBlue = this.colorBounds(blueRed + blueGreen + blueBlue,model.getMaxValue());

        Pixel newPixel = new Pixel(newRed, newGreen, newBlue);
        newContents[i][j] = newPixel;
      }
    }
    return new Image(newContents, model.getFilepath());
  }
}
