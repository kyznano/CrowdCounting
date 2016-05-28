package hcmut.UI;

import android.widget.Button;
import android.widget.Toast;

import hcmut.aclab.crowd.counting.R;
import hcmut.activity.CountingMain;
import hcmut.framework.BasicFlow;
import hcmut.framework.data.ResponseFW;

/**
 * Created by minh on 05/08/2015.
 */
public class FcamUI extends BasicFlow {
    /*private TextView tv;
    private ImageView iv;
    private Button btnTakePicture;*/
    private CountingMain fa;

    public FcamUI(CountingMain frameworkActivity) {
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

        Button preference = (Button) fa.findViewById(R.id.btn_preference);
        Button takepic = (Button) fa.findViewById(R.id.btn_takepic);
        Button back = (Button) fa.findViewById(R.id.btn_back);
        Button send = (Button) fa.findViewById(R.id.btn_send);

        Button btn[] = new Button[] {preference, takepic, back, send};
        for(int i=0; i<btn.length; i++) {
            Button b = btn[i];
            if(b != null) {
                b.setWidth((int)(getScreenWidth()*0.2));
                b.setHeight((int)(getScreenWidth()*0.2));
            }
        }

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
        fa.startPreview();
    }

}
