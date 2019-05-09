package hokage.kaede.gmail.com.BBViewLib.Android.CustomLib;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BBDataAdapterItemProperty;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.FavoriteManager;
import hokage.kaede.gmail.com.BBViewLib.Java.SpecValues;

public class SelectPartsExpandableAdapter extends SelectBBDataExpandableAdapter {

    /**
     * 初期化を行う。
     * @param property 表示のためのデータと設定値
     */
    public SelectPartsExpandableAdapter(BBDataAdapterItemProperty property) {
        super(property);

        clear();
        int series_count = SpecValues.SERIES_NAME_LIST.size();

        for(int i=0; i<series_count; i++) {
            addGroup(SpecValues.SERIES_NAME_LIST.get(i));
        }

        addGroup(FavoriteManager.FAVORITE_CATEGORY_NAME);
    }

    /**
     * パーツデータを追加する。
     * @param item 追加するパーツ
     */
    public void addChild(BBData item) {
        String name = item.get("名称");

        // ブランド名と同じ位置に格納する。
        int size = getGroupCount();
        for(int i=0; i<size-1; i++) {
            String brand_name = getGroup(i);
            if(name.startsWith(brand_name)) {
                super.addChild(i, item);
            }
        }
    }

    /**
     * データのリストを追加する。
     * @param itemlist チップのリスト
     */
    public void addChildren(ArrayList<BBData> itemlist) {
        int size = itemlist.size();
        for(int i=0; i<size; i++) {
            addChild(itemlist.get(i));
        }
    }
}
