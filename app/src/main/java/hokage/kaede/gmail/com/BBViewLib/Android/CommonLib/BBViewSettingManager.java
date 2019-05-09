package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.StandardLib.Android.PreferenceIO;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * BBView専用の設定データを管理するクラス。
 */
public class BBViewSettingManager extends SettingManager {
	
	/**
	 * 全ての設定値を読み込む。
	 * @param context コンテキスト
	 */
	public static void loadSettings(Context context) {
		BBViewSetting.IS_KM_PER_HOUR = isKmPerHour(context);
		BBViewSetting.IS_HOVER_TO_LEGS = isHoverToLegs(context);
		BBViewSetting.IS_ARMOR_RATE = isArmorRate(context);
		BBViewSetting.IS_BATTLE_POWER_OH = isBattlePowerOH(context);
		BBViewSetting.IS_LOADING_LASTDATA = isLoadingLastData(context);
		
		BBViewSetting.IS_SHOW_COLUMN2 = isShowCOLUMN2(context);
		BBViewSetting.IS_SHOW_SPECLABEL = isShowSpecLabel(context);
		BBViewSetting.IS_SHOW_TYPELABEL = isShowTypeLabel(context);
		
		BBViewSetting.IS_SHOW_LISTBUTTON = isShowButton(context);
		BBViewSetting.IS_LISTBUTTON_TYPETEXT = isListButtonTypeText(context);
		BBViewSetting.IS_LISTBUTTON_SHOWINFO = isListButtonShowInfo(context);
		BBViewSetting.IS_LISTBUTTON_SHOWCMP = isListButtonShowCmp(context);
		BBViewSetting.IS_LISTBUTTON_SHOWFULLSET = isListButtonShowFullSet(context);
		BBViewSetting.LISTBUTTON_TEXTSIZE = loadListButtonTextSize(context);
		BBViewSetting.IS_SHOW_CATEGORYPARTS_INIT = isShowCategoryPartsInit(context);
		BBViewSetting.IS_SHOW_HAVING = isHavingOnly(context);
		BBViewSetting.IS_MEMORY_SHOWSPEC = isMemoryShowSpec(context);
		BBViewSetting.IS_MEMORY_SORT = isMemorySort(context);
		BBViewSetting.IS_MEMORY_FILTER = isMemoryFilter(context);
		
		sThemeID = loadThemeID(context);
	}
	
	private static boolean isKmPerHour(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(BBViewSetting.SETTING_KM_PER_HOUR, false);
	}

	private static boolean isHoverToLegs(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(BBViewSetting.SETTING_HOVER_TO_LEGS, false);
	}
	
	private static boolean isArmorRate(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(BBViewSetting.SETTING_ARMOR_RATE, true);
	}

	private static boolean isBattlePowerOH(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(BBViewSetting.SETTING_BATTLE_POWER_OH, true);
	}

	private static boolean isLoadingLastData(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(BBViewSetting.SETTING_LOADING_LASTDATA, true);
	}
	private static boolean isShowCOLUMN2(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(BBViewSetting.SETTING_SHOW_COLUMN2, true);
	}

	private static boolean isShowSpecLabel(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(BBViewSetting.SETTING_SHOW_SPECLABEL, true);
	}

	private static boolean isShowTypeLabel(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(BBViewSetting.SETTING_SHOW_TYPELABEL, true);
	}

	private static boolean isShowButton(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(BBViewSetting.SETTING_SHOW_LISTBUTTON, true);
	}

	private static boolean isListButtonTypeText(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(BBViewSetting.SETTING_LISTBUTTON_TYPETEXT, true);
	}

	private static double loadListButtonTextSize(Context context) {
		double ret = BBViewSetting.LISTBUTTON_TEXTSIZE;
		int size = BBViewSetting.LISTBUTTON_TEXTSIZE_CAPTIONS.length;
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String data = preferences.getString(BBViewSetting.SETTING_LISTBUTTON_TEXTSIZE, BBViewSetting.LISTBUTTON_TEXTSIZE_DEFAULT);
		
		for(int i=0; i<size; i++) {
			if(data.equals(BBViewSetting.LISTBUTTON_TEXTSIZE_CAPTIONS[i])) {
				ret = BBViewSetting.LISTBUTTON_TEXTSIZE_VALUES[i];
				break;
			}
		}
		
		return ret;
	}

	private static boolean isListButtonShowInfo(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(BBViewSetting.SETTING_LISTBUTTON_SHOWINFO, true);
	}

	private static boolean isListButtonShowCmp(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(BBViewSetting.SETTING_LISTBUTTON_SHOWCMP, true);
	}

	private static boolean isListButtonShowFullSet(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(BBViewSetting.SETTING_LISTBUTTON_SHOWFULLSET, true);
	}

	private static boolean isShowCategoryPartsInit(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(BBViewSetting.SETTING_SHOW_CATEGORYPARTS_INIT, false);
	}
	
	private static boolean isHavingOnly(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(BBViewSetting.SETTING_SHOW_HAVING_ONLY, false);
	}

	private static boolean isMemoryShowSpec(Context context) {
		return PreferenceIO.read(context, BBViewSetting.SETTING_MEMORY_SHOWSPEC, true);
	}

	private static boolean isMemorySort(Context context) {
		return PreferenceIO.read(context, BBViewSetting.SETTING_MEMORY_SORT, false);
	}
	
	private static boolean isMemoryFilter(Context context) {
		return PreferenceIO.read(context, BBViewSetting.SETTING_MEMORY_FILTER, false);
	}
}
