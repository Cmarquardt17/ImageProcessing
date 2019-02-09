
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Map;

public class MyPanel extends JPanel
{
  private int rgbVal;
  private Map<Integer, Integer> thisMap;

    BufferedImage grid;
    Graphics2D gridCov;

	public MyPanel(Map<Integer, Integer> thisMap, int rgbVal)
	{
            
        //Needed to create a local copy for this class
	this.rgbVal = rgbVal;
        this.thisMap = thisMap;
        
        //When calling this it will initialize a Panel with these dimensions
        Dimension size = new Dimension(305, 600);
        
        //Save some lines in IMP by using this function
        setPreferredSize(size);
 	}

     public void clear()
    {
       grid = null;
       repaint();
    }
     
    //I had a hard time figuring out how to use the given code so had to look up ways to create a general graph
    public void paintComponent(Graphics g)
    { 
         //Here we want to call our super class
         super.paintComponent(g);  
         
         //Our map should never empty if we are looking at a picture so we will conduct this below
         if (thisMap != null) {
             
            //Creates the portion of the graph of the histogram so it doesnt go outside
            int xCoordBorder = 15;
            int yCoordBorder = 15;
            int widthOfGraph = 285;
            int heightOfGraph = 580;
            
            
            //This portion generates the background of each Panel
            Graphics2D gridCov = (Graphics2D) g;
            gridCov.setColor(Color.BLACK);
            gridCov.drawRect(xCoordBorder,  yCoordBorder, widthOfGraph, heightOfGraph);
            int lineThickness = 1;
            int maxValue = 0;
            
            //This function works to find the most used value in our Map we created for each channel value
            for (Integer key : thisMap.keySet()) {
                int value = thisMap.get(key);
                
                //Goes from beginning to end setting the highest val
                if(value > maxValue)
                    maxValue = value;
            }
            
            //This is our set position
            int xPosition = 25;
            
            //Goes through each key and that rgb channel value to create that specific bar while also making sure it is
            for (Integer key : thisMap.keySet()) {
                
                int value = thisMap.get(key);
                
                //Sized correctly in the graph and doesnt overflow
                int lineHeight = Math.round(((float) value / (float) maxValue) * heightOfGraph);
                int yPosition = heightOfGraph +  yCoordBorder - lineHeight;
                Rectangle2D line = drawing(xPosition, yPosition, lineThickness, lineHeight);
                gridCov.fill(line);
               
                //Create an if, else if, else statement for each rgbVal so it will appear to be that color on the graph
                if(rgbVal == 1){
                 gridCov.setColor(new Color(key,0,0));
                }
                else if(rgbVal ==2){
                 gridCov.setColor(new Color(0,key,0));
                }
                else{
                 gridCov.setColor(new Color(0,0,key));
                }
                
                 //This method call is within the package that draws the line of the rgb channel value to the histogram
                 gridCov.draw(line);
                xPosition += 1;
            }
            
             //After we are done using the value we will reset the value
             gridCov.dispose();
        }
    }
    
    //Couldnt figure out how to create the graph itself so I used this source https://docs.oracle.com/javase/7/docs/api/java/awt/geom/Rectangle2D.html
    private Rectangle2D drawing(int xDraw, int yDraw, int width, int height) {
        return new Rectangle2D.Float(xDraw, yDraw, width, height);
    }
}
