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
 * カテゴリ表示するリストのクラス
 * @param <T> リストで使用するデータ型
 */
public class NormalExpandableAdapter<T extends Object> extends BaseExpandableListAdapter {

    private static final int GROUP_ID_BASE = 1000;

    private ArrayList<String> mGroupNameList;
    private ArrayList<ArrayList<T>> mGroupDataList;

    // 存在しない色の場合の設定
    protected static final int NOTHING_COLOR = -1;

    // フォントの設定
    protected int mTextSize = SettingManager.FLAG_TEXTSIZE_NORMAL;
    protected int mTextColor = NOTHING_COLOR;
    protected int mBackGroundColor = NOTHING_COLOR;
    protected int mCategoryTextColor = NOTHING_COLOR;
    protected int mCategoryBackGroundColor = NOTHING_COLOR;

    /**
     * リストを初期化する。
     */
    public NormalExpandableAdapter() {
        mGroupNameList = new ArrayList<String>();
        mGroupDataList = new ArrayList<ArrayList<T>>();
    }

    /**
     * グループの数を取得する。
     * @return グループの数
     */
    @Override
    public int getGroupCount() {
        return mGroupDataList.size();
    }

    /**
     * データの数を取得する。
     * @param groupPosition グループの位置
     * @return データの数
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        int size = mGroupDataList.size();
        if(groupPosition < 0 || groupPosition >= size) {
            return -1;
        }

        return mGroupDataList.get(groupPosition).size();
    }

    /**
     * グループを取得する。
     * @return グループ名
     */
    @Override
    public String getGroup(int groupPosition) {
        return mGroupNameList.get(groupPosition);
    }

    /**
     * データを取得する。
     * @param groupPosition グループの位置
     * @param childPosition データの位置
     * @return データ
     */
    @Override
    public T getChild(int groupPosition, int childPosition) {
        int size = mGroupDataList.size();
        if(groupPosition < 0 || groupPosition >= size) {
            return null;
        }

        return mGroupDataList.get(groupPosition).get(childPosition);
    }

    /**
     * グループのIDを取得する。
     * @param groupPosition グループの位置
     * @return ID値
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition * GROUP_ID_BASE;
    }

    /**
     * データのIDを取得する。
     * @param groupPosition グループの位置
     * @param childPosition データの位置
     * @return ID値
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * GROUP_ID_BASE + childPosition;
    }

    /**
     * アイテムIDが一定かどうか。
     * @return false固定。(不確定固定)
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * グループのビューを取得する。
     * @param groupPosition グループの位置
     * @param isExpanded 開いている状態かどうか
     * @param convertView 元となるビュー
     * @param parent 親のView
     * @return グループのビュー
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Context context = parent.getContext();
        TextView text_view = (TextView)convertView;

        if(text_view == null) {
            text_view = new TextView(context);
        }

        text_view.setText(mGroupNameList.get(groupPosition));
        text_view.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        text_view.setTextSize(SettingManager.getTextSize(context, mTextSize));
        text_view.setPadding(0, 15, 0, 15);
        text_view.setTextColor(mCategoryTextColor);
        text_view.setBackgroundColor(mCategoryBackGroundColor);
        text_view.setLayoutParams(lp);

        return text_view;
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
        Object data = mGroupDataList.get(childPosition);
        NormalAdapterItem item_view;

        if(convertView == null) {
            item_view = new NormalAdapterItem(context);
            item_view.setTextSize(mTextSize);
            item_view.setTextColor(mTextColor);
            item_view.setBackGroundColor(mBackGroundColor);
        }
        else {
            item_view = (NormalAdapterItem)convertView;
        }

        item_view.setData(data);
        item_view.updateView();

        return item_view;
    }

    /**
     * データが選択可能かどうか。
     * @return trueを返す。
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * グループを追加する。
     * @param name グループ名
     * @return 追加した位置。
     */
    public void addGroup(String name) {
        mGroupNameList.add(name);
        mGroupDataList.add(new ArrayList<T>());
    }

    /**
     * グループの位置を取得する
     * @param name グループ名
     * @return グループの位置を返す。グループが存在しない場合は-1を返す。
     */
    public int indexOfGroup(String name) {
        return mGroupNameList.indexOf(name);
    }

    /**
     * データを追加する。
     * @param groupPosition グループの位置
     * @param data 追加するデータ
     */
    public void addChild(int groupPosition, T data) {

        int size = mGroupDataList.size();
        if(groupPosition < 0 || groupPosition >= size) {
            return;
        }

        mGroupDataList.get(groupPosition).add(data);
    }

    /**
     * データの位置を取得する。
     * @param groupPosition グループの位置
     * @param data 対象のデータ
     * @return データの位置を返す。データが存在しない場合は-1を返す。
     */
    public int indexOfChild(int groupPosition, T data) {
        int size = mGroupDataList.size();
        if(groupPosition < 0 || groupPosition >= size) {
            return -1;
        }

        return mGroupDataList.get(groupPosition).indexOf(data);
    }

    /**
     * 項目を削除する。
     * @param groupPosition グループの位置
     * @param childPosotion データの位置
     */
    public void removeChild(int groupPosition, int childPosotion) {
        int size = mGroupDataList.size();
        if(groupPosition < 0 || groupPosition >= size) {
            return;
        }

        mGroupDataList.get(groupPosition).remove(childPosotion);
    }

    /**
     * 項目を全削除する。
     */
    public void clear() {
        int size = mGroupDataList.size();
        for(int i=0; i<size; i++) {
            mGroupDataList.get(i).clear();
        }

        mGroupNameList.clear();
        mGroupDataList.clear();
    }

    /**
     * 全てのグループから項目を全削除する。
     * グループは削除しない。
     */
    public void clearChildrenAll() {
        int size = mGroupDataList.size();
        for(int i=0; i<size; i++) {
            mGroupDataList.get(i).clear();
        }
    }

    /**
     * ID値からグループ位置を取得する。
     * @param id ID値
     * @return グループ位置
     */
    protected int getGroupPositionFromID(int id) {
        return id / GROUP_ID_BASE;
    }

    /**
     * ID値からデータの位置を取得する。
     * @param id ID値
     * @return データの位置
     */
    protected int getChildPositionFromID(int id) {
        return id % GROUP_ID_BASE;
    }

    /**
     * テキストサイズを設定する。
     * @param text_size テキストサイズ
     */
    public void setTextSize(int text_size) {
        mTextSize = text_size;
    }

    /**
     * テキストカラーを設定する。
     * @param color カラー値
     */
    public void setTextColor(int color) {
        mTextColor = color;
    }

    /**
     * 背景色を設定する。
     * @param color カラー値
     */
    public void setBackGroundColor(int color) {
        mBackGroundColor = color;
    }
}
