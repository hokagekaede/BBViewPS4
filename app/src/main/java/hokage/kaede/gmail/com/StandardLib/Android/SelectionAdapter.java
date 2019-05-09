package hokage.kaede.gmail.com.StandardLib.Android;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 選択式のリストを実現するクラス
 */
public class SelectionAdapter<T extends Object> extends NormalAdapter<T> implements ISelectionAdapter {

    // 選択していない場合の位置
    public static final int UNSELECTED_POSITION = -1;

    // 選択中のアイテムの位置
    private int mSelectedPosition;

    /**
     * 初期化する。
     */
    public SelectionAdapter() {
        super();
        mSelectedPosition = UNSELECTED_POSITION;
    }

    /**
     * 指定のアイテムを選択状態にする。
     * @param position 選択するアイテムの位置
     */
    @Override
    public void select(int position) {
        mSelectedPosition = position;
    }

    /**
     * 選択状態を解除する。
     */
    @Override
    public void unselect() {
        mSelectedPosition = UNSELECTED_POSITION;
    }

    /**
     * 指定の位置が選択されているかどうかを取得する。
     * @param position 指定の位置
     * @return 選択されている場合はtrueを返し、選択されていない場合はfalseを返す。
     */
    @Override
    public boolean isSelected(int position) {
        if(mSelectedPosition == position) {
            return true;
        }

        return false;
    }

    /**
     * 選択状態のアイテムを取得する。
     * @return 選択状態のアイテム
     */
    public T getSelectedItem() {
        if(mSelectedPosition == UNSELECTED_POSITION) {
            return null;
        }

        return get(mSelectedPosition);
    }

    /**
     * 選択状態のアイテムの位置を取得する。
     * @return 選択状態の位置
     */
    public int getSelectedPosition() {
        return mSelectedPosition;
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
        T data = get(position);
        SelectionAdapterItem item_view;

        if(view == null) {
            item_view = new SelectionAdapterItem(context);
            item_view.createView();
            item_view.setTextSize(mTextSize);
            item_view.setTextColor(mTextColor);
            item_view.setBackGroundColor(mBackGroundColor);
        }
        else {
            item_view = (SelectionAdapterItem)view;
        }

        if(mSelectedPosition == position) {
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
