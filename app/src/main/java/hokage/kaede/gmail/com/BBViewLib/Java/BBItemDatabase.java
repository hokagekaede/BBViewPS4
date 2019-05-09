package hokage.kaede.gmail.com.BBViewLib.Java;

import java.util.ArrayList;

import hokage.kaede.gmail.com.StandardLib.Java.FileKeyValueStore;

public class BBItemDatabase {

    private static final String HEADPARTS_FILENAME = "bbps4_head_parts.dat";
    private static final String BODYPARTS_FILENAME = "bbps4_body_parts.dat";
    private static final String ARMSPARTS_FILENAME = "bbps4_arms_parts.dat";
    private static final String LEGSPARTS_FILENAME = "bbps4_legs_parts.dat";
    private static final String WEAPON_FILENAME = "bbps4_weapon.dat";
    private static final String CHIP_FILENAME = "bbps4_chip.dat";

    public static int ITEM_NOT_HAVING = -2;
    public static int ITEM_HAVING = -1;
    public static int ITEM_LEVEL0 = 0;
    public static int ITEM_LEVEL1 = 1;
    public static int ITEM_LEVEL2 = 2;
    public static int ITEM_LEVEL3 = 3;

    public static String STR_NOT_HAVING = "未所持";
    public static String STR_HAVING = "所持";
    public static String STR_LEVEL0 = "強化無し";
    public static String STR_LEVEL1 = "強化1";
    public static String STR_LEVEL2 = "強化2";
    public static String STR_LEVEL3 = "強化3";

    private static BBItemDatabase mInstance = new BBItemDatabase();

    private FileKeyValueStore mHeadPartsStore;
    private FileKeyValueStore mBodyPartsStore;
    private FileKeyValueStore mArmsPartsStore;
    private FileKeyValueStore mLegsPartsStore;
    private FileKeyValueStore mWeaponStore;
    private FileKeyValueStore mChipStore;

    private BBItemDatabase() {
        // Do Nothing
    }

    public static BBItemDatabase getInstance() {
        return mInstance;
    }

    public void init(String dir_path) {
        mHeadPartsStore = new FileKeyValueStore(dir_path, HEADPARTS_FILENAME);
        mBodyPartsStore = new FileKeyValueStore(dir_path, BODYPARTS_FILENAME);
        mArmsPartsStore = new FileKeyValueStore(dir_path, ARMSPARTS_FILENAME);
        mLegsPartsStore = new FileKeyValueStore(dir_path, LEGSPARTS_FILENAME);
        mWeaponStore = new FileKeyValueStore(dir_path, WEAPON_FILENAME);
        mChipStore = new FileKeyValueStore(dir_path, CHIP_FILENAME);

        mHeadPartsStore.load();
        mBodyPartsStore.load();
        mArmsPartsStore.load();
        mLegsPartsStore.load();
        mWeaponStore.load();
        mChipStore.load();
    }

    public void load(ArrayList<BBData> list) {
        int size = list.size();

        for(int i=0; i<size; i++) {
            BBData target_item = list.get(i);

            if (BBDataManager.isParts(target_item)) {
                int level = getPartsLevel(target_item);
                setPartsLevel(target_item, level);
            }
            else if (BBDataManager.isWeapon(target_item)) {
                int level = getWeaponLevel(target_item);
                setWeaponLevel(target_item, level);
            }
            else if (target_item.existCategory(BBDataManager.CHIP_STR)) {
                int owner = getChipInfo(target_item);
                setChipInfo(target_item, owner);
            }
        }
    }

    public void setPartsLevel(BBData data, int level) {

        if(data.existCategory(BBDataManager.BLUST_PARTS_HEAD)) {
            setData(mHeadPartsStore, data, level);
        }
        else if(data.existCategory(BBDataManager.BLUST_PARTS_BODY)) {
            setData(mBodyPartsStore, data, level);
        }
        else if(data.existCategory(BBDataManager.BLUST_PARTS_ARMS)) {
            setData(mArmsPartsStore, data, level);
        }
        else if(data.existCategory(BBDataManager.BLUST_PARTS_LEGS)) {
            setData(mLegsPartsStore, data, level);
        }
    }

    public void setWeaponLevel(BBData data, int level) {
        setData(mWeaponStore, data, level);
    }

