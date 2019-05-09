package hokage.kaede.gmail.com.BBViewPS4.Custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BBDataAdapter;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BBDataAdapterItemProperty;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BaseActivity;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.IntentManager;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataFilter;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomData;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomFileManager;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;

/**
 * 「パーツ武器選択」画面を表示するクラス。
 */
public class SelectChipActivity extends BaseActivity {
	private static final int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
	private static final int FP = LinearLayout.LayoutParams.FILL_PARENT;

	private BBDataManager mDataManager;
	private BBDataFilter mFilter;

	private BBDataAdapterItemProperty mProperty;
	private BBDataAdapter mAdapter;

	// リスト表示時のタイトル文字列設定用キー
	public static final String INTENTKEY_TITLENAME  = "INTENTKEY_TITLENAME";
	public static final String INTENTKEY_CHIPTYPE = "INTENTKEY_CHIPTYPE";
	public static final String INTENTKEY_CHIPINDEX = "INTENTKEY_CHIPINDEX";

	private String mType;
	private int mIndex;

	// リストのViewID
	private static final int VIEW_ID_DEFALUT_LIST = 5000;
	/**
	 * 画面生成時の処理を行う。
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDataManager = BBDataManager.getInstance();
		mDataManager.setSortKey("");

		Intent intent = getIntent();

		// ダイアログの初期化を行う
		initDialog(intent);

		// 画面を生成する
		createView(intent);
	}

	/**
	 * 各ダイアログの初期化を行う
	 * @param intent
	 */
	private void initDialog(Intent intent) {

		// インテントからデータを取得する
		mType = intent.getStringExtra(INTENTKEY_CHIPTYPE);
		mIndex = intent.getIntExtra(INTENTKEY_CHIPINDEX, 0);

		// 表示項目のフラグ設定
		mFilter = new BBDataFilter();

		if(mType.equals(BBDataManager.BLUST_PARTS_HEAD)) {
			mFilter.setOtherType("装着パーツ(頭部)");
		}
		else if(mType.equals(BBDataManager.BLUST_PARTS_BODY)) {
			mFilter.setOtherType("装着パーツ(胴部)");
		}
		else if(mType.equals(BBDataManager.BLUST_PARTS_ARMS)) {
			mFilter.setOtherType("装着パーツ(腕部)");
		}
		else if(mType.equals(BBDataManager.BLUST_PARTS_LEGS)) {
			mFilter.setOtherType("装着パーツ(脚部)");
		}
		else {
			mFilter.setOtherType("サポート");
		}

		BBData recent_data = IntentManager.getSelectedData(intent);

		// アダプタのプロパティの設定
		mProperty = new BBDataAdapterItemProperty();
		mProperty.setShowSwitch(true);
		mProperty.setShowFavorite(true);
		mProperty.setBaseItem(recent_data);

		// アダプタの生成
		ArrayList<BBData> item_list = mDataManager.getList(mFilter);
		item_list.add(mDataManager.getUnselectedChipData());
		mAdapter = new BBDataAdapter(mProperty);
		mAdapter.setList(item_list);
		mAdapter.notifyDataSetChanged();

		if(item_list.size() <= 0) {
			Toast.makeText(this, "条件に一致するチップはありません。", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 画面生成処理を行う。
	 * @param intent
	 */
	private void createView(Intent intent) {

		// タイトルを取得する
		String title = intent.getStringExtra(INTENTKEY_TITLENAME);

		// 全体レイアウト設定
		LinearLayout layout_all = new LinearLayout(this);
		layout_all.setLayoutParams(new LinearLayout.LayoutParams(FP, FP));
		layout_all.setOrientation(LinearLayout.VERTICAL);
		layout_all.setGravity(Gravity.TOP);

		// タイトルを設定
		TextView title_text = new TextView(this);
		title_text.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));
		title_text.setTextSize(BBViewSetting.getTextSize(this, BBViewSetting.FLAG_TEXTSIZE_LARGE));
		title_text.setGravity(Gravity.CENTER);
		title_text.setTextColor(SettingManager.getColorWhite());
		title_text.setBackgroundColor(SettingManager.getColorDarkGreen());
		title_text.setText(title);

		// リスト設定
		ListView list_view = new ListView(this);
		list_view.setLayoutParams(new LinearLayout.LayoutParams(FP, WC, 1));
		list_view.setOnItemClickListener(new OnSelectItemListener());
		list_view.setId(VIEW_ID_DEFALUT_LIST);
		list_view.setAdapter(mAdapter);

		// 画面上部のテキストを設定する
		layout_all.addView(title_text);
		layout_all.addView(list_view);

		// リストの位置を選択中のアイテムの位置に変更する
		list_view.setSelection(mAdapter.getBaseItemIndex());

		setContentView(layout_all);
	}

	/**
	 * リストの項目選択時の処理を行うリスナー
	 */
	private class OnSelectItemListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
			BBData data = mAdapter.getItem(position);
			backCustomView(data);
		}
	}

	/**
	 * 指定位置のパーツを設定し、前画面に戻る。
	 * @param data パーツデータ
	 */
	private void backCustomView(BBData data) {

		// カスタムデータに反映する
		String file_dir = getFilesDir().toString();
		CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
		CustomData custom_data = custom_mng.getCacheData();
		custom_data.setChip(data, mType, mIndex);

		// カスタムデータをキャッシュファイルに書き込む
		custom_mng.saveCacheData(custom_data);

		finish();
	}
}
