package hokage.kaede.gmail.com.StandardLib.Android;

import android.content.Context;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

/**
 * チェックボックス式のリストのアイテムクラス
 */
public class CheckAdapterItem extends SelectionAdapterItem {

    private CheckBox mCheckBox;

    public CheckAdapterItem(Context context) {
        super(context);

        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        this.setPadding(10, 10, 10, 10);
        this.setWeightSum((float)1.0);
    }

    /**
     * 選択状態かどうかを設定する。
     * @param is_selected 選択状態の場合はtrueを設定し、非選択状態の場合はfalseを設定する。
     */
    @Override
    public void setSelected(boolean is_selected) {
        mCheckBox.setChecked(is_selected);
    }

    /**
     * 選択状態かどうかを取得する。
     * @return 選択状態の場合はtrueを返し、非選択状態の場合はfalseを返す。
     */
    @Override
    public boolean isSelected() {
        return mCheckBox.isChecked();
    }

    /**
     * 表示するビューを生成する。
     */
    @Override
    public void createView() {
        Context context = getContext();

        mCheckBox = new CheckBox(context);
        mCheckBox.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));

        this.addView(mCheckBox);
    }

    /**
     * 表示するビューを生成する。
     */
    @Override
    public void updateView() {
        Context context = getContext();
        Object data = getData();

        mCheckBox.setText(data.toString());
        mCheckBox.setTextSize(SettingManager.getTextSize(context, mTextSize));

        if(mTextColor != NOTHING_COLOR) {
            this.setTextColor(mTextColor);
        }

        if(mBackGroundColor != NOTHING_COLOR) {
            this.setBackgroundColor(mBackGroundColor);
        }
    }

    /**
     * チェックボックスを変更した際に実行するリスナーを設定する。
     * @param listener 設定するリスナー。
     */
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        mCheckBox.setOnCheckedChangeListener(listener);
    }
}
