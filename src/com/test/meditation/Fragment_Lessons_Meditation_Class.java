package com.test.meditation;

import java.util.ArrayList;
import java.util.Map;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class Fragment_Lessons_Meditation_Class extends Fragment {

	/**
	 * 
	 * @author lenovo Interface To Communicate With Activity
	 */
	public interface Play_Lesson_File {
		/**
		 * 
		 * @param boolean value
		 */
		public void Play_Music(String audio, String Video);

	}

	private GridView gridview_more_apps;
	private Custom_MediGrid_Adapter mgridview_adapter;
	Map<String, ArrayList<String>> arraylist_medi_list;
	ArrayList<String> meditation_audio = new ArrayList<String>();
	ArrayList<String> meditation_video = new ArrayList<String>();
	Play_Lesson_File mCallback;
	TextView textview_frag_meditation_title;
	private Typeface typeface_avenier_bold;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		typeface_avenier_bold = Typeface.createFromAsset(getActivity()
				.getAssets(), "avenier_font.ttf");
		View view = inflater.inflate(R.layout.fragment_meditation_list,
				container, false);
		arraylist_medi_list = Data_Helper_Global_Class
				.getMeditation_medi_list();
		textview_frag_meditation_title = (TextView) view
				.findViewById(R.id.textView_frag_meditation_title);
		for (String key : arraylist_medi_list.keySet()) {
			if (key.contains(Data_Helper_Global_Class.getApp_key_to_fetch()
					+ "_")) {
				ArrayList<String> medi_list = arraylist_medi_list.get(key);
				meditation_audio.add(medi_list.get(3));
				meditation_video.add(medi_list.get(4));
			}
		}
		textview_frag_meditation_title.setTypeface(typeface_avenier_bold);

		gridview_more_apps = (GridView) view
				.findViewById(R.id.gridview_meditation_lessons);

		// set Details in Gridview using Custom_Grid_Adapter Class.
		mgridview_adapter = new Custom_MediGrid_Adapter(getActivity(),
				arraylist_medi_list);
		gridview_more_apps.setAdapter(mgridview_adapter);

		gridview_more_apps
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						mCallback.Play_Music(meditation_audio.get(arg2),
								meditation_video.get(arg2));
					}

				});

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mCallback = (Play_Lesson_File) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

}
