package hcmut.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.FrameLayout;

import hcmut.UI.Cam;
import hcmut.UI.CameraPreview;
import hcmut.UI.CustomDialog;
import hcmut.UI.FcamUI;
import hcmut.aclab.crowd.counting.R;
import hcmut.controller.FcamController;
import hcmut.controller.FcamDatabase;
import hcmut.data.Const;
import hcmut.framework.FrameworkActivity;
import hcmut.framework.data.RequestFW;
import hcmut.framework.lib.AppLibGeneral;
import hcmut.server.FcamServer;

/**
 * Created by minh on 05/08/2015.
 */
public class CountingMain extends FrameworkActivity {
    // take picture immediately and exit the app
    public static String START_FROM_FLOATING_UI = "START_FROM_FLOATING_UI";
    public static String START_FROM_USER = "START_FROM_USER";
    public static String STOP_SERVICE_FLOATING_UI = "STOP_SERVICE_FLOATING_UI";
    private Camera mCamera;
    private String mStartFrom = START_FROM_USER;

    public Bitmap current_bitmap = null;
    public String current_feature = "";
    public CustomDialog current_dialog = null;

    @Override
    protected void initFramework() {
        //this.mDatabase = new DatabaseAdapter(this.getApplicationContext());
        this.mDatabase = new FcamDatabase(this.getApplicationContext());
        this.mUI = new FcamUI(this);
        this.mController = new FcamController(this);
        this.mServer = new FcamServer(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.screen_app);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent iw = getIntent();
        Bundle extraData = iw.getExtras();
        // take picture immediately and exit the app
        if(true) {
        //if(iw.getAction().equals(START_FROM_FLOATING_UI)) {
            mStartFrom = START_FROM_FLOATING_UI;
            // Create an instance of Camera
            mCamera = Cam.getCameraInstance();
            if(mCamera !=null) {
                /*
                final long time_start = extraData.getLong(FcamService.KEY_TIME_STARTED, System.currentTimeMillis());
                int defaultRamAvailable = Math.round((float) AppLibGeneral.getAvailableMemory(getApplicationContext()) / 5) * 5;
                final int ramAvailable = extraData.getInt(FcamService.KEY_RAM_AVAILABLE, defaultRamAvailable);
                final boolean isInBackground = AppLibGeneral.getConfigurationBoolean(getApplicationContext(),
                        Const.PREF_SETTINGS, Const.SETTINGS_ACTIVITY_IS_STILL_IN_BACKGROUND, false);
                */

                /*mCamera.setFaceDetectionListener(new Camera.FaceDetectionListener() {
                    @Override
                    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
                        Log.d("FaceDetection", "face detected: " + faces.length +
                                " Face 1 Location X: " + faces[0].rect.centerX() +
                                "Y: " + faces[0].rect.centerY());
                    }
                });*/

                //Toast.makeText(getApplicationContext(), "START_FROM_FLOATING_UI", Toast.LENGTH_LONG).show();

                // Create our Preview view and set it as the content of our activity.
                final CameraPreview mPreview = new CameraPreview(this, mCamera, true);
                FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
                preview.addView(mPreview);
                preview.setVisibility(View.VISIBLE);

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(!mPreview.isStarted()) {
                            SystemClock.sleep(50);
                        }
                        //long time_end = System.currentTimeMillis();
                        //long deltaTime = time_end - time_start;
                        //getDatabase().insertTimeEff(new TimeEff(String.valueOf(ramAvailable), String.valueOf(deltaTime), isInBackground));
                        //takePic();
                    }
                });
                t.start();

                // set application settings
                AppLibGeneral.setConfigurationBoolean(getApplicationContext(),
                        Const.PREF_SETTINGS, Const.SETTINGS_ACTIVITY_IS_STILL_IN_BACKGROUND, true);
            }
        } else if(iw.getAction().equals(STOP_SERVICE_FLOATING_UI)) {
            mStartFrom = STOP_SERVICE_FLOATING_UI;
            //Toast.makeText(this, "STOP_SERVICE_FLOATING_UI", Toast.LENGTH_LONG).show();
            stopFloatingUI();
        } else {
            mStartFrom = START_FROM_USER;
            // startService(new Intent(getApplicationContext(), FcamService.class)
            //        .setFlags(FcamService.FLAG_START_NEW_FLOATING_UI));
        }
    }

    private void stopFloatingUI() {
        // startService(new Intent(getApplicationContext(), FcamService.class)
        //        .setFlags(FcamService.FLAG_STOP_FLOATING_UI));
    }

    public void sendFeature(View v) {
        // send feature to server
        mUI.sendRequest(new RequestFW(Const.REQ_ESTIMATION, current_feature));
    }

    public void turnOffDialog(View v) {
        // turn off dialog
        if(current_dialog != null) {
            current_dialog.close();
        }
    }

    public void takePic() {
        // Take picture immediately
        mUI.sendRequest(new RequestFW(Const.REQ_TAKE_PICTURE_NOW));
    }

    public void takePic(View v) {
        // Take picture immediately
        mUI.sendRequest(new RequestFW(Const.REQ_TAKE_PICTURE_NOW));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mStartFrom.equals(START_FROM_FLOATING_UI)) {
            // do nothing
        } else if(mStartFrom.equals(START_FROM_USER)) {
            finishApp();
        } else if(mStartFrom.equals(STOP_SERVICE_FLOATING_UI)) {
            finishApp();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mCamera!=null) {
            mCamera.release();
            mCamera = null;
            //Toast.makeText(this, "onPause(): camera is released!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCamera!=null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if(level == TRIM_MEMORY_RUNNING_CRITICAL) {
            AppLibGeneral.setConfigurationBoolean(getApplicationContext(),
                    Const.PREF_SETTINGS, Const.SETTINGS_ACTIVITY_IS_STILL_IN_BACKGROUND, false);
        }
    }

    @Override
    public void finishApp() {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        finishApp();
    }

    @Override
    public FcamUI getUI() {
        return (FcamUI) mUI;
    }

    @Override
    public FcamController getController() {
        return (FcamController) mController;
    }

    @Override
    public FcamDatabase getDatabase() {
        return (FcamDatabase) mDatabase;
    }

    public Camera getCamera() {
        return mCamera;
    }
}