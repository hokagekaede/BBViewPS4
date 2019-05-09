package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.StandardLib.Android.NormalAdapter;

public class BBDataAdapter extends NormalAdapter<BBData> {

    private BBDataAdapterItemProperty mProperty;

    private ControlPanelBuilder mControlPanelBuilder;

    /**
     * 初期化を行う。
     * @param property 表示のためのデータと設定値
     */
    public BBDataAdapter(BBDataAdapterItemProperty property) {
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
     * 現在選択中のデータの位置を取得する。
     * @return データの位置
     */
    public int getBaseItemIndex() {
        BBData data = mProperty.getBaseItem();
        return getIndex(data);
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
        BBDataAdapterItem item_view;
        ControlPanel control_panel = null;

        if(view == null) {
            item_view = new BBDataAdapterItem(context, mProperty);
            item_view.createView();
            item_view.setTextSize(mTextSize);
            item_view.setTextColor(mTextColor);
            item_view.setBackGroundColor(mBackGroundColor);

            if(mControlPanelBuilder != null) {
                control_panel = mControlPanelBuilder.createControlPanel(context);
                item_view.addView(control_panel, 0); // 先頭(一番左)に置く
            }
        }
        else {
            item_view = (BBDataAdapterItem)view;
            control_panel = (ControlPanel) item_view.getChildAt(0);
        }

        item_view.setData(data);
        item_view.updateView();

        if(control_panel != null) {
            control_panel.updateView(data);
        }

        return item_view;
    }
}
