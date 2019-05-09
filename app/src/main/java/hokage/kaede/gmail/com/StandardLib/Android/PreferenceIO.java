package hokage.kaede.gmail.com.StandardLib.Android;

import hokage.kaede.gmail.com.StandardLib.Java.ListConverter;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Android環境における設定値の入出力を行う処理を有するクラス。
 */
public class PreferenceIO {
	
	public static void write(Context context, String key, String value) {
		SharedPreferences pf = PreferenceManager.getDefaultSharedPreferences(context);
		pf.edit().putString(key, value).commit();
	}

	public static String readString(Context context, String key, String defValue) {
		SharedPreferences pf = PreferenceManager.getDefaultSharedPreferences(context);
		return pf.getString(key, defValue);
	}

	/**
	 * 文字列のリストをプリファレンスに書き込む。
	 * @param context コンテキスト
	 * @param key キー
	 * @param values 格納する文字列のリスト
	 */
	public static void writeStringList(Context context, String key, ArrayList<String> values) {
		int size = values.size();
		String value = "";
		for(int i=0; i<size; i++) {
			value = value + values.get(i);
			if(i < size-1) {
				value = value + ",";
			}
		}

		SharedPreferences pf = PreferenceManager.getDefaultSharedPreferences(context);
		pf.edit().putString(key, value).commit();
	}
	
	/**
	 * 文字列のリストをプリファレンスから読み込む。
	 * @param context コンテキスト
	 * @param key キー
	 * @return 文字列のリスト
	 */
	public static ArrayList<String> readStringList(Context context, String key) {
		SharedPreferences pf = PreferenceManager.getDefaultSharedPreferences(context);
		String value = pf.getString(key, "");
		
		if(value.equals("")) {
			return null;
		}
		
		String[] str_array = value.split(",");
		return ListConverter.convert(str_array);
	}

	/**
	 * boolean型のデータをプリファレンスに書き込む。
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void write(Context context, String key, boolean value) {
		SharedPreferences pf = PreferenceManager.getDefaultSharedPreferences(context);
		pf.edit().putBoolean(key, value).commit();
	}

	/**
	 * boolean型のデータをプリファレンスから読み込む。
	 * @param context
	 * @param key
	 * @param default_value
	 * @return
	 */
	public static Boolean read(Context context, String key, boolean default_value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean(key, default_value);
	}
}
