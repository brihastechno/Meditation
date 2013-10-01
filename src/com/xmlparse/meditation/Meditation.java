package com.xmlparse.meditation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Meditation {
	// Storing The data In HAsHmap
	// list of All meditation
	public Map<String, ArrayList<String>> meditation_app_list = new HashMap<String, ArrayList<String>>();
	// list of app in Store according to A meditation app
	public Map<String, ArrayList<String>> meditation_store_list = new HashMap<String, ArrayList<String>>();
	// list of app in Store according to A meditation app
	public Map<String, ArrayList<String>> meditation_medi_list = new HashMap<String, ArrayList<String>>();

	// key terms of XML
	public static final String MEDITATION = "meditation";
	public static final String STORE_APP = "store_app";
	public static final String Med = "med";
	public static final String ID = "id";
	public static final String APP_NAME = "App_name";
	public static final String APP_TITLE = "App_title";
	public static final String APP_URL = "App_Url";
	public static final String APP_AUTHOR = "App_author";
	public static final String APP_THUMBNAIL = "App_thumbnail";

	// Keys Of Meditaion
	public static final String MEDI_NAME = "name";
	public static final String MEDI_VIDEO = "video";
	public static final String MEDI_AUDIO = "audio";
	public static final String MEDI_LOGO = "logo";
}
