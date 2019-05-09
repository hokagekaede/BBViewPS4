package hokage.kaede.gmail.com.StandardLib.Android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.preference.PreferenceManager;

/**
 * Androidアプリで共通使用可能な設定値を管理するクラス。
 */
public class SettingManager {

	//----------------------------------------------------------
	// フォントサイズのデータ管理
	//----------------------------------------------------------
	
	public static final String TEXT_SIZE_KEY = "FONTSIZE_KEY";
	
	private static final double TEXT_SIZE_BASE = 15;
	private static final int TEXT_SIZE_DEFAULT_INDEX = 3;
	private static final double[] TEXT_SIZE_LIST = { 1.6, 1.3, 1.0, 0.8, 0.6 };

	public static final String[] STR_TEXT_SIZE_LIST = { "特大", "大", "中", "小", "極小" };
	public static final String STR_TEXT_SIZE_DEFAULT = STR_TEXT_SIZE_LIST[TEXT_SIZE_DEFAULT_INDEX];
	
	public static final int FLAG_TEXTSIZE_NORMAL = 0;
	public static final int FLAG_TEXTSIZE_LARGE  = 1;
	public static final int FLAG_TEXTSIZE_SMALL  = 2;
	
	/**
	 * テキストのサイズを取得する。
	 * @param context 対象の画面
	 * @return フォントサイズ
	 */
	public static float getTextSize(Context context) {
		return getTextSize(context, FLAG_TEXTSIZE_NORMAL);
	}
	
	/**
	 * テキストのサイズを取得する。
	 * @param context 対象の画面
	 * @param flag 文字の大きさ情報(LARGE/NORMAL/SMALL)
	 * @return フォントサイズ
	 */
	public static float getTextSize(Context context, int flag) {
		double ret = TEXT_SIZE_LIST[TEXT_SIZE_DEFAULT_INDEX];
		int size = STR_TEXT_SIZE_LIST.length;

		// 現在のフォントサイズの設定値を取得する
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String text_size_str = preferences.getString(TEXT_SIZE_KEY, STR_TEXT_SIZE_DEFAULT);
		
		for(int i=0; i<size; i++) {
			if(text_size_str.equals(STR_TEXT_SIZE_LIST[i])) {
				ret = TEXT_SIZE_LIST[i];
				break;
			}
		}
		
		// フラグに応じて拡大縮小を行う
		if(flag == FLAG_TEXTSIZE_NORMAL) {
			ret = TEXT_SIZE_BASE * ret;
		}
		else if(flag == FLAG_TEXTSIZE_LARGE) {
			ret = TEXT_SIZE_BASE * ret * 1.3;
		}
		else if(flag == FLAG_TEXTSIZE_SMALL) {
			ret = TEXT_SIZE_BASE * ret * 0.7;
		}
		else {
			ret = TEXT_SIZE_BASE * ret;
		}
		
		return (float)(ret);
	}
	
	//----------------------------------------------------------
	// テーマの設定管理
	//----------------------------------------------------------
	
	public static final String THEME_KEY = "THEME_KEY";
	public static final String[] THEME_LIST = { "Default", "Light", "Black" };
	public static final String THEME_DEFAULT = "Default";
	public static final int THEME_DEFAULT_ID = -1;
	public static int sThemeID = THEME_DEFAULT_ID;
	
	protected static int loadThemeID(Context context) {
		int ret = THEME_DEFAULT_ID;
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String theme_value = preferences.getString(THEME_KEY, THEME_DEFAULT);
		
		if(theme_value.equals("Light")) {
			ret = android.R.style.Theme_Holo_Light;
		}
		else if(theme_value.equals("Black")) {
			ret = android.R.style.Theme_Holo;
		}
		
		return ret;
	}
	
	/**
	 * 白色(ホワイト)の設定値を取得する。
	 * @return 文字色
	 */
	public static int getColorWhite() {
		if(sThemeID == android.R.style.Theme_Holo) {
			return Color.WHITE;
		}
		else if(sThemeID == android.R.style.Theme_Holo_Light) {
			return Color.BLACK;
		}
		
		return Color.WHITE;
	}
	
	/**
	 * 灰色の色の設定値を取得する。
	 * @return 文字色
	 */
	public static int getColorGray() {
		if(sThemeID == android.R.style.Theme_Holo) {
			return Color.rgb(60, 60, 60);
		}
		else if(sThemeID == android.R.style.Theme_Holo_Light) {
			return Color.rgb(190, 190, 190);
		}
		
		return Color.rgb(60, 60, 60);
	}
	
	/**
	 * 黒色(ブラック)の設定値を取得する。
	 * @return 文字色
	 */
	public static int getColorBlack() {
		if(sThemeID == android.R.style.Theme_Holo) {
			return Color.BLACK;
		}
		else if(sThemeID == android.R.style.Theme_Holo_Light) {
			return Color.WHITE;
		}
		
		return Color.BLACK;
	}
	
	/**
	 * 赤色(マゼンタ)の設定値を取得する。
	 * @return
	 */
	public static int getColorMazenta() {
		if(sThemeID == android.R.style.Theme_Holo) {
			return Color.MAGENTA;
		}
		else if(sThemeID == android.R.style.Theme_Holo_Light) {
			return Color.RED;
		}

		return Color.MAGENTA;
	}
	
