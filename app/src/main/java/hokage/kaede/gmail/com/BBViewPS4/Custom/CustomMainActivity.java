package hokage.kaede.gmail.com.BBViewPS4.Custom;

import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomData;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BaseActivity;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomFileManager;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 「機体カスタマイズ」機能の各画面（タブのある画面）を表示するクラス。
 */
public class CustomMainActivity extends BaseActivity {

	private String mViewMode;
	private static final String VIEWMODE_STR_CUSTOM = "アセン";
	private static final String VIEWMODE_STR_CHIP   = "チップ";
	private static final String VIEWMODE_STR_SPEC   = "性能";
	private static final String VIEWMODE_STR_RESIST = "耐性";
	private static final String VIEWMODE_STR_FILE   = "データ";

	private static final String MENU_SHOW_TYPEB      = "タイプB表示";
	private static final String MENU_SHOW_CHIPS      = "チップを表示する";
	private static final String MENU_RESET_CUSTOM    = "アセンをリセットする";
	private static final String MENU_SHOW_SIMPLE     = "簡易表示する";
	private static final String MENU_RESIST_FILTER   = "フィルタ設定";
	
	private boolean mIsShowTypeB = false;
	private boolean mIsShowChips = false;
	private boolean mIsShowSimple = false;
	private int mSpecViewMode = SpecView.MODE_BASE;
	
	// メイン画面のレイアウトID
	private static final int MAIN_LAYOUT_ID = 10000;
	private static final int SHOW_VIEW_ID = 20000;
	
	/**
	 * 画面生成時の処理を行う。
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle(this.getTitle() + " (機体カスタマイズ)");
		
		mViewMode = VIEWMODE_STR_CUSTOM;

		// メインの画面
		LinearLayout main_layout = new LinearLayout(this);
		main_layout.setOrientation(LinearLayout.VERTICAL);
		main_layout.setGravity(Gravity.TOP | Gravity.LEFT);
		main_layout.setId(MAIN_LAYOUT_ID);
		
		updateView(main_layout);
		
		setContentView(main_layout);
	}
	
	/**
	 * 画面再表示時の処理を行う。
	 * アセン画面からパーツ/武器を選択し、戻ってきた際に画面を更新する。
	 */
	@Override
	protected void onRestart() {
		super.onRestart();

		LinearLayout main_layout = (LinearLayout)CustomMainActivity.this.findViewById(MAIN_LAYOUT_ID);
		updateView(main_layout);
	}
	
	/**
	 * 画面非表示字の処理を行う。
	 * SBや要請兵器の所持状況をリセットする。
	 * 
	 * onResume()時は所持状況ボタンON/OFF状態を含めて画面表示が保持されているため、
	 * 画面を再構築する必要はない。
	 */
	protected void onPause() {
		super.onPause();

		String file_dir = getFilesDir().toString();
		CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
		CustomData custom_data = custom_mng.getCacheData();
		custom_data.setMode(CustomData.MODE_NORMAL);
	}
	
	/**
	 * 画面を更新する。
	 * 現状、全てのビューを削除し、新たに生成した画面を追加する方法で実装している。
	 * @param main_layout ビューを載せる対象のレイアウト
	 */
	private void updateView(LinearLayout main_layout) {
		String file_dir = getFilesDir().toString();
		CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
		CustomData custom_data = custom_mng.getCacheData();

		custom_data.setMode(CustomData.MODE_NORMAL);
		
		// SpecViewの前回のモードを取得する。前回が存在しない場合は標準を設定する。
		try {
			View view = main_layout.findViewById(SHOW_VIEW_ID);
			
			if(view instanceof SpecView) {
				mSpecViewMode = ((SpecView)view).getMode();
			}
			
		} catch(Exception e) {
			mSpecViewMode = SpecView.MODE_BASE;
		}
		
		main_layout.removeAllViews();
		main_layout.addView(createTopLayout());
		
		View target_view = null;

		if(mViewMode.equals(VIEWMODE_STR_CUSTOM)) {
			target_view = new CustomView(this, custom_data);
		}
		else if(mViewMode.equals(VIEWMODE_STR_CHIP)) {
			target_view = new ChipView(this, custom_data);
		}
		else if(mViewMode.equals(VIEWMODE_STR_SPEC)) {
			target_view = new SpecView(this, mIsShowSimple, mSpecViewMode, mIsShowTypeB);
		}
		else if(mViewMode.equals(VIEWMODE_STR_RESIST)) {
			target_view = new ResistView(this, mIsShowTypeB);
		}
		else if(mViewMode.equals(VIEWMODE_STR_FILE)) {
			target_view = new FileListView(this);
		}
		
		if(target_view != null) {
			target_view.setId(SHOW_VIEW_ID);
			main_layout.addView(target_view);
			
			// オプションメニューを更新する
			invalidateOptionsMenu();
		}
	}
	
	/**
	 * 画面上部のレイアウトを生成する。
	 * @return
	 */
	private LinearLayout createTopLayout() {
		LinearLayout.LayoutParams layout_prm = 
			new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,	1);
		
