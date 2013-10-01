package com.test.meditation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Fragment_Connect_Class extends Fragment implements OnClickListener {

	SharedPreferences sharedPreferences;
	private ImageView imageView_facebook, imageView_tweet, imageView_sms,
			imageView_mail;
	private String share_message = "Hey! Checkout The New Meditaion App For Android. Go To Link: http://google.com";
	Typeface typeface_avenier_bold, typeface_avenier_regular;
	TextView textview_title, textview_share_app, textview_widget;
	Button button_go;
	EditText edittext_email;

	public interface Facebook_connect {

		public void facebook_connect();

	}

	Facebook_connect mCallback;

	public static final String APP_ID = "466988110027194";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		typeface_avenier_bold = Typeface.createFromAsset(getActivity()
				.getAssets(), "avenier_font.ttf");
		typeface_avenier_regular = Typeface.createFromAsset(getActivity()
				.getAssets(), "avenir_65medium.ttf");
		View view = inflater.inflate(R.layout.fragment_connect_layout,
				container, false);
		button_go = (Button) view.findViewById(R.id.button_go);
		edittext_email = (EditText) view.findViewById(R.id.editText1);
		textview_title = (TextView) view
				.findViewById(R.id.textView_connect_title);
		textview_share_app = (TextView) view
				.findViewById(R.id.textView_shere_this);
		textview_widget = (TextView) view.findViewById(R.id.textView_widget);
		imageView_facebook = (ImageView) view
				.findViewById(R.id.imageView_facebook);
		imageView_mail = (ImageView) view.findViewById(R.id.imageView_message);
		imageView_sms = (ImageView) view.findViewById(R.id.imageView_sms);
		imageView_tweet = (ImageView) view.findViewById(R.id.imageView_tweet);
		textview_title.setTypeface(typeface_avenier_bold);
		textview_widget.setTypeface(typeface_avenier_regular);
		textview_share_app.setTypeface(typeface_avenier_regular);
		button_go.setTypeface(typeface_avenier_bold);
		edittext_email.setTypeface(typeface_avenier_regular);
		imageView_facebook.setOnClickListener(this);
		imageView_sms.setOnClickListener(this);
		imageView_mail.setOnClickListener(this);
		imageView_tweet.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int event = v.getId();
		switch (event) {
		case R.id.imageView_facebook:

			// String faceUrl =
			// "http://m.facebook.com/sharer.php?u=http://www.google.com";
			// Uri faceuri = Uri.parse(faceUrl);
			// startActivity(new Intent(Intent.ACTION_VIEW, faceuri));
			//

			mCallback.facebook_connect();

			break;

		case R.id.imageView_sms:
			// Call to The Inbuilt Messaging App of Device To send Sms Message
			Intent sms_intent = new Intent(Intent.ACTION_VIEW);
			sms_intent.putExtra("sms_body", share_message);
			sms_intent.setData(Uri.parse("smsto:"));
			startActivity(sms_intent);

			break;

		case R.id.imageView_message:
			// Open Mail Application of device To Send Mail
			Intent email = new Intent(Intent.ACTION_SEND);
			email.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
			email.putExtra(Intent.EXTRA_SUBJECT, "InfoMeditation");
			email.putExtra(Intent.EXTRA_TEXT, share_message);
			// need this to prompts email client only
			email.setType("message/rfc822");

			startActivity(Intent.createChooser(email,
					"Choose an Email client :"));
			break;
		case R.id.imageView_tweet:
			// Sent The Link TO Share On Twitter....
			String tweetUrl = "https://twitter.com/intent/tweet?text="
					+ share_message + "&url=" + "https://www.google.com";
			Uri uri = Uri.parse(tweetUrl);
			startActivity(new Intent(Intent.ACTION_VIEW, uri));
			break;
		default:
			break;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mCallback = (Facebook_connect) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

}
