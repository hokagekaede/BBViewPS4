package hokage.kaede.gmail.com.BBViewLib.Java;

import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;

/**
 * BBView専用の設定データを管理するクラス。
 */
public class BBViewSetting extends SettingManager {
	
	// 次バージョンのデータ設定
	//public static boolean IS_NEXT_VERSION_ON = false;
	//public static String NEXT_VERSION_TITLE = "X Zero Plus";

	/**
	 * 移動速度の表示単位。trueの場合はkm/hで表示。falseの場合はm/secで表示。
	 */
	public static boolean IS_KM_PER_HOUR = false;
	public static final String SETTING_KM_PER_HOUR = "SETTING_KM_PER_HOUR";

	/**
	 * ホバー脚部時の歩行速度/ダッシュ速度の表示形式設定。
	 * trueの場合はアルファベットを二脚基準で表示し、falseの場合はホバー基準で表示する。
	 */
	public static boolean IS_HOVER_TO_LEGS = false;
	public static final String SETTING_HOVER_TO_LEGS = "SETTING_HOVER_TO_LEGS";

	/**
	 * 装甲値の表示形式設定。trueの場合はダメージ係数で表示。falseの場合はBB.NETなどの公式準拠の値で表示。
	 */
	public static boolean IS_ARMOR_RATE = true;
	public static final String SETTING_ARMOR_RATE = "SETTING_ARMOR_RATE";
	
	/**
	 * OH武器の戦術火力の計算方式設定。trueの場合はOH基準で計算し、falseの場合はリロード基準で計算する。
	 */
	public static boolean IS_BATTLE_POWER_OH = true;
	public static final String SETTING_BATTLE_POWER_OH = "SETTING_BATTLE_POWER_OH";
	
	/**
	 * 前回の編集データを読み込むかどうか。trueの場合は前回データを読み込み、falseの場合は初期アセンを読み込む。
	 */
	public static boolean IS_LOADING_LASTDATA = true;
	public static final String SETTING_LOADING_LASTDATA = "SETTING_LOADING_LASTDATA";

	/**
	 * アセン画面の2列表示設定。trueで表示する。
	 */
	public static boolean IS_SHOW_COLUMN2 = true;
	public static final String SETTING_SHOW_COLUMN2 = "SETTING_SHOW_COLUMN2";
	
	/**
	 * アセン画面の性能表示設定。trueで表示する。
	 */
	public static boolean IS_SHOW_SPECLABEL = true;
	public static final String SETTING_SHOW_SPECLABEL = "SETTING_SHOW_SPECLABEL";
	
	/**
	 * アセン画面の種類表示設定。trueで表示する。
	 */
	public static boolean IS_SHOW_TYPELABEL = true;
	public static final String SETTING_SHOW_TYPELABEL = "SETTING_SHOW_TYPELABEL";
	
	/**
	 * パーツ武器選択画面のリストボタン表示設定。trueで表示する。
	 */
	public static boolean IS_SHOW_LISTBUTTON = true;
	public static final String SETTING_SHOW_LISTBUTTON = "SETTING_SHOW_LISTBUTTON";
	
	/**
	 * パーツ武器選択画面のリストボタン表示設定。trueでTextView配置、falseでButton配置
	 */
	public static boolean IS_LISTBUTTON_TYPETEXT = true;
	public static final String SETTING_LISTBUTTON_TYPETEXT = "SETTING_LISTBUTTON_TYPETEXT";

	/**
	 * パーツ武器選択画面のリストボタンにフルセットボタンを表示するかどうか。trueでTextView配置、falseでButton配置
	 */
	public static double LISTBUTTON_TEXTSIZE = 1.0;
	public static double[] LISTBUTTON_TEXTSIZE_VALUES = {1.6, 1.4, 1.2, 1.0, 0.8}; 
	public static String[] LISTBUTTON_TEXTSIZE_CAPTIONS = { "特大", "大", "中", "小", "極小" };
	public static String LISTBUTTON_TEXTSIZE_DEFAULT = LISTBUTTON_TEXTSIZE_CAPTIONS[3];
	public static final String SETTING_LISTBUTTON_TEXTSIZE = "SETTING_LISTBUTTON_TEXTSIZE";
	
	/**
	 * パーツ武器選択画面のリストボタンに詳細ボタンを表示するかどうか。trueでTextView配置、falseでButton配置
	 */
	public static boolean IS_LISTBUTTON_SHOWINFO = true;
	public static final String SETTING_LISTBUTTON_SHOWINFO = "SETTING_LISTBUTTON_SHOWINFO";
	
	/**
	 * パーツ武器選択画面のリストボタンに比較ボタンを表示するかどうか。trueでTextView配置、falseでButton配置
	 */
	public static boolean IS_LISTBUTTON_SHOWCMP = true;
	public static final String SETTING_LISTBUTTON_SHOWCMP = "SETTING_LISTBUTTON_SHOWCMP";
	
	/**
	 * パーツ武器選択画面のリストボタンにフルセットボタンを表示するかどうか。trueでTextView配置、falseでButton配置
	 */
	public static boolean IS_LISTBUTTON_SHOWFULLSET = true;
	public static final String SETTING_LISTBUTTON_SHOWFULLSET = "SETTING_LISTBUTTON_SHOWFULLSET";

	/**
	 * パーツ選択画面のカテゴリ表示(初期値)を有効にするかどうか。trueでカテゴリ表示、falseで通常表示。
	 */
	public static boolean IS_SHOW_CATEGORYPARTS_INIT = false;
	public static final String SETTING_SHOW_CATEGORYPARTS_INIT = "SETTING_SHOW_CATEGORYPARTS_INIT";

	/**
	 * 所持品表示設定。Trueで購入済み(開発済み)のみを表示する。
	 */
	public static boolean IS_SHOW_HAVING = false;
	public static final String SETTING_SHOW_HAVING_ONLY = "SETTING_SHOW_HAVING_ONLY";
	
	/**
	 * 表示項目選択機能の状態維持設定。Trueで状態維持、Falseで状態破棄。
	 */
	public static boolean IS_MEMORY_SHOWSPEC = true;
	public static final String SETTING_MEMORY_SHOWSPEC = "SETTING_MEMORY_SHOWITEM";
	
	/**
	 * ソート機能の状態維持設定。Trueで状態維持、Falseで状態破棄。
	 */
	public static boolean IS_MEMORY_SORT = false;
	public static final String SETTING_MEMORY_SORT = "SETTING_MEMORY_SORT";
	
	/**
	 * フィルタ機能の状態維持設定。Trueで状態維持、Falseで状態破棄。
	 */
	public static boolean IS_MEMORY_FILTER = false;
	public static final String SETTING_MEMORY_FILTER = "SETTING_MEMORY_FILTER";
}
