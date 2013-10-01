package com.test.meditation;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment_Setting_Class extends Fragment {
	private Typeface typeface_avenier_bold, typeface_avenier_regular;
	TextView textView_setting_title, textview_rate, textview_push,
			textview_privacey, textview_version, textview_terms;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		typeface_avenier_bold = Typeface.createFromAsset(getActivity()
				.getAssets(), "avenier_font.ttf");
		typeface_avenier_regular = Typeface.createFromAsset(getActivity()
				.getAssets(), "avenir_65medium.ttf");
		View view = inflater.inflate(R.layout.fragment_setting_layout,
				container, false);
		textView_setting_title = (TextView) view
				.findViewById(R.id.textView_setting_title);
		textview_rate = (TextView) view.findViewById(R.id.textView_rate);
		textview_privacey = (TextView) view.findViewById(R.id.textView_privacy);
		textview_push = (TextView) view.findViewById(R.id.textView_push);
		textview_version = (TextView) view.findViewById(R.id.textView_version);
		textview_terms = (TextView) view.findViewById(R.id.textView_terms);
		textview_rate.setTypeface(typeface_avenier_regular);
		textview_privacey.setTypeface(typeface_avenier_regular);
		textview_push.setTypeface(typeface_avenier_regular);
		textview_version.setTypeface(typeface_avenier_regular);
		textview_terms.setTypeface(typeface_avenier_regular);
		textView_setting_title.setTypeface(typeface_avenier_bold);
		initialize_layout_components();
		return view;
	}

	/**
	 * Components Of Activity required For Handles Events
	 */
	private void initialize_layout_components() {

	}

}
