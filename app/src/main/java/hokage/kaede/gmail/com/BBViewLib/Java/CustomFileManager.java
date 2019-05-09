package hokage.kaede.gmail.com.BBViewLib.Java;

import java.io.InputStream;
import java.util.ArrayList;

import hokage.kaede.gmail.com.StandardLib.Java.FileIO;
import hokage.kaede.gmail.com.StandardLib.Java.FileKeyValueStore;
import hokage.kaede.gmail.com.StandardLib.Java.FileManager;

/**
 * アセンデータのファイル入出力を行うクラス
 */
public class CustomFileManager extends FileManager {

    private static CustomFileManager sInstance = null;

    // アセンファイルの接頭語・接尾語
    private static final String FILE_HEAD = "file";
    private static final String FILE_TAIL = ".dat";

    private static final String SAVEKEY_CHIP_COUNT = "チップ数";
    private static final String SAVEKEY_CHIP = "チップ";

    // ファイルの格納先のパス
    private String mFileDir;

    // アセンデータ
    private CustomData mCacheData;
    private FileKeyValueStore mDefaultFile;

    // 現在編集中のアセンデータを変更したかどうか
    private boolean mIsCacheChanged = false;

    /**
     * インスタンスの初期化を行う。
     * @param file_path 入出力を行うディレクトリ
     */
    private CustomFileManager(String file_path) {
        super(file_path, FILE_HEAD, FILE_TAIL);
        mFileDir = file_path;
    }

    /**
     * インスタンスを取得する。
     * @return インスタンスを取得する。生成されていない場合は新規に生成する。
     */
    public static CustomFileManager getInstance(String file_path) {
        if(sInstance == null) {
            sInstance = new CustomFileManager(file_path);
        }

        return sInstance;
    }

    /**
     * 初期アセンのデータと編集中のデータの初期化を行う。
     * @param is_defaultset デフォルトデータの入力ストリーム
     */
    public void init(InputStream is_defaultset) {

        // 初期アセンのデータを読み込む
        mDefaultFile = new FileKeyValueStore(mFileDir);
        mDefaultFile.setEncode(FileIO.ENCODE_UTF8);
        mDefaultFile.load(is_defaultset);

        // 編集中ファイルを読み込む
        if(BBViewSetting.IS_LOADING_LASTDATA) {
            FileKeyValueStore cache_file = new FileKeyValueStore(mFileDir);
            cache_file.load();
            mCacheData = read(cache_file);
        }
        else {
            mCacheData = read(mDefaultFile);
        }
    }

    /**
     * 現在編集中のアセンデータを返す。
     * @return 現在編集中のアセンデータ。
     */
    public CustomData getCacheData() {
        boolean speed_unit = BBViewSetting.IS_KM_PER_HOUR;
        boolean data_speed_unit = mCacheData.getSpeedUnit();

        if(speed_unit != data_speed_unit) {
            mCacheData.setSpeedUnit(speed_unit);
        }
        return mCacheData;
    }

    /**
     * 現在編集中のアセンデータを変更したかどうかを取得する。
     * @return 変更した場合はtrueを返し、変更していない場合はfalseを返す。
     */
    public boolean isCacheChanged() {
        return mIsCacheChanged;
    }

    /**
     * 現在編集中のアセンデータを保存する。
     */
    public void saveCacheData(CustomData custom_data) {
        mIsCacheChanged = true;
        mCacheData = custom_data;
        write(mCacheData);
    }

    /**
     * アセンデータをファイルから読み込む。
     * @param filetag ファイルタグ
     * @return 読み込んだカスタムデータ
     */
    public CustomData read(String filetag) {
        String filename = getFileName(filetag);
        FileKeyValueStore filedata = new FileKeyValueStore(mFileDir, filename);
        filedata.load();

        return read(filedata);
    }

    /**
     * 現在編集中のアセンのデータを初期アセンのデータにリセットする。
     */
    public void resetCacheData() {
        mIsCacheChanged = true;
        mCacheData = read(mDefaultFile);
        write(mCacheData);
    }

    /**
     * 初期アセンのアセンデータを取得する。
     * @return 初期アセンデータ
     */
    public CustomData getDefaultCustomData() {
        return read(mDefaultFile);
    }

    /**
     * 指定のファイルを読み込み、現在編集中のアセンデータに反映する。
     * @param filetag ファイルタグ
     */
    public void readFile(String filetag) {
        String filename = getFileName(filetag);
        FileKeyValueStore filedata = new FileKeyValueStore(mFileDir, filename);
        filedata.load();

        filedata.save(FileKeyValueStore.CACHE_NAME);
        mCacheData = read(filetag);
        mIsCacheChanged = false;
    }

    /**
     * 指定のファイルタグでファイルを新規作成する。
     * @param filetag ファイルタグ
     * @return ファイル作成に成功した場合はtrueを返し、失敗した場合はfalseを返す。
     */
    public boolean createFile(String filetag) {
        if(!create(filetag)) {
            return false;
        }

        String filename = getFileName(filetag);

        FileKeyValueStore file_data = new FileKeyValueStore(mFileDir);
        file_data.load();
        file_data.save(filename);
        mIsCacheChanged = false;

        return true;
    }

