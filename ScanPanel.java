
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import com.googlecode.javacv.cpp.videoInputLib.*;
import com.googlecode.javacpp.Loader;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

public class ScanPanel extends JPanel
{
    private static final int WIDTH = 252;
    private static final int HEIGHT = 269;
    private static final int DELAY = 500;
    private static final int CAMERA_ID = 0;
    
    // ignore detected contour boxes smaller than SMALLEST_BOX pixels
    private static final float SMALLEST_BOX =  1000.0f;
    
    // for specifying the size of the largest possible contour box
    private static final double BOX_FRAC =  1;
    
    // for cropping the top/bottom of the fingerprint image
    private static final double CROP_FRAC =  1;
    
    // x- length of final fingerprint (same as in the Biometric SDK)
    private static final double X_LEN =  323.0;
    
    private IplImage snapIm = null;
    private volatile boolean isRunning;
    
    // holds the current fingerprint box outline polygon
    private Polygon gridPoly;    
    
    // has an outline been found?
    private boolean foundOutline = false;
    
    // the extracted fingerprint image
    private BufferedImage fpImage;  
    
    private int fileCount = 0;
    
    // where the labelled fingerprint is displayed
    private ImagePanel skelPanel;   
    private String printName = null;
    
    public ScanPanel(ImagePanel skelPanel)
    {
        this.skelPanel = skelPanel;
        setBackground(Color.white);
        gridPoly = new Polygon(); 
        
        // start updating the panel's image
        //new Thread(this).start();   
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(WIDTH, HEIGHT);
    }
    
//      @Override
//      public void run()
//      {
//      
//      }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        if (snapIm == null)
        {
            g.setColor(Color.BLUE);
            g.drawString("Loading from camera " + CAMERA_ID + "...", 5, HEIGHT/2);
        }
        else
            g2.drawImage(snapIm.getBufferedImage(), 0, 0, this);
        
