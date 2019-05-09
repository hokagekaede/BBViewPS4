package hokage.kaede.gmail.com.BBViewLib.Android.CustomLib;

import hokage.kaede.gmail.com.BBViewPS4.Custom.SelectActivity;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomData;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.CustomAdapter.CustomAdapterBaseItem;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.IntentManager;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomFileManager;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 「アセン」画面のパーツの表示内容を生成するクラス。
 */
public class CustomAdapterItemParts implements CustomAdapterBaseItem {
	public String title;
	public String summary;
	public String type;

	private BBData mItem;
	private TextView mTitleTextView;
	private TextView mSummaryTextView;
	private Context mContext;
	private CustomData mCustomData;
	
	public CustomAdapterItemParts(Context context, BBData item, String summary, String type) {
		this.title = item.get("名称");
		this.summary = summary;
		this.type = type;
		this.mContext = context;
		this.mItem = item;

		String file_dir = context.getFilesDir().toString();
		CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
		mCustomData = custom_mng.getCacheData();
	}

	@Override
	public LinearLayout createView(Context context) {
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.LEFT);
		
		mTitleTextView = new TextView(context);
		layout.addView(mTitleTextView);

		mSummaryTextView = new TextView(context);
		if(BBViewSetting.IS_SHOW_SPECLABEL) {
			layout.addView(mSummaryTextView);
		}

		updateView();
		
		return layout;
	}

	@Override
	public void updateView() {

		if(BBViewSetting.IS_SHOW_SPECLABEL) {
			mTitleTextView.setPadding(25, 10, 0, 0);
		}
		else {
			mTitleTextView.setPadding(25, 10, 0, 10);
		}
		mTitleTextView.setGravity(Gravity.LEFT);
		mTitleTextView.setText(title);
		mTitleTextView.setTextSize(BBViewSetting.getTextSize(mContext, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		
		mSummaryTextView.setPadding(25, 0, 0, 10);
		mSummaryTextView.setGravity(Gravity.LEFT);
		mSummaryTextView.setText(summary);
		mSummaryTextView.setTextSize(BBViewSetting.getTextSize(mContext, BBViewSetting.FLAG_TEXTSIZE_SMALL));
	}

	@Override
	public void click() {
		Intent intent = new Intent(mContext, SelectActivity.class);
		
		intent.putExtra(SelectActivity.INTENTKEY_TITLENAME, type);
		intent.putExtra(SelectActivity.INTENTKEY_PARTSTYPE, type);
		
		BBData select_item = mCustomData.getParts(type);
		IntentManager.setSelectedData(intent, select_item);
		
		mContext.startActivity(intent);
	}

	/**
	 * パーツデータを取得する。
	 * @return パーツデータ
	 */
	@Override
	public BBData getItem() {
		return mItem;
	}
}
