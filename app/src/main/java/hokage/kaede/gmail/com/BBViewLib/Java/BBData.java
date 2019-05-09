package hokage.kaede.gmail.com.BBViewLib.Java;

import hokage.kaede.gmail.com.StandardLib.Java.KVCStore;
import hokage.kaede.gmail.com.StandardLib.Java.KeyValueStore;

/**
 * １つのパーツや武器などの情報を保持するクラス。
 */
public class BBData extends KVCStore {

	public static final int ID_ITEM_NOTHING = -1;
	
	public static final String STR_VALUE_NOTHING = "情報なし";
	public static final double NUM_VALUE_NOTHING = Double.MIN_NORMAL;

	// 属性を示す文字列
	private static final String SHOT_ABS_BULLET    = "実弾";
	private static final String SHOT_ABS_NEWD      = "ニュード";
	private static final String SHOT_ABS_EXPLOSION = "爆発";
	private static final String SHOT_ABS_SLASH     = "近接";
	
	// スイッチ武器のタイプBのデータ
	private BBData mTypeB_data = null;

	public int id;
	
	/**
	 * コンストラクタ
	 */
	public BBData() {
		this.id = ID_ITEM_NOTHING;
	}
	
	/**
	 * スイッチ武器のタイプBデータを返す。
	 * @return
	 */
	public BBData getTypeB() {
		return mTypeB_data;
	}
	
	/**
	 * スイッチ武器のタイプBデータを設定する。
	 * @param data
	 * @return
	 */
	public void setTypeB(BBData data) {
		mTypeB_data = data;
	}
	
	/**
	 * スイッチ武器かどうかを考慮して武器データを取得する
	 * @param is_show_typeb タイプBの場合はtrueを設定し、タイプAの場合はfalseを設定する。
	 * @return 武器名
	 */
	public String getNameWithType(boolean is_show_typeb) {
		String name = this.get("名称");
		
		if(mTypeB_data != null) {
			if(is_show_typeb) {
				name = name + " (タイプB)";
			}
			else {
				name = name + " (タイプA)";
			}
		}
		
		return name;
	}
	
	/**
	 * 値の取得
	 * @param key
	 * @return
	 */
	@Override
	public String get(String key) {
		String ret = STR_VALUE_NOTHING;
		
		if(super.existKey(key)) {
			ret = super.get(key);
		}
		else {
			double calc_value = getCalcValue(key);
			if(calc_value > NUM_VALUE_NOTHING) {
				ret = String.valueOf(calc_value);
			}
		}

		return ret;
	}
	
	// パーツスペック関連
	public static String REAL_LIFE_KEY          = "実耐久値";
	public static String DEF_RECORVER_TIME_KEY  = "DEF回復時間";
	public static String STEP_MAX_COUNT_KEY     = "最大ステップ数";
	public static String BOOST_CHARGE_TIME_KEY  = "ブースター回復時間";
	
	// 武器スペック関連
	public static String FULL_POWER_KEY         = "総火力";
	public static String MAGAZINE_POWER_KEY     = "マガジン火力";
	public static String SEC_POWER_KEY          = "瞬間火力";
	public static String BATTLE_POWER_KEY       = "戦術火力";
	public static String OH_POWER_KEY           = "OH火力";
	
	public static String BULLET_SUM_KEY         = "総弾数(合計)";
	public static String CARRY_KEY              = "積載猶予";
	
	public static String FLAIGHT_TIME_KEY       = "飛翔時間";
	public static String SEARCH_SPACE_KEY       = "索敵面積";
	public static String SEARCH_SPACE_START_KEY = "初動索敵面積";
	public static String SEARCH_SPACE_MAX_KEY   = "総索敵面積";
	public static String SEARCH_SPACE_TIME_KEY  = "戦術索敵面積";
	
	public static String MAX_REPAIR_KEY         = "最大修理量";
	
	public static String ARMOR_BREAK_KEY        = "大破判定";
	public static String ARMOR_DOWN_KEY         = "転倒判定";
	public static String ARMOR_KB_KEY           = "KB判定";
	public static String ARMOR_CS_BREAK_KEY     = "大破判定(CS)";
	public static String ARMOR_CS_DOWN_KEY      = "転倒判定(CS)";
	public static String ARMOR_CS_KB_KEY        = "KB判定(CS)";
	
	public static String SLASH_DAMAGE_NL_KEY    = "通常攻撃(総威力)";
	public static String SLASH_DAMAGE_EX_KEY    = "特殊攻撃(総威力)";
	
	public static String BARRIER_BATTLE_GRD_KEY = "秒間耐久回復量";
	
