package hokage.kaede.gmail.com.StandardLib.Android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 複数選択式のリストを実現するクラス
 */
public class MultiSelectionAdsapter<T extends Object> extends BaseAdapter implements IMultiSelectionAdapter {

    // 管理するデータのリスト
    private ArrayList<T> mList;
    private ArrayList<Boolean> mSelectionList;

    // 存在しない色の場合の設定
    protected static final int NOTHING_COLOR = -1;

    // フォントの設定
    protected int mTextSize = SettingManager.FLAG_TEXTSIZE_NORMAL;
    protected int mTextColor = NOTHING_COLOR;
    protected int mBackGroundColor = NOTHING_COLOR;

    /**
     * 初期化を行う。
     */
    public MultiSelectionAdsapter() {
        mList = new ArrayList<T>();
        mSelectionList = new ArrayList<Boolean>();
    }

    /**
     * リストを設定する。
     * @param list 設定するリスト
     */
    public void setList(ArrayList<T> list) {
        mList = list;
        mSelectionList.clear();
        int count = list.size();
        for(int i=0; i<count; i++) {
            mSelectionList.add(false);
        }
    }

    /**
     * 項目を追加する。
     * @param item 追加する項目
     */
    public void add(T item) {
        mList.add(item);
        mSelectionList.add(false);
    }

    /**
     * 項目を削除する。
     * @param position 削除するアイテムの位置
     */
    public void remove(int position) {
        mList.remove(position);
        mSelectionList.remove(position);
    }

    /**
     * 項目を削除する。
     * @param data 削除対象のデータ
     */
    public void remove(T data) {
        mList.remove(data);
    }

    /**
     * 項目を全削除する。
     */
    public void clear() {
        mList.clear();
        mSelectionList.clear();
    }

    /**
     * 指定位置のアイテムを取得する。
     * @param position 指定位置
     * @return 指定位置のアイテム
     */
    public T get(int position) {
        if(position < 0 || position >= mList.size()) {
            return null;
        }

        return mList.get(position);
    }

    /**
     * データの位置を取得する。
     * @return データの位置
     */
    public int getIndex(Object item) {
        return mList.indexOf(item);
    }

    /**
     * リストの数を取得する。
     * @return リストの数
     */
    @Override
    public int getCount() {
        return mList.size();
    }

    /**
     * リストのデータを取得する。
     * @param position 取得する位置
     * @return 位置に応じたデータ
     */
    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    /**
     * リストのデータIDを取得する。
     * @param position 取得する位置
     * @return 未使用のため、0を返す。
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 指定のアイテムを選択状態にする。
     * @param position 選択するアイテムの位置
     * @param is_select 選択状態かどうか
     */
    @Override
    public void setSelect(int position, boolean is_select) {
        mSelectionList.set(position, is_select);
    }

    /**
     * 指定位置のアイテムが選択中かどうかを取得する。
     * @param position 指定位置
     * @return 選択中の場合はtrueを返し、非選択の場合はfalseを返す。
     */
    @Override
    public boolean isSelected(int position) {
        return mSelectionList.get(position);
    }

    /**
     * 選択状態のアイテムを取得する。
     * 外部処理により、選択状態を示すリストとデータ数が不一致の場合、全て未選択状態として扱う。
     * @return 選択状態のアイテム
     */
    @Override
    public ArrayList<T> getSelectedList() {
        ArrayList<T> selected_item_list = new ArrayList<T>();

        int item_count = getCount();
        int selection_count = mSelectionList.size();

        if(item_count != selection_count) {
            return selected_item_list;
        }

        for(int i=0; i<item_count; i++) {
            if(mSelectionList.get(i)) {
                selected_item_list.add(mList.get(i));
            }
        }

        return selected_item_list;
    }

    /**
     * 指定位置のViewを返す。
     * @param position 取得する位置
     * @param view 取得するView
     * @param viewGroup 取得するViewGroup
     * @return viewのインスタンスを返す。
     */
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        Object data = get(position);
        SelectionAdapterItem item_view;

        if(view == null) {
            item_view = new SelectionAdapterItem(context);
            item_view.setTextSize(mTextSize);
            item_view.setTextColor(mTextColor);
            item_view.setBackGroundColor(mBackGroundColor);
        }
        else {
            item_view = (SelectionAdapterItem)view;
        }

        if(mSelectionList.get(position)) {
            item_view.setSelected(true);
        }
        else {
            item_view.setSelected(false);
        }

        item_view.setData(data);
        item_view.updateView();

        return item_view;
    }
}
