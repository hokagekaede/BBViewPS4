package hokage.kaede.gmail.com.BBViewLib.Java;

import hokage.kaede.gmail.com.StandardLib.Java.KeyValueStoreManager;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 全てのパーツ、武器、チップなどの情報を管理するクラス。
 */
public class BBDataManager extends KeyValueStoreManager<BBData> {
	
	public static final String ROOT_PARTS  = "パーツ";
	public static final String ROOT_WEAPON = "武器";
	public static final String ROOT_BLUST  = "兵装";

	public static final String BLUST_PARTS_HEAD = "頭部パーツ";
	public static final String BLUST_PARTS_BODY = "胴部パーツ";
	public static final String BLUST_PARTS_ARMS = "腕部パーツ";
	public static final String BLUST_PARTS_LEGS = "脚部パーツ";
	
	public static final String BLUST_TYPE_ASSALT  = "強襲兵装";
	public static final String BLUST_TYPE_HEAVY   = "重火力兵装";
	public static final String BLUST_TYPE_SNIPER  = "遊撃兵装";
	public static final String BLUST_TYPE_SUPPORT = "支援兵装";
	
	public static final String WEAPON_TYPE_MAIN    = "主武器";
	public static final String WEAPON_TYPE_SUB     = "副武器";
	public static final String WEAPON_TYPE_SUPPORT = "補助装備";
	public static final String WEAPON_TYPE_SPECIAL = "特別装備";

	public static final String CHIP_STR         = "チップ";	
	public static final String SKILL_CHIP_STR   = "特殊機能チップ";	
	public static final String POWERUP_CHIP_STR = "機体強化チップ";	
	public static final String ACTION_CHIP_STR  = "アクションチップ";
	public static final String ACTION_ACT_CHIP_STR   = "アクションチップ(アクションボタン)";
	public static final String ACTION_DASH_CHIP_STR  = "アクションチップ(ダッシュボタン)";
	public static final String ACTION_JUMP_CHIP_STR  = "アクションチップ(ジャンプボタン)";
	
	public static final String MEDAL_STR    = "勲章";
	public static final String MATERIAL_STR = "素材";
	public static final String SEED_STR     = "シード";
	public static final String REQARM_STR   = "要請兵器";

	public static final String[] ROOT_LIST         = { ROOT_PARTS, ROOT_WEAPON };
	public static final String[] BLUST_PARTS_LIST  = { BLUST_PARTS_HEAD, BLUST_PARTS_BODY, BLUST_PARTS_ARMS, BLUST_PARTS_LEGS };
	public static final String[] BLUST_TYPE_LIST   = { BLUST_TYPE_ASSALT, BLUST_TYPE_HEAVY, BLUST_TYPE_SNIPER, BLUST_TYPE_SUPPORT };
	public static final String[] WEAPON_TYPE_LIST  = { WEAPON_TYPE_MAIN, WEAPON_TYPE_SUB, WEAPON_TYPE_SUPPORT, WEAPON_TYPE_SPECIAL };
	public static final String[] CHIP_TYPE_LIST    = { SKILL_CHIP_STR, POWERUP_CHIP_STR, ACTION_CHIP_STR };
	
	public static final int ERROR = -1;
	
	/**
	 * 性能評価文字
	 */
	public static String[] SPEC_POINT = {
		      "S", "S-",
		"A+", "A", "A-",
		"B+", "B", "B-",
		"C+", "C", "C-",
		"D+", "D", "D-",
		"E+", "E", "E-"
	};
	
	/**
	 * BBDataManagerクラスのインスタンス。
	 * このインスタンスが唯一のインスタンスとする。
	 * また、本クラスのコンストラクタで上記のString型配列を参照しているため、
	 * その箇所より下でインスタンス生成を定義する必要がある。
	 */
	private static final BBDataManager sInstance = new BBDataManager(500);
	
	/**
	 * デフォルト値でソートする項目一覧
	 */
	private static final String SORT_DEFAULT = "デフォルト";
	
