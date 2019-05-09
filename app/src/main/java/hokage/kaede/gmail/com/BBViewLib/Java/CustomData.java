package hokage.kaede.gmail.com.BBViewLib.Java;

import java.util.ArrayList;

/**
 * アセンの詳細情報を保持するクラス。
 */
public class CustomData {
	private BBData[] mRecentParts;
	private BBData[] mRecentAssalt;
	private BBData[] mRecentHeavy;
	private BBData[] mRecentSniper;
	private BBData[] mRecentSupport;
	private BBData mReqArm; // 要請兵器

	// チップデータ
	private BBData[] mRecentHeadChips;
	private BBData[] mRecentBodyChips;
	private BBData[] mRecentArmsChips;
	private BBData[] mRecentLegsChips;
	private BBData[] mRecentSupportChips;
	private static final int CHIP_MAX_COUNT = 2;
	
	// 各種状態のモード設定
	private int mMode;
	
	public static final int MODE_NORMAL        = 0;  // 通常時
	public static final int MODE_SB            = 1;  // サテライトバンカー運搬中
	public static final int MODE_SBR           = 2;  // サテライトバンカーR運搬中
	public static final int MODE_REQARM        = 3;  // 要請兵器装着中
	public static final int MODE_ROCKON        = 4;  // ロックオン中
	public static final int MODE_MOVE          = 5;  // 移動中
	public static final int MODE_MOVE_HIWATER  = 6;  // 移動中(浅い水中)
	public static final int MODE_MOVE_LOWATER  = 7;  // 移動中(深い水中)
	public static final int MODE_MOVE_UPWATER  = 8;  // 移動中(深い水中)
	public static final int MODE_PLANT         = 9;  // 自軍プラント内
	public static final int MODE_ATTACK_DEF    = 10; // N-DEF攻撃中
	public static final int MODE_ATTACK_OBJ    = 11; // 施設攻撃中
	
	public static final String[] CUSTOM_MODES = {
		"通常時",
		"SB運搬中",
		"SBR運搬中",
		"要請兵器装着中",
		"ロックオン中",
		"移動中",
		"移動中(浅い水中)",
		"移動中(深い水中)",
		"移動中(水上移動)",
		"自軍プラント内",
		"N-DEF攻撃中",
		"施設攻撃中"
	};

	private static final int HEAD_IDX = 0;
	private static final int BODY_IDX = 1;
	private static final int ARMS_IDX = 2;
	private static final int LEGS_IDX = 3;

	/**
	 * 初期化を行う。
	 */
	public CustomData() {
		mMode = MODE_NORMAL;
		
		mRecentParts   = new BBData[BBDataManager.BLUST_PARTS_LIST.length];
		mRecentAssalt  = new BBData[BBDataManager.WEAPON_TYPE_LIST.length];
		mRecentHeavy   = new BBData[BBDataManager.WEAPON_TYPE_LIST.length];
		mRecentSniper  = new BBData[BBDataManager.WEAPON_TYPE_LIST.length];
		mRecentSupport = new BBData[BBDataManager.WEAPON_TYPE_LIST.length];

		mRecentHeadChips = new BBData[CHIP_MAX_COUNT];
		mRecentBodyChips = new BBData[CHIP_MAX_COUNT];
		mRecentArmsChips = new BBData[CHIP_MAX_COUNT];
		mRecentLegsChips = new BBData[CHIP_MAX_COUNT];
		mRecentSupportChips = new BBData[CHIP_MAX_COUNT];

		mReqArm = new BBData();
		
		// 配列を初期化する。
		initBBDataArrays(mRecentParts, mRecentAssalt, mRecentHeavy, mRecentSniper, mRecentSupport);
	}
	
	/**
	 * BBDataの配列を初期化する。
	 * @param arrays 初期化対象の配列のリスト。(※複数の配列を同時に初期化)
	 */
	private static void initBBDataArrays(BBData[]... arrays) {
		int size = arrays.length;
		for(int i=0; i<size; i++) {
			BBData[] array = arrays[i];
			int ary_size = array.length;
			for(int j=0; j<ary_size; j++) {
				array[j] = new BBData();
			}
		}
	}
	
	//----------------------------------------------------------
	// データ設定系の関数
	//----------------------------------------------------------

	/**
	 * パーツデータまたは武器データを設定する。
	 * 種類は自動的に判定し、適切な場所に設定する。
	 * @param data パーツデータまたは武器データ
	 */
	public void setData(BBData data) {
		// パラメータがnullの場合、処理を行わない
		if(data == null) {
			return;
		}
		
		// パーツの場合のデータ格納を実行
		int parts_len = BBDataManager.BLUST_PARTS_LIST.length;
		String[] parts_type = BBDataManager.BLUST_PARTS_LIST;
		
		for(int i=0; i<parts_len; i++) {
			if(data.existCategory(parts_type[i])) {
				mRecentParts[i] = data;
				return;
			}
		}
		
		// 武器の場合のデータ格納を実行
		int blust_len  = BBDataManager.BLUST_TYPE_LIST.length;
		int weapon_len = BBDataManager.WEAPON_TYPE_LIST.length;
		String[] blust_type  = BBDataManager.BLUST_TYPE_LIST;
		String[] weapon_type = BBDataManager.WEAPON_TYPE_LIST;
		
		for(int i=0; i<blust_len; i++) {
			if(!data.existCategory(blust_type[i])) {
				continue;
			}
			
			for(int j=0; j<weapon_len; j++) {
				if(data.existCategory(weapon_type[j])) {
					BBData[] target_list = getWeaponList(blust_type[i]);
					target_list[j] = data;
					return;
				}
			}
		}
		
		// 要請兵器の場合のデータ格納を実行
		if(data.existCategory(BBDataManager.REQARM_STR)) {
			this.mReqArm = data;
			return;
		}
	}

	/**
	 * アセンデータの算出モードを設定する。
	 * @param mode モード
	 */
	public void setMode(int mode) {
		mMode = mode;
	}
	
	/**
	 * 要請兵器の情報を設定する。
	 * @param data 要請兵器のデータ
	 */
	public void setReqArm(BBData data) {
		this.mReqArm = data;
	}
	
	/**
	 * 要請兵器の情報を取得する。
	 * @return 要請兵器のデータ
	 */
	public BBData getReqArm() {
		return this.mReqArm;
	}

	/**
	 * チップをセットする。
	 * @param chip 設定するチップ
	 * @param type 設定先の種類。空文字を指定した場合はサポートチップとして扱う。
	 * @param index 設定位置
	 */
	public void setChip(BBData chip, String type, int index) {

		if(type.equals(BBDataManager.BLUST_PARTS_HEAD)) {
			mRecentHeadChips[index] = chip;
		}
		else if(type.equals(BBDataManager.BLUST_PARTS_BODY)) {
			mRecentBodyChips[index] = chip;
		}
		else if(type.equals(BBDataManager.BLUST_PARTS_ARMS)) {
			mRecentArmsChips[index] = chip;
		}
		else if(type.equals(BBDataManager.BLUST_PARTS_LEGS)) {
			mRecentLegsChips[index] = chip;
		}
		else {
			mRecentSupportChips[index] = chip;
		}
	}

	/**
	 * チップを取得する。
	 * @param type 取得先の種類。
	 * @param index 取得位置。
	 * @return 所定のチップを取得する。空文字を指定した場合はサポートチップを返す。
	 */
	public BBData getChip(String type, int index) {

		if(type.equals(BBDataManager.BLUST_PARTS_HEAD)) {
			return mRecentHeadChips[index];
		}
		else if(type.equals(BBDataManager.BLUST_PARTS_BODY)) {
			return mRecentBodyChips[index];
		}
		else if(type.equals(BBDataManager.BLUST_PARTS_ARMS)) {
			return mRecentArmsChips[index];
		}
		else if(type.equals(BBDataManager.BLUST_PARTS_LEGS)) {
			return mRecentLegsChips[index];
		}
		else {
			return mRecentSupportChips[index];
		}
	}

	/**
	 * チップデータのリストを取得する。
	 * @return チップデータのリスト
	 */
	public ArrayList<BBData> getChipList() {
		ArrayList<BBData> list = new ArrayList<BBData>(10);

		list.add(mRecentHeadChips[0]);
		list.add(mRecentHeadChips[1]);
		list.add(mRecentBodyChips[0]);
		list.add(mRecentBodyChips[1]);
		list.add(mRecentArmsChips[0]);
		list.add(mRecentArmsChips[1]);
		list.add(mRecentLegsChips[0]);
		list.add(mRecentLegsChips[1]);
		list.add(mRecentSupportChips[0]);
		list.add(mRecentSupportChips[1]);

		return list;
	}
	
	//----------------------------------------------------------
	// データ判定系の関数
	//----------------------------------------------------------

	/**
	 * ホバー脚部かどうかを判別する。
	 * @return ホバー脚部の場合はtrueを返し、ホバー脚部でない場合はfalseを返す。
	 */
	public boolean isHoverLegs() {
		String parts_name = mRecentParts[LEGS_IDX].get("名称");
		
		if(parts_name.startsWith("B.U.Z.")) {
			return true;
		}
		else if(parts_name.startsWith("ロージー")) {
			return true;
		}
		else if(parts_name.startsWith("ネレイド")) {
			return true;
		}
		else if(parts_name.startsWith("フォーミュラ")) {
			return true;	
		}
		else if(parts_name.startsWith("スペクター")) {
			return true;	
		}
		
		return false;
	}

	/**
	 * 中量機体かどうかを判別する。
	 * @param parts_idx パーツの部位
	 * @return 中量機体の場合はtrueを返し、中量機体でない場合はfalseを返す。
	 */
	public boolean isNormalWeightParts(int parts_idx) {
		String parts_name = mRecentParts[parts_idx].get("名称");

		if(parts_name.startsWith("クーガー")) {
			return true;
		}
		else if(parts_name.startsWith("エンフォーサー")) {
			return true;
		}
		else if(parts_name.startsWith("ツェーブラ")) {
			return true;
		}
		else if(parts_name.startsWith("輝星")) {
			return true;
		}

		return false;
	}

	/**
	 * 重量機体かどうかを判別する。
	 * @param parts_idx パーツの部位
	 * @return 重量機体の場合はtrueを返し、重量機体でない場合はfalseを返す。
	 */
	public boolean isHeavyWeightParts(int parts_idx) {
		String parts_name = mRecentParts[parts_idx].get("名称");

		if(parts_name.startsWith("ヘヴィガード")) {
			return true;
		}
		else if(parts_name.startsWith("ケーファー")) {
			return true;
		}

		return false;
	}

	/**
	 * チップのセット数を取得する。
	 * @param name チップの名前
	 * @return セットされている場合はtrueを返し、セットされていない場合はfalseを返す。
	 */
	private int countChip(String name) {
		int count = 0;

		count += countChipArray(name, mRecentHeadChips);
		count += countChipArray(name, mRecentBodyChips);
		count += countChipArray(name, mRecentArmsChips);
		count += countChipArray(name, mRecentLegsChips);
		count += countChipArray(name, mRecentSupportChips);

		return count;
	}

