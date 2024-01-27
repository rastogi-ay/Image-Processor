package view;

import controller.ImageControllerGUI;

/**
 * Represents a type of view for the graphical user interface. Extends the ImageProcessorView to
 * be able to render messages.
 */
public interface ImageGUIView extends ImageProcessorView {

  /**
   * Adds features to the view in order to process higher-level user requests. The controller gets
   * information from the view in order to do things like update the model, and it passes that
   * information back to the view.
   *
   * @param controller takes information from the view and updates the view accordingly
   */
  void addFeatures(ImageControllerGUI controller);
}
