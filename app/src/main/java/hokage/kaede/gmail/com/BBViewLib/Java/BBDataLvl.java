package hokage.kaede.gmail.com.BBViewLib.Java;

import hokage.kaede.gmail.com.StandardLib.Java.KVCStore;

/**
 * PS4版の強化情報を反映するデータのベースクラス
 */
public abstract class BBDataLvl extends KVCStore {

    public static final int ID_ITEM_NOTHING = -1;

    public static final String STR_VALUE_NOTHING = "情報なし";
    public static final double NUM_VALUE_NOTHING = Double.MIN_NORMAL;

    // 強化段階
    private int mLevel = 0;

    private static String STR_LEVEL_SPEC = "強化対象";
    private static String STR_LEVEL_VALUE = "強化値";

    public static int MIN_LEVEL = 1;
    public static int MAX_LEVEL = 3;

    /**
     * 強化段階を設定する。
     * @param level 強化段階
     */
    public void setLevel(int level) {
        mLevel = level;
    }

    /**
     * データを取得する。
     * @param key 取得するスペックのキー
     * @return 取得したデータ
     */
    public String get(String key) {
        String ret = "";

        if(isLevelLabel(key)) {
            return STR_VALUE_NOTHING;
        }

        for(int i=MIN_LEVEL; i<=mLevel; i++) {
            String spec = super.get(STR_LEVEL_SPEC + String.valueOf(i));
            String value = super.get(STR_LEVEL_VALUE + String.valueOf(i));

            if(spec.equals(key)) {
                return value;
            }
        }

        return super.get(key);
    }

    /**
     * 指定したキーが強化に関する項目かどうかを判定する。
     * @param key 調べるキー
     * @return 強化に関する項目の場合はtrueを返し、異なる場合はfalseを返す。
     */
    private boolean isLevelLabel(String key) {

        for (int i=MIN_LEVEL; i<=MAX_LEVEL; i++) {
            String spec_key = STR_LEVEL_SPEC + String.valueOf(i);
            String value_key = STR_LEVEL_VALUE + String.valueOf(i);

            if (key.equals(spec_key) || key.equals(value_key)) {
                return true;
            }
        }

        return false;
    }

    /**
     * データを取得する。
     * @param key 取得するスペックのキー
     * @param level 強化段階
     * @return 取得したデータ
     */
    public String get(String key, int level) {
        String ret = STR_VALUE_NOTHING;
        int backup_level = mLevel;
        mLevel = level;

        if(super.existKey(key)) {
            ret = get(key);
        }
        else {
            double calc_value = getCalcValue(key);
            if(calc_value > NUM_VALUE_NOTHING) {
                ret = String.valueOf(calc_value);
            }
        }

        mLevel = backup_level;

        return ret;
    }


    /**
     * 算出値データを取得する。
     */
    public abstract double getCalcValue(String key);
}