	/**
	 * 所定の設定個所のチップのセット数を取得する。
	 * @param name チップの名前
	 * @param chips 設定場所
	 * @return セットされている場合はtrueを返し、セットされていない場合はfalseを返す。
	 */
	private int countChipArray(String name, BBData[] chips) {
		int size = chips.length;
		int count = 0;

		for(int i=0; i<size; i++) {
			BBData item = chips[i];
			String item_name = item.get("名称");
			if(item_name.equals(name)) {
				count++;
			}
		}

		return count;
	}

	//----------------------------------------------------------
	// パーツ、武器、チップデータ取得系
	//----------------------------------------------------------

	/**
	 * パーツの種類の配列格納番号を取得する。
	 * @param parts_type パーツの種類
	 * @return 配列格納番号
	 */
	private int getPartsTypeIndex(String parts_type) {
		int ret = -1;
		int size = BBDataManager.BLUST_PARTS_LIST.length;
		for(int i=0; i<size; i++) {
			if(parts_type.equals(BBDataManager.BLUST_PARTS_LIST[i])) {
				ret = i;
				break;
			}
		}
		
		return ret;
	}

	/**
	 * パーツデータを取得する。
	 * @param parts_type パーツの種類。
	 * @return パーツデータ
	 */
	public BBData getParts(String parts_type) {
		return mRecentParts[getPartsTypeIndex(parts_type)];
	}

	/**
	 * パーツデータリストを取得する
	 * @return パーツデータリスト
	 */
	public BBData[] getPartsList() {
		return mRecentParts;
	}
	
	/**
	 * 指定兵装の指定種類の武器を取得する。
	 * @param blust_type 兵装
	 * @param weapon_type 武器の種類
	 * @return 武器データ。兵装名、武器の種類が不正の場合、nullを返す。
	 */
	public BBData getWeapon(String blust_type, String weapon_type) {
		BBData ret = null;
		BBData[] blust_weapon_list = getWeaponList(blust_type);
		
		if(blust_weapon_list != null) {
			int size = blust_weapon_list.length;
			
			for(int i=0; i<size; i++) {
				if(weapon_type.equals(BBDataManager.WEAPON_TYPE_LIST[i])) {
					ret = blust_weapon_list[i];
					break;
				}
			}
		}
		
		return ret;
	}

	/**
	 * 指定兵装の武器データリストを取得する
	 * @param blust_type 武器データリストを取得する兵装
	 * @return 武器データリスト
	 */
	public BBData[] getWeaponList(String blust_type) {
		if(blust_type.equals(BBDataManager.BLUST_TYPE_ASSALT)) {
			return mRecentAssalt;
		}
		else if(blust_type.equals(BBDataManager.BLUST_TYPE_HEAVY)) {
			return mRecentHeavy;
		}
		else if(blust_type.equals(BBDataManager.BLUST_TYPE_SNIPER)) {
			return mRecentSniper;
		}
		else if(blust_type.equals(BBDataManager.BLUST_TYPE_SUPPORT)) {
			return mRecentSupport;
		}
		
		return null;
	}

	//----------------------------------------------------------
	// 性能値取得系(機体)
	//----------------------------------------------------------
	
	/**
	 * パーツの総重量を取得する。サテライトバンカー/要請兵器が有効な場合はここで重量を加算する。
	 * @return 総重量
	 */
	public double getPartsWeight() {
		double ret = 0;
		int parts_len = mRecentParts.length;
		double reqarm_weight = 0;

		try {
			for(int i=0; i<parts_len; i++) {
				String str_buf = mRecentParts[i].get("重量");
				double buf = Double.parseDouble(str_buf);
				ret = ret + buf;
			}

			reqarm_weight = Double.parseDouble(mReqArm.get("重量"));
		} catch(Exception e) {
			// Do Nothing
		}
		
		// サテライトバンカー/要請兵器の重量を加算する
		double carried_weight = 0;
		if(mMode == MODE_SB) {
			carried_weight = SpecValues.SB_WEIGHT;
		}
		else if(mMode == MODE_SBR) {
			carried_weight = SpecValues.SBR_WEIGHT;
		}
		else if(mMode == MODE_REQARM) {
			carried_weight = reqarm_weight;
		}

		/*
		// チップの効果を反映する
		if(existChip("運搬適性")) {
			ret = ret + (int)(carried_weight * 0.85);
		}
		else if(existChip("運搬適性II")) {
			ret = ret + (int)(carried_weight * 0.7);
		}
		else {
			ret = ret + carried_weight;
		}
		*/
		ret = ret + carried_weight;

		return ret;
	}
	
	/**
	 * パーツの総重量に対する積載猶予を取得する
	 * @return 積載猶予
	 */
	public double getSpacePartsWeight() {
		return getAntiWeight() - getPartsWeight();
	}

	/**
	 * 装甲の平均値を取得する
	 * @return 装甲の平均値
	 */
	public double getArmorAve() {
		double ret = 0;
		
		ret += 0.25 * getArmor(HEAD_IDX);
		ret += 0.25 * getArmor(BODY_IDX);
		ret += 0.25 * getArmor(ARMS_IDX);
		ret += 0.25 * getArmor(LEGS_IDX);
		
		return ret;
	}
	
	/**
	 * 装甲平均値を取得する。(空爆時)
	 * @return 装甲平均値
	 */
	public double getArmorAveHead() {
		double ret = 0;
		
		ret += 0.3 * getArmor(HEAD_IDX);
		ret += 0.3 * getArmor(BODY_IDX);
		ret += 0.3 * getArmor(ARMS_IDX);
		ret += 0.1 * getArmor(LEGS_IDX);
		
		return ret;
	}

	/**
	 * 装甲平均値を取得する。(地爆時)
	 * @return 装甲平均値
	 */
	public double getArmorAveLegs() {
		double ret = 0;
		
		ret += 0.05 * getArmor(HEAD_IDX);
		ret += 0.05 * getArmor(BODY_IDX);
		ret += 0.20 * getArmor(ARMS_IDX);
		ret += 0.70 * getArmor(LEGS_IDX);
		
		return ret;
	}

	//----------------------------------------------------------
	// 性能取得系(パーツ)
	//----------------------------------------------------------

	/**
	 * 指定のキーのポイント値を取得する。
	 * @param key 性能名
	 * @return ポイント値
	 */
	public String getPoint(String key) {
		int parts_len = mRecentParts.length;
		for(int i=0; i<parts_len; i++) {
			if(mRecentParts[i].existKey(key)) {
				return mRecentParts[i].get(key);
			}
		}
		
		return "";
	}

    /**
     * 指定のキーを保持するパーツ名を取得する。
     * @param key 性能名
     * @return パーツ名
     */
	public String getPartsName(String key) {
        int parts_len = mRecentParts.length;
        for(int i=0; i<parts_len; i++) {
            if(mRecentParts[i].existKey(key)) {
                return mRecentParts[i].get("名称");
            }
        }

        return "";
    }

	/**
	 * 指定部位の装甲値を取得する。
	 * @param parts_type 指定のパーツ種類
	 * @return 装甲値
	 */
	public double getArmor(String parts_type) {
		int idx = getPartsTypeIndex(parts_type);
		return getArmor(idx);
	}

	/**
	 * 指定部位の装甲値を取得する
	 * @param idx 指定位置
	 * @return 装甲値
	 */
	public double getArmor(int idx) {
		double ret = mRecentParts[idx].getArmor();

		// チップ上乗せ分を反映する
		if(idx == HEAD_IDX) {
			ret = ret + (2 * countChip("頭部装甲"));
		}
		else if(idx == BODY_IDX) {
			ret = ret + (2 * countChip("胴部装甲"));
		}
		else if(idx == ARMS_IDX) {
			ret = ret + (2 * countChip("腕部装甲"));
		}
		else if(idx == LEGS_IDX) {
			ret = ret + (2 * countChip("脚部装甲"));
		}

		if(isNormalWeightParts(idx)) {
			ret = ret + (1 * countChip("中量機体強化"));
		}
		else if(isHeavyWeightParts(idx)) {
			ret = ret + (1 * countChip("重量機体強化"));
		}

		return ret;
	}
	
	/**
	 * 射撃補正の値を取得する
	 * @return 射撃補正の値。
	 */
	public double getShotBonus() {
		double ret = 0;

		BBData head_parts = mRecentParts[HEAD_IDX];
		String spec = head_parts.get("射撃補正");
		ret = SpecValues.getSpecValue(spec, "射撃補正", "");

		// チップセットボーナス
		ret = ret + (1.0 * countChip("射撃補正"));
		ret = ret + (1.5 * countChip("射撃補正II"));

		if(ret == SpecValues.ERROR_VALUE) {
			ret = 0;
		}

		return ret;
	}
	
	/**
	 * 索敵の値を取得する。
	 * @return 索敵の値。単位はm。
	 */
	public int getSearch() {
		double ret = 0;

		BBData head_parts = mRecentParts[HEAD_IDX];
		String spec = head_parts.get("索敵");
		ret = SpecValues.getSpecValue(spec, "索敵", "");

		// チップセットボーナス
		ret = ret + (15 * countChip("索敵"));
		ret = ret + (24 * countChip("索敵II"));

		if(ret == SpecValues.ERROR_VALUE) {
			ret = 0;
		}

		return (int)ret;
	}
	
	/**
	 * ロックオンの値を取得する。
	 * @return ロックオンの値。単位はm。
	 */
	public int getRockOn() {
		double ret = 0;

		BBData head_parts = mRecentParts[HEAD_IDX];
		String spec = head_parts.get("ロックオン");
		ret = SpecValues.getSpecValue(spec, "ロックオン", "");

		// チップセットボーナス
		ret = ret + (3 * countChip("ロックオン"));
		ret = ret + (5 * countChip("ロックオンII"));

		if(ret == SpecValues.ERROR_VALUE) {
			ret = 0;
		}

		return (int)ret;
	}

	/**
	 * DEF回復の値を取得する
	 * @return DEF回復の値。
	 */
	public double getDefRecover() {
		double ret = 0;

		BBData head_parts = mRecentParts[HEAD_IDX];
		String spec = head_parts.get("DEF回復");
		ret = SpecValues.getSpecValue(spec, "DEF回復", "");

		// チップセットボーナス
		ret = ret + (3.0 * countChip("DEF回復"));
		ret = ret + (5.0 * countChip("DEF回復II"));

		if(ret == SpecValues.ERROR_VALUE) {
			ret = 0;
		}

		return ret;
	}

	/**
	 * DEF回復時間を取得する。
	 * @return
	 */
	public double getDefRecoverTime() {
		return 30.0 / (1 + (getDefRecover() / 100));
	}
	
