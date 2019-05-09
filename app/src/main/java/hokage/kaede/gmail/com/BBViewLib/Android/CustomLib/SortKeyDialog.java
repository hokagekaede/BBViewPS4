package hokage.kaede.gmail.com.BBViewLib.Android.CustomLib;

import hokage.kaede.gmail.com.StandardLib.Android.PreferenceIO;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

/**
 * 「パーツ武器選択」画面のソート設定ダイアログを管理するクラス。
 */
public class SortKeyDialog implements OnClickListener, OnCheckedChangeListener {
	private Activity mActivity;
	private String mSortKey;
	private String[] mTargetKeys;
	private OnSelectItemListener mListener;
	
	/**
	 * 昇順か降順かを保持する。trueは昇順を示し、falseを降順を示す。
	 */
	private boolean mIsAsc;

	private String mSaveSpecKey;
	private String mSaveAscKey;
	
	private static final int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
	private static final int FP = LinearLayout.LayoutParams.FILL_PARENT;
	
	private static final String RESET_SORT_KEY_TAG = "ソート解除";
	
	private Dialog mDialog;
	
	/**
	 * 初期化処理を行う。
	 */
	public SortKeyDialog(Activity activity, ArrayList<String> target_keys) {
		mActivity = activity;
		
		int size = target_keys.size();
		mTargetKeys = new String[size + 1];
		mTargetKeys[0] = RESET_SORT_KEY_TAG;
		for(int i=0; i<size; i++) {
			mTargetKeys[i+1] = target_keys.get(i);
		}
		
		mSortKey = "";
		mListener = null;
		mIsAsc = true;
	}
	
	/**
	 * ソートキーを取得する。
	 */
	public String getSortKey() {
		return mSortKey;
	}

	/**
	 * ソートキーを設定する。
	 * @param sort_key
	 */
	public void setSortKey(String sort_key) {
		mSortKey = sort_key;
	}
	
	/**
	 * 昇順か降順かの設定値を取得する。
	 * @return 昇順の場合はtrueを返し、降順の場合はfalseを返す。
	 */
	public boolean getAsc() {
		return mIsAsc;
	}
	
	/**
	 * 昇順/降順の値を設定する。
	 * @param is_asc
	 */
	public void setAsc(boolean is_asc) {
		mIsAsc = is_asc;
	}
	
	/**
	 * ソートキー選択時に実行するリスナーを設定する。
	 * @param listener
	 */
	public void setSelectItemListener(OnSelectItemListener listener) {
		this.mListener = listener;
	}

	/**
	 * ソートキー選択ダイアログを表示する。
	 */
	public void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("ソート設定");
		builder.setIcon(android.R.drawable.ic_menu_more);
		builder.setSingleChoiceItems(mTargetKeys, getSortKeyIdx(), this);
		
		RadioGroup sort_group = new RadioGroup(mActivity);
		sort_group.setOrientation(LinearLayout.HORIZONTAL);
		sort_group.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));

		mDialog = builder.create();
		mDialog.setOwnerActivity(mActivity);
		mDialog.show();
	}
	
	/**
	 * ソートキーのIDを取得する。
	 * @return
	 */
	private int getSortKeyIdx() {
		int ret = 0;
		int size = mTargetKeys.length;
		
		for(int i=0; i<size; i++) {
			if(mSortKey.equals(mTargetKeys[i])) {
				ret = i;
			}
		}
		
		return ret;
	}

	/**
	 * OKボタンタップ時の処理を行う。
	 */
	@Override
	public void onClick(DialogInterface dialog, int which) {
		mSortKey = mTargetKeys[which];
		
		if(mSortKey.equals(RESET_SORT_KEY_TAG)) {
			mSortKey = "";
		}
		
		if(mListener != null) {
			mListener.onSelectItem(this);
		}
		
		dialog.cancel();
	}
	
	/**
	 * 昇順/降順のラジオボタンが変更された場合の処理を行う。
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		mIsAsc = isChecked;
	}

	/**
	 * OKボタンタップ時に動作するリスナー
	 */
	public interface OnSelectItemListener {
		public void onSelectItem(SortKeyDialog manager);
	}

	/**
	 * データ保存のキーを設定する。
	 * @param save_key
	 */
	public void setSaveKey(String save_key) {
		mSaveSpecKey = SortKeyDialog.class.getSimpleName() + "/" + save_key + ":spec";
		mSaveAscKey = SortKeyDialog.class.getSimpleName() + "/" + save_key + ":asc";
	}
	
	/**
	 * ソート設定を保存する。
	 */
	public void updateSetting() {
		PreferenceIO.write(mActivity, mSaveSpecKey, mSortKey);
		PreferenceIO.write(mActivity, mSaveAscKey, mIsAsc);
	}
	
	/**
	 * ソート設定をロードする。
	 */
	public void loadSetting() {
		mSortKey = PreferenceIO.readString(mActivity, mSaveSpecKey, "名称");
		mIsAsc = PreferenceIO.read(mActivity, mSaveAscKey, true);
	}
}
