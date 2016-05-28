package hcmut.data;

import hcmut.framework.lib.AppLibGeneral;

/**
 * Created by minh on 12/08/2015.
 */
public class TimeEff {
    private String mId; // yyyyMMddHHmmss
    private String mRamAvailable;   // MB
    private String mTimeConsume;    // miliseconds
    private String mRepeat = "1"; // times
    private String mIsInBackground = Boolean.toString(false);   // activity (for taking picture) is still kept in background ?!

    public TimeEff(String mId, String mRamAvailable, String mTimeConsume, String mRepeat, String isInBackground) {
        this.mId = mId;
        this.mRamAvailable = mRamAvailable;
        this.mTimeConsume = mTimeConsume;
        this.mRepeat = mRepeat;
        this.mIsInBackground = isInBackground;
    }

    public TimeEff(String mRamAvailable, String mTimeConsume, boolean isInBackground) {
        this.mId = AppLibGeneral.getTimeNow("yyyyMMddHHmmssSS");
        this.mRamAvailable = mRamAvailable;
        this.mTimeConsume = mTimeConsume;
        this.mIsInBackground = Boolean.toString(isInBackground);
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public int getRamAvailable() {
        return Integer.valueOf(mRamAvailable);
    }

    public String getmRamAvailableString() {
        return mRamAvailable;
    }

    public void setRamAvailable(String mRamAvailable) {
        this.mRamAvailable = mRamAvailable;
    }

    public int getTimeConsume() {
        return Integer.valueOf(mTimeConsume);
    }

    public String getmTimeConsumeString() {
        return mTimeConsume;
    }

    public void setTimeConsume(String mTimeConsume) {
        this.mTimeConsume = mTimeConsume;
    }

    public int getRepeat() {
        return Integer.valueOf(mRepeat);
    }

    public String getmRepeatString() {
        return mRepeat;
    }

    public void setRepeat(String mRepeat) {
        this.mRepeat = mRepeat;
    }

    public String getIsInBackground() {
        return mIsInBackground;
    }

    public boolean isInBackground() {
        return Boolean.valueOf(mIsInBackground);
    }

    public void setIsInBackground(String mIsInBackground) {
        this.mIsInBackground = mIsInBackground;
    }
}