	//----------------------------------------------------------
	// クラス固有のインスタンス操作関連
	//----------------------------------------------------------
	
	/**
	 * BBDataManagerのインスタンスを取得する。
	 * @return BBDataManagerのインスタンス。
	 */
	public static BBDataManager getInstance() {
		return sInstance;
	}
	
	//----------------------------------------------------------
	// 初期化処理
	//----------------------------------------------------------
	
	/**
	 * 初期化処理を行う。
	 * フィルタ、ソートの情報を初期状態にする。
	 */
	private BBDataManager(int size) {
		super(size);
		init();
	}
	
	/**
	 * 初期化処理を行う。
	 * パーツ増殖バグ対策のため、データ読み込み前に実行する。
	 */
	public void init() {
		super.clear();
	}
	
	//----------------------------------------------------------
	// データ設定関連の関数
	//----------------------------------------------------------

	/**
	 * ソートキーを設定する。
	 * @param key ソートする対象のキー文字列。指定誤りの場合、"名称"をキーにする。
	 */
	@Override
	public void setSortKey(String key) {
		if(key == null) {
			super.setSortKey("");
		}
		else if(key.equals("名称")) {
			super.setSortKey("");
		}
		else {
			super.setSortKey(key);
		}
	}

	//----------------------------------------------------------
	// データ取得関連の関数
	//----------------------------------------------------------
	
	/**
	 * パーツデータを取得する
	 * @param name パーツ名
	 * @param type パーツの種類
	 * @return パーツデータ
	 */
	public BBData getPartsData(String name, String type) {
		int len = super.size();
		for(int i=0; i<len; i++) {
			BBData buf = super.get(i);
			String buf_name = buf.get("名称");
			if(buf_name.equals(name) && buf.existCategory(type)) {
				return buf;
			}
		}
		
		return new BBData();
	}

	/**
	 * 武器データを取得する。データがない場合はデフォルトの武器情報を返す。
	 * @param name 武器名
	 * @param type1 兵装名
	 * @param type2 武器種類名(主武器など)
	 * @return 武器データ
	 */
	public BBData getWeaponData(String name, String type1, String type2) {
		int len = super.size();
		for(int i=0; i<len; i++) {
			BBData buf = super.get(i);
			String buf_name = buf.get("名称");
			if(buf_name.equals(name) && buf.existCategory(type1) && buf.existCategory(type2)) {
				return buf;
			}
		}
		
		return new BBData();
	}
	
	/**
	 * チップデータを取得する。
	 * @param name チップ名
	 * @return チップデータ
	 */
	public BBData getChipData(String name) {
		int len = super.size();
		for(int i=0; i<len; i++) {
			BBData buf = super.get(i);
			String buf_name = buf.get("名称");
			if(buf_name.equals(name) && buf.existCategory(CHIP_STR)) {
				return buf;
			}
		}
		
		return new BBData();
	}
	
	/**
	 * 指定した系統のチップすべてを取得する。
	 * @param series 系統名
	 * @return チップリスト
	 */
	public ArrayList<BBData> getChipSeries(String series) {
		ArrayList<BBData> list = new ArrayList<BBData>();
		
		int len = super.size();
		for(int i=0; i<len; i++) {
			BBData buf = super.get(i);
			String buf_name = buf.get("名称");
			if(buf_name.startsWith(series) && buf.existCategory(CHIP_STR)) {
				list.add(buf);
			}
		}
		
		return list;
	}
	
	/**
	 * 要請兵器のデータを取得する。
	 * @param name 要請兵器の名前
	 * @return 要請兵器データ。
	 */
	public BBData getReqArmData(String name) {
		int len = super.size();
		for(int i=0; i<len; i++) {
			BBData buf = super.get(i);
			String buf_name = buf.get("名称");
			if(buf_name.equals(name) && buf.existCategory(REQARM_STR)) {
				return buf;
			}
		}
		
		return new BBData();
	}
	
