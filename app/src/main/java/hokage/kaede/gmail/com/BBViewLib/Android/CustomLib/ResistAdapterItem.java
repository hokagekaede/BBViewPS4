package hokage.kaede.gmail.com.BBViewLib.Android.CustomLib;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomData;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 「耐性」画面の武器一覧の中の表示内容を生成するクラス。
 */
public class ResistAdapterItem extends TableLayout {
	public static final int MODE_SHOT      = 1;
	public static final int MODE_EXPLOSION = 2;
	public static final int MODE_SLASH     = 3;

	private TableRow[] mTableRows;
	private TextView[][] mTextViews;

	private static final int TABLE_ROW_MAX = 7;

	private int mPatternMode = MODE_SHOT;
	private int mColumnCount = 0;

	private static final String[] TITLE_ROW_STR_SHOT = { "攻撃方法", "CS時ダメ", "破", "転", "仰" };
	private static final String[] TITLE_ROW_STR_EXPLOSION = { "攻撃方法", "空爆時ダメ", "破", "転", "仰", "地爆時ダメ", "破", "転", "仰" };
	private static final String[] TITLE_ROW_STR_SLASH = { "攻撃方法", "通常攻撃ダメ", "破", "転", "仰", "特殊攻撃ダメ", "破", "転", "仰" };

	private static final String[] PATTERN_NORMAL = { "" };
	private static final String[] PATTERN_CHARGE = { "Charge0", "Charge1", "Charge2" };

	public ResistAdapterItem(Context context, int mode) {
		super(context);
		this.setColumnStretchable(0, true);    // 列幅最大表示
		
		mPatternMode = mode;
		String[] target_title;

		if(mPatternMode == MODE_SHOT) {
			mColumnCount = TITLE_ROW_STR_SHOT.length;
			target_title = TITLE_ROW_STR_SHOT;
		}
		else if(mPatternMode == MODE_EXPLOSION) {
			mColumnCount = TITLE_ROW_STR_EXPLOSION.length;
			target_title = TITLE_ROW_STR_EXPLOSION;
		}
		else {
			mColumnCount = TITLE_ROW_STR_SLASH.length;
			target_title = TITLE_ROW_STR_SLASH;
		}

		mTableRows = new TableRow[TABLE_ROW_MAX];
		mTextViews = new TextView[TABLE_ROW_MAX][];
		
		for(int row=0; row<TABLE_ROW_MAX; row++) {
			mTableRows[row] = new TableRow(context);
			mTextViews[row] = new TextView[mColumnCount];
			
			for(int col=0; col<mColumnCount; col++) {
				mTextViews[row][col] = new TextView(context);
				mTextViews[row][col].setPadding(5, 5, 5, 5);
				mTextViews[row][col].setGravity(Gravity.RIGHT);
				mTextViews[row][col].setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_SMALL));
				mTableRows[row].addView(mTextViews[row][col]);
			}

