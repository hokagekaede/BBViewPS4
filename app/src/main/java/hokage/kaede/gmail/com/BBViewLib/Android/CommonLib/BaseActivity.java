package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;

/**
 * BBViewの各画面のベースとなるクラス。
 */
public abstract class BaseActivity extends Activity {
	
	/**
	 * 商標を示す文字列
	 */
	private static final String TRADEMARK_TEXT = "©SEGA";
	
	/**
	 * 画面を生成する。
	 */
	@Override
	public void setContentView(View view) {
		createLayout(view);
	}
	
	/**
	 * 通常時のベースレイアウトを生成する。
	 * @param view
	 */
	private void createLayout(View view) {

		// テーマを設定する
		setTheme();
		
		LinearLayout main_layout = new LinearLayout(this);
		main_layout.setOrientation(LinearLayout.VERTICAL);
		main_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
		
		view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
		
		// 既存のコード
		TextView sega_text_view = new TextView(this);
		sega_text_view.setText(TRADEMARK_TEXT);
		sega_text_view.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		
		main_layout.addView(view);
		main_layout.addView(sega_text_view);

		super.setContentView(main_layout);
	}
	
	// バージョンアップ前のレイアウトを生成する
	/*
	private static final int VER_VIEW_ID = 1000;
	
	private void createLayout(View view) {

		// テーマを設定する
		setTheme();
		
		LinearLayout main_layout = new LinearLayout(this);
		main_layout.setOrientation(LinearLayout.VERTICAL);
		main_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
		
		view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));

		FrameLayout bottom_layout = new FrameLayout(this);
		
		TextView sega_text_view = new TextView(this);
		sega_text_view.setText(TRADEMARK_TEXT);
		sega_text_view.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		bottom_layout.addView(sega_text_view);
		
		String ver_text_str = "";
		if(BBViewSetting.IS_NEXT_VERSION_ON) {
			ver_text_str = BBViewSetting.NEXT_VERSION_TITLE + " Data = ON";
		}
		else {
			ver_text_str = BBViewSetting.NEXT_VERSION_TITLE + " Data = OFF";
		}
		
		TextView ver_text_view = new TextView(this);
		ver_text_view.setText(ver_text_str);
		ver_text_view.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
		ver_text_view.setId(VER_VIEW_ID);
		bottom_layout.addView(ver_text_view);
		
		main_layout.addView(view);
		main_layout.addView(bottom_layout);
		
		super.setContentView(main_layout);
	}
	
	public void updateVer() {
		TextView ver_text_view = (TextView)this.findViewById(VER_VIEW_ID);

		String ver_text_str = "";
		if(BBViewSetting.IS_NEXT_VERSION_ON) {
			ver_text_str = BBViewSetting.NEXT_VERSION_TITLE + " Data = ON";
		}
		else {
			ver_text_str = BBViewSetting.NEXT_VERSION_TITLE + " Data = OFF";
		}
		
		ver_text_view.setText(ver_text_str);
	}
	*/

	private void setTheme() {
		int res_id = BBViewSetting.sThemeID;
		
		if(res_id != BBViewSetting.THEME_DEFAULT_ID) {
			setTheme(res_id);
		}
	}
}
