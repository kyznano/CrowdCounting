package hcmut.framework;

import hcmut.framework.data.RequestFW;
import hcmut.framework.data.ResponseFW;

/**
 * Created by minh on 05/08/2015.
 */
public class BasicFlow {
    public static final int UI = 0;
    public static final int CONTROLLER = 1;
    public static final int SERVER = 2;
    private FrameworkActivity frameworkActivity;
    private int mFrom;

    public BasicFlow(FrameworkActivity frameworkActivity, int from) {
        this.frameworkActivity = frameworkActivity;
        this.mFrom = from;
    }

    public void sendRequest(RequestFW request) {
        switch (this.mFrom) {
            case UI:
                this.frameworkActivity.getController().listenToRequest(request);
                break;

            case CONTROLLER:
                this.frameworkActivity.getServer().listenToRequest(request);
                break;

            case SERVER:
                break;

            default:
                break;
        }
    }

    public void sendResponse(ResponseFW response) {
        switch (this.mFrom) {
            case UI:
                break;

            case CONTROLLER:
                this.frameworkActivity.getUI().listenToResponse(response);
                break;

            case SERVER:
                this.frameworkActivity.getController().listenToResponse(response);
                break;

            default:
                break;
        }
    }

    // override these methods
    public void listenToRequest(RequestFW request) {}
    public void listenToResponse(ResponseFW response) {}
}
