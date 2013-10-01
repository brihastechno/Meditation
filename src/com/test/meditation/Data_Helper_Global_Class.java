package com.test.meditation;

import java.util.ArrayList;
import java.util.Map;

import android.app.Application;

public class Data_Helper_Global_Class extends Application {
	// Use For Globally DAta Transfer Through Out THe app
	public static Map<String, ArrayList<String>> meditation_store_list;
	public static Map<String, ArrayList<String>> meditation_medi_list;
	public static ArrayList<String> main_application_attributes;
	// default key attribute
	public static String app_key_to_fetch = "med2";
	public static String video_file;
	public static String audio_file;

	public static Map<String, ArrayList<String>> getMeditation_store_list() {
		return meditation_store_list;
	}

	public static void setMeditation_store_list(
			Map<String, ArrayList<String>> meditation_store_list) {
		Data_Helper_Global_Class.meditation_store_list = meditation_store_list;
	}

	public static String getApp_key_to_fetch() {
		return app_key_to_fetch;
	}

	public static String getVideo_file() {
		return video_file;
	}

	public static void setVideo_file(String video_file) {
		Data_Helper_Global_Class.video_file = video_file;
	}

	public static Map<String, ArrayList<String>> getMeditation_medi_list() {
		return meditation_medi_list;
	}

	public static void setMeditation_medi_list(
			Map<String, ArrayList<String>> meditation_medi_list) {
		Data_Helper_Global_Class.meditation_medi_list = meditation_medi_list;
	}

	public static ArrayList<String> getMain_application_attributes() {
		return main_application_attributes;
	}

	public static void setMain_application_attributes(
			ArrayList<String> main_application_attributes) {
		Data_Helper_Global_Class.main_application_attributes = main_application_attributes;
	}

	public static String getAudio_file() {
		return audio_file;
	}

	public static void setAudio_file(String audio_file) {
		Data_Helper_Global_Class.audio_file = audio_file;
	}

}
