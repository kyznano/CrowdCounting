package hcmut.activity;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import hcmut.aclab.crowd.counting.R;

import hcmut.UI.MixedView;
import hcmut.controller.FcamDatabase;
import hcmut.data.Const;
import hcmut.framework.lib.AppLibGeneral;
import hcmut.framework.lib.AppLibUI;

/**
 * Created by minh on 09/08/2015.
 */
public class FcamService extends Service {
    public static final int FLAG_START_NEW_FLOATING_UI  = 600;
    public static final int FLAG_STOP_FLOATING_UI = 601;
    public static final int FLAG_TAKE_PICTURE_DONE = 602;
    public static final int ID = 700;

    public static final String KEY_TIME_STARTED = "KEY_TIME_STARTED";
    public static final String KEY_RAM_AVAILABLE = "KEY_RAM_AVAILABLE";

    private static final int firstLayerIndex = 0;
    private static final int secondLayerIndex = 1;

    private Context context;
    private WindowManager windowManager;
    private MixedView mFloatingCamera;
    private static final String TAG_IV_TACK1 = "TAG_IV_TACK1";
    private static final String TAG_IV_TACK2 = "TAG_IV_TACK2";
    private static final String TAG_IV_TACK3 = "TAG_IV_TACK3";
    private static final String TAG_IV_DONE = "TAG_IV_DONE";
    private ImageView mFloatingTrash;
    private boolean isRunning = false;
    private boolean isTakingPicture = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        //final int floatingUISize = (int) (AppLibGeneral.getScreenWidth(context)/5.5);

        // update 1.2: The same floating UI size for portrait and landscape
        final int floatingUISize = (int) (AppLibGeneral.getScreenShort(context)/5.5);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        /*mFloatingCamera = new ImageView(this);
        mFloatingCamera.setImageResource(R.drawable.icon_circle_blue_120);*/

        // initialize UI of the floating camera
        ViewGroup.LayoutParams ivParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        ImageView ivCameraWithShadow = new ImageView(context);
        ivCameraWithShadow.setImageDrawable(AppLibUI.stickImage(
                getResources().getDrawable(R.drawable.ic_floating_camera_shadow_only),
                getResources().getDrawable(R.drawable.ic_floating_camera_transparent80)
        ));
        ivCameraWithShadow.setLayoutParams(ivParams);

        ImageView ivCameraOpaque = new ImageView(context);
        ivCameraOpaque.setImageResource(R.drawable.ic_floating_camera_opaque);
        ivCameraOpaque.setLayoutParams(ivParams);

        mFloatingCamera = new MixedView(context, floatingUISize, floatingUISize);
        mFloatingCamera.addViewToLayer(ivCameraWithShadow, firstLayerIndex);
        mFloatingCamera.addViewToLayer(ivCameraOpaque, secondLayerIndex);
        mFloatingCamera.showLayer(firstLayerIndex);
        // End -- initialize UI of the floating camera

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                /*WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,*/
                floatingUISize,
                floatingUISize,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;

        final int DEFAULT_FLOATING_UI_X = 0;
        final int DEFAULT_FLOATING_UI_Y = AppLibGeneral.getScreenHeight(context) / 8;

        final int mCamX = AppLibGeneral.getConfigurationInt(context,
                Const.PREF_SETTINGS, Const.SETTINGS_FLOATING_UI_X, DEFAULT_FLOATING_UI_X);
        final int mCamY = AppLibGeneral.getConfigurationInt(context,
                Const.PREF_SETTINGS, Const.SETTINGS_FLOATING_UI_Y, DEFAULT_FLOATING_UI_Y);
        params.x = mCamX;
        params.y = mCamY;

