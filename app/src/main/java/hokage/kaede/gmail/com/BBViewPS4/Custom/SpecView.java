package hokage.kaede.gmail.com.BBViewPS4.Custom;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomData;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomFileManager;
import hokage.kaede.gmail.com.BBViewLib.Java.SpecValues;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.SpecArray;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.ViewBuilder;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;
import hokage.kaede.gmail.com.StandardLib.Android.StringAdapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * 「性能」画面を表示するクラス。
 */
public class SpecView extends FrameLayout {
	
	// レイアウトパラメータ定義
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

	private CustomData mCustomData;
	
	// モード設定値に対する選択中の兵装名
	private String mBlustType = "";

	// 武器スペックをタイプB表示にするかどうか
	private boolean mIsShowTypeB = false;	
	
	private boolean mIsShowSimple = false;
	
	/**
	 * 初期化を行う。画面を生成する。
	 * @param context 対象の画面
	 */
	public SpecView(Context context, boolean is_simple, int mode, boolean is_show_typeb) {
		super(context);

		mIsShowSimple = is_simple;
		mMode = mode;
		mBlustType = MODE_NAME_LIST[mMode];
		mIsShowTypeB = is_show_typeb;

		LinearLayout main_layout = new LinearLayout(context);
		main_layout.setOrientation(LinearLayout.VERTICAL);
		main_layout.setGravity(Gravity.LEFT | Gravity.TOP);
		main_layout.setLayoutParams(new LinearLayout.LayoutParams(FP, FP));
		main_layout.addView(createSpecTable(context));
		main_layout.addView(createBottomView(context));

		this.setLayoutParams(new FrameLayout.LayoutParams(FP, FP));
		this.addView(main_layout);

		// 状態選択ビューは非表示にする。
		//this.addView(createStatusView(context));
	}
	
	/**
	 * モードを取得する。
	 * @return モード
	 */
	public int getMode() {
		return mMode;
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
			layout_table.addView(AssembleViewBuilder.create(context, ""));
			layout_table.addView(BlustSpeedViewBuilder.create(context));
			if(mIsShowSimple) {
				layout_table.addView(CommonSpecViewSimpleBuilder.create(context));
			}
			else {
				layout_table.addView(CommonSpecViewBuilder.create(context));
			}
			layout_table.addView(PartsSpecViewBuilder.create(context, ""));

		}
		else {
			layout_table.addView(AssembleViewBuilder.create(context, mBlustType));
			if(mIsShowSimple) {
				layout_table.addView(PartsSpecViewSimpleBuilder.create(context, mBlustType));
			}
			else {
				layout_table.addView(PartsSpecViewBuilder.create(context, mBlustType));
			}
			layout_table.addView(WeaponSpecViewBuilder.create(context, mBlustType, mIsShowTypeB));
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
				
				ToggleButton assalt_button = (ToggleButton)SpecView.this.findViewById(TOGGLE_BUTTON_ASSALT_ID);
				ToggleButton heavy_button = (ToggleButton)SpecView.this.findViewById(TOGGLE_BUTTON_HEAVY_ID);
				ToggleButton sniper_button = (ToggleButton)SpecView.this.findViewById(TOGGLE_BUTTON_SNIPER_ID);
				ToggleButton support_button = (ToggleButton)SpecView.this.findViewById(TOGGLE_BUTTON_SUPPORT_ID);
				
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
	// 状態選択ビュー関連の処理
	//----------------------------------------------------------

	/**
	 * 画面右上の状態選択ビューを生成する。
	 * @param context 対象の画面
	 * @return ビュー
	 */
	private LinearLayout createStatusView(Context context) {
		StringAdapter adapter = new StringAdapter(context, CustomData.CUSTOM_MODES);
		adapter.setTextColor(SettingManager.getColorWhite());
		
		Spinner sts_spinner = new Spinner(context);
		sts_spinner.setAdapter(adapter);
		sts_spinner.setOnItemSelectedListener(new OnStatusSelectedListener());

		LinearLayout buf_layout = new LinearLayout(context);
		buf_layout.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));
		buf_layout.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
		buf_layout.addView(sts_spinner);
		
