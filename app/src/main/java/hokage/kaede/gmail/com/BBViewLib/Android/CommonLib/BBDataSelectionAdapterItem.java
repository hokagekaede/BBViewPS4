package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import android.content.Context;

import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;

/**
 * 選択式のBBDataリストのアイテムクラス
 */
public class BBDataSelectionAdapterItem extends BBDataAdapterItem {

    /**
     * 初期化を行う。
     * @param context 表示中のContext
     */
    public BBDataSelectionAdapterItem(Context context, BBDataAdapterItemProperty property) {
        super(context, property);
    }

    /**
     * ビューの更新する。
     */
    @Override
    public void updateView() {
        if(isSelected()) {
            setBackgroundColor(SettingManager.getColorGray());
        }
        else {
            setBackgroundColor(SettingManager.getColorBlack());
        }

        super.updateView();
    }
}
