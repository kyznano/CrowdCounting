package hcmut.UI;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import hcmut.aclab.crowd.counting.R;
import hcmut.activity.CountingMain;

/**
 * Created by minh on 19/05/2016.
 */
public class CustomDialog {
    public Bitmap BITMAP_IMAGE;
    private Dialog dialog;

    CountingMain cm;
    public CustomDialog(CountingMain c, Bitmap b){
        this.cm = c;
        this.BITMAP_IMAGE = b;
    }

    public Dialog DialogProcess() {
                dialog = new Dialog(cm);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                if(BITMAP_IMAGE!=null)
                {
                    dialog.setContentView(R.layout.imageshow);
                    ImageView image_frame = (ImageView) dialog.findViewById(R.id.image_show);
                    Drawable d = new BitmapDrawable(cm.getResources(),BITMAP_IMAGE);
                    image_frame.setImageDrawable(d);
                    dialog.getWindow().setBackgroundDrawable(null);
                    image_frame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button back = (Button) dialog.findViewById(R.id.btn_back);
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            close();
                        }
                    });
                    back.setWidth((int)(cm.getUI().getScreenWidth()*0.2));
                    back.setHeight((int)(cm.getUI().getScreenWidth()*0.2));

                    Button send = (Button) dialog.findViewById(R.id.btn_send);
                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cm.sendFeature(v);
                        }
                    });
                    send.setWidth((int)(cm.getUI().getScreenWidth()*0.2));
                    send.setHeight((int)(cm.getUI().getScreenWidth()*0.2));

                } else {
                    dialog.setContentView(R.layout.settings);
                    RelativeLayout rel = (RelativeLayout) dialog.findViewById(R.id.rel_settings);
                    rel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            close();
                        }
                    });

                }



        return dialog;
    }

    public void close() {
        cm.mCamera.startPreview();
        dialog.dismiss();
    }
}