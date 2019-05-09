package hokage.kaede.gmail.com.StandardLib.Android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Spinner向けのリストを実現するクラス
 */
public class SpinnerAdapter extends NormalAdapter {

    /**
     * 初期化を行う。
     */
    public SpinnerAdapter() {
        super();
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
        TextView item_view;
        Context context = viewGroup.getContext();
        String item_text = get(position).toString();

        if(view == null) {
            item_view = new TextView(context);
            item_view.setText(item_text);
            item_view.setPadding(10, 10, 10, 10);
            item_view.setTextSize(SettingManager.getTextSize(context, mTextSize));

            if(mTextColor != NOTHING_COLOR) {
                item_view.setTextColor(mTextColor);
            }

            if(mBackGroundColor != NOTHING_COLOR) {
                item_view.setBackgroundColor(mBackGroundColor);
            }
        }
        else {
            item_view = (TextView)view;
            item_view.setText(item_text);
        }

        return item_view;
    }

}
