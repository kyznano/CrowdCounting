package hcmut.utils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

/**
 * Created by minh on 23/05/2016.
 */
public class AppLibGeneral {
    private static final String Alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String Num = "0123456789";
    private static final String AlphaNum = Num + Alphabet;

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     *
     * Original Source: http://stackoverflow.com/questions/363681/generating-random-integers-in-a-range-with-java
     *
     */
    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static float randFloat(float min, float max) {
        Random rand = new Random();
        float randomNum = rand.nextFloat()*(max-min) + min;
        return randomNum;
    }

    public static boolean isInArray(int[] theArray, int value) {
        boolean result = false;
        for(int val : theArray) {
            if(value==val) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static String randAlphaNumString(int length) {
        return randString(length, AlphaNum);
    }

    public static String randAlphabetString(int length) {
        return randString(length, Alphabet);
    }

    private static String randString(int length, String set) {
        StringBuilder sb = new StringBuilder(length);
        for( int i = 0; i < length; i++ )
            sb.append( set.charAt(randInt(0, set.length()-1)) );
        return sb.toString();
    }

    public static boolean isInArray(String[] theArray, String value) {
        boolean result = false;
        for(String val : theArray) {
            if(value.equals(val)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static void setConfigurationInt(Context context, String PREF_NAME, String PREF_KEY, int newValue) {
        SharedPreferences prefConfig = context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = prefConfig.edit();
        editor.putInt(PREF_KEY, newValue);
        editor.commit();
    }

    public static int getConfigurationInt(Context context, String PREF_NAME, String PREF_KEY) {
        SharedPreferences prefConfig = context.getSharedPreferences(PREF_NAME, 0);
        return prefConfig.getInt(PREF_KEY, 0);
    }

    public static void setConfigurationString(Context context, String PREF_NAME, String PREF_KEY, String newValue) {
        SharedPreferences prefConfig = context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = prefConfig.edit();
        editor.putString(PREF_KEY, newValue);
        editor.commit();
    }

    public static String getConfigurationString(Context context, String PREF_NAME, String PREF_KEY) {
        SharedPreferences prefConfig = context.getSharedPreferences(PREF_NAME, 0);
        return prefConfig.getString(PREF_KEY, "");
    }

    public static void setConfigurationBoolean(Context context, String PREF_NAME, String PREF_KEY, boolean newValue) {
        SharedPreferences prefConfig = context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = prefConfig.edit();
        editor.putBoolean(PREF_KEY, newValue);
        editor.commit();
    }

    public static boolean getConfigurationBoolean(Context context, String PREF_NAME, String PREF_KEY, boolean defaultValue) {
        SharedPreferences prefConfig = context.getSharedPreferences(PREF_NAME, 0);
        return prefConfig.getBoolean(PREF_KEY, defaultValue);
    }

    public static boolean isImageFile(String path) {
        boolean result = false;
        if(path.endsWith(".jpg") || path.endsWith(".jpeg")
                || path.endsWith(".png") || path.endsWith(".bmp")
                || path.endsWith(".gif"))
            result = true;
        return result;
    }

    public static boolean isVideoFile(String path) {
        boolean result = false;
        if(path.endsWith(".3gp") || path.endsWith(".mp4")) {
            Bitmap bm = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
            if(bm!=null) {
                result = true;
            }
        }
        return result;
    }

    public static boolean isNotEmptyString(String...strings) {
        for(String s : strings) {
            if(s==null || s.length()==0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmptyString(String s) {
        return !isNotEmptyString(s);
    }

    public static void rateApp(Context context, String appPackageName) {
        try {
            Uri uri = Uri.parse("market://details?id=" + appPackageName);
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Activity cannot be found", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("DefaultLocale")
    public static boolean findAnAppByPackageName(Context context, String appPackageName) {
        boolean appFound = false;
        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(new Intent(), 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(appPackageName)) {
                appFound = true;
                break;
            }
        }
        return appFound;
    }

    public static void hideKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    // check network connection
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public static boolean isNotNull(Object...objects) {
        for(Object o : objects) {
            if(o==null) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return current time in format: yyyyMMddHHmmss
     */
    /*public static String getTimeNow() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        //get current date time with Date()
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getTimeNow(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        //get current date time with Date()
        Date date = new Date();
        return dateFormat.format(date);
    }*/

    // source: http://stackoverflow.com/questions/642897/removing-an-element-from-an-array-java
    public static void removeElement(Object[] array, int delIndex) {
        System.arraycopy(array, delIndex+1, array, delIndex, array.length-1-delIndex);
    }

    public static Drawable convertBitmap2Drawable(Context context, Bitmap b) {
        return new BitmapDrawable(context.getResources(), b);
    }
}
