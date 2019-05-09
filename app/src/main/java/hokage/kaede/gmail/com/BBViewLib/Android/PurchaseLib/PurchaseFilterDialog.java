package hokage.kaede.gmail.com.BBViewLib.Android.PurchaseLib;

import hokage.kaede.gmail.com.BBViewLib.Java.BBDataFilter;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;

/**
 * 「購入リスト追加」画面のアイテム一覧を生成するクラス。
 */
public class PurchaseFilterDialog implements OnClickListener, OnMultiChoiceClickListener {
	private Activity mActivity;
	private BBDataFilter mFilter;
	private OnOKFilterDialogListener mListener;
	
	private static final String[] FILTER_ITEMS = {
		BBDataManager.BLUST_PARTS_HEAD,
		BBDataManager.BLUST_PARTS_BODY,
		BBDataManager.BLUST_PARTS_ARMS,
		BBDataManager.BLUST_PARTS_LEGS,
		BBDataManager.BLUST_TYPE_ASSALT,
		BBDataManager.BLUST_TYPE_HEAVY,
		BBDataManager.BLUST_TYPE_SNIPER,
		BBDataManager.BLUST_TYPE_SUPPORT,
		BBDataManager.WEAPON_TYPE_MAIN,
		BBDataManager.WEAPON_TYPE_SUB,
		BBDataManager.WEAPON_TYPE_SUPPORT,
		BBDataManager.WEAPON_TYPE_SPECIAL
	};
	private boolean[] mFlags;
	private boolean[] mSelectFlags;
	
	private static int PARTS_END_IDX = 4;
	private static int BLUST_END_IDX = 8;
	private static int WEAPON_END_IDX = 12;
	
	/**
	 * 初期化を行う。
	 * @param activity
	 * @param filter
	 */
	public PurchaseFilterDialog(Activity activity, BBDataFilter filter) {
		mActivity = activity;
		mFilter = filter;
		mListener = null;
		mFlags = new boolean[FILTER_ITEMS.length];
		mSelectFlags = new boolean[FILTER_ITEMS.length];
	}
	
	/**
	 * フィルタを取得する
	 * @return
	 */
	public BBDataFilter getFilter() {
		return mFilter;
	}
	
	/**
	 * フィルタ設定時に行う処理を設定する。
	 * @param listener
	 */
	public void setOnOKFilterDialogListener(OnOKFilterDialogListener listener) {
		mListener = listener;
	}
	
	/**
	 * フィルタ項目の選択用ダイアログを表示する。
	 */
	public void showDialog() {
		System.arraycopy(mFlags, 0, mSelectFlags, 0, mFlags.length);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("フィルタ設定");
		builder.setIcon(android.R.drawable.ic_menu_more);
		builder.setMultiChoiceItems(FILTER_ITEMS, mSelectFlags, this);
		builder.setPositiveButton("OK", this);
		
		Dialog dialog = builder.create();
		dialog.setOwnerActivity(mActivity);
		dialog.show();
	}
	
	
	/**
	 * ダイアログのOKボタンタップ時の処理を行う。
	 */
	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		System.arraycopy(mSelectFlags, 0, mFlags, 0, mFlags.length);
		mFilter.clear();
		
		int size = mSelectFlags.length;
		for(int i=0; i<size; i++) {
			if(mSelectFlags[i]) {
				if(i<PARTS_END_IDX) {
					mFilter.setPartsType(FILTER_ITEMS[i]);
				}
				else if(i<BLUST_END_IDX) {
					mFilter.setBlustType(FILTER_ITEMS[i]);
				}
				else if(i<WEAPON_END_IDX) {
					mFilter.setWeaponType(FILTER_ITEMS[i]);
				}
			}
		}
		
		if(mListener != null) {
			mListener.onOKFilterDialog();
		}
		
		arg0.cancel();
	}

	/**
	 * ダイアログのOKボタンタップ時のリスナーインターフェース
	 */
	public interface OnOKFilterDialogListener {
		public void onOKFilterDialog();
	}

	@Override
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		mSelectFlags[which] = isChecked;
	}

}
