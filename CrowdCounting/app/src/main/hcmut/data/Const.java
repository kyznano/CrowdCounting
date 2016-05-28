package hcmut.data;

import java.io.File;

/**
 * Created by minh on 09/08/2015.
 */
public class Const {
    public static final String PICTURE_FOLDER = "Pictures" + File.separator + "Crowd-Counting";
    // Quality of the taken picture (FcamController > takePicture)
    public static final int PICTURE_QUALITY = 100;

    public static final int REQ_TAKE_PICTURE_NOW = 1;
    public static final int RESP_OK = 101;

    public static final int REQ_ESTIMATION = 77;
    public static final int RESP_ESTIMATION = 777;

    public static final int REQ_GALLERY = 99;
    public static final int ACTIVITY_RESULT_GALLERY_CODE = 98;
    public static final int RESP_GALLERY = 999;

    // App settings
    public static final String PREF_SETTINGS = "PREF_SETTINGS";
    public static final String SETTINGS_SILENT_MODE = "SETTINGS_SILENT_MODE";   // take picture in silent mode or not (true/false)
    public static final String SETTINGS_FLOATING_UI_X = "SETTINGS_FLOATING_UI_X";
    public static final String SETTINGS_FLOATING_UI_Y = "SETTINGS_FLOATING_UI_Y";
    public static final String SETTINGS_ACTIVITY_IS_STILL_IN_BACKGROUND = "SETTINGS_ACTIVITY_IS_STILL_IN_BACKGROUND";

    // server address: localhost (default)
    public static final String SETTINGS_SERVER_ADDRESS = "SETTINGS_SERVER_ADDRESS";
    public static final String SETTINGS_SERVER_ADDRESS_DEFAULT = "192.168.1.157";
    // port: 2000
    public static final String SETTINGS_SERVER_PORT = "SETTINGS_SERVER_PORT";
    public static final String SETTINGS_SERVER_PORT_DEFAULT = "2000";

    public static final int KERNEL_SIZE = 31;
    public static final int IMG_RESIZE_WIDTH = 320;
    public static final int IMG_RESIZE_HEIGHT = 180;
    public static final int ROUND_DIGITS = 5;
}
