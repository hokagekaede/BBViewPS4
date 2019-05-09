package hokage.kaede.gmail.com.StandardLib.Android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * チェックボックス式のリストを実現するクラス
 */
public class CheckAdapter extends MultiSelectionAdsapter {

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
        CheckAdapterItem item_view;

        if(view == null) {
            item_view = new CheckAdapterItem(context);
            item_view.setTextSize(mTextSize);
            item_view.setTextColor(mTextColor);
            item_view.setBackGroundColor(mBackGroundColor);
        }
        else {
            item_view = (CheckAdapterItem)view;
        }

        if(isSelected(position)) {
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
