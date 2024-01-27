package controller;

import java.util.HashMap;
import model.ImageModel;

/**
 * Handles user input and transmits output for the user.
 */
public interface ImageProcessorController {

  /**
   * The method through which the controller implementation is run since it reads user inputs.
   *
   * @throws IllegalStateException if output cannot be transmitted based on the user input
   */
  void runProcessor() throws IllegalStateException;

  /**
   * Returns all the images processed so far in the image processor. Only used for testing purposes
   * so this method isn't used anywhere for any functionality.
   *
   * @return the images processed so far, in a hash map
   */
  HashMap<String, ImageModel> returnImages();
}
