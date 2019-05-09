package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.StandardLib.Android.MultiSelectionAdsapter;

/**
 * BBDataを複数選択可能なリストを実現するクラス
 */
public class BBDataMultiSelectionAdapter extends MultiSelectionAdsapter<BBData> {

    private BBDataAdapterItemProperty mProperty;

    private ControlPanelBuilder mControlPanelBuilder;

    /**
     * 初期化を行う。
     * @param property 表示のためのデータと設定値
     */
    public BBDataMultiSelectionAdapter(BBDataAdapterItemProperty property) {
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
     * コントロールパネルのビルダーを設定する。
     * @param builder コントロールパネルの生成設定がされているビルダー
     */
    public void setBuilder(ControlPanelBuilder builder) {
        mControlPanelBuilder = builder;
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
        ControlPanel control_panel;

        if(view == null) {
            item_view = new BBDataSelectionAdapterItem(context, mProperty);
            item_view.createView();
            item_view.setTextSize(mTextSize);
            item_view.setTextColor(mTextColor);
            item_view.setBackGroundColor(mBackGroundColor);

            control_panel = mControlPanelBuilder.createControlPanel(context);
            item_view.addView(control_panel, 0); // 先頭(一番左)に置く
        }
        else {
            item_view = (BBDataSelectionAdapterItem)view;
            control_panel = (ControlPanel)item_view.getChildAt(0);
        }

        if(isSelected(position)) {
            item_view.setSelected(true);
        }
        else {
            item_view.setSelected(false);
        }

        item_view.setData(data);
        item_view.updateView();
        control_panel.updateView(data);

        return item_view;
    }
}
