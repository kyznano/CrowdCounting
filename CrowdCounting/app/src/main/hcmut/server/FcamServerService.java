package hcmut.server;

import android.content.Context;
import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import hcmut.data.Const;
import hcmut.framework.data.RequestFW;
import hcmut.framework.data.ResponseFW;
import hcmut.framework.lib.AppLibGeneral;

/**
 * Created by minh on 23/05/2016.
 */

public class FcamServerService {
    public static final String server_estimation = "server_estimation";

    private static final int CONNECTION_TIMEOUT = 10000;    // 10s
    private static final int DATARETRIEVAL_TIMEOUT = 10000;

    public static final String jsonKeyResponse = "res";

    public static String generateURL(String...components) {
        String url = "";
        for (String s : components) {
            if(url.length()>0) {
                url += "/";
            }
            url += s;
        }
        return url;
    }

    public static String generateURL(Context context) {
        String server_address = AppLibGeneral.getConfigurationString(context, Const.PREF_SETTINGS, Const.SETTINGS_SERVER_ADDRESS, Const.SETTINGS_SERVER_ADDRESS_DEFAULT);
        String server_port = AppLibGeneral.getConfigurationString(context, Const.PREF_SETTINGS, Const.SETTINGS_SERVER_PORT, Const.SETTINGS_SERVER_PORT_DEFAULT);
        String server = server_address + ":" + server_port;
        return generateURL("http:/", server);

        //return "http://192.168.1.157:2000";
    }

    // https://ihofmann.wordpress.com/2013/01/23/android-sending-post-requests-with-parameters/
    public static String requestUrl(String url, String postParameters) throws Exception {
        String result = null;
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(url);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            // handle POST parameters
            if (postParameters != null) {
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setFixedLengthStreamingMode(
                        postParameters.getBytes().length);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // "HTTP/1.1 defines the "close" connection option for the sender to signal that the connection will be closed
                //	after completion of the response"; very important, if not including this line, the second request will be failed >"<
                urlConnection.setRequestProperty("Connection", "close");

				/*
				//send the POST out
				PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
				out.print(postParameters);
				out.close();*/

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postParameters);

                writer.flush();
                writer.close();
                os.close();

            }

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                // throw some exception
            }

            //InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStream is = urlConnection.getInputStream();

            // read output (only for GET)

            if (postParameters != null) {
                result = getResponseText(is);
            } else {
//				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                result = getResponseText(is);
            }

        } catch (MalformedURLException e) {
            // handle invalid URL
        } catch (SocketTimeoutException e) {
            // handle timeout
        } catch (IOException e) {
            // handle I/0
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
                urlConnection = null;
            }
        }

        return result;
    }

    // Modified code of: https://ihofmann.wordpress.com/2013/01/23/android-sending-post-requests-with-parameters/
    private static final char PARAMETER_DELIMITER = '&';
    private static final char PARAMETER_EQUALS_CHAR = '=';
    public static String generatePostParams(String key, String value) {
        String param = "";
        try {
            param += key + PARAMETER_EQUALS_CHAR + URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //TODO: UnsupportedEncodingException
        }
        return param;
    }

    // required in order to prevent issues in earlier Android version.

    @SuppressWarnings("deprecation")
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    public static String getResponseText(InputStream inStream) {
        String line;
        String response = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
        try {
            while ((line=br.readLine()) != null) {
                response+=line;
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return response;
    }

    public static ResponseFW parseServerResponse(RequestFW request, String serverResponse) {
        ResponseFW errorResponse = new ResponseFW(request, -1);
        ResponseFW finalResponse = null;
        if(AppLibGeneral.isEmptyString(serverResponse)) { return errorResponse; }

        try {
            String value = "No data";
            JSONObject json = new JSONObject(serverResponse);
            for(Iterator<String> iter = json.keys(); iter.hasNext(); ) {
                String key = iter.next();
                String val = json.getString(key);
                value = val;
                break;
            }

            finalResponse = new ResponseFW(request, 1, value);

        } catch (JSONException e) {
            finalResponse = errorResponse;
        }
        return finalResponse;
    }
}
