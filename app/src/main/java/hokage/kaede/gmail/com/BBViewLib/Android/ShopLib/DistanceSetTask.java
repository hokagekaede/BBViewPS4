package hokage.kaede.gmail.com.BBViewLib.Android.ShopLib;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;
import hokage.kaede.gmail.com.BBViewLib.Android.ShopLib.ShopDatabase.UpdateProgressListener;

/**
 * 指定位置から指定距離内にある店舗を検索するクラス。
 */
public class DistanceSetTask extends AsyncTask<Object, String, Object> {
	private Context mContext;
	private ProgressDialog mDialog;
	private Address mAddress;
	private OnEndTaskListener mEndTask;
	private int mDistance = 1000;

	/**
	 * 初期化処理を行う。
	 */
	public DistanceSetTask(Context context, Address address, OnEndTaskListener task) {
		mContext = context;
		mDialog = null;
		mAddress = address;
		mEndTask = task;
	}
	
	/**
	 * 指定位置からの距離を設定する。
	 * @param value 設定する距離
	 */
	public void setDistance(int value) {
		mDistance = value;
	}

	/**
	 * フォアグラウンドの処理を行う。
	 * 店舗データ取得中のダイアログを表示する。
	 */
	@Override
	protected void onPreExecute() {
		mDialog = new ProgressDialog(mContext);
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setMessage("検索中");
		mDialog.setCancelable(false);
		mDialog.show();
	}

	/**
	 * 処理中に表示するダイアログメッセージを更新する。
	 */
	@Override
	protected void onProgressUpdate(String... values) {
		mDialog.setMessage(values[0]);
	}
	
	/**
	 * バックグラウンドの処理を行う。
	 * 店舗データを公式サイトから取得する。
	 */
	@Override
	protected Object doInBackground(Object... arg0) {
		ShopDatabase shop_database = ShopDatabase.getShopDatabase();
		shop_database.setOnUpdateProgressListener(new setOnUpdateListener());
		shop_database.setDistance(mContext, mAddress, mDistance);
		
		return null;
	}
	
	/**
	 * ダイアログの進捗情報を更新する処理を行うリスナー。
	 */
	private class setOnUpdateListener implements UpdateProgressListener {

		@Override
		public void Update(int size, int finish) {
			
			if(size > 0) {
				double rate = (double)finish / (double)size * 100.0;
				publishProgress("検索中 [" + finish + "/" + size + " (" + (int)(rate) + "%)]");
			}
		}
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
		
		if(mEndTask != null) {
			mEndTask.onEndTask();
		}
	}
	
	/**
	 * 処理が終了したときに実行するイベントリスナー
	 */
	public static interface OnEndTaskListener {
		public void onEndTask();
	}
}
