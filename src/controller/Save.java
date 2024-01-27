package controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import model.ImageModel;
import model.ImageStorage;
import view.ImageProcessorView;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * The command for saving an image in the image processor with a specified file path in the user's
 * directory.
 */
public class Save extends AImageProcessorCommand {
  private ImageModel model;
  private final String filename;
  private final ArrayList<String> supportedFormats;

  /**
   * A constructor that takes in the filepath, a model, and a view, which essentially avoids
   * having to use the storage system as well as the message to the user.
   *
   * @param filename the filepath to save an image to
   * @param model the model that the user wants to save
   * @param view the view for rendering certain messages
   */
  public Save(String filename, ImageModel model, ImageProcessorView view) {
    super();
    this.filename = filename;
    this.model = model;
    this.view = view;
    this.supportedFormats = new ArrayList<>();
    this.supportedFormats.add("png");
    this.supportedFormats.add("jpg");
    this.supportedFormats.add("jpeg");
    this.supportedFormats.add("bmp");
  }

  /**
   * Takes in the relevant arguments for being able to save an image.
   *
   * @param filename the filepath of the image that the user wants to upload
   * @param originalImage the name of original image to be added
   * @param images the storage of processed images so far
   * @param view the view that transmits output to the user
   * @param messageToUser a message to be shown to the user based on input
   */
  public Save(String filename, String originalImage, ImageStorage images, ImageProcessorView view,
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
    return model;
    // doesn't do anything because you're not making changes to the model when saving it
  }

  @Override
  public void checkStorage() throws IllegalStateException {
    ImageModel myModel;
    if (this.images == null) {
      myModel = this.model;
      // can only be null if the model doesn't need some sort of storage system (relevant for GUI)
    }
    else if (!this.images.getImages().containsKey(this.originalImage)) {
      try {
        this.view.renderMessage(this.messageToUser);
        return;
        // if you need a storage system, but the name of the image specified isn't in the system...
        // ... then don't save
      }
      catch (IOException e) {
        throw new IllegalStateException("'" + this.messageToUser + "'" + " couldn't be rendered");
      }
    }
    else {
      myModel = this.images.getImages().get(this.originalImage);
    }
    /*
    FIVE CASES TO CONSIDER:
    1st: PPM -> PPM
    2nd: nonPPM -> PPM
    3rd: PPM -> nonPPM
    4th: nonPPM -> nonPPM (different format)
        -- don't do anything, this is not supported
    5th: nonPPM -> nonPPM (same format)
     */
    if (this.filename.endsWith("ppm")) {
      // if the user wants to save as a ppm file
      try {
        if (myModel.getFilepath().endsWith("ppm")) {
          // if the original image is in a PPM format
          String ppm = this.fromPPMtoPPM(myModel);
          FileWriter toWrite = new FileWriter(this.filename);
          toWrite.write(ppm);
          toWrite.close();
        }
        else {
          // if the user wants a PPM file out of a non-PPM file: (ex.) JPG -> PPM
          BufferedImage image = ImageIO.read(new File(myModel.getFilepath())); // read the image
          String ppm = this.nonPPMtoPPM(image, myModel);
          FileWriter toWrite = new FileWriter(this.filename);
          toWrite.write(ppm);
          toWrite.close();
        }
      }
      catch (IOException e) {
        try {
          this.view.renderMessage("PPM file could not be saved.");
        }
        catch (IOException e2) {
          throw new IllegalStateException("'PPM file could not be saved.' couldn't be rendered");
        }
      }
    }
    // if the filepath doesn't end with PPM, then create a new image with the correct format
    else {
      try {
        if (myModel.getFilepath().endsWith("ppm")) {
          // if the user wants a non-PPM file out of a PPM file
          String newFormat = "empty";
          for (String s : this.supportedFormats) {
            if (this.filename.endsWith(s)) {
              newFormat = s;
            }
          }
          if (!newFormat.equals("empty")) {
            BufferedImage image = this.createNonPPM(myModel);
            FileOutputStream outputStream = new FileOutputStream(this.filename);
            ImageIO.write(image, newFormat, outputStream);
            outputStream.close();
          }
          else {
            try {
              this.view.renderMessage("Image format was not recognized.");
            }
            catch (IOException e) {
              throw new IllegalStateException("'Image format was not recognized.' couldn't be"
                      + " rendered");
            }
          }
        }
        else {
          // if the user wants a non-PPM file out of a non-PPM file (needs to be the same format)
          String originalFormat = "empty";
          String newFormat = "empty";
          for (String s : this.supportedFormats) {
            if (myModel.getFilepath().endsWith(s)) {
              originalFormat = s;
            }
          }
          for (String s : this.supportedFormats) {
            if (this.filename.endsWith(s)) {
              newFormat = s;
            }
          }
          if (!originalFormat.equals(newFormat)) {
            try {
              this.view.renderMessage("Image processor doesn't support converting between non-PPM" +
                      " file formats.");
            }
            catch (IOException e) {
              throw new IllegalStateException("'Image processor doesn't support converting between"
                      + " non-PPM file formats.' couldn't be rendered");
            }
          }
          else {
            BufferedImage image = this.createNonPPM(myModel);
            FileOutputStream outputStream = new FileOutputStream(this.filename);
            ImageIO.write(image, newFormat, outputStream);
            outputStream.close();
          }
        }
      }
      catch (IOException e) {
        try {
          this.view.renderMessage("An error occurred while trying to process the image");
        }
        catch (IOException e2) {
          throw new IllegalStateException("'An error occurred while trying to process the image'"
                  + " couldn't be rendered");
        }
      }
    }
  }

