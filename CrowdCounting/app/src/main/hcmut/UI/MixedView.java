package hcmut.UI;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by minh on 14/08/2015.
 */

/**
 * A design of multiple layers of views. At each time, one layer containing its views is visible.
 * All other layers are gone (is not visible).
 */
public class MixedView extends RelativeLayout {
    private ArrayList<Integer> mLayers = new ArrayList<Integer>();
    private int mCurrentLayerIndex = 0;
    private int mWidthPx;
    private int mHeightPx;

    public MixedView(Context context, int widthPx, int heightPx) {
        super(context);
        mWidthPx = widthPx;
        mHeightPx = heightPx;

        ViewGroup.LayoutParams thisParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(thisParams);
    }

    public void addViewsToLayer(int layerIndex, View...views) {
        for(View v:views) {
            addViewToLayer(v, layerIndex);
        }
    }

    public void addViewToLayer(View v, int layerIndex) {
        ViewGroup.LayoutParams params = v.getLayoutParams();
        if(params.width == ViewGroup.LayoutParams.MATCH_PARENT &&
                params.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            this.addView(v, mWidthPx, mHeightPx);
        } else {
            this.addView(v);
        }
        mLayers.add(layerIndex);
        if(layerIndex==mCurrentLayerIndex) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

    public void showLayer(int layerIndex) {
        if(layerIndex==mCurrentLayerIndex) { return; }
        for(int i=0; i<this.getChildCount(); i++) {
            if(mLayers.get(i)==layerIndex) {
                this.getChildAt(i).setVisibility(View.VISIBLE);
            } else {
                this.getChildAt(i).setVisibility(View.GONE);
            }
        }
        mCurrentLayerIndex = layerIndex;
    }

    public int getCurrentLayerIndex() {
        return mCurrentLayerIndex;
    }

    /**
     * remove the views having tags equal to input; use "equals" method for comparation
     * @param tags
     */
    public void removeViewByTag(String...tags) {
        for(String tag : tags) {
            for(int i=0; i<this.getChildCount(); i++) {
                View v = this.getChildAt(i);
                if(v.getTag() instanceof String) {
                    String t = (String) v.getTag();
                    if(tag.equals(t)) {
                        removeView(v);
                        break;
                    }
                }
            }
        }
    }

    /*public ImageView getFirstImageView() {
        for(int i=0; i<this.getChildCount(); i++) {
            View v = this.getChildAt(i);
            if(v instanceof ImageView) {
                return (ImageView) v;
            }
        }
        return null;
    }

    public ImageView getLastImageView() {
        for(int i=this.getChildCount()-1; i>=0; i--) {
            View v = this.getChildAt(i);
            if(v instanceof ImageView) {
                return (ImageView) v;
            }
        }
        return null;
    }*/
}
