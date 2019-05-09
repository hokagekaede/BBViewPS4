package hokage.kaede.gmail.com.BBViewLib.Android.PurchaseLib;

import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BBDataAdapterItemProperty;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BBDataMultiSelectionAdapter;

/**
 * 「購入リスト追加」画面のアイテム一覧を生成するクラス。
 */
public class PurchaseAdapter extends BBDataMultiSelectionAdapter {

    /**
     * 初期化を行う。
     *
     * @param property 表示のためのデータと設定値
     */
    public PurchaseAdapter(BBDataAdapterItemProperty property) {
        super(property);
    }
}
