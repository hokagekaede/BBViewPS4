package hokage.kaede.gmail.com.BBViewPS4.Item;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomData;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomFileManager;
import hokage.kaede.gmail.com.BBViewLib.Java.SpecValues;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.ViewBuilder;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;
import hokage.kaede.gmail.com.StandardLib.Android.StringAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * 「耐性シミュ」画面を表示するクラス。
 */
public class WeaponSimView extends LinearLayout implements OnClickListener {

	private static final int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
	private static final int FP = LinearLayout.LayoutParams.FILL_PARENT;
	
	private static final int VIEWID_ONESHOT_BASE      = 100;
	private static final int VIEWID_ONESHOT_CS_BASE   = 200;
	private static final int VIEWID_MAGAZINE_BASE     = 300;
	private static final int VIEWID_1SEC_BASE         = 400;
	private static final int VIEWID_BATTLE_BASE       = 500;

	private static final int VIEWID_OFFSET_DAMAGE     = 0;
	private static final int VIEWID_OFFSET_KB         = 1;
	private static final int VIEWID_OFFSET_DOWN       = 2;
	private static final int VIEWID_OFFSET_BREAK      = 3;
	
	private static final int VIEWID_SPEEDUP_BASE      = 1000;
	private static final int VIEWID_NEWDUP_BASE       = 1100;
	private static final int VIEWID_PRECISE_BASE      = 1200;
	private static final int VIEWID_FATAL_BASE        = 1300;
	private static final int VIEWID_ANTISTN_BASE      = 1400;

	private static final int VIEWID_CHARGE_LEVEL_BASE = 1500;
	private static final int VIEWID_EXPLOSION_BASE    = 1600;
	private static final int VIEWID_EXPLOSION_BAR     = 1601;
	private static final int VIEWID_EXPLOSION_ROW     = 1602;
	
	private static final int VIEWID_REDUCE_BREAK_BASE = 2000;
	private static final int VIEWID_GURAD_DOWN_BASE   = 2100;
	
	private static final int VIEWID_OFFSET_CHIPI      = 0;
	private static final int VIEWID_OFFSET_CHIPII     = 1;
	private static final int VIEWID_OFFSET_CHIPIII    = 2;
	
	private static final int VIEWID_DEF_LIFE          = 3000;
	private static final int VIEWID_DEF_NDEF          = 3100;

	private static final int VIEWID_ARMOR_TEXT_BASE   = 4000;
	private static final int VIEWID_ARMOR_LIST_BASE   = 4100;
	private static final int VIEWID_ARMOR_HIT_BASE    = 4200;
	private static final int VIEWID_OFFSET_ARMOR_HEAD = 0;
	private static final int VIEWID_OFFSET_ARMOR_BODY = 1;
	private static final int VIEWID_OFFSET_ARMOR_ARMS = 2;
	private static final int VIEWID_OFFSET_ARMOR_LEGS = 3;

	// シミュレーションの武器データ
	private BBData mTargetData;
	private boolean mIsTypeShot = false;
	private boolean mIsTypeExplosion = false;
	private int mExplosionRange = 0;
	
	// 攻撃側のアセン情報
	private CustomData mAttackBlust;
	private int mFatalChipLv       = 0;
	private int mChargeLevel       = 0;
	private int mExplotionDistance = 0;
	
	// 防御側のアセン情報
	private CustomData mDefenceBlust;
	private int mDefenceLife = SpecValues.BLUST_LIFE_MAX;
	
	private boolean mIsArmorValid = false;
	private double[] mArmorArray = { 0, 0, 0, 0 };
	private int[] mHitPercentArray = { 0, 0, 0, 0 };
	private static final String[] ARMOR_TEXT_TITLE = { "頭部", "胴部", "腕部", "脚部" };
	
	private static final int ARMOR_HEAD = 0;
	private static final int ARMOR_BODY = 1;
	private static final int ARMOR_ARMS = 2;
	private static final int ARMOR_LEGS = 3;
	
	// チップデータ
	private ArrayList<BBData> mSpeedUpChips = new ArrayList<BBData>();
	private ArrayList<BBData> mNewdUpChips = new ArrayList<BBData>();
	private ArrayList<BBData> mPriciseChips = new ArrayList<BBData>();
	private ArrayList<BBData> mAntiStnChips = new ArrayList<BBData>();
	private ArrayList<BBData> mReduceBreakChips = new ArrayList<BBData>();
	private ArrayList<BBData> mGuardDownChips = new ArrayList<BBData>();

	/**
	 * 初期化を行う。
	 * @param context コンテキスト
	 * @param target 対象の武器データ
	 */
	public WeaponSimView(Context context, BBData target) {
		super(context);
		
		initData(target);
		
		createView(context, target);
		updateView();
	}
	
	/**
	 * 各データを初期化する。
	 * @param target シミュレーション対象の武器
	 */
	private void initData(BBData target) {
		mTargetData = target;
		mIsTypeShot = target.isShotWeapon();
		mIsTypeExplosion = target.isExplosionWeapon();
		mExplosionRange = target.getExplosionRange();

		Context context = getContext();
		String file_dir = context.getFilesDir().toString();
		CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);

		mAttackBlust = custom_mng.getDefaultCustomData();
		mDefenceBlust = custom_mng.getDefaultCustomData();
		
		BBDataManager data_mng = BBDataManager.getInstance();
		
