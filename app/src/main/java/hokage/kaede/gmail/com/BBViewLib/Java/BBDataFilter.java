package hokage.kaede.gmail.com.BBViewLib.Java;

import java.util.ArrayList;

/**
 * パーツや武器などを一覧表示する際に、そのアイテムが表示対象かどうかを判定するクラス。
 */
public class BBDataFilter {

	private ArrayList<String> mPartsList;
	private ArrayList<String> mBlustList;
	private ArrayList<String> mWeaponList;
	private ArrayList<String> mOtherList;
	
	private ArrayList<String> mKeyList;
	private ArrayList<String> mValueList;

	// 所持済み/未所持のアイテムを表示するかどうか
	private boolean is_show_having = true;
	private boolean is_show_not_having = true;
	
	/**
	 * 初期化を行う。
	 */
	public BBDataFilter() {
		mPartsList = new ArrayList<String>();
		mBlustList = new ArrayList<String>();
		mWeaponList = new ArrayList<String>();
		mOtherList = new ArrayList<String>();
		
		mKeyList = new ArrayList<String>();
		mValueList = new ArrayList<String>();
		
		// 所持済みのデータのみ表示する設定の場合、未所持は非表示にする。
		if(BBViewSetting.IS_SHOW_HAVING) {
			is_show_not_having = false;
		}
	}
	
	/**
	 * フィルタカテゴリを設定する。
	 * @param item カテゴリ名
	 */
	public void setType(String... item) {
		boolean is_set = false;
		int size = item.length;
		
		for(int i=0; i<size; i++) {
			is_set = false;
			
			int sub_size = BBDataManager.BLUST_PARTS_LIST.length;
			for(int j=0; j<sub_size; j++) {
				if(BBDataManager.BLUST_PARTS_LIST[j].equals(item[i])) {
					mPartsList.add(item[i]);
					is_set = true;
					break;
				}
			}
			
			if(is_set) {
				continue;
			}

			sub_size = BBDataManager.BLUST_TYPE_LIST.length;
			for(int j=0; j<sub_size; j++) {
				if(BBDataManager.BLUST_TYPE_LIST[j].equals(item[i])) {
					mBlustList.add(item[i]);
					is_set = true;
					break;
				}
			}

			if(is_set) {
				continue;
			}

			sub_size = BBDataManager.WEAPON_TYPE_LIST.length;
			for(int j=0; j<sub_size; j++) {
				if(BBDataManager.WEAPON_TYPE_LIST[j].equals(item[i])) {
					mWeaponList.add(item[i]);
					is_set = true;
					break;
				}
			}

			if(is_set) {
				continue;
			}

			mOtherList.add(item[i]);
		}
	}
	
	/**
	 * パーツの種類のフィルタカテゴリを設定する。
	 * @param item カテゴリ名
	 */
	public void setPartsType(String... item) {
		int size = item.length;
		for(int i=0; i<size; i++) {
			mPartsList.add(item[i]);
		}
	}

	/**
	 * 兵装名のフィルタカテゴリを設定する。
	 * @param item カテゴリ名
	 */
	public void setBlustType(String... item) {
		int size = item.length;
		for(int i=0; i<size; i++) {
			mBlustList.add(item[i]);
		}
	}

	/**
	 * 武器の種類のフィルタカテゴリを設定する。
	 * @param item カテゴリ名
	 */
	public void setWeaponType(String... item) {
		int size = item.length;
		for(int i=0; i<size; i++) {
			mWeaponList.add(item[i]);
		}
	}

	/**
	 * その他のフィルタカテゴリを設定する。
	 * @param item カテゴリ名
	 */
	public void setOtherType(String... item) {
		int size = item.length;
		for(int i=0; i<size; i++) {
			mOtherList.add(item[i]);
		}
	}
	
	/**
	 * フィルタするキーと値を設定する。
	 * @param key キー
	 * @param value 値 
	 */
	public void setValue(String key, String value) {
		mKeyList.add(key);
		mValueList.add(value);
	}
	
	/**
	 * 全てのフィルタ情報を削除する。
	 */
	public void clear() {
		mPartsList.clear();
		mBlustList.clear();
		mWeaponList.clear();
		mOtherList.clear();
		mKeyList.clear();
		mValueList.clear();
	}
	
