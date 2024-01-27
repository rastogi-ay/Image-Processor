package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import model.Image;
import model.ImageModel;
import model.ImageStorage;
import model.Pixel;
import view.ImageProcessorView;

/**
 * Class to represent the method for applying a mosaic effect to an image, given a number of seeds.
 */
public class Mosaic extends AImageProcessorCommand {
  private final String seeds;

  /**
   * Takes in the number of seeds that the user wants for the mosaic, as well as the view for
   * rendering any sort of important messages.
   *
   * @param seeds the number of seeds desired by the user
   * @param view the view for rendering messages
   */
  public Mosaic(String seeds, ImageProcessorView view) {
    super(view);
    this.seeds = seeds;
  }

  public Mosaic(String seeds, String originalImage, String nameOfNewImage, ImageStorage images,
                ImageProcessorView view, String messageToUser) {
    super(originalImage, nameOfNewImage, images, view, messageToUser);
    this.seeds = seeds;
  }

  /**
   * Represents a simple data structure that keeps track of the x-position and y-position.
   */
  public static class Posn {
    private final int x;
    private final int y;

    /**
     * Takes in the x-position and y-position for storage.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Posn(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Posn)) {
        return false;
      }
      Posn that = (Posn) o;
      return this.x == that.x && this.y == that.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.x, this.y);
    }
  }

  /**
   * Represents a seed in a mosaic, that has a cluster of pixels all with the same color.
   */
  public static class Seed {
    private final Posn position;
    private final List<Pixel> cluster;
    private final List<Posn> pixelPositions;

    /**
     * Represents a seed of the mosaic that has an average color based on the pixels surrounding it.
     * The randomness of the position is not handled by this constructor; instead, it is handled by
     * an outside method in order to more easily account for non-unique positions.
     *
     * @param position the position of the seed on the image, chosen randomly and uniquely
     */
    public Seed(Posn position) {
      this.position = position;
      this.cluster = new ArrayList<>();
      this.pixelPositions = new ArrayList<>();
    }

    /**
     * Adds a specific pixel to this seed for processing the average colors later on. This method
     * adds the pixel for the RGB values, as well as the position of that pixel relative to the
     * image because there is no supported functionality of getting the position of a pixel.
     *
     * @param pixel the pixel to be added
     * @param posn the position of the pixel
     */
    private void addPixelAndPosition(Pixel pixel, Posn posn) {
      this.cluster.add(pixel);
      this.pixelPositions.add(posn);
    }

    /**
     * Gets the average color of the pixels in a seed's cluster. Should only be used when the
     * cluster is completely filled.
     *
     * @return the pixel with the average colors of the pixels in the cluster.
     */
    private Pixel getAverageColor() {
      int totalRed = 0;
      int totalGreen = 0;
      int totalBlue = 0;
      for (Pixel pixel : this.cluster) {
        totalRed = totalRed + pixel.getRed();
        totalGreen = totalGreen + pixel.getGreen();
        totalBlue = totalBlue + pixel.getBlue();
      }
      int averageRed = totalRed / this.cluster.size();
      int averageGreen = totalGreen / this.cluster.size();
      int averageBlue = totalBlue / this.cluster.size();
      return new Pixel(averageRed, averageGreen, averageBlue);
    }
  }

  @Override
  public ImageModel makeModelChange(ImageModel model) {
    try {
      int numSeeds = Integer.parseInt(this.seeds);
      if (numSeeds <= 0) {
        try {
          this.view.renderMessage("Number of seeds must be a positive integer");
          return model;
        }
        catch (IOException e2) {
          throw new IllegalStateException("'Number of seeds must be a positive integer'"
                  + " couldn't be rendered");
        }
      }
      Pixel[][] newContents = new Pixel[model.getHeight()][model.getWidth()];

      // initializes all the positions for the number of seeds specified by the user
      ArrayList<Seed> seeds = this.initializeSeeds(model, numSeeds);

      // goes through each pixel in the copy, gets the closest seed to the pixel at position (i, j)
      // when the seed is identified, add the pixel at position (i, j) to the seed's cluster
      for (int i = 0; i < model.getHeight(); i++) {
        for (int j = 0; j < model.getWidth(); j++) {
          int indexOfClosestSeed = this.getClosestSeedIndex(seeds, i, j);
          seeds.get(indexOfClosestSeed).addPixelAndPosition(model.getPixel(i, j), new Posn(i, j));
        }
      }

      // reconstruct the image based on the new information in the list of seeds
      for (Seed seed : seeds) {
        Pixel averageColor = seed.getAverageColor();
        for (int pixelCount = 0; pixelCount < seed.cluster.size(); pixelCount++) {
          int xPos = seed.pixelPositions.get(pixelCount).x;
          int yPos = seed.pixelPositions.get(pixelCount).y;
          newContents[xPos][yPos] = averageColor;
        }
      }
      return new Image(newContents, model.getFilepath());
    }
    catch (NumberFormatException e) {
      try {
        this.view.renderMessage("Specified value must be an integer");
        return model;
      }
      catch (IOException e2) {
        throw new IllegalStateException("'Specified value must be an integer'"
                + " couldn't be rendered");
      }
    }
  }

  /**
   * Initializes all the seed's positions, doing so in a random fashion. If a position is already
   * chosen, then find a new position for that seed.
   *
   * @param model gives the dimensions for choosing random x and y coordinates
   * @return a list of seeds with randomized, unique positions
   */
  private ArrayList<Seed> initializeSeeds(ImageModel model, int numSeeds) {
    ArrayList<Seed> seedsToReturn = new ArrayList<>();
    ArrayList<Posn> positionsSoFar = new ArrayList<>();
    for (int countSeed = 0; countSeed < numSeeds; countSeed++) {
      int xPos = new Random().nextInt(model.getHeight());
      int yPos = new Random().nextInt(model.getWidth());
      Posn randomPosition = new Posn(xPos, yPos);
      if (!positionsSoFar.contains(randomPosition)) {
        seedsToReturn.add(new Seed(randomPosition));
        positionsSoFar.add(randomPosition);
      }
      else {
        countSeed = countSeed - 1;
      }
    }
    return seedsToReturn;
  }

  /**
   * Determines which seed is closest to a pixel given the position of the pixel.
   *
   * @param seeds the list of seeds to parse through for determining the seed index to return
   * @param pixelXPos the x-position of the pixel
   * @param pixelYPos the y-position of the pixel
   * @return the index of the closest seed in regard to the list
   */
  private int getClosestSeedIndex(ArrayList<Seed> seeds, int pixelXPos, int pixelYPos) {
    double minDistanceSoFar = Math.hypot(seeds.get(0).position.x - pixelXPos,
            seeds.get(0).position.y - pixelYPos);
    // initializing the minimum distance so far as the distance from the pixel to the first seed
    int seedToReturn = 0;
    // initializing this the first seed in the list, but could definitely change
    for (int currentSeed = 0; currentSeed < seeds.size(); currentSeed++) {
      double currentMinDistance = Math.hypot(seeds.get(currentSeed).position.x - pixelXPos,
              seeds.get(currentSeed).position.y - pixelYPos);
      if (currentMinDistance < minDistanceSoFar) {
        minDistanceSoFar = currentMinDistance;
        seedToReturn = currentSeed;
      }
    }
    return seedToReturn;
  }
}