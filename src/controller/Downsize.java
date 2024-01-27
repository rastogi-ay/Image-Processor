package controller;

import java.io.IOException;
import model.Image;
import model.ImageModel;
import model.ImageStorage;
import model.Pixel;
import view.ImageProcessorView;

/**
 * The command for downsizing an image by a particular size.
 */
public class Downsize extends AImageProcessorCommand {
  private final String width;
  private final String height;

  /**
   * Takes in the width and height that the user wants to downsize an image by, as well as the view
   * for rendering error messages.
   *
   * @param width the desired width by the user
   * @param height the desired height by the user
   * @param view the view for transmitting important messages
   */
  public Downsize(String width, String height, ImageProcessorView view) {
    super(view);
    this.width = width;
    this.height = height;
  }

  /**
   * Takes in the relevant arguments for being able to downsize an image.
   *
   * @param width the desired width by the user
   * @param height the desired height by the user
   * @param originalImage the name of the original image to be operated on
   * @param nameOfNewImage the name of the new image to be created
   * @param images the storage of processed images so far
   * @param view the view that transmits output to the user
   * @param messageToUser a message to be shown to the user based on input
   */
  public Downsize(String width, String height, String originalImage, String nameOfNewImage,
                  ImageStorage images, ImageProcessorView view, String messageToUser) {
    super(originalImage, nameOfNewImage, images, view, messageToUser);
    this.width = width;
    this.height = height;
  }

  @Override
  public ImageModel makeModelChange(ImageModel model) {
    if (this.width == null || this.height == null) {
      try {
        this.view.renderMessage("Must enter both width and height values");
        return model;
      }
      catch (IOException e) {
        throw new IllegalStateException("'Must enter both width and height values'"
                + " couldn't be rendered");
      }
    }

    try {
      int newWidth = Integer.parseInt(this.width);
      int newHeight = Integer.parseInt(this.height);

      if (newWidth <= 0 || newWidth > model.getWidth()
              || newHeight <= 0 || newHeight > model.getHeight()) {
        try {
          this.view.renderMessage("Width and height numbers must be greater than 0 and less than"
                  + " the current dimensions");
          return model;
        }
        catch (IOException e) {
          throw new IllegalStateException("'Width and height numbers must be greater than 0 or less"
                  + " than 1' couldn't be rendered");
        }
      }

      double heightRatio = (double) model.getHeight() / newHeight;
      double widthRatio = (double) model.getWidth() / newWidth;

      Pixel[][] newContents = new Pixel[newHeight][newWidth];
      for (int i = 0; i < newHeight; i++) {
        for (int j = 0; j < newWidth; j++) {
          Pixel newPixel = this.floatingPixel(i * heightRatio, j * widthRatio, model);
          newContents[i][j] = newPixel;
        }
      }

      return new Image(newContents, model.getFilepath());
    }

    catch (NumberFormatException e) {
      try {
        this.view.renderMessage("Specified values must be integers");
        return model;
      }
      catch (IOException e2) {
        throw new IllegalStateException("'Specified values must be integers' couldn't be rendered");
      }
    }
  }

  /**
   * Returns a new pixel that represents the collection of four different pixels' colors in the
   * original model, uses floor and ceiling values.
   *
   * @param xPos the approximate x-position of the pixel
   * @param yPos the approximate y-position of the pixel
   * @param model the model to get the four pixels from
   * @return a new Pixel with new colors representing a collection of colors
   */
  private Pixel floatingPixel(double xPos, double yPos, ImageModel model) {
    int floorXPos = (int) xPos;
    int ceilingXPos = (int) xPos + 1;
    int floorYPos = (int) yPos;
    int ceilingYPos = (int) yPos + 1;

    // if the user wants to keep the same height, then offset the bottom row of pixels very
    // slightly in order to not throw an exception
    if (ceilingXPos == model.getHeight()) {
      floorXPos = floorXPos - 1;
      ceilingXPos = ceilingXPos - 1;
    }

    // if the user wants to keep the same width, then offset the right row of pixels very
    // slightly in order to not throw an exception
    if (ceilingYPos == model.getWidth()) {
      floorYPos = floorYPos - 1;
      ceilingYPos = ceilingYPos - 1;
    }

    Pixel a = model.getPixel(floorXPos, floorYPos);
    Pixel b = model.getPixel(ceilingXPos, floorYPos);
    Pixel c = model.getPixel(floorXPos, ceilingYPos);
    Pixel d = model.getPixel(ceilingXPos, ceilingYPos);

    double mRed = (b.getRed() * (xPos - floorXPos)) + (a.getRed() * (ceilingXPos - xPos));
    double nRed = (d.getRed() * (xPos - floorXPos)) + (c.getRed() * (ceilingXPos - xPos));
    int red = (int) ((nRed * (yPos - floorYPos)) + (mRed * (ceilingYPos - yPos)));

    double mGreen = (b.getGreen() * (xPos - floorXPos)) + (a.getGreen() * (ceilingXPos - xPos));
    double nGreen = (d.getGreen() * (xPos - floorXPos)) + (c.getGreen() * (ceilingXPos - xPos));
    int green = (int) ((nGreen * (yPos - floorYPos)) + (mGreen * (ceilingYPos - yPos)));

    double mBlue = (b.getBlue() * (xPos - floorXPos)) + (a.getBlue() * (ceilingXPos - xPos));
    double nBlue = (d.getBlue() * (xPos - floorXPos)) + (c.getBlue() * (ceilingXPos - xPos));
    int blue = (int) ((nBlue * (yPos - floorYPos)) + (mBlue * (ceilingYPos - yPos)));

    return new Pixel(red, green, blue);
  }
}
