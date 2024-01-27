package view;

import java.io.IOException;

/**
 * Represents an implementation for the image processor view, transmits user output based on what
 * is in the model and controller.
 */
public class ImageProcessorViewImpl implements ImageProcessorView {
  private final Appendable output;

  public ImageProcessorViewImpl() {
    this.output = System.out;
  }

  /**
   * Takes in a specific type of output to customize how transmitted output is shown to the user.
   *
   * @param output the desired output for the user
   * @throws IllegalArgumentException if the output was null
   */
  public ImageProcessorViewImpl(Appendable output) throws IllegalArgumentException {
    if (output == null) {
      throw new IllegalArgumentException("The output to transmit to was null");
    }
    this.output = output;
  }

  @Override
  public void renderMessage(String message) throws IOException {
    try {
      this.output.append(message);
    }
    catch (IOException e) {
      throw new IOException("'" + message + "'" + " couldn't be rendered");
    }
  }
}
