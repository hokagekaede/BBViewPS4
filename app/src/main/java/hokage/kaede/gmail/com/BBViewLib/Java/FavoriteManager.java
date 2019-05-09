package hokage.kaede.gmail.com.BBViewLib.Java;

import hokage.kaede.gmail.com.StandardLib.Java.FileArrayList;

/**
 * パーツ武器選択画面およびチップ画面の「お気に入り」グループを管理するクラス。
 */
public class FavoriteManager {

	public static final String FAVORITE_CATEGORY_NAME = "お気に入り";
	
	// パーツのお気に入りファイル
	private static final String HEAD_FILENAME   = "favorite_head.txt";
	private static final String BODY_FILENAME   = "favorite_body.txt";
	private static final String ARMS_FILENAME   = "favorite_arms.txt";
	private static final String LEGS_FILENAME   = "favorite_legs.txt";

	// 武器のお気に入りファイル
	// 装備種別内で同名の武器は存在しないため、ファイルは兵装単位で管理する。
	private static final String ASSALT_FILENAME  = "favorite_assalt.txt";
	private static final String HEAVY_FILENAME   = "favorite_heavy.txt";
	private static final String SNIPER_FILENAME  = "favorite_sniper.txt";
	private static final String SUPPORT_FILENAME = "favorite_support.txt";

	// チップのお気に入りファイル
	private static final String CHIP_FILENAME   = "favorite_chips.txt";
	
	public static FileArrayList sHeadStore;
	public static FileArrayList sBodyStore;
	public static FileArrayList sArmsStore;
	public static FileArrayList sLegsStore;
	
	public static FileArrayList sAssaltStore;
	public static FileArrayList sHeavyStore;
	public static FileArrayList sSniperStore;
	public static FileArrayList sSupportStore;

	public static FileArrayList sChipStore;
	
	public static void init(String dir) {
		sHeadStore = new FileArrayList(dir, HEAD_FILENAME);
		sHeadStore.load();
		
		sBodyStore = new FileArrayList(dir, BODY_FILENAME);
		sBodyStore.load();
		
		sArmsStore = new FileArrayList(dir, ARMS_FILENAME);
		sArmsStore.load();
		
		sLegsStore = new FileArrayList(dir, LEGS_FILENAME);
		sLegsStore.load();

		sAssaltStore = new FileArrayList(dir, ASSALT_FILENAME);
		sAssaltStore.load();

		sHeavyStore = new FileArrayList(dir, HEAVY_FILENAME);
		sHeavyStore.load();

		sSniperStore = new FileArrayList(dir, SNIPER_FILENAME);
		sSniperStore.load();

		sSupportStore = new FileArrayList(dir, SUPPORT_FILENAME);
		sSupportStore.load();
		
		sChipStore = new FileArrayList(dir, CHIP_FILENAME);
		sChipStore.load();

	}
	
	public static FileArrayList getStore(String type) {
		
		if(type.equals(BBDataManager.BLUST_PARTS_HEAD)) {
			return sHeadStore;
		}
		else if(type.equals(BBDataManager.BLUST_PARTS_BODY)) {
			return sBodyStore;
		}
		else if(type.equals(BBDataManager.BLUST_PARTS_ARMS)) {
			return sArmsStore;
		}
		else if(type.equals(BBDataManager.BLUST_PARTS_LEGS)) {
			return sLegsStore;
		}
		else if(type.equals(BBDataManager.BLUST_TYPE_ASSALT)) {
			return sAssaltStore;
		}
		else if(type.equals(BBDataManager.BLUST_TYPE_HEAVY)) {
			return sHeavyStore;
		}
		else if(type.equals(BBDataManager.BLUST_TYPE_SNIPER)) {
			return sSniperStore;
		}
		else if(type.equals(BBDataManager.BLUST_TYPE_SUPPORT)) {
			return sSupportStore;
		}
		else if(type.equals(BBDataManager.CHIP_STR)) {
			return sChipStore;
		}
		
		return null;
	}
}
