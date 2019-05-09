package hokage.kaede.gmail.com.BBViewLib.Java;

/**
 * スペック値から比較に使用する値を算出するクラス
 */
public class BBDataComparatorItem {

    public static int TYPE_NUMBER   = 0;   // 比較する値が数値
    public static int TYPE_SPECCHAR = 1;   // 比較する値が評価値
    public static int TYPE_COMPNG   = 2;   // 比較不可

    private int mCompType = TYPE_COMPNG;

    private static double COMP_NG_NUM = Double.MIN_VALUE;
    private double mCompDataNum = COMP_NG_NUM;

    /**
     * 初期化する。
     * @param data パーツまたは武器のデータ
     * @param target_key ターゲットのスペック名
     * @param is_type_b スイッチ武器のタイプB表示を行うかどうか。
     */
    public BBDataComparatorItem(BBData data, String target_key, boolean is_type_b) {
        BBData target_data = data;

        // スイッチ武器の判定対象判別処理
        if(is_type_b && data.getTypeB() != null) {
            target_data = data.getTypeB();
        }

        String value_str = target_data.get(target_key);

        if(target_key.equals("威力")) {
            mCompDataNum = target_data.getOneShotPower();
            mCompType = TYPE_NUMBER;
        }
        else if(BBDataComparator.isPointKey(target_key)) {
            mCompDataNum = SpecValues.getSpecValue(target_data, target_key);
            mCompType = TYPE_NUMBER;
        }
        else {
            String parts_name = target_data.get("名称");
            init(value_str, target_key, parts_name);
        }
    }

    /**
     * 初期化する。
     * @param data スペック値
     * @param target_key ターゲットのスペック名
     */
    public BBDataComparatorItem(String data, String target_key) {
        init(data, target_key, "");
    }

    /**
     * 初期化する。
     * @param data スペック値
     */
    public BBDataComparatorItem(double data) {
        mCompType = TYPE_NUMBER;
        mCompDataNum = data;
    }

    /**
     * 初期化する。
     * スペック値の場合は数値をキャストする。評価値の場合はIndex値に置き換える。左記以外の場合は下限値を入れる。
     * @param data スペック値
     * @param target_key ターゲットのスペック名
     * @param parts_name パーツ名 (重量耐性値取得用)
     */
    public void init(String data, String target_key, String parts_name) {
        double value = SpecValues.getSpecValue(data, target_key, parts_name);

        if(value == SpecValues.ERROR_VALUE) {
            int len = BBDataManager.SPEC_POINT.length;
            for(int i=0; i<len; i++) {
                if(BBDataManager.SPEC_POINT[i].equals(data)) {
                    mCompDataNum = len - i;
                    mCompType = TYPE_SPECCHAR;
                    return;
                }
            }

            mCompDataNum = COMP_NG_NUM;
            mCompType = TYPE_COMPNG;
        }
        else {
            mCompDataNum = value;
            mCompType = TYPE_NUMBER;
        }
    }

    /**
     * 比較する値を取得する。
     * @return 比較する値。
     */
    public double getCompValue() {
        return mCompDataNum;
    }

    /**
     * 比較する対象の種類を取得する。
     * @return 比較種別。
     */
    public int getCompType() {
        return mCompType;
    }
}
