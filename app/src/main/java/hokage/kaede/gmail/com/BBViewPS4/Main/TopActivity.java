package hokage.kaede.gmail.com.BBViewPS4.Main;

import java.io.InputStream;

import hokage.kaede.gmail.com.BBViewPS4.Custom.CustomMainActivity;
import hokage.kaede.gmail.com.BBViewPS4.Item.CategoryListActivity;
import hokage.kaede.gmail.com.BBViewPS4.R;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataReader;
import hokage.kaede.gmail.com.BBViewLib.Java.BBNetDatabase;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomFileManager;
import hokage.kaede.gmail.com.BBViewLib.Java.FavoriteManager;
import hokage.kaede.gmail.com.BBViewLib.Java.SpecValues;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BBViewSettingManager;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BaseActivity;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;
import hokage.kaede.gmail.com.StandardLib.Java.FileIO;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 起動直後の画面を表示するクラス。
 */
public class TopActivity extends BaseActivity {
	
	private static final String BTN_TEXT_CUSTOM   = "機体カスタマイズ";
	private static final String BTN_TEXT_ITEM     = "所持品確認";
	private static final String BTN_TEXT_PURCHASE = "購入プレビュー";
	private static final String BTN_TEXT_SHOP     = "店舗検索";
	private static final String BTN_TEXT_BLOG     = "BBView開発室へ";
	
	private final static String MENU_SETTING         = "設定";
	private final static String MENU_OTHER           = "その他";
	private final static String MENU_OFFICIAL        = "BB公式ページを開く";
	private final static String MENU_ABOUT           = "本アプリについて";
	
	public static final String[] BLOG_TITLES = { "トップページ", "よくある質問", "スペック計算式" };
	public static final String[] BLOG_URLS   = {
		"http://bbview.blog.fc2.com/",
		"http://bbview.blog.fc2.com/blog-entry-21.html",
		"http://bbview.blog.fc2.com/blog-entry-8.html",
	};

	/**
	 * 画面生成時の処理を行う。
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 初期化する
		SpecValues.init();
		loadPartsData();
		initCustomData();
		FavoriteManager.init(getFilesDir().toString());
		
		// アプリの設定値を読み込む
		BBViewSettingManager.loadSettings(this);

		// BB.NETのデータを更新する
		BBNetDatabase.getInstance().init(this.getFilesDir().toString());
		
		// 初回起動時は説明ページを起動し、アップデート時は更新情報をダイアログ表示する。
		if(BBViewSetting.isFirstFlag(this)) {
			BBViewSetting.setVersionCode(this);
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
		}
		else if(BBViewSetting.isUpdateFlag(this)) {
			BBViewSetting.setVersionCode(this);
			showFirstDialog();
		}

		// メインのレイアウト
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.TOP | Gravity.LEFT);
		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		// アプリ更新情報のタイトルレイアウト
		LinearLayout title_layout = new LinearLayout(this);
		title_layout.setOrientation(LinearLayout.HORIZONTAL);
		title_layout.setGravity(Gravity.TOP | Gravity.LEFT);
		title_layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		// アプリ更新情報のタイトルテキスト
		TextView title_view = new TextView(this);
		title_view.setText("アプリ更新情報");
		title_view.setTextColor(SettingManager.getColorWhite());
		title_view.setTextSize(BBViewSetting.getTextSize(this, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		title_view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
		title_layout.addView(title_view);
		
		layout.addView(title_layout);
		
		// アプリ更新情報のスクロール
		ScrollView sv = new ScrollView(this);
		sv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
		layout.addView(sv);
		
		// アプリ更新情報のビュー
		TextView appupd_text_view = new TextView(this);
		appupd_text_view.setText(loadUpdateText());
		appupd_text_view.setTextColor(SettingManager.getColorWhite());
		appupd_text_view.setTextSize(BBViewSetting.getTextSize(this, BBViewSetting.FLAG_TEXTSIZE_SMALL));
		appupd_text_view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
		sv.addView(appupd_text_view);
		
		// 各種ボタン
		Button custom_btn = new Button(this);
		custom_btn.setText(BTN_TEXT_CUSTOM);
		custom_btn.setOnClickListener(new OnMoveCustomListener());
		layout.addView(custom_btn);

		Button item_btn = new Button(this);
		item_btn.setText(BTN_TEXT_ITEM);
		item_btn.setOnClickListener(new OnMoveItemListener());
		layout.addView(item_btn);

		Button blog_btn = new Button(this);
		blog_btn.setText(BTN_TEXT_BLOG);
		blog_btn.setOnClickListener(new OnSelectBlogListener());
		layout.addView(blog_btn);

		setContentView(layout);
	}

	/**
	 * パーツ・武器のデータを読み込む。
	 */
	private void loadPartsData() {
		loadPartsData(R.raw.bb_data);
	}

