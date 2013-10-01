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

public class Custom_StoreGrid_Adapter extends BaseAdapter {
	ArrayList<String> mapp_Name = new ArrayList<String>();
	ArrayList<String> mapp_author_name = new ArrayList<String>();
	ArrayList<String> mapp_title = new ArrayList<String>();
	ArrayList<String> mapp_url = new ArrayList<String>();
	ArrayList<String> mapp_thumbnail = new ArrayList<String>();
	Context context;
	private static LayoutInflater inflater = null;
	private Typeface typeface_avenier_regular, typeface_avenier_bold;

	public Custom_StoreGrid_Adapter(Context applicationContext,
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
				mapp_Name.add(medi_list.get(1));
				mapp_title.add(medi_list.get(2));
				mapp_url.add(medi_list.get(3));
				mapp_author_name.add(medi_list.get(4));
				mapp_thumbnail.add(medi_list.get(5));
			}

		}

		this.context = applicationContext;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public int getCount() {
		return mapp_Name.size();
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
			vi = inflater.inflate(R.layout.custom_app_layout, null);
			holder = new ViewHolder();
			holder.textview_app_name = (TextView) vi
					.findViewById(R.id.textView_app_name);
			holder.textview_app_title = (TextView) vi
					.findViewById(R.id.textView_app_title);

			holder.textview_app_author_name = (TextView) vi
					.findViewById(R.id.textView_app_author);
			holder.imageView_lesson_thumnail = (ImageView) vi
					.findViewById(R.id.imageView_lesson);

			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
		holder.textview_app_name.setId(position);
		holder.textview_app_title.setId(position);
		holder.textview_app_author_name.setId(position);
		holder.imageView_lesson_thumnail.setId(position);

		holder.textview_app_name.setText(mapp_Name.get(position));
		holder.textview_app_author_name.setText(mapp_author_name.get(position));
		holder.textview_app_title.setText(mapp_title.get(position));
		holder.imageView_lesson_thumnail.setImageDrawable(getThumbnails(
				context, mapp_thumbnail.get(position)));
		holder.textview_app_author_name.setTypeface(typeface_avenier_regular);
		holder.textview_app_name.setTypeface(typeface_avenier_bold);
		holder.textview_app_title.setTypeface(typeface_avenier_regular);

		return vi;
	}

	public static class ViewHolder {
		public TextView textview_app_name, textview_app_author_name,
				textview_app_title;
		public ImageView imageView_lesson_thumnail;

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

}