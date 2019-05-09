package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import android.view.View;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.FavoriteManager;
import hokage.kaede.gmail.com.StandardLib.Android.CheckExpandableAdapter;
import hokage.kaede.gmail.com.StandardLib.Java.FileArrayList;

/**
 * チェックボックス式のカテゴリ表示のリストのクラス
 */
public class BBDataCheckExpandableAdapter extends CheckExpandableAdapter<BBData> {

    private BBDataAdapterItemProperty mProperty;

    private FileArrayList mFavStore;

    /**
     * 初期化を行う。
     * @param property 表示のためのデータと設定値
     */
    public BBDataCheckExpandableAdapter(BBDataAdapterItemProperty property) {
        mProperty = property;
    }

    /**
     * プロパティを取得する。
     * @return プロパティ
     */
    public BBDataAdapterItemProperty getProperty() {
        return mProperty;
    }

    /**
     * お気に入りリストのストアを設定する。
     * @param store
     */
    public void setFavStore(FileArrayList store) {
        mFavStore = store;
    }

    /**
     * データを追加する。
     * @param groupPosition グループの位置
     * @param item 追加するデータ
     */
    @Override
    public void addChild(int groupPosition, BBData item) {
        super.addChild(groupPosition, item);

        int size = getGroupCount();
        if(groupPosition == size - 1) {
            return;
        }

        // お気に入りリストに格納されているアイテムを追加する。
        String name = item.get("名称");
        if(mFavStore != null && mFavStore.exist(name)) {
            super.addChild(size - 1, item);
        }
    }

}
