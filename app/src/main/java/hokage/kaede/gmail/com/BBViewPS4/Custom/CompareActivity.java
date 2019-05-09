package hokage.kaede.gmail.com.BBViewPS4.Custom;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomData;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BaseActivity;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.SpecArray;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.ViewBuilder;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomFileManager;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout.LayoutParams;

/**
 * 「アセン比較」画面を表示するクラス。
 */
public class CompareActivity extends BaseActivity {
	
	private static final int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
	private static final int FP = LinearLayout.LayoutParams.FILL_PARENT;

	// レイアウトID
	private static final int TOGGLE_BUTTON_ASSALT_ID  = 1000;
	private static final int TOGGLE_BUTTON_HEAVY_ID   = 2000;
	private static final int TOGGLE_BUTTON_SNIPER_ID  = 3000;
	private static final int TOGGLE_BUTTON_SUPPORT_ID = 4000;
	
	private static final int TABLELAYOUT_ID = 100;
	
	// モード設定値
	private int mMode = MODE_BASE;
	public static final int MODE_BASE    = 0;
	public static final int MODE_ASSALT  = 1;
	public static final int MODE_HEAVY   = 2;
	public static final int MODE_SNIPER  = 3;
	public static final int MODE_SUPPORT = 4;

	public static String[] MODE_NAME_LIST = {
		"",
		BBDataManager.BLUST_TYPE_ASSALT,
		BBDataManager.BLUST_TYPE_HEAVY,
		BBDataManager.BLUST_TYPE_SNIPER,
		BBDataManager.BLUST_TYPE_SUPPORT
	};

	// モード設定値に対する選択中の兵装名
	private String mBlustType = MODE_NAME_LIST[mMode];

	/**
	 * 比較対象のファイル名指定用のintentキー
	 */
	public static final String INTENTKEY_CMPTO_FILENAME = "INTENTKEY_CMPTO_FILENAME";

	private CustomData mCmpFmData;
	private CustomData mCmpToData;

	/**
	 * アプリ起動時の処理を行う。
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String file_dir = getFilesDir().toString();
		CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
		mCmpFmData = custom_mng.getCacheData();
		mCmpToData = getCmpToCustomData();
		
		if(mCmpFmData == null || mCmpToData == null) {
			Toast.makeText(this, "比較対象のデータに誤りがあります。", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		// アプリ画面の生成
		createView();
	}

	/**
	 * 比較対象のカスタムデータを読み込む。
	 * @return カスタムデータ。読み込みに失敗した場合はnullを返す。
	 */
	private CustomData getCmpToCustomData() {
		Intent intent = getIntent();
		String select_file_name = "";
		
		if(intent == null) {
			return null;
		}

		// intentからファイル名を読み込む
		select_file_name = intent.getStringExtra(INTENTKEY_CMPTO_FILENAME);
		if(select_file_name == null) {
			return null;
		}
		
		String file_dir = getFilesDir().toString();
		CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
		CustomData custom_data = custom_mng.read(select_file_name);
		custom_data.setSpeedUnit(BBViewSetting.IS_KM_PER_HOUR);
		
		return custom_data;
	}

	/**
	 * 画面の生成処理を行う。
	 */
	private void createView() {		
		LinearLayout main_layout = new LinearLayout(this);
		main_layout.setOrientation(LinearLayout.VERTICAL);
		main_layout.setGravity(Gravity.LEFT | Gravity.TOP);
		main_layout.setLayoutParams(new LinearLayout.LayoutParams(FP, FP));
		main_layout.addView(createSpecTable(this));		
		main_layout.addView(createBottomView(this));

		LinearLayout all_layout = new LinearLayout(this);
		all_layout.setLayoutParams(new FrameLayout.LayoutParams(FP, FP));
		all_layout.addView(main_layout);
		
		// 全体レイアウトの画面表示
		setContentView(all_layout);
	}

