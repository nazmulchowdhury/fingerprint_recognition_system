
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

public class ImagePanel extends JPanel
{
    private BufferedImage im;
    
    public ImagePanel(int width, int height)
    {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(width, height));
    }
    
    public void reset()
    {
        im = null;
        repaint();
        revalidate();
    }
    
    public void setImage(BufferedImage image)
    {
        im = image;
        setPreferredSize(new Dimension(im.getWidth(), im.getHeight()));
        repaint();
        revalidate();
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        int pWidth = getWidth(), pHeight = getHeight();
        
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, pWidth, pHeight);
        
        if (im != null)
        {
            int x = (pWidth - im.getWidth())/2, y = (pHeight - im.getHeight())/2;
            g2.drawImage(im, x, y, this);
        }
    }
}