	/**
	 * 指定のアイテムIDに対応したデータを取り出す
	 * @param item_id アイテムID
	 * @return アイテムデータ
	 */
	public BBData getData(int item_id) {
		if(item_id < 0 || item_id >= super.size()) {
			return new BBData();
		}
		
		return super.get(item_id);
	}
	
	/**
	 * フィルタの設定に対応したリストを取得する
	 * @return リスト
	 */
	@Override
	public ArrayList<BBData> getList() {
		return getList(null);
	}

	/**
	 * リストを取得する。
	 * フィルタを設定している場合、フィルタに応じたリストを生成する。
	 * @param filter フィルタ
	 * @return リスト
	 */
	public ArrayList<BBData> getList(BBDataFilter filter) {
		return getList(filter, false);
	}
	
	/**
	 * リストを取得する。
	 * フィルタを設定している場合、フィルタに応じたリストを生成する。
	 * @param filter フィルタ
	 * @param is_sort_type_b タイプBの性能値でソートするかどうか
	 * @return リスト
	 */
	public ArrayList<BBData> getList(BBDataFilter filter, boolean is_sort_type_b) {
		ArrayList<BBData> ret_list = super.getList();
		ret_list.clear();
		
		int datalist_len = super.size();
		
		for(int i=0; i<datalist_len; i++) {
			BBData buf_data = super.get(i);
			
			if(filter == null) {
				ret_list.add(buf_data);
			}
			else if(filter.isTargetData(buf_data)) {
				ret_list.add(buf_data);
			}
		}
		
		Collections.sort(ret_list, new BBDataComparator(super.getSortKey(), super.getASC(), true, is_sort_type_b));
		super.updateSortKeyList();

		return ret_list;
	}
	
	/**
	 * ソートキーの一覧を取得する
	 * @param list ソートキーを抽出する対象のリスト
	 * @return ソートキーのリスト
	 */
	public ArrayList<String> getSortKeyList(ArrayList<BBData> list) {
		ArrayList<String> ret_list = new ArrayList<String>(super.getSortKeyList());
		ret_list.add(0, SORT_DEFAULT);
		
		return ret_list;
	}
	
	/**
	 * 表示項目の一覧を取得する。
	 * @return
	 */
	public ArrayList<String> getShownKeyList() {
		return new ArrayList<String>(super.getSortKeyList());
	}
	
	/**
	 * アイテムを特定するキーを取得する
	 * @param data キーを取得するアイテム
	 * @return キーの名前 (強襲兵装:主武器、など)
	 */
	public static String getUniqueCategory(BBData data) {
		String ret = getPartsType(data);
		
		// パーツデータの場合、●部パーツを返す
		if(!ret.equals("")) {
			return ret;
		}
		
		ret = getBlustType(data);
		
		// 武器データの場合、●●兵装:●武器を返す
		if(!ret.equals("")) {
			return ret + ":" + getWeaponType(data);
		}
		
		return "";
	}
	
	/**
	 * パーツの種類を取得する
	 * @param data パーツデータ
	 * @return パーツの種類の文字列
	 */
	public static String getPartsType(BBData data) {
		String ret = "";
		int len = BLUST_PARTS_LIST.length;
		for(int i=0; i<len; i++) {
			String buf = BLUST_PARTS_LIST[i];
			if(data.existCategory(buf)) {
				ret = buf;
				break;
			}
		}
		
		return ret;
	}

	/**
	 * 指定武器の兵装の種類を取得する
	 * @param data 武器データ
	 * @return 兵装の文字列
	 */
	public static String getBlustType(BBData data) {
		String ret = "";
		int len = BLUST_TYPE_LIST.length;
		for(int i=0; i<len; i++) {
			String buf = BLUST_TYPE_LIST[i];
			if(data.existCategory(buf)) {
				ret = buf;
				break;
			}
		}
		
		return ret;
	}

