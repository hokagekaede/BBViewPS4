package hokage.kaede.gmail.com.StandardLib.Android;

import android.content.Context;

/**
 * 選択式のリストのアイテムクラス
 */
public class SelectionAdapterItem extends NormalAdapterItem {

    // 選択中かどうか
    private boolean mIsSelected = false;

    /**
     * 初期化を行う。
     * @param context 表示中のContext
     */
    public SelectionAdapterItem(Context context) {
        super(context);
    }

    /**
     * 選択状態かどうかを設定する。
     * @param is_selected 選択状態の場合はtrueを設定し、非選択状態の場合はfalseを設定する。
     */
    public void setSelected(boolean is_selected) {
        mIsSelected = is_selected;
    }

    /**
     * 選択状態かどうかを取得する。
     * @return 選択状態の場合はtrueを返し、非選択状態の場合はfalseを返す。
     */
    public boolean isSelected() {
        return mIsSelected;
    }

    /**
     * ビューの更新する。
     */
    @Override
    public void updateView() {
        if(mIsSelected) {
            setBackGroundColor(SettingManager.getColorGray());
        }
        else {
            setBackGroundColor(SettingManager.getColorBlack());
        }

        super.updateView();
    }
}