    private void setData(FileKeyValueStore store, BBData data, int level) {
        String name = data.get("名称");

        if(level == ITEM_LEVEL0) {
            store.set(name, STR_LEVEL0);
            data.setLevel(0);
        }
        else if(level == ITEM_LEVEL1) {
            store.set(name, STR_LEVEL1);
            data.setLevel(1);
        }
        else if(level == ITEM_LEVEL2) {
            store.set(name, STR_LEVEL2);
            data.setLevel(2);
        }
        else if(level == ITEM_LEVEL3) {
            store.set(name, STR_LEVEL3);
            data.setLevel(3);
        }
        else {
            store.set(name, STR_NOT_HAVING);
            data.setLevel(0);
        }

        store.save();
    }

    public void setChipInfo(BBData data, int level) {
        String name = data.get("名称");

        if(level == ITEM_HAVING) {
            mChipStore.set(name, STR_HAVING);
            data.setLevel(1);
        }
        else {
            mChipStore.set(name, STR_NOT_HAVING);
            data.setLevel(0);
        }
    }



    public int getPartsLevel(BBData data) {
        int ret = ITEM_NOT_HAVING;
        String name = data.get("名称");

        if(data.existCategory(BBDataManager.BLUST_PARTS_HEAD)) {
            return getData(mHeadPartsStore, name);
        }
        else if(data.existCategory(BBDataManager.BLUST_PARTS_BODY)) {
            return getData(mBodyPartsStore, name);
        }
        else if(data.existCategory(BBDataManager.BLUST_PARTS_ARMS)) {
            return getData(mArmsPartsStore, name);
        }
        else if(data.existCategory(BBDataManager.BLUST_PARTS_LEGS)) {
            return getData(mLegsPartsStore, name);
        }

        return ret;
    }

    public int getWeaponLevel(BBData data) {
        String name = data.get("名称");
        return getData(mWeaponStore, name);
    }

    private int getData(FileKeyValueStore store, String name) {
        String level = store.get(name);

        if(level.equals(STR_LEVEL0)) {
            return ITEM_LEVEL0;
        }
        else if(level.equals(STR_LEVEL1)) {
            return ITEM_LEVEL1;
        }
        else if(level.equals(STR_LEVEL2)) {
            return ITEM_LEVEL2;
        }
        else if(level.equals(STR_LEVEL3)) {
            return ITEM_LEVEL3;
        }
        else {
            return ITEM_NOT_HAVING;
        }
    }

    public int getChipInfo(BBData data) {
        String name = data.get("名称");
        String level = mChipStore.get(name);

        if(level.equals(STR_HAVING)) {
            return ITEM_HAVING;
        }
        else {
            return ITEM_NOT_HAVING;
        }
    }



    public String getPartsLevelText(BBData data) {
        String name = data.get("名称");
        String level_text = BBItemDatabase.STR_NOT_HAVING;

        if(data.existCategory(BBDataManager.BLUST_PARTS_HEAD)) {
            level_text = mHeadPartsStore.get(name);
        }
        else if(data.existCategory(BBDataManager.BLUST_PARTS_BODY)) {
            level_text = mBodyPartsStore.get(name);
        }
        else if(data.existCategory(BBDataManager.BLUST_PARTS_ARMS)) {
            level_text = mArmsPartsStore.get(name);
        }
        else if(data.existCategory(BBDataManager.BLUST_PARTS_LEGS)) {
            level_text = mLegsPartsStore.get(name);
        }

        if(level_text.equals(BBData.EMPTY_VALUE)) {
            level_text = BBItemDatabase.STR_NOT_HAVING;
        }

        return level_text;
    }

    public String getWeaponLevelText(BBData data) {
        String name = data.get("名称");
        String level_text = mWeaponStore.get(name);

        if(level_text.equals(BBData.EMPTY_VALUE)) {
            level_text = BBItemDatabase.STR_NOT_HAVING;
        }

        return level_text;
    }

    public String getChipInfoText(BBData data) {
        String name = data.get("名称");
        String level_text = mChipStore.get(name);

        if(level_text.equals(BBData.EMPTY_VALUE)) {
            level_text = BBItemDatabase.STR_NOT_HAVING;
        }

        return level_text;
    }

}
