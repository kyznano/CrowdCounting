package hcmut.framework.lib;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class AppLibUI {
	
	public static LayerDrawable stickImage(Drawable... images) {		
		int numberOfImages = images.length;
		Drawable[] layers = new Drawable[numberOfImages];		
		for(int i=0; i<=numberOfImages; i++) {
			layers[i] = images[i];	
		}				
		return new LayerDrawable(layers);
	}
	
	public static LayerDrawable stickImage(Drawable background, Drawable foreground) {		
		Drawable[] layers = new Drawable[2];
		layers[0] = background;
		layers[1] = foreground;		
		return new LayerDrawable(layers);
	}
	
	// fontLocation: "fonts/Symbola.ttf" (and fonts folder is in assets)
	public static void setFont(Context context, String fontLocation, boolean boldOrNot, TextView ...multiTextViews)
	{
		for(TextView v : multiTextViews) {
			Typeface font = Typeface.createFromAsset(context.getAssets(), fontLocation);
			int style = (boldOrNot==true)?Typeface.BOLD:Typeface.NORMAL;
			v.setTypeface(font, style);
		}
	}
	
	public static void setFont(Context context, String fontLocation, boolean boldOrNot, Button b)
	{
		Typeface font = Typeface.createFromAsset(context.getAssets(), fontLocation);
		int style = (boldOrNot==true)?Typeface.BOLD:Typeface.NORMAL;
		b.setTypeface(font, style);
	}
	
	public static void setSizeAndMargins(Button b, int size, int margin)
	{
		LayoutParams p = (LayoutParams) b.getLayoutParams();
		p.width = p.height = size;
		p.setMargins(margin, margin, margin, margin);
		b.setLayoutParams(p);
	}
	
	public static StateListDrawable buildStateList(Context context, Drawable clicked, Drawable nonClicked) {
		StateListDrawable states = new StateListDrawable();
		states.addState(new int[] {-android.R.attr.state_window_focused, android.R.attr.state_enabled},nonClicked);
		states.addState(new int[] {android.R.attr.state_pressed},clicked);
		states.addState(new int[] {android.R.attr.state_focused, android.R.attr.state_enabled},clicked);
		states.addState(new int[] {android.R.attr.state_enabled},nonClicked);
		
		return states;
	}
	
	public static ShapeDrawable generateCircleDrawable(String color, int circleSize) {
		ShapeDrawable circle= new ShapeDrawable( new OvalShape());
	    circle.setIntrinsicHeight( circleSize );
	    circle.setIntrinsicWidth( circleSize);
	    circle.setBounds(new Rect(0, 0, circleSize, circleSize));
	    circle.getPaint().setColor(Color.parseColor(color));
	    return circle;
	}
	
	public static void setTextSizeInPixel(TextView tv, float size) {
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
	}
	
	public static void setTextSizeInPixel(float size, TextView ...multiTextView) {
		for(TextView tv : multiTextView) {
			setTextSizeInPixel(tv, size);
		}
	}
	
	public static void setTextColor(String colorString, TextView ...multiTextView) {
		for(TextView tv : multiTextView) {
			tv.setTextColor(Color.parseColor(colorString));
		}
	}
	
	public static void setLayoutMarginOnly(View v, int marginLeft, int marginTop, int marginRight, int marginBottom) {
		setLayoutWidthHeight(v, -1, -1, marginLeft, marginTop, marginRight, marginBottom);
	}
	
	public static void setLayoutWidthHeight(View v, int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom) {
		setLinearLayoutWidthHeight(v, width, height, marginLeft, marginTop, marginRight, marginBottom);
	}
	
	private static void setLinearLayoutWidthHeight(View v, int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom) {
		try {
			android.widget.LinearLayout.LayoutParams linearParams = (android.widget.LinearLayout.LayoutParams) v.getLayoutParams();
			if(width>=0 && height>=0) {
				linearParams.width = width;
				linearParams.height = height;
			}
			linearParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
			linearParams.gravity = Gravity.CENTER_VERTICAL;
			v.setLayoutParams(linearParams);
		} catch(ClassCastException e) {
			setRelativeLayoutWidthHeight(v, width, height, marginLeft, marginTop, marginRight, marginBottom);
		}
	}
	
	private static void setRelativeLayoutWidthHeight(View v, int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom) {
		try {
			LayoutParams relativeParams = (LayoutParams) v.getLayoutParams();
			if(width>=0 && height>=0) {
				relativeParams.width = width;
				relativeParams.height = height;
			}
			relativeParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
			v.setLayoutParams(relativeParams);
		} catch(ClassCastException e) {
			
		}
	}
	
	/*public static AnimationDrawable getImageAnimation(Img img, String[] imgNames, int durations[]) {
		AnimationDrawable imageAnimation = null;
		if(imgNames.length>0 && imgNames.length==durations.length) {
			int count = imgNames.length;
			Drawable[] images = new Drawable[count];
			for(int i=0; i<count; i++) {
				images[i] = img.get(imgNames[i]);
			}
			imageAnimation = getImageAnimation(images, durations);
		}
		return imageAnimation;
	}*/
	
	public static AnimationDrawable getImageAnimation(Drawable[] images, int durations[]) {
		AnimationDrawable imageAnimation = null;
		if(images.length>0 && images.length==durations.length) {
			imageAnimation = new AnimationDrawable();
			for (int i=0; i<images.length; i++) {
				Drawable frame = images[i];
				imageAnimation.addFrame(frame, durations[i]);
			}
			imageAnimation.setOneShot(false);
			imageAnimation.selectDrawable(0);
		}
		return imageAnimation;
	}
	
	public static void setMaxCharacter(int numberOfMaxCharacter, EditText ...ets) {
		for(EditText et : ets) {
			InputFilter[] filterConstrain = new InputFilter[1];
			filterConstrain[0] = new InputFilter.LengthFilter(numberOfMaxCharacter);
			et.setFilters(filterConstrain);
		}
	}


}