			mTextViews[row][0].setGravity(Gravity.LEFT);
			this.addView(mTableRows[row]);
		}

		for(int col=1; col<mColumnCount; col++) {
			mTextViews[0][col].setText(target_title[col]);
		}
	}

	/**
	 * テーブルの内容を更新する
	 * @param custom_data 被弾側のカスタムデータ
	 * @param data 攻撃側の武器データ
	 * @param is_show_typeb 攻撃側の武器をタイプB扱いするかどうか
	 */
	public void update(CustomData custom_data, BBData data, boolean is_show_typeb) {

		// タイプB設定が有効の場合は、武器のデータをタイプBに切り替える
		String name = data.getNameWithType(is_show_typeb);
		BBData item = data;
		if(is_show_typeb) {
			BBData item_typeb = item.getTypeB();

			if(item_typeb != null) {
				item = item_typeb;
			}
		}

		if (mPatternMode == MODE_SHOT) {
			update_shot(custom_data, item, name);
		}
		else if(mPatternMode == MODE_EXPLOSION) {
			update_explosion(custom_data, item, name);
		}
		else {
			update_slash(custom_data, item, name);
		}
	}

	/**
	 * テーブルのデータを更新する。(射撃武器)
	 * @param custom_data 被弾側のカスタムデータ
	 * @param item 攻撃側の武器データ
	 * @param name 武器の名称
	 */
	private void update_shot(CustomData custom_data, BBData item, String name) {

		String[] pattern = selectPattern(item);
		int size = pattern.length;

		mTextViews[0][0].setText(name);

		for(int idx=0; idx<size; idx++) {
			int target_row = idx + 1;

			double damage = custom_data.getShotDamage(item, BBDataManager.BLUST_PARTS_HEAD, idx);
			mTextViews[target_row][0].setText(pattern[idx]);
			mTextViews[target_row][1].setText(String.format("%.0f", damage));
			mTextViews[target_row][2].setText(getJudgeString(custom_data.isBreak(damage)));
			mTextViews[target_row][3].setText(getJudgeString(custom_data.isDown(damage)));
			mTextViews[target_row][4].setText(getJudgeString(custom_data.isBack(damage)));

			mTableRows[target_row].setVisibility(View.VISIBLE);
		}

		for(int row=size+1; row<TABLE_ROW_MAX; row++) {
			mTableRows[row].setVisibility(View.GONE);
		}
	}

	/**
	 * テーブルのデータを更新する。(爆発武器)
	 * @param custom_data 被弾側のカスタムデータ
	 * @param item 攻撃側の武器データ
	 * @param name 武器の名称
	 */
	private void update_explosion(CustomData custom_data, BBData item, String name) {

		String[] pattern = selectPattern(item);
		int size = pattern.length;

		mTextViews[0][0].setText(name);

		for(int idx=0; idx<size; idx++) {
			int target_row = idx + 1;

			double damage = custom_data.getExplosionHeadDamage(item, idx);
			mTextViews[target_row][0].setText(pattern[idx]);
			mTextViews[target_row][1].setText(String.format("%.0f", damage));
			mTextViews[target_row][2].setText(getJudgeString(custom_data.isBreak(damage)));
			mTextViews[target_row][3].setText(getJudgeString(custom_data.isDown(damage)));
			mTextViews[target_row][4].setText(getJudgeString(custom_data.isBack(damage)));

			damage = custom_data.getExplosionLegsDamage(item, idx);
			mTextViews[target_row][5].setText(String.format("%.0f", damage));
			mTextViews[target_row][6].setText(getJudgeString(custom_data.isBreak(damage)));
			mTextViews[target_row][7].setText(getJudgeString(custom_data.isDown(damage)));
			mTextViews[target_row][8].setText(getJudgeString(custom_data.isBack(damage)));

			mTableRows[target_row].setVisibility(View.VISIBLE);
		}

		for(int row=size+1; row<TABLE_ROW_MAX; row++) {
			mTableRows[row].setVisibility(View.GONE);
		}
	}

	/**
	 * テーブルのデータを更新する。(近接武器)
	 * @param custom_data 被弾側のカスタムデータ
	 * @param item 攻撃側の武器データ
	 * @param name 武器の名称
	 */
	private void update_slash(CustomData custom_data, BBData item, String name) {

		String[] pattern = selectPattern(item);
		int size = pattern.length;

		mTextViews[0][0].setText(name);

		for(int idx=0; idx<size; idx++) {
			int target_row = idx + 1;

			double damage = custom_data.getSlashDamage(item, false, idx);
			mTextViews[target_row][0].setText(pattern[idx]);
			mTextViews[target_row][1].setText(String.format("%.0f", damage));
			mTextViews[target_row][2].setText(getJudgeString(custom_data.isBreak(damage)));
			mTextViews[target_row][3].setText(getJudgeString(custom_data.isDown(damage)));
			mTextViews[target_row][4].setText(getJudgeString(custom_data.isBack(damage)));

			damage = custom_data.getSlashDamage(item, true, idx);
			mTextViews[target_row][5].setText(String.format("%.0f", damage));
			mTextViews[target_row][6].setText(getJudgeString(custom_data.isBreak(damage)));
			mTextViews[target_row][7].setText(getJudgeString(custom_data.isDown(damage)));
			mTextViews[target_row][8].setText(getJudgeString(custom_data.isBack(damage)));

			mTableRows[target_row].setVisibility(View.VISIBLE);
		}

		for(int row=size+1; row<TABLE_ROW_MAX; row++) {
			mTableRows[row].setVisibility(View.GONE);
		}
	}

	/**
	 * テーブルに表示するパターンを選択する
	 * @param data 
	 * @return
	 */
	private String[] selectPattern(BBData data) {
		String[] pattern = PATTERN_NORMAL;

		if(data.isChargeWeapon()) {
			pattern = PATTERN_CHARGE;
		}
		
		return pattern;
	}
	
	private static String getJudgeString(boolean flag) {
		if(flag) {
			return "×";
		}
		else {
			return "○";
		}
	}
	
	/**
	 * 表示モードを取得する。
	 */
	public int getMode() {
		return mPatternMode;
	}

}
