package hokage.kaede.gmail.com.StandardLib.Android;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * チェックボックス式のカテゴリ表示するリストのクラス
 */
public class CheckExpandableAdapter<T extends Object> extends NormalExpandableAdapter<T> {

    private ArrayList<ArrayList<Boolean>> mGroupCheckList;

    /**
     * リストを初期化する。
     */
    public CheckExpandableAdapter() {
        super();
        mGroupCheckList = new ArrayList<ArrayList<Boolean>>();
    }

    /**
     * アイテムのビューを取得する。
     * @param groupPosition グループの位置
     * @param childPosition アイテムの位置
     * @param isExpanded 開いている状態かどうか
     * @param convertView 元となるビュー
     * @param parent 親のView
     * @return アイテムのビュー
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        Object data = getChild(groupPosition, childPosition);
        CheckAdapterItem item_view;

        if(convertView == null) {
            item_view = new CheckAdapterItem(context);
            item_view.setTextSize(mTextSize);
            item_view.setTextColor(mTextColor);
            item_view.setBackGroundColor(mBackGroundColor);
        }
        else {
            item_view = (CheckAdapterItem)convertView;
        }

        if(isSelected(groupPosition, childPosition)) {
            item_view.setSelected(true);
        }
        else {
            item_view.setSelected(false);
        }

        item_view.setData(data);
        item_view.updateView();

        return item_view;
    }

    /**
     * グループを追加する。
     * @param name グループ名
     * @return 追加した位置。
     */
    @Override
    public void addGroup(String name) {
        super.addGroup(name);
        mGroupCheckList.add(new ArrayList<Boolean>());
    }

    /**
     * データを追加する。
     * @param groupPosition グループの位置
     * @param data 追加するデータ
     */
    @Override
    public void addChild(int groupPosition, T data) {
        super.addChild(groupPosition, data);

        int size = getGroupCount();
        if(groupPosition < 0 || groupPosition >= size) {
            return;
        }

        mGroupCheckList.get(groupPosition).add(false);
    }

    /**
     * 項目を削除する。
     * @param groupPosition グループの位置
     * @param childPosotion データの位置
     */
    @Override
    public void removeChild(int groupPosition, int childPosotion) {
        super.removeChild(groupPosition, childPosotion);

        int size = getGroupCount();
        if(groupPosition < 0 || groupPosition >= size) {
            return;
        }

        mGroupCheckList.get(groupPosition).remove(childPosotion);
    }

    /**
     * 項目を全削除する。
     */
    @Override
    public void clear() {
        super.clear();

        int size = mGroupCheckList.size();
        for(int i=0; i<size; i++) {
            mGroupCheckList.get(i).clear();
        }

        mGroupCheckList.clear();
    }

    /**
     * 全てのグループから項目を全削除する。
     * グループは削除しない。
     */
    @Override
    public void clearChildrenAll() {
        super.clearChildrenAll();

        int size = mGroupCheckList.size();
        for(int i=0; i<size; i++) {
            mGroupCheckList.get(i).clear();
        }
    }

    /**
     * 指定のアイテムを選択状態にする。
     * @param groupPosition グループの位置
     * @param childPosotion データの位置
     */
    public void setSelect(int groupPosition, int childPosotion, boolean is_select) {
        try {
            ArrayList<Boolean> checklist = mGroupCheckList.get(groupPosition);
            checklist.set(childPosotion, is_select);
        } catch(Exception e) {
            // Do Nothing
        }
    }

    /**
     * 指定位置のアイテムが選択中かどうかを取得する。
     * @param groupPosition グループの位置
     * @param childPosotion データの位置
     * @return 選択中の場合はtrueを返し、非選択の場合はfalseを返す。
     */
    public boolean isSelected(int groupPosition, int childPosotion) {
        boolean ret;

        try {
            ArrayList<Boolean> checklist = mGroupCheckList.get(groupPosition);
            ret = checklist.get(childPosotion);
        } catch(Exception e) {
            ret = false;
        }

        return ret;
    }

    /**
     * 選択状態を全てクリアする。
     */
    public void clearSelectSts() {
        int group_size = mGroupCheckList.size();

        for(int i=0; i<group_size; i++) {
            ArrayList<Boolean> child_list = mGroupCheckList.get(i);
            int child_size = child_list.size();

            for(int j=0; j<child_size; j++) {
                child_list.set(j, false);
            }
        }
    }
}