		return buf_layout;
	}

	private class OnStatusSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			Context context = getContext();
			String file_dir = context.getFilesDir().toString();
			CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
			CustomData custom_data = custom_mng.getCacheData();

			custom_data.setMode(pos);
			updateSpecTable(parent.getContext());
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// 処理なし
		}
	}
	
	//----------------------------------------------------------
	// 各画面のビューを生成するクラス群
	//----------------------------------------------------------
	
	/**
	 * 「アセン」のビューを生成するクラス
	 */
	private static class AssembleViewBuilder {
		
		private static View create(Context context, String blust_type) {
			String file_dir = context.getFilesDir().toString();
			CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
			CustomData custom_data = custom_mng.getCacheData();
			
			int color = SettingManager.getColorWhite();
			int bg_color = SettingManager.getColorBlue();

			LinearLayout layout_table = new LinearLayout(context);
			layout_table.setOrientation(LinearLayout.VERTICAL);
			layout_table.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));

			TextView assemble_view = ViewBuilder.createTextView(context, "アセン", SettingManager.FLAG_TEXTSIZE_SMALL, color, bg_color);
			layout_table.addView(assemble_view);
			layout_table.addView(createAssembleView(context, custom_data, blust_type));
			layout_table.addView(createChipTable(context, custom_data));
			
			return layout_table;
		}

		/**
		 * 「アセン」のビューを生成する。
		 * @param context
		 * @param custom_data
		 * @return
		 */
		private static TableLayout createAssembleView(Context context, CustomData custom_data, String blust_type) {
			TableLayout table = new TableLayout(context);
			table.setLayoutParams(new TableLayout.LayoutParams(FP, WC));
			
			if(blust_type.equals("")) {
				table.addView(createPartsRow(context, custom_data, blust_type, BBDataManager.BLUST_PARTS_HEAD));
				table.addView(createPartsRow(context, custom_data, blust_type, BBDataManager.BLUST_PARTS_BODY));
				table.addView(createPartsRow(context, custom_data, blust_type, BBDataManager.BLUST_PARTS_ARMS));
				table.addView(createPartsRow(context, custom_data, blust_type, BBDataManager.BLUST_PARTS_LEGS));
			}
			else {
				table.addView(createPartsRow(context, custom_data, blust_type, BBDataManager.BLUST_PARTS_HEAD, BBDataManager.WEAPON_TYPE_MAIN));
				table.addView(createPartsRow(context, custom_data, blust_type, BBDataManager.BLUST_PARTS_BODY, BBDataManager.WEAPON_TYPE_SUB));
				table.addView(createPartsRow(context, custom_data, blust_type, BBDataManager.BLUST_PARTS_ARMS, BBDataManager.WEAPON_TYPE_SUPPORT));
				table.addView(createPartsRow(context, custom_data, blust_type, BBDataManager.BLUST_PARTS_LEGS, BBDataManager.WEAPON_TYPE_SPECIAL));
			}
			
			return table;
		}

		/**
		 * パーツスペックテーブルの行を生成する。
		 * @param context
		 * @param custom_data
		 * @param blust_type
		 * @param parts_key
		 * @param weapon_key
		 * @return 指定のパーツ種類に対応する行
		 */
		private static TableRow createPartsRow(Context context, CustomData custom_data, String blust_type, String parts_key, String weapon_key) {
			int[] colors = { 
					SettingManager.getColorYellow(),
					SettingManager.getColorWhite(),
					SettingManager.getColorYellow(),
					SettingManager.getColorWhite()
			};
			BBData parts = custom_data.getParts(parts_key);
			BBData weapon = custom_data.getWeapon(blust_type, weapon_key);
			return ViewBuilder.createTableRow(context, colors, parts_key, parts.get("名称"), weapon_key, weapon.get("名称"));
		}

		/**
		 * パーツスペックテーブルの行を生成する。
		 * @param context
		 * @param custom_data
		 * @param blust_type
		 * @param parts_key
		 * @return 指定のパーツ種類に対応する行
		 */
		private static TableRow createPartsRow(Context context, CustomData custom_data, String blust_type, String parts_key) {
			int[] colors = { 
					SettingManager.getColorYellow(),
					SettingManager.getColorWhite()
			};
			BBData parts = custom_data.getParts(parts_key);
			return ViewBuilder.createTableRow(context, colors, parts_key, parts.get("名称"));
		}

		/**
		 * チップテーブルを生成する
		 * @param context
		 * @param custom_data
		 * @return チップデータのテーブル
		 */
		private static LinearLayout createChipTable(Context context, CustomData custom_data) {
			LinearLayout layout_chip = new LinearLayout(context);
			layout_chip.setLayoutParams(new LinearLayout.LayoutParams(WC, WC));
			layout_chip.setOrientation(LinearLayout.HORIZONTAL);
			layout_chip.setGravity(Gravity.LEFT | Gravity.TOP);

			ArrayList<BBData> chip_list = custom_data.getChipList();
			int size = chip_list.size();
			
			String chip_names = "";
			for(int i=0; i<size; i++) {
				chip_names = chip_names + chip_list.get(i).get("名称");
				if(i<size-1) {
					chip_names = chip_names + ", ";
				}
			}
			
			if(size==0) {
				chip_names = "<チップ未設定>";
			}
			
			TextView chip_text_view = ViewBuilder.createTextView(context, chip_names, SettingManager.FLAG_TEXTSIZE_SMALL);
			
			layout_chip.addView(ViewBuilder.createTextView(context, "チップ", SettingManager.FLAG_TEXTSIZE_SMALL, SettingManager.getColorYellow()));
			layout_chip.addView(chip_text_view);
			
			return layout_chip;
		}
	}
	
	/**
	 * 「兵装スペック」のビューを生成するクラス
	 */
	private static class BlustSpeedViewBuilder {

		private static View create(Context context) {
			String file_dir = context.getFilesDir().toString();
			CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
			CustomData custom_data = custom_mng.getCacheData();

			int color = SettingManager.getColorWhite();
			int bg_color = SettingManager.getColorBlue();

			LinearLayout layout_table = new LinearLayout(context);
			layout_table.setOrientation(LinearLayout.VERTICAL);
			layout_table.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));

			// 兵装スペックを画面に表示する
			TextView blust_spec_view = ViewBuilder.createTextView(context, "兵装スペック", SettingManager.FLAG_TEXTSIZE_SMALL, color, bg_color);
			layout_table.addView(blust_spec_view);
			layout_table.addView(createBlustSpeedViews(context, custom_data));
			
			return layout_table;
		}
		
		private static final String[] TOTAL_SPEC_LIST = {
			"兵装", "重量(猶予)", "初速", "歩速", "低下率"
		};
		
		/**
		 * 兵装スペックテーブルを生成する。(全兵装におけるアセンの重量と速度を表示)
		 * @param data データ
		 * @return 兵装スペックテーブル
		 */
		private static TableLayout createBlustSpeedViews(Context context, CustomData data) {
			TableLayout table = new TableLayout(context);
			table.setLayoutParams(new TableLayout.LayoutParams(FP, WC));

			String[] blust_list = BBDataManager.BLUST_TYPE_LIST;
			int blust_list_len = blust_list.length;

			// タイトル行を生成
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), TOTAL_SPEC_LIST));
			
			for(int blust_idx=0; blust_idx<blust_list_len; blust_idx++) {
				String blust_name = blust_list[blust_idx];

				double rate = data.getSpeedDownRate(blust_name);
				int color = SettingManager.getColorWhite();

				if(rate < 0) {
					color = SettingManager.getColorMazenta();
				}

				String[] cols = {
						blust_name.substring(0, 2),
						String.format("%.1f", data.getWeight(blust_name)) + "(" + String.format("%.1f", data.getSpaceWeight(blust_name)) + ")",
						SpecValues.getSpecUnit(data.getStartDush(blust_name), "初速", BBViewSetting.IS_KM_PER_HOUR),
						SpecValues.getSpecUnit(data.getWalk(blust_name), "歩速", BBViewSetting.IS_KM_PER_HOUR),
						SpecValues.getSpecUnit(rate, "低下率", BBViewSetting.IS_KM_PER_HOUR),
					};
				
				table.addView(ViewBuilder.createTableRow(context, color, cols));
			}
			
			return table;
		}
	}
	
	/**
	 * 「総合スペック」のビューを生成するクラス(簡易表示)
	 */
	private static class CommonSpecViewSimpleBuilder {

		/**
		 * 総合スペックテーブルを生成する。(セットボーナス、装甲平均値、総重量(猶予))
		 * @param context
		 * @return 総合スペックテーブル
		 */
		private static View create(Context context) {
			String file_dir = context.getFilesDir().toString();
			CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
			CustomData custom_data = custom_mng.getCacheData();
			
			TableLayout table = new TableLayout(context);
			table.setLayoutParams(new TableLayout.LayoutParams(FP, WC));

			// 装甲平均値
			double armor_value = custom_data.getArmorAve();
			double life_value = custom_data.getLife(false);
			String armor_point = SpecValues.getPoint("装甲", armor_value, BBViewSetting.IS_KM_PER_HOUR, custom_data.isHoverLegs());
			String armor_str   = SpecValues.getSpecUnit(armor_value, "装甲平均値", BBViewSetting.IS_KM_PER_HOUR);
			String life_str    = SpecValues.getSpecUnit(life_value, "耐久", BBViewSetting.IS_KM_PER_HOUR);

			armor_str = armor_point + " (" + armor_str + ") (実耐久換算：" + life_str + ")"; 

			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorWhite(), "装甲平均値", armor_str));

			// 装甲平均値(空爆時)
			armor_value = custom_data.getArmorAveHead();
			life_value = custom_data.getLifeHead(false);
			armor_point = SpecValues.getPoint("装甲", armor_value, BBViewSetting.IS_KM_PER_HOUR, custom_data.isHoverLegs());
			armor_str   = SpecValues.getSpecUnit(armor_value, "装甲平均値", BBViewSetting.IS_KM_PER_HOUR);
			life_str    = SpecValues.getSpecUnit(life_value, "耐久", BBViewSetting.IS_KM_PER_HOUR);

			armor_str = armor_point + " (" + armor_str + ") (実耐久換算：" + life_str + ")"; 

			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorWhite(), "装甲平均値(空爆時)", armor_str));

			// 装甲平均値(地爆時)
			armor_value = custom_data.getArmorAveLegs();
			life_value = custom_data.getLifeLegs(false);
			armor_point = SpecValues.getPoint("装甲", armor_value, BBViewSetting.IS_KM_PER_HOUR, custom_data.isHoverLegs());
			armor_str   = SpecValues.getSpecUnit(armor_value, "装甲平均値", BBViewSetting.IS_KM_PER_HOUR);
			life_str    = SpecValues.getSpecUnit(life_value, "耐久", BBViewSetting.IS_KM_PER_HOUR);

			armor_str = armor_point + " (" + armor_str + ") (実耐久換算：" + life_str + ")"; 

			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorWhite(), "装甲平均値(地爆時)", armor_str));
			
			// 総重量(猶予)
			String weight_str = String.format("%.1f (%.1f)", custom_data.getPartsWeight(), custom_data.getSpacePartsWeight());
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorWhite(), "総重量(猶予)", weight_str));
			
			int color = SettingManager.getColorWhite();
			int bg_color = SettingManager.getColorBlue();
			
			LinearLayout layout_table = new LinearLayout(context);
			layout_table.setOrientation(LinearLayout.VERTICAL);
			layout_table.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));

			TextView common_spec_view = ViewBuilder.createTextView(context, "総合スペック", SettingManager.FLAG_TEXTSIZE_SMALL, color, bg_color);
			layout_table.addView(common_spec_view);
			layout_table.addView(table);
			
			return layout_table;
		}
	}

	/**
	 * 「総合スペック」のビューを生成するクラス
	 */
	private static class CommonSpecViewBuilder {

		/**
		 * 総合スペックテーブルを生成する。(セットボーナス、チップ容量、装甲平均値、総重量(猶予))
		 * @param context
		 * @return 総合スペックテーブル
		 */
		private static View create(Context context) {
			String file_dir = context.getFilesDir().toString();
			CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
			CustomData custom_data = custom_mng.getCacheData();
			
			TableLayout table = new TableLayout(context);
			table.setLayoutParams(new TableLayout.LayoutParams(FP, WC));

			// 総重量(猶予)
			String weight_str = String.format("%.1f (%.1f)", custom_data.getPartsWeight(), custom_data.getSpacePartsWeight());
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorWhite(), "総重量(猶予)", weight_str));
			
			// 装甲平均値
			TableLayout armor_table = new TableLayout(context);
			armor_table.setLayoutParams(new TableLayout.LayoutParams(FP, WC));
			
			if(BBViewSetting.IS_ARMOR_RATE) {
				armor_table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), "被ダメ種別", "ダメージ係数", "実耐久値", "実耐久値(N-DEF)"));
			}
			else {
				armor_table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), "被ダメ種別", "装甲平均値", "実耐久値", "実耐久値(N-DEF)"));
			}
			
			double armor_value = custom_data.getArmorAve();
			double life_value = custom_data.getLife(false);
			double life_ndef_value = custom_data.getLife(true);
			String armor_point   = SpecValues.getPoint("装甲", armor_value, BBViewSetting.IS_KM_PER_HOUR, custom_data.isHoverLegs());
			String armor_str     = SpecValues.getSpecUnit(armor_value, "装甲平均値", BBViewSetting.IS_KM_PER_HOUR);
			String life_str      = SpecValues.getSpecUnit(life_value, "耐久", BBViewSetting.IS_KM_PER_HOUR);
			String life_ndef_str = SpecValues.getSpecUnit(life_ndef_value, "耐久", BBViewSetting.IS_KM_PER_HOUR);

			armor_str = armor_point + " (" + armor_str + ")"; 
			
			armor_table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorWhite(), "対近接", armor_str, life_str, life_ndef_str));

			// 装甲平均値(空爆時)
			armor_value = custom_data.getArmorAveHead();
			life_value = custom_data.getLifeHead(false);
			life_ndef_value = custom_data.getLifeHead(true);
			armor_point   = SpecValues.getPoint("装甲", armor_value, BBViewSetting.IS_KM_PER_HOUR, custom_data.isHoverLegs());
			armor_str     = SpecValues.getSpecUnit(armor_value, "装甲平均値", BBViewSetting.IS_KM_PER_HOUR);
			life_str      = SpecValues.getSpecUnit(life_value, "耐久", BBViewSetting.IS_KM_PER_HOUR);
			life_ndef_str = SpecValues.getSpecUnit(life_ndef_value, "耐久", BBViewSetting.IS_KM_PER_HOUR);

			armor_str = armor_point + " (" + armor_str + ")"; 
			
			armor_table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorWhite(), "対空爆時", armor_str, life_str, life_ndef_str));

			// 装甲平均値(地爆時)
			armor_value = custom_data.getArmorAveLegs();
			life_value = custom_data.getLifeLegs(false);
			life_ndef_value = custom_data.getLifeLegs(true);
			armor_point   = SpecValues.getPoint("装甲", armor_value, BBViewSetting.IS_KM_PER_HOUR, custom_data.isHoverLegs());
			armor_str     = SpecValues.getSpecUnit(armor_value, "装甲平均値", BBViewSetting.IS_KM_PER_HOUR);
			life_str      = SpecValues.getSpecUnit(life_value, "耐久", BBViewSetting.IS_KM_PER_HOUR);
			life_ndef_str = SpecValues.getSpecUnit(life_ndef_value, "耐久", BBViewSetting.IS_KM_PER_HOUR);

			armor_str = armor_point + " (" + armor_str + ")"; 
			
			armor_table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorWhite(), "対地爆時", armor_str, life_str, life_ndef_str));
			
			int color = SettingManager.getColorWhite();
			int bg_color = SettingManager.getColorBlue();
			
			LinearLayout layout_table = new LinearLayout(context);
			layout_table.setOrientation(LinearLayout.VERTICAL);
			layout_table.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));

			TextView common_spec_view = ViewBuilder.createTextView(context, "総合スペック", SettingManager.FLAG_TEXTSIZE_SMALL, color, bg_color);
			layout_table.addView(common_spec_view);
			layout_table.addView(table);
			layout_table.addView(armor_table);
			
			return layout_table;
		}
	}
	
	/**
	 * 「パーツスペック」のビューを生成するクラス
	 */
	private static class PartsSpecViewBuilder {

		/**
		 * パーツスペックテーブルを生成する。
		 * @param context
		 * @param blust_type
		 * @return パーツスペックのテーブル
		 */
		private static View create(Context context, String blust_type) {
			String file_dir = context.getFilesDir().toString();
			CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
			CustomData custom_data = custom_mng.getCacheData();
			
			TableLayout table = new TableLayout(context);
			table.setLayoutParams(new TableLayout.LayoutParams(FP, WC));
			
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), "", "補正前", "補正後"));
			
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getPartsSpecArray(custom_data, blust_type, "射撃補正")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getPartsSpecArray(custom_data, blust_type, "索敵")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getPartsSpecArray(custom_data, blust_type, "ロックオン")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getDefRecoverArray(custom_data, blust_type)));
			
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getBoostArray(custom_data, blust_type)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getBoostChargeTimeArray(custom_data, blust_type)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getPartsSpecArray(custom_data, blust_type, "SP供給率")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getPartsSpecArray(custom_data, blust_type, "エリア移動")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getPartsSpecArray(custom_data, blust_type, "DEF耐久")));
			
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getPartsSpecArray(custom_data, blust_type, "反動吸収")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getPartsSpecArray(custom_data, blust_type, "リロード")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getPartsSpecArray(custom_data, blust_type, "武器変更")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getPartsSpecArray(custom_data, blust_type, "予備弾数")));
			
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getPartsSpecArray(custom_data, blust_type, "歩行")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getPartsSpecArray(custom_data, blust_type, "ダッシュ")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getPartsSpecArray(custom_data, blust_type, "重量耐性")));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getPartsSpecArray(custom_data, blust_type, "巡航")));

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
	 * 「パーツスペック」のビュー(簡易表示)を生成するクラス
	 */
	private static class PartsSpecViewSimpleBuilder {

		private static final String[] BASE_COL_STR = { "装甲平均値", "総重量(猶予)", "低下率", "チップ容量"};
		private static final String[] HEAD_COL_STR = { "射撃補正", "索敵", "ロックオン", "DEF回復" };
		private static final String[] BODY_COL_STR = { "ブースター", "SP供給率", "エリア移動", "DEF耐久" };
		private static final String[] ARMS_COL_STR = { "反動吸収", "リロード", "武器変更", "予備弾倉" };
		private static final String[] LEGS_COL_STR = { "歩行", "ダッシュ", "重量耐性", "巡航" };
		
		/**
		 * パーツスペックテーブルを生成する。
		 * @param context
		 * @param blust_type
		 * @return パーツスペックのテーブル
		 */
		private static View create(Context context, String blust_type) {
			String file_dir = context.getFilesDir().toString();
			CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
			CustomData custom_data = custom_mng.getCacheData();
			
			TableLayout table = new TableLayout(context);
			table.setLayoutParams(new TableLayout.LayoutParams(FP, WC));
			
			String[] base_value_col_str = {
				getSpecString(custom_data, "装甲", custom_data.getArmorAve(blust_type)),
				String.format("%.1f (%.1f)", custom_data.getPartsWeight(), custom_data.getSpacePartsWeight()),
				SpecValues.getSpecUnit(custom_data.getSpeedDownRate(blust_type), "低下率", BBViewSetting.IS_KM_PER_HOUR),
			};
			
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), BASE_COL_STR));
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorWhite(), base_value_col_str));
			
			String[] head_value_col_str = {
				getSpecString(custom_data, "射撃補正", custom_data.getShotBonus(blust_type)),
				getSpecString(custom_data, "索敵", custom_data.getSearch(blust_type)),
				getSpecString(custom_data, "ロックオン", custom_data.getRockOn(blust_type)),
				getSpecString(custom_data, "DEF回復", custom_data.getDefRecover(blust_type))
			};

			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), HEAD_COL_STR));
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorWhite(), head_value_col_str));

			String[] body_value_col_str = {
				getSpecString(custom_data, "ブースター", custom_data.getBoost(blust_type)),
				getSpecString(custom_data, "SP供給率", custom_data.getSP(blust_type)),
				getSpecString(custom_data, "エリア移動", custom_data.getAreaMove(blust_type)),
				getSpecString(custom_data, "DEF耐久", custom_data.getDefGuard(blust_type))
			};

			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), BODY_COL_STR));
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorWhite(), body_value_col_str));

			String[] arms_value_col_str = {
				getSpecString(custom_data, "反動吸収", custom_data.getRecoil(blust_type)),
				getSpecString(custom_data, "リロード", custom_data.getReload(blust_type)),
				getSpecString(custom_data, "武器変更", custom_data.getChangeWeapon(blust_type)),
				getSpecString(custom_data, "予備弾数", custom_data.getSpareBullet(blust_type))
			};

			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), ARMS_COL_STR));
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorWhite(), arms_value_col_str));

			String[] legs_value_col_str = {
				getSpecString(custom_data, "歩行", custom_data.getWalk(blust_type)),
				getSpecString(custom_data, "ダッシュ", custom_data.getStartDush(blust_type)),
				getSpecString(custom_data, "重量耐性", custom_data.getAntiWeight(blust_type)),
				getSpecString(custom_data, "巡航", custom_data.getAcceleration(blust_type))
			};

			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), LEGS_COL_STR));
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorWhite(), legs_value_col_str));

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

		private static String getSpecString(CustomData data, String target_key, double value) {
			String blust_point = SpecValues.getPoint(target_key, value, BBViewSetting.IS_KM_PER_HOUR, data.isHoverLegs());
			String blust_str = SpecValues.getSpecUnit(value, target_key, BBViewSetting.IS_KM_PER_HOUR);
			return blust_point + " (" + blust_str + ")";
		}
	}
	
	/**
	 * 「武器スペック」のビューを生成するクラス
	 */
	private static class WeaponSpecViewBuilder {
		
		private static View create(Context context, String blust_type, boolean is_show_typeb) {
			String file_dir = context.getFilesDir().toString();
			CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);
			CustomData custom_data = custom_mng.getCacheData();

			int color = SettingManager.getColorWhite();
			int bg_color = SettingManager.getColorBlue();

			LinearLayout layout_table = new LinearLayout(context);
			layout_table.setOrientation(LinearLayout.VERTICAL);
			layout_table.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));

			TextView weapon_spec_view = ViewBuilder.createTextView(context, "武器スペック", SettingManager.FLAG_TEXTSIZE_SMALL, color, bg_color);
			layout_table.addView(weapon_spec_view);
			layout_table.addView(createWeaponView(context, custom_data, blust_type, is_show_typeb));

			return layout_table;
		}

		/**
		 * 武器スペックテーブルを生成する。(マガジン火力、瞬間火力、戦術火力、リロード時間)
		 * @param data データ
		 * @return 武器スペックテーブル
		 */
		private static TableLayout createWeaponView(Context context, CustomData data, String blust_type, boolean is_show_typeb) {
			TableLayout table = new TableLayout(context);
			table.setLayoutParams(new TableLayout.LayoutParams(FP, WC));
			
			int weapon_list_len = BBDataManager.WEAPON_TYPE_LIST.length;
			for(int weapon_idx=0; weapon_idx<weapon_list_len; weapon_idx++) {
				String weapon_type = BBDataManager.WEAPON_TYPE_LIST[weapon_idx];
				BBData weapon = data.getWeapon(blust_type, weapon_type);
				
				// タイプB表示設定が有効の場合、武器データを切り替える
				String name = weapon.getNameWithType(is_show_typeb);
				if(is_show_typeb) {
					BBData weapon_typeb = weapon.getTypeB();
					
					if(weapon_typeb != null) {
						weapon = weapon_typeb;
					}
				}
				
				if(blust_type.equals("強襲兵装")) {
					if(weapon.existCategory("主武器")) {
						addMainWeaponRow(table, data, weapon, name);
					}
					else if(weapon.existCategory("副武器")) {
						addSubWeaponRow(table, data, weapon, name);
					}
					else if(weapon.existCategory("補助装備")) {
						addSlashRow(table, data, weapon, name);
					}
					else if(weapon.existCategory("特別装備")) {
						addACRow(table, data, weapon, name);
					}
				}
				else if(blust_type.equals("重火力兵装")) {
					if(weapon.existCategory("主武器")) {
						addMainWeaponRow(table, data, weapon, name);
					}
					else if(weapon.existCategory("副武器")) {
						addSubWeaponRow(table, data, weapon, name);
					}
					else if(weapon.existCategory("補助装備")) {
						if(weapon.existCategory("パイク系統") || weapon.existCategory("チェーンソー系統")) {
							addSlashRow(table, data, weapon, name);
						}
						else if(weapon.existCategory("ハウルHSG系統")) {
							addMainWeaponRow(table, data, weapon, name);
						}
						else {
							addSupportBombRow(table, data, weapon, name);
						}
					}
					else if(weapon.existCategory("特別装備")) {
						if(weapon.existCategory("バリアユニット系統")) {
							addBarrierRow(table, data, weapon, name);
						}
						else {
							addCannonRow(table, data, weapon, name);
						}
					}
				}
				else if(blust_type.equals("遊撃兵装")) {
					if(weapon.existCategory("主武器")) {
						addMainWeaponRow(table, data, weapon, name);
					}
					else if(weapon.existCategory("副武器")) {
						addMainWeaponRow(table, data, weapon, name);
					}
					else if(weapon.existCategory("補助装備")) {
						if(weapon.existCategory("偵察機系統") || weapon.existCategory("レーダーユニット系統") ||
						   weapon.existCategory("NDディテクター系統") || weapon.existCategory("クリアリングソナー系統")) {
							addSearchRow(table, data, weapon, name);
						}
						else if(weapon.existCategory("高振動ブレード系統")) {
							addSlashRow(table, data, weapon, name);
						}
						else if(weapon.existCategory("スタングレネード系統")) {
							addSupportBombRow(table, data, weapon, name);
						}
						else {
							addMainWeaponRow(table, data, weapon, name);
						}
					}
					else if(weapon.existCategory("特別装備")) {
						if(weapon.existCategory("EUS系統")) {
							addEUSRow(table, data, weapon, name);
						}
						else if(weapon.existCategory("シールド系統")) {
							addBarrierRow(table, data, weapon, name);
						}
						else {
							addExtraRow(table, data, blust_type, weapon, name);
						}
					}
				}
				else if(blust_type.equals("支援兵装")) {
					if(weapon.existCategory("主武器")) {
						addMainWeaponRow(table, data, weapon, name);
					}
					else if(weapon.existCategory("副武器")) {
						addSubWeaponRow(table, data, weapon, name);
					}
					else if(weapon.existCategory("補助装備")) {
						addSearchRow(table, data, weapon, name);
					}
					else if(weapon.existCategory("特別装備")) {
						addReapirRow(table, data, weapon, name);
					}
				}
			}
			
			return table;
		}

		/**
		 * 主武器の情報を記載した列を追加する。
		 * @param table 追加先のテーブル
		 * @param data 対象のアセンデータ
		 * @param weapon 対象の武器データ
		 */
		private static void addMainWeaponRow(TableLayout table, CustomData data, BBData weapon, String name) {
			Context context = table.getContext();
			
			String[] title = { name, "補正前", "補正後" };
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), title));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getOneShotPowerArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getCsShotPowerArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getMagazinePowerArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getSecPowerArray(data, weapon)));
			
			if(weapon.existKey("OH耐性")) {
				table.addView(ViewBuilder.createTableRow(context, SpecArray.getBattlePowerOverheatArray(data, weapon)));
				table.addView(ViewBuilder.createTableRow(context, SpecArray.getOverheatPowerArray(data, weapon)));
				table.addView(ViewBuilder.createTableRow(context, SpecArray.getOverheatTimeArray(data, weapon)));
				table.addView(ViewBuilder.createTableRow(context, SpecArray.getOverheatRepairTimeArray(data, weapon)));
			}
			else {
				table.addView(ViewBuilder.createTableRow(context, SpecArray.getBattlePowerArray(data, weapon)));
			}
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getReloadTimeArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getMagazineCount(data, weapon)));
			
			if(weapon.isChargeWeapon()) {
				table.addView(ViewBuilder.createTableRow(context, SpecArray.getChargeTimeArray(data, weapon)));
			}
		}

		/**
		 * 副武器の情報を記載した列を追加する。
		 * @param table 追加先のテーブル
		 * @param data 対象のアセンデータ
		 * @param weapon 対象の武器データ
		 */
		private static void addSubWeaponRow(TableLayout table, CustomData data, BBData weapon, String name) {
			Context context = table.getContext();

			String[] title = { name, "補正前", "補正後" };
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), title));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getOneShotPowerArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getExplosionRangeArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getReloadTimeArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getMagazineCount(data, weapon)));

			if(weapon.isChargeWeapon()) {
				table.addView(ViewBuilder.createTableRow(context, SpecArray.getChargeTimeArray(data, weapon)));
			}
		}

		/**
		 * 近接武器の情報を記載した列を追加する。
		 * @param table 追加先のテーブル
		 * @param data 対象のアセンデータ
		 * @param weapon 対象の武器データ
		 */
		private static void addSlashRow(TableLayout table, CustomData data, BBData weapon, String name) {
			Context context = table.getContext();

			String[] title = { name, "補正前", "補正後" };
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), title));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getNormalSlashArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getDashSlashArray(data, weapon)));
		}

		/**
		 * 補助装備ボムの情報を記載した列を追加する。
		 * @param table 追加先のテーブル
		 * @param data 対象のアセンデータ
		 * @param weapon 対象の武器データ
		 */
		private static void addSupportBombRow(TableLayout table, CustomData data, BBData weapon, String name) {
			Context context = table.getContext();

			String[] title = { name, "補正前", "補正後" };
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), title));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getExplosionRangeArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getReloadTimeArray(data, weapon)));
			
			if(weapon.existKey("効果持続")) {
				table.addView(ViewBuilder.createTableRow(context, SpecArray.getEffectTimeArray(data, weapon)));
			}
		}

		/**
		 * 索敵装備の情報を記載した列を追加する。
		 * @param table 追加先のテーブル
		 * @param data 対象のアセンデータ
		 * @param weapon 対象の武器データ
		 */
		private static void addSearchRow(TableLayout table, CustomData data, BBData weapon, String name) {
			Context context = table.getContext();

			String[] title = { name, "補正前", "補正後" };
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), title));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getSearchTimeArray(data, weapon)));
		}

		/**
		 * ACの情報を記載した列を追加する。
		 * @param table 追加先のテーブル
		 * @param data 対象のアセンデータ
		 * @param weapon 対象の武器データ
		 */
		private static void addACRow(TableLayout table, CustomData data, BBData weapon, String name) {
			Context context = table.getContext();

			String[] title = { name, "補正前", "補正後" };
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), title));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getAcSpeedArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getAcBattleSpeedArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getSpChargeTimeArray(data, weapon, BBDataManager.BLUST_TYPE_ASSALT)));
		}

		/**
		 * 砲撃装備の情報を記載した列を追加する。
		 * @param table 追加先のテーブル
		 * @param data 対象のアセンデータ
		 * @param weapon 対象の武器データ
		 */
		private static void addCannonRow(TableLayout table, CustomData data, BBData weapon, String name) {
			Context context = table.getContext();

			String[] title = { name, "補正前", "補正後" };
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), title));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getOneShotPowerArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getExplosionRangeArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getSpChargeTimeArray(data, weapon, BBDataManager.BLUST_TYPE_HEAVY)));
		}

		/**
		 * バリア装備の情報を記載した列を追加する。
		 * @param table 追加先のテーブル
		 * @param data 対象のアセンデータ
		 * @param weapon 対象の武器データ
		 */
		private static void addBarrierRow(TableLayout table, CustomData data, BBData weapon, String name) {
			Context context = table.getContext();

			String[] title = { name, "補正前", "補正後" };
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), title));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getBattleBarrierGuardArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getSpChargeTimeArray(data, weapon, BBDataManager.BLUST_TYPE_HEAVY)));
		}
		
		/**
		 * EUS系統の情報を記載した列を追加する。
		 * @param table 追加先のテーブル
		 * @param data 対象のアセンデータ
		 * @param weapon 対象の武器データ
		 */
		private static void addEUSRow(TableLayout table, CustomData data,  BBData weapon, String name) {
			Context context = table.getContext();

			String[] title = { name, "補正前", "補正後" };
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), title));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getMagazinePowerArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getSecPowerArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getBattlePowerArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getReloadTimeArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getSpChargeTimeArray(data, weapon, BBDataManager.BLUST_TYPE_SNIPER)));
		}

		/**
		 * リペア装備の情報を記載した列を追加する。
		 * @param table 追加先のテーブル
		 * @param data 対象のアセンデータ
		 * @param weapon 対象の武器データ
		 */
		private static void addReapirRow(TableLayout table, CustomData data, BBData weapon, String name) {
			Context context = table.getContext();

			String[] title = { name, "補正前", "補正後" };
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), title));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getMaxRepairArray(data, weapon)));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getSpChargeTimeArray(data, weapon, BBDataManager.BLUST_TYPE_SUPPORT)));
		}

		/**
		 * 特別装備の基本情報を記載した列を追加する。
		 * @param table 追加先のテーブル
		 * @param data 対象のアセンデータ
		 * @param weapon 対象の武器データ
		 */
		private static void addExtraRow(TableLayout table, CustomData data, String blust_type, BBData weapon, String name) {
			Context context = table.getContext();

			String[] title = { name, "補正前", "補正後" };
			table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), title));
			table.addView(ViewBuilder.createTableRow(context, SpecArray.getSpChargeTimeArray(data, weapon, blust_type)));
		}
	}
}
