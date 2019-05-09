package hokage.kaede.gmail.com.BBViewLib.Java;

import java.util.ArrayList;

import hokage.kaede.gmail.com.StandardLib.Java.FileKeyValueStore;

/**
 * 所持情報の管理を行うクラス。
 */
public class BBNetDatabase {
	
	public static final String NO_CARD_DATA = "カード未登録";
	
	public static final int CARD_MAX = 4;
	
	private static final String CARDNAME_FILENAME_HEAD = "card";
	private static final String HEADPARTS_FILENAME_HEAD = "head_parts";
	private static final String BODYPARTS_FILENAME_HEAD = "body_parts";
	private static final String ARMSPARTS_FILENAME_HEAD = "arms_parts";
	private static final String LEGSPARTS_FILENAME_HEAD = "legs_parts";
	private static final String WEAPON_FILENAME_HEAD = "weapon";
	private static final String CHIP_FILENAME_HEAD = "chip";
	private static final String MATERIAL_FILENAME_HEAD = "material";
	private static final String MEDAL_FILENAME_HEAD = "medal";
	private static final String SEED_FILENAME_HEAD = "seed";
	private static final String FILENAME_TAIL = ".dat";
	
	private static final String CARDKEY_HEAD = "card";
	
	private static BBNetDatabase mInstance = new BBNetDatabase();

	private int mCardNumber;
	private FileKeyValueStore mCardName;
	private FileKeyValueStore[] mHeadPartsStore;
	private FileKeyValueStore[] mBodyPartsStore;
	private FileKeyValueStore[] mArmsPartsStore;
	private FileKeyValueStore[] mLegsPartsStore;
	private FileKeyValueStore[] mWeaponStore;
	private FileKeyValueStore[] mChipStore;
	private FileKeyValueStore[] mMaterialStore;
	private FileKeyValueStore[] mMedalStore;
	private FileKeyValueStore[] mSeedStore;
	
	private BBNetDatabase() {
		mCardNumber = 0;
		mHeadPartsStore = new FileKeyValueStore[CARD_MAX];
		mBodyPartsStore = new FileKeyValueStore[CARD_MAX];
		mArmsPartsStore = new FileKeyValueStore[CARD_MAX];
		mLegsPartsStore = new FileKeyValueStore[CARD_MAX];
		mWeaponStore = new FileKeyValueStore[CARD_MAX];
		mChipStore = new FileKeyValueStore[CARD_MAX];
		mMaterialStore = new FileKeyValueStore[CARD_MAX];
		mMedalStore = new FileKeyValueStore[CARD_MAX];
		mSeedStore = new FileKeyValueStore[CARD_MAX];
	}
	
	/**
	 * 初期化処理を行う。
	 * @param dir_path
	 */
	public void init(String dir_path) {
		mCardName = new FileKeyValueStore(dir_path, CARDNAME_FILENAME_HEAD + FILENAME_TAIL);
		mCardName.load();
		
		for(int i=0; i<CARD_MAX; i++) {
			mHeadPartsStore[i] = new FileKeyValueStore(dir_path, HEADPARTS_FILENAME_HEAD + String.valueOf(i) + FILENAME_TAIL);
			mHeadPartsStore[i].load();
			mBodyPartsStore[i] = new FileKeyValueStore(dir_path, BODYPARTS_FILENAME_HEAD + String.valueOf(i) + FILENAME_TAIL);
			mBodyPartsStore[i].load();
			mArmsPartsStore[i] = new FileKeyValueStore(dir_path, ARMSPARTS_FILENAME_HEAD + String.valueOf(i) + FILENAME_TAIL);
			mArmsPartsStore[i].load();
			mLegsPartsStore[i] = new FileKeyValueStore(dir_path, LEGSPARTS_FILENAME_HEAD + String.valueOf(i) + FILENAME_TAIL);
			mLegsPartsStore[i].load();
			mWeaponStore[i] = new FileKeyValueStore(dir_path, WEAPON_FILENAME_HEAD + String.valueOf(i) + FILENAME_TAIL);
			mWeaponStore[i].load();
			mChipStore[i] = new FileKeyValueStore(dir_path, CHIP_FILENAME_HEAD + String.valueOf(i) + FILENAME_TAIL);
			mChipStore[i].load();
			mMaterialStore[i] = new FileKeyValueStore(dir_path, MATERIAL_FILENAME_HEAD + String.valueOf(i) + FILENAME_TAIL);
			mMaterialStore[i].load();
			mMedalStore[i] = new FileKeyValueStore(dir_path, MEDAL_FILENAME_HEAD + String.valueOf(i) + FILENAME_TAIL);
			mMedalStore[i].load();
			mSeedStore[i] = new FileKeyValueStore(dir_path, SEED_FILENAME_HEAD + String.valueOf(i) + FILENAME_TAIL);
			mSeedStore[i].load();
		}
	}
	
