package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.StandardLib.Android.ISelectionAdapter;

public class BBDataSelectionAdapter extends BBDataAdapter implements ISelectionAdapter<BBData> {

    // 選択していない場合の位置
    private static final int UNSELECTED_POSITION = -1;

    // 選択中のアイテムの位置
    private int mSelectedPosition;

    /**
     * 初期化を行う。
     *
     * @param property 表示のためのデータと設定値
     */
    public BBDataSelectionAdapter(BBDataAdapterItemProperty property) {
        super(property);
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
    @Override
    public BBData getSelectedItem() {
        return get(mSelectedPosition);
    }

    /**
     * 選択状態の位置を取得する。
     * @return 選択状態の位置
     */
    @Override
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
        BBData data = get(position);
        BBDataSelectionAdapterItem item_view;
        BBDataAdapterItemProperty property = getProperty();

        if(view == null) {
            item_view = new BBDataSelectionAdapterItem(context, property);
            item_view.createView();
            item_view.setTextSize(mTextSize);
            item_view.setTextColor(mTextColor);
            item_view.setBackGroundColor(mBackGroundColor);
        }
        else {
            item_view = (BBDataSelectionAdapterItem)view;
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
