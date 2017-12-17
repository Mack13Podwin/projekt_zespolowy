package tools;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class Camera {
    private final boolean isCorrectlyOpened;
    private final VideoCapture capture;

    /**
     * Constructs camera representation
     * @param cameraNumber if there is one camera in the system it should be 0 other cameras will be 1 and so on
     */
    public Camera(int cameraNumber){
        if(CVUtils.hasNativeLibrary==false){
            isCorrectlyOpened=false;
            System.out.println("OpenCV not working properly cannot open camera");
            capture=null;
            return;
        }
        capture = new VideoCapture(cameraNumber);
        if (!capture.isOpened()) {
            capture.release();
            System.out.println("tools.Camera " + cameraNumber + "  Error");
            isCorrectlyOpened = false;
            //createErrorImage();
        } else {
            System.out.println("tools.Camera " + cameraNumber + " probably OK");
            isCorrectlyOpened = true;
        }

    }

    public boolean isCorrectlyOpened() {
        return isCorrectlyOpened;
    }

    /**
     * put frame in dst, dst will of course be modified
     * @param dst
     * @return
     */
    public synchronized boolean captureFrame(Mat dst){
        if(isCorrectlyOpened()){
            capture.read(dst);
            return true;
        } else {
            return false;
        }
    }
    public void releaseCam(){
        capture.release();
    }
}
