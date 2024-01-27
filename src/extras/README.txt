Hello! Hope you're doing well.

MVC:
Model: two interfaces: ImageModel & ImageModelState; one implementation: Image class
    1) ImageModelState has general, observing methods for an implementation, such as methods like
    getPixel(int row, int col) as well as getMaxValue()
    2) The ImageModel interface extends the ImageModelState in order to get the observing methods,
    but we didn't want to put all the methods in one interface in order to separate model
    functionality by observing methods and methods that perform operations on the model
    3) For now, we just have one Image class implementing the ImageModel class
    4) The Image model has two constructors: one that takes in a 2D array of pixels representing
    image contents as well as a filepath to represent the location in the user's device, as well as
    another "dummy" constructor that doesn't have any pixel contents or filepath associated with it,
    it's used for calling methods from the interface

Model: Pixel
    1) Pixels are what each image are comprised of, they essentially have red, green,
    and blue components, meaning that each pixel has its own color
    2) Provides data to represent the image
    3) You can also perform methods on pixels, and we decided to make each of these methods public
    because you should be able to use these methods on any kind of image within this project

Model: interface: IHistogram; one implementation: Histogram class
    1) Represents a histogram for a user to observe based on the image that is being edited
    2) Has several relevant methods for being able to make the histogram, like getComponents()
    3) Used for the graphical user interface, specifically for making a line chart

Model: interface: ImageStorage; one implementation: ImageStorageImpl class
    1) Every time a user makes a request for processing an image, it gets stored here
    2) This is not used by the GUI since the GUI doesn't need any sort of image storage system
    3) Decided to use this in the model since it's technically a collection of images, and
    each individual image is part of the model

View: interface: ImageProcessorView; one implementation: ImageProcessorViewImpl class
    1) All this interface has for now is renderMessage(message)
    2) We just have one class implementing this interface, known as ImageProcessorViewImpl
    3) This class's constructors are similar to the view class in marble solitaire in that an
    appendable can be specified; if not, the default will be System.out

View: interface: ImageGUIView; one implementation: JFrameView class
    1) Something important to note: we have the ImageGUIView extend the ImageProcessorView because
    we wanted the graphical user interface to also be capable of rendering important messages
    2) Other than that, the only other method that the ImageGUIView has is addFeatures(), which
    essentially allows the view and the controller to work together in order to accurately reflect
    the model changes that the user expects
    3) The JFrameView constructor creates an entire GUI from Swing
    4) The GUI contains the image, histogram, and buttons for user requests

Controller: interface: ImageProcessorController; one implementation: ImageProcessorControllerImpl
    1) The interface a runProcessor() method, similar to playGame() in marble solitaire where it
    allows users to put inputs for running the program, this is where the controller does all of
    its work parsing through inputs and giving that information to the model as well as the view
    2) The implementation takes in a view, and the user input through a readable
    3) We used to have it take in a model, but we realized there's no more use for it because
    the controller instead makes use of the image storage system

Controller: interface: ImageProcessorCommand; eight implementations
    1) ImageProcessorCommand is a function object that essentially runs a command based on user
    inputs, and with that, it'll update different things: the model, view, storage of images, etc.
    2) We put this in the controller package because the controller makes use of this command
    pattern in order to perform the correct implementation of the function object on an image

Controller: interface: ImageControllerGUI; one implementation: ImageControllerGUIImpl
    1) The controller for the GUI has all the methods that represent the information that needs
    to be passed back to the JFrameView in order to correctly display changes when the user does something
    2) Makes use of a Features implementation in order to mainly make changes to the model, or save
    an image with a specified file path

Controller: interface: Features; one implementation: FeaturesImpl
    1) Represents higher-level user requests that a user can carry out through the GUI
    2) Called by the ImageControllerGUIImpl class

Controller: ImageUtil
    1) Simply "helps" the actual model by giving data to represent the images
    2) To be precise, the ImageUtil class essentially reads a PPM or non PPM file and parses through
    the entire file in order to return a 2D array of Pixels that have all the correct RGB values
    3) The methods are static since this method would be the same for all implementations of an image


RUNNING THE PROGRAM:
There are three different ways to run the program. Two implementations, the ImageProcessor and
ImageGUI, don't actually make use of the args parameter in the main method. Instead, the ImageProcessor
waits for user input through the console (interactive scripting) and the ImageGUI opens up a
graphical user interface through swing for user interaction. The last way is the ImageProgram method,
which actually does make use of the args. It does so to check the validity of a JAR file. A JAR file
can be used in three different ways. It can be used to run the ImageProcessor or ImageGUI just
mentioned, or it can run a .txt file of script commands. See below:

Interactive scripting: java -jar Program.jar -text
Graphical user interface: java -jar Program.jar
Running a file: java -jar Program.jar -file path-of-script-file


DESIGN CHANGES:
For Assignment 5:
We had to make a few major design changes. One big mistake we made from the last assignment was
that our model did not have an apply method for our function object. This means that every function
object we had essentially had to have itself show up somehow in our model, which was poor design
because that meant every implementation of the model would have to have all those methods. Therefore,
we added an apply method that would make changes to the model based on a passed in function object.
Another big design change was that we made an abstract class for our function object, which helped
reduce code duplication a lot for methods and fields.

For Assignment 6:
We didn't have to change too much about our previous design. The only big design change was that
we used to have our image storage system be a part of our Image class, which didn't make a lot of
sense because it was weird for each Image to have a reference to all the images being processed so
far through each Image having a hash map field. Instead, create an entirely separate model storage
system for just the controller to have access to, and the controller will make use of it to update
model and view accordingly.

For Assignment 8:
For implementing the downsize feature, none of the code had to be changed, it was just a matter of
making a new class that implemented the ImageProcessorCommand interface, and writing all the logic
there. As for mask images, there were some design changes necessary. According to the assignment,
the user was able to type in "blur source-image dest-image" but could now also type:
"blur source-image mask-image dest-image". Unfortunately, we read the directions wrong, and we thought
the user was able to do something like "blur source-image dest-image mask-image".
This unfortunately made this process unnecessarily complicated, but it still works just fine. Essentially,
we had to change the existing constructor for the commands that supported mask images (everything except for flip,
load, save, downsize, and mosaic). This constructor would take in a masked image name on top of its
previous arguments. We also created a new method for this interface that would make a model change to
the image data with regard to it's masked image. We also had to change our method that would run the
controller in order to account for the fact that the masked image name may not exist.


CITATIONS:
For our tests, we made a 2x2 pixel image with 1 red, 1 green, and 2 blue pixels.
We authorize ourselves to use this image.

For running the script files, we found an image of the Eiffel Tower  as well as a jungle that were free to
use. We got them from Unsplash, which provides images that are free to use.