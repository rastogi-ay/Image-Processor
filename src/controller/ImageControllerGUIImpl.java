package controller;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;
import java.io.IOException;
import model.Histogram;
import model.IHistogram;
import model.ImageModel;
import view.ImageGUIView;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * A type of controller for the graphical user interface that makes changes to the model and
 * updates the view accordingly.
 */
public class ImageControllerGUIImpl implements ImageControllerGUI {
  private final Features delegate;
  private ImageModel model;
  private final ImageGUIView view;

  /**
   * Takes in the model and the view for updating based on the user input.
   *
   * @param model the model to be changed
   * @param view the view to have messages rendered to
   */
  public ImageControllerGUIImpl(ImageModel model, ImageGUIView view) {
    if (model == null || view == null) {
      throw new IllegalArgumentException("At least one of the given parameters was null");
    }
    this.delegate = new FeaturesImpl();
    this.model = model;
    this.view = view;
  }

  @Override
  public void loadImage(String filePath) {
    ImageModel newModel = this.delegate.loadImage(new Load(filePath), this.model);
    if (newModel == null) {
      // represents a case where a user tries to load an unsupported format
      try {
        this.view.renderMessage("An error occurred while trying to process the file. Make sure that"
                + " it falls under the supported formats.");
      }
      catch (IOException e) {
        throw new IllegalStateException("'An error occurred while trying to process the file." +
                " Make sure that it falls under the supported formats.' couldn't be rendered");
      }
    }
    else {
      this.model = newModel;
    }
  }

  @Override
  public void saveImage(String filePath) {
    if (this.model.getFilepath() == null) { // could just check if this.model == null???*****
      // represents when a user tries to perform an operation on an image without loading it first
      try {
        this.view.renderMessage("Must load an image into the program first");
      }
      catch (IOException e) {
        throw new IllegalStateException("'Must load an image into the program first'"
                + " couldn't be rendered");
      }
    }
    else {
      Save saveCommand = new Save(filePath, this.model, this.view);
      this.delegate.saveImage(saveCommand);
    }
  }

  @Override
  public void updateImage(ImageProcessorCommand command) {
    if (this.model.getFilepath() == null) {
      // represents when a user tries to perform an operation on an image without loading it first
      try {
        this.view.renderMessage("Must load an image into the program first");
      }
      catch (IOException e) {
        throw new IllegalStateException("'Must load an image into the program first'"
                + " couldn't be rendered");
      }
    }
    else {
      this.model = this.delegate.changeImage(command, this.model);
    }
  }

  @Override
  public BufferedImage renderImage() {
    if (this.model == null || this.model.getFilepath() == null) {
      return new BufferedImage(1, 1, TYPE_INT_RGB);
      // return a dummy image, can't render an actual image due to bad inputs by the user
    }
    else {
      BufferedImage image = new BufferedImage(this.model.getWidth(), this.model.getHeight(),
              TYPE_INT_RGB);
      for (int i = 0; i < image.getHeight(); i++) {
        for (int j = 0; j < image.getWidth(); j++) {
          Color color = new Color(this.model.getPixel(i, j).getRed(),
                  this.model.getPixel(i, j).getGreen(), this.model.getPixel(i, j).getBlue());
          int colorValue = color.getRGB();
          image.setRGB(j, i, colorValue);
        }
      }
      return image;
    }
  }

  @Override
  public BufferedImage renderHistogram() {
    IHistogram histogram = new Histogram(this.model);
    int height = 1500;
    BufferedImage image = new BufferedImage(300,height,1);
    Graphics chart = image.createGraphics();
    for (int i = 1; i <= this.model.getMaxValue(); i++) {
      chart.drawLine(i - 1,height - histogram.getFrequency(i - 1,"red"),
              i, height - histogram.getFrequency(i, "red"));
      chart.setColor(new Color(255,0,0));

      chart.drawLine(i - 1,height - histogram.getFrequency(i - 1,"blue"),
              i, height - histogram.getFrequency(i, "blue"));
      chart.setColor(new Color(0,0,255));

      chart.drawLine(i - 1,height - histogram.getFrequency(i - 1,"green"),
              i, height - histogram.getFrequency(i, "green"));
      chart.setColor(new Color(0,255,0));

      chart.drawLine(i - 1,height - histogram.getFrequency(i - 1,"intensity"),
              i, height - histogram.getFrequency(i, "intensity"));
      chart.setColor(new Color(255,200,25));
    }

    chart.dispose();
    return image;
  }
}
