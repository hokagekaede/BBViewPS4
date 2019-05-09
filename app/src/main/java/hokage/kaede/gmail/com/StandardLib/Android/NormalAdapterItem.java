package hokage.kaede.gmail.com.StandardLib.Android;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 一般的なリストの各項目のアイテムクラス
 */
public class NormalAdapterItem<T extends Object> extends LinearLayout {

    // アイテムのデータを表示するテキストビュー
    private TextView mTextView;

    // 表示対象のデータ
    private T mData;

    // 存在しない色の場合の設定
    protected static final int NOTHING_COLOR = -1;

    // フォントの設定
    protected int mTextSize = SettingManager.FLAG_TEXTSIZE_NORMAL;
    protected int mTextColor = NOTHING_COLOR;
    protected int mBackGroundColor = NOTHING_COLOR;

    /**
     * 初期化を行う。
     * @param context 表示中のContext
     */
    public NormalAdapterItem(Context context) {
        super(context);

        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL );
        this.setPadding(10, 10, 10, 10);
        this.setWeightSum((float)1.0);
    }

    /**
     * 表示するデータを設定する。
     * @param data データ
     */
    public void setData(T data) {
        mData = data;
    }

    /**
     * 表示するデータを取得する。
     * @return 表示するデータ
     */
    public T getData() {
        return mData;
    }

    /**
     * 表示するビューを生成する。
     */
    public void createView() {
        Context context = getContext();

        mTextView = new TextView(context);
        mTextView.setPadding(10, 10, 10, 10);
        this.addView(mTextView);
    }

    /**
     * ビューの更新する。
     */
    public void updateView() {
        Context context = getContext();

        mTextView.setText(mData.toString());
        mTextView.setTextSize(SettingManager.getTextSize(context, mTextSize));

        if(mTextColor != NOTHING_COLOR) {
            this.setTextColor(mTextColor);
        }

        if(mBackGroundColor != NOTHING_COLOR) {
            this.setBackgroundColor(mBackGroundColor);
        }
    }

    /**
     * テキストサイズを設定する。
     * @param text_size テキストサイズ
     */
    public void setTextSize(int text_size) {
        mTextSize = text_size;
    }

    /**
     * テキストカラーを設定する。
     * @param color カラー値
     */
    public void setTextColor(int color) {
        mTextColor = color;
    }

    /**
     * 背景色を設定する。
     * @param color カラー値
     */
    public void setBackGroundColor(int color) {
        mBackGroundColor = color;
    }

}
