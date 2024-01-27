package controller;

import model.ImageModel;

/**
 * An implementation of the Features class to carry out user requests. Used in the controller for
 * the graphical user interface in order to make changes to the model as well as write new files.
 */
public class FeaturesImpl implements Features {

  @Override
  public ImageModel loadImage(ImageProcessorCommand command, ImageModel model) {
    return command.makeModelChange(model);
  }

  @Override
  public void saveImage(ImageProcessorCommand saveCommand) {
    saveCommand.checkStorage();
  }

  @Override
  public ImageModel changeImage(ImageProcessorCommand command, ImageModel model) {
    return command.makeModelChange(model);
  }
}
