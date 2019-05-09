package hokage.kaede.gmail.com.BBViewLib.Android.CustomLib;

import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.ViewBuilder;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataComparator;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomData;
import hokage.kaede.gmail.com.BBViewLib.Java.SpecValues;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;

/**
 * 性能画面およびアセン比較画面の比較行を生成するクラス。
 */
public class SpecArray {
	
	/**
	 * スペック表示行のデータクラス。
	 * スペック項目とスペック値(補正前、補正後)から表示色を決定する。
	 */
	public static class SpecRow {
		private String[] mValues = new String[4];
		private int[] mColors = new int[4];
		
		public static final int KEY_INDEX = 0;
		public static final int KEY_NORMAL = 1;
		public static final int KEY_REAL = 2;
		public static final int KEY_MEMO = 3;
		
		/**
		 * 初期化を行う。
		 * @param key スペック項目
		 * @param normal_value 補正前の値
		 * @param real_value 補正後の値
		 * @param is_km_per_hour 速度の単位設定
		 */
		public SpecRow(String key, double normal_value, double real_value, boolean is_km_per_hour) {
			init(key, normal_value, real_value, is_km_per_hour);
			
			mValues[KEY_MEMO] = "";
			mColors[KEY_MEMO] = SettingManager.getColorWhite();
		}

		/**
		 * 初期化を行う。
		 * @param key スペック項目
		 * @param normal_value 補正前の値
		 * @param real_value 補正後の値
		 * @param memo_value 追加情報の値。例えば、アンチスタビリティの火力。
		 * @param is_km_per_hour 速度の単位設定
		 */
		public SpecRow(String key, double normal_value, double real_value, double memo_value, boolean is_km_per_hour) {
			init(key, normal_value, real_value, is_km_per_hour);

			BBDataComparator cmp = new BBDataComparator(key, true, BBViewSetting.IS_KM_PER_HOUR);
			int ret_cmp = cmp.compareValue(normal_value, memo_value);

			if(ret_cmp > 0) {
				mColors[KEY_MEMO] = SettingManager.getColorMazenta();
			}
			else if(ret_cmp < 0) {
				mColors[KEY_MEMO] = SettingManager.getColorCyan();
			}
			else {
				mColors[KEY_MEMO] = SettingManager.getColorWhite();
			}
		}
		
		/**
		 * 初期化を行う。
		 * 2つのコンストラクタがコールし、追加情報のデータ以外を初期化する。
		 * @param key スペック項目
		 * @param normal_value 補正前の値
		 * @param real_value 補正後の値
		 * @param is_km_per_hour 速度の単位設定
		 */
		private void init(String key, double normal_value, double real_value, boolean is_km_per_hour) {
			mValues[KEY_INDEX] = key;
			mValues[KEY_NORMAL] = SpecValues.getSpecUnit(normal_value, key, is_km_per_hour);
			mValues[KEY_REAL] = SpecValues.getSpecUnit(real_value, key, is_km_per_hour);
			
			mColors = ViewBuilder.getColors(mColors, normal_value, real_value, key);
		}

		/**
		 * 表示する文字列を設定する。
		 * @param normal_str 補正前の表示文字列
		 * @param real_str 補正後の表示文字列
		 */
		public void setValues(String normal_str, String real_str) {
			mValues[KEY_NORMAL] = normal_str;
			mValues[KEY_REAL] = real_str;
		}
		
		/**
		 * 表示する文字列を設定する。
		 * @param normal_str 補正前の表示文字列
		 * @param real_str 補正後の表示文字列
		 * @param memo_str 追加情報の表示文字列
		 */
		public void setValues(String normal_str, String real_str, String memo_str) {
			mValues[KEY_NORMAL] = normal_str;
			mValues[KEY_REAL] = real_str;
			mValues[KEY_MEMO] = memo_str;
		}
		
		/**
		 * 指定の位置の表示文字列を設定する
		 * @param target_str 設定する表示文字列
		 * @param index 設定先。
		 */
		public void setValues(String target_str, int index) {
			if(index < mValues.length) {
				mValues[index] = target_str;
			}
		}
		
		/**
		 * 文字色をクリアする。
		 */
		public void clearColors() {
			mColors[KEY_INDEX] = SettingManager.getColorWhite();
			mColors[KEY_NORMAL] = SettingManager.getColorWhite();
			mColors[KEY_REAL] = SettingManager.getColorWhite();
			mColors[KEY_MEMO] = SettingManager.getColorWhite();
		}
		
		/**
		 * 表示文字列の配列を取得する。
		 * @return 表示文字列の配列
		 */
		public String[] getValues() {
			return mValues;
		}
		
		/**
		 * 表示色の配列を取得する。
		 * @return 表示色の配列
		 */
		public int[] getColors() {
			return mColors;
		}
	}
	
	//----------------------------------------------------------
	// アセン共通のスペック
	//----------------------------------------------------------
	