	/**
	 * ブースターの値を取得する。
	 * @return ブースターの値。
	 */
	public double getBoost() {
		BBData body_parts = mRecentParts[BODY_IDX];
		double ret = body_parts.getBoost();

		// チップセットボーナス
		ret = ret + (2 * countChip("ブースター"));
		ret = ret + (3 * countChip("ブースターII"));
			
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
	 * SP供給の値を取得する。
	 * @return SP供給の値。
	 */
	public double getSP() {
		double ret = 0;

		BBData body_parts = mRecentParts[BODY_IDX];
		String spec = body_parts.get("SP供給");
		ret = SpecValues.getSpecValue(spec, "SP供給", "");

		// チップセットボーナス
		ret = ret + (3.0 * countChip("SP供給"));
		ret = ret + (4.5 * countChip("SP供給II"));

		if(ret == SpecValues.ERROR_VALUE) {
			ret = 0;
		}

		return ret;
	}
	
	/**
	 * エリア移動の値を取得する。
	 * @return エリア移動の値。
	 */
	public double getAreaMove() {
		double ret = 0;

		BBData body_parts = mRecentParts[BODY_IDX];
		String spec = body_parts.get("エリア移動");
		ret = SpecValues.getSpecValue(spec, "エリア移動", "");

		// チップセットボーナス
		ret = ret - (0.20 * countChip("エリア移動"));

		if(ret == SpecValues.ERROR_VALUE) {
			ret = 0;
		}

		// エリア移動の上限設定
		if(ret < 2.0) {
			ret = 2.0;
		}

		return ret;
	}

	/**
	 * DEF耐久の値を取得する。
	 * @return DEF耐久の値。
	 */
	public double getDefGuard() {
		double ret = 0;

		BBData body_parts = mRecentParts[BODY_IDX];
		String spec = body_parts.get("DEF耐久");
		ret = SpecValues.getSpecValue(spec, "DEF耐久", "");

		// チップセットボーナス
		ret = ret + (80 * countChip("DEF耐久"));
		ret = ret + (120 * countChip("DEF耐久II"));

		if(ret == SpecValues.ERROR_VALUE) {
			ret = 0;
		}

		return ret;
	}
	
	/**
	 * 反動吸収の値を取得する。
	 * @return 反動吸収の値。単位は%。
	 */
	public double getRecoil() {
		double ret = 0;

		BBData arms_parts = mRecentParts[ARMS_IDX];
		String spec = arms_parts.get("反動吸収");
		ret = SpecValues.getSpecValue(spec, "反動吸収", "");

		// チップセットボーナス
		ret = ret + (3.0 * countChip("反動吸収"));
		ret = ret + (4.5 * countChip("反動吸収II"));

		if(ret == SpecValues.ERROR_VALUE) {
			ret = 0;
		}

		return ret;
	}
	
	/**
	 * リロードの倍率を取得する。
	 * @return リロードの倍率。
	 */
	public double getReload() {
		double ret = 0;

		BBData arms_parts = mRecentParts[ARMS_IDX];
		String spec = arms_parts.get("リロード");
		ret = SpecValues.getSpecValue(spec, "リロード", "");

		// チップセットボーナス
		ret = ret + (1.0 * countChip("リロード"));
		ret = ret + (1.5 * countChip("リロードII"));

		if(ret == SpecValues.ERROR_VALUE) {
			ret = 0;
		}

		return ret;
	}
	
	/**
	 * 武器変更の値を取得する。
	 * @return 武器変更の値。単位は秒。
	 */
	public double getChangeWeapon() {
		double ret = 0;

		BBData arms_parts = mRecentParts[ARMS_IDX];
		String spec = arms_parts.get("武器変更");
		ret = SpecValues.getSpecValue(spec, "武器変更", "");

		// チップセットボーナス
		ret = ret + (2 * countChip("武器変更"));
		ret = ret + (3 * countChip("武器変更II"));

		if(ret == SpecValues.ERROR_VALUE) {
			ret = 0;
		}

		return ret;
	}

	/**
	 * 予備弾数の値を取得する。
	 * @return 予備弾数の値。
	 */
	public double getSpareBullet() {
		double ret = 0;

		BBData arms_parts = mRecentParts[ARMS_IDX];
		String spec = arms_parts.get("予備弾数");
		ret = SpecValues.getSpecValue(spec, "予備弾数", "");

		// チップセットボーナス
		ret = ret + (2 * countChip("予備弾数"));
		ret = ret + (3 * countChip("予備弾数II"));

		if(ret == SpecValues.ERROR_VALUE) {
			ret = 0;
		}

		return ret;
	}

	/**
	 * 歩行速度を取得する
	 * @return 歩行速度。単位はkm/h。
	 */
	public double getWalk() {
		double ret = 0.0;

		BBData legs_parts = mRecentParts[LEGS_IDX];
		String spec = legs_parts.get("歩行");
		ret = SpecValues.getSpecValue(spec, "歩行", "");

		// チップセットボーナス(km/h)
		ret = ret + (1.08 * countChip("歩行"));
		ret = ret + (1.62 * countChip("歩行II"));

		// ホバー脚部の場合の補正値を計算する
		boolean is_hover = isHoverLegs();
		if(is_hover) {
			ret = ret * 4 / 3;
		}

		// 上限値によるガードを行う (ホバー：14.70[m/s], 二脚：11.02[m/s])
		if(is_hover && ret > 14.70) {
			ret = 14.70;
		}
		else if(!is_hover && ret > 11.02) {
			ret = 11.02;
		}

		if(ret == SpecValues.ERROR_VALUE) {
			ret = 0;
		}

		return ret;
	}
	
	/**
	 * ダッシュ速度を取得する。
	 * @return ダッシュ速度。
	 */
	public double getDash() {
		return getDash(true);
	}
	
	/**
	 * ダッシュ速度を取得する。
	 * @param is_start 初速かどうか。
	 * @return ダッシュ速度。
	 */
	public double getDash(boolean is_start) {
		double ret = calcDash();

		// 巡航補正計算を行う。
		ret = calcNormalDush(ret, is_start);

		return ret;
	}
	
	/**
	 * 重量耐性の値を取得する。
	 * @return 重量耐性の値。
	 */
	public int getAntiWeight() {
		double ret = 0;

		BBData legs_parts = mRecentParts[LEGS_IDX];
		String spec = legs_parts.get("重量耐性");
		String name = legs_parts.get("名称");
		ret = SpecValues.getSpecValue(spec, "重量耐性", name);

		// チップセットボーナス
		ret = ret + (40 * countChip("重量耐性"));
		ret = ret + (60 * countChip("重量耐性II"));

		if(ret == SpecValues.ERROR_VALUE) {
			ret = 0;
		}

		return (int)ret;
	}

	/**
	 * 巡航の値を取得する。
	 * @return 加速の値。
	 */
	public double getAcceleration() {
		double ret = 0;

		BBData legs_parts = mRecentParts[LEGS_IDX];
		String spec = legs_parts.get("巡航");
		ret = SpecValues.getSpecValue(spec, "巡航", "");

		// チップセットボーナス
		ret = ret + (0.36 * countChip("巡航"));
		ret = ret + (0.54 * countChip("巡航II"));

		if(ret == SpecValues.ERROR_VALUE) {
			ret = 0;
		}

		return ret;
	}

	//----------------------------------------------------------
	// 性能取得系(各兵装)
	//----------------------------------------------------------

	/**
	 * 指定兵装の総重量を取得する。
	 * @param blust_type 総重量を取得する兵装
	 * @return 総重量
	 */
	public double getWeight(String blust_type) {
		return getPartsWeight() + getWeaponsWeight(blust_type);
	}
	
	/**
	 * 指定兵装の積載猶予を取得する
	 * @param blust_type 積載猶予を取得する兵装
	 * @return 積載猶予
	 */
	public double getSpaceWeight(String blust_type) {
		return getAntiWeight(blust_type) - getWeight(blust_type);
	}

	/**
	 * 指定兵装の武器の総重量を取得する。
	 * @param blust_type 総重量を取得する兵装
	 * @return 総重量
	 */
	private double getWeaponsWeight(String blust_type) {
		double ret = 0;
		
		BBData[] blust_data = getWeaponList(blust_type);
		int blust_len = blust_data.length;

		try {
			for(int i=0; i<blust_len; i++) {
				String str_buf = blust_data[i].get("重量");
				double buf = Double.parseDouble(str_buf);
				ret = ret + buf;
			}
		} catch(Exception e) {
			// e.printStackTrace();
		}
		
		return ret;
	}

	/**
	 * 指定部位の装甲値を取得する。
	 * @param blust_type 兵装の種類
	 * @param parts_type パーツ種別番号
	 * @return 装甲値
	 */
	public double getArmor(String blust_type, String parts_type) {
		int idx = getPartsTypeIndex(parts_type);
		return getArmor(blust_type, idx);
	}
	
	/**
	 * 指定部位の装甲値を取得する。
	 * @param blust_type 兵装の種類
	 * @param idx パーツ種別番号
	 * @return 装甲値
	 */
	public double getArmor(String blust_type, int idx) {
		double ret = getArmor(idx);

		/*
		if(existChip("重火力兵装強化") && blust_type.equals("重火力兵装")) {
			ret = ret + 1;
		}
		else if(existChip("重火力兵装強化II") && blust_type.equals("重火力兵装")) {
			ret = ret + 2;
		}
		*/
		
		return ret;
	}
	
	/**
	 * 装甲平均値を取得する。
	 * @param blust_type 兵装の種類
	 * @return 装甲値の平均値
	 */
	public double getArmorAve(String blust_type) {
		double ret = getArmorAve();

		/*
		if(existChip("重火力兵装強化") && blust_type.equals("重火力兵装")) {
			ret = ret + 1;
		}
		else if(existChip("重火力兵装強化II") && blust_type.equals("重火力兵装")) {  // 暫定対応。値は不明。
			ret = ret + 2;
		}
		*/
		
		return ret;
	}

	/**
	 * 射撃補正の値を取得する。
	 * @param blust_type 兵装の種類
	 * @return 射撃補正の値
	 */
	public double getShotBonus(String blust_type) {
		double ret = getShotBonus();

		/*
		if(existChip("遊撃兵装強化") && blust_type.equals("遊撃兵装")) {
			ret = ret + 0.03;
		}
		else if(existChip("遊撃兵装強化II") && blust_type.equals("遊撃兵装")) {  // 暫定対応。値は不明。
			ret = ret + 0.06;
		}
		*/
		
		return ret;
	}

	/**
	 * 索敵の値を取得する。
	 * @param blust_type 兵装の種類
	 * @return 索敵の値
	 */
	public int getSearch(String blust_type) {
		return getSearch();
	}

	/**
	 * ロックオンの値を取得する。
	 * @param blust_type 兵装の種類
	 * @return ロックオンの値。
	 */
	public int getRockOn(String blust_type) {
		return getRockOn();
	}

	/**
	 * DEF回復の値を取得する。
	 * @param blust_type 兵装の種類
	 * @return DEF回復の値。
	 */
	public double getDefRecover(String blust_type) {
		return getDefRecover();
	}
	
	/**
	 * ブースターの値を取得する。
	 * @param blust_type 兵装の種類
	 * @return ブースターの値。
	 */
	public double getBoost(String blust_type) {
		double ret = getBoost();

		/*
		if(existChip("強襲兵装強化") && blust_type.equals("強襲兵装")) {
			ret = ret + 3;
		}
		else if(existChip("強襲兵装強化II") && blust_type.equals("強襲兵装")) {  // 暫定対応。値は不明。
			ret = ret + 6;
		}
		*/
		
		return ret;
	}

	/**
	 * 最大ステップ回数を取得する。(1ステップの消費量は12)
	 * @return 最大ステップ回数
	 */
	public double getStepMaxCount(String blust_type) {		
		return Math.ceil(getBoost(blust_type) / 12.0);
	}
	
	/**
	 * ブースター回復時間を取得する。(非OH時は毎秒50回復する)
	 * @return ブースター回復時間
	 */
	public double getBoostChargeTime(String blust_type) {
		double charge = 50;

		/*
		if(existChip("ブースター回復")) {
			charge = charge * 1.1;
		}
		else if(existChip("ブースター回復II")) {
			charge = charge * 1.25;
		}
		*/
		
		return getBoost(blust_type) / charge;
	}
	
	/**
	 * SP供給の値を取得する。
	 * @param blust_type 兵装の種類
	 * @return SP供給の値。
	 */
	public double getSP(String blust_type) {
		double ret = getSP();

		/*
		if(existChip("支援兵装強化") && blust_type.equals("支援兵装")) {
			ret = ret + 0.04;
		}
		else if(existChip("支援兵装強化II") && blust_type.equals("支援兵装")) {  // 暫定対応。値は不明。
			ret = ret + 0.08;
		}
		*/
		
		return ret;
	}

	/**
	 * エリア移動の値を取得する。
	 * @param blust_type 兵装の種類
	 * @return エリア移動の値。
	 */
	public double getAreaMove(String blust_type) {
		double ret = getAreaMove();

		/*
		if(existChip("支援兵装強化") && blust_type.equals("支援兵装")) {
			ret = ret - 0.50;
		}
		else if(existChip("支援兵装強化II") && blust_type.equals("支援兵装")) {  // 暫定対応。値は不明。
			ret = ret - 1.00;
		}
		*/

		// エリア移動の上限設定
		if(ret < 2.0) {
			ret = 2.0;
		}
		
		return ret;
	}

	/**
	 * DEF耐久の値を取得する。
	 * @param blust_type 兵装の種類
	 * @return DEF耐久の値
	 */
	public double getDefGuard(String blust_type) {
		return getDefGuard();
	}

	/**
	 * 反動吸収の値を取得する。
	 * @param blust_type 兵装の種類。
	 * @return 反動吸収の値。
	 */
	public double getRecoil(String blust_type) {
		return getRecoil();
	}

	/**
	 * リロードの倍率を取得する。
	 * @param blust_type 兵装の種類
	 * @return リロードの倍率。
	 */
	public double getReload(String blust_type) {
		double ret = getReload();

		/*
		if(existChip("遊撃兵装強化") && blust_type.equals("遊撃兵装")) {
			ret = ret - 0.02;
		}
		else if(existChip("遊撃兵装強化II") && blust_type.equals("遊撃兵装")) {  // 暫定対応。値は不明。
			ret = ret - 0.04;
		}
		*/
		
		return ret;
	}

	/**
	 * 武器変更の値を取得する。
	 * @param blust_type 兵装の種類。
	 * @return 武器変更の値。
	 */
	public double getChangeWeapon(String blust_type) {
		return getChangeWeapon();
	}
	
	/**
	 * 予備弾数の値を取得する。
	 * @param blust_type 兵装の種類
	 * @return 予備弾装の値
	 */
	public double getSpareBullet(String blust_type) {
		return getSpareBullet();
	}

	/**
	 * 歩行速度を取得する。
	 * @param blust_type 兵装の種類
	 * @return 設定した単位に応じた歩行速度を取得する。
	 */
	public double getWalk(String blust_type) {
		return calcSpeed(blust_type, getWalk(), SpecValues.MIN_WALK);
	}

	/**
	 * 巡航速度を取得する。
	 * @param blust_type 兵装の種類。
	 * @return 巡航速度。
	 */
	public double getNormalDush(String blust_type) {
		return getDashBlust(blust_type, false);
	}

	/**
	 * 初速を取得する。
	 * @param blust_type 兵装の種類。
	 * @return 初速
	 */
	public double getStartDush(String blust_type) {	
		return getDashBlust(blust_type, true);
	}

	/**
	 * 走行速度(初速or巡航)を取得する (km/h)
	 * @param blust_type 兵装の種類
	 * @param is_start 初速かどうか。初速の場合はtrueを設定し、巡航の場合はfalseを設定する。
	 * @return 歩行速度を返す。
	 */
	private double getDashBlust(String blust_type, boolean is_start) {
		double ret = calcDash(is_start);

		/*
		// 兵装強化チップの効果を反映する
		if(existChip("強襲兵装強化") && blust_type.equals("強襲兵装")) {
			ret = ret + calcHover(1.08, is_start);
		}
		else if(existChip("強襲兵装強化II") && blust_type.equals("強襲兵装")) {  // 暫定対応。値は不明。
			ret = ret + calcHover(2.16, is_start);
		}
		*/

		// 超過を考慮した速度計算を行う
		ret = calcSpeed(blust_type, ret, SpecValues.MIN_DASH);

		// 巡航補正計算を行う。
		ret = calcNormalDush(ret, is_start);
		
		// 水中移動の速度低下を反映する
		ret = applySpeedDown(ret);

		return ret;
	}
	
	/**
	 * ダッシュ速度(初速)を取得する。
	 * @return
	 */
	public double calcDash() {
		double ret = calcDash(true);
		return applySpeedDown(ret);
	}

	/**
	 * ダッシュ速度を取得する。
	 * @param is_start 初速かどうか。
	 * @return ダッシュ速度。
	 */
	public double calcDash(boolean is_start) {
		double ret = 0;

		BBData legs_parts = mRecentParts[LEGS_IDX];
		String spec = legs_parts.get("ダッシュ");
		ret = SpecValues.getSpecValue(spec, "ダッシュ", "");

		// チップセットボーナス
		ret = ret + (0.36 * countChip("ダッシュ"));
		ret = ret + (0.54 * countChip("ダッシュII"));

		// ホバー補正計算を行う。
		ret = calcHover(ret, is_start);

		if(ret == SpecValues.ERROR_VALUE) {
			ret = 0;
		}

		return ret;
	}
	
	/**
	 * ホバー時の速度変換を行う。
	 * @param speed 対象の速度
	 * @param is_start 初速かどうか
	 * @return 変換後の速度
	 */
	private double calcHover(double speed, boolean is_start) {
		double ret = speed;

		if(isHoverLegs()) {
			if(is_start) {
				ret = ret * 0.8;
			}
			else {
				ret = ret * 7.0 / 6.0;
			}
		}
		
		return ret;
	}

	/**
	 * 初速or巡航速度を算出する。
	 * @param base_speed 基本の速度。
	 * @param is_start 初速かどうか。
	 * @return 算出した速度。
	 */
	private double calcNormalDush(double base_speed, boolean is_start) {
		double ret = base_speed;

		if(!is_start) {
			ret = ret * 0.6;
		}
		
		return ret;
	}

	/**
	 * 速度の低下率から実際の速度を計算する。
	 * @param blust_type 兵装の文字列。
	 * @param base_speed 基本の速度。
	 * @param min_speed 速度の下限値。
	 * @return 算出した速度。
	 */
	private double calcSpeed(String blust_type, double base_speed, double min_speed) {
		double ret = base_speed;
	
		try {			
			double down_rate = getSpeedDownRate(blust_type);
			ret = ret + ( ret * down_rate / 100.0 );
			
			// 超過により下限値を下回った場合、下限値を設定する(クーガーI型の半分)
			if(ret < min_speed) {
				ret = min_speed;
			}
		
		} catch (Exception e) {
			// e.printStackTrace();
		}
		
		return ret;
	}

	/**
	 * パーツのみの場合の速度低下率を取得する
	 * @return 速度低下率
	 */
	public double getSpeedDownRate() {
		double ret = 0;
		double space = getSpacePartsWeight();
		
		if(space >= 0) {
			ret = 0;
		}
		else {
			ret = space * 0.025;
		}

		// 重量超過耐性チップの効果を反映
		double chip_rate = getValueResistOverWeightChip();
		return ret * ((100.0 - chip_rate) / 100.0);
	}
	
	/**
	 * 速度低下率を取得する
	 * @param blust_type 取得する兵装名
	 * @return 速度低下率
	 */
	public double getSpeedDownRate(String blust_type) {
		double ret = 0;
		double space = getSpaceWeight(blust_type);
		
		if(space >= 0) {
			ret = 0;
		}
		else {
			ret = space * 0.025;
		}

		// 重量超過耐性チップの効果を反映
		double chip_rate = getValueResistOverWeightChip();
		return ret * ((100.0 - chip_rate) / 100.0);
	}
	
	/**
	 * 重量超過耐性チップの効果を取得する。
	 * @return チップ効果(%)
	 */
	private double getValueResistOverWeightChip() {
		double ret = 0.0;

		/*
		// チップの効果を反映
		if(existChip("重量超過耐性")) {
			ret = 20.0;
		}
		else if(existChip("重量超過耐性II")) {
			ret = 50.0;
		}
		else if(existChip("重量超過耐性III")) {
			ret = 70.0;
		}
		*/
		
		return ret;
	}
	
	/**
	 * 水中移動時の速度低下倍率を算出する。
	 * @param base_speed 基本となる速度
	 * @return 計算結果の速度
	 */
	public double applySpeedDown(double base_speed) {
		double ret = base_speed;
		double chip_bonus = 0;

		/*
		// チップの効果を反映
		if(existChip("水中移動適性")) {
			chip_bonus = 0.25;
		}
		else if(existChip("水中移動適性II")) {
			chip_bonus = 0.5;
		}
		*/
		
		// 各種状態による速度低下倍率を反映する
		if(mMode == MODE_MOVE_HIWATER) {
			ret = ret * (1.0 - 0.25 * (1 - chip_bonus));
		}
		else if(mMode == MODE_MOVE_LOWATER) {
			ret = ret * (1.0 - 0.50 * (1 - chip_bonus));
		}
		else if(mMode == MODE_MOVE_UPWATER) {
			ret = ret * (1.0 - 0.30 * (1 - chip_bonus));
		}
		
		return ret;
	}

	/**
	 * 重量耐性の値を取得する。
	 * @param blust_type 兵装の種類
	 * @return 重量耐性の値。
	 */
	public int getAntiWeight(String blust_type) {
		int ret = getAntiWeight();

		/*
		if(existChip("重火力兵装強化") && blust_type.equals("重火力兵装")) {
			ret = ret + 100;
		}
		else if(existChip("重火力兵装強化II") && blust_type.equals("重火力兵装")) {  // 暫定対応。値は不明。
			ret = ret + 200;
		}
		*/
		
		return ret;
	}
	
	/**
	 * 加速の値を取得する。
	 * @param blust_type 兵装の種類
	 * @return 加速の値。
	 */
	public double getAcceleration(String blust_type) {
		return getAcceleration();
	}

	//----------------------------------------------------------
	// 性能取得系(武器関連)
	//----------------------------------------------------------

	/**
	 * 予備弾数を反映した総弾数を計算する。
	 */
	public double getBulletSum(BBData data) {
		return Math.floor(data.getBulletSum() * (1.0 + (getSpareBullet() / 100.0)));
	}

	/**
	 * 1射の威力を算出する。(ニュード威力上昇の効果を反映する)
	 * @param data 武器データ
	 * @return 威力の値
	 */
	public double getOneShotPower(BBData data) {
		return getOneShotPowerMain(data, data.getChargeMaxCount() - 1, false, false);
	}
	
	/**
	 * 1射の威力を算出する。(ニュード威力上昇の効果を反映する)
	 * @param data 武器データ
	 * @param charge_level 武器のチャージレベル
	 * @return 威力の値
	 */
	private double getOneShotPower(BBData data, int charge_level) {
		return getOneShotPowerMain(data, charge_level, false, false);
	}
	
	/**
	 * CS時の威力を算出する。
	 * プリサイスショットチップによりCS倍率が変化するため、BBDataクラスの関数は参照しないこと。
	 * @param data 武器データ
	 * @return 威力の値
	 */
	public double getCsShotPower(BBData data) {
		return getOneShotPowerMain(data, data.getChargeMaxCount() - 1, true, false);
	}

	/**
	 * 転倒関連の威力値を取得する。
	 * @param is_critical クリティカルかどうか。
	 * @return 転倒関連の威力。
	 */
	public double getShotAntiStability(BBData data, boolean is_critical) {
		return getOneShotPowerMain(data, 0, is_critical, true);
	}

	/**
	 * 施設攻撃時の威力を算出する。
	 * @param data 武器データ
	 * @return 威力の値
	 */
	public double getObjectShotPower(BBData data) {
		return getOneShotPowerMain(data, 0, false, false);
	}
	
	/**
	 * 命中距離を考慮した爆発武器の威力を算出する。
	 * @param data 武器データ
	 * @param charge_level チャージレベル
	 * @param is_stn 転倒ダメージ値かどうか。
	 * @param distance 命中距離
	 * @param range 爆発範囲。
	 * @return 爆発武器の威力
	 */
	public double getExplosionPower(BBData data, int charge_level, boolean is_stn, double distance, double range) {
		double power = getOneShotPowerMain(data, charge_level, false, is_stn);
		return power * (1.0 - (distance / range));
	}

	/**
	 * 単発威力を取得する。
	 * @param data 武器のデータ
	 * @param charge_level チャージレベル
	 * @param is_critical クリティカルかどうか。
	 * @param is_stn 転倒ダメージ値かどうか。
	 * @return 単発火力
	 */
	public double getOneShotPowerMain(BBData data, int charge_level, boolean is_critical, boolean is_stn) {
		double power = 0;
		
		// 武器の単発火力(または転倒ダメージ値)を取得する。
		if(is_stn) {
			power = data.getShotAntiStability(charge_level);
		}
		else {
			power = data.getOneShotPower(charge_level);
		}
		
		// ニュード威力上昇チップの効果を反映する
		power = power * getNewdChipBonus(data.getNewdAbsPer());

		// CSによる補正を行う
		if(is_critical) {
			double cs_rate = SpecValues.CS_SHOT_RATE;

			cs_rate = cs_rate + (0.1 * countChip("プリサイスショット"));
			cs_rate = cs_rate + (0.125 * countChip("プリサイスショットII"));
			
			power = power * cs_rate;
		}
		
		// 転倒ダメージ値の場合はアンチスタビリティチップの効果を反映する
		if(is_stn) {
			double rate = 0;

			/*
			// チップの補正値を取得
			if(existChip("アンチスタビリティ")) {
				rate = 0.03;
			}
			else if(existChip("アンチスタビリティII")) {
				rate = 0.07;
			}
			else if(existChip("アンチスタビリティIII")) {
				rate = 0.10;
			}
			*/
			
			power = power * (1.0 + rate);
		}
		
		// 対N-DEFまたは対施設による補正を行う
		if(mMode == MODE_ATTACK_DEF) {
			double rate = 0;
			double base_power = power;

			power =  base_power * (1.2 * data.getBulletAbsPer() / 100.0);
			power += base_power * (0.8 * data.getExplosionAbsPer() / 100.0);
			power += base_power * (1.2 * data.getNewdAbsPer() / 100.0);
			power += base_power * (1.0 * data.getSlashAbsPer() / 100.0);

			/*
			if(existChip("対DEF破壊適性")) {
				rate = 0.20;
			}
			else if(existChip("対DEF破壊適性II")) {
				rate = 0.40;
			}
			else if(existChip("対DEF破壊適性III")) {
				rate = 0.60;   // IとIIからの暫定値
			}
			*/
			
			power = power * (1 + rate);
		}
		else if(mMode == MODE_ATTACK_OBJ) {
			double rate = 0;
			double base_power = power;
			
			power =  base_power * (0.9 * data.getBulletAbsPer() / 100.0);
			power += base_power * (1.0 * data.getExplosionAbsPer() / 100.0);
			power += base_power * (1.2 * data.getNewdAbsPer() / 100.0);
			power += base_power * (1.0 * data.getSlashAbsPer() / 100.0);

			/*
			if(existChip("対物破壊適性")) {
				rate = 0.07;
			}
			else if(existChip("対物破壊適性II")) {
				rate = 0.12;
			}
			else if(existChip("対物破壊適性III")) {
				rate = 0.15;
			}
			*/
			
			power = power * (1 + rate);
		}
		
		return power;
	}
	
	/**
	 * マガジン火力を取得する。(チップの効果を反映する)
	 * @param data 指定の武器
	 * @return マガジン火力
	 */
	public double getMagazinePower(BBData data) {
		double ret = 0;

		if(data.existCategory("フレアグレネード系統")) {
			ret = getMagazinePowerFlare(data);
		}
		else {
			ret = getMagazinePowerDefault(data);
		}
		
		return ret;
	}
	
	/**
	 * 主武器などの基本的な武器のマガジン火力を取得する。
	 * @param data 武器データ(総弾数を持つ武器限定)
	 * @return マガジン火力
	 */
	private double getMagazinePowerDefault(BBData data) {
		double one_power = getOneShotPower(data, 0);
		int bullet = data.getMagazine();

		return one_power * bullet;
	}
	
	/**
	 * フレアグレネード系統のマガジン火力を取得する。
	 * @param data 武器データ(フレアグレネード系統限定)
	 * @return マガジン火力。1個分の爆発にフルタイムヒットした場合の威力。
	 */
	private double getMagazinePowerFlare(BBData data) {
		double one_power = getOneShotPower(data, 0);
		double time = 0;
		
		try {
			time = Double.valueOf(data.get("効果持続"));
			
		} catch(NumberFormatException e) {
			time = 0;
		}
		
		return one_power * time;
	}

	/**
	 * 連射速度を算出する。(実弾速射の効果を反映する)
	 * @param data 指定の武器
	 * @return 連射速度
	 */
	private double getShotSpeed(BBData data) {
		double shot_chip_bonus = 1.0;

		/*
		// チップの補正値を取得
		if(existChip("実弾速射")) {
			shot_chip_bonus += 0.03 * (data.getBulletAbsPer() / 100.0);
		}
		else if(existChip("実弾速射II")) {
			shot_chip_bonus += 0.08 * (data.getBulletAbsPer() / 100.0);
		}
		else if(existChip("実弾速射III")) {
			shot_chip_bonus += 0.12 * (data.getBulletAbsPer() / 100.0);
		}
		*/

		return data.getShotSpeed() * shot_chip_bonus;
	}
	
	/**
	 * 秒間火力を取得する。(チップの効果を反映する)
	 * @param data 指定の武器
	 * @return 秒間火力
	 */
	public double get1SecPower(BBData data) {
		double ret = 0;

		if(data.get("名称").equals("ライトニングスマック")) {
			ret = get1SecPowerLightning(data);
		}
		else {
			ret = get1SecPowerDefault(data);
		}
		
		return ret;
	}
	
	/**
	 * 主武器などの基本的な秒間火力を取得する。
	 * 連射速度が設定されていない場合、単発の威力を返す。(チャージ武器の場合はフルチャージ時の単発火力)
	 * 1秒間に1マガジンを撃ち切る場合、マガジン火力を返す。
	 * 
	 * @param data 武器データ(連射速度をデータとして持つ武器限定)
	 * @return 秒間火力
	 */
	private double get1SecPowerDefault(BBData data) {
		double ret = 0;
		double shot_speed = getShotSpeed(data);

		if(shot_speed > 0) {
			double one_power = getOneShotPower(data, 0);
			double magazine_power = getMagazinePower(data);
			
			ret = one_power * shot_speed / 60;
			
			// 1秒間以内に1マガジンを撃ち切る場合、マガジン火力に変更する。
			if(ret > magazine_power) {
				ret = magazine_power;
			}
		}
		else {
			ret = getOneShotPower(data);
		}
		
		return ret;
	}

	/**
	 * ライトニングスマックの秒間火力を取得する。
	 * ポンプアクション込みの計算を行う。
	 * 連射速度から1射に要する時間を求め、それにポンプアクションの時間(0.8s)を加算し、
	 * 1秒から算出結果を除算することで実連射速度を求める。
	 * 
	 * @param data ライトニングスマック
	 * @return 秒間火力
	 */
	private double get1SecPowerLightning(BBData data) {
		double shot_speed = getShotSpeed(data);
		double one_power = getOneShotPower(data, 0);
		
		return (3 * one_power) * (1.0 / ((3.0 * (60.0 / shot_speed)) + 0.8));
	}

	/**
	 * リロード時間を計算する。(クイックリロードチップの効果は反映しない)
	 * @param data リロード時間を計算する武器
	 * @return リロード時間。単位は秒。
	 */
	public double getReloadTime(BBData data) {
		return getReloadTime(data, false);
	}

	/**
	 * リロード時間を計算する。
	 * @param data リロード時間を計算する武器
	 * @param is_quickreload クイックリロードチップの効果を反映するかどうか。
	 * @return リロード時間。単位は秒。
	 */
	public double getReloadTime(BBData data, boolean is_quickreload) {
		double ret = 0;
		double reload_time_value = 0;
		double reload_spec_value = 0;

		// 武器のリロード時間を読み込む
		reload_time_value = data.getReloadTime();
		
		// リロード倍率の読み込み
		reload_spec_value = getReload();

		// リロード時間の計算
		ret = reload_time_value * (1.0 - reload_spec_value / 100.0);

		/*
		// チップの補正値を取得
		if(is_quickreload) {
			if(existChip("クイックリロード")) {
				ret = ret * (5.0 / 6.0);
			}
			else if(existChip("クイックリロードII")) {
				ret = ret * (5.0 / 8.0);
			}
		}
		*/
		
		return ret;
	}

	/**
	 * OH復帰時間を算出する。(高速冷却の効果を反映する)
	 * @param data 指定の武器
	 * @return OH復帰時間
	 */
	public double getOverheatRepairTime(BBData data) {
		return getOverheatRepairTime(data, true);
	}

	/**
	 * OH復帰時間を算出する。(高速冷却の効果を反映する)
	 * @param data 指定の武器
	 * @param is_overheat OH状態
	 * @return OH復帰時間
	 */
	public double getOverheatRepairTime(BBData data, boolean is_overheat) {
		double ret = 0;
		double chip_bonus = 1.0;

		/*
		// チップの補正値を取得
		if(existChip("高速冷却")) {
			chip_bonus = 0.8;
		}
		else if(existChip("高速冷却II")) {
			chip_bonus = 0.5;
		}
		*/
		
		ret = data.getOverheatRepairTime(is_overheat) * chip_bonus;
		
		return ret;
	}
	
	/**
	 * 戦術火力を取得する。クイックリロード以外のチップの効果を反映する。
	 * @param data 武器データ
	 * @return 戦術火力
	 */
	public double getBattlePower(BBData data) {
		return getBattlePower(data, false);
	}
	
	/**
	 * 戦術火力を取得する。チップの効果を反映する。
	 * @param data 武器データ
	 * @param is_quickreload クイックリロードチップの効果を反映するかどうか。
	 * @return 戦術火力
	 */
	public double getBattlePower(BBData data,  boolean is_quickreload) {
		double ret = 0;

		if(BBViewSetting.IS_BATTLE_POWER_OH && data.existKey("OH復帰時間")) {
			ret = getBattlePowerOverHeat(data, false);  // OH前の戦術火力を取得する
		}
		else if(data.get("名称").equals("ライトニングスマック")) {
			ret = getBattlePowerLightning(data, false);
		}
		else {
			ret = getBattlePowerDefault(data, is_quickreload);
		}
		
		return ret;
	}
	
	/**
	 * リロード基準の戦術火力を取得する。
	 * 
	 * 戦術火力＝マガジン火力÷（撃ち切り時間＋リロード時間）
	 * なお、連射速度の情報がない場合は単発火力とする。
	 * 
	 * @param data 武器データ(リロードと連射速度をデータとして持つ武器限定)
	 * @param is_quickreload クイックリロードチップの効果を反映するかどうか。
	 * @return 戦術火力
	 */
	public double getBattlePowerDefault(BBData data, boolean is_quickreload) {
		double ret = 0;
		double shot_speed = getShotSpeed(data);
		
		if(shot_speed > 0) {
			double magazine_power = getMagazinePower(data);
			double reload_time = getReloadTime(data, is_quickreload);
			int magazine = data.getMagazine();
			
			double all_shot_time = magazine / shot_speed * 60;
			ret = magazine_power / (all_shot_time + reload_time);
		}
		else {
			ret = getOneShotPower(data);
		}
		
		return ret;
	}

	/**
	 * ライトニングスマックの戦術火力を取得する。
	 * @param data ライトニングスマック
	 * @param is_quickreload クイックリロードチップの効果を反映するかどうか。
	 * @return 戦術火力
	 */
	public double getBattlePowerLightning(BBData data, boolean is_quickreload) {
		double ret = 0;
		double shot_speed = getShotSpeed(data);
		double magazine_power = getMagazinePower(data);
		double reload_time = getReloadTime(data, is_quickreload);
		int magazine = data.getMagazine();
		
		// 撃ち切り時間＝弾自体を撃ち切る時間＋ポンプアクション時間×回数
		double all_shot_time = (magazine / shot_speed * 60) + (0.8 * (magazine / 3.0));
		
		ret = magazine_power / (all_shot_time + reload_time);
		
		return ret;
	}
	
	/**
	 * OH火力を取得する。
	 * @param data 武器データ
	 * @return OH火力
	 */
	public double getOverHeatPower(BBData data) {
		double shot_speed = getShotSpeed(data);
		double oneshot_power = getOneShotPower(data);
		double oh_guard_time = data.getOverheatTime();
		
		return oneshot_power * (shot_speed / 60.0) * oh_guard_time;
	}

	/**
	 * OH武器の戦術火力を取得する。(OH中)
	 * @return 戦術火力
	 */
	public double getBattlePowerOverHeat(BBData data) {
		return getBattlePowerOverHeat(data, true);
	}
	
	/**
	 * OH基準の戦術火力を取得する。
	 * 
	 * 戦術火力＝OH火力÷（OH耐性時間＋OH復帰時間）
	 * @param data 武器データ
	 * @param is_overheat OH中かどうか。
	 * @return 戦術火力
	 */
	public double getBattlePowerOverHeat(BBData data, boolean is_overheat) {
		double ret = 0;
		double shot_speed = getShotSpeed(data);
		
		if(shot_speed > 0) {
			double oh_guard_time = data.getOverheatTime();
			double oh_repair_time = getOverheatRepairTime(data, is_overheat);
			double oh_power = getOverHeatPower(data);
	
			ret = oh_power / (oh_guard_time + oh_repair_time);
		}
		else {
			ret = getOneShotPower(data);
		}
		
		return ret;
	}
	
	//-------------------------------------------------------------
	// 以下は現状未使用
	//-------------------------------------------------------------
	
	/**
	 * 継続火力を取得する。(チップの効果を反映する)
	 * @param data 指定の武器
	 * @return 継続火力(10sec)
	 */
	public double get10SecPower(BBData data) {
		double ret = 0;
		double one_power = getOneShotPower(data);
		double shot_speed = getShotSpeed(data);
		double reload_time = getReloadTime(data);
		double remain_time = 10;
		int magazine_count = 0;
		int magazine = data.getMagazine();
		
		if(shot_speed > 0) {
			
			// 何マガジン撃てるかを算出
			double all_shot_time = magazine / shot_speed * 60;
			double one_cycle_time = all_shot_time + reload_time;
			while(remain_time > one_cycle_time) {
				remain_time = remain_time - one_cycle_time;
				magazine_count = magazine_count + 1;
			}
			
			// 威力×(打ち切った弾数＋残った秒数で撃てる弾数)
			ret = one_power * ((magazine * magazine_count) + (magazine * shot_speed / 60.0));
		}
		
		return ret;
	}

	//-------------------------------------------------------------
	// ここまで
	//-------------------------------------------------------------
	
	/**
	 * 近接武器の威力を算出する。
	 * 近接武器強化チップとニュード威力上昇チップの効果を反映する。
	 * @param data 武器データ
	 * @param is_dash 通常攻撃の時はfalseを設定し、特殊攻撃の時はtrueを設定する。
	 * @return 威力の値
	 */
	public double getSlashPower(BBData data, boolean is_dash) {
		double power = data.getSlashDamage(is_dash);
		double slash_rate = getSlashChipBonus(data.getSlashAbsPer());
		double newd_rate = getNewdChipBonus(data.getNewdAbsPer());

		return power * slash_rate * newd_rate;
	}

	/**
	 * ニュード威力上昇チップの効果による上昇倍率を取得する。
	 * @param newd_percent 武器のニュード属性倍率
	 * @return 上昇率の値
	 */
	private double getNewdChipBonus(double newd_percent) {
		double newd_chip_bonus = 0;

		/*
		// チップの補正値を取得
		if(existChip("ニュード威力上昇")) {
			newd_chip_bonus = 0.025;
		}
		else if(existChip("ニュード威力上昇II")) {
			newd_chip_bonus = 0.07;
		}
		else if(existChip("ニュード威力上昇III")) {
			newd_chip_bonus = 0.09;
		}
		*/
		
		return 1.0 + (newd_chip_bonus * (newd_percent / 100.0));
	}

	/**
	 * 近接攻撃強化チップの効果による上昇倍率を取得する。
	 * @param slash_percent 武器の近接属性倍率
	 * @return 上昇率の値
	 */
	private double getSlashChipBonus(double slash_percent) {
		double slash_chip_bonus = 0;

		/*
		// チップの補正値を取得
		if(existChip("近接攻撃強化")) {
			slash_chip_bonus = 0.00005;
		}
		else if(existChip("近接攻撃強化II")) {
			slash_chip_bonus = 0.00012;
		}
		else if(existChip("近接攻撃強化III")) {
			slash_chip_bonus = 0.00020;
		}
		*/
		
		return 1.0 + ((getPartsWeight() - 2000) * slash_chip_bonus) * (slash_percent / 100.0);
	}

	/**
	 * 爆発範囲を取得する。爆発範囲拡大チップの効果を反映する。
	 * @param data 対象の武器
	 * @return 爆発範囲
	 */
	public double getExplosionRange(BBData data) {
		double range = data.getExplosionRange();
		double chip_bonus = 0;

		/*
		// チップの補正値を取得
		if(existChip("爆発範囲拡大")) {
			chip_bonus = 1;
		}
		else if(existChip("爆発範囲拡大II")) {
			chip_bonus = 2;
		}
		else if(existChip("爆発範囲拡大III")) {
			chip_bonus = 3;
		}
		*/
		
		return range + (chip_bonus * ((double)data.getExplosionAbsPer() / 100.0));
	}
	
	/**
	 * 索敵装備の索敵時間を取得する。索敵継続延長チップの効果を反映する。
	 * @param data 対象の武器
	 * @return 索敵時間
	 */
	public int getSearchTime(BBData data) {
		int ret = data.getSearchTime();
		int chip_bonus = 0;

		/*
		// チップの補正値を取得
		if(existChip("索敵継続延長")) {
			chip_bonus = 2;
		}
		else if(existChip("索敵継続延長II")) {
			chip_bonus = 5;
		}
		*/
		
		return ret + chip_bonus;
	}

	/**
	 * 特別装備のチャージ時間を算出する。(非SP枯渇時)
	 * @param data 対象の特別装備の武器。
	 * @return SP供給を反映したチャージ時間。チャージ時間の値が無い場合は0を返す。
	 */
	public double getSpChargeTime(BBData data) {
		return getSpChargeTime(data, false);
	}
	
	/**
	 * 特別装備のチャージ時間を算出する。
	 * @param data 対象の特別装備の武器。
	 * @param is_overheat SPが枯渇しているかどうか。枯渇している場合は時間が1.2倍となる。
	 * @return SP供給を反映したチャージ時間。チャージ時間の値が無い場合は0を返す。
	 */
	public double getSpChargeTime(BBData data, boolean is_overheat) {
		double ret = data.getSpChargeTime() / (1.00 + (getSP() / 100.0));
		
		if(is_overheat) {
			ret = ret * 1.2;
		}
		
		return ret;
	}

	/**
	 * 特別装備のチャージ時間を算出する。(非SP枯渇時)
	 * @param blust_type 兵装名
	 * @param data 対象の特別装備の武器。
	 * @return SP供給を反映したチャージ時間。チャージ時間の値が無い場合は0を返す。
	 */
	public double getSpChargeTime(String blust_type, BBData data) {
		return getSpChargeTime(blust_type, data, false);
	}
	
	/**
	 * 特別装備のチャージ時間を算出する。兵装強化チップの効果も反映する。
	 * @param blust_type 兵装名
	 * @param data 対象の特別装備の武器。
	 * @param is_overheat SPが枯渇しているかどうか。枯渇している場合は時間が1.2倍となる。
	 * @return SP供給を反映したチャージ時間。チャージ時間の値が無い場合は0を返す。
	 */
	public double getSpChargeTime(String blust_type, BBData data, boolean is_overheat) {
		double ret = data.getSpChargeTime() / (1.00 + (getSP(blust_type) / 100.0));
		
		if(is_overheat) {
			ret = ret * 1.2;
		}
		
		return ret;
	}
	
	/**
	 * AC使用時の速度を算出する。
	 * @param data 使用するAC
	 * @return AC使用時の速度。AC以外を引数とした場合は0を返す。
	 */
	public double getACSpeed(BBData data) {
		double ret = 0;
		
		try {
			double ac_power = Double.valueOf(data.get("出力"));
			ret = (ac_power * getDashBlust("強襲兵装", false)) + 16.2;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * AC使用時の戦術速度を算出する。
	 * 強襲兵装限定のため、強襲兵装強化チップのダッシュ上昇効果も反映する。
	 * @param data 使用するAC
	 * @return AC使用時の戦術速度。AC以外を引数とした倍は0を返す。
	 * AC使用時の戦術速度＝（（AC使用時速度×使用時間）＋（初速×チャージ時間））÷（使用時間＋チャージ時間）
	 */
	public double getBattleACSpeed(BBData data) {
		double ret = 0;
		
		try {
			double ac_on_time = Double.valueOf(data.get("連続使用"));
			double ac_off_time = getSpChargeTime(data);
			ret = ((getACSpeed(data) * ac_on_time) + (getDashBlust("強襲兵装", true) * ac_off_time)) / (ac_on_time + ac_off_time);

		} catch(Exception e) {
			ret = 0;
		}
		
		return ret;
	}
	
	/**
	 * バリア装備の秒間耐久回復量を算出する。
	 * 重火力兵装限定で支援兵装強化チップのSP供給の上昇を考慮しなくてよいため、兵装名は不要。
	 * 基本的なチップの効果を受けるSP供給で除算するため、BBDataクラスの関数は参照できない。
	 * @param data 対象のバリア装備
	 * @return 秒間耐久回復量
	 */
	public double getBattleBarrierGuard(BBData data) {
		int guard = 0;
		
		try {
			guard = Integer.valueOf(data.get("耐久力"));
			
		} catch(Exception e) {
			guard = 0;
		}
		
		return guard / getSpChargeTime(data);
	}
	
	/**
	 * 最大回復量を算出する。
	 * @param data 使用する支援の特別装備
	 * @return
	 */
	public double getMaxRepair(BBData data) {
		return data.getMaxRepair();
	}
	
	/**
	 * チャージ武器の充填時間を算出する。
	 * 高速充填チップの効果(充填速度の上昇率)を反映する。
	 * エックス時点    ：I=1.5倍、II=2.0倍
	 * エックスゼロ時点：I=1.4倍、II=1.7倍
	 * @param data 対象の武器
	 * @return 充填時間
	 */
	public double getChargeTime(BBData data) {
		double ret = data.getChargeTime();

		/*
		// チップの補正値を取得
		if(existChip("高速充填")) {
			ret = ret / 1.4;
		}
		else if(existChip("高速充填II")) {
			ret = ret / 1.7;
		}
		*/
		
		return ret;
	}
	
	//----------------------------------------------------------
	// 性能取得系(耐性関連)
	//----------------------------------------------------------

	/**
	 * 射撃武器に被弾した際のダメージを計算する。
	 * @param data 武器データ
	 * @param parts_type パーツの種類
	 * @return ダメージ値
	 */
	public double getShotDamage(BBData data, String parts_type, int charge_level) {
		double damage;
		double armor = getArmor(parts_type);
		double attack_value = data.getOneShotPower(charge_level);
		
		damage = getBulletDamage(data, attack_value, armor)
		    + getExplosionDamage(data, attack_value, armor)
			+ getNewdDamage(data, attack_value, armor)
			+ getSlashDamage(data, attack_value, armor);

		// 頭部パーツの場合はダメージを2.5倍する。
		if(parts_type.equals(BBDataManager.BLUST_PARTS_HEAD)) {
			damage = damage * 2.5;
		}
		
		return damage;
	}
	
	/**
	 * 射撃武器に被弾した際のダメージを計算する。威力、装甲値は引数の値を使用する。
	 * @param data 武器データ
	 * @param attack_value 威力値
	 * @param armor 装甲値
	 * @return ダメージ値
	 */
	public double getShotDamage(BBData data, double attack_value, double armor) {
		double damage = getBulletDamage(data, attack_value, armor)
			    + getExplosionDamage(data, attack_value, armor)
				+ getNewdDamage(data, attack_value, armor)
				+ getSlashDamage(data, attack_value, armor);
		
		return damage;
	}

	/**
	 * 爆発武器に被弾した際のダメージを計算する。(空爆時)
	 * @param data 武器データ
	 * @return ダメージ値
	 */
	public double getExplosionHeadDamage(BBData data, int charge_level) {
		double damage;
		double armor = getArmorAveHead();
		double attack_value = data.getOneShotPower(charge_level);

		damage = getBulletDamage(data, attack_value, armor)
				+ getExplosionDamage(data, attack_value, armor)
				+ getNewdDamage(data, attack_value, armor)
				+ getSlashDamage(data, attack_value, armor);

		return damage;
	}

	/**
	 * 爆発武器に被弾した際のダメージを計算する。(地爆時)
	 * @param data 武器データ
	 * @return ダメージ値
	 */
	public double getExplosionLegsDamage(BBData data, int charge_level) {
		double damage;
		double armor = getArmorAveLegs();
		double attack_value = data.getOneShotPower(charge_level);

		damage = getBulletDamage(data, attack_value, armor)
				+ getExplosionDamage(data, attack_value, armor)
				+ getNewdDamage(data, attack_value, armor)
				+ getSlashDamage(data, attack_value, armor);

		return damage;
	}

	/**
	 * 爆発武器に被弾した際のダメージを計算する。
	 * @param data 武器データ
	 * @return ダメージ値
	 */
	public double getExplosionDamage(BBData data, int charge_level) {
		double damage;
		double armor = getArmorAve();
		double attack_value = data.getOneShotPower(charge_level);
		
		damage = getBulletDamage(data, attack_value, armor)
		    + getExplosionDamage(data, attack_value, armor)
			+ getNewdDamage(data, attack_value, armor)
			+ getSlashDamage(data, attack_value, armor);

		return damage;
	}

	/**
	 * 近接武器のダメージを取得する。
	 * @param data 近接武器データ
	 * @param is_dash ダッシュの場合はtrueを設定し、そうでない場合はfalseを設定する。
	 * @return 近接武器のダメージ
	 */
	public double getSlashDamage(BBData data, boolean is_dash) {
		return getSlashDamage(data, is_dash, 0);
	}
	
	/**
	 * 近接武器のダメージを取得する。
	 * @param data 近接武器データ
	 * @param is_dash ダッシュの場合はtrueを設定し、そうでない場合はfalseを設定する。
	 * @return 近接武器のダメージ
	 */
	public double getSlashDamage(BBData data, boolean is_dash, int charge_level) {
		double armor = getArmorAve();
		
		double attack_value = data.getSlashDamage(is_dash, charge_level);
		
		double damage = getBulletDamage(data, attack_value, armor)
			+ getExplosionDamage(data, attack_value, armor)
			+ getNewdDamage(data, attack_value, armor)
			+ getSlashDamage(data, attack_value, armor);
		
		return damage;
	}
	
	/**
	 * 実弾属性のダメージ値を取得する。
	 * @param data 武器データ
	 * @param attack_value 威力値
	 * @param armor 装甲値
	 * @return ダメージ値
	 */
	private double getBulletDamage(BBData data, double attack_value, double armor) {
		double chip_bonus = 1.0;

		chip_bonus = chip_bonus - (0.04 * countChip("対実弾防御"));
		chip_bonus = chip_bonus - (0.06 * countChip("対実弾防御II"));

		return calcDamage(attack_value, chip_bonus, data.getBulletAbsPer(), armor);
	}
	
	/**
	 * 爆発属性のダメージ値を取得する。
	 * @param data 武器データ
	 * @param attack_value 威力値
	 * @param armor 装甲値
	 * @return ダメージ値
	 */
	private double getExplosionDamage(BBData data, double attack_value, double armor) {
		double chip_bonus = 1.0;

		chip_bonus = chip_bonus - (0.05 * countChip("対爆発防御"));

		return calcDamage(attack_value, chip_bonus, data.getExplosionAbsPer(), armor);
	}

	/**
	 * ニュード属性のダメージ値を取得する。
	 * @param data 武器データ
	 * @param attack_value 威力値
	 * @param armor 装甲値
	 * @return ダメージ値
	 */
	private double getNewdDamage(BBData data, double attack_value, double armor) {
		double chip_bonus = 1.0;

		chip_bonus = chip_bonus - (0.04 * countChip("対ニュード防御"));
		chip_bonus = chip_bonus - (0.06 * countChip("対ニュード防御II"));

		return calcDamage(attack_value, chip_bonus, data.getNewdAbsPer(), armor);
	}

	/**
	 * 近接属性のダメージ値を取得する。
	 * @param data 武器データ
	 * @param attack_value 威力値
	 * @param armor 装甲値
	 * @return ダメージ値
	 */
	private double getSlashDamage(BBData data, double attack_value, double armor) {
		double chip_bonus = 1.0;

		chip_bonus = chip_bonus - (0.05 * countChip("対近接防御"));

		return calcDamage(attack_value, chip_bonus, data.getSlashAbsPer(), armor);
	}

	/**
	 * 属性の比率に応じたダメージ値を算出する。
	 * @param attack_value 基本となる威力
	 * @param chip_bonus チップの効果値
	 * @param abs_per 属性割合
	 * @param armor 装甲値
	 * @return ダメージ値
	 */
	private double calcDamage(double attack_value, double chip_bonus, double abs_per, double armor) {
		double attack_damege = (attack_value * chip_bonus) * (abs_per / 100);
		
		return ((100.0 - armor) / 100.0) * attack_damege;
	}
	
	/**
	 * 被弾割合から計算したダメージ量を取得する。
	 * @param data 武器データ
	 * @param attack_value 威力値
	 * @param armor 装甲値。頭部、胴部、腕部、脚部の順で設定する。
	 * @param hit_per 被弾割合。頭部、胴部、腕部、脚部の順で設定する。
	 * @param is_shot 射撃武器かどうか。射撃武器の場合はCS計算も行う。
	 * @return ダメージ値
	 */
	public double getHitDamage(BBData data, double attack_value, double[] armor, int[] hit_per, boolean is_shot) {
		double ret = 0;
		int size = armor.length;
			
		for(int i=0; i<size; i++) {
			double damage = getBulletDamage(data, attack_value, armor[i]) 
				           + getExplosionDamage(data, attack_value, armor[i])
			               + getNewdDamage(data, attack_value, armor[i])
			               + getSlashDamage(data, attack_value, armor[i]);
			
			if(is_shot && i == HEAD_IDX) {
				damage = damage * 2.5;
			}
			
			ret += damage * ((double)hit_per[i] / 100.0);
		}
		
		return ret;
	}

	/**
	 * 装甲平均値から算出する実耐久値を算出する。(近接攻撃時)
	 * @param ndef_on N-DEFが残っている場合の実耐久値。対近接時は効果を発揮しない。
	 * @return 耐久値
	 */
	public double getLife(boolean ndef_on) {
		double damege_rate = (100 - getArmorAve()) / 100.0;
		return SpecValues.BLUST_LIFE_MAX / damege_rate;
	}

	/**
	 * 装甲平均値から算出する実耐久値を算出する。(空爆時)
	 * @param ndef_on N-DEFが残っている場合の実耐久値。
	 * @return 耐久値
	 */
	public double getLifeHead(boolean ndef_on) {
		double damege_rate = (100 - getArmorAveHead()) / 100.0;
		double life = SpecValues.BLUST_LIFE_MAX / damege_rate;

		if(ndef_on) {
			life = life + (getDefGuard() / 0.8);
		}
		
		return life;
	}

	/**
	 * 装甲平均値から算出する実耐久値を算出する。(地爆時)
	 * @param ndef_on N-DEFが残っている場合の実耐久値。
	 * @return 耐久値
	 */
	public double getLifeLegs(boolean ndef_on) {
		double damege_rate = (100 - getArmorAveLegs()) / 100.0;
		double life = SpecValues.BLUST_LIFE_MAX / damege_rate;

		if(ndef_on) {
			life = life + (getDefGuard() / 0.8);
		}
		
		return life;
	}

	/**
	 * 大破するかどうか
	 * @param attack_value 計算対象の威力
	 * @return 大破するときはtrueを返し、大破しないときはfalseを返す。
	 */
	public boolean isBreak(double attack_value) {
		return isBreak(attack_value, SpecValues.BLUST_LIFE_MAX, 0);
	}
	
	/**
	 * 大破するかどうか
	 * @param attack_value 計算対象の威力
	 * @param life 現在の耐久値
	 * @param fatal_attack_lv フェイタルアタックチップのレベル。セットしないときは0に設定する。
	 * @return 大破するときはtrueを返し、大破しないときはfalseを返す。
	 */
	public boolean isBreak(double attack_value, int life, int fatal_attack_lv) {
		boolean ret = false;
		double attack_value_calc = attack_value;
		double blust_break_damage = life + 5000;
		
		// チップの効果を反映する
		/*
		if(existChip("大破抑制")) {
			attack_value_calc = attack_value_calc - 1500;
		}
		*/
		// フェイタルアタックチップの効果を反映する
		if(fatal_attack_lv == 1) {
			blust_break_damage = blust_break_damage - 1000;
		}
		else if(fatal_attack_lv == 2) {
			blust_break_damage = blust_break_damage - 2000;
		}
		
		// 大破するかどうか判定する
		if(attack_value_calc >= blust_break_damage) {
			ret = true;
		}
		else {
			ret = false;
		}
		
		return ret;
	}
	
	/**
	 * ダウンするかどうか
	 * @param attack_value
	 * @return
	 */
	public boolean isDown(double attack_value) {
		boolean ret = false;
		int resist_chip_value = 0;

		/*
		// 転倒耐性チップの効果を反映する
		if(existChip("転倒耐性")) {
			resist_chip_value = 1000;
		}
		else if(existChip("転倒耐性II")) {
			resist_chip_value = 2000;
		}
		else if(existChip("転倒耐性III")) {
			resist_chip_value = 3000;
		}
		*/
		
		if(isHoverLegs()) {
			if(attack_value >= SpecValues.HOVER_DOWN_DAMAGE + resist_chip_value) {
				ret = true;
			}
			else {
				ret = false;
			}
		}
		else {
			if(attack_value >= SpecValues.BLUST_DOWN_DAMAGE + resist_chip_value) {
				ret = true;
			}
			else {
				ret = false;
			}
		}

		return ret;
	}
	
	/**
	 * よろけが発生するかどうか
	 * @param attack_value
	 * @return
	 */
	public boolean isBack(double attack_value) {
		boolean ret = false;
		int resist_chip_value = 0;

		/*
		// 転倒耐性チップの効果を反映する
		if(existChip("転倒耐性")) {
			resist_chip_value = 500;
		}
		else if(existChip("転倒耐性II")) {
			resist_chip_value = 1000;
		}
		else if(existChip("転倒耐性III")) {
			resist_chip_value = 1500;
		}
		*/
		
		if(isHoverLegs()) {
			if(attack_value >= SpecValues.HOVER_KB_DAMAGE + resist_chip_value) {
				ret = true;
			}
			else {
				ret = false;
			}
		}
		else {
			if(attack_value >= SpecValues.BLUST_KB_DAMAGE + resist_chip_value) {
				ret = true;
			}
			else {
				ret = false;
			}
		}

		return ret;
	}

	
	//----------------------------------------------------------
	// 性能取得系(指定キー)
	//----------------------------------------------------------

	/**
	 * 兵装依存の指定データを取得する
	 * @param key キー
	 * @param blust_type 兵装名
	 * @return データ
	 */
	public double getSpecValue(String key, String blust_type) {
		double ret = 0;
	
		if(key.equals("総重量")) {
			ret = getWeight(blust_type);
		}
		else if(key.equals("猶予")) {
			ret = getSpaceWeight(blust_type);
		}
		else if(key.equals("初速")) {
			ret = getStartDush(blust_type);
		}
		else if(key.equals("歩速")) {
			ret = getWalk(blust_type);
		}
		else if(key.equals("低下率")) {
			ret = getSpeedDownRate(blust_type);
		}
		else if(key.equals("装甲平均値")) {
			ret = getArmorAve(blust_type);
		}
		else if(key.equals("射撃補正")) {
			ret = getShotBonus(blust_type);
		}
		else if(key.equals("索敵")) {
			ret = getSearch(blust_type);
		}
		else if(key.equals("ロックオン")) {
			ret = getRockOn(blust_type);
		}
		else if(key.equals("DEF回復")) {
			ret = getDefRecover(blust_type);
		}
		else if(key.equals("ブースター")) {
			ret = getBoost(blust_type);
		}
		else if(key.equals("最大ステップ数")) {
			ret = getStepMaxCount(blust_type);
		}
		else if(key.equals("ブースター回復時間")) {
			ret = getBoostChargeTime(blust_type);
		}
		else if(key.equals("SP供給")) {
			ret = getSP(blust_type);
		}
		else if(key.equals("エリア移動")) {
			ret = getAreaMove(blust_type);
		}
		else if(key.equals("DEF耐久")) {
			ret = getDefGuard(blust_type);
		}
		else if(key.equals("反動吸収")) {
			ret = getRecoil(blust_type);
		}
		else if(key.equals("リロード")) {
			ret = getReload(blust_type);
		}
		else if(key.equals("武器変更")) {
			ret = getChangeWeapon(blust_type);
		}
		else if(key.equals("予備弾数")) {
			ret = getSpareBullet(blust_type);
		}
		else if(key.equals("重量耐性")) {
			ret = getAntiWeight(blust_type);
		}
		else if(key.equals("ダッシュ")) {
			ret = getStartDush(blust_type);
		}
		else if(key.equals("歩行")) {
			ret = getWalk(blust_type);
		}
		else if(key.equals("巡航")) {
			ret = getAcceleration(blust_type);
		}
		else {
			ret = getSpecValue(key);
		}
		
		return ret;
	}
	
	/**
	 * 指定のキーに対応したスペック値を取得する。
	 * @param key キー
	 * @return 性能値
	 */
	public double getSpecValue(String key) {
		double ret = 0;
		
		if(key.equals("装甲平均値")) {
			ret = getArmorAve();
		}
		else if(key.equals("射撃補正")) {
			ret = getShotBonus();
		}
		else if(key.equals("索敵")) {
			ret = getSearch();
		}
		else if(key.equals("ロックオン")) {
			ret = getRockOn();
		}
		else if(key.equals("DEF回復")) {
			ret = getDefRecover();
		}
		else if(key.equals("ブースター")) {
			ret = getBoost();
		}
		else if(key.equals("最大ステップ数")) {
			ret = getStepMaxCount();
		}
		else if(key.equals("ブースター回復時間")) {
			ret = getBoostChargeTime();
		}
		else if(key.equals("SP供給")) {
			ret = getSP();
		}
		else if(key.equals("エリア移動")) {
			ret = getAreaMove();
		}
		else if(key.equals("DEF耐久")) {
			ret = getDefGuard();
		}
		else if(key.equals("反動吸収")) {
			ret = getRecoil();
		}
		else if(key.equals("リロード")) {
			ret = getReload();
		}
		else if(key.equals("武器変更")) {
			ret = getChangeWeapon();
		}
		else if(key.equals("予備弾数")) {
			ret = getSpareBullet();
		}
		else if(key.equals("重量耐性")) {
			ret = getAntiWeight();
		}
		else if(key.equals("ダッシュ")) {
			ret = getDash();
		}
		else if(key.equals("歩行")) {
			ret = getWalk();
		}
		else if(key.equals("巡航")) {
			ret = getAcceleration();
		}
		else if(key.equals("低下率")) {
			ret = getSpeedDownRate();
		}
		
		return ret;
	}
}
