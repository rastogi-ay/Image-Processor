package model;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of the storage system for processed images. May be used for some MVC designs
 * and may not be used. If this part of the model is not used, the hash map should be null.
 */
public class ImageStorageImpl implements ImageStorage {
  private HashMap<String, ImageModel> images;

  /**
   * Initializes the storage system to be empty, no images are added.
   */
  public ImageStorageImpl() {
    this.images = new HashMap<>();
  }

  @Override
  public HashMap<String, ImageModel> getImages() {
    HashMap<String, ImageModel> copy = new HashMap<>();
    for (Map.Entry<String, ImageModel> e : this.images.entrySet()) {
      String elementName = e.getKey();
      ImageModel element = e.getValue();
      copy.put(elementName, element);
    }
    return copy;
  }

  @Override
  public void addImage(String name, ImageModel image) {
    this.images.put(name, image);
  }
}
