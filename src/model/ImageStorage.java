package model;

import java.util.HashMap;

/**
 * This part of the model serves as storage for the processed images so far.
 */
public interface ImageStorage {

  /**
   * Returns all processed images in the form of a hash map.
   *
   * @return a hash map representing the images
   */
  HashMap<String, ImageModel> getImages();

  /**
   * Adds an image to this implementation of the storage system.
   *
   * @param name the name of the image to be added
   * @param image the data of the image to be added
   */
  void addImage(String name, ImageModel image);
}
