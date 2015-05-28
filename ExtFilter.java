
import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ExtFilter extends FileFilter 
{
    private String ext;
    
    public ExtFilter(String ext)
    {
        this.ext = ext.toLowerCase();
    }
    
    @Override
    public String getDescription()
    {
        return ext;
    }
    
    @Override
    public boolean accept(File file)
    {
        if (file.isDirectory())
            return true;
        else
        {
            String path = file.getAbsolutePath().toLowerCase();
            if (path.endsWith("." + ext))
                return true;
        }
        return false;
    }
}