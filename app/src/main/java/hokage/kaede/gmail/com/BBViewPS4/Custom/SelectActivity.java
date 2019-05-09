package hokage.kaede.gmail.com.BBViewPS4.Custom;

import hokage.kaede.gmail.com.BBViewPS4.Item.InfoActivity;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BBDataAdapter;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BBDataAdapterItemProperty;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.ControlPanel;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.CmpPartsTableBuilder;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.CmpWeaponTableBuilder;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.SelectPartsExpandableAdapter;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.SelectWeaponExpandableAdapter;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataFilter;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomData;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomFileManager;
import hokage.kaede.gmail.com.BBViewLib.Java.FavoriteManager;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.ControlPanelBuilder;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.ShownKeysDialog;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.SortKeyDialog;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.ValueFilterDialog;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.SelectBBDataExpandableAdapter;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.ShownKeysDialog.OnOKClickListener;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.SortKeyDialog.OnSelectItemListener;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.ValueFilterDialog.OnClickValueFilterButtonListener;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BaseActivity;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.IntentManager;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;
import hokage.kaede.gmail.com.StandardLib.Java.FileArrayList;
import hokage.kaede.gmail.com.StandardLib.Java.ListConverter;

import java.util.ArrayList;

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
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * 「パーツ武器選択」画面を表示するクラス。
 */
public class SelectActivity extends BaseActivity implements OnItemClickListener, OnItemLongClickListener, OnSelectItemListener, OnOKClickListener, OnClickValueFilterButtonListener {
	private static final int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
	private static final int FP = LinearLayout.LayoutParams.FILL_PARENT;
	
	private BBDataManager mDataManager;
	private BBDataFilter mFilter;
	private FileArrayList mFavStore;

	private BBDataAdapterItemProperty mProperty;
	private BBDataAdapter mAdapter;
	private SelectBBDataExpandableAdapter mExAdapter;

	
	private SortKeyDialog mSortKeyDialog;
	private ShownKeysDialog mShownKeysDialog;
	private ValueFilterDialog mFilterManager;
	private ControlPanelBuilder mCmdDialog;
	private String[] mCommandList;
	
	private CmpPartsTableBuilder mCmpPartsDialog;
	private CmpWeaponTableBuilder mCmpWeaponDialog;
	
	// リスト表示時のタイトル文字列設定用キー
	public static final String INTENTKEY_TITLENAME  = "INTENTKEY_TITLENAME";
	public static final String INTENTKEY_PARTSTYPE  = "INTENTKEY_PARTSTYPE";
	public static final String INTENTKEY_BLUSTTYPE  = "INTENTKEY_BLUSTTYPE";
	public static final String INTENTKEY_WEAPONTYPE = "INTENTKEY_WEAPONTYPE";
	public static final String INTENTKEY_REQARM     = "INTENTKEY_REQARM";
	
	private String[] mSortKeys;

	// メニュー番号
	private static final int MENU_ITEM0 = 0;
	private static final int MENU_ITEM1 = 1;
	private static final int MENU_ITEM2 = 2;
	private static final int MENU_ITEM3 = 3;
	private static final int MENU_ITEM4 = 4;
	private static final int MENU_ITEM5 = 5;
	private static final int MENU_ITEM6 = 6;
	
	// コマンド制御ダイアログ関連の定義
	private static final String DIALOG_LIST_ITEM_INFO = "詳細を表示する";
	private static final String DIALOG_LIST_ITEM_CMP  = "比較する";
	private static final String DIALOG_LIST_ITEM_FULL = "フルセットを設定する";
	
	private static final int DIALOG_LIST_IDX_INFO = 0;
	private static final int DIALOG_LIST_IDX_CMP  = 1;
	private static final int DIALOG_LIST_IDX_FULL = 2;
	
	private static final String[] DIALOG_LIST_ITEMS_PARTS = { DIALOG_LIST_ITEM_INFO, DIALOG_LIST_ITEM_CMP, DIALOG_LIST_ITEM_FULL };
	private static final String[] DIALOG_LIST_ITEMS_BASE = { DIALOG_LIST_ITEM_INFO, DIALOG_LIST_ITEM_CMP };

	// リストのViewID
	private static final int VIEW_ID_DEFALUT_LIST = 5000;
	private static final int VIEW_ID_EX_LIST = 6000;
	
	// ソート時のタイプB設定
	private boolean mIsSortTypeB = false;

	// リストのカテゴリ表示設定
	private boolean mIsExpandable = false;