    /**
     * 指定のファイルタグでファイルを上書きする。
     * @param filetag ファイルタグ
     */
    public void updateFile(String filetag) {
        String filename = getFileName(filetag);

        FileKeyValueStore file_data = new FileKeyValueStore(mFileDir);
        file_data.load();
        file_data.save(filename);
        mIsCacheChanged = false;
    }

    //------------------------------------------------------------
    // 以降の関数はファイルへの直接入出力を実装
    //------------------------------------------------------------

    /**
     * アセンデータをファイルに書き込む。
     * @param data 対象のアセンデータ
     */
    private void write(CustomData data) {
        FileKeyValueStore file_data = new FileKeyValueStore(mFileDir);

        // パーツのデータを設定する。
        int parts_size = BBDataManager.BLUST_PARTS_LIST.length;
        for(int i=0; i<parts_size; i++) {
            String type = BBDataManager.BLUST_PARTS_LIST[i];
            file_data.set(type, data.getParts(type).get("名称"));
        }

        // 武器のデータを設定する。
        int blust_size = BBDataManager.BLUST_PARTS_LIST.length;
        int weapon_size = BBDataManager.WEAPON_TYPE_LIST.length;

        for(int i=0; i<blust_size; i++) {
            String blust_type = BBDataManager.BLUST_TYPE_LIST[i];

            for(int j=0; j<weapon_size; j++) {
                String weapon_type = BBDataManager.WEAPON_TYPE_LIST[j];
                file_data.set(blust_type + ":" + weapon_type, data.getWeapon(blust_type, weapon_type).get("名称"));
            }
        }

        // チップのデータを設定する。
        ArrayList<BBData> chip_list = data.getChips();
        int size = chip_list.size();

        for(int i=0; i<size; i++) {
            BBData chip_data = chip_list.get(i);
            String key = SAVEKEY_CHIP + String.format("%02d", i);
            String name = chip_data.get("名称");
            file_data.set(key, name);
        }

        file_data.set(SAVEKEY_CHIP_COUNT, String.valueOf(size));

        // 要請兵器のデータを設定する。
        file_data.set(BBDataManager.REQARM_STR, data.getReqArm().get("名称"));

        // ファイルに書き込む。
        file_data.save();
    }

    /**
     * アセンデータをファイルから読み込む
     * @param file_data ファイルデータ
     * @return 読み込んだカスタムデータ
     */
    private CustomData read(FileKeyValueStore file_data) {
        CustomData custom_data = new CustomData();
        BBDataManager data_mng = BBDataManager.getInstance();

        // 各パーツデータを読み込む
        String[] parts_type = BBDataManager.BLUST_PARTS_LIST;
        int parts_len = parts_type.length;

        for(int i=0; i<parts_len; i++) {
            String parts_name = file_data.get(parts_type[i]);
            BBData parts_data = data_mng.getPartsData(parts_name, parts_type[i]);

            // データが見つからなかった場合はデフォルトデータを設定する。
            if(parts_data.id == BBData.ID_ITEM_NOTHING) {
                parts_name = mDefaultFile.get(parts_type[i]);
                parts_data = data_mng.getPartsData(parts_name, parts_type[i]);
            }

            custom_data.setData(parts_data);
        }

        // 各武器データを読み込む
        String[] blust_type  = BBDataManager.BLUST_TYPE_LIST;
        String[] weapon_type = BBDataManager.WEAPON_TYPE_LIST;
        int blust_len  = blust_type.length;
        int weapon_len = weapon_type.length;

        for(int i=0; i<blust_len; i++) {
            for(int j=0; j<weapon_len; j++) {
                String key = blust_type[i] + ":" + weapon_type[j];
                String name = file_data.get(key);
                BBData weapon_data = data_mng.getWeaponData(name, blust_type[i], weapon_type[j]);

                // データが見つからなかった場合はデフォルトデータを設定する。
                if(weapon_data.id == BBData.ID_ITEM_NOTHING) {
                    name = mDefaultFile.get(key);
                    weapon_data = data_mng.getWeaponData(name, blust_type[i], weapon_type[j]);
                }

                custom_data.setData(weapon_data);
            }
        }

        // チップデータを読み込む
        int size = 0;
        try {
            size = Integer.valueOf(file_data.get(SAVEKEY_CHIP_COUNT));

        } catch(NumberFormatException e) {
            size = 0;
        }

        for(int i=0; i<size; i++) {
            String key = SAVEKEY_CHIP + String.format("%02d", i);
            String name = file_data.get(key);
            BBData chip_data = data_mng.getChipData(name);

            // チップデータが存在する場合、データを登録する。
            if(chip_data.id != BBData.ID_ITEM_NOTHING) {
                custom_data.addChip(chip_data);
            }
        }

        // 要請兵器のデータを読み込む
        String reqarm_name = file_data.get(BBDataManager.REQARM_STR);
        if(reqarm_name.equals(FileKeyValueStore.EMPTY_VALUE)) {
            reqarm_name = "バラム重機砲";  // デフォルト値
        }

        BBData reqarm_data = BBDataManager.getInstance().getReqArmData(reqarm_name);

        if(reqarm_data.id == BBData.ID_ITEM_NOTHING) {
            reqarm_name = mDefaultFile.get(BBDataManager.REQARM_STR);
            reqarm_data = BBDataManager.getInstance().getReqArmData(reqarm_name);
        }

        custom_data.setReqArm(reqarm_data);

        return custom_data;
    }

}
