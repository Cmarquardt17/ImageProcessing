/*
 *Hunter Lloyd
 * Copyrite.......I wrote, ask permission if you want to use it outside of class. 
 */

/* Christian Marquardt
 * 1/30/2018
 * I edited this for CSCI442 assignment 1
 * I commented this program pretty extensively in case one day I have to look back on it
 * and it won't be so trivial at an inital glance!
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.prefs.Preferences;

class IMP implements MouseListener{
   JFrame frame;
   JPanel mp;
   JButton panels;
   JScrollPane scroll;
   JMenuItem openItem, exitItem, resetItem;
   Toolkit toolkit;
   File pic;
   ImageIcon img;
   int colorX, colorY;
   int [] pixels;
   int [] results;
   //Instance Fields you will be using below
   
   //This will be your height and width of your 2d array
   int height=0, width=0;
   
   //your 2D array of pixels
    int picture[][];

    /* 
     * In the Constructor I set up the GUI, the frame the menus. The open pulldown 
     * menu is how you will open an image to manipulate. 
     */
   IMP()
   {
      toolkit = Toolkit.getDefaultToolkit();
      frame = new JFrame("Image Processing Software by Hunter");
      JMenuBar bar = new JMenuBar();
      JMenu file = new JMenu("File");
      JMenu functions = getFunctions();
      frame.addWindowListener(new WindowAdapter(){
            @Override
              public void windowClosing(WindowEvent ev){ quit();}
            });
      openItem = new JMenuItem("Open");
      openItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ handleOpen(); }
           });
      resetItem = new JMenuItem("Reset");
      resetItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ reset(); }
           });     
      exitItem = new JMenuItem("Exit");
      exitItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ quit(); }
           });
      file.add(openItem);
      file.add(resetItem);
      file.add(exitItem);
      bar.add(file);
      bar.add(functions);
      frame.setSize(600, 600);
      mp = new JPanel();
      mp.setBackground(new Color(0, 0, 0));
      scroll = new JScrollPane(mp);
      frame.getContentPane().add(scroll, BorderLayout.CENTER);
      JPanel butPanel = new JPanel();
      butPanel.setBackground(Color.black);
      
      //Since the button is changed to histogram when pressed it will immediatly called that function
      panels = new JButton("Histogram");
      
      //Had to switch to true to make sure that it would show up
      panels.setEnabled(true);
      
      panels.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){
           histogram();
                  }
           });
      butPanel.add(panels);
      frame.getContentPane().add(butPanel, BorderLayout.SOUTH);
      frame.setJMenuBar(bar);
      frame.setVisible(true);      
   }
   
   /* 
    * This method creates the pulldown menu and sets up listeners to selection of the menu choices. If the listeners are activated they call the methods 
    * for handling the choice, fun1, fun2, fun3, fun4, etc. etc. 
    */
   
  private JMenu getFunctions()
  {
     JMenu fun = new JMenu("Functions");
     
     JMenuItem firstItem = new JMenuItem("MyExample - fun1 method");
     firstItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){fun1();}
           });
     
     //These are all my JMenuItems I have created for this program
         JMenuItem secondItem = new JMenuItem("Image Rotate Left");
      secondItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){imageRotateLeft();}
           });
      
      JMenuItem secondItemPt2 = new JMenuItem("Image Rotate Right");
      secondItemPt2.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){imageRotateRight();}
           });
      
      JMenuItem thirdItem = new JMenuItem("Grayscale");
      thirdItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){grayscale();}
           });
      
      JMenuItem fourthItem = new JMenuItem("ImageBlur");
      fourthItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){imageBlur();}
           });
      
      JMenuItem fifthItem = new JMenuItem("MaskDetection 3x3");
      fifthItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){maskEdge3x3();}
           });
      
      JMenuItem fifth_ptTwoItem = new JMenuItem("MaskDetection 5x5");
      fifth_ptTwoItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){maskEdge5x5();}
           });
      
      JMenuItem sixthItem = new JMenuItem("Histogram");
      sixthItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){histogram();}
           });
      
      JMenuItem seventhItem = new JMenuItem("Histogram via Equalization");
      seventhItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){equalizeImage();}
           });
      
      JMenuItem eigthItem = new JMenuItem("OrangeConverter");
      eigthItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){orangeConverter();}
           });
   
      //This is them being actually added to the pop up screen and being able to run on the click
      fun.add(firstItem);
      fun.add(secondItem);
      fun.add(secondItemPt2);
      fun.add(thirdItem);
      fun.add(fourthItem);
      fun.add(fifthItem);
      fun.add(fifth_ptTwoItem);
      fun.add(sixthItem);
      fun.add(seventhItem);
      fun.add(eigthItem);
      
      return fun;   

  }
  
  /*
   * This method handles opening an image file, breaking down the picture to a one-dimensional array and then drawing the image on the frame. 
   * You don't need to worry about this method. 
   */
    private void handleOpen()
  {  
     img = new ImageIcon();
     JFileChooser chooser = new JFileChooser();
      Preferences pref = Preferences.userNodeForPackage(IMP.class);
      String path = pref.get("DEFAULT_PATH", "");

      chooser.setCurrentDirectory(new File(path));
     int option = chooser.showOpenDialog(frame);
     
     if(option == JFileChooser.APPROVE_OPTION) {
        pic = chooser.getSelectedFile();
        pref.put("DEFAULT_PATH", pic.getAbsolutePath());
       img = new ImageIcon(pic.getPath());
      }
     width = img.getIconWidth();
     height = img.getIconHeight(); 
     
     JLabel label = new JLabel(img);
     label.addMouseListener(this);
     pixels = new int[width*height];
     
     results = new int[width*height];
  
          
     Image image = img.getImage();
        
     PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width );
     try{
         pg.grabPixels();
     }catch(InterruptedException e)
       {
          System.err.println("Interrupted waiting for pixels");
          return;
       }
     for(int i = 0; i<width*height; i++)
        results[i] = pixels[i];  
     turnTwoDimensional();
     mp.removeAll();
     mp.add(label);
     
     mp.revalidate();
  }
  
  /*
   * The libraries in Java give a one dimensional array of RGB values for an image, I thought a 2-Dimensional array would be more usefull to you
   * So this method changes the one dimensional array to a two-dimensional. 
   */
  private void turnTwoDimensional()
  {
     picture = new int[height][width];
     for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
          picture[i][j] = pixels[i*width+j];
      
     
  }
  /*
   *  This method takes the picture back to the original picture
   */
  private void reset()
  {
        for(int i = 0; i<width*height; i++)
             pixels[i] = results[i]; 
       Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width)); 

       JLabel label2 = new JLabel(new ImageIcon(img2));    
       mp.removeAll();
       mp.add(label2);
       
       //This is what helped fix the reset in the dropdown on the JPanel
       //The repaint repaints the image from overlapping on itself especailly if you slap another picture on top of the other
       turnTwoDimensional();
       mp.repaint();
       
       mp.revalidate(); 
    }
  /*
   * This method is called to redraw the screen with the new image. 
   */
  private void resetPicture()
  {
       for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
          pixels[i*width+j] = picture[i][j];
      Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width)); 

      JLabel label2 = new JLabel(new ImageIcon(img2));    
       mp.removeAll();
       mp.add(label2);
       mp.repaint();
       mp.revalidate(); 
   
    }
    /*
     * This method takes a single integer value and breaks it down doing bit manipulation to 4 individual int values for A, R, G, and B values
     */
  private int [] getPixelArray(int pixel)
  {
      int temp[] = new int[4];
      temp[0] = (pixel >> 24) & 0xff;
      temp[1]   = (pixel >> 16) & 0xff;
      temp[2] = (pixel >>  8) & 0xff;
      temp[3]  = (pixel      ) & 0xff;
      return temp;
      
    }
    /*
     * This method takes an array of size 4 and combines the first 8 bits of each to create one integer. 
     */
  private int getPixels(int rgb[])
  {
         int alpha = 0;
         int rgba = (rgb[0] << 24) | (rgb[1] <<16) | (rgb[2] << 8) | rgb[3];
        return rgba;
  }
  
  public void getValue()
  {
      int pix = picture[colorY][colorX];
      int temp[] = getPixelArray(pix);
      System.out.println("Color value " + temp[0] + " " + temp[1] + " "+ temp[2] + " " + temp[3]);
    }
  
  /**************************************************************************************************
   * This is where you will put your methods. Every method below is called when the corresponding pulldown menu is 
   * used. As long as you have a picture open first the when your fun1, fun2, fun....etc method is called you will 
   * have a 2D array called picture that is holding each pixel from your picture. 
   *************************************************************************************************/
   /*
    * Example function that just removes all red values from the picture. 
    * Each pixel value in picture[i][j] holds an integer value. You need to send that pixel to getPixelArray the method which will return a 4 element array 
    * that holds A,R,G,B values. Ignore [0], that's the Alpha channel which is transparency, we won't be using that, but you can on your own.
    * getPixelArray will breaks down your single int to 4 ints so you can manipulate the values for each level of R, G, B. 
    * After you make changes and do your calculations to your pixel values the getPixels method will put the 4 values in your ARGB array back into a single
    * integer value so you can give it back to the program and display the new picture. 
    */
  private void fun1()
  {
     
    for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
       {   
          int rgbArray[] = new int[4];
         
          //get three ints for R, G and B
          rgbArray = getPixelArray(picture[i][j]);
         
        
           rgbArray[1] = 0;
           //take three ints for R, G, B and put them back into a single int
           picture[i][j] = getPixels(rgbArray);
        } 
     resetPicture();
  }
  
  //This function will flip the picture left by setting a temp height and 
  //Switching the width and height so the pixels can be stores when it is flipped
  //NOTE: If you want to do a full roation you must alternate in called imageRotateLeft() and imageRotateRight()
  private void imageRotateLeft(){
      int[][] rotateImage = new int[width][height];
      int tempHeight = height;
      height = width;
      width = tempHeight;
      for(int i=0; i<width; i++){
       for(int j=0; j<height; j++)
       {
           //Put each pixel into our rotated image
          rotateImage[j][i] = picture[i][j];
        
        } 
      }
      
     //since our reset picture is called calling picture we set our changed image (rotateImage)
     //we set picture equal to our changed image
     picture = rotateImage;
     resetPicture();
  
  }
  
    //This function will flip the picture right by setting a temp height and 
    //Switching the width and height so the pixels can be stores when it is flipped
    private void imageRotateRight(){
      int[][] rotateImage = new int[width][height];
      int tempHeight = height;
      height = width;
      width = tempHeight;
      for(int i=0; i<width; i++){
       for(int j=0; j<height; j++)
       {
          rotateImage[j][i] = picture[width-i-1][height-j-1];
        
        } 
      }
      
     //since our reset picture is called calling picture we set our changed image (rotateImage)
     //we set picture equal to our changed image
     picture = rotateImage;
     resetPicture();
  
  }
  
  //For grayscale each pixel we are at we changed its value to get that effect
  private void grayscale(){
    int red, blue, green;
    for(int i=0; i <height; i++)
        for(int j=0; j<width; j++){
            
            int rgbArray[] = new int[4];
            rgbArray = getPixelArray(picture[i][j]);
            
            //This is the luminosity coversion of getting a grayscale
            red = (int) (rgbArray[1] * .21);
            green = (int) (rgbArray[2] * .72);
            blue = (int) (rgbArray[3] * .07);
            
            rgbArray[1] = red + blue + green;
            rgbArray[2] = red + blue + green;
            rgbArray[3] = red + blue + green;
            
            picture[i][j] = getPixels(rgbArray);
        }
    resetPicture();
  }
  
  //Once we call grayscale we then create a mask of surrounding pixels 3x3 and edit the value
  private void imageBlur(){
      grayscale();
      int average;
      int blurredImg[][] = new int[height][width];
      
      //The values are changed so we do not get an array out of bounds
      for(int i=1; i<height-1; i++)
       for(int j=1; j<width-1; j++)
       {
          int rgbArray[] = new int[4];
         
                 //This is all of the surrounding pixels and the center pixels we are about to change
                 int rgbArray1[] = getPixelArray(picture[i-1][j-1]);
                 int rgbArray2[] = getPixelArray(picture[i-1][j]);
                 int rgbArray3[] = getPixelArray(picture[i-1][j+1]);
                 int rgbArray4[] = getPixelArray(picture[i][j-1]);
                 int rgbArray5[] = getPixelArray(picture[i][j+1]);
                 int rgbArray9[] = getPixelArray(picture[i][j]);
                 int rgbArray6[] = getPixelArray(picture[i+1][j-1]);
                 int rgbArray7[] = getPixelArray(picture[i+1][j]);
                 int rgbArray8[] = getPixelArray(picture[i+1][j+1]);
                 int k =1;
                 
                 //We add up all the sourrounding pixels and the center pixel then get the average by dividing by 9
                 average= (rgbArray1[k] + rgbArray2[k] + rgbArray3[k]+ rgbArray4[k]+ 
                         rgbArray5[k]+ rgbArray6[k]+ rgbArray7[k] + rgbArray8[k]+rgbArray9[k])/9;
                
                 
                 //We then set all the rgb values to that average which gives it that blur effect
                 rgbArray[0] = 255;
                 rgbArray[1] = average;
                 rgbArray[2] = average;
                 rgbArray[3] = average;
                 
           //take three ints for R, G, B and put them back into a single int
           blurredImg[i][j] = getPixels(rgbArray);
           
        }
      
     //Since resetPicture focuses on picture we must set our picture to our blurredImage we just calculated
     picture = blurredImg;
     resetPicture();
  }
  
  
  //This is an edge detector that is 3x3 searching for edges
  private void maskEdge3x3(){
       grayscale();
       int mask[][] = new int[height][width];
       int outside, inside;
       int pixel;
       
       //Changing the bounds so we do not error out especially for a 3x3
       for(int i=1; i<height-1; i++)
       for(int j=1; j<width-1; j++)
       {
          int rgbArray[] = new int[4];
         
                //The pixel we are looking to change
                int centerPixel0[] = getPixelArray(picture[i][j]);
          
                 //Here we are getting all of the outside pixels of our center pixel and the center pixel as well
                 int pixel1[] = getPixelArray(picture[i-1][j-1]);
                 int pixel2[] = getPixelArray(picture[i-1][j]);
                 int pixel3[] = getPixelArray(picture[i-1][j+1]);
                 int pixel4[] = getPixelArray(picture[i][j-1]);
                 int pixel5[] = getPixelArray(picture[i][j+1]);
                 int pixel6[] = getPixelArray(picture[i+1][j-1]);
                 int pixel7[] = getPixelArray(picture[i+1][j]);
                 int pixel8[] = getPixelArray(picture[i+1][j+1]);
                 
                 //We use this because we are focusing on only the red channel since it is in grayscale
                 int k = 1;
                 
                 //Make all of the outside pixels negative and add them up
                 outside = (- pixel1[k] - pixel2[k] - pixel3[k] - pixel4[k] -
                         pixel5[k] - pixel6[k] - pixel7[k] - pixel8[k]);
                 
                 //Our inside pixel red val * (Amount of outside pixels)
                 inside = centerPixel0[k] * 8;
                 
                 //Get the difference in the inside and outside (average outside) pixel
                 pixel = inside + outside;
                 
                 
                 //If our pixel we are looking at is below 100 then it is not an edge
                 if(pixel < 100){
                    rgbArray[0] = 255;
                    rgbArray[1] = 0;
                    rgbArray[2] = 0;
                    rgbArray[3] = 0;
                 }
                 
                //Else we change it to white and it is apart of an edge
                 else{
                    rgbArray[0] = 255;
                    rgbArray[1] = 255;
                    rgbArray[2] = 255;
                    rgbArray[3] = 255;
                 }
                   
               mask[i][j] = getPixels(rgbArray);
           
        }
     //Set original picture to the mask that we changed 
     picture = mask;
     resetPicture();
  }
  
  //This function is the same as the above besides it being 5x5 thats searches and makes a stronger edge
   private void maskEdge5x5(){
       grayscale();
       int mask[][] = new int[height][width];
       int outside, inside;
       int pixel;
       
       //Change the bounds so we do not error out especually for a 5x5
       for(int i=2; i<height-2; i++)
       for(int j=2; j<width-2; j++)
       {
          int rgbArray[] = new int[4];
         
                //This is the pixel we are looking to change
                int centerPixel0[] = getPixelArray(picture[i][j]);
          
                 //Here we are getting all of the outside pixels of our center pixel and the center pixel as well
                 int pixel1[] = getPixelArray(picture[i-2][j-2]);
                 int pixel2[] = getPixelArray(picture[i-2][j-1]);
                 int pixel3[] = getPixelArray(picture[i-2][j]);
                 int pixel4[] = getPixelArray(picture[i-2][j+1]);
                 int pixel5[] = getPixelArray(picture[i-2][j+2]);
                 int pixel6[] = getPixelArray(picture[i-1][j+2]);
                 int pixel7[] = getPixelArray(picture[i][j+2]);
                 int pixel8[] = getPixelArray(picture[i+1][j+2]);
                 int pixel9[] = getPixelArray(picture[i+2][j+2]);
                 int pixel10[] = getPixelArray(picture[i+2][j+1]);
                 int pixel11[] = getPixelArray(picture[i+2][j]);
                 int pixel12[] = getPixelArray(picture[i+2][j-1]);
                 int pixel13[] = getPixelArray(picture[i+2][j-2]);
                 int pixel14[] = getPixelArray(picture[i+1][j-2]);
                 int pixel15[] = getPixelArray(picture[i][j-2]);
                 int pixel16[] = getPixelArray(picture[i-1][j-2]);
                 
                 //We use this because we are focusing on only the red channel since it is in grayscale
                 int k = 1;
                 
                 //Make all of the outside pixels negative and add them up
                 outside = (- pixel1[k] - pixel2[k] - pixel3[k] - pixel4[k] -
                         pixel5[k] - pixel6[k] - pixel7[k] - pixel8[k] - pixel9[k] -
                         pixel10[k] - pixel11[k] - pixel12[k] - pixel13[k] - pixel14[k] - 
                         pixel15[k] - pixel16[k]);
                 
                 
                 
                 inside = centerPixel0[k] * 16;
                 
                 //Get the difference in the inside and outside (average outside) pixel
                 pixel = inside + outside;
                 
                 //If our pixel we are looking at is below 100 then it is not an edge
                 if(pixel < 100){
                    rgbArray[0] = 255;
                    rgbArray[1] = 0;
                    rgbArray[2] = 0;
                    rgbArray[3] = 0;
                 }
                 
                 //Else we change it to white and it is apart of an edge
                 else{
                    rgbArray[0] = 255;
                    rgbArray[1] = 255;
                    rgbArray[2] = 255;
                    rgbArray[3] = 255;
                 }
                 
               mask[i][j] = getPixels(rgbArray);
           
        }
     
     //Set original picture to the mask that we changed 
     picture = mask;
     resetPicture();
  
  }
     
   private void histogram(){
       
       //We will want three different panels for each channel and create a map for it with a key assigned to each channel val
       int red[][];
       red = fillMyColorChannel(1);
       Map<Integer, Integer> rMap = constructAMap(red);
       
       int green[][];
       green = fillMyColorChannel(2);
       Map<Integer, Integer> gMap = constructAMap(green);
       
       int blue[][];
       blue = fillMyColorChannel(3);
       Map<Integer, Integer> bMap = constructAMap(blue);
    
          //Creating 3 frames for each rgb channel
          JFrame redFrame = new JFrame("Red");
          JFrame greenFrame = new JFrame("Green");
          JFrame blueFrame = new JFrame("Blue");

          //We want to send this to the constructor
          redFrame.add(new JScrollPane(new MyPanel(rMap,1)));
          greenFrame.add(new JScrollPane(new MyPanel(gMap,2)));
          blueFrame.add(new JScrollPane(new MyPanel(bMap,3)));

          //This enables that all frames are at their preferred size
          redFrame.pack();
          greenFrame.pack();
          blueFrame.pack();

          //I changed the locations because my laptop is not that wide for it to work cause it will overlap
          redFrame.setLocation(430, 0);
          greenFrame.setLocation(750, 0);
          blueFrame.setLocation(1080, 0);

          //Makes sure all the panels are visible to the user
          redFrame.setVisible(true);
          greenFrame.setVisible(true);
          blueFrame.setVisible(true);
   }
   
   
   //This is the function that will be pressed and will call some helper functions that makes it easier to read
   //If you want to see the histogram of this click the histogram button
    private void equalizeImage(){
        
        //All three channels we are going to look at
        int redChannel[][];
        int blueChannel[][];
        int greenChannel[][];
        int returnedEqualizedRgb[][];
        
        //For each channel we want to fill it with all the channel values then construct a map for that channel
        //Then we will finally want to call the equlization method for that channel and set it for 
        //Our eventual picture will be changed after each channel until the last one is complete
        
        redChannel = fillMyColorChannel(1);
        Map<Integer, Integer> rMap = constructAMap(redChannel);
        returnedEqualizedRgb = equalization(rMap, 1);
        picture = returnedEqualizedRgb;
        
        greenChannel = fillMyColorChannel(2);
        Map<Integer, Integer> gMap = constructAMap(greenChannel);
        returnedEqualizedRgb = equalization(gMap, 2);
        picture = returnedEqualizedRgb;
        
        blueChannel = fillMyColorChannel(3);
        Map<Integer, Integer> bMap = constructAMap(blueChannel);
        returnedEqualizedRgb = equalization(bMap, 3);
        picture =  returnedEqualizedRgb;

        resetPicture();
  }
    
  //We call this from equalizeImage to do all of the calculations needed for that specific channel for it to be equlized
  private int[][] equalization(Map<Integer, Integer> channel, int rgbChannel){
      
      //CDF = Culmulative distributive function 0-255
      double cdf[] = new double[256];
      
      //PMF = probability mass function 0-255
      double pmf[] = new double[256];
      
      //The pic we will eventually return
      int pic[][] = new int[height][width];
      
      //The max amount of pixels
      int maxPixels = width * height;
      
      
      //First we must calculate the PMF of every pixel then the second step is to calculate the CDF
      for(Integer key : channel.keySet()){
          double value = channel.get(key);
          
          //Do this with every key
          pmf[key] = value / maxPixels;
          
          //If the key does not exist we must do...
          if (key == 0) {
             cdf[key] = pmf[key];
          
          //Else we change the value with the previous calculated pmf of that key
          } else {
             cdf[key] = cdf[key - 1] + pmf[key];
          }
        }
      
      //Then we push all the changed values to the rgb channel at hand and return the pic
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);
                
                //Math for that channel to be stored
                rgbArray[rgbChannel] = (int) Math.round((rgbArray[rgbChannel] - 1) * cdf[rgbArray[rgbChannel]]);
                pic[i][j] = getPixels(rgbArray);
            
      }
      return pic;
  }
   //It was a lot easier to fill each array by creating a separate method rather than inside the function
   private int [][] fillMyColorChannel(int rgbVal){
        int tempChannel[][] = new int[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);
                tempChannel[i][j] = rgbArray[rgbVal];

            }
        return tempChannel;
   }
   
   //This function is to create a Map for each rgb Value and how many of the exact value there are
   private Map constructAMap(int[][] rgbVal){
       
       //Init a map for that color
       Map<Integer, Integer> thisMap = new TreeMap<>();
       
       //Go through every pixel with that color channel val
       for(int i =0; i < rgbVal.length; i++){
           for(int j= 0; j< rgbVal.length; j++){
               
               //At that current pixel we set the val to that color channel
               int val = rgbVal[i][j];
               int count;
               
               //If the Map that we created already has this value we will make note and add it 
               if(thisMap.containsKey(val)){
                   count = thisMap.get(val);
                   count++;
               }
               
               //If not we will see that key to 1 being its only one of a kind
               else{
                   count = 1;
               }
               
               //Then we will put that value into thisMap with the value and the number of times it appears
               thisMap.put(val, count);
           }
       }
       
       //We want the values of 0-255 and we are looking for which colors are not present
       for(int k = 0; k<256; k++){
           if(!thisMap.containsKey(k)){
               thisMap.put(k,0);
           }
       }
       return thisMap;
   }
   
  //In the picture that is uplaoded we are looking for any pixel rgb value that closely resembles orange
  private void orangeConverter(){
      for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
       {   
          int rgbArray[] = new int[4];
         
          //get three ints for R, G and B
          rgbArray = getPixelArray(picture[i][j]);
          
          
          //Needs to meet all of these requirements for each channel
          //If true then we change the color to white
          if((rgbArray[1] <= 255 && rgbArray[1] >= 230) && 
                  (rgbArray[2] >= 80 && rgbArray[2] <= 180) && 
                    (rgbArray[3] <= 80 && rgbArray[3] >=0))
              {
                    rgbArray[1] = 255;
                    rgbArray[2] = 255;
                    rgbArray[3] = 255;
                }
            
          else{
              
              //Else we want to change it to black
              rgbArray[1] = 0;
              rgbArray[2] = 0;
              rgbArray[3] = 0;
          }
          
          picture[i][j] = getPixels(rgbArray);
       }
        resetPicture();
  }
  
  private void quit()
  {  
     System.exit(0);
  }

    @Override
   public void mouseEntered(MouseEvent m){}
    @Override
   public void mouseExited(MouseEvent m){}
    @Override
   public void mouseClicked(MouseEvent m){
        colorX = m.getX();
        colorY = m.getY();
        System.out.println(colorX + "  " + colorY);
        getValue();
        panels.setEnabled(true);
    }
    @Override
   public void mousePressed(MouseEvent m){}
    @Override
   public void mouseReleased(MouseEvent m){}
   
   public static void main(String [] args)
   {
      IMP imp = new IMP();
   }
}