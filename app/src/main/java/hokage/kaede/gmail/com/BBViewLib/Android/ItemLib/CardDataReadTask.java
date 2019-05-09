package hokage.kaede.gmail.com.BBViewLib.Android.ItemLib;

import hokage.kaede.gmail.com.BBViewLib.Java.BBNetPageParser;
import hokage.kaede.gmail.com.StandardLib.Android.LastErrorData;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

// 尚、毎日3:00～7:30までの時間帯はBB.NETのゲームデータ集計のため、サービスを一旦休止させていただいております。
// お客様にはご迷惑をお掛けして申し訳ありませんが、ご理解とご協力の程お願いいたします。

/**
 * 所持情報取得中のダイアログ表示と進捗状況を管理するクラス。
 */
public class CardDataReadTask extends AsyncTask<Object, String, Object> {
	private Context mContext;
	private ProgressDialog mDialog;
	private OnPostExecuteListener mListener;
	private String mUid;
	private String mPassword;
	private boolean mSuccess;
	
	private boolean mIsComplete;
	private int mMode;
	
	private int mCardIndex;
	
	public static final int MODE_GETCARDLIST = 0;
	public static final int MODE_GETCARDDATA = 1;

	public CardDataReadTask(Context context, String uid, String password) {
		mContext = context;
		mDialog = null;
		mListener = null;
		mUid = uid;
		mPassword = password;
		mSuccess = false;
		
		mMode = MODE_GETCARDLIST;
		mIsComplete = false;
		mCardIndex = 0;
	}
	
	public void setOnPostExecuteListener(OnPostExecuteListener listener) {
		mListener = listener;
	}
	
	public void setMode(int mode) {
		mMode = mode;
	}
	
	public void setCardIndex(int card_index) {
		mCardIndex = card_index;
	}
	
	/**
	 * スレッド前処理。
	 */
	@Override
	protected void onPreExecute() {
		mDialog = new ProgressDialog(mContext);
		mDialog.setTitle("Please wait");
		mDialog.setMessage("Loading data...");
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setCancelable(false);
		mDialog.show();
	}

	/**
	 * スレッド内処理。カードデータを取得する。
	 */
	@Override
	protected Object doInBackground(Object... arg0) {
		
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					if(mMode == MODE_GETCARDLIST) {
						mSuccess = BBNetPageParser.getCardList(mUid, mPassword);
					}
					else if(mMode == MODE_GETCARDDATA) {
						mSuccess = BBNetPageParser.parse(mUid, mPassword, mCardIndex);
					}
				} catch (Exception e) {
					LastErrorData.setPageString(BBNetPageParser.getPageString());
					LastErrorData.setDebugMode(true);
					LastErrorData.setException(e);
				}
				
				mIsComplete = true;
			}
		};
		
		thread.start();

		while(!mIsComplete) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// Do Nothing
			}
			
			publishProgress(BBNetPageParser.getProgressMessage());
		}
		
		return null;
	}
	
	/**
	 * 処理中に表示するダイアログメッセージを更新する。
	 */
	@Override
	protected void onProgressUpdate(String... values) {
		mDialog.setMessage("Loading data...\n(" + values[0] + "中...)");
	}
	
	/**
	 * スレッド処理完了後の処理。
	 */
	@Override
	protected void onPostExecute(Object result) {
		if(mDialog != null) {
			try {
				mDialog.dismiss();
				
			} catch(IllegalArgumentException e) {
				// Do Nothing
			}
		}
		
		if(!mSuccess) {
			String msg = BBNetPageParser.getProgressMessage() + "に失敗しました。";
			String error_msg = BBNetPageParser.getErrorMessage();
			
			if(!error_msg.equals("")) {
				msg = msg + "\n" + error_msg;
			}
			
			Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
			
			// エラー内容をメールで送信する
			if(LastErrorData.isDebugMode()) {
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:hokage.kaede@gmail.com"));
				intent.putExtra(Intent.EXTRA_SUBJECT, "BBViewUnionエラー報告");
				intent.putExtra(Intent.EXTRA_TEXT, "エラー内容：" + msg + "\n" + LastErrorData.getErrorMessage());
				mContext.startActivity(intent);
			}
		}
		
		if(mListener != null) {
			mListener.onPostExecute(mSuccess);
		}
	}
	
	/**
	 * カードデータ取得完了時のイベントリスナー
	 */
	public static interface OnPostExecuteListener {
		public void onPostExecute(boolean success);
	}

}
