package hokage.kaede.gmail.com.BBViewLib.Java;

import java.util.Comparator;

/**
 * ２つのパーツや武器に対し、特定のスペック項目で比較し、優劣を算出するクラス。
 */
public class BBDataComparator implements Comparator<BBData> {
	private String mTargetKey;
	private boolean mIsAsc;
	private boolean mIsSortTypeB;
	
	private double mCmpLastValue;
	private double mMinValue = -10000;
	private boolean mIsCmpOk;

	private int mLastCmpDoubleSize; // 最後の比較結果の倍率
	
	/**
	 * int型を比較する際の倍率の大きさ
	 */
	public static final int CMP_INT_SIZE = 1;
	
	/**
	 * double型を比較する際の倍率の大きさ
	 */
	public static final int CMP_DOUBLE_SIZE = 100;
	
	/**
	 * 性能評価文字でソートする項目
	 */
	private static final String[] SORT_POINT_TARGET = {
		"装甲", 
		"射撃補正",   "索敵",     "ロックオン", "DEF回復", 
		"ブースター", "SP供給", "エリア移動", "DEF耐久",
		"反動吸収",   "リロード", "武器変更",   "予備弾数",
		"歩行",       "ダッシュ", "重量耐性",   "巡航"
	};
	
	private static final String[] SORT_REVERSE_TARGET = {
		"重量",
		"エリア移動",
		"リロード時間",
		"総重量",
		"DEF回復時間",
		"ブースター回復時間",
		"チャージ時間",
		"OH復帰時間",
		"充填時間"
	};

	/**
	 * 初期化処理を行う。
	 * @param target_key 比較するキー。
	 * @param is_asc 昇順の場合はtrueを設定し、降順の場合はfalseを設定する。
	 */
	public BBDataComparator(String target_key, boolean is_asc) {
		init(target_key, is_asc, false);
	}
	
	/**
	 * 初期化処理を行う。
	 * @param target_key 比較するキー。
	 * @param is_asc 昇順の場合はtrueを設定し、降順の場合はfalseを設定する。
	 * @param is_sort_type_b タイプBの性能値でソートするかどうか。
	 */
	public BBDataComparator(String target_key, boolean is_asc, boolean is_sort_type_b) {
		init(target_key, is_asc, is_sort_type_b);
	}
	
	/**
	 * 初期化処理を行う。
	 * @param target_key 比較するキー。
	 * @param is_asc 昇順の場合はtrueを設定し、降順の場合はfalseを設定する。
	 * @param is_sort_type_b タイプBの性能値でソートするかどうか。
	 */
	private void init(String target_key, boolean is_asc, boolean is_sort_type_b) {
		this.mTargetKey = target_key;
		this.mIsAsc = is_asc;
		this.mCmpLastValue = 0;
		this.mIsCmpOk = false;
		this.mIsSortTypeB = is_sort_type_b;
		
		if(target_key != null) {
			int len = SORT_REVERSE_TARGET.length;
			for(int i=0; i<len; i++) {
				if(target_key.equals(SORT_REVERSE_TARGET[i])) {
					this.mIsAsc = !is_asc;
					break;
				}
			}
		}
	}
	
	/**
	 * パーツ同士または武器同士の比較を行う。
	 * @param from_data パーツや武器のデータ
	 * @param to_data パーツや武器のデータ
	 * @return 正の値の場合は引数1側が高い、0の場合は同値、負の値の場合は引数2側が高い。
	 */
	@Override
	public int compare(BBData from_data, BBData to_data) {
		BBDataComparatorItem item0 = new BBDataComparatorItem(from_data, mTargetKey, mIsSortTypeB);
		BBDataComparatorItem item1 = new BBDataComparatorItem(to_data, mTargetKey, mIsSortTypeB);

		return compareMain(item0, item1);
	}

	/**
	 * パーツや武器のデータとスペック値を比較する。
	 * @param from_data パーツや武器のデータ
	 * @param value スペック値
	 * @return 正の値の場合は引数1側が高い、0の場合は同値、負の値の場合は引数2側が高い。
	 */
	public int compareFilter(BBData from_data, String value) {
		BBDataComparatorItem item0 = new BBDataComparatorItem(from_data, mTargetKey, mIsSortTypeB);
		BBDataComparatorItem item1 = new BBDataComparatorItem(value, mTargetKey);

		return compareMain(item0, item1);
	}

	/**
	 * スペック値同士の比較処理を行う。
	 * @param from_value 比較対象のスペック値1
	 * @param to_value 比較対象のスペック値2
	 * @return 正の値の場合は引数1側が高い、0の場合は同値、負の値の場合は引数2側が高い。
	 */
	public int compareValue(double from_value, double to_value) {
		BBDataComparatorItem item0 = new BBDataComparatorItem(from_value);
		BBDataComparatorItem item1 = new BBDataComparatorItem(to_value);

		return compareMain(item0, item1);
	}

	/**
	 * 比較用のデータについて比較処理を行う。
	 * @param from_item 比較用データ1
	 * @param to_item 比較用データ2
	 * @return 正の値の場合は引数1側が高い、0の場合は同値、負の値の場合は引数2側が高い。
	 */
	public int compareMain(BBDataComparatorItem from_item, BBDataComparatorItem to_item) {
		int from_comptype = from_item.getCompType();
		int to_comptype = to_item.getCompType();

		// 比較の種類が同じ場合のみ比較成功扱いにする。
		if(from_comptype == to_comptype) {
			mIsCmpOk = true;
		}
		else {
			mIsCmpOk = false;
		}

		double from_value = from_item.getCompValue();
		double to_value = to_item.getCompValue();

		if(mIsAsc) {
			mCmpLastValue = from_value - to_value;
		}
		else {
			mCmpLastValue = to_value - from_value;
		}

		return (int)(mCmpLastValue * CMP_DOUBLE_SIZE);
	}

	/**
	 * 比較結果の差分値を返す。
	 * @return 比較結果の差分値。
	 */
	public double getCmpValue() {
		return mCmpLastValue;
	}

	/**
	 * 比較に成功したかどうかを返す。
	 * @return 比較に成功した場合はtrueを返し、失敗した場合はfalseを返す。
	 */
	public boolean isCmpOK() {
		return mIsCmpOk;
	}

	/**
	 * 性能値がポイント表示の項目かどうかを判別する。
	 * @param key 項目名
	 * @return 性能値がポイント表示の項目の場合はtrueを返し、そうでない場合はfalseを返す。
	 */
	public static boolean isPointKey(String key) {
		int len = SORT_POINT_TARGET.length;
		
		for(int i=0; i<len; i++) {
			if(key.equals(SORT_POINT_TARGET[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 最後の比較の倍率の大きさを取得する。
	 * @return 最後の比較の倍率の大きさ
	 */
	public int getLastCmpSize() {
		return mLastCmpDoubleSize;
	}
}
