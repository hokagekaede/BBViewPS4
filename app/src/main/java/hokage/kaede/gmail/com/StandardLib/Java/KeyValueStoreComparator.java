package hokage.kaede.gmail.com.StandardLib.Java;

import java.util.Comparator;

/**
 * KeyValueStoreのデータを比較するクラス。
 * @param <T> KeyValueStoreクラスを継承したサブクラス
 */
public class KeyValueStoreComparator<T extends KeyValueStore> implements Comparator<T> {
	
	private static final int RESULT_DOUBLE_SIZE = 100;
	
	private String mSortKey;
	private double mResultValue;
	private boolean mSuccess;
	private boolean mASC;
	
	/**
	 * 初期化を行う。
	 * @param sort_key 比較に使用するキー。
	 * @param is_asc 昇順または降順を示す。
	 */
	public KeyValueStoreComparator(String sort_key, boolean is_asc) {
		mSortKey = sort_key;
		mASC = is_asc;
		mSuccess = false;
	}

	/**
	 * 比較を行う。
	 */
	@Override
	public int compare(T arg0, T arg1) {
		int ret = 0;
		String arg0_target = arg0.get(mSortKey);
		String arg1_target = arg1.get(mSortKey);
		
		try {
			double arg0_num = Double.valueOf(arg0_target);
			double arg1_num = Double.valueOf(arg1_target);

			if(mASC) {
				mResultValue = arg0_num - arg1_num;
			}
			else {
				mResultValue = arg1_num - arg0_num;
			}
			
			ret = (int)(mResultValue * RESULT_DOUBLE_SIZE);
			mSuccess = true;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * 比較結果の詳細値を取得する。
	 * @return 比較結果の詳細値。
	 */
	public double getResult() {
		return mResultValue;
	}
	
	/**
	 * 前回の比較が成功したかどうかを取得する。
	 * @return 比較に成功した場合はtrueを返し、失敗した場合はfalseを返す。
	 */
	public boolean isLastCmpSuccess() {
		return mSuccess;
	}
}