	/**
	 * 指定武器の種類を取得する
	 * @param data 武器データ
	 * @return 種類の文字列
	 */
	public static String getWeaponType(BBData data) {
		String ret = null;
		int len = WEAPON_TYPE_LIST.length;
		for(int i=0; i<len; i++) {
			String buf = WEAPON_TYPE_LIST[i];
			if(data.existCategory(buf)) {
				ret = buf;
				break;
			}
		}
		
		return ret;
	}

	private static final String[] CMP_ITEM_INIT = {
		"重量"
	};
	
	private static final String[] CMP_HEAD = {
		"重量", "装甲", "射撃補正", "索敵", "ロックオン", "DEF回復", "DEF回復時間", "実耐久値"
	};
	
	private static final String[] CMP_BODY = {
		"重量", "装甲", "ブースター", "SP供給率", "エリア移動", "DEF耐久", 
		BBData.STEP_MAX_COUNT_KEY,
		BBData.BOOST_CHARGE_TIME_KEY,
		"実耐久値"
	};
	
	private static final String[] CMP_ARMS = {
		"重量", "装甲", "反動吸収", "リロード", "武器変更", "予備弾数", "実耐久値"
	};
	
	private static final String[] CMP_LEGS = {
		"重量", "装甲", "歩行", "ダッシュ", "巡航", "重量耐性", BBData.CARRY_KEY, "実耐久値"
	};
	
	private static final String[] CMP_MAIN_WEAPON = {
		"重量", "威力", "連射速度", "リロード時間",
		"総火力", "マガジン火力", "瞬間火力", "戦術火力", "OH火力", "総弾数(合計)",
		BBData.ARMOR_BREAK_KEY,
		BBData.ARMOR_DOWN_KEY,
		BBData.ARMOR_KB_KEY
	};

	private static final String[] CMP_SUB_WEAPON = {
		"重量", "威力", "連射速度", "リロード時間",
		"総火力", "マガジン火力", "瞬間火力", "戦術火力", "総弾数(合計)",
		"爆発半径",
		BBData.ARMOR_BREAK_KEY,
		BBData.ARMOR_DOWN_KEY,
		BBData.ARMOR_KB_KEY
	};

	private static final String[] CMP_SUPPORT_ASSALT = {
		"重量", "通常攻撃(威力)", "特殊攻撃(威力)", BBData.SLASH_DAMAGE_NL_KEY, BBData.SLASH_DAMAGE_EX_KEY
	};

	private static final String[] CMP_SUPPORT_HEAVY = {
		"重量", "威力", "爆発半径",
		BBData.ARMOR_BREAK_KEY,
		BBData.ARMOR_DOWN_KEY,
		BBData.ARMOR_KB_KEY
	};
	
	private static final String[] CMP_SUPPORT_SNIPER = {
		"重量", "威力", BBData.MAGAZINE_POWER_KEY, BBData.SEC_POWER_KEY, BBData.BATTLE_POWER_KEY,
		"索敵時間", "索敵範囲", "滞空時間"
	};
	
	private static final String[] CMP_SUPPORT_SUPPORT = {
		"重量", "耐久力", "索敵範囲", "索敵面積", "初動索敵面積", "総索敵面積", "戦術索敵面積", "索敵時間"
	};
	
	private static final String[] CMP_SPECIAL_ASSALT = {
		"重量", "出力", "チャージ時間"
	};
	
	private static final String[] CMP_SPECIAL_HEAVY = {
		"重量", "威力", "爆発半径", "チャージ時間",
		BBData.ARMOR_BREAK_KEY,
		BBData.ARMOR_DOWN_KEY,
		BBData.ARMOR_KB_KEY
	};
	
	private static final String[] CMP_SPECIAL_SUPPORT = {
		"重量", "最大持続時間", "修理速度", "最大修理量", "チャージ時間"
	};
	
