package hokage.kaede.gmail.com.BBViewLib.Android.CustomLib;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.IntentManager;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.CustomAdapter.CustomAdapterBaseItem;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomData;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomFileManager;
import hokage.kaede.gmail.com.BBViewPS4.Custom.SelectActivity;
import hokage.kaede.gmail.com.BBViewPS4.Custom.SelectChipActivity;

/**
 * 「アセン」画面のパーツの表示内容を生成するクラス。
 */
public class CustomAdapterItemChip implements CustomAdapterBaseItem {
	public String title;
	public String summary;
	public String type;

	private BBData mItem;
	private TextView mTitleTextView;
	private TextView mSummaryTextView;
	private Context mContext;
	private int mIndex;

	public CustomAdapterItemChip(Context context, BBData item, String summary, String type, int index) {
		this.title = item.get("名称");
		this.summary = summary;
		this.type = type;
		this.mContext = context;
		this.mItem = item;

		String file_dir = context.getFilesDir().toString();
		CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
		mIndex = index;
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
		Intent intent = new Intent(mContext, SelectChipActivity.class);
		
		intent.putExtra(SelectChipActivity.INTENTKEY_TITLENAME, type);
		intent.putExtra(SelectChipActivity.INTENTKEY_CHIPTYPE, type);
		intent.putExtra(SelectChipActivity.INTENTKEY_CHIPINDEX, mIndex);

		IntentManager.setSelectedData(intent, mItem);
		
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
