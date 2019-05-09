package hokage.kaede.gmail.com.BBViewLib.Android.CustomLib;

import hokage.kaede.gmail.com.StandardLib.Android.PreferenceIO;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;

/**
 * 「パーツ武器選択」画面の表示項目選択設定ダイアログを管理するクラス。
 */
public class ShownKeysDialog implements OnClickListener, OnMultiChoiceClickListener, OnCancelListener {
	
	private Activity mActivity;
	private String[] mKeys;
	private boolean[] mFlags;
	private boolean[] mFlagsBefore;
	private OnOKClickListener mListener;
	private String mSaveKey;
	
	/**
	 * 初期化を行う。
	 */
	public ShownKeysDialog(Activity activity, ArrayList<String> keys) {
		mActivity = activity;
		mListener = null;
		
		keys.remove("名称");
		int size = keys.size();
				
		mKeys = new String[size];
		mFlags = new boolean[size];
		mFlagsBefore = new boolean[size];
		
		for(int i=0; i<size; i++) {
			mKeys[i] = keys.get(i);
			mFlags[i] = false;
			mFlagsBefore[i] = false;
		}
	}
	
	/**
	 * ボタン押下時の処理を設定する。
	 * @param listener
	 */
	public void setOnButtonClickListener(OnOKClickListener listener) {
		mListener = listener;
	}

	/**
	 * 表示項目を設定するダイアログを表示する。
	 */
	public void showDialog() {
		mFlagsBefore = mFlags;    /* 選択状態を一時保存 */
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("表示項目設定");
		builder.setIcon(android.R.drawable.ic_menu_more);
		builder.setMultiChoiceItems(mKeys, mFlags, this);
		builder.setPositiveButton("OK", this);
		builder.setOnCancelListener(this);
		
		Dialog dialog = builder.create();
		dialog.setOwnerActivity(mActivity);
		dialog.show();
	}
	
	/**
	 * 指定のキーの表示状態を設定する。
	 * 指定のキーが存在しない場合は何もしない。
	 * @param key 変更対象のキー
	 * @param is_show 表示設定。表示する場合はtrueを設定し、非表示にする場合はfalseを設定する。
	 */
	public void set(String key, boolean is_show) {
		int size = mKeys.length;
		for(int i=0; i<size; i++) {
			if(key.equals(mKeys[i])) {
				mFlags[i] = is_show;
				return;
			}
		}
	}
	
	/**
	 * 表示するキーのリストを取得する。
	 * @return
	 */
	public ArrayList<String> getShownKeys() {
		ArrayList<String> shown_keys = new ArrayList<String>();
		
		int size = mFlags.length;
		for(int i=0; i<size; i++) {
			if(mFlags[i]) {
				shown_keys.add(mKeys[i]);
			}
		}
		
		return shown_keys;
	}

	/**
	 * ダイアログに設置したOKボタンタップ時の処理を行う。
	 */
	@Override
	public void onClick(DialogInterface dialog, int which) {
		mFlagsBefore = mFlags;
		updateSetting();
		
		if(mListener != null) {
			mListener.onSelectItem(this);
		}
	}

	/**
	 * ダイアログをキャンセルした場合の処理を行う。
	 */
	@Override
	public void onCancel(DialogInterface arg0) {
		mFlags = mFlagsBefore;
	}

	/**
	 * データ保存のキーを設定する。
	 * @param save_key
	 */
	public void setSaveKey(String save_key) {
		mSaveKey = save_key;
	}
	
	/**
	 * 表示項目設定を保存する。
	 */
	public void updateSetting() {
		String key = ShownKeysDialog.class.getSimpleName() + "/" + mSaveKey;
		ArrayList<String> shown_keys = getShownKeys();
		PreferenceIO.writeStringList(mActivity, key, shown_keys);
	}
	
	/**
	 * 表示項目設定をロードする。
	 */
	public void loadSetting() {
		String key = ShownKeysDialog.class.getSimpleName() + "/" + mSaveKey;
		ArrayList<String> shown_keys = PreferenceIO.readStringList(mActivity, key);
		if(shown_keys == null) {
			return;
		}
		
		int size = mKeys.length;
		for(int i=0; i<size; i++) {
			if(shown_keys.contains(mKeys[i])) {
				mFlags[i] = true;
			}
			else {
				mFlags[i] = false;
			}
		}
	}
	
	/**
	 * 項目タップ時の処理を行う。
	 */
	@Override
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		mFlags[which] = isChecked;
	}

	/**
	 * OKボタンタップ時のリスナー
	 */
	public interface OnOKClickListener {
		public void onSelectItem(ShownKeysDialog manager);
	}

}
