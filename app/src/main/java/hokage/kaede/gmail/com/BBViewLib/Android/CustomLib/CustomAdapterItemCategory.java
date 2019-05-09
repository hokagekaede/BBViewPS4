package hokage.kaede.gmail.com.BBViewLib.Android.CustomLib;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.CustomAdapter.CustomAdapterBaseItem;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 「アセン」画面のカテゴリ(青背景部分)の表示内容を生成するクラス。
 */
public class CustomAdapterItemCategory implements CustomAdapterBaseItem {
	public String title;
	
	private TextView mTitleTextView;
	private Context mContext;
	
	public CustomAdapterItemCategory(Context context, String title) {
		this.title = title;
		this.mContext = context;
	}

	@Override
	public LinearLayout createView(Context context) {
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.LEFT);
		layout.setBackgroundColor(SettingManager.getColorBlue());
		
		mTitleTextView = new TextView(context);
		layout.addView(mTitleTextView);
		
		updateView();

		return layout;
	}

	@Override
	public void updateView() {
		mTitleTextView.setTextColor(SettingManager.getColorWhite());
		mTitleTextView.setPadding(5, 0, 0, 0);
		mTitleTextView.setGravity(Gravity.LEFT);
		mTitleTextView.setText(title);
		mTitleTextView.setTextSize(BBViewSetting.getTextSize(mContext, BBViewSetting.FLAG_TEXTSIZE_SMALL));
	}

	@Override
	public void click() {
		// Do Nothing
	}
	
	/**
	 * データを取得する。カテゴリ表示なのでデータ無し。
	 */
	@Override
	public BBData getItem() {
		return null;
	}
}

