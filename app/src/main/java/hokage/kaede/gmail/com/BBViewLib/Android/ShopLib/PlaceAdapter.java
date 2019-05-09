package hokage.kaede.gmail.com.BBViewLib.Android.ShopLib;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 店舗検索機能の各画面の店舗一覧を生成するクラス。
 */
public class PlaceAdapter extends BaseAdapter {
	private Context mContext;
	private ShopDatabase mShopDatabase;
	
	private boolean mIsDistance;
	
	public PlaceAdapter(Context context, ShopDatabase location_data) {
		mContext = context;
		mShopDatabase = location_data;
		mIsDistance = false;
	}
	
	@Override
	public int getCount() {
		return mShopDatabase.getCount();
	}

	@Override
	public ShopData getItem(int index) {
		return mShopDatabase.getItem(index);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	
	public void setDistanceFlag(boolean flag) {
		mIsDistance = flag;
	}

	@Override
	public View getView(int index, View arg1, ViewGroup arg2) {
		PlaceAdapterView view = null;
		
		if(arg1 == null) {
			view = new PlaceAdapterView(mContext);
		}
		else {
			view = (PlaceAdapterView)arg1;
		}
		
		ShopData shopdata = mShopDatabase.getItem(index);
		String text = shopdata.name;
		if(mIsDistance) {
			text = text + " (" + String.valueOf((int)shopdata.distance) + "m)";
		}
		
		view.setText(text, shopdata.address);
		
		return (View)view;
	}
	
	private class PlaceAdapterView extends LinearLayout {

		private TextView mMainTextView;
		private TextView mSubTextView;
		
		public PlaceAdapterView(Context context) {
			super(context);

			this.setOrientation(LinearLayout.VERTICAL);
			//this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1));
			this.setGravity(Gravity.LEFT | Gravity.TOP);
			
			mMainTextView = new TextView(context);
			mMainTextView.setTextSize(24);
			mMainTextView.setPadding(20, 20, 20, 0);
			this.addView(mMainTextView);
			
			mSubTextView = new TextView(context);
			mSubTextView.setTextSize(12);
			mSubTextView.setPadding(20, 0, 20, 20);
			this.addView(mSubTextView);
		}
		
		public void setText(String main, String sub) {
			mMainTextView.setText(main);
			mSubTextView.setText(sub);
		}
	}
}
