package hokage.kaede.gmail.com.BBViewLib.Android.ShopLib;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class ShopDataTask extends AsyncTask<Object, Integer, Object> {
	private Activity mActivity;
	private ProgressDialog mDialog;
	private boolean mIsSuccess;
	
	/**
	 * 初期化処理を行う。
	 * @param activity タスクを実行しているアクティビティ
	 */
	public ShopDataTask(Activity activity) {
		this.mActivity = activity;
		this.mDialog = null;
		this.mIsSuccess = false;
	}

	/**
	 * フォアグラウンドの処理を行う。
	 * 店舗データ取得中のダイアログを表示する。
	 */
	@Override
	protected void onPreExecute() {
		mDialog = new ProgressDialog(mActivity);
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setMessage("店舗データ取得中");
		mDialog.setCancelable(false);
		mDialog.setOwnerActivity(mActivity);
		mDialog.show();
	}
	
	/**
	 * バックグラウンドの処理を行う。
	 * 店舗データを公式サイトから取得する。
	 */
	@Override
	protected Object doInBackground(Object... arg0) {
		ShopDatabase location_manager = ShopDatabase.getShopDatabase();
		mIsSuccess = location_manager.readWebData();

		return null;
	}
	
	/**
	 * 終了処理を行う。
	 * 次画面への遷移、または店舗情報取得失敗ダイアログを表示する。
	 */
	@Override
	protected void onPostExecute(Object arg0) {

		if(mDialog != null) {
			try {
				mDialog.dismiss();
			
			} catch(IllegalArgumentException e) {
				// Do Nothing
			}
		}

		if(!mIsSuccess) {
			Toast.makeText(mActivity, "店舗情報を取得できませんでした。", Toast.LENGTH_SHORT).show();
		}
	}
}
