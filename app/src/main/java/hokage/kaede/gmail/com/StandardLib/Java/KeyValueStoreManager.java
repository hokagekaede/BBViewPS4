package hokage.kaede.gmail.com.StandardLib.Java;

import java.util.ArrayList;
import java.util.Collections;

/**
 * KeyValueStoreのデータを管理するデータベースクラス。
 * @param <T> KeyValueStoreクラスのサブクラス
 */
public class KeyValueStoreManager<T extends KeyValueStore> {
	
	private ArrayList<T> mStoreAllList;
	private ArrayList<T> mStoreRecentList;
	
	private ArrayList<String> mSortKeyList;
	private String mRecentSortKey;
	
	private boolean mASC;

	/**
	 * 初期化処理を行う。
	 */
	public KeyValueStoreManager(int size) {
		mStoreAllList = new ArrayList<T>(size);
		mStoreRecentList = new ArrayList<T>();
		mSortKeyList = new ArrayList<String>();
		mRecentSortKey = "";
		mASC = true;
	}
	
	/**
	 * データを追加する。
	 * @param store 追加するデータ。
	 */
	public void add(T store) {
		mStoreAllList.add(store);
	}
	
	/**
	 * データを取得する。
	 * @param idx 取得するデータの位置。
	 * @return 指定位置のデータ。
	 */
	public T get(int idx) {
		return mStoreAllList.get(idx);
	}
	
	/**
	 * 全項目のリストのサイズを取得する。
	 * @return 全項目のリストのサイズ。
	 */
	public int size() {
		return mStoreAllList.size();
	}
	
	/**
	 * 全項目を格納したリストを取得する。
	 * @return 全項目を格納したリスト。
	 */
	public ArrayList<T> getAllList() {
		return mStoreAllList;
	}
	
	/**
	 * 現在のリストを取得する。
	 * @return 現在のリスト。
	 */
	public ArrayList<T> getList() {
		return mStoreRecentList;
	}
	
	/**
	 * 現在のリストをソートする。
	 */
	public void sort() {
		Collections.sort(mStoreRecentList, new KeyValueStoreComparator<KeyValueStore>(mRecentSortKey, mASC));
	}
	
	/**
	 * ソートキーのリストを更新する。
	 */
	public void updateSortKeyList() {
		mSortKeyList.clear();
		int store_size = mStoreRecentList.size();
		
		for(int i=0; i<store_size; i++) {
			KeyValueStore buf_kvs = mStoreRecentList.get(i);
			ArrayList<String> keys = buf_kvs.getKeys();
			int keys_size = keys.size();
			
			for(int j=0; j<keys_size; j++) {
				String key = keys.get(j);

				if(!mSortKeyList.contains(key)) {
					mSortKeyList.add(key);
				}
			}
		}
	}
	
	/**
	 * ソートキーを設定する。
	 * @param sort_key ソートキー。
	 */
	public void setSortKey(String sort_key) {
		mRecentSortKey = sort_key;
	}
	
	/**
	 * ソートキーを取得する。
	 * @return ソートキー。
	 */
	public String getSortKey() {
		return mRecentSortKey;
	}
	
	/**
	 * 現在のソートキーのリストを取得する。
	 * @return 現在のソートキーのリスト
	 */
	public ArrayList<String> getSortKeyList() {
		return mSortKeyList;
	}
	
	/**
	 * 昇順/降順を設定する。
	 * @param is_asc 昇順/降順
	 */
	public void setASC(boolean is_asc) {
		mASC = is_asc;
	}
	
	/**
	 * 昇順/降順を取得する。
	 * @return 昇順の場合はtrueを返し、降順の場合はfalseを返す。
	 */
	public boolean getASC() {
		return mASC;
	}
	
	/**
	 * データをクリアする。
	 */
	public void clear() {
		mStoreAllList.clear();
		mStoreRecentList.clear();
		mSortKeyList.clear();
		mRecentSortKey = "";
	}
}
