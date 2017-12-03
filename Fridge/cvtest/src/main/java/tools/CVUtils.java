package tools;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


//

/**
 * Some important things:
 * Mat objects are not freed by GC, they need to be released by calling release() method
 *https://docs.opencv.org/java/3.1.0/  - javadoc
 *
 */
public class CVUtils {
    public static final boolean hasNativeLibrary;

    static {
        boolean temp=false;
        try {
            System.loadLibrary("opencv_java331");//Core.NATIVE_LIBRARY_NAME);
            temp = true;
        } catch (UnsatisfiedLinkError ex) {
            temp=false;
            System.out.println(ex.getMessage());
        } finally {
            hasNativeLibrary=temp;
        }
    }
    public CVUtils(){

    }

    /**
     * returns Mat object converted to Java BufferedImage, Mat remains unmodified
     * @param src Mat to convert
     * @return BufferedImage
     */
    public static BufferedImage MatToBufferedImage(Mat src){
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", src, matOfByte);
        byte[] byteArray = matOfByte.toArray();

        //Preparing the Buffered Image
        InputStream in = new ByteArrayInputStream(byteArray);
        BufferedImage bufImage = null;
        try {
            bufImage = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        matOfByte.release();
        return bufImage;
    }
    public static Image MatToFXImage(Mat mat){
        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".bmp", mat, byteMat);
        return new Image(new ByteArrayInputStream(byteMat.toArray()));
    }
    /**
     * returns image as mat, remember to release mat
     * @param filename path to file to read
     * @return
     */
    public static Mat getImageFromFile(String filename) {
         return Imgcodecs.imread(filename, Imgcodecs.IMREAD_COLOR);
    }
    public static BufferedImage getBufferedImageFromFile(String filename){
        File imageFile = new File(filename);
        try {
            BufferedImage image = ImageIO.read(imageFile );
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * function to rotate images withouot cropping
     * based on: https://www.pyimagesearch.com/2017/01/02/rotate-images-correctly-with-opencv-and-python/
     * @param src input image
     * @param dst output image
     * @param angle angle in radians
     */
    public static void rotate_bound(Mat src, Mat dst, double angle) {
        int h = src.height();
        int w = src.width();
        int cX = w / 2;
        int cY = h / 2;
        Mat rot = Imgproc.getRotationMatrix2D(new Point(cX, cY), -angle, 1.0);
        double cos = Math.abs(rot.get(0, 0)[0]);
        double sin = Math.abs(rot.get(0, 1)[0]);
        int nW = (int) ((h * sin) + (w * cos));
        int nH = (int) ((h * cos) + (w * sin));
        rot.put(0, 2, new double[]{rot.get(0, 2)[0] + (nW / 2) - cX});
        rot.put(1, 2, new double[]{rot.get(1, 2)[0] + (nH / 2) - cY});
        Imgproc.warpAffine(src, dst, rot, new Size(nW, nH));
    }

    static void preprocess(Mat src, Mat dst) {
        Mat temp=new Mat();
        Mat temp2=new Mat();
        Imgproc.cvtColor(src, temp2, Imgproc.COLOR_BGR2GRAY);
        CLAHE clahe = Imgproc.createCLAHE(2.0, new Size(16, 16));
        clahe.apply(temp2,temp);
        temp.copyTo(dst);
        temp.release();
        temp2.release();      //Imgproc.threshold(dst, frame, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        //frame = dst;
        //rotate_bound(frame, dst, 0.0);
        //frame.release();

    }
}
