package hokage.kaede.gmail.com.BBViewLib.Android.CustomLib;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomData;

/**
 * 「アセン」画面のアイテム一覧を生成するクラス。
 */
public class CustomAdapter extends BaseAdapter {
	private ArrayList<CustomAdapterBaseItem> mList;
	private Context mContext;

	public CustomAdapter(Context context) {
		mList = new ArrayList<CustomAdapterBaseItem>();
		mContext = context;
	}

	/**
	 * 対象のContextデータを取得する。
	 * @return Contextデータ
	 */
	public Context getContext() {
		return mContext;
	}

	@Override
	public int getCount() {
		return mList.size();
	}
	
	public void addItem(CustomAdapterBaseItem item) {
		mList.add(item);
	}
	
	@Override
	public CustomAdapterBaseItem getItem(int position) {
		return mList.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CustomAdapterBaseItem item = mList.get(position);
		return item.createView(mContext);
	}
	
	/**
	 * 表示インターフェース
	 */
	public static interface CustomAdapterBaseItem {
		public LinearLayout createView(Context context);
		public void updateView();
		public BBData getItem();
		public void click();
	}
}
