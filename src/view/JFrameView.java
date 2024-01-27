package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.*;
import controller.BrightDark;
import controller.ColorTransformation;
import controller.Component;
import controller.Downsize;
import controller.Filter;
import controller.HorizontalFlip;
import controller.ImageControllerGUI;
import controller.Mosaic;
import controller.VerticalFlip;

/**
 * Represents a type of view that uses Swing, this view is a type of graphical user interface.
 */
public class JFrameView extends JFrame implements ImageGUIView {
  private final JTextArea message;
  private final JLabel renderedImage;
  private final JLabel renderedHistogram;
  private final JFileChooser loadFile;
  private final JFileChooser saveFile;
  private final JButton load;
  private final JButton save;
  private final JButton horizontal;
  private final JButton vertical;
  private final JButton greyscale;
  private final JButton sepia;
  private final JButton blur;
  private final JButton sharpen;
  private final JButton red;
  private final JButton green;
  private final JButton blue;
  private final JButton luma;
  private final JButton value;
  private final JButton intensity;
  private final JTextField brightDark;
  private final JTextField mosaic;
  private final JButton downsize;

  /**
   * Simply lays out all the relevant components of the graphical user interface.
   */
  public JFrameView() {
    super();
    JPanel leftSide;
    JPanel rightSide;
    JPanel loadSaveImagePanel;
    JPanel imageChangesPanel;
    JPanel centerSide;
    JPanel messagePanel;
    JPanel histogramPanel;
    JPanel imagePanel;

    this.setSize(1300,800);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("Assignment 6");
    this.setLayout(new BorderLayout());
    this.message = new JTextArea();
    this.renderedImage = new JLabel();
    this.renderedHistogram = new JLabel();
    this.brightDark = new JTextField();
    this.mosaic = new JTextField();
    leftSide = new JPanel();
    rightSide = new JPanel();
    centerSide = new JPanel();
    messagePanel = new JPanel();
    histogramPanel = new JPanel();
    imagePanel = new JPanel();
    messagePanel.add(this.message);
    histogramPanel.add(this.renderedHistogram);
    imagePanel.add(this.renderedImage);

    JPanel outerLoadSaveImagePanel = new JPanel();
    outerLoadSaveImagePanel.setSize(300, 300);
    loadSaveImagePanel = new JPanel();
    this.load = new JButton("Load");
    this.save = new JButton("Save");
    this.loadFile = new JFileChooser("C:/Users/Ayush/Documents/CodingProjects/CS3500/hw4/src/res/");
    this.saveFile = new JFileChooser("C:/Users/Ayush/Documents/CodingProjects/CS3500/hw4/src/res/");
    // "C:/Users/Ayush/Documents/CodingProjects/CS3500/hw4/src/res/"
    this.loadFile.setVisible(false);
    this.saveFile.setVisible(false);
    loadSaveImagePanel.add(this.load);
    loadSaveImagePanel.add(this.loadFile);
    loadSaveImagePanel.add(this.save);
    loadSaveImagePanel.add(this.saveFile);

    JPanel outerImageChangesPanel = new JPanel();
    outerImageChangesPanel.setSize(150, 800);
    imageChangesPanel = new JPanel();
    imageChangesPanel.setLayout(new GridLayout(12, 1));
    this.horizontal = new JButton("Horizontal Flip");
    this.vertical = new JButton("Vertical Flip");
    this.greyscale = new JButton("Greyscale");
    this.sepia = new JButton("Sepia");
    this.blur = new JButton("Blur");
    this.sharpen = new JButton("Sharpen");
    this.red = new JButton("Red");
    this.green = new JButton("Green");
    this.blue = new JButton("Blue");
    this.luma = new JButton("Luma");
    this.value = new JButton("Value");
    this.intensity = new JButton("Intensity");
    this.downsize = new JButton("Downsize");
    JLabel brightDarkLabel = new JLabel("Brighten / darken an image: ");
    JLabel mosaicLabel = new JLabel("Mosaic an image: ");
    JLabel downsizeLabel = new JLabel("Downsize an image: ");

    imageChangesPanel.add(this.horizontal);
    imageChangesPanel.add(this.vertical);
    imageChangesPanel.add(this.greyscale);
    imageChangesPanel.add(this.sepia);
    imageChangesPanel.add(this.blur);
    imageChangesPanel.add(this.sharpen);
    imageChangesPanel.add(this.red);
    imageChangesPanel.add(this.green);
    imageChangesPanel.add(this.blue);
    imageChangesPanel.add(this.luma);
    imageChangesPanel.add(this.value);
    imageChangesPanel.add(this.intensity);
    imageChangesPanel.add(brightDarkLabel);
    imageChangesPanel.add(this.brightDark);
    imageChangesPanel.add(mosaicLabel);
    imageChangesPanel.add(this.mosaic);
    imageChangesPanel.add(downsizeLabel);
    imageChangesPanel.add(this.downsize);

    outerLoadSaveImagePanel.add(loadSaveImagePanel);
    outerImageChangesPanel.add(imageChangesPanel);
    leftSide.add(outerLoadSaveImagePanel);
    rightSide.add(outerImageChangesPanel);

    imagePanel.setLayout(new GridLayout(1,0,10,10));
    JScrollPane imageScroll = new JScrollPane(this.renderedImage);
    imageScroll.setPreferredSize(new Dimension(700,300));
    imagePanel.add(imageScroll);

    histogramPanel.setLayout(new GridLayout(1,0,10,10));
    JScrollPane histogramScroll = new JScrollPane(this.renderedHistogram);
    histogramScroll.setPreferredSize(new Dimension(300,400));
    histogramPanel.add(histogramScroll);
    centerSide.add(imagePanel);
    centerSide.add(histogramPanel);

    this.add(leftSide, BorderLayout.WEST);
    this.add(rightSide, BorderLayout.EAST);
    this.add(messagePanel, BorderLayout.SOUTH);
    this.add(centerSide, BorderLayout.CENTER);
    this.setVisible(true);
  }

