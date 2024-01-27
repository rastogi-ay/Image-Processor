package controller;

import java.io.IOException;
import java.util.ArrayList;
import model.ImageModel;
import model.Image;
import model.ImageStorage;
import view.ImageProcessorView;

/**
 * Stores an image into the image processor, which allows further processing on that image.
 */
public class Load extends AImageProcessorCommand {
  private final String filename;
  private final ArrayList<String> supportedFormats;

  /**
   * Just takes in the filepath for loading an image. Also has a list of supported formats.
   *
   * @param filename the path of the image file
   */
  public Load(String filename) {
    super();
    this.filename = filename;
    this.supportedFormats = new ArrayList<>();
    this.supportedFormats.add("png");
    this.supportedFormats.add("jpg");
    this.supportedFormats.add("jpeg");
    this.supportedFormats.add("bmp");
  }

  /**
   * Takes in the relevant arguments for being able to load an image.
   *
   * @param filename the filepath of the image that the user wants to upload
   * @param originalImage the name of original image to be added
   * @param images the storage of processed images so far
   * @param view the view that transmits output to the user
   * @param messageToUser a message to be shown to the user based on input
   */
  public Load(String filename, String originalImage, ImageStorage images, ImageProcessorView view,
              String messageToUser) {
    super(originalImage, images, view, messageToUser);
    this.filename = filename;
    this.supportedFormats = new ArrayList<>();
    this.supportedFormats.add("png");
    this.supportedFormats.add("jpg");
    this.supportedFormats.add("jpeg");
    this.supportedFormats.add("bmp");
  }

  @Override
  public ImageModel makeModelChange(ImageModel model) {
    String format = "empty";
    for (String s : this.supportedFormats) {
      if (this.filename.endsWith(s)) {
        format = s;
      }
    }
    if (this.filename.endsWith("ppm")) {
      return new Image(ImageUtil.readPPM(this.filename), this.filename);
    }
    else if (!format.equals("empty")) {
      return new Image(ImageUtil.readNonPPM(this.filename), this.filename);
    }
    else {
      return null;
      // other methods check for null
    }
  }

  @Override
  public void checkStorage() throws IllegalStateException {
    ImageModel dummyImage = new Image();
    try {
      ImageModel newImage = this.makeModelChange(dummyImage);
      if (newImage == null) {
        try {
          this.view.renderMessage(this.messageToUser);
        }
        catch (IOException e) {
          throw new IllegalStateException("'" + this.messageToUser + "'" + " couldn't be rendered");
        }
      }
      else {
        this.images.addImage(this.originalImage, newImage);
      }
    }
    catch (IllegalArgumentException e) {
      try {
        this.view.renderMessage(e.getMessage());
      }
      catch (IOException e2) {
        throw new IllegalStateException("'" + e.getMessage() + "'" + " couldn't be rendered");
      }
    }
  }
}
