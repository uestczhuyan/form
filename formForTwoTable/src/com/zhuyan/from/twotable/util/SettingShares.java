package com.zhuyan.from.twotable.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.SharedPreferences;
import android.os.Environment;

public class SettingShares {

	public static final String ROOT = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/form20150603";

	public final static String NAME = "setting";

	private static final String KEY_PATCH_NUM = "patch";
	private static final String KEY_FILE = "file";
	private static final String KEY_MOHU = "mohu";
	private static final String KEY_TIME = "time";
	private static final String KEY_CODE = "code";

	public static final SimpleDateFormat FORMAT = new SimpleDateFormat(
			"yyyy-dd-MM HH:mm");

	public static final Date getCalender(String time) {
		try {
			if (time == null) {
				return null;
			}
			return FORMAT.parse(time);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public static boolean getOpenMohu(SharedPreferences sharedPreferences) {
		return sharedPreferences.getBoolean(KEY_MOHU, true);
	}

	public static boolean storeMohu(boolean open,
			SharedPreferences sharedPreferences) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		try {
			editor.putBoolean(KEY_MOHU, open);
			return editor.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public static int getPatch(SharedPreferences sharedPreferences) {
		return sharedPreferences.getInt(KEY_PATCH_NUM, 1);
	}

	public static boolean storePatch(int open,
			SharedPreferences sharedPreferences) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		try {
			editor.putInt(KEY_PATCH_NUM, open);
			return editor.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public static String getFileName(SharedPreferences sharedPreferences) {
		return sharedPreferences.getString(KEY_FILE, "file.txt");
	}

	public static boolean storeFileName(String open,
			SharedPreferences sharedPreferences) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		try {
			editor.putString(KEY_FILE, open);
			return editor.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public static String getCode(SharedPreferences sharedPreferences) {
		return sharedPreferences.getString(KEY_CODE, null);
	}

	public static boolean storeCode(String open,
			SharedPreferences sharedPreferences) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		try {
			editor.putString(KEY_CODE, open);
			return editor.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public static boolean clearCode(SharedPreferences sharedPreferences) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		try {
			editor.remove(KEY_CODE);
			return editor.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public static String getTime(SharedPreferences sharedPreferences) {
		return sharedPreferences.getString(KEY_TIME, null);
	}

	public static boolean storeTime(String open,
			SharedPreferences sharedPreferences) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		try {
			editor.putString(KEY_TIME, open);
			return editor.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
}
