package hokage.kaede.gmail.com.BBViewPS4.Custom;

import hokage.kaede.gmail.com.BBViewPS4.Item.InfoActivity;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.ResistAdapter;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.ResistAdapterItem;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataFilter;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomData;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.ValueFilterDialog;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.ValueFilterDialog.OnClickValueFilterButtonListener;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.IntentManager;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomFileManager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 「耐性」画面を表示するクラス。
 */
public class ResistView extends LinearLayout implements OnClickValueFilterButtonListener, OnItemClickListener {
	
	private ValueFilterDialog mFilterManager;
	private BBDataFilter mFilter;
	private ResistAdapter mAdapter;
	
	private String mRecentAbsolute;
	private static final String ABSOLUTE_SHOT_KEY      = "射撃";
	private static final String ABSOLUTE_EXPLOSION_KEY = "爆発";
	private static final String ABSOLUTE_SLASH_KEY     = "近接";

	// 武器スペックをタイプB表示にするかどうか
	private boolean mIsShowTypeB = false;	

	public ResistView(Context context, boolean is_show_typeb) {
		super(context);

		mIsShowTypeB = is_show_typeb;
		
		// 各管理変数
		String file_dir = context.getFilesDir().toString();
		CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
		CustomData custom_data = custom_mng.getCacheData();

		// フィルタ設定ダイアログを初期化する
		ArrayList<String> key_list = new ArrayList<String>();
		key_list.add("威力");
		mFilter = new BBDataFilter();
		mFilterManager = new ValueFilterDialog(mFilter, key_list);
		mFilterManager.setOnClickValueFilterButtonListener(this);
		
		// アダプタの生成
		mAdapter = new ResistAdapter(context, custom_data, null);
		mAdapter.setShowTypeB(mIsShowTypeB);
		mRecentAbsolute = ABSOLUTE_SHOT_KEY;
		updateList();

		LinearLayout layout_all = new LinearLayout(context);
		layout_all.setOrientation(LinearLayout.VERTICAL);
		layout_all.setGravity(Gravity.LEFT | Gravity.TOP);
		
		ListView listview = new ListView(context);
		listview.setAdapter(mAdapter);
		listview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
		listview.setOnItemClickListener(this);

		LinearLayout layout_btm = new LinearLayout(context);
		layout_btm.setOrientation(LinearLayout.HORIZONTAL);
		layout_btm.setGravity(Gravity.CENTER | Gravity.TOP);
		
		Button set_shot_btn = new Button(context);
		set_shot_btn.setText("射撃武器");
		set_shot_btn.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		set_shot_btn.setOnClickListener(new OnSelectAbsoluteListener(ABSOLUTE_SHOT_KEY));
		
		Button set_explosion_btn = new Button(context);
		set_explosion_btn.setText("爆発武器");
		set_explosion_btn.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		set_explosion_btn.setOnClickListener(new OnSelectAbsoluteListener(ABSOLUTE_EXPLOSION_KEY));

		Button set_slash_btn = new Button(context);
		set_slash_btn.setText("近接武器");
		set_slash_btn.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		set_slash_btn.setOnClickListener(new OnSelectAbsoluteListener(ABSOLUTE_SLASH_KEY));
		
		layout_btm.addView(set_shot_btn);
		layout_btm.addView(set_explosion_btn);
		layout_btm.addView(set_slash_btn);
		
		layout_all.addView(listview);
		layout_all.addView(layout_btm);

		addView(layout_all);
	}

	/**
	 * リスト表示を更新する。
	 */
	private void updateList() {
		BBDataManager data_mng = BBDataManager.getInstance();

		mFilter.setBlustType(BBDataManager.BLUST_TYPE_LIST);
		mFilter.setWeaponType(BBDataManager.WEAPON_TYPE_LIST);
		mFilter.setValue("属性", mRecentAbsolute);
		ArrayList<BBData> weapons = data_mng.getList(mFilter);
		
		if(mRecentAbsolute.equals(ABSOLUTE_SHOT_KEY)) {
			mAdapter.setMode(ResistAdapterItem.MODE_SHOT);
		}
		else if(mRecentAbsolute.equals(ABSOLUTE_EXPLOSION_KEY)) {
			mAdapter.setMode(ResistAdapterItem.MODE_EXPLOSION);
		}
		else if(mRecentAbsolute.equals(ABSOLUTE_SLASH_KEY)) {
			mAdapter.setMode(ResistAdapterItem.MODE_SLASH);
		}
		
		mAdapter.setList(weapons);
		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 属性選択ボタンを押下した時の処理を行うリスナー。
	 */
	private class OnSelectAbsoluteListener implements OnClickListener {
		private String mAbsolute;
		
		public OnSelectAbsoluteListener(String absolute) {
			mAbsolute = absolute;
		}

		@Override
		public void onClick(View arg0) {
			mRecentAbsolute = mAbsolute;
			mFilter.clear();
			updateList();
		}
	}

	/**
	 * フィルタ設定ダイアログを表示する。
	 * @param activity オーナーアクティビティ
	 */
	public void showFilterDialog(Activity activity) {
		mFilterManager.showDialog(activity);
	}

	/**
	 * フィルタ設定を実行した場合の処理を行う。
	 */
	@Override
	public void onClickValueFilterButton() {
		updateList();
	}

	/**
	 * 武器リスト選択時の処理を行う。
	 */
	@Override
	public void onItemClick(AdapterView<?> list, View view, int position, long id) {
		BBData to_item = mAdapter.getItem(position);
		
		Context context = this.getContext();
		Intent intent = new Intent(context, InfoActivity.class);
		IntentManager.setSelectedData(intent, to_item);
		intent.putExtra(InfoActivity.INTENTKEY_SHOWMODE, InfoActivity.MODE_SIM);
		context.startActivity(intent);
	}
}
