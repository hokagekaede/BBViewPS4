package hokage.kaede.gmail.com.BBViewLib.Java;

import java.util.ArrayList;

import hokage.kaede.gmail.com.StandardLib.Java.FileKeyValueStore;

/**
 * 購入リストをファイルから読み込み、書き込みするクラス。
 */
public class PurchaseStore {

	private FileKeyValueStore mStore;
	
	private static final String PURCHASE_FILENAME = "purchase.dat";
	
	public PurchaseStore(String dir_path) {
		mStore = new FileKeyValueStore(dir_path, PURCHASE_FILENAME);
		mStore.load();
	}
	
	public boolean add(BBData data) {
		String key = createKey(data);
		
		if(!mStore.existKey(key)) {
			mStore.set(key, "null");
			mStore.save();
			return true;
		}
		
		return false;
	}
	
	public void remove(BBData data) {
		String key = createKey(data);
		mStore.remove(key);
		mStore.save();
	}

	public void clear() {
		mStore.clear();
		mStore.save();
	}
	
	private String createKey(BBData data) {
		String key = "";
		
		if(BBDataManager.isParts(data)) {
			key = "パーツ:" + BBDataManager.getPartsType(data) + ":" + data.get("名称");
		}
		else {
			key = BBDataManager.getBlustType(data) + ":" + BBDataManager.getWeaponType(data) + ":" + data.get("名称");
		}
		
		return key;
	}
	
	public ArrayList<BBData> getPurchaseList() {
		ArrayList<BBData> list = new ArrayList<BBData>();
		
		BBDataManager data_mng = BBDataManager.getInstance();
		
		int len = mStore.size();
		ArrayList<String> keys = mStore.getKeys();
		
		for(int i=0;i<len;i++) {
			String key = keys.get(i);
			String[] split_key = key.split(":");
			int key_count = split_key.length;
			
			if(key_count!=3) {
				continue;
			}
			
			if(split_key[0].equals("パーツ")) {
				list.add(data_mng.getPartsData(split_key[2], split_key[1]));
			}
			else {
				list.add(data_mng.getWeaponData(split_key[2], split_key[1], split_key[0]));
			}
		}
		
		return list;
	}
}