	/**
	 * 各種性能のビューを表示するためのビューを生成する。
	 * @return ビュー
	 */
	private View createSpecTable(Context context) {
		LinearLayout layout_table = new LinearLayout(context);
		layout_table.setOrientation(LinearLayout.VERTICAL);
		layout_table.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));
		layout_table.setId(TABLELAYOUT_ID);
		
		createSpecTableMain(context, layout_table);

		ScrollView data_view = new ScrollView(context);
		data_view.addView(layout_table);
		data_view.setLayoutParams(new LinearLayout.LayoutParams(FP, WC, 1));
		
		return data_view;
	}

	/**
	 * 各種性能のビューを表示するためのビューを更新する。
	 */
	private void updateSpecTable(Context context) {
		LinearLayout layout = (LinearLayout)this.findViewById(TABLELAYOUT_ID);
		layout.removeAllViews();
		
		createSpecTableMain(context, layout);
	}

	/**
	 * 各種性能のビューを表示するためのビューに対して、その中身を作る。
	 * 生成メソッドと構築メソッドが共通で使用する。
	 */
	private void createSpecTableMain(Context context, LinearLayout layout_table) {

		if(mMode == MODE_BASE) {
			layout_table.addView(AssembleViewBuilder.create(context, mCmpFmData, mCmpToData, ""));
			layout_table.addView(PartsSpecViewBuilder.create(context, mCmpFmData, mCmpToData, ""));

		}
		else {
			layout_table.addView(AssembleViewBuilder.create(context, mCmpFmData, mCmpToData, mBlustType));
			layout_table.addView(PartsSpecViewBuilder.create(context, mCmpFmData, mCmpToData, mBlustType));
			layout_table.addView(WeaponSpecViewBuilder.create(context, mCmpFmData, mCmpToData, mBlustType));
		}
	}
	
	//----------------------------------------------------------
	// 兵装選択トグルボタン関連の処理
	//----------------------------------------------------------
	
	/**
	 * 画面下部のボタンビューを生成する。
	 * @param context 対象の画面
	 * @return ビュー
	 */
	private LinearLayout createBottomView(Context context) {
		LinearLayout bottom_layout = new LinearLayout(context);
		bottom_layout.setOrientation(LinearLayout.HORIZONTAL);
		
		ChangeNothingListener nothing_listener = new ChangeNothingListener();

		ToggleButton assalt_button = new ToggleButton(context);
		assalt_button.setTextOn("強襲");
		assalt_button.setTextOff("強襲");
		assalt_button.setChecked(false);
		assalt_button.setId(TOGGLE_BUTTON_ASSALT_ID);
		assalt_button.setLayoutParams(new LayoutParams(WC, WC, 1));
		assalt_button.setOnClickListener(new ChangeBlustTypeListener(MODE_ASSALT));
		assalt_button.setOnCheckedChangeListener(nothing_listener);
		bottom_layout.addView(assalt_button);
		
		ToggleButton heavy_button = new ToggleButton(context);
		heavy_button.setTextOn("重火力");
		heavy_button.setTextOff("重火力");
		heavy_button.setChecked(false);
		heavy_button.setId(TOGGLE_BUTTON_HEAVY_ID);
		heavy_button.setLayoutParams(new LayoutParams(WC, WC, 1));
		heavy_button.setOnClickListener(new ChangeBlustTypeListener(MODE_HEAVY));
		heavy_button.setOnCheckedChangeListener(nothing_listener);
		bottom_layout.addView(heavy_button);
		
		ToggleButton sniper_button = new ToggleButton(context);
		sniper_button.setTextOn("遊撃");
		sniper_button.setTextOff("遊撃");
		sniper_button.setChecked(false);
		sniper_button.setId(TOGGLE_BUTTON_SNIPER_ID);
		sniper_button.setLayoutParams(new LayoutParams(WC, WC, 1));
		sniper_button.setOnClickListener(new ChangeBlustTypeListener(MODE_SNIPER));
		sniper_button.setOnCheckedChangeListener(nothing_listener);
		bottom_layout.addView(sniper_button);

		ToggleButton support_button = new ToggleButton(context);
		support_button.setTextOn("支援");
		support_button.setTextOff("支援");
		support_button.setChecked(false);
		support_button.setId(TOGGLE_BUTTON_SUPPORT_ID);
		support_button.setLayoutParams(new LayoutParams(WC, WC, 1));
		support_button.setOnClickListener(new ChangeBlustTypeListener(MODE_SUPPORT));
		support_button.setOnCheckedChangeListener(nothing_listener);
		bottom_layout.addView(support_button);

		if(mMode == MODE_ASSALT) {
			assalt_button.setChecked(true);
		}
		else if(mMode == MODE_HEAVY) {
			heavy_button.setChecked(true);
		}
		else if(mMode == MODE_SNIPER) {
			sniper_button.setChecked(true);
		}
		else if(mMode == MODE_SUPPORT) {
			support_button.setChecked(true);
		}
		
		return bottom_layout;
	}

	/**
	 * トグルボタン押下時の処理を行うリスナー。
	 */
	private class ChangeBlustTypeListener implements OnClickListener {
		
		private int mTargetMode;
		
		public ChangeBlustTypeListener(int mode) {
			mTargetMode = mode;
		}

		/**
		 * トグルボタン押下時の処理を行う。
		 * 兵装の選択状態を切り替える。
		 * 
		 * ■実装メモ
		 * 押下されたボタンの状態を保持した後、全てのトグルボタンをOFFにする。
		 * その後、保持値を反転させて押下されたボタンに設定する。
		 */
		@Override
		public void onClick(View view) {
			
			try {
				ToggleButton btn = (ToggleButton)view;
				boolean is_checked = btn.isChecked();

				if(is_checked) {
					mMode = mTargetMode;
				}
				else {
					mMode = MODE_BASE;
				}
				mBlustType = MODE_NAME_LIST[mMode];
				
				ToggleButton assalt_button = (ToggleButton)CompareActivity.this.findViewById(TOGGLE_BUTTON_ASSALT_ID);
				ToggleButton heavy_button = (ToggleButton)CompareActivity.this.findViewById(TOGGLE_BUTTON_HEAVY_ID);
				ToggleButton sniper_button = (ToggleButton)CompareActivity.this.findViewById(TOGGLE_BUTTON_SNIPER_ID);
				ToggleButton support_button = (ToggleButton)CompareActivity.this.findViewById(TOGGLE_BUTTON_SUPPORT_ID);
				
				assalt_button.setChecked(false);
				heavy_button.setChecked(false);
				sniper_button.setChecked(false);
				support_button.setChecked(false);
				
				btn.setChecked(is_checked);

			} catch(Exception e) {
				e.printStackTrace();

				mMode = MODE_BASE;
				mBlustType = MODE_NAME_LIST[mMode];
			}
			
			// 画面を更新する
			updateSpecTable(view.getContext());
		}
	}
	
	/**
	 * トグルボタンのチェックが変更された場合の処理を行うリスナー
	 */
	private class ChangeNothingListener implements OnCheckedChangeListener {

		/**
		 * トグルボタンのチェックが変更された場合の処理。
	     * 既存の処理の実行を防ぐため、本関数では何も処理を行わない。
		 */
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// Do Nothing
		}
	}

	//----------------------------------------------------------
	// 各画面のビューを生成するクラス群
	//----------------------------------------------------------
	
	/**
	 * 「アセン」のビューを生成するクラス
	 */
	private static class AssembleViewBuilder {
		
		private static View create(Context context, CustomData from_data, CustomData to_data, String blust_type) {
			int color = SettingManager.getColorWhite();
			int bg_color = SettingManager.getColorBlue();

			LinearLayout layout_table = new LinearLayout(context);
			layout_table.setOrientation(LinearLayout.VERTICAL);
			layout_table.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));

			TextView assemble_view = ViewBuilder.createTextView(context, "アセン", SettingManager.FLAG_TEXTSIZE_SMALL, color, bg_color);
			layout_table.addView(assemble_view);
			layout_table.addView(createAssembleView(context, from_data, to_data, blust_type));
			
			return layout_table;
		}

		/**
		 * 「アセン」のビューを生成する。
		 * @param context
		 * @param from_data
		 * @param to_data
		 * @param blust_type
		 * @return
		 */
		private static TableLayout createAssembleView(Context context, CustomData from_data, CustomData to_data, String blust_type) {
			TableLayout table = new TableLayout(context);
			table.setLayoutParams(new TableLayout.LayoutParams(FP, WC));
			
			if(blust_type.equals("")) {
				table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), "", "比較元", "比較先"));
				table.addView(createPartsRow(context, from_data, to_data, BBDataManager.BLUST_PARTS_HEAD));
				table.addView(createPartsRow(context, from_data, to_data, BBDataManager.BLUST_PARTS_BODY));
				table.addView(createPartsRow(context, from_data, to_data, BBDataManager.BLUST_PARTS_ARMS));
				table.addView(createPartsRow(context, from_data, to_data, BBDataManager.BLUST_PARTS_LEGS));
			}
			else {
				table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), "", "比較元", "比較先"));
				table.addView(createPartsRow(context, from_data, to_data, BBDataManager.BLUST_PARTS_HEAD));
				table.addView(createPartsRow(context, from_data, to_data, BBDataManager.BLUST_PARTS_BODY));
				table.addView(createPartsRow(context, from_data, to_data, BBDataManager.BLUST_PARTS_ARMS));
				table.addView(createPartsRow(context, from_data, to_data, BBDataManager.BLUST_PARTS_LEGS));

				table.addView(createWeaponRow(context, from_data, to_data, blust_type, BBDataManager.WEAPON_TYPE_MAIN));
				table.addView(createWeaponRow(context, from_data, to_data, blust_type, BBDataManager.WEAPON_TYPE_SUB));
				table.addView(createWeaponRow(context, from_data, to_data, blust_type, BBDataManager.WEAPON_TYPE_SUPPORT));
				table.addView(createWeaponRow(context, from_data, to_data, blust_type, BBDataManager.WEAPON_TYPE_SPECIAL));
			}

			return table;
		}

		/**
		 * パーツスペックテーブルの行を生成する。
		 * @param from_data 比較元のアセンデータ
		 * @param to_data 比較先のアセンデータ
		 * @param parts_type パーツの種類
		 * @return 指定のパーツ種類に対応する行
		 */
		private static TableRow createPartsRow(Context context, CustomData from_data, CustomData to_data, String parts_type) {
			int[] colors = {
					SettingManager.getColorYellow(),
					SettingManager.getColorWhite(),
					SettingManager.getColorWhite()
			};
			BBData from_parts = from_data.getParts(parts_type);
			BBData to_parts = to_data.getParts(parts_type);
			
			return ViewBuilder.createTableRow(context, colors, parts_type, from_parts.get("名称"), to_parts.get("名称"));
		}
		/**
		 * パーツスペックテーブルの行を生成する。
		 * @param from_data 比較元のアセンデータ
		 * @param to_data 比較先のアセンデータ
		 * @param blust_type 兵装名
		 * @param weapon_type 武器の種類
		 * @return 指定のパーツ種類に対応する行
		 */
		private static TableRow createWeaponRow(Context context, CustomData from_data, CustomData to_data, String blust_type, String weapon_type) {
			int[] colors = {
					SettingManager.getColorYellow(),
					SettingManager.getColorWhite(),
					SettingManager.getColorWhite(),
					SettingManager.getColorYellow(),
					SettingManager.getColorWhite(),
					SettingManager.getColorWhite()
			};

			BBData from_weapon = from_data.getWeapon(blust_type, weapon_type);
			BBData to_weapon = to_data.getWeapon(blust_type, weapon_type);
			
			return ViewBuilder.createTableRow(context, colors, weapon_type, from_weapon.get("名称"), to_weapon.get("名称"));
		}
	}
	
	/**
	 * 「パーツスペック」のビューを生成するクラス
	 */
	private static class PartsSpecViewBuilder {

		/**
		 * パーツスペックテーブルを生成する。
		 * @param from_data
		 * @param to_data
		 * @param blust_type
		 * @return パーツスペックのテーブル
		 */
		private static View create(Context context, CustomData from_data, CustomData to_data, String blust_type) {
			TableLayout table = new TableLayout(context);
			table.setLayoutParams(new TableLayout.LayoutParams(FP, WC));
			
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), "", "比較元", "比較先"));

			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpPartsSpecArray(from_data, to_data, blust_type, "チップ容量")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpChipWeight(from_data, to_data)));
			
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpWeightSpecArray(from_data, to_data, blust_type)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpSpaceWeightSpecArray(from_data, to_data, blust_type)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpArmorAveSpecArray(from_data, to_data, blust_type)));

			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpPartsSpecArray(from_data, to_data, blust_type, "射撃補正")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpPartsSpecArray(from_data, to_data, blust_type, "索敵")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpPartsSpecArray(from_data, to_data, blust_type, "ロックオン")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpPartsSpecArray(from_data, to_data, blust_type, "DEF回復")));
			
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpPartsSpecArray(from_data, to_data, blust_type, "ブースター")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpPartsSpecArray(from_data, to_data, blust_type, "SP供給率")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpPartsSpecArray(from_data, to_data, blust_type, "エリア移動")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpPartsSpecArray(from_data, to_data, blust_type, "DEF耐久")));
			
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpPartsSpecArray(from_data, to_data, blust_type, "反動吸収")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpPartsSpecArray(from_data, to_data, blust_type, "リロード")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpPartsSpecArray(from_data, to_data, blust_type, "武器変更")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpPartsSpecArray(from_data, to_data, blust_type, "予備弾数")));
			
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpWalkSpecArray(from_data, to_data, blust_type)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpStartDushSpecArray(from_data, to_data, blust_type)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpNormalDushSpecArray(from_data, to_data, blust_type)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpSpeedDonRateSpecArray(from_data, to_data, blust_type)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpPartsSpecArray(from_data, to_data, blust_type, "重量耐性")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpPartsSpecArray(from_data, to_data, blust_type, "加速")));

			int color = SettingManager.getColorWhite();
			int bg_color = SettingManager.getColorBlue();

			LinearLayout layout_table = new LinearLayout(context);
			layout_table.setOrientation(LinearLayout.VERTICAL);
			layout_table.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));

			TextView parts_spec_view = ViewBuilder.createTextView(context, "パーツスペック", SettingManager.FLAG_TEXTSIZE_SMALL, color, bg_color);
			layout_table.addView(parts_spec_view);
			layout_table.addView(table);
			
			return layout_table;
		}
	}

	/**
	 * 「武器スペック」のビューを生成するクラス
	 */
	private static class WeaponSpecViewBuilder {
		
		private static View create(Context context, CustomData from_data, CustomData to_data, String blust_type) {

			int color = SettingManager.getColorWhite();
			int bg_color = SettingManager.getColorBlue();

			LinearLayout layout_table = new LinearLayout(context);
			layout_table.setOrientation(LinearLayout.VERTICAL);
			layout_table.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));

			TextView weapon_spec_view = ViewBuilder.createTextView(context, "武器スペック", SettingManager.FLAG_TEXTSIZE_SMALL, color, bg_color);
			layout_table.addView(weapon_spec_view);
			layout_table.addView(createWeaponView(context, from_data, to_data, blust_type));

			return layout_table;
		}

		/**
		 * 武器スペックテーブルを生成する。(マガジン火力、瞬間火力、戦術火力、リロード時間)
		 * @param from_data
		 * @param to_data
		 * @param blust_type
		 * @return 武器スペックテーブル
		 */
		private static TableLayout createWeaponView(Context context, CustomData from_data, CustomData to_data, String blust_type) {
			TableLayout table = new TableLayout(context);
			table.setLayoutParams(new TableLayout.LayoutParams(FP, WC));

			BBData fm_weapon = from_data.getWeapon(blust_type, BBDataManager.WEAPON_TYPE_MAIN);
			BBData to_weapon = to_data.getWeapon(blust_type, BBDataManager.WEAPON_TYPE_MAIN);
			addMainWeaponRow(table, from_data, to_data, fm_weapon, to_weapon);

			return table;
		}

		/**
		 * 主武器の情報を記載した列を追加する。
		 * @param table 追加先のテーブル
		 * @param from_data
		 * @param to_data
		 * @param from_weapon
		 * @param to_weapon
		 */
		private static void addMainWeaponRow(TableLayout table, CustomData from_data, CustomData to_data, BBData from_weapon, BBData to_weapon) {
			Context context = table.getContext();
			
			String[] title = { "", "比較元", "比較先" };
			String[] names = { "名称", from_weapon.get("名称"), to_weapon.get("名称") };
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), title));
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorWhite(), names));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpOneShotPowerArray(from_data, to_data, from_weapon, to_weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpCsShotPowerArray(from_data, to_data, from_weapon, to_weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpMagazinePowerArray(from_data, to_data, from_weapon, to_weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpSecPowerArray(from_data, to_data, from_weapon, to_weapon)));
			
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpBattlePowerArray(from_data, to_data, from_weapon, to_weapon)));

			if(from_weapon.existKey("OH耐性") || to_weapon.existKey("OH耐性")) {
				table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpOverheatPowerArray(from_data, to_data, from_weapon, to_weapon)));
				table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpOverheatTimeArray(from_data, to_data, from_weapon, to_weapon)));
				table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpOverheatRepairTimeArray(from_data, to_data, from_weapon, to_weapon)));
			}

			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpReloadTimeArray(from_data, to_data, from_weapon, to_weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpMagazineCount(from_data, to_data, from_weapon, to_weapon)));
			
			if(from_weapon.isChargeWeapon() || to_weapon.isChargeWeapon()) {
				table.addView(ViewBuilder.createTableRow(context, SpecArray.getCmpChargeTimeArray(from_data, to_data, from_weapon, to_weapon)));
			}
		}
	}
}
