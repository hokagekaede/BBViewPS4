package hokage.kaede.gmail.com.BBViewLib.Android.ShopLib;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;

/**
 * 「県別検索（県選択）」の都道府県一覧を生成するクラス。
 */
public class AreaAdapter extends BaseAdapter {
	private Context mContext;
	
	/**
	 * 初期化処理を行う。
	 * @param context リストを表示する画面のコンテキスト
	 */
	public AreaAdapter(Context context) {
		this.mContext = context;
	}

	/**
	 * リストの項目数を取得する
	 * @return 項目数
	 */
	@Override
	public int getCount() {
		return ShopDatabase.AREA_LIST.length;
	}

	/**
	 * 指定番号の地域データを取得する
	 * @param index 指定番号
	 * @return 地域データを返す。
	 * リスト範囲外の値が指定された場合、nullを返す。
	 */
	@Override
	public String getItem(int index) {
		if(index < 0 || index >= ShopDatabase.AREA_LIST.length) {
			return null;
		}
		
		return ShopDatabase.AREA_LIST[index];
	}

	/**
	 * 指定番号の地域データIDを取得する
	 * @param index 指定番号
	 * @return 常に0を返す。(使用しないため)
	 */
	@Override
	public long getItemId(int index)  {
		return 0;
	}
	
	/**
	 * 項目表示用のビューを取得する
	 * @param index 指定番号
	 * @param arg1 前回表示のビュー(再利用オブジェクト)
	 * @param arg2 前回表示のビューグループ
	 * @return 表示するビュー
	 */
	@Override
	public View getView(int index, View arg1, ViewGroup arg2) {
		TextView text = new TextView(mContext);
		text.setText(ShopDatabase.AREA_LIST[index]);
		text.setTextSize(24);
		text.setTextSize(BBViewSetting.getTextSize(mContext, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		text.setPadding(20, 20, 20, 20);
		
		return text;
	}
}
