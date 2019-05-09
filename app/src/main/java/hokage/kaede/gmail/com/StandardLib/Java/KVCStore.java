package hokage.kaede.gmail.com.StandardLib.Java;

import java.util.ArrayList;

/**
 * キーと値によるデータ管理に加え、カテゴリデータ管理機能を備えたクラス。
 */
public class KVCStore extends KeyValueStore {
	
	/**
	 * カテゴリデータのリスト
	 */
	private ArrayList<String> mCategorys;
	
	/**
	 * 初期化を行う。
	 */
	public KVCStore() {
		mCategorys = new ArrayList<String>();
		
	}

	/**
	 * カテゴリデータを追加する。
	 * @param category 追加するカテゴリデータ。
	 */
	public void addCategory(String category) {
		mCategorys.add(category);
	}

	/**
	 * カテゴリデータのリストを取得する。
	 * @return カテゴリデータのリスト。
	 */
	public ArrayList<String> getCategorys() {
		return new ArrayList<String>(mCategorys);
	}
	
	/**
	 * カテゴリデータを削除する。
	 * @param category 削除するカテゴリデータ。
	 */
	public void removeCategory(String category) {
		int idx = mCategorys.indexOf(category);
		
		if(idx >= 0) {
			mCategorys.remove(idx);
		}
	}
	
	/**
	 * カテゴリデータの有無を取得する。
	 * @param category 判定するカテゴリデータ。
	 * @return カテゴリデータが存在する場合はtrueを返し、存在しない場合はfalseを返す。
	 */
	public boolean existCategory(String category) {
		return mCategorys.contains(category);
	}
}
