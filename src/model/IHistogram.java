package model;

/**
 * Represents a histogram to make observations about an image.
 */
public interface IHistogram {

  /**
   * Returns the number of times a certain value of a image component shows up in an image.
   *
   * @param value the relevant value of the component
   * @param component the type of component (red, green, blue, intensity)
   * @return the amount of times it shows up
   */
  int getFrequency(int value, String component);

  /**
   * Gets every single value of every single component in an image model.
   *
   * @param model the model to retrieve values of components from
   */
  void getComponents(ImageModel model);
}