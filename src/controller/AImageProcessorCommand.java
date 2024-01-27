package controller;

import java.io.IOException;
import model.Image;
import model.ImageModel;
import model.ImageStorage;
import model.Pixel;
import view.ImageProcessorView;

/**
 * Represents a type of image processor command that is used for reducing code duplication for the
 * implementations. Abstracts common fields and methods.
 */
public abstract class AImageProcessorCommand implements ImageProcessorCommand {
  protected String originalImage;
  protected String nameOfNewImage;
  protected ImageProcessorView view;
  protected String messageToUser;
  protected ImageStorage images;
  protected String maskedImage;

  /**
   * Default constructor, can be used to call specific methods that don't require arguments.
   */
  public AImageProcessorCommand() {
    // empty body because no fields required
  }

  /**
   * A constructor that needs the view in order to transmit messages to the user, will most often
   * be used when inputs need to be parsed.
   *
   * @param view the view to render messages to
   */
  public AImageProcessorCommand(ImageProcessorView view) {
    this.view = view;
  }

  /**
   * A constructor that is used for commands that solely work with one image, and one image only.
   * This applies to loading and saving an image.
   *
   * @param originalImage the name of the original image to be operated on
   * @param images the storage of processed images so far
   * @param view the view that transmits output to the user
   * @param messageToUser a message to be shown to the user based on input
   */
  public AImageProcessorCommand(String originalImage, ImageStorage images, ImageProcessorView view,
                                String messageToUser) {
    this.originalImage = originalImage;
    this.images = images;
    this.view = view;
    this.messageToUser = messageToUser;
  }

  /**
   * A constructor that is used for commands that create a new image based on a previous one. Does
   * not require the name of a masked image, so set it to the empty String.
   *
   * @param originalImage the name of the original image to be operated on
   * @param nameOfNewImage the name of the new image to be created
   * @param images the storage of processed images so far
   * @param view the view that transmits output to the user
   * @param messageToUser a message to be shown to the user based on input
   */
  public AImageProcessorCommand(String originalImage, String nameOfNewImage, ImageStorage images,
                                ImageProcessorView view, String messageToUser) {
    this.originalImage = originalImage;
    this.nameOfNewImage = nameOfNewImage;
    this.view = view;
    this.messageToUser = messageToUser;
    this.images = images;
    this.maskedImage = "";
    // doesn't have a use for masked images, so set it to empty for the checkStorage() method below
  }

  /**
   * A constructor that is used for commands that want to make use of a masked image as well as
   * creating a new image based on a previous one.
   *
   * @param originalImage the name of the original image to be operated on
   * @param nameOfNewImage the name of the new image to be created
   * @param images the storage of processed images so far
   * @param view the view that transmits output to the user
   * @param messageToUser a message to be shown to the user based on input
   * @param maskedImage the name of the masked image for applying a partial command
   */
  public AImageProcessorCommand(String originalImage, String nameOfNewImage, String maskedImage,
                                ImageStorage images, ImageProcessorView view, String messageToUser) {
    this.originalImage = originalImage;
    this.nameOfNewImage = nameOfNewImage;
    this.view = view;
    this.messageToUser = messageToUser;
    this.images = images;
    this.maskedImage = maskedImage;
  }

  @Override
  public void checkStorage() {
    // if the specified image name isn't in the processor at all
    if (!(this.images.getImages().containsKey(this.originalImage))) {
      try {
        this.view.renderMessage(this.messageToUser);
      }
      catch (IOException e) {
        throw new IllegalStateException("'" + this.messageToUser + "'" + " couldn't be rendered");
      }
    }
    // first, check if the maskedImage name is empty, if it is, the user only specified
    // the number of arguments needed for a script not requiring a masked image
    else if (!this.maskedImage.isEmpty()) {
      if (!this.images.getImages().containsKey(this.maskedImage)) {
        // check if the storage contains the maskedImage name
        try {
          this.view.renderMessage("Unable to locate the specified masked image");
        }
        catch (IOException e) {
          throw new IllegalStateException("'Unable to locate the specified masked image' couldn't be rendered");
        }
      }
      else {
        // make a change to the model, requires a masked image
        ImageModel newModel = this.makeModelChangeWithMask(this.images.getImages().get(this.originalImage));
        this.images.addImage(this.nameOfNewImage, newModel);
      }
    }
    else {
      // make a change to the model, doesn't require a masked image
      ImageModel newModel = this.makeModelChange(this.images.getImages().get(this.originalImage));
      this.images.addImage(this.nameOfNewImage, newModel);
    }
  }

  @Override
  public ImageModel makeModelChangeWithMask(ImageModel model) {
    ImageModel maskImage = new Mask().makeModelChange(model);
    ImageModel modified = this.makeModelChange(model);

    Pixel[][] newContents = new Pixel[model.getHeight()][model.getWidth()];
    for (int i = 0; i < maskImage.getHeight(); i++) {
      for (int j = 0; j < maskImage.getWidth(); j++) {
        Pixel curr = maskImage.getPixel(i, j);
        if (curr.getRed() < 127 && curr.getBlue() < 127 && curr.getGreen() < 127) {
          // these pixels are more black than white, so these pixels should reflect the change
          newContents[i][j] = modified.getPixel(i, j);
        }
        else {
          newContents[i][j] = model.getPixel(i, j);
        }
      }
    }
    return new Image(newContents, model.getFilepath());
  }

  /**
   * Ensures that the color doesn't go outside its boundaries.
   *
   * @param color the color for checking boundaries
   * @param maxValue the maximum value allowed by the model
   * @return an integer within the boundaries for the model
   */
  protected int colorBounds(int color, int maxValue) {
    if (color < 0) {
      return 0;
    }
    else if (color > maxValue) {
      return maxValue;
    }
    else {
      return color;
    }
  }

  @Override
  public abstract ImageModel makeModelChange(ImageModel model);
}
