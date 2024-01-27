package model;

/**
 * This model represents the data to be passed into an image processor. The fields represent the
 * state of a specific image.
 */
public class Image implements ImageModel {
  private int height;
  private int width;
  private Pixel[][] contents;
  private String filepath;

  /**
   * Used to create a dummy image model, doesn't actually contain any image data.
   */
  public Image() {
    // no fields required, so empty body
  }

  /**
   * Represents an image with a certain 2D array of pixels as well as a filepath from the user's
   * device.
   *
   * @param contents the 2D array of pixels that make up the image itself
   * @param filepath the path to the image on the user's device
   */
  public Image(Pixel[][] contents, String filepath) {
    this.contents = contents;
    this.height = this.contents.length;
    this.width = this.contents[0].length;
    this.filepath = filepath;
  }

  @Override
  public int getMaxValue() {
    return 255;
  }

  @Override
  public Pixel getPixel(int row, int col) throws IllegalArgumentException {
    if (row < 0 || row > this.height - 1 || col < 0 || col > this.width - 1) {
      throw new IllegalArgumentException("Specified pixel is out of bounds");
    }
    return this.contents[row][col];
  }

  @Override
  public int getHeight() {
    int height = this.height;
    return height;
  }

  @Override
  public int getWidth() {
    int width = this.width;
    return width;
  }

  @Override
  public String getFilepath() {
    String s = this.filepath;
    return s;
  }
}