        final Intent fcamIntent = new Intent(context, CountingMain.class);
        fcamIntent.setAction(CountingMain.START_FROM_FLOATING_UI);
        fcamIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mFloatingCamera.setOnTouchListener(new View.OnTouchListener() {
            private int initialX = mCamX;
            private int initialY = mCamY;
            private float initialTouchX = 0;
            private float initialTouchY = 0;
            //private long firstTouchTime = -1;
            //private int minDistance = AppLibGeneral.getScreenWidth(FcamService.this.getApplicationContext()) / 20;

            private boolean isMove = false;
            private FcamDatabase database;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int dx = (int) Math.abs(event.getRawX() - initialTouchX);
                int dy = (int) Math.abs(event.getRawY() - initialTouchY);
                int d = (int) Math.sqrt(dx * dx + dy * dy);
                long thisTime = System.currentTimeMillis();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        //firstTouchTime = System.currentTimeMillis();
                        mFloatingCamera.showLayer(secondLayerIndex);

                        // access database
                        database = new FcamDatabase(context);
                        return true;
                    case MotionEvent.ACTION_UP:
                        /*if (d < minDistance && thisTime - firstTouchTime <= 1500) {
                            FcamService.this.startActivity(fcamIntent);
                        }*/
                        if (!isMove) {
                            if (!isTakingPicture) {
                                int ramAvailable = Math.round((float) AppLibGeneral.getAvailableMemory(context) / 5) * 5;
                                fcamIntent.putExtra(KEY_TIME_STARTED, System.currentTimeMillis());
                                fcamIntent.putExtra(KEY_RAM_AVAILABLE, ramAvailable);
                                if (database != null) {
                                    // estimate time consume
                                    boolean isInBackground = AppLibGeneral.getConfigurationBoolean(context,
                                            Const.PREF_SETTINGS, Const.SETTINGS_ACTIVITY_IS_STILL_IN_BACKGROUND, false);

                                    int time_consume = database.getTimeConsume(ramAvailable, isInBackground);
                                    if (time_consume > 0) {
                                    /*Toast.makeText(context,
                                            "FcamService > onCreate : Open in " + String.valueOf(time_consume) + " ms",
                                            Toast.LENGTH_LONG).show();*/
                                        startProgressBar(time_consume);
                                    }
                                }
                                mFloatingCamera.showLayer(secondLayerIndex);
                                FcamService.this.startActivity(fcamIntent);
                                isTakingPicture = true;
                            } else {
                                // do nothing
                            }
                        } else {
                            mFloatingCamera.showLayer(firstLayerIndex);
                        }

                        if (mFloatingTrash != null) {
                            windowManager.removeView(mFloatingTrash);
                            mFloatingTrash = null;
                        }

                        isMove = false;
                        // update app configurations
                        AppLibGeneral.setConfigurationInt(context, Const.PREF_SETTINGS, Const.SETTINGS_FLOATING_UI_X, params.x);
                        AppLibGeneral.setConfigurationInt(context, Const.PREF_SETTINGS, Const.SETTINGS_FLOATING_UI_Y, params.y);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if(d < floatingUISize/5) {
                            // "isMove" variable does not changed
                        } else {
                            isMove = true;
                            params.x = initialX - (int) (event.getRawX() - initialTouchX);
                            params.y = initialY - (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(mFloatingCamera, params);
                        }

                        /*if (d < minDistance && thisTime - firstTouchTime > 1500) {
                            FcamService.this.stopSelf();
                        }*/

                        if (mFloatingTrash == null) {
                            /*mFloatingTrash = createTrash();
                            WindowManager.LayoutParams trashParams = new WindowManager.LayoutParams(
                                    WindowManager.LayoutParams.WRAP_CONTENT,
                                    WindowManager.LayoutParams.WRAP_CONTENT,
                                    WindowManager.LayoutParams.TYPE_PHONE,
                                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                    PixelFormat.TRANSLUCENT);

                            trashParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                            trashParams.x = 0;
                            trashParams.y = 0;
                            windowManager.addView(mFloatingTrash, trashParams);

                            ClipData data = ClipData.newPlainText("", "");
                            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                            v.startDrag(data, shadowBuilder, v, 0);
                            v.setVisibility(View.INVISIBLE);*/
                        }
                        return true;
                }
                return false;
            }

            private void startProgressBar(final int miliseconds) {
                if (miliseconds <= 0) {
                    return;
                }

                final ImageView ivFirstTack = new ImageView(context);
                ivFirstTack.setTag(TAG_IV_TACK1);
                ivFirstTack.setImageResource(R.drawable.ic_floating_camera_tack_1);
                ivFirstTack.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mFloatingCamera.addViewToLayer(ivFirstTack, mFloatingCamera.getCurrentLayerIndex());

                final ImageView ivSecondTack = new ImageView(context);
                ivSecondTack.setTag(TAG_IV_TACK3);
                ivSecondTack.setImageResource(R.drawable.ic_floating_camera_tack_3);
                ivSecondTack.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                // update 1.2: The remaining time is least than 2 seconds, show "ivSecondTack"
                final int t = miliseconds - 2000;
                if (t <= 0) {
                    mFloatingCamera.addViewToLayer(ivSecondTack, mFloatingCamera.getCurrentLayerIndex());
                } else {    // least than 2 seconds before taking picture
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mFloatingCamera.addViewToLayer(ivSecondTack, mFloatingCamera.getCurrentLayerIndex());
                        }
                    }, t);
                }

                /*Handler delIv = new Handler();
                delIv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFloatingCamera.removeView(ivSecondTack);
                        mFloatingCamera.removeView(ivFirstTack);
                    }
                }, miliseconds);*/
            }

        });

        windowManager.addView(mFloatingCamera, params);
    }

    private void startForegroundWithNotification() {
        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this);
        notiBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notiBuilder.setContentTitle(context.getString(R.string.app_name));
        notiBuilder.setContentText(context.getString(R.string.noti_remove_floating_button));

        Intent intentCloseFloatingUI = new Intent(context, CountingMain.class);
        intentCloseFloatingUI.setAction(CountingMain.STOP_SERVICE_FLOATING_UI);
        PendingIntent closeFloatingUI = PendingIntent.getActivity(context, ID,
                intentCloseFloatingUI, PendingIntent.FLAG_UPDATE_CURRENT);
        notiBuilder.setContentIntent(closeFloatingUI);
        Notification noti = notiBuilder.build();
        startForeground(ID, noti);
    }

    private void animFloatingUI() {
        final ImageView ivDoneMark = new ImageView(context);
        ivDoneMark.setTag(TAG_IV_DONE);
        ivDoneMark.setImageResource(R.drawable.ic_foreground_done);
        ivDoneMark.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        final ImageView finalTack = new ImageView(context);
        finalTack.setTag(TAG_IV_TACK2);
        finalTack.setImageResource(R.drawable.ic_floating_camera_tack_2);
        finalTack.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        synchronized (mFloatingCamera) {
            mFloatingCamera.addViewsToLayer(mFloatingCamera.getCurrentLayerIndex(), ivDoneMark, finalTack);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFloatingCamera.removeViewByTag(TAG_IV_TACK1, TAG_IV_TACK2, TAG_IV_TACK3, TAG_IV_DONE);
                mFloatingCamera.showLayer(firstLayerIndex);
            }
        }, 1500);
        /*Animation animDisappear = AnimationUtils.loadAnimation(context, R.anim.disappear);
        animDisappear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mFloatingCamera.removeView(ivDoneMark);
                mFloatingCamera.showLayer(firstLayerIndex);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivDoneMark.startAnimation(animDisappear);*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int value = START_STICKY;
        if(intent!=null) {
            switch (intent.getFlags()) {
                case FLAG_START_NEW_FLOATING_UI:
                    if(!isRunning) {
                        startForegroundWithNotification();
                        isRunning = true;
                    } else {
                        isRunning = false;
                        stopSelf();
                        value = START_NOT_STICKY;
                    }
                    break;
                case FLAG_STOP_FLOATING_UI:
                    if(isRunning) {
                        isRunning = false;
                        stopSelf();
                    }
                    value = START_NOT_STICKY;
                    break;
                case FLAG_TAKE_PICTURE_DONE:
                    if(isRunning) {
                        animFloatingUI();
                        isTakingPicture = false;
                    }
                    break;
                default:
                    if(!isRunning) {
                        startForegroundWithNotification();
                        isRunning = true;
                    }
                    break;
            }
        }
        //Toast.makeText(this, String.valueOf(flags), Toast.LENGTH_LONG).show();
        return value;
    }

    /*
    private ImageView createTrash() {
        final ImageView trash = new ImageView(this);
        trash.setImageResource(R.drawable.icon_circle_blue_120);
        trash.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        trash.setImageResource(R.drawable.icon_clock_blue_120);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        trash.setImageResource(R.drawable.icon_circle_blue_120);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        return trash;
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingCamera != null) {
            windowManager.removeView(mFloatingCamera);
        }
        stopForeground(true);
    }

}
