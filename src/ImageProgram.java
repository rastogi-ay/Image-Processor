import java.io.InputStreamReader;
import java.io.StringReader;
import controller.ImageControllerGUI;
import controller.ImageControllerGUIImpl;
import controller.ImageProcessorController;
import controller.ImageProcessorControllerImpl;
import model.Image;
import model.ImageModel;
import view.ImageGUIView;
import view.ImageProcessorView;
import view.ImageProcessorViewImpl;
import view.JFrameView;

/**
 * The entry point for the user to run the program, can be run in different ways.
 */
public class ImageProgram {

  /**
   * The method through which the image program runs.
   *
   * @param args the arguments inputted by the user
   */
  public static void main(String[] args) {
    ImageModel model;
    ImageProcessorView view;
    ImageGUIView viewGUI;
    ImageProcessorController controller;
    ImageControllerGUI controllerGUI;

    if (args.length == 1 && args[0].equals("-text")) {
      Readable readable = new InputStreamReader(System.in);
      view = new ImageProcessorViewImpl();
      controller = new ImageProcessorControllerImpl(view, readable);
      controller.runProcessor();
    }
    else if (args.length == 2 && args[0].equals("-file")) {
      Readable readable = new StringReader(args[0] + " " + args[1]);
      view = new ImageProcessorViewImpl();
      controller = new ImageProcessorControllerImpl(view, readable);
      controller.runProcessor();
    }
    else if (args.length == 0) {
      model = new Image();
      viewGUI = new JFrameView();
      controllerGUI = new ImageControllerGUIImpl(model, viewGUI);
      viewGUI.addFeatures(controllerGUI);
    }
    else {
      System.out.println("JAR file script was not inputted correctly");
    }
  }
}