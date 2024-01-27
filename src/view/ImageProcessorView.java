package view;

import java.io.IOException;

/**
 * Represents the view that contains the transmitted output to the user.
 */
public interface ImageProcessorView {

  /**
   * Shows a message to the user based on user inputs.
   *
   * @param message the message to be shown
   * @throws IOException if the message cannot successfully be transmitted
   */
  void renderMessage(String message) throws IOException;
}
