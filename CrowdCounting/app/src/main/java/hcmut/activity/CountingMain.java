package hcmut.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;

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
import hcmut.framework.lib.AppLibFile;
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
    public Camera mCamera;
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

    public void settingDialog(View v) {
        CustomDialog customDialog = new CustomDialog(this, null);
        customDialog.DialogProcess().show();
        //fcam.current_dialog = customDialog;
    }

    public void startPreview() {
        Toast.makeText(this, "starting preview", Toast.LENGTH_SHORT).show();
        if(mCamera!=null) {
            mCamera.startPreview();
        } else {
            mCamera = Cam.getCameraInstance();
            if(mCamera !=null) {
                //mCamera.unlock();
                final CameraPreview mPreview = new CameraPreview(this, mCamera, true);
                FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
                preview.removeAllViews();
                preview.addView(mPreview);
                preview.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "mCamera is null after Cam.getCameraInstance(): CountingMain.java > startPreview()", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void releaseCamera(){
        Toast.makeText(this, "releasing preview", Toast.LENGTH_SHORT).show();
        if(mCamera!=null) {
            //mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent iw = getIntent();
        Bundle extraData = iw.getExtras();
        // take picture immediately and exit the app
        //if(true) {
        //if(iw.getAction().equals(START_FROM_FLOATING_UI)) {
            mStartFrom = START_FROM_FLOATING_UI;
            // Create an instance of Camera
            startPreview();

            Button preference = (Button) findViewById(R.id.btn_preference);
            preference.bringToFront();

            Button takepic = (Button) findViewById(R.id.btn_takepic);
            takepic.bringToFront();

            /*mCamera = Cam.getCameraInstance();
            if(mCamera !=null) {
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
                    }
                });
                t.start();

                // set application settings
                AppLibGeneral.setConfigurationBoolean(getApplicationContext(),
                        Const.PREF_SETTINGS, Const.SETTINGS_ACTIVITY_IS_STILL_IN_BACKGROUND, true);
            }*/
        /*} else if(iw.getAction().equals(STOP_SERVICE_FLOATING_UI)) {
            mStartFrom = STOP_SERVICE_FLOATING_UI;
            //Toast.makeText(this, "STOP_SERVICE_FLOATING_UI", Toast.LENGTH_LONG).show();
            stopFloatingUI();
        } else {
            mStartFrom = START_FROM_USER;
            // startService(new Intent(getApplicationContext(), FcamService.class)
            //        .setFlags(FcamService.FLAG_START_NEW_FLOATING_UI));
        }*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case Const.ACTIVITY_RESULT_GALLERY_CODE:
                    if(data!=null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        File pf = new File(picturePath);
                        if(!pf.canRead()) {
                            Toast.makeText(this, "Failed to read image: CountingMain.java > onActivityResult()", Toast.LENGTH_LONG).show();
                            break;
                        }

                        if(AppLibGeneral.isImageFile(picturePath)) {
                            int largerDimension = this.getUI().getScreenHeight()>this.getUI().getScreenWidth()?this.getUI().getScreenHeight():this.getUI().getScreenWidth();
                            Bitmap cb = AppLibFile.getBitmapFromPath(picturePath, largerDimension, largerDimension);
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(cb, cb.getWidth(), cb.getHeight(),true);
                            CustomDialog customDialog = new CustomDialog(this, scaledBitmap);
                            customDialog.DialogProcess().show();
                            this.current_dialog = customDialog;
                        }
                        else {
                            Toast.makeText(this, "Not image file: CountingMain.java > onActivityResult()", Toast.LENGTH_LONG).show();
                        }

                        startPreview();
                    }
                    break;
                default:
                    break;
            }

        } else if(resultCode == RESULT_CANCELED) {

        } else {

        }
    }

    private void stopFloatingUI() {
        // startService(new Intent(getApplicationContext(), FcamService.class)
        //        .setFlags(FcamService.FLAG_STOP_FLOATING_UI));
    }

    // press button
    public void loadImageFromGallery(View v) {
        mUI.sendRequest(new RequestFW(Const.REQ_GALLERY));
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
        /*startPreview();
        if(mStartFrom.equals(START_FROM_FLOATING_UI)) {
            // do nothing
        } else if(mStartFrom.equals(START_FROM_USER)) {
            finishApp();
        } else if(mStartFrom.equals(STOP_SERVICE_FLOATING_UI)) {
            finishApp();
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        //releaseCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //releaseCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
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