  @Override
  public void addFeatures(ImageControllerGUI controller) {
    this.load.addActionListener(e -> {
      if (!this.loadFile.isVisible()) {
        this.loadFile.setVisible(true);
      }
      int option = this.loadFile.showOpenDialog(null);
      if (option == JFileChooser.APPROVE_OPTION) {
        controller.loadImage(this.loadFile.getSelectedFile().getAbsolutePath());
        this.renderImageAndHistogram(controller);
      }
    });
    this.save.addActionListener(e -> {
      if (!this.saveFile.isVisible()) {
        this.saveFile.setVisible(true);
      }
      int option = this.saveFile.showSaveDialog(null);
      if (option == JFileChooser.APPROVE_OPTION) {
        controller.saveImage(this.saveFile.getSelectedFile().getAbsolutePath());
      }
    });
    this.horizontal.addActionListener(e -> {
      controller.updateImage(new HorizontalFlip());
      this.renderImageAndHistogram(controller);
    });
    this.vertical.addActionListener(e -> {
      controller.updateImage(new VerticalFlip());
      this.renderImageAndHistogram(controller);
    });
    this.greyscale.addActionListener(e -> {
      controller.updateImage(new ColorTransformation("greyscale"));
      this.renderImageAndHistogram(controller);
    });
    this.sepia.addActionListener(e -> {
      controller.updateImage(new ColorTransformation("sepia"));
      this.renderImageAndHistogram(controller);
    });
    this.blur.addActionListener(e -> {
      controller.updateImage(new Filter("blur"));
      this.renderImageAndHistogram(controller);
    });
    this.sharpen.addActionListener(e -> {
      controller.updateImage(new Filter("sharpen"));
      this.renderImageAndHistogram(controller);
    });
    this.red.addActionListener(e -> {
      controller.updateImage(new Component("red-component"));
      this.renderImageAndHistogram(controller);
    });
    this.green.addActionListener(e -> {
      controller.updateImage(new Component("green-component"));
      this.renderImageAndHistogram(controller);
    });
    this.blue.addActionListener(e -> {
      controller.updateImage(new Component("blue-component"));
      this.renderImageAndHistogram(controller);
    });
    this.luma.addActionListener(e -> {
      controller.updateImage(new Component("luma-component"));
      this.renderImageAndHistogram(controller);
    });
    this.value.addActionListener(e -> {
      controller.updateImage(new Component("value-component"));
      this.renderImageAndHistogram(controller);
    });
    this.intensity.addActionListener(e -> {
      controller.updateImage(new Component("intensity-component"));
      this.renderImageAndHistogram(controller);
    });
    this.brightDark.addActionListener(e -> {
      controller.updateImage(new BrightDark(this.brightDark.getText(), this));
      this.renderImageAndHistogram(controller);
    });
    this.mosaic.addActionListener(e -> {
      controller.updateImage(new Mosaic(this.mosaic.getText(), this));
      this.renderImageAndHistogram(controller);
    });
    this.downsize.addActionListener(e -> {
      String width = JOptionPane.showInputDialog("Enter a new width");
      String height = JOptionPane.showInputDialog("Enter a new height");
      controller.updateImage(new Downsize(width, height, this));
      this.renderImageAndHistogram(controller);
    });
  }

  @Override
  public void renderMessage(String message) throws IOException {
    this.message.setText(message);
  }

  /**
   * A method used for reducing code duplication, renders the image and the histogram when an
   * input is made by the user.
   *
   * @param controller the controller that will make changes to the model and view based on input
   */
  private void renderImageAndHistogram(ImageControllerGUI controller) {
    this.renderedImage.setIcon(new ImageIcon(controller.renderImage()));
    this.renderedHistogram.setIcon(new ImageIcon(controller.renderHistogram()));
  }
}
