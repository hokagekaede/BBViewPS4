package hokage.kaede.gmail.com.BBViewLib.Java;

import java.util.ArrayList;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;

/**
 * 購入リストにあるアイテムの購入に必要な勲章数と素材数、GPを算出するクラス。
 */
public class PurchaseManager {
	private ArrayList<String> mMedalNameList;
	private ArrayList<Integer> mMedalSumList;
	private ArrayList<String> mMaterialNameList;
	private ArrayList<Integer> mMaterialSumList;
	private int mSumGp;

	/**
	 * 初期化処理を行う。
	 * 勲章データおよび素材データのリストを抽出する。
	 * @param medal_list
	 * @param material_list
	 */
	public PurchaseManager(ArrayList<BBData> medal_list, ArrayList<BBData> material_list) {
		this.mMedalNameList = new ArrayList<String>();
		this.mMedalSumList = new ArrayList<Integer>();
		this.mMaterialNameList = new ArrayList<String>();
		this.mMaterialSumList = new ArrayList<Integer>();
		this.mSumGp = 0;
		
		int size = medal_list.size();
		for(int i=0; i<size; i++) {
			this.mMedalNameList.add(medal_list.get(i).get("名称"));
			this.mMedalSumList.add(0);
		}

		size = material_list.size();
		for(int i=0; i<size; i++) {
			this.mMaterialNameList.add(material_list.get(i).get("名称"));
			this.mMaterialSumList.add(0);
		}
	}
	
	/**
	 * 購入アイテムを設定する。
	 * @param data 購入アイテム
	 */
	public void setData(BBData data) {
		addItem(data);
		addGP(data);
	}
	
	/**
	 * 素材勲章データを追加する
	 * @param data 追加する素材勲章データ
	 */
	private void addItem(BBData data) {
		String[] target_list = data.get("素材・勲章").split(" ");
		int target_list_len = target_list.length;
		
		// 与えられた各データについて、素材/勲章のデータを設定する
		for(int i=0; i<target_list_len; i++) {
			String[] name_and_num = target_list[i].split("x");
			int num = 0;
			
			try {
				String name = name_and_num[0];
				num = Integer.valueOf(name_and_num[1]);

				// 素材リストまたは勲章リストにデータを追加する
				if(isExistItem(mMaterialNameList, name_and_num[0])) {
					setItemNum(mMaterialNameList, mMaterialSumList, name, num, false);
				}
				else if(isExistItem(mMedalNameList, name_and_num[0])) {
					setItemNum(mMedalNameList, mMedalSumList, name, num, true);
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * リストに指定の名称のデータが含まれるかどうか判定する
	 * @param source_list 判定対象のリスト
	 * @param item_name 判定対象の名称
	 * @return リストに含まれている場合はtrueを返す。含まれていない場合はfalseを返す。
	 */
	private boolean isExistItem(ArrayList<String> source_list, String item_name) {
		boolean ret = false;
		int size = source_list.size();
		for(int i=0; i<size; i++) {
			String source_name = source_list.get(i);
			
			if(item_name.equals(source_name)) {
				ret = true;
				break;
			}
		}
		
		return ret;
	}
	
	/**
	 * リストに素材勲章データと個数を設定する
	 * @param name_list 対象のデータリスト (素材or勲章)
	 * @param num_list 対象の個数リスト (素材or勲章)
	 * @param name 素材勲章の名前
	 * @param num 素材勲章データの個数
	 * @param is_medal 勲章の場合はtrue。
	 */
	private void setItemNum(ArrayList<String> name_list, ArrayList<Integer> num_list, String name, int num, boolean is_medal) {
		int index = name_list.indexOf(name);
		
		if(index >= 0) {
			int recent_num = num_list.get(index);
			if(is_medal) {
				if(recent_num < num) {
					num_list.set(index, num);
				}
			}
			else {
				num_list.set(index, recent_num + num);
			}
		}
		else {
			name_list.add(name);
			num_list.add(num);
		}
	}
	
	/**
	 * 指定データのGPを加算する
	 * @param data GPを加算するデータ
	 */
	private void addGP(BBData data) {
		int gp = 0;
		
		try {
			gp = Integer.valueOf(data.get("GP"));
			mSumGp = mSumGp + gp;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 購入に必要な合計GPを取得する
	 * @return
	 */
	public int getSumGP() {
		return mSumGp;
	}

	/**
	 * 購入に必要な素材の種類の数を取得する
	 * @return 素材の種類の数
	 */
	public int getMaterialCount() {
		return mMaterialNameList.size();
	}

	/**
	 * 指定位置に登録された勲章の名前を取得する
	 * @param position 指定位置
	 * @return 勲章の名前
	 */
	public String getMaterialName(int position) {
		return mMaterialNameList.get(position);
	}
	
	/**
	 * 指定位置に登録された勲章の個数を取得する
	 * @param position 指定位置
	 * @return 勲章の個数
	 */
	public int getMaterialSum(int position) {
		return mMaterialSumList.get(position);
	}
	
	/**
	 * 購入に必要な勲章の種類の数を取得する
	 * @return 勲章の種類の数
	 */
	public int getMedalCount() {
		return mMedalNameList.size();
	}

	/**
	 * 指定位置に登録された勲章の名前を取得する
	 * @param position 指定位置
	 * @return 勲章の名前
	 */
	public String getMedalName(int position) {
		return mMedalNameList.get(position);
	}
	
	/**
	 * 指定位置に登録された勲章の個数を取得する
	 * @param position 指定位置
	 * @return 勲章の個数
	 */
	public int getMedalSum(int position) {
		return mMedalSumList.get(position);
	}
	
}