	// 所持品のみ表示設定
	private boolean mIsHavingOnly = false;
	
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
		String parts_type  = intent.getStringExtra(INTENTKEY_PARTSTYPE);
		String blust_type  = intent.getStringExtra(INTENTKEY_BLUSTTYPE);
		String weapon_type = intent.getStringExtra(INTENTKEY_WEAPONTYPE);
		String req_arm = intent.getStringExtra(INTENTKEY_REQARM);
		
		BBData recent_data = IntentManager.getSelectedData(intent);
		
		// 表示項目のフラグ設定
		mFilter = new BBDataFilter();
		String shown_save_key = "";
		mSortKeys = BBDataManager.getCmpTarget(recent_data);

		if(parts_type != null) {
			mIsExpandable = BBViewSetting.IS_SHOW_CATEGORYPARTS_INIT;
			mFavStore = FavoriteManager.getStore(parts_type);
			
			mFilter.setPartsType(parts_type);
			shown_save_key = parts_type;
			initCmdListDialog(true);
		}
		if(blust_type != null && weapon_type != null) {
			mIsExpandable = BBViewSetting.IS_SHOW_CATEGORYPARTS_INIT;
			mFavStore = FavoriteManager.getStore(blust_type);
			
			mFilter.setBlustType(blust_type);
			mFilter.setWeaponType(weapon_type);
			shown_save_key = blust_type + ":" + weapon_type;
			initCmdListDialog(false);
		}
		if(req_arm != null) {
			mFilter.setOtherType(req_arm);
			mFilter.setNotHavingShow(true);
			shown_save_key = req_arm;
			initCmdListDialog(false);
		}
		ArrayList<String> key_list = ListConverter.convert(mSortKeys);

		// ソート選択ダイアログを初期化する
		mSortKeyDialog = new SortKeyDialog(this, key_list);
		mSortKeyDialog.setSelectItemListener(this);
		
		// ソート設定をロードする
		if(BBViewSetting.IS_MEMORY_SORT) {
			mSortKeyDialog.setSaveKey(shown_save_key);
			mSortKeyDialog.loadSetting();
			mDataManager.setSortKey(mSortKeyDialog.getSortKey());
			mDataManager.setASC(mSortKeyDialog.getAsc());
		}
		
		// 表示項目選択ダイアログを初期化する
		mShownKeysDialog = new ShownKeysDialog(this, key_list);
		mShownKeysDialog.setOnButtonClickListener(this);
		
		// 表示項目設定をロードする
		if(BBViewSetting.IS_MEMORY_SHOWSPEC) {
			mShownKeysDialog.setSaveKey(shown_save_key);
			mShownKeysDialog.loadSetting();
		}

		// フィルタ設定ダイアログを初期化する
		mFilterManager = new ValueFilterDialog(mFilter, key_list);
		mFilterManager.setOnClickValueFilterButtonListener(this);
		mFilterManager.setBBData(recent_data);
		
		// フィルタ設定をロードする
		if(BBViewSetting.IS_MEMORY_FILTER) {
			mFilterManager.setSaveKey(shown_save_key);
			mFilterManager.loadSetting(this);
			mFilter = mFilterManager.getFilter();
		}

		// アダプタのプロパティの設定
		mProperty = new BBDataAdapterItemProperty();
		mProperty.setShowSwitch(true);
		mProperty.setShowFavorite(true);
		mProperty.setBaseItem(recent_data);
		mProperty.setShownKeys(mShownKeysDialog.getShownKeys());
		
		// アダプタの生成
		ArrayList<BBData> item_list = mDataManager.getList(mFilter);
		mAdapter = new BBDataAdapter(mProperty);
		mAdapter.setList(item_list);
		mAdapter.setBuilder(mCmdDialog);
		mAdapter.notifyDataSetChanged();

		if(blust_type != null && weapon_type != null) {
			mExAdapter = new SelectWeaponExpandableAdapter(mProperty, blust_type, weapon_type);
		}
		else {
			mExAdapter = new SelectPartsExpandableAdapter(mProperty);
		}

		mExAdapter.setFavStore(mFavStore);
		mExAdapter.addChildren(item_list);
		mExAdapter.setBuilder(mCmdDialog);
		mExAdapter.notifyDataSetChanged();

		if(item_list.size() <= 0) {
			Toast.makeText(this, "条件に一致するパーツはありません。", Toast.LENGTH_SHORT).show();
		}

