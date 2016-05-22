package hcmut.UI;

import android.widget.Toast;

import hcmut.framework.BasicFlow;
import hcmut.framework.FrameworkActivity;
import hcmut.framework.data.ResponseFW;

/**
 * Created by minh on 05/08/2015.
 */
public class FcamUI extends BasicFlow {
    /*private TextView tv;
    private ImageView iv;
    private Button btnTakePicture;*/
    private FrameworkActivity fa;

    public FcamUI(FrameworkActivity frameworkActivity) {
        super(frameworkActivity, BasicFlow.UI);
        fa = frameworkActivity;
        initUI();
    }

    private void initUI() {
        /*tv = (TextView) fa.findViewById(R.id.tv);
        iv = (ImageView) fa.findViewById(R.id.iv);
        btnTakePicture = (Button) fa.findViewById(R.id.btn_takepicture);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest(new RequestFW(Const.REQ_TAKE_PICTURE_NOW));
                //fa.startService(new Intent(fa.getApplicationContext(), FcamService.class));
                Toast.makeText(fa, "FcamUI > button clicked", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public int getScreenWidth() {
        return fa.getResources().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return fa.getResources().getDisplayMetrics().heightPixels;
    }

    /*public TextView getTv() {
        return tv;
    }
    public ImageView getIv() {
        return iv;
    }*/


    @Override
    public void listenToResponse(ResponseFW response) {
        Toast.makeText(fa, (String)response.getData(), Toast.LENGTH_LONG).show();
    }

}
