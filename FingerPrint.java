
import java.awt.*;
import java.awt.image.*;

public class FingerPrint
{
    private static final int LINE_LEN = 5;
    private String printName;
    private double[] template;
    private BufferedImage labelIm;
    
    public FingerPrint(String pName, BufferedImage im, boolean check)
    {
        printName = pName;
        if (im == null)
            return;
        int imWidth = im.getWidth();
        int imHeight = im.getHeight();
        
        // save the fingerprint to a file
        FingerUtils.savePrint(printName, im, check);
        
        // create the template and labelled image
        byte[][] skel = FingerUtils.binarize(im);
        template = FingerUtils.buildTemplate(skel, imWidth, imHeight);
        labelIm = labelImage(skel, template, imWidth, imHeight);
        
        // save the template and labelled image in files
        System.out.println();
        FingerUtils.saveTemplate(printName, template, check);
        FingerUtils.saveLabel(printName, labelIm, check);
    }
    
    /* The labelled fingerprint image combines the fingerprint and template data.
       The fingerprint ridges are drawn in blue.
       The template's minutiae data is drawn in the following ways:
       - bifurcations: red boxes;
       - ridge ends: green boxes;
       - the print's core: a red '+' drawn using lines.
    */
    private BufferedImage labelImage(byte[][] skel, double[] tmplt, int imWidth, int imHeight)
    {
        double xCore = tmplt[1];
        double yCore = tmplt[2];
        
        // draw the finger print
        BufferedImage im = new BufferedImage(imWidth, imHeight, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < imWidth; i++)
        {
            for (int j = 0; j < imHeight; j++)
            {
                if (skel[i][j] == 1)
                    im.setRGB(i, j, Color.BLUE.getRGB());
                else
                    im.setRGB(i, j, Color.WHITE.getRGB());
            }
        }
        
        // draw a red box for each bifurcation and a green box for each ridge ending
        Graphics2D g2d = im.createGraphics();
        for (int i = 7; i < tmplt[0]; i = i+6)
        {
            if (tmplt[i+4] > 1)
            {
                // examine the "number-of-ends" field for each template entry
                // ridge bifurcation
                g2d.setColor(Color.RED);
                g2d.drawRect((int)tmplt[i] + (int)xCore-3, (int)tmplt[i+1] + (int)yCore-2, LINE_LEN, LINE_LEN);
            }
            else if (tmplt[i+4] == 1)
            {
                // ridge ending
                g2d.setColor(Color.GREEN);
                g2d.drawRect((int)tmplt[i] + (int)xCore-3, (int)tmplt[i+1] + (int)yCore-2, LINE_LEN, LINE_LEN);
            }
        }
        
        // draw the fingprint's core
        g2d.setColor(Color.RED);
        int len = 2*LINE_LEN;
        g2d.drawLine((int)xCore-len, (int)yCore, (int)xCore+len, (int)yCore);
        g2d.drawLine((int)xCore, (int)yCore-len, (int)xCore, (int)yCore+len);
        
        return im;
    }
    
    public BufferedImage getLabelledImage()
    {
        return labelIm;
    }
    
    public double[] getTemplate()
    {
        return template;
    }
    
    public String getTemplateString()
    {
        return FingerUtils.templateToString(template);
    }
}