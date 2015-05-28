
import java.io.*;
import java.util.*;

public class Matcher
{
    private MatchInfo bestMatch;
    
    public Matcher(String pName)
    {
        if (!FingerUtils.hasTemplate(pName,false))
        {
            System.out.println("No template information found for " + pName);
            System.exit(0);
        }
        if (!FingerUtils.hasLabel(pName,false)) 
        {
            System.out.println("No label information found for " + pName);
            System.exit(0);
        }
        
        ArrayList<String> prints = collectPrints(pName);
        if (prints.isEmpty())
        {
            System.out.println("No other prints found.");
            System.exit(0);
        }
        
        // build match info for the supplied print name
        MatchInfo testFinger = new MatchInfo(pName,false);
        
        // build match information for all the other prints, calculating their match scores
        MatchInfo[] matches = new MatchInfo[prints.size()];
        for (int i=0; i < prints.size(); i++)
        {
            matches[i] = new MatchInfo( prints.get(i),true);
            matches[i].score(testFinger);
        }
        
        Arrays.sort(matches);
        bestMatch = matches[0];
    }
    
    public java.awt.image.BufferedImage getMatchedLabel ()
    {
        return bestMatch.getLabel();
    }
    
    public int getScore()
    {
        return bestMatch.getScore();
    }
    
    private ArrayList<String> collectPrints(String pName)
    {
        ArrayList<String> prints = new ArrayList<>();
        String fnm, printName;
        
        File[] listOfFiles = new File(FingerUtils.PRINT_DIR).listFiles();
        for (int i = 0; i < listOfFiles.length; i++)
        {
            if (listOfFiles[i].isFile())
            {
                fnm = listOfFiles[i].getName();
                int labelPos = fnm.lastIndexOf(FingerUtils.LABEL_EXT);
                if (labelPos != -1)
                {
                    printName = fnm.substring(0, labelPos);
                    
                    if (FingerUtils.hasTemplate(printName,true))
                    {
                        System.out.println("Found print " + printName);
                        prints.add(printName);
                    }
                    else
                        System.out.println("File " + printName + " has labelled image but no template info");
                }
            }
        }
        return prints;
    }
}