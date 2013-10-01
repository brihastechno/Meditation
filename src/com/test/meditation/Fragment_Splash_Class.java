package com.test.meditation;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

public class Fragment_Splash_Class extends Fragment {
	/**
	 * 
	 * @author lenovo Interface To Communicate With Activity
	 */
	public interface Inteface_Hide {
		/**
		 * 
		 * @param boolean value
		 */
		public void hide_layout(boolean value);

	}

	private Typeface typeface_avenier_bold, typeface_avenier_regular;
	ImageView image_logo;
	ObjectAnimator objectAnimator;
	private TextView textview_meditation_name, textview_medi_title;
	private TextView textview_author;
	ImageView imageView_author;
	ArrayList<String> arraylist_meditation_attributes;
	LinearLayout framelayout_frame2;
	Inteface_Hide mCallback;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		typeface_avenier_bold = Typeface.createFromAsset(getActivity()
				.getAssets(), "avenier_font.ttf");
		typeface_avenier_regular = Typeface.createFromAsset(getActivity()
				.getAssets(), "avenir_65medium.ttf");

		View view = inflater.inflate(R.layout.fragment_splash_layout,
				container, false);

		arraylist_meditation_attributes = Data_Helper_Global_Class
				.getMain_application_attributes();

		// initialize_Components();

		framelayout_frame2 = (LinearLayout) view.findViewById(R.id.frame2);
		image_logo = (ImageView) view.findViewById(R.id.image_logo);
		textview_meditation_name = (TextView) view
				.findViewById(R.id.textView_medi_name);
		textview_author = (TextView) view
				.findViewById(R.id.textView_authorname);
		textview_medi_title = (TextView) view.findViewById(R.id.textView_title);
		imageView_author = (ImageView) view.findViewById(R.id.imageView_author);

		// Set the Values
		// author_name
		textview_author.setText(arraylist_meditation_attributes.get(0));
		// title _meditation
		textview_medi_title.setText(arraylist_meditation_attributes.get(2));
		// name _meditation
		textview_meditation_name
				.setText(arraylist_meditation_attributes.get(3));
		// author_image from path getThumbnail
		imageView_author.setImageDrawable(getThumbnails(getActivity(),
				arraylist_meditation_attributes.get(1)));
		textview_author.setTypeface(typeface_avenier_regular);
		textview_meditation_name.setTypeface(typeface_avenier_bold);
		textview_medi_title.setTypeface(typeface_avenier_regular);

		show_animation_to_Image(image_logo);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mCallback = (Inteface_Hide) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	/**
	 * Make The Fading Animation to imageView logo
	 * 
	 * @param ImageView
	 */
	private void show_animation_to_Image(final ImageView image) {
		// TODO Auto-generated method stub
		objectAnimator = ObjectAnimator.ofFloat(image, "alpha", 0f, 1f);
		objectAnimator.setDuration(2000);
		objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
		objectAnimator.setRepeatCount(1);
		objectAnimator.addListener(new AnimatorListener() {
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub
				// start the music in background

			}

			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				// Call To The Next Layout Function
				framelayout_frame2.setVisibility(View.VISIBLE);
				show_animation_to_TextView(textview_meditation_name);

			}

			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
			}
		});
		objectAnimator.start();
	}

	/**
	 * 
	 * @param context
	 * @param mthumbnail_path
	 * @return Drawable
	 * @exception Resources.NotFoundException
	 */
	private Drawable getThumbnails(Context context, String mthumbnail_path) {
		Drawable drawable = null;
		Resources res = context.getResources();

		int resID = res.getIdentifier(mthumbnail_path, "drawable",
				context.getPackageName());
		try {
			drawable = res.getDrawable(resID);
		} catch (Resources.NotFoundException e) {
			Log.e("Not Found Exception", "Not Found Resources at");
			// TODO: handle exception
		}

		return drawable;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ObjectAnimator.clearAllAnimations();
	}

	/**
	 * 
	 * @param TextView
	 */
	private void show_animation_to_TextView(final TextView textview) {
		// TODO Auto-generated method stub
		objectAnimator = ObjectAnimator.ofFloat(textview, "alpha", 0f, 1f);
		objectAnimator.setDuration(1000);
		objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
		objectAnimator.setRepeatCount(1);
		objectAnimator.addListener(new AnimatorListener() {
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub

			}

			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				// Start New Activity And Send Parameter The Video file name
				// Fetch From XML
				mCallback.hide_layout(true);

			}

			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
			}
		});
		objectAnimator.start();
	}

}
