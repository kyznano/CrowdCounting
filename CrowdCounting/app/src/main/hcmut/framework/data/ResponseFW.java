package hcmut.framework.data;

/**
 * Created by minh on 05/08/2015.
 */
public class ResponseFW {
    private RequestFW mRequest;
    private int mCode;
    private Object data;

    public ResponseFW(RequestFW request, int code, Object data) {
        this.mRequest = request;
        this.mCode = code;
        this.data = data;
    }

    public ResponseFW(RequestFW request, int code) {
        this.mRequest = request;
        this.mCode = code;
    }

    public RequestFW getRequest() {
        return this.mRequest;
    }

    public int getRequestCode() {
        return this.mRequest.getCode();
    }

    public Object getRequestData() {
        return this.mRequest.getData();
    }

    public int getCode() {
        return mCode;
    }

    public Object getData() {
        return data;
    }
}