		mSpeedUpChips = data_mng.getChipSeries("実弾速射");
		mNewdUpChips = data_mng.getChipSeries("ニュード威力上昇");
		mPriciseChips = data_mng.getChipSeries("プリサイスショット");
		mAntiStnChips = data_mng.getChipSeries("アンチスタビリティ");
		mReduceBreakChips = data_mng.getChipSeries("大破抑制");
		mGuardDownChips = data_mng.getChipSeries("転倒耐性");
	}
	
	/**
	 * 画面に表示するビューを生成する。
	 * @param context コンテキスト
	 * @param data 対象の武器
	 */
	private void createView(Context context, BBData data) {
		this.setLayoutParams(new LinearLayout.LayoutParams(FP, FP));
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.LEFT | Gravity.TOP);
		
		this.addView(createPowerTable(context, data));
		
		ScrollView data_view = new ScrollView(context);
		data_view.addView(createSettingTable(context));
		data_view.setLayoutParams(new LinearLayout.LayoutParams(FP, WC, 1));
		
		this.addView(data_view);
	}
	
	/**
	 * 火力を表示するテーブルを生成する。
	 * @param context コンテキスト
	 * @param data 対象の武器
	 * @return テーブルデータ
	 */
	private TableLayout createPowerTable(Context context, BBData data) {
		String title = data.get("名称");
		float fontsize = BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL);
		
		TableLayout table_power = new TableLayout(context);
		table_power.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));
		table_power.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), fontsize, title, "ダメージ", "よろけ", "転倒", "大破"));
		table_power.addView(createPowerRow(context, "単発火力(BS)", VIEWID_ONESHOT_BASE));
		table_power.addView(createPowerRow(context, "単発火力(CS)", VIEWID_ONESHOT_CS_BASE));
		table_power.addView(createPowerRow(context, "マガジン火力", VIEWID_MAGAZINE_BASE));
		table_power.addView(createPowerRow(context, "瞬間火力", VIEWID_1SEC_BASE));
		table_power.addView(createPowerRow(context, "戦術火力", VIEWID_BATTLE_BASE));
		
		return table_power;
	}
	
	/**
	 * 設定テーブルを生成する。
	 * @param context コンテキスト
	 * @return テーブルデータ
	 */
	private TableLayout createSettingTable(Context context) {
		TableLayout table_setting = new TableLayout(context);
		table_setting.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));
		table_setting.addView(createChipRow(context, "実弾速射", VIEWID_SPEEDUP_BASE, 3));
		table_setting.addView(createChipRow(context, "ニュード強化", VIEWID_NEWDUP_BASE, 3));
		table_setting.addView(createChipRow(context, "プリサイス", VIEWID_PRECISE_BASE, 3));
		table_setting.addView(createChipRow(context, "フェイタル", VIEWID_FATAL_BASE, 2));
		table_setting.addView(createChipRow(context, "アンスタ", VIEWID_ANTISTN_BASE, 3));
		
		if(mTargetData.isChargeWeapon()) {
			table_setting.addView(createChipRow(context, "チャージLv", VIEWID_CHARGE_LEVEL_BASE, 2));
		}
		
		table_setting.addView(createExplosionDistance(context));

		table_setting.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));
		table_setting.addView(createLifeRow(context));

		table_setting.addView(createArmorValidRow(context));
		table_setting.addView(createArmorRow(context, "頭部", VIEWID_OFFSET_ARMOR_HEAD));
		table_setting.addView(createArmorRow(context, "胴部", VIEWID_OFFSET_ARMOR_BODY));
		table_setting.addView(createArmorRow(context, "腕部", VIEWID_OFFSET_ARMOR_ARMS));
		table_setting.addView(createArmorRow(context, "脚部", VIEWID_OFFSET_ARMOR_LEGS));
		table_setting.addView(createChipRow(context, "大破抑制", VIEWID_REDUCE_BREAK_BASE, 1));
		table_setting.addView(createChipRow(context, "転倒耐性", VIEWID_GURAD_DOWN_BASE, 3));
		
		return table_setting;	
	}
	
	/**
	 * 火力を表示するテキストビュー行を生成する。
	 * @param context コンテキスト
	 * @param title タイトル
	 * @param base_id ベースID
	 * @return 行のView
	 */
	private TableRow createPowerRow(Context context, String title, int base_id) {
		TableRow row = new TableRow(context);
		
		TextView title_text_view = new TextView(context);
		title_text_view.setText(title);
		title_text_view.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		title_text_view.setTextColor(SettingManager.getColorWhite());

		TextView text_view1 = new TextView(context);
		text_view1.setId(base_id + VIEWID_OFFSET_DAMAGE);
		text_view1.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		text_view1.setTextColor(SettingManager.getColorWhite());

		TextView text_view2 = new TextView(context);
		text_view2.setId(base_id + VIEWID_OFFSET_KB);
		text_view2.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		text_view2.setTextColor(SettingManager.getColorWhite());

		TextView text_view3 = new TextView(context);
		text_view3.setId(base_id + VIEWID_OFFSET_DOWN);
		text_view3.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		text_view3.setTextColor(SettingManager.getColorWhite());

		TextView text_view4 = new TextView(context);
		text_view4.setId(base_id + VIEWID_OFFSET_BREAK);
		text_view4.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		text_view4.setTextColor(SettingManager.getColorWhite());
		
		row.addView(title_text_view);
		row.addView(text_view1);
		row.addView(text_view2);
		row.addView(text_view3);
		row.addView(text_view4);
		
		return row;
	}
	
	/**
	 * チップを表示するボタン行を生成する。
	 * @param context コンテキスト
	 * @param title タイトル
	 * @param base_id ベースID
	 * @return 行のView
	 */
	private TableRow createChipRow(Context context, String title, int base_id, int count) {
		TableRow row = new TableRow(context);

		TextView title_text_view = new TextView(context);
		title_text_view.setText(title);
		title_text_view.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		title_text_view.setTextColor(SettingManager.getColorWhite());
		row.addView(title_text_view);

		ToggleButton tgl_button1 = new ToggleButton(context);
		tgl_button1.setId(base_id + VIEWID_OFFSET_CHIPI);
		tgl_button1.setOnClickListener(this);
		tgl_button1.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		tgl_button1.setText("I");
		tgl_button1.setTextOn("I");
		tgl_button1.setTextOff("I");
		row.addView(tgl_button1);

		if(count >= 2) {
			ToggleButton tgl_button2 = new ToggleButton(context);
			tgl_button2.setId(base_id + VIEWID_OFFSET_CHIPII);
			tgl_button2.setOnClickListener(this);
			tgl_button2.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
			tgl_button2.setText("II");
			tgl_button2.setTextOn("II");
			tgl_button2.setTextOff("II");
			row.addView(tgl_button2);
		}

		if(count >= 3) {
			ToggleButton tgl_button3 = new ToggleButton(context);
			tgl_button3.setId(base_id + VIEWID_OFFSET_CHIPIII);
			tgl_button3.setOnClickListener(this);
			tgl_button3.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
			tgl_button3.setText("III");
			tgl_button3.setTextOn("III");
			tgl_button3.setTextOff("III");
			row.addView(tgl_button3);
		}
		
		return row;
	}

	/**
	 * 命中距離のシークバー行を生成する。
	 * @param context コンテキスト
	 */
	private TableRow createExplosionDistance(Context context) {
		TableRow row = new TableRow(context);
		row.setId(VIEWID_EXPLOSION_ROW);
		
		TextView title_text_view = new TextView(context);
		title_text_view.setId(VIEWID_EXPLOSION_BASE);
		title_text_view.setText("命中距離(" + mExplotionDistance + "m)");
		title_text_view.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		title_text_view.setTextColor(SettingManager.getColorWhite());
		row.addView(title_text_view);

	    TableRow.LayoutParams seekbar_row_layout = new TableRow.LayoutParams();
	    seekbar_row_layout.span = 3;
		
		SeekBar bar = new SeekBar(context);
		bar.setId(VIEWID_EXPLOSION_BAR);
		bar.setMax(mExplosionRange);
		bar.setProgress(0);
		bar.setPadding(30, 0, 30, 30);
		bar.setOnSeekBarChangeListener(new OnExplosionBarChangeListener());
		row.addView(bar, seekbar_row_layout);
		
		if(!mIsTypeExplosion) {
			row.setVisibility(View.GONE);
		}
		
		return row;
	}
	
	/**
	 * 命中距離のシークバーが変化した時の処理を行うリスナー
	 */
	private class OnExplosionBarChangeListener implements OnSeekBarChangeListener {

		/**
		 * シークバーが変化した時の処理を行う。
		 * @param seekbar シークバー
		 * @param progress シークバーの位置
		 * @param from_user ユーザー操作によるものかどうか
		 */
		@Override
		public void onProgressChanged(SeekBar seekbar, int progress, boolean from_user) {
			mExplotionDistance = progress;

			TextView title_text_view = (TextView)WeaponSimView.this.findViewById(VIEWID_EXPLOSION_BASE);
			title_text_view.setText("命中距離(" + mExplotionDistance + "m)");
			
			updateView();
		}

		/**
		 * スライド開始した時の処理を行う。
		 * @param seekbar シークバー
		 */
		@Override
		public void onStartTrackingTouch(SeekBar seekbar) {
			// 何もしない			
		}

		/**
		 * スライド終了した時の処理を行う。火力および大破計算を再実行する。
		 * @param seekbar シークバー
		 */
		@Override
		public void onStopTrackingTouch(SeekBar seekbar) {
			// 何もしない
		}
		
	}
	
	/**
	 * 耐久力のシークバー行を生成する。
	 * @param context コンテキスト
	 */
	private TableRow createLifeRow(Context context) {
		TableRow row = new TableRow(context);
		
		TextView title_text_view = new TextView(context);
		title_text_view.setId(VIEWID_DEF_LIFE);
		title_text_view.setText("耐久値(" + mDefenceLife + ")");
		title_text_view.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		title_text_view.setTextColor(SettingManager.getColorWhite());
		row.addView(title_text_view);

	    TableRow.LayoutParams seekbar_row_layout = new TableRow.LayoutParams();
	    seekbar_row_layout.span = 3;
		
		SeekBar bar = new SeekBar(context);
		bar.setMax(SpecValues.BLUST_LIFE_MAX);
		bar.setProgress(SpecValues.BLUST_LIFE_MAX);
		bar.setPadding(30, 0, 30, 30);
		bar.setOnSeekBarChangeListener(new OnLifeSeekBarChangeListener());
		row.addView(bar, seekbar_row_layout);
		
		return row;
	}
	
	/**
	 * 耐久値のシークバーが変化した時の処理を行うリスナー
	 */
	private class OnLifeSeekBarChangeListener implements OnSeekBarChangeListener {

		/**
		 * シークバーが変化した時の処理を行う。
		 * @param seekbar シークバー
		 * @param progress シークバーの位置
		 * @param from_user ユーザー操作によるものかどうか
		 */
		@Override
		public void onProgressChanged(SeekBar seekbar, int progress, boolean from_user) {
			mDefenceLife = progress;
			
			TextView title_text_view = (TextView)WeaponSimView.this.findViewById(VIEWID_DEF_LIFE);
			title_text_view.setText("耐久値(" + mDefenceLife + ")");
			
			updateView();
		}

		/**
		 * スライド開始した時の処理を行う。
		 * @param seekbar シークバー
		 */
		@Override
		public void onStartTrackingTouch(SeekBar seekbar) {
			// 何もしない
		}

		/**
		 * スライド終了した時の処理を行う。火力および大破計算を再実行する。
		 * @param seekbar シークバー
		 */
		@Override
		public void onStopTrackingTouch(SeekBar seekbar) {
			// 何もしない
		}
	}
	
	/**
	 * 装甲設定の有効/無効を選択するチェックボックス行を生成する。
	 * @param context コンテキスト
	 * @return 行
	 */
	private TableRow createArmorValidRow(Context context) {
		TableRow row = new TableRow(context);

	    TableRow.LayoutParams row_layout = new TableRow.LayoutParams();
	    row_layout.span = 4;
		
		CheckBox armor_valid_checkbox = new CheckBox(context);
		armor_valid_checkbox.setText("装甲の設定を有効にする");
		armor_valid_checkbox.setChecked(mIsArmorValid);
		armor_valid_checkbox.setOnCheckedChangeListener(new OnArmorValidChangeListener());
		armor_valid_checkbox.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		armor_valid_checkbox.setTextColor(SettingManager.getColorWhite());
		row.addView(armor_valid_checkbox, row_layout);
		
		return row;
	}
	
	/**
	 * 装甲設定の有効/無効を変化させた時の処理を行うリスナー
	 */
	private class OnArmorValidChangeListener implements OnCheckedChangeListener {

		/**
		 * 設定変更時の処理を行う。
		 */
		@Override
		public void onCheckedChanged(CompoundButton button_view, boolean is_checked) {
			mIsArmorValid = is_checked;
			updateView();
		}
	}

	/**
	 * 装甲値のシークバーを生成する。
	 * @param context コンテキスト
	 * @return 行
	 */
	private TableRow createArmorRow(Context context, String title, int offset) {
		TableRow row = new TableRow(context);
		
		TextView title_text_view = new TextView(context);
		title_text_view.setId(VIEWID_ARMOR_TEXT_BASE + offset);
		title_text_view.setText(title + "(" + mArmorArray[offset] + ")");
		title_text_view.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		title_text_view.setTextColor(SettingManager.getColorWhite());
		row.addView(title_text_view);
		
		int size = BBDataManager.SPEC_POINT.length;
		String[] spec_strs = new String[size];
		
		for(int i=0; i<size; i++) {
			String point = BBDataManager.SPEC_POINT[i];
			double value = SpecValues.getSpecValue(point, "装甲", "");
			String armor_str = SpecValues.getSpecUnit(value, "装甲");
			
			spec_strs[i] = point + " (" + armor_str + ")";
		}
		
		StringAdapter adapter = new StringAdapter(context, spec_strs);
		adapter.setMode(StringAdapter.MODE_SPINNER);
		adapter.setTextColor(SettingManager.getColorWhite());
		adapter.setBackGroundColor(SettingManager.getColorBlack());
		
		Spinner armor_spinner = new Spinner(context);
		armor_spinner.setId(VIEWID_ARMOR_LIST_BASE + offset);
		armor_spinner.setAdapter(adapter);
		armor_spinner.setOnItemSelectedListener(new OnArmorSelectedListener(offset));
		armor_spinner.setSelection(8);
		row.addView(armor_spinner);
		
	    TableRow.LayoutParams seekbar_row_layout = new TableRow.LayoutParams();
	    seekbar_row_layout.span = 2;
		
		SeekBar hit_parcent_bar = new SeekBar(context);
		hit_parcent_bar.setId(VIEWID_ARMOR_HIT_BASE + offset);
		hit_parcent_bar.setMax(100);
		hit_parcent_bar.setProgress(0);
		hit_parcent_bar.setOnSeekBarChangeListener(new OnHitSeekBarChangeListener(offset));
		row.addView(hit_parcent_bar, seekbar_row_layout);
		
		return row;
	}
	
	/**
	 * 装甲値を選択した時の処理を行うリスナー
	 */
	private class OnArmorSelectedListener implements OnItemSelectedListener {

		private int mOffset = 0;
		
		/**
		 * 初期化処理を行う。テキストビューのIDを設定する。
		 * @param offset オフセット値
		 */
		public OnArmorSelectedListener(int offset) {
			mOffset = offset;
		}

		/**
		 * 装甲選択時の処理を行う。
		 */
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			String selected_armor = BBDataManager.SPEC_POINT[position];
			mArmorArray[mOffset] = SpecValues.getSpecValue(selected_armor, "装甲", "");
			
			updateView();
		}

		/**
		 * 装甲非選択時の処理を行う。
		 */
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// 処理無し
		}
	}
	
	/**
	 * 被弾率のシークバーが変化した時の処理を行うリスナー。
	 */
	private class OnHitSeekBarChangeListener implements OnSeekBarChangeListener {
		
		private int mOffset = 0;
		
		/**
		 * 初期化処理を行う。テキストビューのIDを設定する。
		 * @param offset オフセット値
		 */
		public OnHitSeekBarChangeListener(int offset) {
			mOffset = offset;
		}

		/**
		 * シークバーが変化した時の処理を行う。
		 * @param seekbar シークバー
		 * @param progress シークバーの位置
		 * @param from_user ユーザー操作によるものかどうか
		 */
		@Override
		public void onProgressChanged(SeekBar seekbar, int progress, boolean from_user) {
			mHitPercentArray[mOffset] = progress;
			
			int per_sum = mHitPercentArray[ARMOR_HEAD] + mHitPercentArray[ARMOR_BODY] + mHitPercentArray[ARMOR_ARMS] + mHitPercentArray[ARMOR_LEGS];
			
			int size = mHitPercentArray.length;
			for(int i=size-1; i>=0; i--) {
				if(i == mOffset) {
					continue;
				}
				
				if(per_sum <= 100) {
					break;
				}
				
				int over_value = per_sum - 100;
				
				if(mHitPercentArray[i] >= over_value) {
					mHitPercentArray[i] = mHitPercentArray[i] - over_value;
					break;
				}
				else {
					mHitPercentArray[i] = 0;
				}
			}
			
			updateView();
		}

		/**
		 * スライド開始した時の処理を行う。
		 * @param seekbar シークバー
		 */
		@Override
		public void onStartTrackingTouch(SeekBar seekbar) {
			// 何もしない
		}

		/**
		 * スライド終了した時の処理を行う。火力および大破計算を再実行する。
		 * @param seekbar シークバー
		 */
		@Override
		public void onStopTrackingTouch(SeekBar seekbar) {
			// 何もしない
		}
	}
	
	/**
	 * 対象の武器データを設定する。
	 * @param target 主武器データ
	 */
	public void setData(BBData target) {
		mTargetData = target;

		mIsTypeShot = target.isShotWeapon();
		mIsTypeExplosion = target.isExplosionWeapon();
		mExplosionRange = target.getExplosionRange();

		updateExplosionBar();
		updateView();
	}
	
	/**
	 * 爆発距離のバーを更新する。
	 */
	private void updateExplosionBar() {

		if(mIsTypeExplosion) {
			SeekBar bar = (SeekBar)this.findViewById(VIEWID_EXPLOSION_BAR);
			bar.setMax(mExplosionRange);
			bar.setProgress(0);
			
			mExplotionDistance = 0;
			
			TextView title_text_view = (TextView)this.findViewById(VIEWID_EXPLOSION_BASE);
			title_text_view.setText("命中距離(" + mExplotionDistance + "m)");
		}
		else {
			TableRow row = (TableRow)this.findViewById(VIEWID_EXPLOSION_ROW);
			row.setVisibility(View.GONE);
		}
	}
	
	/**
	 * シミュレーション結果を更新する。
	 */
	private void updateView() {
		
		// 射撃武器として計算するかどうか。falseの場合は爆発武器として計算する。
		boolean is_shot = true;
		if(!mIsTypeShot || mExplotionDistance > 0) {
			is_shot = false;
		}
		
		// 火力を再計算して表示する
		double one_shot_damage    = mAttackBlust.getOneShotPowerMain(mTargetData, mChargeLevel, false, false);
		double one_shot_cs_damage = mAttackBlust.getOneShotPowerMain(mTargetData, mChargeLevel, true, false);
		double one_shot_stn_damage    = mAttackBlust.getOneShotPowerMain(mTargetData, mChargeLevel, false, true);
		double one_shot_cs_stn_damage = mAttackBlust.getOneShotPowerMain(mTargetData, mChargeLevel, true, true);
		double magazine_damage    = mAttackBlust.getMagazinePower(mTargetData);
		double sec_damage         = mAttackBlust.get1SecPower(mTargetData);
		double battle_damage      = mAttackBlust.getBattlePower(mTargetData);
		
		if(!is_shot) {
			one_shot_damage = mAttackBlust.getExplosionPower(mTargetData, mChargeLevel, false, mExplotionDistance, mExplosionRange);
			one_shot_stn_damage = mAttackBlust.getExplosionPower(mTargetData, mChargeLevel, true, mExplotionDistance, mExplosionRange);
		}
		
		if(mIsArmorValid) {
			if(is_shot) {
				one_shot_damage = mDefenceBlust.getShotDamage(mTargetData, one_shot_damage, mArmorArray[ARMOR_BODY]);
				one_shot_cs_damage = mDefenceBlust.getShotDamage(mTargetData, one_shot_cs_damage, mArmorArray[ARMOR_HEAD]);
				one_shot_stn_damage = mDefenceBlust.getShotDamage(mTargetData, one_shot_stn_damage, mArmorArray[ARMOR_BODY]);
				one_shot_cs_stn_damage = mDefenceBlust.getShotDamage(mTargetData, one_shot_cs_stn_damage, mArmorArray[ARMOR_HEAD]);
			}
			else {
				one_shot_damage = mDefenceBlust.getHitDamage(mTargetData, one_shot_damage, mArmorArray, mHitPercentArray, is_shot);
				one_shot_stn_damage = mDefenceBlust.getHitDamage(mTargetData, one_shot_stn_damage,mArmorArray, mHitPercentArray, is_shot);
			}
			magazine_damage = mDefenceBlust.getHitDamage(mTargetData, magazine_damage, mArmorArray, mHitPercentArray, is_shot);
			sec_damage = mDefenceBlust.getHitDamage(mTargetData, sec_damage, mArmorArray, mHitPercentArray, is_shot);
			battle_damage = mDefenceBlust.getHitDamage(mTargetData, battle_damage, mArmorArray, mHitPercentArray, is_shot);
		}
		
		String one_shot_damage_str    = SpecValues.getSpecUnit(one_shot_damage, "威力");
		String one_shot_cs_damage_str = SpecValues.getSpecUnit(one_shot_cs_damage, "威力");
		String magazine_damage_str    = SpecValues.getSpecUnit(magazine_damage, "威力");
		String sec_damage_str         = SpecValues.getSpecUnit(sec_damage, "威力");
		String battle_damage_str      = SpecValues.getSpecUnit(battle_damage, "威力");
		
		if(!is_shot) {
			one_shot_cs_damage_str = "－";
			magazine_damage_str = "－";
			sec_damage_str = "－";
			battle_damage_str = "－";
		}
	
		updateTextView(VIEWID_ONESHOT_BASE, VIEWID_OFFSET_DAMAGE, one_shot_damage_str);
		updateTextView(VIEWID_ONESHOT_CS_BASE, VIEWID_OFFSET_DAMAGE, one_shot_cs_damage_str);
		updateTextView(VIEWID_MAGAZINE_BASE, VIEWID_OFFSET_DAMAGE, magazine_damage_str);
		updateTextView(VIEWID_1SEC_BASE, VIEWID_OFFSET_DAMAGE, sec_damage_str);
		updateTextView(VIEWID_BATTLE_BASE, VIEWID_OFFSET_DAMAGE, battle_damage_str);
		
		// ノックバック有無を再計算して表示する
		boolean one_shot_kb = mDefenceBlust.isBack(one_shot_stn_damage);
		boolean one_shot_cs_kb = mDefenceBlust.isBack(one_shot_cs_stn_damage);
		
		String one_shot_kb_str = getJudgeString(one_shot_kb);
		String one_shot_cs_kb_str = getJudgeString(one_shot_cs_kb);

		if(!is_shot) {
			one_shot_cs_kb_str = "－";
		}
	
		updateTextView(VIEWID_ONESHOT_BASE, VIEWID_OFFSET_KB, one_shot_kb_str);
		updateTextView(VIEWID_ONESHOT_CS_BASE, VIEWID_OFFSET_KB, one_shot_cs_kb_str);
		updateTextView(VIEWID_MAGAZINE_BASE, VIEWID_OFFSET_KB, "－");
		updateTextView(VIEWID_1SEC_BASE, VIEWID_OFFSET_KB, "－");
		updateTextView(VIEWID_BATTLE_BASE, VIEWID_OFFSET_KB, "－");

		// 転倒有無を再計算して表示する
		boolean one_shot_down = mDefenceBlust.isDown(one_shot_stn_damage);
		boolean one_shot_cs_down = mDefenceBlust.isDown(one_shot_cs_stn_damage);
		
		String one_shot_down_str = getJudgeString(one_shot_down);
		String one_shot_cs_down_str = getJudgeString(one_shot_cs_down);

		if(!is_shot) {
			one_shot_cs_down_str = "－";
		}
	
		updateTextView(VIEWID_ONESHOT_BASE, VIEWID_OFFSET_DOWN, one_shot_down_str);
		updateTextView(VIEWID_ONESHOT_CS_BASE, VIEWID_OFFSET_DOWN, one_shot_cs_down_str);
		updateTextView(VIEWID_MAGAZINE_BASE, VIEWID_OFFSET_DOWN, "－");
		updateTextView(VIEWID_1SEC_BASE, VIEWID_OFFSET_DOWN, "－");
		updateTextView(VIEWID_BATTLE_BASE, VIEWID_OFFSET_DOWN, "－");

		// 大破有無を再計算して表示する
		boolean one_shot_break = mDefenceBlust.isBreak(one_shot_damage, mDefenceLife, mFatalChipLv);
		boolean one_shot_cs_break = mDefenceBlust.isBreak(one_shot_cs_damage, mDefenceLife, mFatalChipLv);
		
		String one_shot_break_str = getJudgeString(one_shot_break);
		String one_shot_cs_break_str = getJudgeString(one_shot_cs_break);

		if(!is_shot) {
			one_shot_cs_break_str = "－";
		}
	
		updateTextView(VIEWID_ONESHOT_BASE, VIEWID_OFFSET_BREAK, one_shot_break_str);
		updateTextView(VIEWID_ONESHOT_CS_BASE, VIEWID_OFFSET_BREAK, one_shot_cs_break_str);
		updateTextView(VIEWID_MAGAZINE_BASE, VIEWID_OFFSET_BREAK, "－");
		updateTextView(VIEWID_1SEC_BASE, VIEWID_OFFSET_BREAK, "－");
		updateTextView(VIEWID_BATTLE_BASE, VIEWID_OFFSET_BREAK, "－");

		int size = mArmorArray.length;
		for(int i=0; i<size; i++) {
			String per_str = mHitPercentArray[i] + "(%)";
			
			TextView title_text_view = (TextView)this.findViewById(VIEWID_ARMOR_TEXT_BASE + i);
			title_text_view.setText(ARMOR_TEXT_TITLE[i] + "(" + per_str + ")");
			
			SeekBar hit_bar = (SeekBar)this.findViewById(VIEWID_ARMOR_HIT_BASE + i);
			hit_bar.setProgress(mHitPercentArray[i]);
		}
	}
	
	/**
	 * シミュレーション結果を表示するテキストビューを取得する。
	 * @param base_id ベースID
	 * @param offset オフセット値
	 * @return テキストビュー
	 */
	private void updateTextView(int base_id, int offset, String text) {
		TextView text_view = (TextView)this.findViewById(base_id + offset);
		text_view.setText(text);
	}
	
	/**
	 * KB, 転倒, 大破の判定結果から表示する文字列を取得する。
	 * @param status 判定結果
	 * @return 表示する文字列
	 */
	private String getJudgeString(boolean status) {
		if(status) {
			return "する";
		}
		else {
			return "しない";
		}
	}

	/**
	 * ボタンが押下された時の処理を行う。
	 */
	@Override
	public void onClick(View view) {
		int id = view.getId();
		boolean checked = !((ToggleButton)view).isChecked();  // 既に変更された後の値が設定されるので反転する
		
		updateSetting(id, checked);
		updateView();
	}
	
	/**
	 * 設定状態を更新する。
	 * @param id 対象のID
	 * @param checked ボタンのチェック状態
	 */
	private void updateSetting(int id, boolean checked) {

		switch(id) {
		
			//----------------------------------------------------------
			// 攻撃側
			//----------------------------------------------------------
			
			// 実弾速射
			case VIEWID_SPEEDUP_BASE + VIEWID_OFFSET_CHIPI:
				updateButtonLamp(VIEWID_SPEEDUP_BASE, VIEWID_OFFSET_CHIPI, checked);
				updateAttackChip("実弾速射", mSpeedUpChips, VIEWID_OFFSET_CHIPI, checked);
				break;

			case VIEWID_SPEEDUP_BASE + VIEWID_OFFSET_CHIPII:
				updateButtonLamp(VIEWID_SPEEDUP_BASE, VIEWID_OFFSET_CHIPII, checked);
				updateAttackChip("実弾速射", mSpeedUpChips, VIEWID_OFFSET_CHIPII, checked);
				break;

			case VIEWID_SPEEDUP_BASE + VIEWID_OFFSET_CHIPIII:
				updateButtonLamp(VIEWID_SPEEDUP_BASE, VIEWID_OFFSET_CHIPIII, checked);
				updateAttackChip("実弾速射", mSpeedUpChips, VIEWID_OFFSET_CHIPIII, checked);
				break;

			// ニュード威力上昇
			case VIEWID_NEWDUP_BASE + VIEWID_OFFSET_CHIPI:
				updateButtonLamp(VIEWID_NEWDUP_BASE, VIEWID_OFFSET_CHIPI, checked);
				updateAttackChip("ニュード威力上昇", mNewdUpChips, VIEWID_OFFSET_CHIPI, checked);
				break;

			case VIEWID_NEWDUP_BASE + VIEWID_OFFSET_CHIPII:
				updateButtonLamp(VIEWID_NEWDUP_BASE, VIEWID_OFFSET_CHIPII, checked);
				updateAttackChip("ニュード威力上昇", mNewdUpChips, VIEWID_OFFSET_CHIPII, checked);
				break;

			case VIEWID_NEWDUP_BASE + VIEWID_OFFSET_CHIPIII:
				updateButtonLamp(VIEWID_NEWDUP_BASE, VIEWID_OFFSET_CHIPIII, checked);
				updateAttackChip("ニュード威力上昇", mNewdUpChips, VIEWID_OFFSET_CHIPIII, checked);
				break;

			// プリサイスショット
			case VIEWID_PRECISE_BASE + VIEWID_OFFSET_CHIPI:
				updateButtonLamp(VIEWID_PRECISE_BASE, VIEWID_OFFSET_CHIPI, checked);
				updateAttackChip("プリサイスショット", mPriciseChips, VIEWID_OFFSET_CHIPI, checked);
				break;

			case VIEWID_PRECISE_BASE + VIEWID_OFFSET_CHIPII:
				updateButtonLamp(VIEWID_PRECISE_BASE, VIEWID_OFFSET_CHIPII, checked);
				updateAttackChip("プリサイスショット", mPriciseChips, VIEWID_OFFSET_CHIPII, checked);
				break;

			case VIEWID_PRECISE_BASE + VIEWID_OFFSET_CHIPIII:
				updateButtonLamp(VIEWID_PRECISE_BASE, VIEWID_OFFSET_CHIPIII, checked);
				updateAttackChip("プリサイスショット", mPriciseChips, VIEWID_OFFSET_CHIPIII, checked);
				break;

			// フェイタルアタック
			case VIEWID_FATAL_BASE + VIEWID_OFFSET_CHIPI:
				updateButtonLamp(VIEWID_FATAL_BASE, VIEWID_OFFSET_CHIPI, checked);
				updateFatalChip(1, checked);
				break;

			case VIEWID_FATAL_BASE + VIEWID_OFFSET_CHIPII:
				updateButtonLamp(VIEWID_FATAL_BASE, VIEWID_OFFSET_CHIPII, checked);
				updateFatalChip(2, checked);
				break;

			// アンチスタビリティ
			case VIEWID_ANTISTN_BASE + VIEWID_OFFSET_CHIPI:
				updateButtonLamp(VIEWID_ANTISTN_BASE, VIEWID_OFFSET_CHIPI, checked);
				updateAttackChip("アンチスタビリティ", mAntiStnChips, VIEWID_OFFSET_CHIPI, checked);
				break;

			case VIEWID_ANTISTN_BASE + VIEWID_OFFSET_CHIPII:
				updateButtonLamp(VIEWID_ANTISTN_BASE, VIEWID_OFFSET_CHIPII, checked);
				updateAttackChip("アンチスタビリティ", mAntiStnChips, VIEWID_OFFSET_CHIPII, checked);
				break;

			case VIEWID_ANTISTN_BASE + VIEWID_OFFSET_CHIPIII:
				updateButtonLamp(VIEWID_ANTISTN_BASE, VIEWID_OFFSET_CHIPIII, checked);
				updateAttackChip("アンチスタビリティ", mAntiStnChips, VIEWID_OFFSET_CHIPIII, checked);
				break;

			
			// チャージレベル
			case VIEWID_CHARGE_LEVEL_BASE + VIEWID_OFFSET_CHIPI:
				updateButtonLamp(VIEWID_CHARGE_LEVEL_BASE, VIEWID_OFFSET_CHIPI, checked);
				updateChargeLevel(1, checked);
				break;

			case VIEWID_CHARGE_LEVEL_BASE + VIEWID_OFFSET_CHIPII:
				updateButtonLamp(VIEWID_CHARGE_LEVEL_BASE, VIEWID_OFFSET_CHIPII, checked);
				updateChargeLevel(2, checked);
				break;

			//----------------------------------------------------------
			// 防御側
			//----------------------------------------------------------

			// 大破抑制
			case VIEWID_REDUCE_BREAK_BASE + VIEWID_OFFSET_CHIPI:
				updateButtonLamp(VIEWID_REDUCE_BREAK_BASE, VIEWID_OFFSET_CHIPI, checked);
				updateDefenceChip("大破抑制", mReduceBreakChips, VIEWID_OFFSET_CHIPI, checked);
				break;

			// 転倒耐性
			case VIEWID_GURAD_DOWN_BASE + VIEWID_OFFSET_CHIPI:
				updateButtonLamp(VIEWID_GURAD_DOWN_BASE, VIEWID_OFFSET_CHIPI, checked);
				updateDefenceChip("転倒耐性", mGuardDownChips, VIEWID_OFFSET_CHIPI, checked);
				break;

			case VIEWID_GURAD_DOWN_BASE + VIEWID_OFFSET_CHIPII:
				updateButtonLamp(VIEWID_GURAD_DOWN_BASE, VIEWID_OFFSET_CHIPII, checked);
				updateDefenceChip("転倒耐性", mGuardDownChips, VIEWID_OFFSET_CHIPII, checked);
				break;

			case VIEWID_GURAD_DOWN_BASE + VIEWID_OFFSET_CHIPIII:
				updateButtonLamp(VIEWID_GURAD_DOWN_BASE, VIEWID_OFFSET_CHIPIII, checked);
				updateDefenceChip("転倒耐性", mGuardDownChips, VIEWID_OFFSET_CHIPIII, checked);
				break;
		}
	}

	/**
	 * 対象ベースIDに属するボタンのチェック状態を変更する。
	 * @param base_id ベースID
	 * @param offset オフセット
	 * @param checked チェック状態
	 */
	private void updateButtonLamp(int base_id, int offset, boolean checked) {
		setCheckedButtonLamp(base_id, VIEWID_OFFSET_CHIPI, false);
		setCheckedButtonLamp(base_id, VIEWID_OFFSET_CHIPII, false);
		setCheckedButtonLamp(base_id, VIEWID_OFFSET_CHIPIII, false);
		
		setCheckedButtonLamp(base_id, offset, !checked);
	}
	
	/**
	 * 指定のボタンのチェック状態を変更する。
	 * @param base_id ベースID
	 * @param offset オフセット
	 * @param checked チェック状態
	 */
	private void setCheckedButtonLamp(int base_id, int offset, boolean checked) {
		
		View btn1 = this.findViewById(base_id + offset);
		if(btn1 == null) {
			return;
		}
		
		((ToggleButton)btn1).setChecked(checked);
	}
	
	/**
	 * 攻撃側チップ情報を更新する。
	 * @param chip_series チップの系統名
	 * @param chip_list チップリスト
	 * @param level レベル
	 * @param is_setting 現在の設定状態
	 */
	private void updateAttackChip(String chip_series, ArrayList<BBData> chip_list, int level, boolean is_setting) {
		//mAttackBlust.removeChipSeries(chip_series);

		//if(!is_setting) {
		//	mAttackBlust.addChip(chip_list.get(level));
		//}
		
	}

	/**
	 * 防御側チップ情報を更新する。
	 * @param chip_series チップの系統名
	 * @param chip_list チップリスト
	 * @param level レベル
	 * @param is_setting 現在の設定状態
	 */
	private void updateDefenceChip(String chip_series, ArrayList<BBData> chip_list, int level, boolean is_setting) {
		//mDefenceBlust.removeChipSeries(chip_series);

		//if(!is_setting) {
		//	mDefenceBlust.addChip(chip_list.get(level));
		//}
		
	}
	/**
	 * フェイタルチップの設定状態を更新する。
	 * ※防御側アセンの大破計算の引数として必要であるため、他の攻撃チップとは別枠で設定状態を管理する。
	 * @param level チップレベル
	 * @param checked チェック状態
	 */
	private void updateFatalChip(int level, boolean checked) {
		if(checked) {
			mFatalChipLv = 0;
		}
		else {
			mFatalChipLv = level;
		}
	}
	
	/**
	 * チャージレベルを更新する。
	 * @param level レベル
	 * @param checked 現在の設定状態
	 */
	private void updateChargeLevel(int level, boolean checked) {
		if(checked) {
			mChargeLevel = 0;
		}
		else {
			mChargeLevel = level;
		}
	}
	

}