		int text_color = SettingManager.getColorWhite();
		float text_size = SettingManager.getTextSize(this);
		
		TextView custom_text_view = new TextView(this);
		custom_text_view.setTextColor(text_color);
		custom_text_view.setPadding(5, 15, 5, 15);
		custom_text_view.setLayoutParams(layout_prm);
		custom_text_view.setGravity(Gravity.CENTER);
		custom_text_view.setText(VIEWMODE_STR_CUSTOM);
		custom_text_view.setTextSize(text_size);
		custom_text_view.setOnClickListener(new OnClickTopMenuListener(VIEWMODE_STR_CUSTOM));

		TextView chip_text_view = new TextView(this);
		chip_text_view.setTextColor(text_color);
		chip_text_view.setPadding(5, 15, 5, 15);
		chip_text_view.setLayoutParams(layout_prm);
		chip_text_view.setGravity(Gravity.CENTER);
		chip_text_view.setText(VIEWMODE_STR_CHIP);
		chip_text_view.setTextSize(text_size);
		chip_text_view.setOnClickListener(new OnClickTopMenuListener(VIEWMODE_STR_CHIP));

		TextView spec_text_view = new TextView(this);
		spec_text_view.setTextColor(text_color);
		spec_text_view.setPadding(5, 15, 5, 15);
		spec_text_view.setLayoutParams(layout_prm);
		spec_text_view.setGravity(Gravity.CENTER);
		spec_text_view.setText(VIEWMODE_STR_SPEC);
		spec_text_view.setTextSize(text_size);
		spec_text_view.setOnClickListener(new OnClickTopMenuListener(VIEWMODE_STR_SPEC));

		TextView resist_text_view = new TextView(this);
		resist_text_view.setTextColor(text_color);
		resist_text_view.setPadding(5, 15, 5, 15);
		resist_text_view.setLayoutParams(layout_prm);
		resist_text_view.setGravity(Gravity.CENTER);
		resist_text_view.setText(VIEWMODE_STR_RESIST);
		resist_text_view.setTextSize(text_size);
		resist_text_view.setOnClickListener(new OnClickTopMenuListener(VIEWMODE_STR_RESIST));

		TextView file_text_view = new TextView(this);
		file_text_view.setTextColor(text_color);
		file_text_view.setPadding(5, 15, 5, 15);
		file_text_view.setLayoutParams(layout_prm);
		file_text_view.setGravity(Gravity.CENTER);
		file_text_view.setText(VIEWMODE_STR_FILE);
		file_text_view.setTextSize(text_size);
		file_text_view.setOnClickListener(new OnClickTopMenuListener(VIEWMODE_STR_FILE));
		
		// 選択中の場合、文字色と背景色を変更して見た目を強調する
		if(mViewMode.equals(VIEWMODE_STR_CUSTOM)) {
			custom_text_view.setTextColor(SettingManager.getColorCyan());
			custom_text_view.setBackgroundColor(SettingManager.getColorBlack());
		}
		else if(mViewMode.equals(VIEWMODE_STR_CHIP)) {
			chip_text_view.setTextColor(SettingManager.getColorCyan());
			chip_text_view.setBackgroundColor(SettingManager.getColorBlack());
		}
		else if(mViewMode.equals(VIEWMODE_STR_SPEC)) {
			spec_text_view.setTextColor(SettingManager.getColorCyan());
			spec_text_view.setBackgroundColor(SettingManager.getColorBlack());
		}
		else if(mViewMode.equals(VIEWMODE_STR_RESIST)) {
			resist_text_view.setTextColor(SettingManager.getColorCyan());
			resist_text_view.setBackgroundColor(SettingManager.getColorBlack());
		}
		else if(mViewMode.equals(VIEWMODE_STR_FILE)) {
			file_text_view.setTextColor(SettingManager.getColorCyan());
			file_text_view.setBackgroundColor(SettingManager.getColorBlack());
		}

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setGravity(Gravity.CENTER);
		layout.setBackgroundColor(SettingManager.getColorGray());
		
		layout.addView(custom_text_view);
		layout.addView(chip_text_view);
		layout.addView(spec_text_view);
		layout.addView(resist_text_view);
		layout.addView(file_text_view);
		