		// 比較ダイアログを初期化する
		mCmpPartsDialog = new CmpPartsTableBuilder(this, BBViewSetting.IS_KM_PER_HOUR);
		mCmpWeaponDialog = new CmpWeaponTableBuilder(this, BBViewSetting.IS_KM_PER_HOUR);
	}
	
	/**
	 * コマンド制御ダイアログを初期化する。
	 * @param is_parts パーツかどうか。
	 */
	private void initCmdListDialog(boolean is_parts) {

		if(is_parts) {
			mCommandList = DIALOG_LIST_ITEMS_PARTS;
		}
		else {
			mCommandList = DIALOG_LIST_ITEMS_BASE;
		}
		
		mCmdDialog = new ControlPanelBuilder(mCommandList, new OnClickControlPanelListener());
		
		// 設定に応じてボタンを非表示にする
		if(!BBViewSetting.IS_SHOW_LISTBUTTON) {
			mCmdDialog.setHiddenPanel(true);
		}
		else {
			if (!BBViewSetting.IS_LISTBUTTON_SHOWINFO) {
				mCmdDialog.setHiddenButton(DIALOG_LIST_IDX_INFO);
			}

			if (!BBViewSetting.IS_LISTBUTTON_SHOWCMP) {
				mCmdDialog.setHiddenButton(DIALOG_LIST_IDX_CMP);
			}

			if (!BBViewSetting.IS_LISTBUTTON_SHOWFULLSET) {
				mCmdDialog.setHiddenButton(DIALOG_LIST_IDX_FULL);
			}
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
		list_view.setOnItemClickListener(this);
		list_view.setOnItemLongClickListener(this);
		list_view.setId(VIEW_ID_DEFALUT_LIST);
		list_view.setAdapter(mAdapter);
		
		// カテゴリリスト設定
		ExpandableListView exlist_view = new ExpandableListView(this);
		exlist_view.setAdapter(mExAdapter);
		exlist_view.setLayoutParams(new LinearLayout.LayoutParams(FP, WC, 1));
		exlist_view.setOnChildClickListener(new OnClickExItemListener());
		exlist_view.setId(VIEW_ID_EX_LIST);

		if(mIsExpandable) {
			list_view.setVisibility(View.GONE);
		}
		else {
			exlist_view.setVisibility(View.GONE);
		}
		
		// 画面上部のテキストを設定する
		layout_all.addView(title_text);
		layout_all.addView(list_view);
		layout_all.addView(exlist_view);
		
		// リストの位置を選択中のアイテムの位置に変更する
		list_view.setSelection(mAdapter.getBaseItemIndex());
		
		setContentView(layout_all);
	}
	
	/**
	 * オプションメニュー生成時の処理を行う。
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		Intent intent = getIntent();
		String parts_type  = intent.getStringExtra(INTENTKEY_PARTSTYPE);
		String weapon_type = intent.getStringExtra(INTENTKEY_WEAPONTYPE);
		
		menu.add(0, MENU_ITEM0, 0, "ソート設定").setIcon(android.R.drawable.ic_menu_sort_alphabetically);
		menu.add(0, MENU_ITEM6, 0, "ソート昇順/降順切替");
		menu.add(0, MENU_ITEM2, 0, "表示項目設定").setIcon(android.R.drawable.ic_menu_add);
		menu.add(0, MENU_ITEM1, 0, "フィルタ設定").setIcon(android.R.drawable.ic_menu_add);

		if(parts_type != null || weapon_type != null) {
			MenuItem item = menu.add(0, MENU_ITEM4, 0, "カテゴリ表示").setIcon(android.R.drawable.ic_menu_add);
			item.setCheckable(true);
			item.setChecked(mIsExpandable);
		}

		if(weapon_type != null) {
			MenuItem item = menu.add(0, MENU_ITEM3, 0, "タイプB表示").setIcon(android.R.drawable.ic_menu_add);
			item.setCheckable(true);
			item.setOnMenuItemClickListener(new ClickTypebMenuListener());
		}

		MenuItem item = menu.add(0, MENU_ITEM5, 0, "所持品のみ表示").setIcon(android.R.drawable.ic_menu_add);
		item.setCheckable(true);
		item.setChecked(mIsHavingOnly);
		
		return true;
	}
	
	/**
	 * タイプB表示ボタンを選択した際の処理を行うリスナー。
	 */
	private class ClickTypebMenuListener implements OnMenuItemClickListener {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			boolean is_checked = !item.isChecked();
			mProperty.setShowTypeB(is_checked);

			mIsSortTypeB = is_checked;
			item.setChecked(is_checked);

			ArrayList<BBData> datalist = mDataManager.getList(mFilter, mIsSortTypeB);
			mAdapter.setList(datalist);

			mAdapter.notifyDataSetChanged();
			mExAdapter.notifyDataSetChanged();
			
			return false;
		}
	}
	
	/**
	 * オプションメニュータップ時の処理を行う。
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case MENU_ITEM0:
				mSortKeyDialog.showDialog();
				break;

			case MENU_ITEM1:
				mFilterManager.showDialog(this);
				break;
				
			case MENU_ITEM2:
				mShownKeysDialog.showDialog();
				break;
				
			case MENU_ITEM4:
				changedListVisiblity(item);
				break;

			case MENU_ITEM5:
				mIsHavingOnly = !mIsHavingOnly;
				item.setChecked(mIsHavingOnly);
				updateList();
				break;

			case MENU_ITEM6:
				mSortKeyDialog.setAsc(!mSortKeyDialog.getAsc());
				updateSort(mSortKeyDialog);
				break;
		}
		
		return true;
	}
	
	/**
	 * リストの表示状態(通常表示/カテゴリ表示)を変更する。
	 */
	private void changedListVisiblity(MenuItem item) {
		View default_list_view = this.findViewById(VIEW_ID_DEFALUT_LIST);
		View ex_list_view = this.findViewById(VIEW_ID_EX_LIST);
		
		mIsExpandable = !mIsExpandable;
		if(mIsExpandable) {
			default_list_view.setVisibility(View.GONE);
			ex_list_view.setVisibility(View.VISIBLE);
		}
		else {
			default_list_view.setVisibility(View.VISIBLE);
			ex_list_view.setVisibility(View.GONE);
		}

		item.setChecked(mIsExpandable);
	}
	
	/**
	 * リストの項目選択時の処理を行う。
	 */
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		BBData data = mAdapter.getItem(position);
		backCustomView(data);
	}
	
	/**
	 * カテゴリリストのデータ選択時の処理を行うリスナー
	 * データをアセンに設定する。
	 */
	private class OnClickExItemListener implements ExpandableListView.OnChildClickListener {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
			backCustomView(mExAdapter.getChild(groupPosition, childPosition));
			return false;
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
		custom_data.setData(data);
		
		// カスタムデータをキャッシュファイルに書き込む
		custom_mng.saveCacheData(custom_data);

		finish();
	}

	/**
	 * 指定の位置のパーツでフルセットを設定し、前画面に戻る。
	 * @param target_data パーツデータ
	 */
	private void backCustomViewFullSetCheck(BBData target_data) {
		String name = target_data.get("名称");
		
		AlertDialog.Builder check_dialog = new AlertDialog.Builder(this);
		check_dialog.setTitle("フルセット設定");
		check_dialog.setPositiveButton("OK", new OnOKFullSetListener(target_data));
		check_dialog.setNegativeButton("Cancel", null);
		check_dialog.setMessage(name + "に設定しますか？");
		check_dialog.show();
	}
	
	/**
	 * フルセット設定OKの場合の処理を行うリスナー。
	 */
	private class OnOKFullSetListener implements OnClickListener {
		private BBData mTarget;
		
		public OnOKFullSetListener(BBData target_data) {
			mTarget = target_data;
		}

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			backCustomViewFullSet(mTarget);
		}
	}

	/**
	 * 指定の位置のパーツでフルセットを設定し、前画面に戻る。
	 * @param target_data パーツデータ
	 */
	private void backCustomViewFullSet(BBData target_data) {
		
		// 選択したパーツ名を取得し、全部位のパーツをカスタムデータに反映する。
		String name = target_data.get("名称");

		BBDataManager manager = BBDataManager.getInstance();

		String file_dir = getFilesDir().toString();
		CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
		CustomData custom_data = custom_mng.getCacheData();
		
		int size = BBDataManager.BLUST_PARTS_LIST.length;
		for(int i=0; i<size; i++) {
			BBData data = manager.getPartsData(name, BBDataManager.BLUST_PARTS_LIST[i]);
			custom_data.setData(data);
		}

		// カスタムデータをキャッシュファイルに書き込む
		custom_mng.saveCacheData(custom_data);

		finish();
	}
	
	/**
	 * リストの項目選択時(長)の処理を行う。
	 * コマンドダイアログに選択したデータを設定し、操作選択ダイアログを表示する。
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
		BBData to_item = mAdapter.getItem(position);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("操作を選択");
		builder.setIcon(android.R.drawable.ic_menu_more);
		builder.setItems(mCommandList, new OnClickCommandListener(to_item));

		Dialog dialog = builder.create();
		dialog.setOwnerActivity(this);
		dialog.show();
		
		return true;
	}
	
	/**
	 * 詳細画面へ移動する。
	 * @param to_item 詳細画面で表示するデータ
	 */
	private void moveInfoActivity(BBData to_item) {
		Intent intent = new Intent(this, InfoActivity.class);
		IntentManager.setSelectedData(intent, to_item);
		startActivity(intent);
	}
	
	/**
	 * パーツ/武器の比較ダイアログを表示する。
	 * @param to_item 比較対象のパーツ/武器
	 */
	private void showCmpView(BBData to_item) {
		BBData base_item = IntentManager.getSelectedData(getIntent());
		
		if(BBDataManager.isParts(base_item)) {
			mCmpPartsDialog.showDialog(base_item, to_item);
		}
		else {
			mCmpWeaponDialog.showDialog(base_item, to_item);
		}
	}

	/**
	 * ソートキーの設定を行う。
	 */
	@Override
	public void onSelectItem(SortKeyDialog manager) {
		updateSort(manager);
	}

	/**
	 * ソート設定に応じてリストを更新する。
	 */
	private void updateSort(SortKeyDialog manager) {
		String sort_key = manager.getSortKey();
		mDataManager.setSortKey(sort_key);
		mDataManager.setASC(manager.getAsc());

		// ソートキー選択状態を記録する
		if(BBViewSetting.IS_MEMORY_SORT) {
			mSortKeyDialog.updateSetting();
		}

		// 表示項目の再設定を行う
		ArrayList<String> recent_key_list = mProperty.getShownKeys();
		ArrayList<String> new_key_list = new ArrayList<String>();

		int size = mSortKeys.length;
		for(int i=0; i<size; i++) {
			String key_buf = mSortKeys[i];
			if(recent_key_list.contains(key_buf)) {
				new_key_list.add(key_buf);
			}
			else if(sort_key.equals(key_buf)) {
				new_key_list.add(key_buf);
			}
		}

		mShownKeysDialog.set(sort_key, true);

		// 表示項目選択状態を記録する
		if(BBViewSetting.IS_MEMORY_SHOWSPEC) {
			mShownKeysDialog.updateSetting();
		}

		mProperty.setShownKeys(new_key_list);

		ArrayList<BBData> datalist = mDataManager.getList(mFilter, mIsSortTypeB);
		mAdapter.setList(datalist);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 表示項目の設定を行う。
	 */
	@Override
	public void onSelectItem(ShownKeysDialog manager) {
		mProperty.setShownKeys(manager.getShownKeys());

		mAdapter.notifyDataSetChanged();
		mExAdapter.notifyDataSetChanged();
	}

	/**
	 * パーツまたは武器に対する処理を選択した場合の処理を行うリスナー
	 */
	private class OnClickCommandListener implements DialogInterface.OnClickListener {
		private BBData mData;

		public OnClickCommandListener(BBData data) {
			mData = data;
		}

		@Override
		public void onClick(DialogInterface dialogInterface, int index) {
			executeCommand(mData, index);
		}
	}

	/**
	 * パーツまたは武器に対する処理を選択した場合の処理を行うリスナー
	 */
	private class OnClickControlPanelListener implements ControlPanel.OnExecuteListenerInterface {

		@Override
		public void onExecute(BBData data, int cmd_idx) {
			executeCommand(data, cmd_idx);
		}
	}

	/**
	 * コマンドボタン押下または操作選択ダイアログ選択時の処理を行う。
	 */
	public void executeCommand(BBData data, int cmd_idx) {
		if(cmd_idx == DIALOG_LIST_IDX_INFO) {
			moveInfoActivity(data);
		}
		else if(cmd_idx == DIALOG_LIST_IDX_CMP) {
			showCmpView(data);
		}
		else if(cmd_idx == DIALOG_LIST_IDX_FULL) {
			backCustomViewFullSetCheck(data);
		}
	}
	
	/**
	 * フィルタダイアログのOKボタンが押下された時の処理を行う。
	 */
	@Override
	public void onClickValueFilterButton() {
		mFilterManager.updateSetting(this);
		mFilter = mFilterManager.getFilter();
		
		updateList();
	}
	
	/**
	 * リストを更新する。
	 */
	private void updateList() {
		mFilter.setNotHavingShow(!mIsHavingOnly);
		
		ArrayList<BBData> datalist = mDataManager.getList(mFilter, mIsSortTypeB);
		mAdapter.setList(datalist);
		mAdapter.notifyDataSetChanged();
		
		mExAdapter.clearChildrenAll();
		mExAdapter.addChildren(datalist);
		mExAdapter.notifyDataSetChanged();

		if(datalist.size() == 0) {
			Toast.makeText(this, "条件に一致するパーツはありません。", Toast.LENGTH_SHORT).show();
		}
	}

}
