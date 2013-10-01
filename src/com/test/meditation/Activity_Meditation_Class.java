package com.test.meditation;

/**
 *  Activity_Meditation_Class Contains All The Fragments And Communicates With Fragments 
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookConnector;
import com.facebook.SessionEvents;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.xmlparse.meditation.Meditation;
import com.xmlparse.meditation.XMLMeditationParser;

//Implements Interface_Hide For Communicate With Fragment_Play_Video_Class
public class Activity_Meditation_Class extends FragmentActivity implements
		Fragment_Splash_Class.Inteface_Hide,
		Fragment_Lessons_Meditation_Class.Play_Lesson_File,
		SurfaceHolder.Callback, OnClickListener,
		Fragment_Connect_Class.Facebook_connect,
		MediaPlayer.OnCompletionListener {
	private String share_message = "Hey! Checkout The New Meditaion App For Android. Go To Link: http://google.com";

	private static final String FACEBOOK_APPID = "466988110027194";
	private static final String FACEBOOK_PERMISSION = "publish_stream";

	private final Handler mFacebookHandler = new Handler();
	private FacebookConnector facebookConnector;

	final Runnable mUpdateFacebookNotification = new Runnable() {
		public void run() {
			Toast.makeText(getBaseContext(), "Posted On Your TimeLine",
					Toast.LENGTH_LONG).show();

		}
	};

	private String TAG = "Fragment_Connect_Class";
	SharedPreferences sharedPreferences;
	Context mContext;
	Facebook mFacebook;
	private AsyncFacebookRunner mAsyncRunner;
	public static final String APP_ID = "265307690218720";
	private AssetFileDescriptor videoPath, audiopath;
	AnimatorSet animatorSet;
	private Button button_Setting, button_Store_cart, button_Group_Connect,
			button_listen;
	private Fragment fragment_Cart, fragment_connect, fragment_setting,
			fragment_medi_list;

	android.support.v4.app.Fragment fragment_splash_screen;
	private FrameLayout frameLayout_seekbar;
	private SeekBar seekbar;
	private LinearLayout linearlayout_menus_bar;
	private TextView textView_max_duration, textView_current_duration;
	private ImageView imageView_play_pause_icon, imageView_home_icon;
	private MediaPlayer mMediaPlayer, mMediPlayer_Audio;
	private SurfaceView surfaceView_mView;
	private SurfaceHolder surface_holder;
	private final Handler handler_for_seek = new Handler();
	private Handler handler_hide_show = new Handler();
	private Handler handler_tap_hide = new Handler();
	private SharedPreferences sharedpref;
	Meditation class_object_meditation;
	TextView textview_tap;
	ArrayList<String> arraylist_meditation_attributes = new ArrayList<String>();

	// Variables for Store the Music Play....
	int int_audio_background = 0, int_audio_lesson = 0;
	private String string_video_file_name, string_audio_file,
			string_lesson_video, string_lesson_audio;
	private Typeface typeface_avenier_bold;
	private static final String[] PERMS = new String[] { "publish_stream",
			"read_stream", "offline_access" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// fetching all Data From XML File
		this.facebookConnector = new FacebookConnector(FACEBOOK_APPID, this,
				getApplicationContext(), new String[] { FACEBOOK_PERMISSION });
		fetch_data_fromXML();
		// Set The layout
		setContentView(R.layout.activity_meditation_layout);
		typeface_avenier_bold = Typeface.createFromAsset(getAssets(),
				"avenier_font.ttf");
		// Get The application default video file name and audio file name
		string_video_file_name = Data_Helper_Global_Class.getVideo_file();
		string_audio_file = Data_Helper_Global_Class.getAudio_file();

		// Get The Full Path Of fileName From Assets Folder
		try {
			videoPath = getAssets().openFd(string_video_file_name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Set The Seek BAr to Zero On Create Activity
		seek_progess_store(int_audio_background, int_audio_lesson);
		// initialize activity layout components
		initialize_layout_components();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// Implement on start Activity
		// show hide and menu_bar and seek_bar at first time
		if (fragment_connect.isHidden() && fragment_setting.isHidden()
				&& fragment_Cart.isHidden()
				&& fragment_splash_screen.isHidden())
			hide_menus_seekBars();
		// Initialize and Start The Video Palyer
		initialize_start_Video();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		// finish the activity
		remove_hide_callback();
		finish();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// On Resume Check If Already media player exist and Playing
		try {
			if (mMediaPlayer != null) {
				if (mMediaPlayer.isPlaying())
					mMediaPlayer.start();

			}
			if (mMediPlayer_Audio != null) {
				if (mMediPlayer_Audio.isPlaying())
					mMediPlayer_Audio.start();
			}
		} catch (IllegalStateException e) {

		} catch (NullPointerException e) {

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			finish();

		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Functions to fetch the data from XML
	 */
	private void fetch_data_fromXML() {
		// TODO Auto-generated method stub
		// get Data as InputStream from File
		InputStream input_stream = getXMLFileFromRaw();

		// Send The input_stream for parsing Using XMLPArser and set Data in
		// class Meditation
		class_object_meditation = XMLMeditationParser.parse(input_stream);

		// Fetching The mediation attributes and their list corresponding to
		// that.
		// For This Time Test key is set to static and fetch data of meditation
		// have id "med1" and one of member "med1_meditation1" in XML File
		// Storing the Key On Data_Helper_Global_Class You can change the key
		// w.r.t. your application key there
		arraylist_meditation_attributes = class_object_meditation.meditation_app_list
				.get(Data_Helper_Global_Class.getApp_key_to_fetch());

		// set The application attributes in array the starting tag Of XML
		Data_Helper_Global_Class
				.setMain_application_attributes(arraylist_meditation_attributes);
		Data_Helper_Global_Class
				.setMeditation_store_list(class_object_meditation.meditation_store_list);

		Data_Helper_Global_Class
				.setMeditation_medi_list(class_object_meditation.meditation_medi_list);

		// Get The Video File name from XML Data fetch
		string_video_file_name = arraylist_meditation_attributes.get(5);
		string_audio_file = arraylist_meditation_attributes.get(4);
		Log.e("Video File", "video file Name " + string_video_file_name);

		// set The audi And Video File In Gloabal Class
		Data_Helper_Global_Class.setVideo_file(string_video_file_name);
		Data_Helper_Global_Class.setAudio_file(string_audio_file);
	}

	/**
	 * 
	 * @return InputStream
	 */
	private InputStream getXMLFileFromRaw() {
		// the target filename in the application path
		InputStream input_Stream = null;
		try {
			input_Stream = getResources()
					.openRawResource(R.raw.meditations_app);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return input_Stream;

	}

	/**
	 * Components Of Activity required For Handles Events
	 */
	private void initialize_layout_components() {
		textView_current_duration = (TextView) findViewById(R.id.textView_current_duration);
		textView_max_duration = (TextView) findViewById(R.id.textView_max_duration);
		textview_tap = (TextView) findViewById(R.id.textView_tap);
		seekbar = (SeekBar) findViewById(R.id.seekBar_media);
		imageView_play_pause_icon = (ImageView) findViewById(R.id.imageView_play_icon);
		surfaceView_mView = (SurfaceView) findViewById(R.id.screen_tutorial_video_surface);
		linearlayout_menus_bar = (LinearLayout) findViewById(R.id.show_hide);
		frameLayout_seekbar = (FrameLayout) findViewById(R.id.layout_seek_bar);
		button_Setting = (Button) findViewById(R.id.button_settings);
		button_Group_Connect = (Button) findViewById(R.id.button_group_connect);
		button_Store_cart = (Button) findViewById(R.id.button_store_cart);
		button_listen = (Button) findViewById(R.id.button_listen);
		imageView_home_icon = (ImageView) findViewById(R.id.imageView_home);
		textView_current_duration.setTypeface(typeface_avenier_bold);
		textView_max_duration.setTypeface(typeface_avenier_bold);
		textview_tap.setTypeface(typeface_avenier_bold);
		// disable the button on first time ...
		seekbar.setEnabled(false);
		surfaceView_mView.setEnabled(false);
		imageView_play_pause_icon.setOnClickListener(this);

		// Get The All Fragment USing Fragment Manager From Layout XML
		FragmentManager frag_manager = getSupportFragmentManager();

		fragment_splash_screen = frag_manager
				.findFragmentById(R.id.fragment_splash_screen);
		fragment_connect = frag_manager
				.findFragmentById(R.id.fragment_group_connect);
		fragment_Cart = frag_manager
				.findFragmentById(R.id.fragment_store_moreapp);
		fragment_medi_list = frag_manager
				.findFragmentById(R.id.fragment_medi_list);
		fragment_setting = frag_manager.findFragmentById(R.id.fragment_setting);

		// hide All The Fragment At Activity Created
		FragmentTransaction frag_transaction = getSupportFragmentManager()
				.beginTransaction();
		frag_transaction.hide(fragment_connect);
		frag_transaction.hide(fragment_Cart);
		frag_transaction.hide(fragment_setting);
		frag_transaction.hide(fragment_medi_list);

		// showing the Splash Screen at Very First of open application
		frag_transaction.show(fragment_splash_screen);

		frag_transaction.commit();

		// OnClickListener Event On Button
		button_listen.setOnClickListener(this);
		button_Group_Connect.setOnClickListener(this);
		button_Store_cart.setOnClickListener(this);
		imageView_home_icon.setOnClickListener(this);
		button_Setting.setOnClickListener(this);

		// Set The OnTouchListener On SeekBar
		seekbar.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				seekChange(v);
				return false;
			}
		});

		// Set The setOnClickListener On surfaceView_mView
		surfaceView_mView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (linearlayout_menus_bar.getVisibility() == View.VISIBLE) {
					handler_hide_show.removeCallbacks(hideControllerThread);
					// show and hide the layouts
					// hiding controllers using callback with animation
					hide_menus_seekBars();
				} else {
					// showing seekbar and play pause button and other
					// controllers with Animation fade in
					show_all_controllers();
				}
			}
		});

	}

	/**
	 * Auto_Hide Layouts..Time set 5000 5 seconds To Hide
	 */
	public void hide_menus_seekBars() {
		handler_hide_show.postDelayed(hideControllerThread, 5000);
	}

	/**
	 * Handle The Thread of Auto Hide
	 * 
	 */
	private Runnable hideControllerThread = new Runnable() {

		public void run() {
			// Hide controllers with animation
			hide_controllers_animation();
		}
	};
	/**
	 * Handle The hideTapmenuThread of Auto Hide for 3 sec
	 * 
	 */
	private Runnable hideTapmenuThread = new Runnable() {

		public void run() {
			textview_tap.setVisibility(View.GONE);
		}
	};

	/**
	 * Shows The Layouts
	 * 
	 */
	public void showControllers() {
		linearlayout_menus_bar.setVisibility(View.VISIBLE);
		imageView_play_pause_icon.setVisibility(View.VISIBLE);
		frameLayout_seekbar.setVisibility(View.VISIBLE);
		imageView_home_icon.setVisibility(View.VISIBLE);
		textview_tap.setVisibility(View.GONE);
		handler_hide_show.removeCallbacks(hideControllerThread);
		// call to auto_hide
		hide_menus_seekBars();
	}

	/**
	 * 
	 * In initialize medialPlayer and Surface Holder
	 */

	private void initialize_start_Video() {
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		surface_holder = surfaceView_mView.getHolder();
		surface_holder.addCallback(this);

		// deprecated setting, but required on Android versions prior to 3.0
		surface_holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surface_holder.setFixedSize(getWindow().getWindowManager()
				.getDefaultDisplay().getWidth(), getWindow().getWindowManager()
				.getDefaultDisplay().getHeight());

	}

	/**
	 * Override Methods To Handle The surface Change we used surfaceCreated to
	 * start video
	 */
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		// start_video
		start_Player();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		stop_media_player();
		mMediaPlayer.release();
		mMediPlayer_Audio.release();
		seek_progess_store(seekbar.getProgress(), 0);

	}

	/**
	 * start_the Player
	 */
	private void start_Player() {
		mMediPlayer_Audio = new MediaPlayer();
		Log.e("String audi File NAme", "" + string_audio_file);
		try {
			audiopath = getAssets().openFd(string_audio_file);
			mMediPlayer_Audio.setDataSource(audiopath.getFileDescriptor(),
					audiopath.getStartOffset(), audiopath.getLength());
			mMediPlayer_Audio.prepare();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMediPlayer_Audio.setLooping(true);
		mMediPlayer_Audio.setVolume(100, 100);
		mMediPlayer_Audio.start();
		mMediPlayer_Audio.setOnCompletionListener(this);

		seekbar.setMax(mMediPlayer_Audio.getDuration());
		// textView_max_duration.setText(""
		// + formatTime(mMediPlayer_Audio.getDuration()));
		imageView_play_pause_icon.setImageResource(R.drawable.icon_pause);
		get_updated_seek_progress(true);
		// Start The progress Updater to seekBar
		startPlayProgressUpdater();
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setDisplay(surface_holder);
		try {
			// Get The Video File From Video Path and prepare
			mMediaPlayer.setDataSource(videoPath.getFileDescriptor(),
					videoPath.getStartOffset(), videoPath.getLength());
			mMediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		// Get length Of video file and set to TextView and seekBar as Maximum

		mMediaPlayer.setLooping(true);
		mMediaPlayer.setVolume(0, 0);
		// start The Video
		mMediaPlayer.start();

	}

	/**
	 * remove The CAllBAck Of Auto Hide Menus BAr
	 */
	public void remove_hide_callback() {
		ObjectAnimator.clearAllAnimations();
		frameLayout_seekbar.setVisibility(View.GONE);
		imageView_play_pause_icon.setVisibility(View.GONE);
		imageView_home_icon.setVisibility(View.GONE);
		textview_tap.setVisibility(View.GONE);
		surfaceView_mView.setEnabled(false);
		handler_hide_show.removeCallbacksAndMessages(null);
		linearlayout_menus_bar.setVisibility(View.VISIBLE);
		handler_tap_hide.removeCallbacksAndMessages(null);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int event = v.getId();
		switch (event) {
		case R.id.button_listen:
			// check if not lesson is selected OR stay in lesson_play Screen
			// ......Open The home page Lesson view
			if (string_lesson_audio == null
					|| (fragment_splash_screen.isHidden()
							&& fragment_setting.isHidden()
							&& fragment_Cart.isHidden()
							&& fragment_connect.isHidden() && fragment_medi_list
							.isHidden())) {

				button_listen.setEnabled(true);
				button_Group_Connect.setEnabled(true);
				button_Setting.setEnabled(true);
				button_Store_cart.setEnabled(true);
				// Removing Callback of Auto Hide From Play Fragment

				remove_hide_callback();
				// check if in lesson play screen and have to reset audio with
				// change background
				if (string_lesson_audio != null) {
					seek_progess_store(int_audio_background,
							seekbar.getProgress());
					reset_audio_player(string_audio_file, true);
					reset_video_player(string_video_file_name);

				}
				FragmentTransaction fragment_medi = getSupportFragmentManager()
						.beginTransaction();
				// Setting THe Fading Animation to layout Fade in And Fade Out
				// Effects To Layout at Transaction
				fragment_medi.setCustomAnimations(android.R.anim.fade_in,
						android.R.anim.fade_out, android.R.anim.fade_in,
						android.R.anim.fade_out);
				// Hide And show Fragment
				fragment_medi.hide(fragment_splash_screen);
				fragment_medi.hide(fragment_Cart);
				fragment_medi.hide(fragment_connect);
				fragment_medi.hide(fragment_setting);
				fragment_medi.show(fragment_medi_list);
				// Commit Transaction
				fragment_medi.commit();

			} else {

				// store the progress of audio in shared preferences...
				seek_progess_store(seekbar.getProgress(), int_audio_lesson);
				// enable the unable buttons...and disables button_listen to
				// click
				button_listen.setEnabled(true);
				button_Group_Connect.setEnabled(true);
				button_Setting.setEnabled(true);
				button_Store_cart.setEnabled(true);
				// as on change Change audio and video on media
				// players...playing existing lesson video and audio...
				reset_audio_player(string_lesson_audio, false);
				reset_video_player(string_lesson_video);
				// set looping false as in lesson_play screen
				mMediPlayer_Audio.setLooping(false);

				FragmentTransaction frag_paly = getSupportFragmentManager()
						.beginTransaction();
				// Setting THe Fading Animation to layout Fade in And Fade Out
				// Effects To Layout
				frag_paly.setCustomAnimations(android.R.anim.fade_in,
						android.R.anim.fade_out, android.R.anim.fade_in,
						android.R.anim.fade_out);
				// Hide all except Play Fragment
				frag_paly.hide(fragment_splash_screen);
				frag_paly.hide(fragment_Cart);
				frag_paly.hide(fragment_connect);
				frag_paly.hide(fragment_setting);
				frag_paly.hide(fragment_medi_list);

				// commit Transaction
				frag_paly.commit();
				// start the Auto Hide Callback On Play Sceen
				// On play lesson screen showing all controllers...
				seekbar.setEnabled(true);
				surfaceView_mView.setEnabled(true);
				showControllers();
			}
			break;
		case R.id.button_group_connect:
			// check if not lesson is selected OR stay in lesson_play Screen
			// ......Open The home page Lesson view
			if (fragment_splash_screen.isHidden()
					&& fragment_setting.isHidden() && fragment_Cart.isHidden()
					&& fragment_connect.isHidden()
					&& fragment_medi_list.isHidden()) {
				// store the lesson audio progress..and change audio and video
				// in media players...
				seek_progess_store(int_audio_background, seekbar.getProgress());
				reset_audio_player(string_audio_file, true);
				reset_video_player(string_video_file_name);

			} else {
				// store the background audio progress for next time...
				seek_progess_store(seekbar.getProgress(), int_audio_lesson);
			}
			button_listen.setEnabled(true);
			button_Group_Connect.setEnabled(false);
			button_Setting.setEnabled(true);
			button_Store_cart.setEnabled(true);
			// Removing Callback of Auto Hide From Play Fragment

			remove_hide_callback();

			FragmentTransaction fragment_group = getSupportFragmentManager()
					.beginTransaction();
			// Setting THe Fading Animation to layout Fade in And Fade Out
			// Effects To Layout at Transaction
			fragment_group.setCustomAnimations(android.R.anim.fade_in,
					android.R.anim.fade_out, android.R.anim.fade_in,
					android.R.anim.fade_out);
			// Hide And show Fragment
			fragment_group.hide(fragment_splash_screen);
			fragment_group.hide(fragment_Cart);
			fragment_group.show(fragment_connect);
			fragment_group.hide(fragment_setting);
			fragment_group.hide(fragment_medi_list);
			// commit Transaction
			fragment_group.commit();
			break;

		case R.id.button_store_cart:
			// check if not lesson is selected OR stay in lesson_play Screen
			// ......Open The home page Lesson view
			if (fragment_splash_screen.isHidden()
					&& fragment_setting.isHidden() && fragment_Cart.isHidden()
					&& fragment_connect.isHidden()
					&& fragment_medi_list.isHidden()) {
				seek_progess_store(int_audio_background, seekbar.getProgress());
				reset_audio_player(string_audio_file, true);
				reset_video_player(string_video_file_name);
			}

			else {
				seek_progess_store(seekbar.getProgress(), int_audio_lesson);
			}
			button_listen.setEnabled(true);
			button_Group_Connect.setEnabled(true);
			button_Setting.setEnabled(true);
			button_Store_cart.setEnabled(false);
			Log.d("Button_onClick_Listen", "Button_store Cart_preess");
			// Removing Callback of Auto Hide From Play Fragment

			remove_hide_callback();
			FragmentTransaction fragment_cart = getSupportFragmentManager()
					.beginTransaction();
			// Setting THe Fading Animation to layout Fade in And Fade Out
			// Effects To Layout at Transaction
			fragment_cart.setCustomAnimations(android.R.anim.fade_in,
					android.R.anim.fade_out, android.R.anim.fade_in,
					android.R.anim.fade_out);
			// Hide And show Fragment
			fragment_cart.hide(fragment_splash_screen);
			fragment_cart.show(fragment_Cart);
			fragment_cart.hide(fragment_connect);
			fragment_cart.hide(fragment_setting);
			fragment_cart.hide(fragment_medi_list);
			// commit Transaction
			fragment_cart.commit();
			break;

		case R.id.button_settings:

			// Removing Callback of Auto Hide From Play Fragment
			if (fragment_splash_screen.isHidden()
					&& fragment_setting.isHidden() && fragment_Cart.isHidden()
					&& fragment_connect.isHidden()
					&& fragment_medi_list.isHidden()) {
				seek_progess_store(int_audio_background, seekbar.getProgress());
				reset_audio_player(string_audio_file, true);
				reset_video_player(string_video_file_name);
			} else {
				seek_progess_store(seekbar.getProgress(), int_audio_lesson);
			}
			button_listen.setEnabled(true);
			button_Group_Connect.setEnabled(true);
			button_Setting.setEnabled(false);
			button_Store_cart.setEnabled(true);
			remove_hide_callback();

			FragmentTransaction fragment_set = getSupportFragmentManager()
					.beginTransaction();

			// Setting THe Fading Animation to layout Fade in And Fade Out
			// Effects To Layout at Transaction
			fragment_set.setCustomAnimations(android.R.anim.fade_in,
					android.R.anim.fade_out, android.R.anim.fade_in,
					android.R.anim.fade_out);

			// Hide And show Fragment
			fragment_set.hide(fragment_splash_screen);
			fragment_set.hide(fragment_Cart);
			fragment_set.hide(fragment_connect);
			fragment_set.show(fragment_setting);
			fragment_set.hide(fragment_medi_list);

			// Commit Transaction
			fragment_set.commit();
			break;

		case R.id.imageView_home:
			button_listen.setEnabled(true);
			button_Group_Connect.setEnabled(true);
			button_Setting.setEnabled(true);
			button_Store_cart.setEnabled(true);
			// Removing Callback of Auto Hide From Play Fragment
			seek_progess_store(int_audio_background, seekbar.getProgress());
			remove_hide_callback();
			reset_audio_player(string_audio_file, true);
			reset_video_player(string_video_file_name);
			FragmentTransaction fragment_medi = getSupportFragmentManager()
					.beginTransaction();
			// Setting THe Fading Animation to layout Fade in And Fade Out
			// Effects To Layout at Transaction
			fragment_medi.setCustomAnimations(android.R.anim.fade_in,
					android.R.anim.fade_out, android.R.anim.fade_in,
					android.R.anim.fade_out);
			// Hide And show Fragment
			fragment_medi.hide(fragment_splash_screen);
			fragment_medi.hide(fragment_Cart);
			fragment_medi.hide(fragment_connect);
			fragment_medi.hide(fragment_setting);
			fragment_medi.show(fragment_medi_list);
			// Commit Transaction
			fragment_medi.commit();
			break;
		case R.id.imageView_play_icon:
			// switch The Play and Pause Image
			if (mMediPlayer_Audio.isPlaying()) {
				imageView_play_pause_icon
						.setImageResource(R.drawable.icon_play);
				// pause the video
				mMediPlayer_Audio.pause();
			} else {

				imageView_play_pause_icon
						.setImageResource(R.drawable.icon_pause);
				// start the video
				mMediPlayer_Audio.start();

				// start progressUpdater
				startPlayProgressUpdater();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * change the milliseconds To Output in mm:ss as string
	 * 
	 * @param millisecond
	 * @return String
	 */
	private String formatTime(long millisecond) {
		String output = "00:00:00";
		long seconds = millisecond / 1000;
		long minutes = seconds / 60;
		seconds = seconds % 60;

		String secondsD = String.valueOf(seconds);
		String minutesD = String.valueOf(minutes);

		if (seconds < 10)
			secondsD = "0" + seconds;
		if (minutes < 10)
			minutesD = "0" + minutes;

		output = minutesD + " : " + secondsD;
		return output;
	}

	/**
	 * This is event handler thumb moving event
	 * 
	 * @param view
	 */
	private void seekChange(View view) {
		SeekBar sb = (SeekBar) view;
		mMediPlayer_Audio.seekTo(sb.getProgress());
		textView_current_duration.setText(""
				+ formatTime(seekbar.getProgress()));

	}

	/**
	 * Update The Progress in SeekBar and display the Text
	 */
	public void startPlayProgressUpdater() {

		if (mMediPlayer_Audio.isPlaying()) {
			seekbar.setProgress(mMediPlayer_Audio.getCurrentPosition());

			Runnable notification = new Runnable() {
				public void run() {
					startPlayProgressUpdater();
				}
			};
			handler_for_seek.postDelayed(notification, 1000);
			textView_current_duration.setText(""
					+ formatTime(seekbar.getProgress()));
			textView_max_duration.setText(""
					+ formatTime(seekbar.getMax() - seekbar.getProgress()));
		} else {

		}

	}

	/**
	 * stop the MediaPlayer and remove callback of Handler
	 */

	public void stop_media_player() {
		mMediaPlayer.pause();
		mMediPlayer_Audio.pause();
		handler_for_seek.removeCallbacksAndMessages(null);

	}

	/**
	 * 
	 * get Seek_progress_
	 */
	public void get_updated_seek_progress(boolean value) {
		sharedpref = this.getSharedPreferences("mymeditation", MODE_PRIVATE);
		if (value) {
			int_audio_background = sharedpref.getInt("seek_back", 0);
			seekbar.setProgress(int_audio_background);
			// set New state To MediaPlayer
			seekChange(seekbar);
		} else {
			int_audio_lesson = sharedpref.getInt("seek_lesson", 0);
			seekbar.setProgress(int_audio_lesson);
			// set New state To MediaPlayer
			seekChange(seekbar);
		}

	}

	/**
	 * Store The Seek_progess
	 * 
	 * @param int seekprogress
	 */
	public void seek_progess_store(int seek_back, int seek_lesson) {
		int_audio_background = seek_back;
		int_audio_lesson = seek_lesson;
		sharedpref = this.getSharedPreferences("mymeditation", MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = sharedpref.edit();
		prefsEditor.putInt("seek_back", seek_back);
		prefsEditor.putInt("seek_lesson", seek_lesson);

		prefsEditor.commit();
	}

	/**
	 * Override Method From Fragment_Splash_Class
	 * 
	 * @param boolean value
	 */
	@Override
	public void hide_layout(boolean value) {
		// TODO Auto-generated method stub
		if (value) {
			FragmentTransaction fragment_medi_spalsh = getSupportFragmentManager()
					.beginTransaction();
			// Setting THe Fading Animation to layout Fade in And Fade Out
			// Effects To Layout at Transaction
			fragment_medi_spalsh.setCustomAnimations(android.R.anim.fade_in,
					android.R.anim.fade_out, android.R.anim.fade_in,
					android.R.anim.fade_out);
			// Hide And show Fragment
			fragment_medi_spalsh.hide(fragment_splash_screen);
			fragment_medi_spalsh.show(fragment_medi_list);
			fragment_medi_spalsh.commit();
			remove_hide_callback();
			linearlayout_menus_bar.setVisibility(View.VISIBLE);

		}
	}

	/**
	 * Overide Method From Fragment_Lesson_Meditation Class accept audi and
	 * video filename of selected lesson and update in mediaplayers
	 * 
	 * @param String
	 *            audio and video
	 */
	@Override
	public void Play_Music(String audio, String Video) {
		// TODO Auto-generated method stub
		Log.e("Log audi file name", "ddd " + audio);
		Log.e("Log audi file name", "ddd" + Video);
		// Change The Files In Both Players
		button_listen.setEnabled(true);
		button_Setting.setEnabled(true);
		button_Store_cart.setEnabled(true);
		button_Group_Connect.setEnabled(true);
		string_lesson_audio = audio;
		string_lesson_video = Video;
		seek_progess_store(int_audio_background, 0);
		reset_audio_player(audio, false);
		reset_video_player(Video);
		mMediPlayer_Audio.setLooping(false);
		FragmentTransaction fragment_medi_spalsh = getSupportFragmentManager()
				.beginTransaction();
		// Setting THe Fading Animation to layout Fade in And Fade Out
		// Effects To Layout at Transaction
		fragment_medi_spalsh.setCustomAnimations(android.R.anim.fade_in,
				android.R.anim.fade_out, android.R.anim.fade_in,
				android.R.anim.fade_out);
		// Hide And show Fragment
		fragment_medi_spalsh.hide(fragment_medi_list);

		fragment_medi_spalsh.commit();

		showControllers();
		seekbar.setEnabled(true);
		surfaceView_mView.setEnabled(true);

	}

	/**
	 * Reset The AudioPlayer and Set The NEw File From lesson XML
	 * 
	 * @param audio_file
	 *            ,boolean value
	 */
	public void reset_audio_player(String audio_file, boolean value) {

		mMediPlayer_Audio.reset();
		try {
			AssetFileDescriptor audiopath = getAssets().openFd(audio_file);
			mMediPlayer_Audio.setDataSource(audiopath.getFileDescriptor(),
					audiopath.getStartOffset(), audiopath.getLength());
			mMediPlayer_Audio.prepare();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMediPlayer_Audio.setLooping(true);
		mMediPlayer_Audio.setVolume(100, 100);
		mMediPlayer_Audio.start();
		if (value == false) {
			int int_audio_lesson = sharedpref.getInt("seek_lesson", 0);
			int valuess = mMediPlayer_Audio.getDuration();
			if (int_audio_lesson < valuess) {
				imageView_play_pause_icon
						.setImageResource(R.drawable.icon_pause);

			} else {
				imageView_play_pause_icon
						.setImageResource(R.drawable.icon_play);
				mMediPlayer_Audio.pause();
			}
		}
		// mMediPlayer_Audio.start();

		seekbar.setMax(mMediPlayer_Audio.getDuration());
		get_updated_seek_progress(value);
		/*
		 * textView_max_duration.setText("" +
		 * formatTime(mMediPlayer_Audio.getDuration()));
		 */
		startPlayProgressUpdater();
	}

	/**
	 * Reset The BAckground Video MediaPlayer
	 * 
	 * @param video
	 */
	private void reset_video_player(String video) {
		// TODO Auto-generated method stub
		mMediaPlayer.reset();
		try {
			AssetFileDescriptor audiopath = getAssets().openFd(video);
			mMediaPlayer.setDataSource(audiopath.getFileDescriptor(),
					audiopath.getStartOffset(), audiopath.getLength());
			mMediaPlayer.prepare();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMediaPlayer.setLooping(true);
		mMediaPlayer.setVolume(0, 0);
		mMediaPlayer.start();

	}

	// oncompletion of audio stop the player and set icon to play
	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		imageView_play_pause_icon.setImageResource(R.drawable.icon_play);
	}

	/**
	 * Make The Fading Animation to all The controller Views
	 * 
	 * 
	 */
	private void hide_controllers_animation() {
		// TODO Auto-generated method stub
		ObjectAnimator object_one = ObjectAnimator.ofFloat(frameLayout_seekbar,
				"alpha", 1f, 0f);
		ObjectAnimator object_two = ObjectAnimator.ofFloat(
				linearlayout_menus_bar, "alpha", 1f, 0f);
		ObjectAnimator object_three = ObjectAnimator.ofFloat(
				imageView_play_pause_icon, "alpha", 1f, 0f);
		ObjectAnimator object_four = ObjectAnimator.ofFloat(
				imageView_home_icon, "alpha", 1f, 0f);
		// make the animator set
		animatorSet = new AnimatorSet();
		animatorSet.playTogether(object_one, object_two, object_three,
				object_four);
		animatorSet.setDuration(500);

		animatorSet.addListener(new AnimatorListener() {
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub

				// disable all the layouts and button till animation complete
				linearlayout_menus_bar.setEnabled(false);
				imageView_play_pause_icon.setEnabled(false);
				imageView_home_icon.setEnabled(false);
				frameLayout_seekbar.setEnabled(false);
				button_Group_Connect.setEnabled(false);
				button_Setting.setEnabled(false);
				button_Store_cart.setEnabled(false);
				button_listen.setEnabled(false);
			}

			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				// Call To The Next Layout Function

				linearlayout_menus_bar.setVisibility(View.GONE);
				imageView_play_pause_icon.setVisibility(View.GONE);
				frameLayout_seekbar.setVisibility(View.GONE);
				imageView_home_icon.setVisibility(View.GONE);
				// set visibility of Tap menu visible
				textview_tap.setVisibility(View.VISIBLE);
				// show tap_text
				handler_tap_hide.postDelayed(hideTapmenuThread, 3000);

			}

			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub

			}
		});
		animatorSet.start();
	}

	/**
	 * Showing all controllers with animation fade in
	 * 
	 * 
	 */
	private void show_all_controllers() {
		// TODO Auto-generated method stub
		ObjectAnimator object_one = ObjectAnimator.ofFloat(frameLayout_seekbar,
				"alpha", 0f, 1f);
		ObjectAnimator object_two = ObjectAnimator.ofFloat(
				linearlayout_menus_bar, "alpha", 0f, 1f);
		ObjectAnimator object_three = ObjectAnimator.ofFloat(
				imageView_play_pause_icon, "alpha", 0f, 1f);
		ObjectAnimator object_four = ObjectAnimator.ofFloat(
				imageView_home_icon, "alpha", 0f, 1f);

		final AnimatorSet animSetXY = new AnimatorSet();
		animSetXY.playTogether(object_one, object_two, object_three,
				object_four);
		animSetXY.setDuration(500);

		animSetXY.addListener(new AnimatorListener() {
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub

				// disable all the layouts and button till animation complete
				linearlayout_menus_bar.setEnabled(false);
				button_Group_Connect.setEnabled(false);
				button_Setting.setEnabled(false);
				button_Store_cart.setEnabled(false);
				button_listen.setEnabled(false);
				imageView_play_pause_icon.setEnabled(false);
				imageView_home_icon.setEnabled(false);
				frameLayout_seekbar.setEnabled(false);
				linearlayout_menus_bar.setVisibility(View.VISIBLE);
				imageView_play_pause_icon.setVisibility(View.VISIBLE);
				frameLayout_seekbar.setVisibility(View.VISIBLE);
				imageView_home_icon.setVisibility(View.VISIBLE);
				textview_tap.setVisibility(View.GONE);
				handler_hide_show.removeCallbacks(hideControllerThread);
				// call to auto_hide
				hide_menus_seekBars();

			}

			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				// Enable all LAyouts
				linearlayout_menus_bar.setEnabled(true);
				imageView_play_pause_icon.setEnabled(true);
				imageView_home_icon.setEnabled(true);
				frameLayout_seekbar.setEnabled(true);
				button_Group_Connect.setEnabled(true);
				button_Setting.setEnabled(true);
				button_Store_cart.setEnabled(true);
				button_listen.setEnabled(true);

			}

			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
			}
		});
		animSetXY.start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		this.facebookConnector.getFacebook().authorizeCallback(requestCode,
				resultCode, data);
	}

	@Override
	public void facebook_connect() {
		// TODO Auto-generated method stub
		postMessage();
	}

	public void postMessage() {

		if (facebookConnector.getFacebook().isSessionValid()) {
			postMessageInThread();
		} else {
			SessionEvents.AuthListener listener = new SessionEvents.AuthListener() {

				@Override
				public void onAuthSucceed() {
					postMessageInThread();
				}

				@Override
				public void onAuthFail(String error) {

				}
			};
			SessionEvents.addAuthListener(listener);
			facebookConnector.login();
		}
	}

	private void postMessageInThread() {
		Thread t = new Thread() {
			public void run() {

				try {
					facebookConnector.postMessageOnWall(share_message);
					mFacebookHandler.post(mUpdateFacebookNotification);
				} catch (Exception ex) {
					Log.e(TAG, "Error sending msg", ex);
				}
			}
		};
		t.start();
	}

	private void clearCredentials() {
		try {
			facebookConnector.getFacebook().logout(getApplicationContext());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}