		return layout;
	}
	
	/**
	 * トップメニューを押下した際の処理を行うリスナー。
	 */
	private class OnClickTopMenuListener implements OnClickListener {
		private String mMenuName;
		
		public OnClickTopMenuListener(String menu_name) {
			mMenuName = menu_name;
		}
 
		@Override
		public void onClick(View arg0) {
			mViewMode = mMenuName;
			LinearLayout main_layout = (LinearLayout)CustomMainActivity.this.findViewById(MAIN_LAYOUT_ID);
			updateView(main_layout);
		}
	}
	
	/**
	 * オプションメニュー生成時の処理を行う。
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		if(mViewMode.equals(VIEWMODE_STR_CUSTOM)) {
			MenuItem item = menu.add(MENU_SHOW_CHIPS);
			item.setCheckable(true);
			item.setChecked(mIsShowChips);
			item.setOnMenuItemClickListener(new OnMenuShowChipListener());
			
			item = menu.add(MENU_RESET_CUSTOM);
			item.setOnMenuItemClickListener(new OnMenuResetCustomListener());
		}
		else if(mViewMode.equals(VIEWMODE_STR_SPEC)) {
			MenuItem item = menu.add(MENU_SHOW_SIMPLE);
			item.setCheckable(true);
			item.setChecked(mIsShowSimple);
			item.setOnMenuItemClickListener(new OnMenuShowSimpleListener());

			item = menu.add(MENU_SHOW_TYPEB);
			item.setCheckable(true);
			item.setChecked(mIsShowTypeB);
			item.setOnMenuItemClickListener(new OnMenuShowTypeBListener());
		}
		else if(mViewMode.equals(VIEWMODE_STR_RESIST)) {
			MenuItem item = menu.add(MENU_RESIST_FILTER);
			item.setOnMenuItemClickListener(new OnMenuFilterResistListener());

			item = menu.add(MENU_SHOW_TYPEB);
			item.setCheckable(true);
			item.setChecked(mIsShowTypeB);
			item.setOnMenuItemClickListener(new OnMenuShowTypeBListener());
		}

		return true;
	}
	
	/**
	 * タイプB表示のメニュー選択時の処理を行う。
	 */
	private class OnMenuShowTypeBListener implements OnMenuItemClickListener {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			LinearLayout main_layout = (LinearLayout)CustomMainActivity.this.findViewById(MAIN_LAYOUT_ID);
			
			if(item.isChecked()) {
				mIsShowTypeB = false;
				item.setChecked(false);
				updateView(main_layout);
			}
			else {
				mIsShowTypeB = true;
				item.setChecked(true);
				updateView(main_layout);
			}
			
			return false;
		}
		
	}

	/**
	 * チップを表示するのメニュー選択時の処理を行う。
	 */
	private class OnMenuShowChipListener implements OnMenuItemClickListener {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			LinearLayout main_layout = (LinearLayout)CustomMainActivity.this.findViewById(MAIN_LAYOUT_ID);
			
			if(item.isChecked()) {
				mIsShowChips = false;
				item.setChecked(false);
				updateView(main_layout);
			}
			else {
				mIsShowChips = true;
				item.setChecked(true);
				updateView(main_layout);
			}
			
			return false;
		}
	}
	
	/**
	 * アセンリセットメニュー選択時の処理を行う。
	 */
	private class OnMenuResetCustomListener implements OnMenuItemClickListener, android.content.DialogInterface.OnClickListener {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			AlertDialog.Builder reset_dialog = new AlertDialog.Builder(CustomMainActivity.this);
			reset_dialog.setTitle("アセンのリセット");
			reset_dialog.setPositiveButton("OK", this);
			reset_dialog.setNegativeButton("Cancel", null);
			reset_dialog.setMessage("編集中のデータを初期状態にリセットしますか？");
			reset_dialog.show();
			
			return false;
		}

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			String file_dir = getFilesDir().toString();
			CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
			custom_mng.resetCacheData();

			LinearLayout main_layout = (LinearLayout)CustomMainActivity.this.findViewById(MAIN_LAYOUT_ID);
			updateView(main_layout);

		}
	}

	/**
	 * 耐性画面のフィルタ設定起動時の処理を行う。
	 */
	private class OnMenuFilterResistListener implements OnMenuItemClickListener {

		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
			LinearLayout main_layout = (LinearLayout)CustomMainActivity.this.findViewById(MAIN_LAYOUT_ID);
			View view = main_layout.findViewById(SHOW_VIEW_ID);
			
			if(view instanceof ResistView) {
				((ResistView)view).showFilterDialog(CustomMainActivity.this);
			}
			
			return false;
		}
	}

	/**
	 * 簡易表示のメニュー選択時の処理を行う。
	 */
	private class OnMenuShowSimpleListener implements OnMenuItemClickListener {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			LinearLayout main_layout = (LinearLayout)CustomMainActivity.this.findViewById(MAIN_LAYOUT_ID);
			
			if(item.isChecked()) {
				mIsShowSimple = false;
				item.setChecked(false);
				updateView(main_layout);
			}
			else {
				mIsShowSimple = true;
				item.setChecked(true);
				updateView(main_layout);
			}
			
			return false;
		}
	}

	/**
	 * Backキーを押下した際の処理を行う。
	 * アセン画面の場合はトップ画面に遷移し、アセン画面以外の場合はアセン画面に遷移する。
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(mViewMode != VIEWMODE_STR_CUSTOM) {
				LinearLayout main_layout = (LinearLayout)CustomMainActivity.this.findViewById(MAIN_LAYOUT_ID);
				
				mViewMode = VIEWMODE_STR_CUSTOM;
				updateView(main_layout);
				return false;
			}
		}
		
		return super.onKeyDown(keyCode, event);
	}
}
