package tools;

import org.opencv.core.*;
import tools.BarcodeUtils;
import tools.CVUtils;
import tools.Camera;

import java.awt.image.BufferedImage;

public class MainTest {
    public static void main(String[] args) throws InterruptedException {

        Camera cam=new Camera(0);
        Mat frame=new Mat();
        SwingWindow swingWindow=new SwingWindow();
        cam.captureFrame(frame);
        swingWindow.refreshFrame(CVUtils.MatToBufferedImage(frame));
        //frame.release();
        //cam.releaseCam();
        //System.exit(0);
        BarcodeUtils barcodeUtils=new BarcodeUtils();

        Mat dst=new Mat();
        while (true) {

            cam.captureFrame(frame);
            CVUtils.preprocess(frame, dst);
            BufferedImage img = CVUtils.MatToBufferedImage(dst);
            swingWindow.refreshFrame(img);
            Thread.sleep(100);// do not kill the CPU
            barcodeUtils.handleBarcodeImage(img,swingWindow);
        }
        //dst.release();
    }
}
