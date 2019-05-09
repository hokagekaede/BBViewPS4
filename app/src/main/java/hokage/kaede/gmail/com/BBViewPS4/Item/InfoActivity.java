package hokage.kaede.gmail.com.BBViewPS4.Item;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BaseActivity;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.IntentManager;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 「パーツ武器詳細」画面を表示するクラス。
 */
public class InfoActivity extends BaseActivity {
	// スペック表示画面のID
	private static final int VIEWID_SHOWSPEC  = 30000;
	private static final int VIEWID_WEAPONSIM = 30001;

	// オプションメニューのID
	private static final int MENU_ITEM0 = 0;
	private static final int MENU_ITEM1 = 1;

	// 表示モード
	public static final String INTENTKEY_SHOWMODE = "INTENTKEY_SHOWMODE";
	public static final int MODE_INFO = 0;
	public static final int MODE_SIM  = 1;
	
	// 表示するデータ
	private BBData mTargetData;
	
	private boolean isShowingTypeB = false;
	private boolean isShowingWeaponSim = false;
	
	
	/**
	 * アプリ起動時の処理を行う。
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// intentデータ読み込み
		Intent intent = getIntent();
		BBData data = IntentManager.getSelectedData(intent);
		if(intent.getIntExtra(INTENTKEY_SHOWMODE, MODE_INFO) == MODE_SIM) {
			isShowingWeaponSim = true;
		}
		
		setTitle(getTitle() + " (項目詳細情報)");

		// データがnullの場合はアクティビティを終了する
		if(data == null) {
			finish();
		}
		
		mTargetData = data;

		createView();
	}

	/**
	 * 画面全体の生成処理を行う。
	 */
	private void createView() {
		LinearLayout layout_all = new LinearLayout(this);
		layout_all.setOrientation(LinearLayout.VERTICAL);
		layout_all.setGravity(Gravity.LEFT | Gravity.TOP);

		InfoView info_view = new InfoView(this, mTargetData);
		info_view.setId(VIEWID_SHOWSPEC);

		WeaponSimView sim_view = new WeaponSimView(this, mTargetData);
		sim_view.setId(VIEWID_WEAPONSIM);
		
		if(isShowingWeaponSim) {
			info_view.setVisibility(View.GONE);
		}
		else {
			sim_view.setVisibility(View.GONE);
		}

		layout_all.addView(info_view);
		layout_all.addView(sim_view);

        if(BBDataManager.isParts(mTargetData)) {
            PartsSeriesView series_view = new PartsSeriesView(this, mTargetData);
			series_view.setPadding(0, 20, 0, 0);
            layout_all.addView(series_view);
        }

        // 全体レイアウトの画面表示
		setContentView(layout_all);
	} 

	/**
	 * オプションメニュー生成時の処理を行う。
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		if(mTargetData.getTypeB() != null) {
			MenuItem item = menu.add(0, MENU_ITEM0, 0, "タイプB表示");
			item.setCheckable(true);
		}
		
		if(mTargetData.isShotWeapon() || mTargetData.isExplosionWeapon()) {
			MenuItem item = menu.add(0, MENU_ITEM1, 0, "耐性シミュ表示");
			item.setCheckable(true);
			item.setChecked(isShowingWeaponSim);
		}

		return true;
	}

	/**
	 * オプションメニュータップ時の処理を行う。
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case MENU_ITEM0:
				changeShownWeaponType();
				item.setChecked(isShowingTypeB);
				break;
				
			case MENU_ITEM1:
				changeShownWeaponSim();
				item.setChecked(isShowingWeaponSim);
				break;
		}
		
		return true;
	}
	
	/**
	 * タイプA/タイプBの表示切り替えを行う。
	 */
	private void changeShownWeaponType() {
		InfoView info_view = (InfoView)(InfoActivity.this.findViewById(VIEWID_SHOWSPEC));
		WeaponSimView weapon_sim_view = (WeaponSimView)(InfoActivity.this.findViewById(VIEWID_WEAPONSIM));

		BBData data = mTargetData;
		
		if(isShowingTypeB) {
			isShowingTypeB = false;
			info_view.update(data);
			weapon_sim_view.setData(data);
		}
		else {
			isShowingTypeB = true;
			info_view.update(data.getTypeB());
			weapon_sim_view.setData(data.getTypeB());
		}
	}
	
	/**
	 * 武器シミュレータの表示切替を行う。
	 */
	private void changeShownWeaponSim() {
		InfoView info_view = (InfoView)(InfoActivity.this.findViewById(VIEWID_SHOWSPEC));
		WeaponSimView weapon_sim_view = (WeaponSimView)(InfoActivity.this.findViewById(VIEWID_WEAPONSIM));
		
		if(isShowingWeaponSim) {
			isShowingWeaponSim = false;
			info_view.setVisibility(View.VISIBLE);
			weapon_sim_view.setVisibility(View.GONE);
		}
		else {
			isShowingWeaponSim = true;
			info_view.setVisibility(View.GONE);
			weapon_sim_view.setVisibility(View.VISIBLE);
		}
	}
}
