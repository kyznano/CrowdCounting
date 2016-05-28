package hcmut.UI;

/**
 * Created by minh on 05/08/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;

import hcmut.activity.CountingMain;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private Context mContext;
    private CountingMain fcam;
    private SurfaceHolder mHolder;
    private Camera mCamera;

    private boolean isStarted = false;

    private int mPreviewWidth = 0;
    private int mPreviewHeight = 0;
    private static final int mPreviewFormat = ImageFormat.YV12;

    public CameraPreview(CountingMain fcam, Camera camera, boolean isVisible) {
        super(fcam);
        mContext = fcam;
        this.fcam = fcam;
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        if(isVisible) {
            int[] pwph = Cam.getBestPreviewSize(mContext, mCamera);
            if(pwph[0] > 0 && pwph[1]>0) {
                /*mPreviewWidth = pwph[0];
                mPreviewHeight = pwph[1];
                mHolder.setFixedSize(mPreviewWidth, mPreviewHeight);*/

                //Toast.makeText(mContext, "w=" + String.valueOf(pwph[0] + ";h=" + String.valueOf(pwph[1])), Toast.LENGTH_LONG).show();

                mPreviewWidth = fcam.getUI().getScreenWidth();
                mPreviewHeight = fcam.getUI().getScreenHeight();
                mHolder.setFixedSize(mPreviewWidth, mPreviewHeight);
            }
        }

        int rotatedAngle = Cam.setCameraDisplayOrientation(fcam, Camera.CameraInfo.CAMERA_FACING_BACK,mCamera);
        if(rotatedAngle==90 || rotatedAngle==270) {
            int tmp = mPreviewWidth;
            mPreviewWidth = mPreviewHeight;
            mPreviewHeight = tmp;
        }
        //setLayoutParams(new ViewGroup.LayoutParams(mPreviewWidth, mPreviewHeight));
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        requestLayout();

        // set taken picture quality (best quality)
        Camera.Size bestPictureSize = Cam.getBestPictureSize(mCamera);
        mCamera.getParameters().setPictureSize(bestPictureSize.width, bestPictureSize.height);

        /*
        // set preview format
        mPreviewFormat = Cam.getPreviewFormat(mCamera);
        if(mPreviewFormat==ImageFormat.RGB_565) {
            Toast.makeText(mContext, "CameraPreview: PreviewFormat = RGB_565", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "CameraPreview: PreviewFormat = YV12", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if(mPreviewWidth>=0 && mPreviewHeight>=0) {
            //mCamera.getParameters().setPreviewSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mCamera.getParameters().setPreviewFormat(CameraPreview.mPreviewFormat);
            //mCamera.setPreviewCallback(new PreviewFrame(fcam, mPreviewFormat, mCamera));
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                // comment the line below when testing with GenyMotion
                mCamera.startPreview();
                //startFaceDetection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mContext, "Error at: CameraPreview > surfaceCreated()", Toast.LENGTH_SHORT).show();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        if(mCamera!=null) {
            //mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        synchronized (mCamera) {
            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here
            if(mPreviewWidth>=0 && mPreviewHeight>=0) {
                //mCamera.getParameters().setPreviewSize(mPreviewWidth, mPreviewHeight);
                mCamera.getParameters().setPreviewFormat(mPreviewFormat);
                //mCamera.setPreviewCallback(new PreviewFrame(fcam, mPreviewFormat, mCamera));
                // start preview with new settings
                try {
                    mCamera.setPreviewDisplay(mHolder);
                    mCamera.startPreview();
                    //startFaceDetection();
                } catch (Exception e){
                    //Log.d("surfaceChanged", "Error starting camera preview: " + e.getMessage());
                }
            }
        }
        isStarted = true;
    }

    /*
    public void startFaceDetection(){
        // Try starting Face Detection
        Camera.Parameters params = mCamera.getParameters();

        // start face detection only *after* preview has started
        if (params.getMaxNumDetectedFaces() > 0){
            Toast.makeText(mContext, "FaceDetection is SUPPORTED", Toast.LENGTH_LONG).show();
            // camera supports face detection, so can start it:
            mCamera.startFaceDetection();
        } else {
            Toast.makeText(mContext, "FaceDetection is not supported!", Toast.LENGTH_LONG).show();
        }
    }*/

    public boolean isStarted() {
        return isStarted;
    }

}

class PreviewFrame implements Camera.PreviewCallback {
    private CountingMain fcam;
    private Context mContext;
    private int mPreviewFormat;
    private Bitmap mBitmap;
    private Camera.Size mPreviewSize;

    private long mCount = 0;

    public PreviewFrame(CountingMain fcam, int previewFormat, Camera camera) {
        this.fcam = fcam;
        this.mContext = fcam.getApplicationContext();
        this.mPreviewFormat = previewFormat;
        this.mPreviewSize = camera.getParameters().getPreviewSize();
        //fcam.getUI().getTv().setText("abc");
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if(mBitmap==null /*&& mPreviewFormat==ImageFormat.RGB_565*/) {
            Toast.makeText(mContext, "w=" + String.valueOf(mPreviewSize.width) + ";h=" + String.valueOf(mPreviewSize.height) + ";size=" + String.valueOf(data.length), Toast.LENGTH_LONG).show();
            mBitmap = Bitmap.createBitmap(mPreviewSize.width, mPreviewSize.height, Bitmap.Config.RGB_565);
        }
        switch (mPreviewFormat) {
            case ImageFormat.YV12:
                /*YuvImage image = new YuvImage(data, mPreviewFormat,
                        mPreviewSize.width, mPreviewSize.height, null);*/

                /*byte[] bbArray = data.clone();
                if(fcam.getUI().getTv().getText().toString().equals("abc")) {
                    String bb = "";
                    for(int i=0; i<640*240; i++) {
                        bb += String.valueOf(data[i]);
                        data[i] = 127;
                    }
                    //fcam.getUI().getTv().setText(bb);
                }*/

                /*fcam.getUI().getTv().setText(String.valueOf(mCount));
                mCount++;*/

                //camera.addCallbackBuffer(data);
                break;
            case ImageFormat.RGB_565:
                ByteBuffer buf = ByteBuffer.wrap(data); // data is my array
                mBitmap.copyPixelsFromBuffer(buf);
                for(int x=0; x<mBitmap.getWidth(); x++) {
                    for(int y=0; y<mBitmap.getHeight(); y++) {
                        int pixel = mBitmap.getPixel(x,y);
                        int red = Color.red(pixel);
                        int green = Color.green(pixel);
                        int blue = Color.blue(pixel);
                        mBitmap.setPixel(x, y, Color.rgb(0, 0, blue));
                    }
                }
                ByteBuffer buffer = ByteBuffer.allocate(mBitmap.getByteCount());
                mBitmap.copyPixelsToBuffer(buffer);
                byte[] bArray = buffer.array();
                //camera.addCallbackBuffer(bArray);
                break;
            default:
                //camera.addCallbackBuffer(data);
                break;
        }

    }
}