	/**
	 * 算出値データを取得する
	 * @param key 取得する算出値データのキー。(CALC_KEY)
	 * @return 算出値データ。対象のキーがない場合はDouble.MIN_NORMALを返す。
	 */
	public double getCalcValue(String key) {
		double ret = NUM_VALUE_NOTHING;
		
		if(key == null) {
			// Do Nothing
		}
		else if(key.equals(REAL_LIFE_KEY)) {
			ret = getLife();
		}
		else if(key.equals(DEF_RECORVER_TIME_KEY)) {
			ret = getDefRecoverTime();
		}
		else if(key.equals(STEP_MAX_COUNT_KEY)) {
			ret = getStepMaxCount();
		}
		else if(key.equals(BOOST_CHARGE_TIME_KEY)) {
			ret = getBoostChargeTime();
		}
		else if(key.equals(FULL_POWER_KEY)) {
			ret = getFullPower();
		}
		else if(key.equals(MAGAZINE_POWER_KEY)) {
			ret = getMagazinePower();
		}
		else if(key.equals(SEC_POWER_KEY)) {
			ret = get1SecPower();
		}
		else if(key.equals(BATTLE_POWER_KEY)) {
			ret = getBattlePower();
		}
		else if(key.equals(OH_POWER_KEY)) {
			ret = getOverHeatPower();
		}
		else if(key.equals(BULLET_SUM_KEY)) {
			ret = getBulletSum();
		}
		else if(key.equals(CARRY_KEY)) {
			ret = getCarry();
		}
		// BBPS4では不明のため、一旦無効化
		/*
		else if(key.equals(FLAIGHT_TIME_KEY)) {
			ret = getFlaightTime();
		}
		else if(key.equals(SEARCH_SPACE_KEY)) {
			ret = getSearchSpace();
		}
		else if(key.equals(SEARCH_SPACE_START_KEY)) {
			ret = getSearchSpaceStart();
		}
		else if(key.equals(SEARCH_SPACE_MAX_KEY)) {
			ret = getSearchSpaceMax();
		}
		else if(key.equals(SEARCH_SPACE_TIME_KEY)) {
			ret = getSearchSpaceTime();
		}
		else if(key.equals(MAX_REPAIR_KEY)) {
			ret = getMaxRepair();
		}
		*/
		else if(key.equals(ARMOR_BREAK_KEY)) {
			ret = getArmorBreakJdg(false);
		}
		else if(key.equals(ARMOR_DOWN_KEY)) {
			ret = getArmorDownJdg(false);
		}
		else if(key.equals(ARMOR_KB_KEY)) {
			ret = getArmorKBJdg(false);
		}
		else if(key.equals(ARMOR_CS_BREAK_KEY)) {
			ret = getArmorBreakJdg(true);
		}
		else if(key.equals(ARMOR_CS_DOWN_KEY)) {
			ret = getArmorDownJdg(true);
		}
		else if(key.equals(ARMOR_CS_KB_KEY)) {
			ret = getArmorKBJdg(true);
		}
		else if(key.equals(SLASH_DAMAGE_NL_KEY)) {
			ret = getSlashDamage(false);
		}
		else if(key.equals(SLASH_DAMAGE_EX_KEY)) {
			ret = getSlashDamage(true);
		}
		else if(key.equals(BARRIER_BATTLE_GRD_KEY)) {
			ret = getBattleBarrierGuard();
		}
		
		return ret;
	}
	
	/**
	 * 装甲値を取得する。
	 * @return 装甲値
	 */
	public double getArmor() {
		double ret = 0;

		// パーツの設定値の読み込み
		try {
			String spec = super.get("装甲");
			ret = Integer.valueOf(SpecValues.ARMOR.get(spec));

		} catch (Exception e) {
			ret = NUM_VALUE_NOTHING;
		}

		return ret;
	}

	/**
	 * 装甲値から算出する実耐久値を算出する。
	 * @return 耐久値
	 */
	public double getLife() {
		double armor = getArmor();
		if(armor == NUM_VALUE_NOTHING) {
			return NUM_VALUE_NOTHING;
		}
		
		double damege_rate = (100.0 - armor) / 100.0;
		return SpecValues.BLUST_LIFE_MAX / damege_rate;
	}

