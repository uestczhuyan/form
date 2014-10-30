package com.zhuyan.form;

import android.content.SharedPreferences;
import android.os.Environment;

public class SettingShares {
	
	public static final String ROOT = Environment.
			getExternalStorageDirectory()
			.getAbsolutePath() + "/form";
	
	public final static String NAME = "setting";
	
	private static final String KEY_PATCH_NUM = "patch";
	private static final String KEY_FILE = "file";
	
	public static int getPatch(SharedPreferences sharedPreferences){
		return sharedPreferences.getInt(KEY_PATCH_NUM, 2);
	}
	
	public static boolean storePatch(int open,SharedPreferences sharedPreferences){
		 SharedPreferences.Editor editor = sharedPreferences.edit();
			try {
				editor.putInt(KEY_PATCH_NUM, open);
				return editor.commit();
			} catch (Exception e) {
				// TODO: handle exception
			}
			return false;
	}
	
	public static String getFileName(SharedPreferences sharedPreferences){
		return sharedPreferences.getString(KEY_FILE, "file.txt");
	}
	
	public static boolean storeFileName(String open,SharedPreferences sharedPreferences){
		 SharedPreferences.Editor editor = sharedPreferences.edit();
			try {
				editor.putString(KEY_FILE, open);
				return editor.commit();
			} catch (Exception e) {
				// TODO: handle exception
			}
			return false;
	}
}
