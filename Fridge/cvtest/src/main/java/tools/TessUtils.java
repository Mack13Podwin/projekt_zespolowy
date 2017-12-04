package tools;



import com.sun.jna.Pointer;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TessAPI1;
import net.sourceforge.tess4j.util.ImageIOHelper;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.file.Paths;

public class TessUtils {
    private ITesseract instance;
    ITessAPI.TessBaseAPI handle;

    public TessUtils() {

        handle = TessAPI1.TessBaseAPICreate();
        String datapath = Paths.get("tessdata").toAbsolutePath().toString();
        String language = "bpd+dotc+dots+hydro+jd+eng";
        TessAPI1.TessBaseAPIInit3(handle, datapath, language);
        TessAPI1.TessBaseAPISetVariable(handle, "tessedit_char_whitelist", "0123456789-/.");
    }

    String[][] getAllChoices(BufferedImage image) {
        ByteBuffer buf = ImageIOHelper.convertImageData(image);
        int bpp = image.getColorModel().getPixelSize();
        int bytespp = bpp / 8;
        int bytespl = (int) Math.ceil(image.getWidth() * bpp / 8.0);
        TessAPI1.TessBaseAPISetPageSegMode(handle, ITessAPI.TessPageSegMode.PSM_SINGLE_BLOCK);
        TessAPI1.TessBaseAPISetImage(handle, buf, image.getWidth(), image.getHeight(), bytespp, bytespl);
        TessAPI1.TessBaseAPISetVariable(handle, "save_blob_choices", "T");
        TessAPI1.TessBaseAPIRecognize(handle, null);
        ITessAPI.TessResultIterator ri = TessAPI1.TessBaseAPIGetIterator(handle);
        int level = ITessAPI.TessPageIteratorLevel.RIL_SYMBOL;

        int i = 0;
        int j = 0;
        int jmax = 0;

        String[][] symbols = new String[100][10];
        double[][] confidences = new double[100][10];
        if (ri != null) {
            do {
                Pointer symbol = TessAPI1.TessResultIteratorGetUTF8Text(ri, level);
                if (symbol != null) {
                    ITessAPI.TessChoiceIterator ci = TessAPI1.TessResultIteratorGetChoiceIterator(ri);
                    do {
                        String choice = TessAPI1.TessChoiceIteratorGetUTF8Text(ci);
                        symbols[i][j] = choice;
                        confidences[i][j] = TessAPI1.TessChoiceIteratorConfidence(ci);
                        j++;
                        if (jmax < j)
                            jmax = j;
                    } while (TessAPI1.TessChoiceIteratorNext(ci) == ITessAPI.TRUE);
                    TessAPI1.TessChoiceIteratorDelete(ci);
                }
                i++;
                j = 0;
                TessAPI1.TessDeleteText(symbol);
            } while (TessAPI1.TessResultIteratorNext(ri, level) == ITessAPI.TRUE);
            return symbols;
        }
        return null;
    }
}
