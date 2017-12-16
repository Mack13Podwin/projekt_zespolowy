package tools;

import org.opencv.core.Size;

import java.util.concurrent.ConcurrentHashMap;

public class CameraFactory {

    private final static Object creationLock = new Object();

    private final static ConcurrentHashMap<Integer, Camera> Cameras = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<Integer, Size> CameraFrameSize = new ConcurrentHashMap<Integer, Size>();

    public static Camera getInstance() {
        return getInstance(0);
    }



    public static Camera getInstance(int cameraNumber) {
        if (Cameras.get(cameraNumber) != null) {
            return Cameras.get(cameraNumber);
        } else {
            synchronized (creationLock) {
                if (Cameras.get(cameraNumber) == null) {
                    if (CameraFrameSize.get(cameraNumber) != null) {
                        Cameras.put(cameraNumber, new Camera(cameraNumber));
                    } else {
                        Cameras.put(cameraNumber, new Camera(cameraNumber));
                    }

                }
            }
        }
        return Cameras.get(cameraNumber);
    }
    public static void release(){
        for(Camera cam:Cameras.values()){
            cam.releaseCam();
        }
    }
}

