
import java.awt.image.*;

public class MatchInfo implements Comparable<MatchInfo>
{
    private String printName;
    private double[] template;
    private int score;     
    private BufferedImage labelledPrint = null;
    
    public MatchInfo(String pName, boolean check)
    {
        if (check)
        {
            printName = pName;
            template = FingerUtils.loadTemplate(printName,true);
            if (template == null)
                System.out.println("No template found for " + printName);
        }
        else
        {
            printName = pName;
            template = FingerUtils.loadTemplate(printName,false);
            if (template == null)
                System.out.println("No template found for " + printName);
        }
    }
    
    public void score(MatchInfo mi)
    {
        double[] tmplt = mi.getTemplate();
        if ((tmplt == null) || (template == null)) 
        {
            System.out.println("Could not match templates for " + mi.getPrintName() + " and " + printName);
            score = 0;
        }
        else
            score = FingerUtils.match(tmplt, template, 65, false); 
    }
    
    public String getPrintName()
    {
        return printName;
    }
    
    public double[] getTemplate()
    {
        return template;  
    }
    
    public int getScore()
    {
        return score;
    }
    
    @Override
    public int compareTo(MatchInfo mi) 
    {
        // means that an array of MatchInfo objects will be sorted into descending order
        return mi.getScore() - score;
    }
    
    @Override
    public String toString()
    {
        return printName + "; score = " + score;
    }
    
    public BufferedImage getLabel()
    {
        if (labelledPrint == null)
        {
            labelledPrint = FingerUtils.loadLabel(printName,true);
            if (labelledPrint == null)
                System.out.println("No labelled image found for " + printName);
        }
        return labelledPrint;
    }
}