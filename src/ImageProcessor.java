import java.io.InputStreamReader;

import controller.ImageProcessorController;
import controller.ImageProcessorControllerImpl;
import view.ImageProcessorView;
import view.ImageProcessorViewImpl;

/**
 * The entry point for the user to run the image processor.
 */
public class ImageProcessor {

  /**
   * The method through which the image processor runs.
   *
   * @param args the arguments inputted by the user, the first command should be load
   */
  public static void main(String[] args) {
    ImageProcessorView view;
    ImageProcessorController controller;
    Readable readable = new InputStreamReader(System.in);

    view = new ImageProcessorViewImpl();
    controller = new ImageProcessorControllerImpl(view, readable);
    controller.runProcessor();
  }
}
