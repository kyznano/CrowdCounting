package hcmut.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.util.Log;
import android.view.Surface;

import java.util.List;

import hcmut.framework.lib.AppLibGeneral;

/**
 * Created by minh on 05/08/2015.
 */
public class Cam {

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.e("Camera", "Cam > getCameraInstance() | Cause = " + e.getCause() + " | Message = " + e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }

    public static int[] getBestPreviewSize(Context context, Camera camera) {
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        int pw = -1;
        int ph = -1;

        if(camera!=null) {
            List<Camera.Size> supportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();
            for(Camera.Size s : supportedPreviewSizes) {
                if(s.width <= screenWidth && s.height <= screenHeight) {
                    pw = s.width;
                    ph = s.height;
                    break;
                }
            }
        }

        return new int[] {pw, ph};
    }

    public static Camera.Size getBestPictureSize(Camera camera) {
        Camera.Size bestSize = null;
        List<Camera.Size> sizeList = camera.getParameters().getSupportedPreviewSizes();
        bestSize = sizeList.get(0);
        for(int i = 1; i < sizeList.size(); i++){
            if((sizeList.get(i).width * sizeList.get(i).height) > (bestSize.width * bestSize.height)){
                bestSize = sizeList.get(i);
            }
        }
        return bestSize;
    }

    public static final int[] getSmallestPreviewSize(Context context, Camera camera) {
        int screenWidth = AppLibGeneral.getScreenWidth(context);
        int screenHeight = AppLibGeneral.getScreenHeight(context);
        int pw = screenWidth;
        int ph = screenHeight;

        if(camera!=null) {
            List<Camera.Size> supportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();
            for(Camera.Size s : supportedPreviewSizes) {
                if(s.width <= pw && s.height <= ph) {
                    pw = s.width;
                    ph = s.height;
                    break;
                }
            }
        }

        return new int[] {pw, ph};
    }

    public static int getPreviewFormat(Camera camera) {
        int format = ImageFormat.YV12;
        List<Integer> previewFormats = camera.getParameters().getSupportedPreviewFormats();
        if(previewFormats.contains(ImageFormat.RGB_565)) {
            format = ImageFormat.RGB_565;
        }
        return format;
    }

    private static final int[] AUDIO_STREAMS = new int[]{
            AudioManager.STREAM_ALARM,
            AudioManager.STREAM_DTMF,
            AudioManager.STREAM_MUSIC,
            AudioManager.STREAM_NOTIFICATION,
            AudioManager.STREAM_RING,
            AudioManager.STREAM_SYSTEM,
            AudioManager.STREAM_VOICE_CALL};

    @SuppressLint("NewApi")
    public static void disableShutterCompletely(AudioManager manager, Camera cam) {

        for(int i=0; i<AUDIO_STREAMS.length; i++) {
            manager.setStreamMute(AUDIO_STREAMS[i], true);
        }

        Camera.CameraInfo info = new Camera.CameraInfo();
        int numberOfCameras = Camera.getNumberOfCameras();
        for(int i=0; i<numberOfCameras; i++) {
            Camera.getCameraInfo(i, info);
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                if(info.canDisableShutterSound) {
                    cam.enableShutterSound(false);
                }
            }
        }
    }

    public static void enableSound(AudioManager manager) {
        for(int stream : AUDIO_STREAMS) {
            manager.setStreamMute(stream, false);
        }
    }

    public static int setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result = 0;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
        return result;
    }

    public static int getCameraRotatingAngle(Activity activity,
                                                  int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result = 0;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }
}
