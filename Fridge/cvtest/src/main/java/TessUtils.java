import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;

public class TessUtils {
    private ITesseract instance;
    public TessUtils(){
        instance = new Tesseract();
        instance.setDatapath(Paths.get("tessdata").toAbsolutePath().toString());
        instance.setLanguage("bpd+dotc+dots+hydro+jd+eng");
        instance.setTessVariable("tessedit_char_whitelist", "0123456789-/.");
    }
    String getText(BufferedImage image){
        try{
            String result = instance.doOCR(image);
            return result;

        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
