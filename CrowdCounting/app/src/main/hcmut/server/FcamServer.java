package hcmut.server;

import android.os.AsyncTask;

import hcmut.activity.CountingMain;
import hcmut.data.Const;
import hcmut.framework.BasicFlow;
import hcmut.framework.data.RequestFW;
import hcmut.framework.data.ResponseFW;
import hcmut.framework.lib.AppLibGeneral;

/**
 * Created by minh on 23/05/2016.
 */
public class FcamServer extends BasicFlow {
    private CountingMain fcam;

    public FcamServer(CountingMain fcam) {
        super(fcam, BasicFlow.SERVER);
        this.fcam = fcam;
    }

    @Override
    public void listenToRequest(RequestFW request) {
        // TODO:
        new FakeServer(this.fcam, request);
    }

    public class FakeServer {

        private CountingMain fcam;
        private RequestFW request;

        public FakeServer(final CountingMain friendnet, final RequestFW request){
            this.fcam = friendnet;
            this.request = request;
            new waitFewSeconds().execute();
        }

        private class waitFewSeconds extends AsyncTask<String, Void, String> {

            private String serverResponse;

            @Override
            protected String doInBackground(String... params) {
                //SystemClock.sleep(AppLibGeneral.randInt(2000, 4000));

                serverResponse = "";

                try {
                    // sending request to url
                    String url = FcamServerService.generateURL(fcam);

                    //Toast.makeText(fcam, "url = " + url, Toast.LENGTH_LONG).show();

                    String postParam = FcamServerService.generatePostParams("feature", (String) request.getData());

                    //Toast.makeText(fcam, "post = " + postParam, Toast.LENGTH_LONG).show();

                    serverResponse = FcamServerService.requestUrl(url, postParam);
                } catch (Exception e) {
                    // TODO Error occurs while sending request to web service
                    // do nothing
                    serverResponse = "Demo server response: 777 people";
                }
                if(AppLibGeneral.isEmptyString(serverResponse)) {
                    serverResponse = "Demo server response: 777 people";
                }
                return "";
            }

            @Override
            protected void onPostExecute(String resultOfDoInBackground) {
                fcam.getServer().sendResponse(new ResponseFW(request, Const.RESP_ESTIMATION, serverResponse));
            }

        }

    }
}
