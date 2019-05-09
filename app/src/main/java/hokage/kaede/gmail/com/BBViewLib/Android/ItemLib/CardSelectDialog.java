package hokage.kaede.gmail.com.BBViewLib.Android.ItemLib;

import hokage.kaede.gmail.com.BBViewLib.Java.BBNetDatabase;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * 「カード選択」ダイアログを表示するクラス。
 */
public class CardSelectDialog implements OnClickListener {
	
	private Activity mActivity;
	private OnSelectCardListener mListener;
	
	public CardSelectDialog(Activity activity) {
		mActivity = activity;
	}
	
	public void show() {
		BBNetDatabase database = BBNetDatabase.getInstance();
		
		String[] card_names = (String[])(database.getValidCardNames()).toArray(new String[0]);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("カード選択");
		builder.setItems(card_names, this);
		
		Dialog dialog = builder.create();
		dialog.setOwnerActivity(mActivity);
		dialog.show();
	}
	
	public void setOnSelectCardListener(OnSelectCardListener listener) {
		mListener = listener;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(mListener!=null) {
			mListener.OnSelectCard(which);
		}
	}

	public interface OnSelectCardListener {
		public abstract void OnSelectCard(int card_index);
	}
}
