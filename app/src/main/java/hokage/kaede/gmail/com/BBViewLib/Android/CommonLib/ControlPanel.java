package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;

/**
 * 左側のボタンを表示するパネル
 */
public class ControlPanel extends LinearLayout {

    private String[] mControlNames;
    private boolean[] mIsHidden;
    private OnExecuteListenerInterface mOnClickListener;

    private BBData mItem;

    /**
     * 初期化する。
     * @param context コンストラクタ
     * @param control_names ボタンに表示する名前の一覧
     */
    public ControlPanel(Context context, String[] control_names, boolean[] is_hidden) {
        super(context);
        mControlNames = control_names;
        mIsHidden = is_hidden;
    }

    /**
     * ボタン押下時に実行する処理を登録する。
     * @param listener リスナー
     */
    public void setOnExecuteListener(OnExecuteListenerInterface listener) {
        mOnClickListener = listener;
    }

    /**
     * ビューを生成する。
     */
    public void createView() {
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.LEFT | Gravity.CENTER_HORIZONTAL);

        int count = mControlNames.length;

        if(BBViewSetting.IS_LISTBUTTON_TYPETEXT) {
            for (int i = 0; i < count; i++) {
                TextView text_view = createIndexTextView(i);
                this.addView(text_view);

                if(mIsHidden[i]) {
                    text_view.setVisibility(View.GONE);
                }
            }
        }
        else {
            for (int i = 0; i < count; i++) {
                Button button_view = createIndexButton(i);
                this.addView(button_view);

                if(mIsHidden[i]) {
                    button_view.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * コントロールパネルのボタン(TextView版)を生成する。
     * @param idx 生成する番号
     * @return ボタンのビュー
     */
    private TextView createIndexTextView(int idx) {
        Context context = getContext();
        TextView text_view = new TextView(context);

        text_view.setText(mControlNames[idx].substring(0, 1));
        text_view.setTextSize((float)(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL) * BBViewSetting.LISTBUTTON_TEXTSIZE));
        text_view.setOnClickListener(new OnClickControlButtonListener(idx));
        text_view.setClickable(true);
        text_view.setFocusable(false);

        text_view.setPadding(10, 10, 10, 10);
        text_view.setBackgroundColor(SettingManager.getColorGray());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        text_view.setLayoutParams(lp);

        return text_view;
    }

    /**
     * コントロールパネルのボタンを生成する。
     * @param idx 生成する番号
     * @return ボタンのビュー
     */
    private Button createIndexButton(int idx) {
        Context context = getContext();
        Button button_view = new Button(context);

        button_view.setText(mControlNames[idx].substring(0, 1));
        button_view.setOnClickListener(new OnClickControlButtonListener(idx));
        button_view.setClickable(true);
        button_view.setFocusable(false);

        return button_view;
    }

    /**
     * ビューの更新する。
     */
    public void updateView(BBData item) {
        mItem = item;
    }

    /**
     * コントールパネルのボタン押下時の処理を行うリスナー。
     * 押された場所を取得して、要求元の画面に戻す。
     */
    private class OnClickControlButtonListener implements OnClickListener {

        private int mIndex;

        public OnClickControlButtonListener(int idx) {
            mIndex = idx;
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onExecute(mItem, mIndex);
        }
    }

    /**
     * コントロールパネルのボタン押下時の処理を行うインターフェース
     */
    public interface OnExecuteListenerInterface {
        void onExecute(BBData data, int cmd_idx);
    }
}