	/**
	 * パーツまたは武器の種類に応じた比較用のリストを取得する。
	 * @param item パーツまたは武器データ
	 * @return 比較用のリスト。パーツでない場合は武器用のリストを返す。
	 */
	public static String[] getCmpTarget(BBData item) {
		String[] ret = CMP_ITEM_INIT;
		
		if(item.existCategory(BLUST_PARTS_HEAD)) {
			ret = CMP_HEAD;
		}
		else if(item.existCategory(BLUST_PARTS_BODY)) {
			ret = CMP_BODY;
		}
		else if(item.existCategory(BLUST_PARTS_ARMS)) {
			ret = CMP_ARMS;
		}
		else if(item.existCategory(BLUST_PARTS_LEGS)) {
			ret = CMP_LEGS;
		}
		else if(item.existCategory(WEAPON_TYPE_MAIN)) {
			ret = CMP_MAIN_WEAPON;
		}
		else if(item.existCategory(WEAPON_TYPE_SUB)) {
			ret = CMP_SUB_WEAPON;
		}
		else if(item.existCategory(WEAPON_TYPE_SUPPORT)) {
			if(item.existCategory(BLUST_TYPE_ASSALT)) {
				ret = CMP_SUPPORT_ASSALT;
			}
			else if(item.existCategory(BLUST_TYPE_HEAVY)) {
				ret = CMP_SUPPORT_HEAVY;
			}
			else if(item.existCategory(BLUST_TYPE_SNIPER)) {
				ret = CMP_SUPPORT_SNIPER;
			}
			else if(item.existCategory(BLUST_TYPE_SUPPORT)) {
				ret = CMP_SUPPORT_SUPPORT;
			}
		}
		else if(item.existCategory(WEAPON_TYPE_SPECIAL)) {
			if(item.existCategory(BLUST_TYPE_ASSALT)) {
				ret = CMP_SPECIAL_ASSALT;
			}
			else if(item.existCategory(BLUST_TYPE_HEAVY)) {
				ret = CMP_SPECIAL_HEAVY;
			}
			else if(item.existCategory(BLUST_TYPE_SUPPORT)) {
				ret = CMP_SPECIAL_SUPPORT;
			}
		}
		
		return ret;
	}

	//----------------------------------------------------------
	// データ判定関連の関数
	//----------------------------------------------------------
	