        if (foundOutline)
        {
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(6));
            synchronized(gridPoly)
            {
                g2.drawPolygon(gridPoly);
            }
        }
    }
    
    // ------------------------- fingerprint processing ----------------------------
    
    public void findPrint()
    {
        fpImage = null;
        //fpPanel.reset();
        // not found an outline yet
        foundOutline = false;     
        skelPanel.reset();
        
        if (snapIm == null)
            return;
        
        // convert to grayscale and equalize
        IplImage grayImg = IplImage.create(cvGetSize(snapIm), IPL_DEPTH_8U, 1);
        cvCvtColor(snapIm, grayImg, CV_BGR2GRAY); 
        cvEqualizeHist(grayImg, grayImg);
        
        // blur fingerprint into a black blob
        IplImage blobImg = IplImage.create(cvGetSize(snapIm), IPL_DEPTH_8U, 1);
        // convert print into grayish blob
        cvErode(grayImg, blobImg, null, 5);    
        
        // change blob to black using thresholding
        cvThreshold(blobImg, blobImg, 150, 255, CV_THRESH_BINARY); 
        
//        reducing the threshold value makes more things white in the resulting blob
//        threshold value, max_value
//        sharpen the fingerprint (which also adds a lot of general noise to the image)
        IplImage threshImg = IplImage.create(cvGetSize(snapIm), IPL_DEPTH_8U, 1);
        cvAdaptiveThreshold(grayImg, threshImg, 255, CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY, 5, 2);
        
        /* remove the noise surrounding the fingerprint while using the black blob as a mask
           to protect the fingerprint itself */
        
        // remove threshImg areas that are white in blobImg
        cvMax(threshImg, blobImg, threshImg);
        
        // carry out more noise reduction to improve the white insides of the fingerprint
        cvSmooth(threshImg, threshImg, CV_MEDIAN, 3);
        cvEqualizeHist(threshImg, threshImg);
        
        IplImage largeFPImg = null;
        
        /* find a contour box near the center of the image, which should be the outline
           of the fingerprint  */
        IplImage boxImg = IplImage.create(cvGetSize(snapIm), IPL_DEPTH_8U, 1);
        // so fingerprint is white on black background
        cvNot(blobImg, boxImg);   
        CvRect centerBox = boxNearCenter(boxImg);
        
        if (centerBox == null)
        {
            System.out.println("No center box found in blob image");
        }
        else
        {
            // System.out.println("Box: " + centerBox);
            // calculate the bounded box around the selected contour
            int x = centerBox.x();
            int y = centerBox.y();
            int w = centerBox.width();
            int h = centerBox.height();
            // cvRectangle(origImg, cvPoint(x, y), cvPoint(x+w, y+h), CvScalar.RED, 3, CV_AA, 0);
            
            // store the box's outline in the polygon object for later drawing
            synchronized(gridPoly)
            {
                // add points in clockwise order
                gridPoly.reset();  
                gridPoly.addPoint(x, y);
                gridPoly.addPoint(x+w, y);
                gridPoly.addPoint(x+w, y+h);
                gridPoly.addPoint(x, y+h);
            }
            foundOutline = true;
            
            // crop top and bottom part of fingerprint from threshImg into fpImg
            int hFrac = (int)(h * CROP_FRAC);
            int yFrac = y + (h-hFrac)/2;
            IplImage fpImg = cvCreateImage(cvSize(w, hFrac), IPL_DEPTH_8U, 1);
            cvSetImageROI(threshImg, cvRect(x, yFrac, w, hFrac) );
            cvCopy(threshImg, fpImg);
            cvResetImageROI(threshImg);
            
            // scale the image so it's x- dimension == X_LEN
            double scale = X_LEN / fpImg.width();
            largeFPImg = cvCreateImage( cvSize((int)(fpImg.width()*scale), (int)(fpImg.height()*scale)), IPL_DEPTH_8U, 1);
            if (scale > 1) 
                cvResize(fpImg, largeFPImg, CV_INTER_CUBIC);  
            else 
                // shrink
                cvResize(fpImg, largeFPImg, CV_INTER_AREA);
            // IplImage --> BufferedImage
            fpImage = largeFPImg.getBufferedImage();
        }
    }
   
    /* return the bounded box of the contour nearest the image's center,
       whose box area is the biggest within the range SMALLEST_BOX -- maxBox
    */
    private CvRect boxNearCenter(IplImage boxImg)
    {
        // this upper limit stops the entire image being selected as an outline
        int maxBox = (int)Math.round((boxImg.width() * boxImg.height())*BOX_FRAC);
        // System.out.println("Max box size: " + maxBox);
        
        // generate all the contours in the image
        CvSeq contours = new CvSeq(null);
        CvMemStorage storage = CvMemStorage.create();
        cvFindContours(boxImg, storage, contours, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
        
        // center of the image
        int xCenter = boxImg.width()/2;
        int yCenter = boxImg.height()/2;
        
        // squared distance from center
        int minDist2 = xCenter*xCenter + yCenter*yCenter;    
        CvRect centerBox = null;
        
        /* find the convex box contour nearest the center, whose box area is the biggest
           within the range SMALLEST_BOX -- maxBox */
        while (contours != null && !contours.isNull()) 
        {
            if (contours.elem_size() > 0) 
            {
                CvSeq quad = cvApproxPoly(contours, Loader.sizeof(CvContour.class), storage, CV_POLY_APPROX_DP, cvContourPerimeter(contours)*0.02, 0);
                CvSeq convexHull = cvConvexHull2(quad, storage, CV_CLOCKWISE, 1);
                //System.out.println("Found hull");
                if (convexHull != null)
                {
                    CvRect boundBox = cvBoundingRect(convexHull, 0);
                    int area = boundBox.width()*boundBox.height();
                    if ((area > SMALLEST_BOX) && (area < maxBox))
                    {
                        int dist2 = distApart2(xCenter, yCenter, boundBox);
                        if (minDist2 > dist2)
                        {
                            // nearer center than the previous best match?
                            minDist2 = dist2;
                            centerBox = boundBox;
                            // System.out.println("New nearest box: " + centerBox);
                        }
                    }
                }
            }
            contours = contours.h_next();
        }
        return centerBox;
    }
    
    private int distApart2(int xc, int yc, CvRect box)
    {
        // squared distance between (xc,yc) and and the center of the box
        int xBox = box.x() + box.width()/2;
        int yBox = box.y() + box.height()/2;
        return ((xc - xBox)*(xc -xBox) + (yc - yBox)*(yc - yBox));
    }
    
    // ----------------------------- build fingerprint -------------------------
    
    /* Save the fingerprint image, and use it to create a FingerPrint object
     which contains template and labelled image info. 
     Display the labelled fingerprint image in the skelPanel ImagePanel
    */
    public void analyzePrint(String printName)
    {
        if (fpImage == null)
            return;
        // fileCount++;
        
        JFileChooser jfc = new JFileChooser(FingerUtils.PRINT_DIR);
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.addChoosableFileFilter(new ExtFilter("png"));
        jfc.setSelectedFile(new File(printName));
        int userSelection = jfc.showSaveDialog(this);
        
        if (userSelection == JFileChooser.CANCEL_OPTION)
            return;
        
        // extract print name from the selected filename
        if (userSelection == JFileChooser.APPROVE_OPTION) 
        {
            String fnm = jfc.getSelectedFile().getName();
            printName  = FingerUtils.extractPrintName(fnm);
        }
        
        if (printName == null)
            return;
        
        /* create a FingerPrint object which contains template and
           labelled image info */
        FingerPrint fp = new FingerPrint(printName, fpImage , true);
        
        // display the labelled fingerprint image in the skelPanel ImagePanel
        BufferedImage labelledImage = fp.getLabelledImage();
        if (labelledImage != null)
            skelPanel.setImage(labelledImage);
    }
    
    public void mtchAnalyzePrint(String printName)
    {
        this.printName = printName;
        if (fpImage == null)
            return;
        
        if (printName == null)
            return;
        
        FingerPrint fp = new FingerPrint(printName, fpImage, false);
        BufferedImage labelledImage = fp.getLabelledImage();
        
        if (labelledImage != null)
            skelPanel.setImage(labelledImage);
    }
    
    public void getAbsPath(String path)
    {
        snapIm = cvLoadImage(path);
    }
}