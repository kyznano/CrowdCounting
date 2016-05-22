package hcmut.UI;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

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
                dialog.setContentView(R.layout.imageshow);
                ImageView image_frame = (ImageView) dialog.findViewById(R.id.image_show);
                if(BITMAP_IMAGE!=null)
                {
                    Drawable d = new BitmapDrawable(cm.getResources(),BITMAP_IMAGE);
                    image_frame.setImageDrawable(d);
                    dialog.getWindow().setBackgroundDrawable(null);
                }
                image_frame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button back = (Button) dialog.findViewById(R.id.back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        close();
                    }
                });

                Button send = (Button) dialog.findViewById(R.id.send);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cm.sendFeature(v);
                    }
                });

        return dialog;
    }

    public void close() {
        dialog.dismiss();
    }
}