	/**
	 * DEF回復時間を取得する。
	 * @return
	 */
	public double getDefRecoverTime() {
		double ret = NUM_VALUE_NOTHING;
		
		String point = super.get("DEF回復");
		String spec = SpecValues.DEF_RECOVER.get(point);
		
		try {
			double value = Double.valueOf(spec);
			ret = 30.0 / (1 + (value / 100));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	/**
	 * ブースターの値を取得する。
	 * @return ブースターの値。
	 */
	public double getBoost() {
		double ret = 0;

		String point = super.get("ブースター");
		String value = SpecValues.BOOST.get(point);
		
		try {
			ret = Integer.valueOf(value);
			
		} catch(Exception e) {
			ret = 0;
		}
			
		return ret;
	}
	/**
	 * 最大ステップ回数を取得する。(1ステップの消費量は12)
	 * @return 最大ステップ回数
	 */
	public double getStepMaxCount() {
		return Math.ceil(getBoost() / 12.0);
	}
	
	/**
	 * ブースター回復時間を取得する。(非OH時は毎秒50回復する)
	 * @return ブースター回復時間
	 */
	public double getBoostChargeTime() {
		double charge = 50;

		return getBoost() / charge;
	}
	
	/**
	 * 積載猶予を取得する。
	 * @return 積載猶予
	 */
	public int getCarry() {
		int ret = 0;
		String weight_str = super.get("重量");
		String anti_weight_str = super.get("重量耐性");
		String name = super.get("名称");
		
		if(weight_str.equals(BBData.EMPTY_VALUE) || anti_weight_str.equals(BBData.EMPTY_VALUE)) {
			return 0;
		}
		
		try {
			int weight = Integer.valueOf(weight_str);
			int anti_weight = SpecValues.getAntiWeight(anti_weight_str, name);
			ret = anti_weight - weight;
			
		} catch(NumberFormatException e) {
			ret = 0;
			
		} catch(IndexOutOfBoundsException e) {
			ret = 0;
		}
		
		return ret;
	}
	
	/**
	 * セットボーナスの情報を取得する。
	 * @return セットボーナスの文字列
	 */
	public String getSetBonus() {
		String name = super.get("名称");
		String brand = "";
		
		// 頭部のパーツのブランド名を取得する
		int len = SpecValues.SETBONUS.size();
		for(int i=0; i<len; i++) {
			String brand_buf = SpecValues.SETBONUS.getKey(i);
			if(name.indexOf(brand_buf) > -1) {
				brand = brand_buf;
				return SpecValues.SETBONUS.get(brand);
			}
		}
		
		return "なし";
	}

	//--------------------------------------------------
	// 索敵装備関連
	//--------------------------------------------------
	
	/**
	 * 索敵装備の索敵時間を算出する。
	 * @return
	 */
	public int getSearchTime() {
		int ret = 0;
		
		try {
			ret = Integer.valueOf(super.get("索敵時間"));
			
		} catch(Exception e) {
			ret = 0;
		}
		
		return ret;
	}
	
	/**
	 * 偵察機の飛翔時間を算出する。
	 * @return
	 */
	public double getFlaightTime() {
		double ret = 0;
		
		String flaight_speed_str = super.get("飛翔速度");
		String flaight_distance_str = super.get("飛翔距離");

		if(flaight_speed_str.equals(BBData.EMPTY_VALUE) || flaight_distance_str.equals(BBData.EMPTY_VALUE)) {
			return 0;
		}
		
		try {
			double flaight_speed = Double.valueOf(flaight_speed_str);
			double flaight_distance = Double.valueOf(flaight_distance_str);
			ret = flaight_distance / flaight_speed;
			
		} catch(NumberFormatException e) {
			ret = 0;
			
		}
		
		return ret;
	}
	
	/**
	 * 索敵範囲を取得する。
	 * @return
	 */
	public double getRadius() {
		double ret = 0;
		String radius_str = super.get("索敵範囲");

		if(radius_str.equals(BBData.EMPTY_VALUE)) {
			return 0;
		}
		
		try {
			ret = Double.valueOf(radius_str);
			
		} catch(NumberFormatException e) {
			ret = 0;
		}
		
		return ret;
	}
	
	/**
	 * 索敵装備の索敵面積を算出する。
	 * @return
	 */
	public double getSearchSpace() {
		double ret = 0;
		double radius = getRadius();
		
		if(radius == 0) {
			return ret;
		}
		
		if(super.existCategory("偵察機系統")) {
			ret = getSearchSpaceCircle(radius);
		}
		else if(super.existCategory("索敵センサー系統") || super.existCategory("滞空索敵弾系統")) {
			ret = getSearchSpaceCircle(radius);
		}
		else if(super.existCategory("レーダーユニット系統")) {
			ret = getSearchSpaceFan(radius);
		}
		else if(super.existCategory("ND索敵センサー系統")) {
			ret = getSearchSpaceLine(radius);
		}
		
		return ret;
	}
	
	/**
	 * 索敵装備の初動索敵面積を算出する。
	 * @return
	 */
	public double getSearchSpaceStart() {
		double ret = 0;
		double radius = getRadius();
		
		if(radius == 0) {
			return ret;
		}
		
		if(super.existCategory("偵察機系統") || super.existCategory("索敵センサー系統") || super.existCategory("滞空索敵弾系統")) {
			ret = getSearchSpaceCircle(radius);
		}
		else if(super.existCategory("レーダーユニット系統")) {
			ret = 0;
		}
		else if(super.existCategory("ND索敵センサー系統")) {
			ret = getSearchSpaceLine(radius);
		}
		
		return ret;
	}
	
	/**
	 * 偵察機の索敵面積を算出する。
	 * @param radius
	 * @return
	 */
	/*
	private double getSearchSpacePlane(double radius) {
		double flaight_distance = getFlaightDistance();
		return (flaight_distance * radius) + (radius * radius * Math.PI);
	}
	*/
	
	/**
	 * 円形の索敵装備(索敵センサー、滞空索敵弾)の索敵面積を算出する。
	 * @param radius
	 * @return
	 */
	private double getSearchSpaceCircle(double radius) {
		return radius * radius * Math.PI;
	}
	
	/**
	 * 扇形の索敵装備(レーダーユニット)の索敵面積を算出する。
	 * @param radius
	 * @return
	 */
	private double getSearchSpaceFan(double radius) {
		double ret = 0;

		String degree_str = super.get("索敵角度");

		if(degree_str.equals(BBData.EMPTY_VALUE)) {
			return 0;
		}
		
		try {
			double degree = Double.valueOf(degree_str);
			ret = (radius * radius * Math.PI) * (degree / 360);
			
		} catch(NumberFormatException e) {
			ret = 0;
		}
		
		return ret;
	}
	
	/**
	 * 線形の索敵装備(ND索敵センサー)の索敵面積を算出する。(直径)
	 * @param radius
	 * @return
	 */
	private double getSearchSpaceLine(double radius) {
		return radius * 2;
	}

	/**
	 * 総索敵面積を取得する。
	 * @return 
	 */
	private double getSearchSpaceMax() {
		double ret = 0;
		
		// レーダーユニットは所持数データが無いため、索敵面積を返す
		if(super.existCategory("レーダーユニット系統")) {
			return getSearchSpace();
		}
		
		String count_str = super.get("所持数");

		if(count_str.equals(BBData.EMPTY_VALUE)) {
			return 0;
		}
		
		try {
			double count = Double.valueOf(count_str);
			double space = getSearchSpace();
			ret = space * count;
			
		} catch(NumberFormatException e) {
			ret = 0;
		}
		
		return ret;
	}
	
	/**
	 * 戦術索敵面積を算出する。
	 * @return
	 */
	private double getSearchSpaceTime() {
		double ret = 0;

		if(super.existCategory("偵察機系統") || super.existCategory("滞空索敵弾系統")) {

			String float_time_str = super.get("滞空時間");

			if(float_time_str.equals(BBData.EMPTY_VALUE)) {
				return 0;
			}
			
			try {
				double float_time = Double.valueOf(float_time_str);
				double space = getSearchSpaceMax();
				ret = space * float_time / 600;
				
			} catch(NumberFormatException e) {
				ret = 0;
			}
		}
		else if(super.existCategory("索敵センサー系統")) {
			ret = getSearchSpaceMax();
		}
		else if(super.existCategory("ND索敵センサー系統")) {
			ret = getSearchSpaceMax();
		}
		else if(super.existCategory("レーダーユニット系統")) {
			ret = 0;
		}
		
		return ret;
	}
	
	/**
	 * 最大修理量を取得する。
	 * @return
	 */
	public double getMaxRepair() {
		double ret = 0;
		
		if(super.existCategory("リペアユニット系統") ||
		   super.existCategory("リペアポスト系統") ||
		   super.existCategory("リペアフィールド系統") ||
		   super.existCategory("リペアインジェクター系統")) {
			try {
				String max_repair = super.get("容量");
				ret = Double.valueOf(max_repair);
				
			} catch(NumberFormatException e) {
				ret = 0;
			}
		}
		else if(super.existCategory("リペアショット系統")) {
			try {
				String repair_power = super.get("修理量");
				String repair_count = super.get("最大発射回数");
				ret = getOneShotPowerMain(repair_power) * Double.valueOf(repair_count);
				
			} catch(NumberFormatException e) {
				ret = 0;
			}
		}
		else if(super.existCategory("リペアセントリー系統")) {
			try {
				String repair_power = super.get("修理量");
				String repair_count = super.get("最大発射回数");
				ret = Double.valueOf(repair_power) * Double.valueOf(repair_count);
				
			} catch(NumberFormatException e) {
				ret = 0;
			}
		}
		
		return ret;
	}
	
	//--------------------------------------------------
	// 属性武器関連
	//--------------------------------------------------
	
	public int getBulletAbsPer() {
		return readAbsolutePer(SHOT_ABS_BULLET);
	}
	
	public int getNewdAbsPer() {
		return readAbsolutePer(SHOT_ABS_NEWD);
	}

	public int getExplosionAbsPer() {
		return readAbsolutePer(SHOT_ABS_EXPLOSION);
	}

	public int getSlashAbsPer() {
		return readAbsolutePer(SHOT_ABS_SLASH);
	}

	/**
	 * 指定の属性値の数値を読み込む。
	 * @param abs_type 属性値の種類
	 * @return 属性値 [%]
	 */
	private int readAbsolutePer(String abs_type) {
		int ret = 0;
		
		String abs_str = this.get("属性");
		int start_idx = abs_str.indexOf(abs_type);
		
		if(start_idx >= 0) {
			try {
				int per_idx = abs_str.indexOf("%", start_idx + 1);
				if(per_idx >= 0) {
					String per_value = abs_str.substring(start_idx + abs_type.length(), per_idx);
					ret = Integer.valueOf(per_value);
				}
				else {
					ret = 100;
				}
				
			} catch(NumberFormatException e) {
				e.printStackTrace();
				ret = 0;
				
			} catch(IndexOutOfBoundsException e) {
				e.printStackTrace();
				ret = 0;
			}
		}
		
		return ret;
	}
	
	/**
	 * 射撃武器かどうか。CSが存在する武器を射撃武器とする。
	 * 以下の武器を対象とする。
	 * ・実弾属性を含む武器。
	 * ・パイロダート系統。(CSが存在する)
	 * ・ニュード属性100%の武器。ただし、以下は除く。
	 *     ・二連光波刃系統
	 *     ・ランサー系統
	 *     ・NeLIS系統
	 *     ・バインドダート系統
	 *     ・バインドマイン系統
	 *     ・スタナー系統
	 *     ・ワイヤーユニット系統
	 * @return 射撃武器の場合はtrueを返し、そうでない場合はfalseを返す。
	 */
	public boolean isShotWeapon() {
		String abs_str = super.get("属性");
		String name = super.get("名称");

		if(abs_str.contains(SHOT_ABS_BULLET)) {
			return true;
		}
		else if(existCategory("パイロダート系統")) {
			return true;
		}
		else if(abs_str.contains(SHOT_ABS_NEWD) && readAbsolutePer(SHOT_ABS_NEWD) == 100) {

			if(existCategory("二連光波刃系統")) {
				return false;
			}
			else if(existCategory("ランサー系統")) {
				return false;
			}
			else if(existCategory("NeLIS系統")) {
				return false;
			}
			else if(existCategory("バインドダート系統")) {
				return false;
			}
			else if(existCategory("バインドマイン系統")) {
				return false;
			}
			else if(existCategory("スタナー系統")) {
				return false;
			}
			else if(existCategory("ワイヤーユニット系統")) {
				return false;
			}
			else {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 近接武器かどうか。
	 * @return 近接武器の場合はtrueを返し、そうでない場合はfalseを返す。
	 */
	public boolean isSlashWeapon() {
		String abs_str = super.get("属性");
		
		if(abs_str.contains(SHOT_ABS_SLASH)) {
			return true;
		}
		else if(existCategory("二連光波刃系統")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 爆発武器かどうか。
	 * @return 爆発武器の場合はtrueを返し、そうでない場合はfalseを返す。
	 */
	public boolean isExplosionWeapon() {
		String abs_str = super.get("属性");
		
		if(abs_str.contains(SHOT_ABS_EXPLOSION)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 拡散武器かどうか。
	 * 文字に"x"が含まれる場合は拡散武器とする。(ショットガン系統など)
	 * @return 拡散武器の場合はtrueを返し、そうでない場合はfalseを返す。
	 */
	public boolean isSpreadWeapon() {
		String power_str = super.get("威力");
		
		if(power_str.contains("x")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * チャージ武器かどうか。
	 * @return チャージ武器の場合はtrueを返し、そうでない場合はfalseを返す。
	 */
	public boolean isChargeWeapon() {
		String abs_str = super.get("属性");
		
		if(abs_str.contains("(チャージ)")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * チャージ武器の充填時間を取得する。
	 * @return 充填時間
	 */
	public double getChargeTime() {
		double ret = 0;
		
		try {
			ret = Double.valueOf(super.get("充填時間"));
		}
		catch(Exception e) {
			ret = 0;
		}
		
		return ret;
	}
	
	/**
	 * チャージレベルに応じた威力の文字列を取得する。
	 * @param power_str 威力
	 * @param charge_level チャージレベル
	 * @return チャージレベルに応じた威力の文字列
	 */
	private String getChargeString(String power_str, int charge_level) {
		String ret = "";
		String[] buf = power_str.split("/");
		
		if(charge_level >= 0 && charge_level < buf.length) {
			ret = buf[charge_level];
		}
		else {
			ret = buf[0];
		}
		
		return ret;
	}

	/**
	 * チャージの最大レベルを取得する。
	 * @return
	 */
	public int getChargeMaxCount() {
		if(isChargeWeapon()) {
			return getChargeMaxCount(super.get("威力"));
		}
		
		return -1;
	}
	
	/**
	 * チャージの最大レベルを取得する。
	 * @param power_str 威力の文字列
	 * @return チャージの最大レベル
	 */
	private int getChargeMaxCount(String power_str) {
		return power_str.split("/").length;
	}
	
	/**
	 * 1ショットの威力を返す。
	 * ショットガンの場合、1ショットで発射される弾と威力の積を返す。
	 * @param charge_level チャージのレベル
	 * @return 1ショットの威力を返す。
	 */
	public int getOneShotPower(int charge_level) {
		String power_str = super.get("威力");
		
		// チャージ武器の場合、チャージレベルに応じた武器の威力を取得する。
		if(isChargeWeapon()) {
			power_str = getChargeString(power_str, charge_level);
		}

		return getOneShotPowerMain(power_str);
	}
	
	/**
	 * 1ショットの威力を返す。
	 * ショットガンの場合、1ショットで発射される弾と威力の積を返す。
	 * チャージ武器の場合、最大威力で発射した場合の威力を返す。
	 * @return 1ショットの威力を返す。
	 */
	public int getOneShotPower() {
		String power_str = super.get("威力");

		// チャージ武器の場合、最大レベル時の武器の威力を取得する。
		if(isChargeWeapon()) {
			int charge_level = getChargeMaxCount(power_str);
			power_str = getChargeString(power_str, charge_level - 1);
		}
		
		return getOneShotPowerMain(power_str);
	}

	/**
	 * CS時の威力を取得する。
	 * @return CS時の威力
	 */
	public double getCsShotPower() {
		return getOneShotPower() * SpecValues.CS_SHOT_RATE;
	}

	/**
	 * CS時の威力を取得する。
	 * @param charge_level チャージレベル
	 * @return CS時の威力
	 */
	public double getCsShotPower(int charge_level) {
		return getOneShotPower(charge_level) * SpecValues.CS_SHOT_RATE;
	}

	/**
	 * 転倒関連の威力値を取得する。
	 * @param charge_level チャージレベル
	 * @return 転倒関連の威力。
	 */
	public double getShotAntiStability(int charge_level) {
		double ret = getOneShotPower(charge_level);

		// ハウルHSGのダウン値は4倍
		if(existCategory("ハウルHSG系統")) {
			ret = ret * 4.0;
		}
		
		return ret;
	}
	
	/**
	 * 転倒関連の威力値を取得する。
	 * @param is_critical クリティカルかどうか。
	 * @return 転倒関連の威力。
	 */
	private double getShotAntiStability(boolean is_critical) {
		double ret;
		
		if(is_critical) {
			ret = getCsShotPower();
		}
		else {
			ret = getOneShotPower();
		}

		// ハウルHSGのダウン値は4倍
		if(existCategory("ハウルHSG系統")) {
			ret = ret * 4.0;
		}
		
		return ret;
	}
	
	/**
	 * 1ショットの威力を返す。チャージ関連の算出を終わらせた後の文字列に対して処理を行う。
	 * @param power_str 威力の文字列
	 * @return 1ショットの威力を返す。
	 */
	private int getOneShotPowerMain(String power_str) {
		int ret = 0;

		// 威力の取得に失敗した場合、0を返す。
		if(power_str.equals(KeyValueStore.EMPTY_VALUE)) {
			return 0;
		}
		
		// イコールがある場合はその後ろの数値を結果として返す。
		String[] power_strs = power_str.split("=");
		
		if(power_strs.length == 2) {
			try {
				ret = Integer.valueOf(power_strs[1]);
				
			} catch(NumberFormatException e) {
				ret = 0;
			}
		}
		else {
			// イコールがない場合、発射する弾数と1発の威力の積を結果として返す。
			power_strs = power_str.split("x");
			
			try {
				ret = Integer.valueOf(power_strs[0]);
				
				for(int i=1; i<power_strs.length; i++) {
					int power_num = Integer.valueOf(power_strs[i]);
					ret = ret * power_num;
				}
				
			} catch(NumberFormatException e) {
				ret = 0;
			}
		}

		return ret;
	}
	
	/**
	 * 総火力を返す。
	 * @return 総火力。威力、総弾数が設定されていない場合は0を返す。
	 */
	public int getFullPower() {
		return getOneShotPower() * getBulletSum();
	}
	
	/**
	 * 1マガジンの弾数を返す。
	 * @return 1マガジンの弾数
	 */
	public int getMagazine() {
		int ret = 0;
		int bullet_index = super.indexOf("総弾数");
		
		if(bullet_index >= 0) {
			String[] bullet_str = super.get(bullet_index).split("x");

			try {
				ret = Integer.valueOf(bullet_str[0]);

			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		return ret;
	}
	
	/**
	 * マガジン火力を返す。
	 * @return マガジン火力を返す。威力、総弾数が設定されていない場合は0を返す。
	 */
	public int getMagazinePower() {
		int power = getOneShotPower(0);
		
		if(existCategory("フレアグレネード系統")) {
			
			try {
				double time = Double.valueOf(get("効果持続"));
				power = (int)(power * time);
				
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		return power * getMagazine();
	}
	
	/**
	 * 連射速度を返す。
	 * @return 連射速度。
	 */
	public int getShotSpeed() {
		int ret = -1;
		int speed_index = super.indexOf("連射速度");
		
		if(speed_index >= 0) {
			String speed_str = super.get(speed_index);
			
			try {
				ret = Integer.valueOf(speed_str);
				
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		return ret;
	}

	/**
	 * 秒間火力を取得する。
	 * @return 秒間火力
	 */
	public double get1SecPower() {
		double ret = 0;

		if(super.get("名称").equals("ライトニングスマック")) {
			ret = get1SecPowerLightning();
		}
		else {
			ret = get1SecPowerDefault();
		}
		
		return ret;
	}
	
	/**
	 * 瞬間火力を返す。
	 * 連射速度が設定されていない場合、単発の威力を返す。(チャージ武器の場合はフルチャージ時の単発火力)
	 * 1秒間に1マガジンを撃ち切る場合、マガジン火力を返す。
	 * 
	 * @return 秒間火力
	 */
	private double get1SecPowerDefault() {
		double ret = 0;
		double shot_speed = getShotSpeed();

		if(shot_speed > 0) {
			double one_power = getOneShotPower(0);
			double magazine_power = getMagazinePower();
			
			ret = one_power * shot_speed / 60;
			
			if(ret > magazine_power) {
				ret = magazine_power;
			}
		}
		else {
			ret = getOneShotPower();
		}

		return ret;
	}

	/**
	 * ライトニングスマックの秒間火力を取得する。
	 * ポンプアクション込みの計算を行う。
	 * 連射速度から1射に要する時間を求め、それにポンプアクションの時間(0.8s)を加算し、
	 * 1秒から算出結果を除算することで実連射速度を求める。
	 *
	 * @return 秒間火力
	 */
	private double get1SecPowerLightning() {
		double shot_speed = getShotSpeed();
		double one_power = getOneShotPower(0);
		
		return (3 * one_power) * (1.0 / ((3 * (60 / shot_speed)) + 0.8));
	}

	/**
	 * リロード時間を取得する。
	 * @return リロード時間。単位は秒。
	 */
	public double getReloadTime() {
		double ret = 0;

		try {
			String reload_time_str = super.get("リロード時間");
			ret = Double.valueOf(reload_time_str);
			
		} catch (Exception e) {
			ret = SpecValues.ERROR_VALUE;
		}
		
		return ret;
	}
	
	/**
	 * 戦術火力を取得する。
	 * @return 戦術火力
	 */
	public double getBattlePower() {
		double ret = 0;

		if(BBViewSetting.IS_BATTLE_POWER_OH && super.existKey("OH復帰時間")) {
			ret = getBattlePowerOverHeat(false);  // OH前の戦術火力を取得する
		}
		else if(super.get("名称").equals("ライトニングスマック")) {
			ret = getBattlePowerLightning();
		}
		else {
			ret = getBattlePowerDefault();
		}
		
		return ret;
	}
	
	/**
	 * リロードに依存した基本的な戦術火力を取得する。
	 * 
	 * 戦術火力＝マガジン火力÷（撃ち切り時間＋リロード時間）
	 * なお、連射速度の情報がない場合は単発火力とする。
	 * @return 戦術火力
	 */
	public double getBattlePowerDefault() {
		double ret = 0;
		double shot_speed = getShotSpeed();
		
		if(shot_speed > 0) {
			double magazine_power = getMagazinePower();
			double reload_time = getReloadTime();
			int magazine = getMagazine();
			
			double all_shot_time = magazine / shot_speed * 60;
			ret = magazine_power / (all_shot_time + reload_time);
		}
		else {
			ret = getOneShotPower();
		}
		
		return ret;
	}

	/**
	 * ライトニングスマックの戦術火力を取得する。
	 * @return 戦術火力
	 */
	public double getBattlePowerLightning() {
		double ret = 0;
		double shot_speed = getShotSpeed();
		double magazine_power = getMagazinePower();
		double reload_time = getReloadTime();
		int magazine = getMagazine();
		
		// 撃ち切り時間＝弾自体を撃ち切る時間＋ポンプアクション時間×回数
		double all_shot_time = (magazine / shot_speed * 60) + (0.8 * (magazine / 3));
		
		ret = magazine_power / (all_shot_time + reload_time);
		
		return ret;
	}
	
	/**
	 * OH火力を取得する。
	 * @return OH火力
	 */
	public double getOverHeatPower() {
		double shot_speed = getShotSpeed();
		double oneshot_power = getOneShotPower();
		double oh_guard_time = getOverheatTime();
		
		return oneshot_power * (shot_speed / 60) * oh_guard_time;
	}

	/**
	 * OH武器の戦術火力を取得する。(OH中)
	 * @return 戦術火力
	 */
	public double getBattlePowerOverHeat() {
		return getBattlePowerOverHeat(true);
	}
	
	/**
	 * OH武器の戦術火力を取得する。
	 * 
	 * 戦術火力＝OH火力÷（OH耐性時間＋OH復帰時間）
	 * @param is_overheat OH中かどうか。
	 * @return 戦術火力
	 */
	public double getBattlePowerOverHeat(boolean is_overheat) {
		double ret = 0;
		double shot_speed = getShotSpeed();
		double oneshot_power = getOneShotPower();
		
		if(shot_speed > 0) {
			double oh_guard_time = getOverheatTime();
			double oh_repair_time = getOverheatRepairTime(is_overheat);
			double oh_power = getOverHeatPower();
	
			ret = oh_power / (oh_guard_time + oh_repair_time);
		}
		else {
			ret = oneshot_power;
		}
		
		return ret;
	}
	
	/**
	 * OH耐性を返す。
	 * @return
	 */
	public double getOverheatTime() {
		double ret = 0;
		
		try {
			String oh_str = super.get("OH耐性");
			ret = Double.valueOf(oh_str);
			
		} catch(Exception e) {
			ret = 0;
		}
		
		return ret;
	}
	
	/**
	 * OH復帰時間を返す。
	 * @return OH復帰時間
	 */
	public double getOverheatRepairTime() {
		return getOverheatRepairTime(true);
	}
	
	/**
	 * OH復帰時間を返す。
	 * OH状態がtrueの場合は等倍のOH復帰時間を返し、falseの場合は0.8倍のOH復帰時間を返す。
	 * @param is_overheat オーバーヒート状態
	 * @return OH復帰時間
	 */
	public double getOverheatRepairTime(boolean is_overheat) {
		double ret = 0;
		
		try {
			String oh_repair_str = super.get("OH復帰時間");
			ret = Double.valueOf(oh_repair_str);
			
			if(!is_overheat) {
				ret = ret * 0.8;
			}
			
		} catch(Exception e) {
			ret = 0;
		}
		
		return ret;
	}
	
	/**
	 * 総弾数(合計)を取得する。
	 * @return 総弾数。設定されていない場合、0を返す。
	 */
	public int getBulletSum() {
		int ret = 0;
		int index = super.indexOf("総弾数");
		
		if(index >= 0) {
			String[] num = super.get(index).split("x");
			
			try {
				int bullet = Integer.valueOf(num[0]);
				int magazine = Integer.valueOf(num[1]);
				ret = bullet * magazine;

			} catch(NumberFormatException e) {
				e.printStackTrace();
				
			} catch(ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		
		return ret;
	}
	

	/**
	 * 爆発範囲を取得する。
	 * @return 爆発範囲。設定されていない場合、0を返す。
	 */
	public int getExplosionRange() {
		int ret = 0;
		
		try {
			ret = Integer.valueOf(super.get("爆発半径"));
		}
		catch(Exception e) {
			ret = 0;
		}
		
		return ret;
	}
	
	/**
	 * 近接武器のダメージを取得する。
	 * @param is_dash ダッシュの場合はtrueを設定し、そうでない場合はfalseを設定する。
	 * @return 近接武器のダメージ。
	 */
	public int getSlashDamage(boolean is_dash) {
		String damage_str = selectSlashString(is_dash);
		
		// チャージ武器の場合、最大チャージ時の威力の文字列を取得する
		if(isChargeWeapon()) {
			damage_str = getChargeString(damage_str, getChargeMaxCount(damage_str) - 1);
		}
		
		return getSlashSumPower(damage_str);
	}
	
	/**
	 * 近接武器のダメージを取得する。
	 * @param is_dash ダッシュの場合はtrueを設定し、そうでない場合はfalseを設定する。
	 * @return 近接武器のダメージ。
	 */
	public int getSlashDamage(boolean is_dash, int charge_level) {
		String damage_str = "";

		// チェーンソー系統はダッシュ時も通常攻撃とダメージが同じ
		if(existCategory("チェーンソー系統")) {
			damage_str = selectSlashString(false);
		}
		else {
			damage_str = selectSlashString(is_dash);
		}
		
		// チャージ武器の場合、チャージレベルに応じた威力の文字列を取得する
		if(isChargeWeapon()) {
			damage_str = getChargeString(damage_str, charge_level);
		}
		
		return getSlashSumPower(damage_str);
	}
	
	/**
	 * 近接武器の威力の文字列を取得する
	 * @param is_dash
	 * @return
	 */
	private String selectSlashString(boolean is_dash) {
		String damage_str = "";
		
		if(is_dash) {
			damage_str = super.get("特殊攻撃(威力)");
		}
		else {
			damage_str = super.get("通常攻撃(威力)");
		}
		
		// 括弧内の文字列を取得する
		int start_idx = damage_str.indexOf("(");
		int end_idx = damage_str.indexOf(")", start_idx);
		
		if(start_idx >= 0 || end_idx >= 0) {
			damage_str = damage_str.substring(start_idx + 1, end_idx);
		}
		
		return damage_str;
	}
	
	/**
	 * 近接武器の威力の文字列から合計ダメージ値を取得する.
	 * 合計ダメージは"="以降の数値とする。
	 * @param damage_str 武器の文字列データ
	 * @return 合計ダメージ値
	 */
	private int getSlashSumPower(String damage_str) {
		int ret = 0;

		// イコール以降の文字列を取得する
		int start_idx = damage_str.indexOf("=");
		if(start_idx >= 0) {
			damage_str = damage_str.substring(start_idx + 1);
		}

		// 数値に変換する
		try {
			ret = Integer.valueOf(damage_str);

		} catch(NumberFormatException e) {
			ret = 0;
		}
		
		return ret;
	}

	/**
	 * 特別装備のチャージ時間を算出する。(非SP枯渇時)
	 * @return チャージ時間。値が無い場合は0を返す。
	 */
	public double getSpChargeTime() {
		return getSpChargeTime(false);
	}
	
	/**
	 * 特別装備のチャージ時間を算出する。
	 * @param is_overheat SPが枯渇しているかどうか。枯渇している場合は時間が1.2倍となる。
	 * @return チャージ時間。値が無い場合は0を返す。
	 */
	public double getSpChargeTime(boolean is_overheat) {
		double ret = 0;
		
		try {
			ret = Double.valueOf(super.get("チャージ時間"));
			
			if(is_overheat) {
				ret = ret * 1.2;
			}
			
		} catch(Exception e) {
			ret = 0;
		}
		
		return ret;
	}
	
	/**
	 * バリア装備の秒間耐久回復量を表示する。
	 * @return 秒間耐久回復量
	 */
	public double getBattleBarrierGuard() {
		int guard = 0;
		
		try {
			guard = Integer.valueOf(super.get("耐久力"));
			
		} catch(Exception e) {
			guard = 0;
		}
		
		return guard / getSpChargeTime();
	}

	/**
	 * スイッチ武器かどうか
	 * @return スイッチ武器の場合はtrueを返し、そうでない場合はfalseを返す。
	 */
	public boolean isSwitchWeapon() {
		boolean ret = false;

		if(mTypeB_data != null) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 大破可能な装甲値(スペック)を表示する。
	 * @param is_critical クリティカルかどうか
	 * @return
	 */
	public double getArmorBreakJdg(boolean is_critical) {
		double power = getShotAntiStability(is_critical);
		double damege_rate = (SpecValues.BLUST_LIFE_MAX + 5000) / power;
		return 100 - (damege_rate * 100);
	}
	
	/**
	 * 転倒可能な装甲値(スペック)を表示する。
	 * @param is_critical クリティカルかどうか
	 * @return
	 */
	public double getArmorDownJdg(boolean is_critical) {
		double power = getShotAntiStability(is_critical);
		double damege_rate = SpecValues.BLUST_DOWN_DAMAGE / power;
		return 100 - (damege_rate * 100);
	}
	
	/**
	 * ノックバック可能な装甲値(スペック)を表示する。
	 * @param is_critical クリティカルかどうか
	 * @return
	 */
	public double getArmorKBJdg(boolean is_critical) {
		double power = getShotAntiStability(is_critical);
		double damege_rate = SpecValues.BLUST_KB_DAMAGE / power;
		return 100 - (damege_rate * 100);
	}
}
