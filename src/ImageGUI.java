import controller.ImageControllerGUI;
import controller.ImageControllerGUIImpl;
import model.ImageModel;
import model.Image;
import view.ImageGUIView;
import view.JFrameView;

/**
 * The entry point for the user to run the graphical user interface.
 */
public class ImageGUI {

  /**
   * The method through which the graphical user interface runs.
   *
   * @param args not needed because graphical user interface doesn't support interactive scripting
   */
  public static void main(String[] args) {
    ImageModel model = new Image();
    ImageGUIView view = new JFrameView();
    ImageControllerGUI controller = new ImageControllerGUIImpl(model, view);
    view.addFeatures(controller);
  }
}
