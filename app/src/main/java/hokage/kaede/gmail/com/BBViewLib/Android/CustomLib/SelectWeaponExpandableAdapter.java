package hokage.kaede.gmail.com.BBViewLib.Android.CustomLib;

import java.util.ArrayList;

import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BBDataAdapterItemProperty;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.FavoriteManager;
import hokage.kaede.gmail.com.BBViewLib.Java.SpecValues;

/**
 * パーツ選択画面のリストを実現するクラス
 */
public class SelectWeaponExpandableAdapter extends SelectBBDataExpandableAdapter {

    /**
     * 初期化を行う。
     * @param property 表示のためのデータと設定値
     * @param blust_type 兵装名
     * @param weapon_type 武器の種類名
     */
    public SelectWeaponExpandableAdapter(BBDataAdapterItemProperty property, String blust_type, String weapon_type) {
        super(property);

        clear();
        ArrayList<String> series_list = SpecValues.getWeaponSeiresList(blust_type, weapon_type);
        int series_count = series_list.size();

        for(int i=0; i<series_count; i++) {
            addGroup(series_list.get(i));
        }

        addGroup(FavoriteManager.FAVORITE_CATEGORY_NAME);
    }

    /**
     * 武器データを追加する。
     * @param item 追加する武器
     */
    public void addChild(BBData item) {
        // ブランド名と同じ位置に格納する。
        int size = getGroupCount();
        for(int i=0; i<size-1; i++) {
            String series_name = getGroup(i);

            if(item.isSwitchWeapon()) {
                series_name = series_name + "(タイプA)";
            }

            if(item.existCategory(series_name)) {
                super.addChild(i, item);
            }
        }
    }

    /**
     * データのリストを追加する。
     * @param itemlist チップのリスト
     */
    @Override
    public void addChildren(ArrayList<BBData> itemlist) {
        int size = itemlist.size();
        for(int i=0; i<size; i++) {
            addChild(itemlist.get(i));
        }
    }
}