	/**
	 * 青色(シアン)の設定値を取得する。
	 * @return 文字色
	 */
	public static int getColorCyan() {
		if(sThemeID == android.R.style.Theme_Holo) {
			return Color.CYAN;
		}
		else if(sThemeID == android.R.style.Theme_Holo_Light) {
			return Color.BLUE;
		}
		
		return Color.CYAN;
	}

	/**
	 * 青色(ブルー)の設定値を取得する。
	 * @return 文字色
	 */
	public static int getColorBlue() {
		if(sThemeID == android.R.style.Theme_Holo) {
			return Color.rgb(0x00, 0x00, 0xAA);
		}
		else if(sThemeID == android.R.style.Theme_Holo_Light) {
			return Color.rgb(0x00, 0xCC, 0xFF);
		}

		return Color.rgb(0x00, 0x00, 0xAA);
	}
	
	/**
	 * 黄色(イエロー)の設定値を取得する。
	 * @return 文字色
	 */
	public static int getColorYellow() {
		if(sThemeID == android.R.style.Theme_Holo) {
			return Color.YELLOW;
		}
		else if(sThemeID == android.R.style.Theme_Holo_Light) {
			return Color.rgb(0xFF, 0x99, 0x00);
		}

		return Color.YELLOW;
	}
	
	/**
	 * 緑色(グリーン)の設定値を取得する。
	 * @return 文字色
	 */
	public static int getColorGreen() {
		if(sThemeID == android.R.style.Theme_Holo) {
			return Color.GREEN;
		}
		else if(sThemeID == android.R.style.Theme_Holo_Light) {
			return Color.rgb(0x00, 0xAA, 0x00);
		}

		return Color.GREEN;
	}
	
	public static int getColorDarkGreen() {
		if(sThemeID == android.R.style.Theme_Holo) {
			return Color.rgb(0, 0xAA, 0x00);
		}
		else if(sThemeID == android.R.style.Theme_Holo_Light) {
			return Color.rgb(0, 0xFF, 0xCC);
		}

		return Color.rgb(0x00, 0xAA, 0x00);
	}

	private static final String COLOR_TAG_MAGENTA_DARK  = "<font color=\"Magenta\">";
	private static final String COLOR_TAG_MAGENTA_LIGHT = "<font color=\"Red\">";
	private static final String COLOR_TAG_CYAN_DARK     = "<font color=\"Cyan\">";
	private static final String COLOR_TAG_CYAN_LIGHT    = "<font color=\"Blue\">";
	
	/**
	 * Html.fromHtml()関数向けの赤色文字色のHTMLコードを取得する。
	 * @return 
	 */
	public static String getCodeMagenta() {
		if(sThemeID == android.R.style.Theme_Holo) {
			return COLOR_TAG_MAGENTA_DARK;
		}
		else if(sThemeID == android.R.style.Theme_Holo_Light) {
			return COLOR_TAG_MAGENTA_LIGHT;
		}

		return COLOR_TAG_MAGENTA_DARK;
	}

	/**
	 * Html.fromHtml()関数向けの青色文字色のHTMLコードを取得する。
	 * @return 
	 */
	public static String getCodeCyan() {
		if(sThemeID == android.R.style.Theme_Holo) {
			return COLOR_TAG_CYAN_DARK;
		}
		else if(sThemeID == android.R.style.Theme_Holo_Light) {
			return COLOR_TAG_CYAN_LIGHT;
		}

		return COLOR_TAG_CYAN_DARK;
	}
	
	
	//----------------------------------------------------------
	// バージョン情報管理
	//----------------------------------------------------------

	/**
	 * バージョン情報を管理する設定キー
	 */
	private static final String SETTING_IS_LAST_VERSIONCODE = "SETTING_IS_LAST_VERSIONCODE";
	
	/**
	 * 初回起動フラグを取得する。
	 * @return 初回起動の場合はtrueを返し、初回起動でない場合はfalseを返す。
	 */
	public static boolean isFirstFlag(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		int last_code = sp.getInt(SETTING_IS_LAST_VERSIONCODE, 0);
		
		if(last_code == 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * アップデートフラグを取得する。
	 * @param context 
	 * @return アップデート後の場合はtrueを返し、そうでない場合はfalseを返す。
	 */
	public static boolean isUpdateFlag(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		int last_code = sp.getInt(SETTING_IS_LAST_VERSIONCODE, 0);
		int recent_code = getVersionCode(context);
		
		if(last_code == recent_code) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 初回起動フラグ（現在のバージョンコード）を設定する。
	 */
	public static void setVersionCode(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().putInt(SETTING_IS_LAST_VERSIONCODE, getVersionCode(context)).commit();
	}
	
	/**
	 * バージョンコードを取得する。
	 * @return バージョンコード
	 */
	public static int getVersionCode(Context context) {
		int ret;
		
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
			ret = pi.versionCode;
			
		} catch (NameNotFoundException e) {
			ret = -1;
		}
		
		return ret;
	}
}