	/**
	 * 指定の２つのデータが同じデータであるか判別する。
	 * 判別はアイテムIDで行う。
	 * @param fm_data 比較対象１
	 * @param to_data 比較対象２
	 * @return 比較結果。同じ場合はtrueを返し、異なる場合はfalseを返す。
	 */
	public static boolean equalData(BBData fm_data, BBData to_data) {
		boolean ret = false;
		
		if(fm_data.id == to_data.id) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 指定のデータがパーツかどうか判別する
	 * @param data 判別するデータ
	 * @return パーツの場合はtrueを返し、パーツでない場合はfalseを返す。
	 */
	public static boolean isParts(BBData data) {
		boolean ret = false;
		int parts_len = BLUST_PARTS_LIST.length;
		
		for(int i=0; i<parts_len; i++) {
			if(data.existCategory(BLUST_PARTS_LIST[i])) {
				ret = true;
				break;
			}
		}
		
		return ret;
	}
	
	/**
	 * 指定のデータが要請兵器かどうか判別する。
	 * @param data 判別するデータ
	 * @return 要請兵器の場合はtrueを返し、そうでない場合はfalseを返す。
	 */
	public static boolean isReqArm(BBData data) {
		if(data.existCategory(REQARM_STR)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 指定名称のデータがパーツかどうか判別する。
	 * @param name 判別する名称
	 * @return パーツの場合はtrueを返し、パーツでない場合はfalseを返す。
	 */
	public boolean isParts(String name) {
		boolean ret = false;
		ArrayList<BBData> values = super.getAllList();
		int size = values.size();
		int parts_type_count = BLUST_PARTS_LIST.length;
		
		for(int i=0; i<size; i++) {
			BBData data = values.get(i);
			if(name.equals(data.get("名称"))) {
				
				for(int j=0; j<parts_type_count; j++) {
					if(data.existCategory(BLUST_PARTS_LIST[j])) {
						ret = true;
						break;
					}
				}
			}
		}
		
		return ret;
	}

	/**
	 * 指定データが武器かどうか判別する
	 * @param data 判別するデータ
	 * @return 武器の場合はtrueを返し、武器でない場合はfalseを返す。
	 */
	public static boolean isWeapon(BBData data) {
		boolean ret = false;
		int parts_len = WEAPON_TYPE_LIST.length;
		
		for(int i=0; i<parts_len; i++) {
			if(data.existCategory(WEAPON_TYPE_LIST[i])) {
				ret = true;
				break;
			}
		}
		
		return ret;
	}

	/**
	 * 指定名称のデータがパーツかどうか判別する。
	 * @param name 判別する名称
	 * @return パーツの場合はtrueを返し、パーツでない場合はfalseを返す。
	 */
	public boolean isWeapon(String name) {
		boolean ret = false;
		ArrayList<BBData> values = super.getAllList();
		int size = values.size();
		int weapon_type_count = WEAPON_TYPE_LIST.length;
		
		for(int i=0; i<size; i++) {
			BBData data = values.get(i);
			if(name.equals(data.get("名称"))) {
				
				for(int j=0; j<weapon_type_count; j++) {
					if(data.existCategory(WEAPON_TYPE_LIST[j])) {
						ret = true;
						break;
					}
				}
			}
		}
		
		return ret;
	}

	/**
	 * 指定の名称のデータがチップかどうか判別する。
	 * @param name 判別する名称
	 * @return チップの場合はtrueを返し、チップでない場合はfalseを返す。
	 */
	public boolean isChip(String name) {
		boolean ret = false;
		ArrayList<BBData> values = super.getAllList();
		int size = values.size();
		
		for(int i=0; i<size; i++) {
			BBData data = values.get(i);
			if(name.equals(data.get("名称")) && data.existCategory(CHIP_STR)) {
				ret = true;
				break;
			}
		}
		
		return ret;
	}
	
	/**
	 * 指定のデータがアクションチップかどうか判別する。
	 * @param chip 判別するチップ名
	 * @return アクションチップの場合はtrueを返し、そうでない場合はfalseを返す。
	 */
	public boolean isActionChip(BBData chip) {
		if(chip.existCategory(BBDataManager.ACTION_CHIP_STR)) {
			return true;
		}
		else if(chip.existCategory(BBDataManager.ACTION_ACT_CHIP_STR)) {
			return true;
		}
		else if(chip.existCategory(BBDataManager.ACTION_DASH_CHIP_STR)) {
			return true;				
		}
		else if(chip.existCategory(BBDataManager.ACTION_JUMP_CHIP_STR)) {
			return true;				
		}
		
		return false;
	}
	
	/**
	 * 指定の名称のデータが勲章かどうか判別する。
	 * @param name 判別する名称
	 * @return 勲章の場合はtrueを返し、勲章でない場合はfalseを返す。
	 */
	public boolean isMedal(String name) {
		boolean ret = false;
		ArrayList<BBData> values = super.getAllList();
		int size = values.size();
		
		for(int i=0; i<size; i++) {
			BBData data = values.get(i);
			if(name.equals(data.get("名称")) && data.existCategory(MEDAL_STR)) {
				ret = true;
				break;
			}
		}
		
		return ret;
	}
	
	/**
	 * 指定の名称のデータが素材かどうか判別する。
	 * @param name 判別する名称
	 * @return 素材の場合はtrueを返し、素材でない場合はfalseを返す。
	 */
	public boolean isMaterial(String name) {
		boolean ret = false;
		ArrayList<BBData> values = super.getAllList();
		int size = values.size();
		
		for(int i=0; i<size; i++) {
			BBData data = values.get(i);
			if(name.equals(data.get("名称")) && data.existCategory(MATERIAL_STR)) {
				ret = true;
				break;
			}
		}
		
		return ret;
	}
}
