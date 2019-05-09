package hokage.kaede.gmail.com.BBViewPS4.Item;

import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BaseActivity;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 「カテゴリ選択」画面を表示するクラス。
 */
public class CategoryListActivity extends BaseActivity implements OnClickListener {
	
	private static final int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
	private static final int FP = LinearLayout.LayoutParams.FILL_PARENT;	

	/**
	 * アプリ起動時の処理を行う。
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ScrollView sv = new ScrollView(this);
		sv.setLayoutParams(new LinearLayout.LayoutParams(FP, FP));
		
		// レイアウトの生成
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.LEFT | Gravity.TOP);
		layout.setLayoutParams(new LinearLayout.LayoutParams(FP, FP));
		sv.addView(layout);
		
		// パーツボタンを生成
		TextView parts_text = new TextView(this);
		parts_text.setText("機体パーツ");
		parts_text.setTextColor(SettingManager.getColorWhite());
		parts_text.setTextSize(BBViewSetting.getTextSize(this, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		layout.addView(parts_text);

		LinearLayout parts_layout = new LinearLayout(this);
		parts_layout.setOrientation(LinearLayout.HORIZONTAL);
		parts_layout.setGravity(Gravity.LEFT | Gravity.TOP);
		parts_layout.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));
		
		int parts_size = BBDataManager.BLUST_PARTS_LIST.length;
		
		for(int i=0; i<parts_size; i++) {
			Button parts_btn = new Button(this);
			parts_btn.setText(BBDataManager.BLUST_PARTS_LIST[i]);
			parts_btn.setTag("");
			parts_btn.setLayoutParams(new LinearLayout.LayoutParams(FP, WC, 1));
			parts_btn.setOnClickListener(this);
			parts_layout.addView(parts_btn);
		}

		layout.addView(parts_layout);
		
		// 武器ボタンを生成
		int blust_size = BBDataManager.BLUST_TYPE_LIST.length;
		
		for(int i=0; i<blust_size; i++) {
			TextView weapon_text = new TextView(this);
			weapon_text.setText(BBDataManager.BLUST_TYPE_LIST[i]);
			weapon_text.setTextColor(SettingManager.getColorWhite());
			weapon_text.setTextSize(BBViewSetting.getTextSize(this, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
			layout.addView(weapon_text);
			
			LinearLayout weapon_layout = new LinearLayout(this);
			weapon_layout.setOrientation(LinearLayout.HORIZONTAL);
			weapon_layout.setGravity(Gravity.LEFT | Gravity.TOP);
			weapon_layout.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));

			int weapon_size = BBDataManager.WEAPON_TYPE_LIST.length;

			for(int j=0; j<weapon_size; j++) {
				Button weapon_btn = new Button(this);
				weapon_btn.setText(BBDataManager.WEAPON_TYPE_LIST[j]);
				weapon_btn.setTag(BBDataManager.BLUST_TYPE_LIST[i]);
				weapon_btn.setLayoutParams(new LinearLayout.LayoutParams(FP, WC, 1));
				weapon_btn.setOnClickListener(this);
				weapon_layout.addView(weapon_btn);
			}
			
			layout.addView(weapon_layout);
		};
		
		// その他ボタンを生成
		TextView others_text = new TextView(this);
		others_text.setText("その他");
		others_text.setTextColor(SettingManager.getColorWhite());
		others_text.setTextSize(BBViewSetting.getTextSize(this, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		layout.addView(others_text);

		LinearLayout others_layout = new LinearLayout(this);
		others_layout.setOrientation(LinearLayout.HORIZONTAL);
		others_layout.setGravity(Gravity.LEFT | Gravity.TOP);
		others_layout.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));

		// チップ
		Button chip_btn = new Button(this);
		chip_btn.setText(BBDataManager.CHIP_STR);
		chip_btn.setTag("");
		chip_btn.setLayoutParams(new LinearLayout.LayoutParams(FP, WC, 1));
		chip_btn.setOnClickListener(this);
		others_layout.addView(chip_btn);
		
		layout.addView(others_layout);

		setContentView(sv);
	}

	/**
	 * 各ボタンを押下した時の処理を行う。
	 */
	@Override
	public void onClick(View v) {
		if(v instanceof Button) {
			String btn_text = ((Button)v).getText().toString();
			String tag_text = ((Button)v).getTag().toString();

			Intent intent = new Intent(this, BBDataListActivity.class);

			if(tag_text.equals("")) {
				intent.putExtra(BBDataListActivity.INTENTEY_FILTER_MAIN, btn_text);
				intent.putExtra(BBDataListActivity.INTENTEY_FILTER_SUB, "");
			}
			else {
				intent.putExtra(BBDataListActivity.INTENTEY_FILTER_MAIN, tag_text);
				intent.putExtra(BBDataListActivity.INTENTEY_FILTER_SUB, btn_text);
			}

			startActivity(intent);
		}
	}
}
