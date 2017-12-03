package tools;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;

public class BarcodeUtils {

    LinkedList<String> readbarcodes = new LinkedList<>();
    boolean clear = false;
    int noCodeCount=0;
    public static String readEAN13Code(BufferedImage image) {
        BinaryBitmap bitmap = null;
        Result result = null;
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        RGBLuminanceSource source = new RGBLuminanceSource(image.getWidth(), image.getHeight(), pixels);
        bitmap = new BinaryBitmap(new HybridBinarizer(source));


        if (bitmap == null)
            return null;

        MultiFormatReader reader = new MultiFormatReader();
        try {
            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            LinkedList<BarcodeFormat> formats = new LinkedList<>();
            formats.add(BarcodeFormat.EAN_13);
            hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);

            result = reader.decode(bitmap, hints);
            return result.getText();
        } catch (NotFoundException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return null;
    }
    public String handleBarcodeImage(BufferedImage img, IView view){
        String code = BarcodeUtils.readEAN13Code(img);
        if (code != null) {
            noCodeCount=0;
            clear = false;
            readbarcodes.addFirst(code);
            Map<String, Long> counts =readbarcodes.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
            String popular = Collections.max(counts.entrySet(),
                    new Comparator<Map.Entry<String,Long>>() {
                        @Override
                        public int compare(Map.Entry<String,Long> o1, Map.Entry<String, Long> o2) {
                            return o1.getValue().compareTo(o2.getValue());
                        }
                    }).getKey();
            System.out.println(popular);
            view.setCurrentGreen();
            if (readbarcodes.size() > 7) {
                readbarcodes.removeLast();
                view.setLastCode(view.getCurrent());
                view.setCurrentCode(popular);
            }


        } else if (clear == false) {
            view.setCurrentRed();
            noCodeCount++;
            if(noCodeCount>10){
                readbarcodes.clear();
                view.setCurrentCode("no code yet");
            }
//                clear=true;
//                for (int i = 0; i <20 ; i++) {
//                    System.out.println();
//                }
        }
        return code;
    }
}
