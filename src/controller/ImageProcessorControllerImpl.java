package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Function;
import model.ImageModel;
import model.ImageStorage;
import model.ImageStorageImpl;
import view.ImageProcessorView;

/**
 * A specific implementation for the controller, uses a map of commands to make changes to an image
 * based on interactive scripting. Also makes use of an image storage.
 */
public class ImageProcessorControllerImpl implements ImageProcessorController {
  private final ImageProcessorView view;
  private final Readable readable;
  private final HashMap<String, Function<Scanner, ImageProcessorCommand>> commands;
  private final ImageStorage images;

  /**
   * Represents an implementation for the image processor controller that updates the different
   * types of images and a passed-in view based on user input from the readable.
   *
   * @param view the view to transmit output to
   * @param readable where user input comes from
   * @throws IllegalArgumentException if the view or readable are null
   */
  public ImageProcessorControllerImpl(ImageProcessorView view, Readable readable)
          throws IllegalArgumentException {
    if (view == null || readable == null) {
      throw new IllegalArgumentException("At least one of the given parameters was null");
    }

    this.view = view;
    this.readable = readable;
    this.images = new ImageStorageImpl();
    this.commands = new HashMap<>();
    this.commands.put("load", s -> new Load(s.next(), s.next(), this.images, this.view,
            "An error occurred while trying to process the file. Make sure that it"
                    + " falls under the supported formats"));
    this.commands.put("save", s -> new Save(s.next(), s.next(), this.images, this.view,
            "Cannot save an image that has not been stored in the program"));
    this.commands.put("vertical-flip", s -> new VerticalFlip(s.next(), s.next(), this.images,
            this.view, "Cannot flip an image that has not been stored in the program"));
    this.commands.put("horizontal-flip", s -> new HorizontalFlip(s.next(), s.next(), this.images,
            this.view, "Cannot flip an image that has not been stored in the program"));
    this.commands.put("brighten", s -> new BrightDark(s.next(), s.next(), s.next(), s.nextLine().trim(),
            this.images, this.view, "Cannot brighten an image that has not been stored in the program"));
    this.commands.put("darken", s -> new BrightDark(s.next(), s.next(), s.next(), s.nextLine().trim(),
            this.images, this.view, "Cannot darken an image that has not been stored in the program"));
    this.commands.put("red-component", s -> new Component("red-component", s.next(),
            s.next(), s.nextLine().trim(), this.images, this.view, "Cannot get the"
            + " red-component of an image that has not been stored in the program"));
    this.commands.put("green-component", s -> new Component("green-component", s.next(),
            s.next(), s.nextLine().trim(), this.images, this.view, "Cannot get the"
            + " green-component of an image that has not been stored in the program"));
    this.commands.put("blue-component", s -> new Component("blue-component", s.next(),
            s.next(), s.nextLine().trim(), this.images, this.view, "Cannot get the"
            + " blue-component of an image that has not been stored in the program"));
    this.commands.put("value-component", s -> new Component("value-component", s.next(),
            s.next(), s.nextLine().trim(), this.images, this.view, "Cannot get the"
            + " value-component of an image that has not been stored in the program"));
    this.commands.put("luma-component", s -> new Component("luma-component", s.next(),
            s.next(), s.nextLine().trim(), this.images, this.view, "Cannot get the"
            + " luma-component of an image that has not been stored in the program"));
    this.commands.put("intensity-component", s -> new Component("intensity-component",
            s.next(), s.next(), s.nextLine().trim(), this.images, this.view, "Cannot"
            + " get the intensity-component of an image that has not been stored in the program"));
    this.commands.put("blur", s -> new Filter("blur", s.next(), s.next(), s.nextLine().trim(),
            this.images, this.view, "Cannot apply a filter to an image that has not"
            + " been stored in the program"));
    this.commands.put("sharpen", s -> new Filter("sharpen", s.next(), s.next(), s.nextLine().trim(),
            this.images, this.view, "Cannot apply a filter to an image that has not"
            + " been stored in the program"));
    this.commands.put("greyscale", s -> new ColorTransformation("greyscale", s.next(), s.next(),
            s.nextLine().trim(), this.images, this.view, "Cannot apply a"
            + " color transformation to an image that has not been stored in the program"));
    this.commands.put("sepia", s -> new ColorTransformation("sepia", s.next(), s.next(),
            s.nextLine().trim(), this.images, this.view, "Cannot apply a"
            + " color transformation to an image that has not been stored in the program"));
    this.commands.put("mosaic", s -> new Mosaic(s.next(), s.next(), s.next(), this.images,
            this.view, "Cannot mosaic an image that has not been stored in the program"));
    this.commands.put("mask", s -> new Mask(s.next(), s.next(), this.images, this.view,
            "Cannot mask an image that has not been stored in the program"));
    this.commands.put("downsize", s -> new Downsize(s.next(), s.next(), s.next(), s.next(),
            this.images, this.view, "Cannot downsize an image that has not been stored"
            + " in the program"));
  }

  @Override
  public void runProcessor() throws IllegalStateException {
    Scanner scanner = new Scanner(this.readable);
    while (scanner.hasNext()) {
      String next = scanner.next();
      if (next.equalsIgnoreCase("q") || (next.equalsIgnoreCase("quit"))) {
        return;
      }
      if (next.equals("-file")) {
        String scriptName = scanner.next();
        File file = new File(scriptName);
        try {
          scanner = new Scanner(file);
        }
        catch (FileNotFoundException e) {
          try {
            this.view.renderMessage("File " + scriptName + " not found!");
          }
          catch (IOException e1) {
            throw new IllegalStateException("Couldn't render the error message");
          }
        }
      }
      else {
        ImageProcessorCommand command;
        Function<Scanner, ImageProcessorCommand> cmd = this.commands.getOrDefault(next, null);
        if (cmd == null) {
          try {
            this.view.renderMessage("Could not recognize the command: " + next);
          }
          catch (IOException e) {
            throw new IllegalStateException("'Could not recognize the command' couldn't be"
                    + " rendered");
          }
        }
        else {
          command = cmd.apply(scanner);
          command.checkStorage();
        }
      }
    }
  }

  @Override
  public HashMap<String, ImageModel> returnImages() {
    return this.images.getImages();
  }
}
