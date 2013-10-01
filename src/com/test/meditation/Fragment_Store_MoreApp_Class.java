package com.test.meditation;

import java.util.ArrayList;
import java.util.Map;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class Fragment_Store_MoreApp_Class extends Fragment {

	private GridView gridview_more_apps;
	private Custom_StoreGrid_Adapter mgridview_adapter;
	Map<String, ArrayList<String>> arraylist_medi_list;
	private Typeface typeface_avenier_bold;
	private TextView textView_frag_moreapp_title;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		typeface_avenier_bold = Typeface.createFromAsset(getActivity()
				.getAssets(), "avenier_font.ttf");
		View view = inflater.inflate(R.layout.fragment_store_moreapps_layout,
				container, false);
		textView_frag_moreapp_title = (TextView) view
				.findViewById(R.id.textView_frag_moreapp_title);
		arraylist_medi_list = Data_Helper_Global_Class
				.getMeditation_store_list();
		textView_frag_moreapp_title.setTypeface(typeface_avenier_bold);
		// initializeUI();
		gridview_more_apps = (GridView) view
				.findViewById(R.id.gridview_more_apps);

		// set Details in Gridview using Custom_Grid_Adapter Class.
		mgridview_adapter = new Custom_StoreGrid_Adapter(getActivity(),
				arraylist_medi_list);
		gridview_more_apps.setAdapter(mgridview_adapter);

		gridview_more_apps
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

					}

				});
		return view;

	}

}