	/**
	 * フィルタ対象のキー/値情報を削除する。
	 */
	public void clearKey() {
		mKeyList.clear();
		mValueList.clear();
	}
	
	/**
	 * 所持しているアイテムを表示するかどうかを設定する。
	 * 未設定時はtrue。
	 * @param is_show
	 */
	public void setHavingShow(boolean is_show) {
		is_show_having = is_show;
	}
	
	/**
	 * 未所持のアイテムを表示するかどうかを設定する。
	 * 未設定時はtrue。
	 * @param is_show
	 */
	public void setNotHavingShow(boolean is_show) {
		is_show_not_having = is_show;
	}
	
	/**
	 * 指定のデータがフィルタの設定に対応したデータかどうか判定する。
	 * @param target 判定するデータ
	 * @return 判定結果。対応している場合はtrueを返し、対応していない場合はfalseを返す。
	 */
	public boolean isTargetData(BBData target) {
		boolean data_type_ok = false;
		boolean ret = false;
		
		// 所持状況によるフィルタを行う
		if(!is_show_having || !is_show_not_having) {
			BBItemDatabase database = BBItemDatabase.getInstance();
			boolean is_having;
			if(database.existParts(target) || database.existWeapon(target) || database.existChip(target)) {
				is_having = true;
			}
			else {
				is_having = false;
			}
			
			if(!is_show_having && is_having) {
				return false;
			}
			else if(!is_show_not_having && !is_having) {
				return false;
			}
		}

		// データの種類についてフィルタを行う
		if(existCategory(target, mPartsList)) {
			data_type_ok = true;
		}
		else if(existCategory(target, mBlustList) && existCategory(target, mWeaponList)) {
			data_type_ok = true;
		}
		else if(existCategory(target, mOtherList)) {
			data_type_ok = true;
		}
		
		// 性能値についてフィルタを行う
		if(data_type_ok && compareValue(target)) {
			ret = true;
		}

		return ret;
	}
	
	/**
	 * 指定のカテゴリかどうかを判定する。
	 * @param target 対象のパーツまたは武器
	 * @param list 対象のカテゴリ
	 * @return 対象のカテゴリを1つでも有する場合はtrue、有しない場合はfalse。
	 */
	private boolean existCategory(BBData target, ArrayList<String> list) {
		boolean ret = false;
		int size = list.size();
		
		for(int i=0; i<size; i++) {
			if(target.existCategory(list.get(i))) {
				ret = true;
				break;
			}
		}
		
		return ret;
	}
	
	/**
	 * 各フィルタ用のデータが指定の値以上かどうか判定する。
	 * 比較対象のキーがコストの場合は、値が不一致の場合にfalseとなる。
	 * @param target 対象のパーツまたは武器
	 * @return 全て指定以上の場合はTrue、１つでも指定未満の場合はFalse。
	 */
	private boolean compareValue(BBData target) {
		boolean ret = true;
		int size = mKeyList.size();
		
		for(int i=0; i<size; i++) {
			String key = mKeyList.get(i);
			String value = mValueList.get(i);
			
			if(key.equals("属性")) {
				if(!compareAbsolute(target, value)) {
					ret = false;
					break;
				}
			}
			else if(key.equals("コスト")) {
				if(!value.equals(target.get(key))) {
					ret = false;
					break;
				}
			}
			else {
				BBDataComparator comparator = new BBDataComparator(key, true, true);
				if(comparator.compareFilter(target, value) < 0) {
					ret = false;
					break;
				}
			}
		}

		return ret;
	}
	
	/**
	 * 指定の属性データを含むかどうか判定する。
	 * @param target 対象のパーツまたは武器
	 * @param cmp_value 対象の属性
	 * @return 指定の属性を有する場合はtrue、有しない場合はfalse。
	 */
	private boolean compareAbsolute(BBData target, String cmp_value) {
		boolean ret = false;
		
		if(cmp_value.equals("射撃") && target.isShotWeapon()) {
			ret = true;
		}
		else if(cmp_value.equals("爆発") && target.isExplosionWeapon()) {
			ret = true;
		}
		else if(cmp_value.equals("近接") && target.isSlashWeapon()) {
			ret = true;
		}
		else {
			ret = false;
		}
		
		return ret;
	}
}
