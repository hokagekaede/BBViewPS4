package hokage.kaede.gmail.com.BBViewPS4.Main;

import hokage.kaede.gmail.com.BBViewLib.Java.CustomFileManager;
import hokage.kaede.gmail.com.StandardLib.Java.FileKeyValueStore;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BBViewSettingManager;
import hokage.kaede.gmail.com.StandardLib.Java.FileIO;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 設定画面を表示するクラス。
 */
public class SettingActivity extends PreferenceActivity implements OnClickListener {
	private static final String NEWLINE = System.getProperty("line.separator");

	private FileKeyValueStore mSaveData;

	// ダイアログの種類
	private int dialog_type;
	private static final int CMD_IMPORT  = 0;
	private static final int CMD_EXPORT  = 1;

	// メニュー番号
	private static final int MENU_IMPORT = 1;
	private static final int MENU_EXPORT = 2;

	/**
	 * 設定画面起動時の処理を行う
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// テーマを設定する
		/*
		int res_id = BBViewSettingManager.sThemeID;
		
		if(res_id != BBViewSettingManager.THEME_DEFAULT_ID) {
			setTheme(res_id);
		}
		*/
		
		loadSaveFileList();
		createScreen();
	}
	
	/**
	 * セーブデータ管理ファイルを読み込む
	 */
	private void loadSaveFileList() {
		String dir = getFilesDir().toString();
		mSaveData = new FileKeyValueStore(dir, FileKeyValueStore.FILELIST_DAT);
		mSaveData.load();
	}
	
	/**
	 * 設定画面を生成する
	 */
	private void createScreen() {
		PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(this);
		
		// 全体設定
		PreferenceCategory all_category = new PreferenceCategory(this);
		all_category.setTitle("全体設定");
		screen.addPreference(all_category);

		ListPreference text_size_type = new ListPreference(this);
		text_size_type.setTitle("テキストサイズ");
		text_size_type.setSummary("文字の大きさを変更する");
		text_size_type.setKey(BBViewSetting.TEXT_SIZE_KEY);
		text_size_type.setEntries(BBViewSetting.STR_TEXT_SIZE_LIST);
		text_size_type.setEntryValues(BBViewSetting.STR_TEXT_SIZE_LIST);	
		text_size_type.setDefaultValue(BBViewSetting.STR_TEXT_SIZE_DEFAULT);
		screen.addPreference(text_size_type);
		
		ListPreference theme_type = new ListPreference(this);
		theme_type.setTitle("テーマ");
		theme_type.setSummary("画面の背景色などを変更する");
		theme_type.setKey(BBViewSetting.THEME_KEY);
		theme_type.setEntries(BBViewSetting.THEME_LIST);
		theme_type.setEntryValues(BBViewSetting.THEME_LIST);	
		theme_type.setDefaultValue(BBViewSetting.THEME_DEFAULT);
		screen.addPreference(theme_type);
		
		CheckBoxPreference speed_view_type = new CheckBoxPreference(this);
		speed_view_type.setTitle("移動速度のkm/h表示");
		speed_view_type.setSummary("ONの場合はkm/h表示、OFFの場合はm/s表示");
		speed_view_type.setKey(BBViewSetting.SETTING_KM_PER_HOUR);
		speed_view_type.setChecked(BBViewSetting.IS_KM_PER_HOUR);
		screen.addPreference(speed_view_type);

		CheckBoxPreference armor_view_type = new CheckBoxPreference(this);
		armor_view_type.setTitle("装甲のダメージ係数表示");
		armor_view_type.setSummary("ONの場合はダメージ係数で表示、OFFの場合は公式準拠値で表示");
		armor_view_type.setKey(BBViewSetting.SETTING_ARMOR_RATE);
		armor_view_type.setChecked(BBViewSetting.IS_ARMOR_RATE);
		screen.addPreference(armor_view_type);

		CheckBoxPreference battle_power_oh_view_type = new CheckBoxPreference(this);
		battle_power_oh_view_type.setTitle("OH武器の戦術火力計算");
		battle_power_oh_view_type.setSummary("ONの場合はOH基準で計算、OFFの場合はリロード基準で計算");
		battle_power_oh_view_type.setKey(BBViewSetting.SETTING_BATTLE_POWER_OH);
		battle_power_oh_view_type.setChecked(BBViewSetting.IS_BATTLE_POWER_OH);
		screen.addPreference(battle_power_oh_view_type);

		CheckBoxPreference loading_lastdata_preference = new CheckBoxPreference(this);
		loading_lastdata_preference.setTitle("起動時に前回データを読み込む");
		loading_lastdata_preference.setSummary("ONの場合は前回データを読み込み、OFFの場合は初期アセンを読み込む");
		loading_lastdata_preference.setKey(BBViewSetting.SETTING_LOADING_LASTDATA);
		loading_lastdata_preference.setChecked(BBViewSetting.IS_LOADING_LASTDATA);
		screen.addPreference(loading_lastdata_preference);
		
		// アセン画面
		PreferenceCategory custom_category = new PreferenceCategory(this);
		custom_category.setTitle("アセン画面");
		screen.addPreference(custom_category);

		CheckBoxPreference show_column2 = new CheckBoxPreference(this);
		show_column2.setTitle("2列表示");
		show_column2.setSummary("武器を2列表示にする");
		show_column2.setKey(BBViewSetting.SETTING_SHOW_COLUMN2);
		show_column2.setChecked(BBViewSetting.IS_SHOW_COLUMN2);
		screen.addPreference(show_column2);
		
		CheckBoxPreference show_spec_label = new CheckBoxPreference(this);
		show_spec_label.setTitle("性能表示");
		show_spec_label.setSummary("パーツの下に性能を簡略表示する");
		show_spec_label.setKey(BBViewSetting.SETTING_SHOW_SPECLABEL);
		show_spec_label.setChecked(BBViewSetting.IS_SHOW_SPECLABEL);
		screen.addPreference(show_spec_label);

		CheckBoxPreference show_type_label = new CheckBoxPreference(this);
		show_type_label.setTitle("武器種類表示");
		show_type_label.setSummary("武器の下に兵装名と武器の種類を表示する");
		show_type_label.setKey(BBViewSetting.SETTING_SHOW_TYPELABEL);
		show_type_label.setChecked(BBViewSetting.IS_SHOW_TYPELABEL);
		screen.addPreference(show_type_label);

		// 性能画面
		PreferenceCategory spec_category = new PreferenceCategory(this);
		spec_category.setTitle("性能画面");
		screen.addPreference(spec_category);

		CheckBoxPreference hover_view_type = new CheckBoxPreference(this);
		hover_view_type.setTitle("ホバー脚部の二脚基準表示");
		hover_view_type.setSummary("ONの場合は二脚基準、OFFの場合はホバー基準");
		hover_view_type.setKey(BBViewSetting.SETTING_HOVER_TO_LEGS);
		hover_view_type.setChecked(BBViewSetting.IS_HOVER_TO_LEGS);
		screen.addPreference(hover_view_type);

		// パーツ/武器選択画面
		PreferenceCategory select_category = new PreferenceCategory(this);
		select_category.setTitle("パーツ/武器選択画面");
		screen.addPreference(select_category);

		/*
		CheckBoxPreference show_having_item_only = new CheckBoxPreference(this);
		show_having_item_only.setTitle("パーツ武器表示");
		show_having_item_only.setSummary("所持済みのパーツ/武器のみ表示する");
		show_having_item_only.setKey(BBViewSetting.SETTING_SHOW_HAVING_ONLY);
		show_having_item_only.setChecked(BBViewSetting.IS_SHOW_HAVING);
		screen.addPreference(show_having_item_only);
		*/

		CheckBoxPreference show_listbutton = new CheckBoxPreference(this);
		show_listbutton.setTitle("操作ボタンの表示");
		show_listbutton.setSummary("左側に操作ボタンを表示する");
		show_listbutton.setKey(BBViewSetting.SETTING_SHOW_LISTBUTTON);
		show_listbutton.setChecked(BBViewSetting.IS_SHOW_LISTBUTTON);
		screen.addPreference(show_listbutton);

		CheckBoxPreference btn_listbutton_typetext = new CheckBoxPreference(this);
		btn_listbutton_typetext.setTitle("操作ボタンの表示形式");
		btn_listbutton_typetext.setSummary("操作ボタンの表示をテキストにする");
		btn_listbutton_typetext.setKey(BBViewSetting.SETTING_LISTBUTTON_TYPETEXT);
		btn_listbutton_typetext.setChecked(BBViewSetting.IS_LISTBUTTON_TYPETEXT);
		screen.addPreference(btn_listbutton_typetext);

		ListPreference listbutton_size = new ListPreference(this);
		listbutton_size.setTitle("操作ボタンのサイズ");
		listbutton_size.setSummary("操作ボタン(テキスト)のサイズを変更する");
		listbutton_size.setKey(BBViewSetting.SETTING_LISTBUTTON_TEXTSIZE);
		listbutton_size.setEntries(BBViewSetting.LISTBUTTON_TEXTSIZE_CAPTIONS);
		listbutton_size.setEntryValues(BBViewSetting.LISTBUTTON_TEXTSIZE_CAPTIONS);	
		listbutton_size.setDefaultValue(BBViewSetting.LISTBUTTON_TEXTSIZE_DEFAULT);
		screen.addPreference(listbutton_size);
		
		CheckBoxPreference btn_listbutton_showinfo = new CheckBoxPreference(this);
		btn_listbutton_showinfo.setTitle("操作ボタン表示(詳細)");
		btn_listbutton_showinfo.setSummary("操作ボタン(詳細)を表示する");
		btn_listbutton_showinfo.setKey(BBViewSetting.SETTING_LISTBUTTON_SHOWINFO);
		btn_listbutton_showinfo.setChecked(BBViewSetting.IS_LISTBUTTON_SHOWINFO);
		screen.addPreference(btn_listbutton_showinfo);

		CheckBoxPreference btn_listbutton_showcmp = new CheckBoxPreference(this);
		btn_listbutton_showcmp.setTitle("操作ボタン表示(比較)");
		btn_listbutton_showcmp.setSummary("操作ボタン(比較)を表示する");
		btn_listbutton_showcmp.setKey(BBViewSetting.SETTING_LISTBUTTON_SHOWCMP);
		btn_listbutton_showcmp.setChecked(BBViewSetting.IS_LISTBUTTON_SHOWCMP);
		screen.addPreference(btn_listbutton_showcmp);

		CheckBoxPreference btn_listbutton_showfullset = new CheckBoxPreference(this);
		btn_listbutton_showfullset.setTitle("操作ボタン表示(フルセット)");
		btn_listbutton_showfullset.setSummary("操作ボタン(フルセット)を表示する");
		btn_listbutton_showfullset.setKey(BBViewSetting.SETTING_LISTBUTTON_SHOWFULLSET);
		btn_listbutton_showfullset.setChecked(BBViewSetting.IS_LISTBUTTON_SHOWFULLSET);
		screen.addPreference(btn_listbutton_showfullset);

		CheckBoxPreference btn_show_category_partsinit = new CheckBoxPreference(this);
		btn_show_category_partsinit.setTitle("カテゴリ表示");
		btn_show_category_partsinit.setSummary("パーツ武器選択画面の初期状態をカテゴリ表示にする");
		btn_show_category_partsinit.setKey(BBViewSetting.SETTING_SHOW_CATEGORYPARTS_INIT);
		btn_show_category_partsinit.setChecked(BBViewSetting.IS_SHOW_CATEGORYPARTS_INIT);
		screen.addPreference(btn_show_category_partsinit);
		
		CheckBoxPreference btn_memory_showitem = new CheckBoxPreference(this);
		btn_memory_showitem.setTitle("表示項目選択状態");
		btn_memory_showitem.setSummary("表示項目選択状態を維持する");
		btn_memory_showitem.setKey(BBViewSetting.SETTING_MEMORY_SHOWSPEC);
		btn_memory_showitem.setChecked(BBViewSetting.IS_MEMORY_SHOWSPEC);
		screen.addPreference(btn_memory_showitem);

		CheckBoxPreference btn_memory_sort = new CheckBoxPreference(this);
		btn_memory_sort.setTitle("ソート状態");
		btn_memory_sort.setSummary("ソートの状態を維持する");
		btn_memory_sort.setKey(BBViewSetting.SETTING_MEMORY_SORT);
		btn_memory_sort.setChecked(BBViewSetting.IS_MEMORY_SORT);
		screen.addPreference(btn_memory_sort);

		CheckBoxPreference btn_memory_filter = new CheckBoxPreference(this);
		btn_memory_filter.setTitle("フィルタ状態");
		btn_memory_filter.setSummary("フィルタの状態を維持する");
		btn_memory_filter.setKey(BBViewSetting.SETTING_MEMORY_FILTER);
		btn_memory_filter.setChecked(BBViewSetting.IS_MEMORY_FILTER);
		screen.addPreference(btn_memory_filter);

		setPreferenceScreen(screen);
	}
	
	/**
	 * 画面に制御が戻った際に、設定を読み込む。
	 */
	protected void onResume() {
		super.onResume();
		BBViewSettingManager.loadSettings(this);
	}
	
	/**
	 * 画面から制御が離れた際に、設定を読み込む。
	 */
	protected void onPause() {
		super.onPause();
		BBViewSettingManager.loadSettings(this);
	}
	
	/**
	 * オプションメニュー生成時の処理を行う。
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		// メニューアイテム(エクスポート)
		MenuItem item2 = menu.add(0, MENU_IMPORT, 0, "インポート");
		item2.setIcon(android.R.drawable.ic_menu_save);

		// メニューアイテム(エクスポート)
		MenuItem item3 = menu.add(0, MENU_EXPORT, 0, "エクスポート");
		item3.setIcon(android.R.drawable.ic_menu_save);

		return true;
	}
	
	/**
	 * オプションメニューの選択時の処理を行う。
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String title = "";
		String msg = "";
		
		switch(item.getItemId()) {
		
			// ファイルのインポート
			case MENU_IMPORT:
				title = "ファイルのインポート";
				msg = 
					"SDカードのBBViewフォルダからファイルをインポートします。" + NEWLINE +
					"指定のフォルダやファイルが存在しない場合、処理は行われません。" + NEWLINE +
					"アプリ内のデータは削除しないため、不要なファイルが残る可能性があります。";
				dialog_type = CMD_IMPORT;
				ShowDialog(title, msg, true);
				break;
				
			// ファイルのエクスポート
			case MENU_EXPORT:
				title = "ファイルのエクスポート";
				msg = 
					"SDカードのBBViewフォルダへファイルをエクスポートします。" + NEWLINE +
					"フォルダが存在しない場合、自動的に生成します。" + NEWLINE +
					"同名のフォルダ、ファイルが存在する場合は上書きするため、事前に退避してください。";
				dialog_type = CMD_EXPORT;
				ShowDialog(title, msg, true);
				break;

		}

		return true;
	}

	/**
	 * ダイアログのボタンタップ時の処理を行う。
	 */
	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(which != DialogInterface.BUTTON1) {
			return;
		}
		
		if(dialog_type == CMD_IMPORT) {
			importFile();
		}
		else if(dialog_type == CMD_EXPORT) {
			exportFile();
		}
	}

	/**
	 * エクスポート処理
	 */
	private void exportFile() {
		boolean ret = false;
		
		String copy_fm_str = this.getFilesDir().toString();
		File copy_to = Environment.getExternalStorageDirectory();
		String copy_to_str = copy_to.getPath() + File.separator + "BBView";
		
		ret = FileIO.copy(copy_fm_str, copy_to_str);
		
		if(ret) {
			String warning_str = 
				"エクスポートが完了しました" + NEWLINE + 
				"エクスポート先：" + copy_to_str;
			Toast.makeText(this, warning_str, Toast.LENGTH_LONG).show();
		}
		else {
			String warning_str = 
				"エクスポートに失敗しました" + NEWLINE + 
				"エクスポート先：" + copy_to_str;
			Toast.makeText(this, warning_str, Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * インポート処理
	 */
	private void importFile() {
		boolean ret = false;
		
		File copy_fm = Environment.getExternalStorageDirectory();
		String copy_fm_str = copy_fm.getPath() + File.separator + "BBView";
		String copy_to_str = this.getFilesDir().toString();
		
		ret = FileIO.copy(copy_fm_str, copy_to_str);
		
		if(ret) {
			String warning_str = 
				"インポートが完了しました" + NEWLINE + 
				"インポート元：" + copy_fm_str;
			Toast.makeText(this, warning_str, Toast.LENGTH_LONG).show();
		}
		else {
			String warning_str = 
				"インポートに失敗しました" + NEWLINE + 
				"インポート元：" + copy_fm_str;
			Toast.makeText(this, warning_str, Toast.LENGTH_LONG).show();
		}

		String file_dir = getFilesDir().toString();
		CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
		custom_mng.load();
	}
	
	/**
	 * ダイアログの生成および表示
	 * @param title
	 * @param msg
	 * @param is_yesno
	 */
	private void ShowDialog(String title, String msg, boolean is_yesno) {
		AlertDialog.Builder cmd_dialog = new AlertDialog.Builder(this);
		cmd_dialog.setIcon(android.R.drawable.ic_menu_more);
		cmd_dialog.setTitle(title);
		cmd_dialog.setMessage(msg);
		if(is_yesno) {
			cmd_dialog.setPositiveButton("Yes", this);
			cmd_dialog.setNegativeButton("No", this);
		}
		else {
			cmd_dialog.setPositiveButton("OK", this);
		}
		cmd_dialog.show();
	}
}

