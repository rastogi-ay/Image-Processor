package model;

/**
 * An implementation of the histogram that creates a line chart.
 */
public class Histogram implements IHistogram {
  private final int[] red;
  private final int[] green;
  private final int[] blue;
  private final int[] intensity;

  /**
   * A constructor that initializes the red, green, blue, and intensity arrays using the model
   * to create a histogram out of.
   *
   * @param model the image model to create a histogram out of
   */
  public Histogram(ImageModel model) {
    this.red = new int[model.getWidth() * model.getHeight()];
    this.green = new int[model.getWidth() * model.getHeight()];
    this.blue = new int[model.getWidth() * model.getHeight()];
    this.intensity = new int[model.getWidth() * model.getHeight()];
    this.getComponents(model);
  }

  @Override
  public void getComponents(ImageModel model) {
    int counter = 0;
    for (int i = 0; i < model.getHeight(); i++) {
      for (int j = 0; j < model.getWidth(); j++) {
        this.red[counter] = model.getPixel(i, j).getRed();
        this.green[counter] = model.getPixel(i, j).getGreen();
        this.blue[counter] = model.getPixel(i, j).getBlue();
        this.intensity[counter] = model.getPixel(i, j).getIntensity();
        counter++;
      }
    }
  }

  @Override
  public int getFrequency(int value, String component) {
    int frequency = 0;
    int[] array;
    switch (component) {
      case ("red"):
        array = this.red;
        break;
      case ("green"):
        array = this.green;
        break;
      case ("blue"):
        array = this.blue;
        break;
      case ("intensity"):
        array = this.intensity;
        break;
      default:
        throw new IllegalArgumentException("Invalid component");
    }
    for (int i : array) {
      if (i == value) {
        frequency++;
      }
    }
    return frequency;
  }
}