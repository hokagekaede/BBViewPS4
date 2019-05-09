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
 * 「アセン」画面の武器の表示内容を生成するクラス。
 */
public class CustomAdapterItemWeapon implements CustomAdapterBaseItem {
	public String title;
	public String summary;
	public String blust;
	public String type;
	
	private BBData mItem;
	private TextView mTitleTextView;
	private TextView mSummaryTextView;
	private Context mContext;
	private CustomData mCustomData;
	
	public CustomAdapterItemWeapon(Context context, BBData item, String blust, String type) {
		this.title = item.get("名称");
		this.summary = blust + ":" + type;
		this.blust = blust;
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
		if(BBViewSetting.IS_SHOW_TYPELABEL) {
			layout.addView(mSummaryTextView);
		}
		
		updateView();
		
		return layout;
	}

	@Override
	public void updateView() {
		
		if(BBViewSetting.IS_SHOW_TYPELABEL) {
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
	
		String title = blust + "(" + type + ")";
		intent.putExtra(SelectActivity.INTENTKEY_TITLENAME, title);
		intent.putExtra(SelectActivity.INTENTKEY_BLUSTTYPE, blust);
		intent.putExtra(SelectActivity.INTENTKEY_WEAPONTYPE, type);

		BBData select_item = mCustomData.getWeapon(blust, type);
		IntentManager.setSelectedData(intent, select_item);
		
		mContext.startActivity(intent);
	}

	/**
	 * 武器データを取得する。
	 * @return 武器データ
	 */
	@Override
	public BBData getItem() {
		return mItem;
	}
}