	public void save() {
		mCardName.save();
		
		for(int i=0; i<CARD_MAX; i++) {
			mHeadPartsStore[i].save();
			mBodyPartsStore[i].save();
			mArmsPartsStore[i].save();
			mLegsPartsStore[i].save();
			mWeaponStore[i].save();
			mChipStore[i].save();
			mMaterialStore[i].save();
			mMedalStore[i].save();
			mSeedStore[i].save();
		}
	}
	
	public void clear() {
		mCardName.clear();
		
		for(int i=0; i<CARD_MAX; i++) {
			mCardName.set(CARDKEY_HEAD + String.valueOf(i), NO_CARD_DATA);
			clear(i);
		}
	}
	
	public void clear(int card_index) {
		mHeadPartsStore[card_index].clear();
		mBodyPartsStore[card_index].clear();
		mArmsPartsStore[card_index].clear();
		mLegsPartsStore[card_index].clear();
		mWeaponStore[card_index].clear();
		mChipStore[card_index].clear();
		mMaterialStore[card_index].clear();
		mMedalStore[card_index].clear();
		mSeedStore[card_index].clear();
	}
	
	public static BBNetDatabase getInstance() {
		return mInstance;
	}
	
	public void setCardNumber(int num) {
		mCardNumber = num;
	}
	
	public int getCardNumber() {
		return mCardNumber;
	}
	
	public void setCardName(String name) {
		mCardName.set(CARDKEY_HEAD + String.valueOf(mCardNumber), name);
	}
	
	public String getCardName() {
		return mCardName.get(CARDKEY_HEAD + String.valueOf(mCardNumber));
	}
	
	public ArrayList<String> getCardNames() {
		return mCardName.getValues();
	}
	
	/**
	 * 有効なカード名のみの一覧を取得する。
	 * @return
	 */
	public ArrayList<String> getValidCardNames() {
		ArrayList<String> list = new ArrayList<String>();
		
		int size = mCardName.size();
		for(int i=0; i<size; i++) {
			String name = mCardName.get(i);
			if(!name.equals(NO_CARD_DATA)) {
				list.add(name);
			}
		}
		
		return list;
	}

	public FileKeyValueStore getHeadParts() {
		return mHeadPartsStore[mCardNumber];
	}
	
	public FileKeyValueStore getBodyParts() {
		return mBodyPartsStore[mCardNumber];
	}

	public FileKeyValueStore getArmsParts() {
		return mArmsPartsStore[mCardNumber];
	}

	public FileKeyValueStore getLegsParts() {
		return mLegsPartsStore[mCardNumber];
	}
	
	public boolean existParts(BBData data) {
		boolean ret = false;
		String name = data.get("名称");
		
		if(data.existCategory(BBDataManager.BLUST_PARTS_HEAD)) {
			ret = mHeadPartsStore[mCardNumber].existKey(name);
		}
		if(data.existCategory(BBDataManager.BLUST_PARTS_BODY)) {
			ret = mBodyPartsStore[mCardNumber].existKey(name);
		}
		if(data.existCategory(BBDataManager.BLUST_PARTS_ARMS)) {
			ret = mArmsPartsStore[mCardNumber].existKey(name);
		}
		if(data.existCategory(BBDataManager.BLUST_PARTS_LEGS)) {
			ret = mLegsPartsStore[mCardNumber].existKey(name);
		}
		
		return ret;
	}

	public FileKeyValueStore getWeapons() {
		return mWeaponStore[mCardNumber];
	}
	
	public boolean existWeapon(String name) {
		return mWeaponStore[mCardNumber].existKey(name);
	}
	
	public FileKeyValueStore getChips() {
		return mChipStore[mCardNumber];
	}
	
	public FileKeyValueStore getMaterials() {
		return mMaterialStore[mCardNumber];
	}
	
	public FileKeyValueStore getMedals() {
		return mMedalStore[mCardNumber];
	}

	public FileKeyValueStore getSeeds() {
		return mSeedStore[mCardNumber];
	}
	
	public boolean existChip(String name) {
		return mChipStore[mCardNumber].existKey(name);
	}

	/**
	 * カードデータが空かどうかをチェックする。
	 * カード名のみの場合も空とする。
	 * 判定基準は頭パーツのデータが1つ以上あるかどうか。
	 * @return
	 */
	public boolean isEmpty() {
		if(mHeadPartsStore[mCardNumber].size()>0) {
			return false;
		}
		
		return true;
	}
	
}
