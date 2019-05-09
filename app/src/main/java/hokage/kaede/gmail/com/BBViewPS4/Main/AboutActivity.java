package hokage.kaede.gmail.com.BBViewPS4.Main;

import hokage.kaede.gmail.com.BBViewPS4.R;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BaseActivity;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;
import hokage.kaede.gmail.com.StandardLib.Java.FileIO;

import java.io.InputStream;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 「BBViewについて」画面を表示するクラス。
 */
public class AboutActivity extends BaseActivity
{
	private static final int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
	private static final int FP = LinearLayout.LayoutParams.FILL_PARENT;

	/**
	 * 画面生成時の処理を行う。
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// aboutデータの読み込み
		Resources res = this.getResources();
		InputStream is = res.openRawResource(R.raw.about_text);
		String about_text = FileIO.readInputStream(is, FileIO.ENCODE_UTF8);
		
		setTitle(getTitle() + " (本アプリについて)");
		
		// 全体レイアウト設定
		LinearLayout layout_all = new LinearLayout(this);
		layout_all.setLayoutParams(new LinearLayout.LayoutParams(FP, FP));
		layout_all.setOrientation(LinearLayout.VERTICAL);
		layout_all.setGravity(Gravity.TOP);
		
		TextView about_text_view = new TextView(this);
		about_text_view.setText(about_text);
		about_text_view.setTextColor(SettingManager.getColorWhite());
		about_text_view.setTextSize(BBViewSetting.getTextSize(this, BBViewSetting.FLAG_TEXTSIZE_SMALL));
		
		ScrollView sv = new ScrollView(this);
		sv.addView(about_text_view);
		sv.setLayoutParams(new LinearLayout.LayoutParams(FP, WC, 1));
		
		layout_all.addView(sv);
		
		
		setContentView(layout_all);
	}
}