  /**
   * Converts a PPM file to another PPM file.
   *
   * @param model the relevant data for making a new PPM file
   * @return a new PPM file with the updated data
   */
  private String fromPPMtoPPM(ImageModel model) {
    StringBuilder builder = new StringBuilder();
    builder.append("P3").append(System.lineSeparator());
    builder.append(model.getWidth()).append(" ").append(model.getHeight())
            .append(System.lineSeparator());
    builder.append(model.getMaxValue()).append(System.lineSeparator());

    for (int i = 0; i < model.getHeight(); i++) {
      for (int j = 0; j < model.getWidth(); j++) {
        builder.append(model.getPixel(i, j).getRed()).append(System.lineSeparator());
        builder.append(model.getPixel(i, j).getGreen()).append(System.lineSeparator());
        builder.append(model.getPixel(i, j).getBlue()).append(System.lineSeparator());
      }
    }
    return builder.toString();
  }

  /**
   * Creates a PPM file out of a file that is not a PPM file.
   *
   * @param image the image representation of the non PPM file
   * @param model simply used for getting the relevant maximum value of the image
   * @return a new PPM file with the updated data
   */
  private String nonPPMtoPPM(BufferedImage image, ImageModel model) {
    StringBuilder builder = new StringBuilder();
    builder.append("P3").append(System.lineSeparator());
    builder.append(image.getWidth()).append(" ").append(image.getHeight())
            .append(System.lineSeparator());
    builder.append(model.getMaxValue()).append(System.lineSeparator());

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        Color color = new Color(image.getRGB(j, i));
        builder.append(color.getRed()).append(System.lineSeparator());
        builder.append(color.getGreen()).append(System.lineSeparator());
        builder.append(color.getBlue()).append(System.lineSeparator());
      }
    }
    return builder.toString();
  }

  /**
   * Creates a non PPM file out of another file, can be non PPM or PPM.
   *
   * @param model used for getting the width and height of the original image
   * @return an image with the updated data
   */
  private BufferedImage createNonPPM(ImageModel model) {
    BufferedImage image = new BufferedImage(model.getWidth(), model.getHeight(), TYPE_INT_RGB);
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        Color color = new Color(model.getPixel(i, j).getRed(),
                model.getPixel(i, j).getGreen(), model.getPixel(i, j).getBlue());
        int colorValue = color.getRGB();
        image.setRGB(j, i, colorValue);
      }
    }
    return image;
  }
}
