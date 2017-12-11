package tools;



import com.sun.jna.Pointer;
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.util.ImageIOHelper;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    String getMostProbableText(BufferedImage image)
    {
        ITesseract instance = new Tesseract();
        instance.setDatapath(Paths.get("tessdata").toAbsolutePath().toString());
        instance.setLanguage("bpd+dotc+dots+hydro+jd+eng");
        instance.setTessVariable("tessedit_char_whitelist", "0123456789-/.");

        try{
            String result = instance.doOCR(image);
            return result;

        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
    String getDays(String text, String[][]symbols, int jmax)
    {
        for(int it = 0; it<jmax;it++) {
            if (Objects.equals(symbols[0][it], "3")) {
                text = text.concat("3");
                System.out.println(text);
                for (int it2 = 0; it2 < jmax; it2++) {
                    if (symbols[1][it2].matches("[0-1]")) {
                        text = text.concat(symbols[1][it2]);
                        System.out.println(text);
                        break;
                    }
                }
                if (text.length() != 2)
                    return "error";
                break;
            }
            if (symbols[0][it].matches("[0-2]")) {
                text = text.concat(symbols[0][it]);
                for (int it2 = 0; it2 < jmax; it2++) {
                    if (symbols[1][it].matches("[0-9]")) {
                        text = text.concat(symbols[1][it2]);
                        break;
                    }
                }
                if (text.length() != 2)
                    return "error";
                break;
            }
        }
        return text;
    }
    String getMonths(String text, String[][] symbols, int jmax)
    {
        for(int it = 0; it<jmax;it++)
        {
            if(Objects.equals(symbols[3][it], "0"))
            {
                text=text.concat("0");
                for(int it2=0;it2<jmax;it2++)
                {
                    if(symbols[4][it2].matches("[1-9]"))
                    {
                        text=text.concat(symbols[4][it2]);
                        break;
                    }
                }
                if(text.length()!=5)
                    return "error";
                break;
            }
            else if(Objects.equals(symbols[3][it], "1"))
            {
                text=text.concat("1");
                for(int it2=0;it2<jmax;it2++)
                {
                    if(symbols[4][it2].matches("[0-2]")) {
                        text=text.concat(symbols[4][it2]);
                        break;
                    }
                }
                if(text.length()!=5)
                    return "error";
                break;
            }
        }
        return text;
    }
    String getYears8(String text, String[][] symbols, int jmax)
    {
        for(int it=0;it<jmax;it++)
        {
            if(symbols[5][it].matches(".|/|-"))
            {
                text=text.concat(".20");
                for(int k=0;k<jmax;k++)
                {
                    for(int l=0;l<jmax;l++)
                    {
                        if(symbols[6][k]!=null && symbols[7][l]!=null)
                        {
                            String year = symbols[6][k];
                            year = year.concat((symbols[7][l]));
                            if(Integer.parseInt(year)>=17)
                            {
                                text=text.concat(year);
                                break;
                            }
                        }
                    }
                    if(text.length()==10)
                        return text;

                }

                break;
            }
        }
        return "error";
    }
    String getYears10(String text,String[][] symbols, int jmax)
    {
        for (int it = 0; it < jmax; it++) {
            if (symbols[5][it].matches(".|/|-")) {
                text = text.concat(".20");
                for (int k = 0; k < jmax; k++) {
                    for (int l = 0; l < jmax; l++) {
                        if (symbols[8][k] != null && symbols[9][l] != null) {
                            String year = symbols[8][k];
                            year = year.concat((symbols[9][l]));
                            if (Integer.parseInt(year) >= 17) {
                                text = text.concat(year);
                                break;
                            }
                        }
                    }
                    if (text.length() == 10)
                        return text;
                }
                break;
            }
        }
        return "error";
    }
    String getText8(String text, String[][] symbols, int jmax)
    {
        text = getDays(text, symbols, jmax);
        if (Objects.equals(text, "error"))
            return text;
        for (int it = 0; it < jmax; it++)
            if (symbols[2][it].matches(".|/|-")) {
                text = text.concat(".");
                break;
            }
        if (text.length() != 3)
            return "error";
        text = getMonths(text, symbols, jmax);
        if (Objects.equals(text, "error"))
            return text;
        text = getYears8(text, symbols, jmax);
        return text;
    }
    String getText10(String text,String[][] symbols, int jmax)
    {
        text = getDays(text, symbols, jmax);
        if (Objects.equals(text, "error"))
            return text;
        for (int it = 0; it < jmax; it++)
            if (symbols[2][it].matches(".|/|-")) {
                text = text.concat(".");
                break;
            }
        if (text.length() != 3)
            return "error";
        text = getMonths(text, symbols, jmax);
        if (Objects.equals(text, "error"))
            return text;
        text = getYears10(text, symbols, jmax);
        return text;
    }
    String getText(BufferedImage image)
    {
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
            if(i==7)
            {
                String text = "30.";
                for(int it = 0; it<jmax;it++)
                {
                    if(Objects.equals(symbols[0][it], "0"))
                    {
                        text.concat("0");
                        for(int it2=0;it2<jmax;it2++)
                        {
                            if(symbols[1][it2].matches("[1-9]"))
                            {
                                text.concat(symbols[1][it2]);
                                if(Objects.equals(symbols[1][it2], "2"))
                                    text = "28.02";
                                break;
                            }
                        }
                        if(text.length()!=5)
                            return "error";
                        break;
                    }
                    else if(Objects.equals(symbols[0][it], "1"))
                    {
                        text.concat("1");
                        for(int it2=0;it2<jmax;it2++)
                        {
                            if(symbols[1][it2].matches("[0-2]")) {
                                text.concat(symbols[1][it2]);
                                break;
                            }
                        }
                        if(text.length()!=5)
                            return "error";
                        break;
                    }
                }
                for(int it=0;it<jmax;it++)
                {
                    if(symbols[2][it].matches(".|/|-"))
                    {
                        text.concat(".20");
                        for(int k=0;k<jmax;k++)
                            for(int l=0;l<jmax;l++)
                            {
                                String year = symbols[5][k].concat(symbols[6][l]);
                                if(Integer.parseInt(year)>=17)
                                {
                                    text.concat(year);
                                    break;
                                }
                            }
                        break;
                    }
                }
                if(text.length()!=10)
                    return "error";
                return text;
            }
            else if(i==8)
                return getText8("",symbols,jmax);
            else if (i==10)
                return getText10("",symbols,jmax);
            else
            {
                String text = "";
                for(int it=0;it<i;it++)
                {
                    text = text.concat(symbols[it][0]);
                }
                List<String> allMatches = new ArrayList<String>();
                List<Integer> mIndex = new ArrayList<Integer>();
                Matcher m = Pattern.compile("\\d{2}(-|/|.)\\d{2}(-|/|.)\\d{4}").matcher(text);
                while (m.find()) {
                    allMatches.add(m.group());
                    mIndex.add(m.start());
                }
                for(int it=0; it<allMatches.size();it++)
                {
                    String[][] sym2 = new String[11][10];
                    for(int it2=mIndex.get(it);it2<mIndex.get(it)+10;it2++)
                    {
                        int k = it2-mIndex.get(it);
                        for(int it3 = 0; it3<jmax;it3++)
                            sym2[k][it3]=symbols[it2][it3];
                    }
                    String r = getText10(allMatches.get(it),sym2,jmax);
                    if(!r.equals("error"))
                        return r;
                }
                return "error";
            }
        }
        return null;
    }
}