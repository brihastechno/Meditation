package com.test.meditation;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Custom_MediGrid_Adapter extends BaseAdapter {
	ArrayList<String> meditation_Name = new ArrayList<String>();

	ArrayList<String> meditation_thumbnail = new ArrayList<String>();
	Context context;
	private static LayoutInflater inflater = null;
	private Typeface typeface_avenier_regular, typeface_avenier_bold;

	public Custom_MediGrid_Adapter(Context applicationContext,
			Map<String, ArrayList<String>> arraylist_medi_list) {
		// TODO Auto-generated constructor stub
		// setting The elemnts View To Grid Layout
		typeface_avenier_regular = Typeface.createFromAsset(
				applicationContext.getAssets(), "avenir_65medium.ttf");
		typeface_avenier_bold = Typeface.createFromAsset(
				applicationContext.getAssets(), "avenier_font.ttf");
		for (String key : arraylist_medi_list.keySet()) {
			if (key.contains(Data_Helper_Global_Class.getApp_key_to_fetch()
					+ "_")) {
				ArrayList<String> medi_list = arraylist_medi_list.get(key);
				meditation_Name.add(medi_list.get(1));
				meditation_thumbnail.add(medi_list.get(2));
			}

		}

		this.context = applicationContext;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public int getCount() {
		return meditation_Name.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.custom_medi_layout, null);
			holder = new ViewHolder();
			holder.textview_medi_name = (TextView) vi
					.findViewById(R.id.textView_medi_name);

			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}

		holder.textview_medi_name.setId(position);

		holder.textview_medi_name.setText(meditation_Name.get(position));

		if (position == 0) {
			vi.setBackgroundResource(R.drawable.store_back3);
			holder.textview_medi_name.setTypeface(typeface_avenier_bold);

		} else if (position == 1) {

			vi.setBackgroundResource(R.drawable.store_back);
			holder.textview_medi_name.setTypeface(typeface_avenier_regular);
		} else {

			vi.setBackgroundResource(R.drawable.store_back2);
			holder.textview_medi_name.setTypeface(typeface_avenier_regular);
		}

		return vi;
	}

	public static class ViewHolder {
		public TextView textview_medi_name;

	}

}