	/**
	 * パーツ・武器のデータを読み込む。
	 * @param resource パーツ・武器のリソースデータID
	 */
	private void loadPartsData(int resource) {

		BBDataManager data_mng = BBDataManager.getInstance();
		data_mng.init();
		
		try {
			Resources res = this.getResources();
			InputStream is_bbdata = res.openRawResource(resource);  // Resources.NotFoundException
			BBDataReader.read(is_bbdata);

		} catch(Resources.NotFoundException res_e) {
			Toast.makeText(this, "パーツ・武器のデータが見つかりません。", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * アセンデータの初期化を行う。
	 */
	private void initCustomData() {
		String filedir = getFilesDir().toString();
		CustomFileManager custom_mng = CustomFileManager.getInstance(filedir);

		try {
			Resources res = this.getResources();
			InputStream is_defaultset = res.openRawResource(R.raw.defaultset);  // Resources.NotFoundException
			custom_mng.init(is_defaultset);

		} catch(Resources.NotFoundException res_e) {
			Toast.makeText(this, "リソースデータが見つかりません。", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 初回起動時に表示するダイアログを生成する。
	 * @return 初回起動時に表示するダイアログ
	 */
	private void showFirstDialog() {
		String update_info_str = loadUpdateText();

		// ダイアログを生成する
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("更新情報");
		builder.setMessage(update_info_str);
		builder.setPositiveButton("OK", null);

		Dialog dialog = builder.create();
		dialog.setOwnerActivity(this);
		dialog.show();
	}
	
	/**
	 * 更新情報のテキストを読み込む。
	 * @return 更新情報のテキスト
	 */
	private String loadUpdateText() {
		String update_info_str;

		// 更新情報を読み込む
		try {
			Resources res = this.getResources();
			InputStream update_info = res.openRawResource(R.raw.update_info);  // Resources.NotFoundException
			update_info_str = FileIO.readInputStream(update_info, FileIO.ENCODE_UTF8);

		} catch(Resources.NotFoundException res_e) {
			update_info_str = "更新情報を読み込めませんでした";
		}
		
		return update_info_str;
	}

	/**
	 * オプションメニュー生成時の処理を行う。
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(MENU_SETTING);
		
		SubMenu sub_menu = menu.addSubMenu(MENU_OTHER);
		sub_menu.add(MENU_OFFICIAL);
		sub_menu.add(MENU_ABOUT);
		
		return true;
	}
	
	/**
	 * オプションメニュー選択時の処理を行う。
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String menu_title = item.getTitle().toString();
		
		if(menu_title.equals(MENU_SETTING)) {
			Intent intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
		}
		else if(menu_title.equals(MENU_OFFICIAL)) {
			Uri uri = Uri.parse("http://ps4.borderbreak.com/");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
		else if(menu_title.equals(MENU_ABOUT)) {
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
		}
		
		return true;
	}

	/**
	 * 機体カスタマイズ画面へのインテントを発行するリスナー
	 */
	private class OnMoveCustomListener implements OnClickListener {
		
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(TopActivity.this, CustomMainActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * 所持品確認画面へのインテントを発行するリスナー
	 */
	private class OnMoveItemListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(TopActivity.this, CategoryListActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * BBView開発室の表示するページを選択するリスナー
	 */
	private class OnSelectBlogListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(TopActivity.this);
			builder.setTitle("表示するページを選択");
			builder.setItems(BLOG_TITLES, new OnMoveBlogListener());

			Dialog dialog = builder.create();
			dialog.setOwnerActivity(TopActivity.this);
			dialog.show();
		}
		
	}

	/**
	 * BBView開発室を表示するインテントを発行するリスナー
	 */
	private class OnMoveBlogListener implements android.content.DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int position) {
			Uri uri = Uri.parse(BLOG_URLS[position]);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	}

}
