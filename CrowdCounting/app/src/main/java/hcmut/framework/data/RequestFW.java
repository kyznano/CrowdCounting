package hcmut.framework.data;

/**
 * Created by minh on 05/08/2015.
 */
public class RequestFW {
    private int mCode;
    private Object mData;

    public RequestFW(int code, Object data) {
        this.mCode = code;
        this.mData = data;
    }

    public RequestFW(int code) {
        this.mCode = code;
    }

    public int getCode() {
        return mCode;
    }

    public Object getData() {
        return mData;
    }

    public void setData(Object newData) {
        this.mData = newData;
    }
}