	/**
	 * パーツデータの配列を生成する。
	 * @param data アセンデータ
	 * @param blust_type 兵装名
	 * @param target_key スペック名
	 * @return 配列
	 */
	public static SpecRow getPartsSpecArray(CustomData data, String blust_type, String target_key) {
		String normal_point = data.getPoint(target_key);
		double normal_value = SpecValues.getSpecValue(normal_point, target_key, BBViewSetting.IS_KM_PER_HOUR);
		String normal_value_str = SpecValues.getSpecUnit(normal_value, target_key, BBViewSetting.IS_KM_PER_HOUR);
		
		double real_value;
		if(blust_type.equals("")) {
			real_value = data.getSpecValue(target_key);
		}
		else {
			real_value = data.getSpecValue(target_key, blust_type);
		}
		
		String real_point = SpecValues.getPoint(target_key, real_value, BBViewSetting.IS_KM_PER_HOUR, data.isHoverLegs());
		
		String real_value_str = SpecValues.getSpecUnit(real_value, target_key, BBViewSetting.IS_KM_PER_HOUR);

		// スペックと内部値を結合する
		if(BBDataComparator.isPointKey(target_key)) {
			normal_value_str = normal_point + " (" + normal_value_str + ")";
			real_value_str = real_point + " (" + real_value_str + ")"; 
		}

		SpecRow row = new SpecRow(target_key, normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
		row.setValues(normal_value_str, real_value_str);
		return row;
	}

	/**
	 * アセン比較用のパーツデータの配列を生成する。
	 * @param from_data 比較元のアセンデータ
	 * @param to_data 比較先のアセンデータ
	 * @param blust_type 兵装名
	 * @param target_key スペック名
	 * @return 配列
	 */
	public static SpecRow getCmpPartsSpecArray(CustomData from_data, CustomData to_data, String blust_type, String target_key) {
		double from_value, to_value;
		if(blust_type.equals("")) {
			from_value = from_data.getSpecValue(target_key);
			to_value = to_data.getSpecValue(target_key);
		}
		else {
			from_value = from_data.getSpecValue(target_key, blust_type);
			to_value = to_data.getSpecValue(target_key, blust_type);
		}
		
		String from_point = SpecValues.getPoint(target_key, from_value, BBViewSetting.IS_KM_PER_HOUR, from_data.isHoverLegs());
		String to_point = SpecValues.getPoint(target_key, to_value, BBViewSetting.IS_KM_PER_HOUR, to_data.isHoverLegs());
		
		String from_value_str = SpecValues.getSpecUnit(from_value, target_key, BBViewSetting.IS_KM_PER_HOUR);
		String to_value_str = SpecValues.getSpecUnit(to_value, target_key, BBViewSetting.IS_KM_PER_HOUR);

		// スペックと内部値を結合する
		if(BBDataComparator.isPointKey(target_key)) {
			from_value_str = from_point + " (" + from_value_str + ")";
			to_value_str = to_point + " (" + to_value_str + ")"; 
		}

		SpecRow row = new SpecRow(target_key, from_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
		row.setValues(from_value_str, to_value_str);
		return row;
	}

	/**
	 * DEF回復の配列を生成する。隣にDEF回復時間を併記する。
	 * @param data アセンデータ
	 * @param blust_type 兵装名
	 * @return 配列
	 */
	public static SpecRow getDefRecoverArray(CustomData data, String blust_type) {
		String key = "DEF回復";
		String normal_point = data.getPoint(key);
		double normal_value = SpecValues.getSpecValue(normal_point, key, BBViewSetting.IS_KM_PER_HOUR);
		String normal_value_str = SpecValues.getSpecUnit(normal_value, key, BBViewSetting.IS_KM_PER_HOUR);
		
		double real_value = data.getDefRecover(blust_type);
		String real_point = SpecValues.getPoint(key, real_value, BBViewSetting.IS_KM_PER_HOUR, data.isHoverLegs());
		String real_value_str = SpecValues.getSpecUnit(real_value, key, BBViewSetting.IS_KM_PER_HOUR);

		// スペックと内部値を結合する
		normal_value_str = normal_point + " (" + normal_value_str + ")";
		real_value_str = real_point + " (" + real_value_str + ")"; 

		// DEF回復の場合、隣に回復時間を併記する
		BBData parts = data.getParts(BBDataManager.BLUST_PARTS_HEAD);
		normal_value_str = String.format("%s (%s)", normal_value_str,
				SpecValues.getSpecUnit(parts.getDefRecoverTime(), "回復時間", BBViewSetting.IS_KM_PER_HOUR));
		real_value_str = String.format("%s (%s)", real_value_str,
				SpecValues.getSpecUnit(data.getDefRecoverTime(), "回復時間", BBViewSetting.IS_KM_PER_HOUR));
		
		SpecRow row = new SpecRow(key, normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
		row.setValues(normal_value_str, real_value_str);
		return row;
	}

	/**
	 * ブースターの配列を生成する。隣に最大ステップ数を併記する。
	 * @param data アセンデータ
	 * @param blust_type 兵装名
	 * @return 配列
	 */
	public static SpecRow getBoostArray(CustomData data, String blust_type) {
		String key = "ブースター";
		String normal_point = data.getPoint(key);
		double normal_value = SpecValues.getSpecValue(normal_point, key, BBViewSetting.IS_KM_PER_HOUR);
		String normal_value_str = SpecValues.getSpecUnit(normal_value, key, BBViewSetting.IS_KM_PER_HOUR);
		
		double real_value = data.getBoost(blust_type);
		String real_point = SpecValues.getPoint(key, real_value, BBViewSetting.IS_KM_PER_HOUR, data.isHoverLegs());
		String real_value_str = SpecValues.getSpecUnit(real_value, key, BBViewSetting.IS_KM_PER_HOUR);

		// スペックと内部値を結合する
		normal_value_str = normal_point + " (" + normal_value_str + ")";
		real_value_str = real_point + " (" + real_value_str + ")";

		// 隣に最大ステップ数を併記する
		BBData parts = data.getParts(BBDataManager.BLUST_PARTS_BODY);
		normal_value_str = String.format("%s (%s)", normal_value_str,
				SpecValues.getSpecUnit(parts.getStepMaxCount(), BBData.STEP_MAX_COUNT_KEY, BBViewSetting.IS_KM_PER_HOUR));
		real_value_str = String.format("%s (%s)", real_value_str,
				SpecValues.getSpecUnit(data.getStepMaxCount(blust_type), BBData.STEP_MAX_COUNT_KEY, BBViewSetting.IS_KM_PER_HOUR));

		SpecRow row = new SpecRow(key, normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
		row.setValues(normal_value_str, real_value_str);
		return row;
	}

	/**
	 * ブースター回復時間の配列を生成する。
	 * @param data アセンデータ
	 * @param blust_type 兵装名
	 * @return 配列
	 */
	public static SpecRow getBoostChargeTimeArray(CustomData data, String blust_type) {
		String key = "ブースター回復時間";
		BBData parts = data.getParts(BBDataManager.BLUST_PARTS_BODY);
		
		double normal_value = parts.getBoostChargeTime();
		double real_value = data.getBoostChargeTime(blust_type);

		String normal_value_str = SpecValues.getSpecUnit(normal_value, key, BBViewSetting.IS_KM_PER_HOUR);
		String real_value_str = SpecValues.getSpecUnit(real_value, key, BBViewSetting.IS_KM_PER_HOUR);

		SpecRow row = new SpecRow(key, normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
		row.setValues(normal_value_str, real_value_str);
		return row;
	}

	/**
	 * アセン比較用のチップ使用量の配列を生成する
	 * @param from_data 比較元のアセンデータ
	 * @param to_data 比較先のアセンデータ
	 * @return 配列
	 */
	public static SpecRow getCmpChipWeight(CustomData from_data, CustomData to_data) {
		double from_value, to_value;
		from_value = from_data.getChipWeight();
		to_value = to_data.getChipWeight();

		return new SpecRow("チップ使用量", from_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
	}
	
	/**
	 * アセン比較用の装甲平均値の配列を生成する
	 * @param from_data 比較元のアセンデータ
	 * @param to_data 比較先のアセンデータ
	 * @return 配列
	 */
	public static SpecRow getCmpArmorAveSpecArray(CustomData from_data, CustomData to_data, String blust_type) {
		double from_value, to_value;
		if(blust_type.equals("")) {
			from_value = from_data.getArmorAve();
			to_value = to_data.getArmorAve();			
		}
		else {
			from_value = from_data.getArmorAve(blust_type);
			to_value = to_data.getArmorAve(blust_type);
		}

		String from_point = SpecValues.getPoint("装甲", from_value, BBViewSetting.IS_KM_PER_HOUR, from_data.isHoverLegs());
		String to_point = SpecValues.getPoint("装甲", to_value, BBViewSetting.IS_KM_PER_HOUR, to_data.isHoverLegs());
		
		String from_value_str = SpecValues.getSpecUnit(from_value, "装甲", BBViewSetting.IS_KM_PER_HOUR);
		String to_value_str = SpecValues.getSpecUnit(to_value, "装甲", BBViewSetting.IS_KM_PER_HOUR);

		// スペックと内部値を結合する
		from_value_str = from_point + " (" + from_value_str + ")";
		to_value_str = to_point + " (" + to_value_str + ")"; 

		SpecRow row = new SpecRow("装甲平均値", from_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
		row.setValues(from_value_str, to_value_str);
		return row;
	}
	
	/**
	 * アセン比較用の重量の配列を生成する
	 * @param from_data 比較元のアセンデータ
	 * @param to_data 比較先のアセンデータ
	 * @return 配列
	 */
	public static SpecRow getCmpWeightSpecArray(CustomData from_data, CustomData to_data, String blust_type) {
		double from_value, to_value;
		if(blust_type.equals("")) {
			from_value = from_data.getPartsWeight();
			to_value = to_data.getPartsWeight();
		}
		else {
			from_value = from_data.getWeight(blust_type);
			to_value = to_data.getWeight(blust_type);
		}

		return new SpecRow("重量", from_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	/**
	 * アセン比較用の積載猶予の配列を生成する
	 * @param from_data 比較元のアセンデータ
	 * @param to_data 比較先のアセンデータ
	 * @return 配列
	 */
	public static SpecRow getCmpSpaceWeightSpecArray(CustomData from_data, CustomData to_data, String blust_type) {
		double from_value, to_value;
		if(blust_type.equals("")) {
			from_value = from_data.getSpacePartsWeight();
			to_value = to_data.getSpacePartsWeight();
		}
		else {
			from_value = from_data.getSpaceWeight(blust_type);
			to_value = to_data.getSpaceWeight(blust_type);
		}

		return new SpecRow("積載猶予", from_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	/**
	 * アセン比較用の初速の配列を生成する
	 * @param from_data 比較元のアセンデータ
	 * @param to_data 比較先のアセンデータ
	 * @return 配列
	 */
	public static SpecRow getCmpStartDushSpecArray(CustomData from_data, CustomData to_data, String blust_type) {
		double from_value = from_data.getStartDush(blust_type);
		double to_value = to_data.getStartDush(blust_type);

		String from_point = SpecValues.getPoint("ダッシュ", from_value, BBViewSetting.IS_KM_PER_HOUR, from_data.isHoverLegs());
		String to_point = SpecValues.getPoint("ダッシュ", to_value, BBViewSetting.IS_KM_PER_HOUR, to_data.isHoverLegs());
		
		String from_value_str = SpecValues.getSpecUnit(from_value, "ダッシュ", BBViewSetting.IS_KM_PER_HOUR);
		String to_value_str = SpecValues.getSpecUnit(to_value, "ダッシュ", BBViewSetting.IS_KM_PER_HOUR);

		// スペックと内部値を結合する
		from_value_str = from_point + " (" + from_value_str + ")";
		to_value_str = to_point + " (" + to_value_str + ")"; 

		SpecRow row = new SpecRow("ダッシュ(初速)", from_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
		row.setValues(from_value_str, to_value_str);
		return row;
	}

	/**
	 * アセン比較用の巡航速度の配列を生成する
	 * @param from_data 比較元のアセンデータ
	 * @param to_data 比較先のアセンデータ
	 * @return 配列
	 */
	public static SpecRow getCmpNormalDushSpecArray(CustomData from_data, CustomData to_data, String blust_type) {
		double from_value = from_data.getNormalDush(blust_type);
		double to_value = to_data.getNormalDush(blust_type);

		return new SpecRow("巡航", from_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	/**
	 * アセン比較用の歩行速度の配列を生成する
	 * @param from_data 比較元のアセンデータ
	 * @param to_data 比較先のアセンデータ
	 * @return 配列
	 */
	public static SpecRow getCmpWalkSpecArray(CustomData from_data, CustomData to_data, String blust_type) {
		double from_value = from_data.getWalk(blust_type);
		double to_value = to_data.getWalk(blust_type);

		String from_point = SpecValues.getPoint("歩行", from_value, BBViewSetting.IS_KM_PER_HOUR, from_data.isHoverLegs());
		String to_point = SpecValues.getPoint("歩行", to_value, BBViewSetting.IS_KM_PER_HOUR, to_data.isHoverLegs());
		
		String from_value_str = SpecValues.getSpecUnit(from_value, "歩行", BBViewSetting.IS_KM_PER_HOUR);
		String to_value_str = SpecValues.getSpecUnit(to_value, "歩行", BBViewSetting.IS_KM_PER_HOUR);

		// スペックと内部値を結合する
		from_value_str = from_point + " (" + from_value_str + ")";
		to_value_str = to_point + " (" + to_value_str + ")"; 

		SpecRow row = new SpecRow("歩行", from_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
		row.setValues(from_value_str, to_value_str);
		return row;
	}

	/**
	 * アセン比較用の低下率の配列を生成する
	 * @param from_data 比較元のアセンデータ
	 * @param to_data 比較先のアセンデータ
	 * @return 配列
	 */
	public static SpecRow getCmpSpeedDonRateSpecArray(CustomData from_data, CustomData to_data, String blust_type) {
		double from_value, to_value;
		
		if(blust_type.equals("")) {
			from_value = from_data.getSpeedDownRate();
			to_value = to_data.getSpeedDownRate();
		}
		else {
			from_value = from_data.getSpeedDownRate(blust_type);
			to_value = to_data.getSpeedDownRate(blust_type);
		}

		return new SpecRow("低下率", from_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
	}
	
	//----------------------------------------------------------
	// 武器共通のスペック
	//----------------------------------------------------------
	
	/**
	 * 単発威力の配列を生成する。備考欄に転倒ダメージ値を表示する。
	 * @param data アセンデータ
	 * @param weapon 武器データ
	 * @return 配列
	 */
	public static SpecRow getOneShotPowerArray(CustomData data, BBData weapon) {
		String key = "単発火力";
		double normal_value = weapon.getOneShotPower();
		double real_value = data.getOneShotPower(weapon);

		if(data.existChipGroup("アンチスタビリティ")) {
			double real_value_stn = data.getShotAntiStability(weapon, false);
			String real_value_stn_str = "転倒Dmg値：" + SpecValues.getSpecUnit(real_value_stn, key, BBViewSetting.IS_KM_PER_HOUR);
			
			SpecRow row = new SpecRow(key, normal_value, real_value, real_value_stn, BBViewSetting.IS_KM_PER_HOUR);
			row.setValues(real_value_stn_str, SpecRow.KEY_MEMO);
			return row;
		}

		return new SpecRow(key, normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	/**
	 * 単発威力の配列を生成する。備考欄に転倒ダメージ値を表示する。
	 * @param from_data
	 * @param to_data
	 * @param from_weapon
	 * @param to_weapon
	 * @return 配列
	 */
	public static SpecRow getCmpOneShotPowerArray(CustomData from_data, CustomData to_data, BBData from_weapon, BBData to_weapon) {
		String key = "単発火力";
		double fm_value = from_data.getOneShotPower(from_weapon);
		double to_value = to_data.getOneShotPower(to_weapon);

		return new SpecRow(key, fm_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	/**
	 * 単発威力(CS時)の配列を生成する。備考欄に転倒ダメージ値を表示する。
	 * @param data アセンデータ
	 * @param weapon 武器データ
	 * @return 配列
	 */
	public static SpecRow getCsShotPowerArray(CustomData data, BBData weapon) {
		String key = "単発火力(CS時)";
		double normal_value = weapon.getCsShotPower();
		double real_value = data.getCsShotPower(weapon);
		
		if(data.existChipGroup("アンチスタビリティ")) {
			double real_value_stn = data.getShotAntiStability(weapon, true);
			String real_value_stn_str = "転倒Dmg値：" + SpecValues.getSpecUnit(real_value_stn, key, BBViewSetting.IS_KM_PER_HOUR);
			
			SpecRow row = new SpecRow(key, normal_value, real_value, real_value_stn, BBViewSetting.IS_KM_PER_HOUR);
			row.setValues(real_value_stn_str, SpecRow.KEY_MEMO);
			
			return row;
		}

		return new SpecRow(key, normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	/**
	 * 単発威力(CS時)の配列を生成する。備考欄に転倒ダメージ値を表示する。
	 * @param from_data
	 * @param to_data
	 * @param from_weapon
	 * @param to_weapon
	 * @return 配列
	 */
	public static SpecRow getCmpCsShotPowerArray(CustomData from_data, CustomData to_data, BBData from_weapon, BBData to_weapon) {
		String key = "単発火力(CS時)";
		double fm_value = from_data.getCsShotPower(from_weapon);
		double to_value = to_data.getCsShotPower(to_weapon);
		
		return new SpecRow(key, fm_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	public static SpecRow getChargeTimeArray(CustomData data, BBData weapon) {
		double normal_value = weapon.getChargeTime();
		double real_value = data.getChargeTime(weapon);
		
		return new SpecRow("充填時間", normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	public static SpecRow getCmpChargeTimeArray(CustomData from_data, CustomData to_data, BBData from_weapon, BBData to_weapon) {
		double fm_value = from_data.getChargeTime(from_weapon);
		double to_value = to_data.getChargeTime(to_weapon);
		
		return new SpecRow("充填時間", fm_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
	}
	
	//----------------------------------------------------------
	// 主武器のスペック
	//----------------------------------------------------------
	
	public static SpecRow getMagazinePowerArray(CustomData data, BBData weapon) {
		double normal_value = weapon.getMagazinePower();
		double real_value = data.getMagazinePower(weapon);
		
		return new SpecRow("マガジン火力", normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	public static SpecRow getCmpMagazinePowerArray(CustomData from_data, CustomData to_data, BBData from_weapon, BBData to_weapon) {
		double fm_value = from_data.getMagazinePower(from_weapon);
		double to_value = to_data.getMagazinePower(to_weapon);
		
		return new SpecRow("マガジン火力", fm_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	public static SpecRow getOverheatPowerArray(CustomData data, BBData weapon) {
		double normal_value = weapon.getOverHeatPower();
		double real_value = data.getOverHeatPower(weapon);
		
		return new SpecRow("OH火力", normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	public static SpecRow getCmpOverheatPowerArray(CustomData from_data, CustomData to_data, BBData from_weapon, BBData to_weapon) {
		double fm_value = from_data.getOverHeatPower(from_weapon);
		double to_value = to_data.getOverHeatPower(to_weapon);
		
		SpecRow row = new SpecRow("OH火力", fm_value, to_value, BBViewSetting.IS_KM_PER_HOUR);

		if(fm_value <= 0) {
			row.setValues("-", SpecRow.KEY_NORMAL);
			row.clearColors();
		}

		if(to_value <= 0) {
			row.setValues("-", SpecRow.KEY_REAL);
			row.clearColors();
		}
		
		return row;
	}
	
	public static SpecRow getSecPowerArray(CustomData data, BBData weapon) {
		double normal_value = weapon.get1SecPower();
		double real_value = data.get1SecPower(weapon);

		return new SpecRow("瞬間火力", normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	public static SpecRow getCmpSecPowerArray(CustomData from_data, CustomData to_data, BBData from_weapon, BBData to_weapon) {
		double fm_value = from_data.get1SecPower(from_weapon);
		double to_value = to_data.get1SecPower(to_weapon);

		return new SpecRow("瞬間火力", fm_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	/**
	 * 戦術火力の配列を生成する。
	 * @param data アセンデータ
	 * @param weapon 武器データ
	 * @return 配列
	 */
	public static SpecRow getBattlePowerArray(CustomData data, BBData weapon) {
		String key = "戦術火力";
		double normal_value = weapon.getBattlePower();
		double real_value = data.getBattlePower(weapon);

		if(data.existChipGroup("クイックリロード")) {
			double real_value_quick = data.getBattlePower(weapon, true);
			String real_value_quick_str = "手動リロ時：" + SpecValues.getSpecUnit(real_value_quick, key, BBViewSetting.IS_KM_PER_HOUR);
			
			SpecRow row = new SpecRow(key, normal_value, real_value, real_value_quick, BBViewSetting.IS_KM_PER_HOUR);
			row.setValues(real_value_quick_str, SpecRow.KEY_MEMO);
			
			return row;
		}

		return new SpecRow(key, normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	/**
	 * 戦術火力の配列を生成する。
	 * @param from_data
	 * @param to_data
	 * @param from_weapon
	 * @param to_weapon
	 * @return 配列
	 */
	public static SpecRow getCmpBattlePowerArray(CustomData from_data, CustomData to_data, BBData from_weapon, BBData to_weapon) {
		String key = "戦術火力";

		double fm_value, to_value;
		if(from_weapon.existKey("OH耐性")) {
			fm_value = from_data.getBattlePowerOverHeat(from_weapon, false);
		}
		else {
			fm_value = from_data.getBattlePower(from_weapon);
		}
		
		if(to_weapon.existKey("OH耐性")) {
			to_value = to_data.getBattlePowerOverHeat(to_weapon, false);
		}
		else {
			to_value = to_data.getBattlePower(to_weapon);
		}

		return new SpecRow(key, fm_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	/**
	 * OH武器の戦術火力の配列を生成する。
	 * @param data アセンデータ
	 * @param weapon 武器データ
	 * @return 配列
	 */
	public static SpecRow getBattlePowerOverheatArray(CustomData data, BBData weapon) {
		String key = "戦術火力";
		double normal_value_notoh = weapon.getBattlePowerOverHeat(false);
		double real_value_notoh = data.getBattlePowerOverHeat(weapon, false);
		
		double normal_value_oh = weapon.getBattlePowerOverHeat(true);
		double real_value_oh = data.getBattlePowerOverHeat(weapon, true);
		
		String normal_str = SpecValues.getSpecUnit(normal_value_oh, key, BBViewSetting.IS_KM_PER_HOUR) + " ("
				          + SpecValues.getSpecUnit(normal_value_notoh, key, BBViewSetting.IS_KM_PER_HOUR) + ")";

		String real_str = SpecValues.getSpecUnit(real_value_oh, key, BBViewSetting.IS_KM_PER_HOUR) + " ("
				        + SpecValues.getSpecUnit(real_value_notoh, key, BBViewSetting.IS_KM_PER_HOUR) + ")";
		
		SpecRow row = new SpecRow(key, normal_value_notoh, real_value_notoh, BBViewSetting.IS_KM_PER_HOUR);
		row.setValues(normal_str, real_str);
		return row;
	}
	
	/**
	 * リロード時間の配列を生成する。
	 * @param data アセンデータ
	 * @param weapon 武器データ
	 * @return 配列
	 */
	public static SpecRow getReloadTimeArray(CustomData data, BBData weapon) {
		String key = "リロード時間";
		double normal_value = weapon.getReloadTime();
		double real_value = data.getReloadTime(weapon);

		if(data.existChipGroup("クイックリロード")) {
			double real_value_quick = data.getReloadTime(weapon, true);
			String real_value_quick_str = "手動リロ時：" + SpecValues.getSpecUnit(real_value_quick, key, BBViewSetting.IS_KM_PER_HOUR);
			
			SpecRow row = new SpecRow(key, normal_value, real_value, real_value_quick, BBViewSetting.IS_KM_PER_HOUR);
			row.setValues(real_value_quick_str, SpecRow.KEY_MEMO);
			
			return row;
		}

		return new SpecRow(key, normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	/**
	 * リロード時間の配列を生成する。
	 * @param from_data
	 * @param to_data
	 * @param from_weapon
	 * @param to_weapon
	 * @return 配列
	 */
	public static SpecRow getCmpReloadTimeArray(CustomData from_data, CustomData to_data, BBData from_weapon, BBData to_weapon) {
		String key = "リロード時間";
		double fm_value = from_data.getReloadTime(from_weapon);
		double to_value = to_data.getReloadTime(to_weapon);

		return new SpecRow(key, fm_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
	}
	
	/**
	 * OH耐性の配列を生成する。
	 * @param data アセンデータ
	 * @param weapon 武器データ
	 * @return 配列
	 */
	public static SpecRow getOverheatTimeArray(CustomData data, BBData weapon) {
		double normal_value = weapon.getOverheatTime();
		double real_value = normal_value;

		return new SpecRow("OH耐性", normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	/**
	 * OH耐性の配列を生成する。
	 * @param from_data
	 * @param to_data
	 * @param from_weapon
	 * @param to_weapon
	 * @return 配列
	 */
	public static SpecRow getCmpOverheatTimeArray(CustomData from_data, CustomData to_data, BBData from_weapon, BBData to_weapon) {
		double fm_value = from_weapon.getOverheatTime();
		double to_value = to_weapon.getOverheatTime();

		SpecRow row = new SpecRow("OH耐性", fm_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
		
		if(fm_value <= 0) {
			row.setValues("-", SpecRow.KEY_NORMAL);
			row.clearColors();
		}

		if(to_value <= 0) {
			row.setValues("-", SpecRow.KEY_REAL);
			row.clearColors();
		}
		
		return row;
	}

	/**
	 * OH復帰時間の配列を生成する。
	 * @param data アセンデータ
	 * @param weapon 武器データ
	 * @return 配列
	 */
	public static SpecRow getOverheatRepairTimeArray(CustomData data, BBData weapon) {
		String key = "OH復帰時間";
		double normal_value_notoh = weapon.getOverheatRepairTime(false);
		double real_value_notoh = data.getOverheatRepairTime(weapon, false);
		
		double normal_value_oh = weapon.getOverheatRepairTime(true);
		double real_value_oh = data.getOverheatRepairTime(weapon, true);
		
		String normal_str = SpecValues.getSpecUnit(normal_value_oh, key, BBViewSetting.IS_KM_PER_HOUR) + " ("
				          + SpecValues.getSpecUnit(normal_value_notoh, key, BBViewSetting.IS_KM_PER_HOUR) + ")";

		String real_str = SpecValues.getSpecUnit(real_value_oh, key, BBViewSetting.IS_KM_PER_HOUR) + " ("
				        + SpecValues.getSpecUnit(real_value_notoh, key, BBViewSetting.IS_KM_PER_HOUR) + ")";
		
		SpecRow row = new SpecRow(key, normal_value_notoh, real_value_notoh, BBViewSetting.IS_KM_PER_HOUR);
		row.setValues(normal_str, real_str);
		return row;
	}

	/**
	 * OH復帰時間の配列を生成する。
	 * @param from_data
	 * @param to_data
	 * @param from_weapon
	 * @param to_weapon
	 * @return 配列
	 */
	public static SpecRow getCmpOverheatRepairTimeArray(CustomData from_data, CustomData to_data, BBData from_weapon, BBData to_weapon) {
		String key = "OH復帰時間";
		double fm_value_notoh = from_data.getOverheatRepairTime(from_weapon, false);
		double fm_value_oh = from_data.getOverheatRepairTime(from_weapon, true);
		
		double to_value_notoh = to_data.getOverheatRepairTime(to_weapon, false);
		double to_value_oh = to_data.getOverheatRepairTime(to_weapon, true);
		
		SpecRow row = new SpecRow(key, fm_value_notoh, to_value_notoh, BBViewSetting.IS_KM_PER_HOUR);
		
		String fm_str, to_str;
		if(fm_value_notoh > 0) {
			fm_str = SpecValues.getSpecUnit(fm_value_oh, key, BBViewSetting.IS_KM_PER_HOUR) + " ("
				     + SpecValues.getSpecUnit(fm_value_notoh, key, BBViewSetting.IS_KM_PER_HOUR) + ")";
		}
		else {
			fm_str = "-";
			row.clearColors();
		}

		if(to_value_notoh > 0) {
			to_str = SpecValues.getSpecUnit(to_value_oh, key, BBViewSetting.IS_KM_PER_HOUR) + " ("
			         + SpecValues.getSpecUnit(to_value_notoh, key, BBViewSetting.IS_KM_PER_HOUR) + ")";
		}
		else {
			to_str = "-";
			row.clearColors();
		}
		
		row.setValues(fm_str, to_str);
		return row;
	}

	/**
	 * 総弾数の配列を生成する。
	 * 1マガジンからあふれた分は"+n"で表示する。
	 * @param data アセンデータ
	 * @param weapon 武器データ
	 * @return 配列
	 */
	public static SpecRow getMagazineCount(CustomData data, BBData weapon) {
		double normal_value = weapon.getBulletSum();
		double real_value = data.getBulletSum(weapon);

		/* 総弾数の文字列を生成する */
		double magazine_bullet = weapon.getMagazine();
		double over_bullet = real_value % magazine_bullet;
		double magazine_count = Math.floor(real_value / magazine_bullet);
		
		String bullet_str = "";
		if(magazine_bullet == 1) {
			bullet_str = String.format("1x%.0f", real_value);
		}
		else {
			bullet_str = String.format("%.0fx%.0f +%.0f", magazine_bullet, magazine_count, over_bullet);
		}
		
		SpecRow row = new SpecRow("総弾数", normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
		row.setValues(weapon.get("総弾数"), bullet_str);
		
		return row;
	}

	/**
	 * 総弾数の配列を生成する。
	 * 1マガジンからあふれた分は"+n"で表示する。
	 * @param from_data
	 * @param to_data
	 * @param from_weapon
	 * @param to_weapon
	 * @return 配列
	 */
	public static SpecRow getCmpMagazineCount(CustomData from_data, CustomData to_data, BBData from_weapon, BBData to_weapon) {
		double fm_value = from_data.getBulletSum(from_weapon);
		double magazine_bullet = from_weapon.getMagazine();
		double over_bullet = fm_value % magazine_bullet;
		double magazine_count = Math.floor(fm_value / magazine_bullet);
		
		String fm_str = "";
		if(magazine_bullet == 1) {
			fm_str = String.format("1x%.0f", fm_value);
		}
		else {
			fm_str = String.format("%.0fx%.0f +%.0f", magazine_bullet, magazine_count, over_bullet);
		}
		
		double to_value = to_data.getBulletSum(to_weapon);
		magazine_bullet = to_weapon.getMagazine();
		over_bullet = to_value % magazine_bullet;
		magazine_count = Math.floor(to_value / magazine_bullet);

		String to_str = "";
		if(magazine_bullet == 1) {
			to_str = String.format("1x%.0f", to_value);
		}
		else {
			to_str = String.format("%.0fx%.0f +%.0f", magazine_bullet, magazine_count, over_bullet);
		}
		
		SpecRow row = new SpecRow("総弾数", fm_value, to_value, BBViewSetting.IS_KM_PER_HOUR);
		row.setValues(fm_str, to_str);
		
		return row;
	}

	//----------------------------------------------------------
	// 副武器のスペック
	//----------------------------------------------------------

	public static SpecRow getExplosionRangeArray(CustomData data, BBData weapon) {
		double normal_value = weapon.getExplosionRange();
		double real_value = data.getExplosionRange(weapon);

		return new SpecRow("爆発半径", normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	//----------------------------------------------------------
	// 補助装備のスペック
	//----------------------------------------------------------

	public static SpecRow getNormalSlashArray(CustomData data, BBData weapon) {
		double normal_value = weapon.getSlashDamage(false);
		double real_value = data.getSlashPower(weapon, false);

		return new SpecRow("通常攻撃(総威力)", normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	public static SpecRow getDashSlashArray(CustomData data, BBData weapon) {
		double normal_value = weapon.getSlashDamage(true);
		double real_value = data.getSlashPower(weapon, true);

		return new SpecRow("特殊攻撃(総威力)", normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	public static SpecRow getSearchTimeArray(CustomData data, BBData weapon) {
		double normal_value = weapon.getSearchTime();
		double real_value = data.getSearchTime(weapon);

		return new SpecRow("索敵時間", normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	public static SpecRow getEffectTimeArray(CustomData data, BBData weapon) {
		String str_value = weapon.get("効果持続");
		double normal_value = 0;
		double real_value = 0;
		
		try {
			normal_value = Double.valueOf(str_value);
			real_value = normal_value;
			
		} catch(Exception e) {
			// Do Nothing
		}
		
		return new SpecRow("効果持続", normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	//----------------------------------------------------------
	// 特別装備のスペック
	//----------------------------------------------------------

	/**
	 * 特別装備のチャージ時間の配列を生成する。
	 * 支援兵装強化チップでSP供給が上昇するため、引数に兵装名が必要。
	 * @param data アセンデータ
	 * @param weapon 武器データ
	 * @param blust_type 兵装名
	 * @return 配列
	 */
	public static SpecRow getSpChargeTimeArray(CustomData data, BBData weapon, String blust_type) {
		double normal_value_notoh = weapon.getSpChargeTime(false);
		double real_value_notoh = data.getSpChargeTime(blust_type, weapon, false);

		double normal_value_oh = weapon.getSpChargeTime(true);
		double real_value_oh = data.getSpChargeTime(blust_type, weapon, true);

		String key = "チャージ時間";
		String normal_str = SpecValues.getSpecUnit(normal_value_oh, key, BBViewSetting.IS_KM_PER_HOUR) + " ("
				          + SpecValues.getSpecUnit(normal_value_notoh, key, BBViewSetting.IS_KM_PER_HOUR) + ")";

		String real_str = SpecValues.getSpecUnit(real_value_oh, key, BBViewSetting.IS_KM_PER_HOUR) + " ("
				        + SpecValues.getSpecUnit(real_value_notoh, key, BBViewSetting.IS_KM_PER_HOUR) + ")";
		
		SpecRow row = new SpecRow(key, normal_value_notoh, real_value_notoh, BBViewSetting.IS_KM_PER_HOUR);
		row.setValues(normal_str, real_str);
		return row;
	}

	/**
	 * AC速度の配列を生成する。
	 * AC自体は速度値を持たないので、補正前も補正後も同じ値とする。
	 * @param data アセンデータ
	 * @param weapon 武器データ
	 * @return 配列
	 */
	public static SpecRow getAcSpeedArray(CustomData data, BBData weapon) {
		double normal_value = data.getACSpeed(weapon);
		double real_value = normal_value;

		return new SpecRow("AC速度", normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	/**
	 * AC戦術速度の配列を生成する。
	 * AC自体は速度値を持たないので、補正前も補正後も同じ値とする。
	 * @param data アセンデータ
	 * @param weapon 武器データ
	 * @return 配列
	 */
	public static SpecRow getAcBattleSpeedArray(CustomData data, BBData weapon) {
		double normal_value = data.getBattleACSpeed(weapon);
		double real_value = normal_value;

		return new SpecRow("AC戦術速度", normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	/**
	 * バリア装備の秒間耐久回復量の配列を生成する。
	 * @param data アセンデータ
	 * @param weapon 武器データ
	 * @return 配列
	 */
	public static SpecRow getBattleBarrierGuardArray(CustomData data, BBData weapon) {
		double normal_value = weapon.getBattleBarrierGuard();
		double real_value = data.getBattleBarrierGuard(weapon);

		return new SpecRow("秒間耐久回復量", normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

	/**
	 * リペア装備の回復量の配列を生成する。
	 * @param data アセンデータ
	 * @param weapon 武器データ
	 * @return 配列
	 */
	public static SpecRow getMaxRepairArray(CustomData data, BBData weapon) {
		double normal_value = weapon.getMaxRepair();
		double real_value = data.getMaxRepair(weapon);

		return new SpecRow("最大回復量", normal_value, real_value, BBViewSetting.IS_KM_PER_HOUR);
	}

}
