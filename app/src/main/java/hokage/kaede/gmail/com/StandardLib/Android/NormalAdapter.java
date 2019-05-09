package hokage.kaede.gmail.com.StandardLib.Android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 一般的なリストを実現するクラス
 */
public class NormalAdapter<T extends Object> extends BaseAdapter {

    // 管理するデータのリスト
    private ArrayList<T> mList;

    // 存在しない色の場合の設定
    protected static final int NOTHING_COLOR = -1;

    // フォントの設定
    protected int mTextSize = SettingManager.FLAG_TEXTSIZE_NORMAL;
    protected int mTextColor = NOTHING_COLOR;
    protected int mBackGroundColor = NOTHING_COLOR;

    /**
     * 初期化する。
     */
    public NormalAdapter() {
        mList = new ArrayList<T>();
    }

    /**
     * リストを設定する。
     * @param list 設定するリスト
     */
    public void setList(ArrayList<T> list) {
        mList = list;
    }

    /**
     * 項目を追加する。
     * @param item 追加する項目
     */
    public void add(T item) {
        mList.add(item);
    }

    /**
     * 項目を削除する。
     * @param position 削除するアイテムの位置
     */
    public void remove(int position) {
        mList.remove(position);
    }

    /**
     * 項目を削除する。
     * @param data 削除対象のデータ
     */
    public void remove(T data) {
        mList.remove(data);
    }

    /**
     * 項目を全削除する。
     */
    public void clear() {
        mList.clear();
    }

    /**
     * リストの指定データを置き換える。
     * @param from_value 置き換え前のデータ
     * @param to_value 置き換え後のデータ
     */
    public void replace(T from_value, T to_value) {
        int idx = mList.indexOf(from_value);

        if(idx >= 0) {
            mList.set(idx, to_value);
        }
    }

    /**
     * リストのデータを入れ替える。
     * @param from_position 移動元の位置
     * @param to_position 移動先の位置
     */
    public void swap(int from_position, int to_position) {
        T from_data = mList.get(from_position);
        T to_data = mList.get(to_position);

        mList.set(to_position, from_data);
        mList.set(from_position, to_data);
    }

    /**
     * 指定位置のアイテムを取得する。
     * @param position 指定位置
     * @return 指定位置のアイテム
     */
    public T get(int position) {
        return mList.get(position);
    }

    /**
     * データの位置を取得する。
     * @return データの位置
     */
    public int getIndex(Object item) {
        return mList.indexOf(item);
    }

    /**
     * リストの数を取得する。
     * @return リストの数
     */
    @Override
    public int getCount() {
        return mList.size();
    }

    /**
     * リストのデータを取得する。
     * @param position 取得する位置
     * @return 位置に応じたデータ
     */
    @Override
    public T getItem(int position) {
        if(position < 0 || position >= mList.size()) {
            return null;
        }

        return mList.get(position);
    }

    /**
     * リストのデータIDを取得する。
     * @param position 取得する位置
     * @return 未使用のため、0を返す。
     */
    @Override
    public long getItemId(int position) {
        return 0;
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
        Object data = mList.get(position);
        NormalAdapterItem item_view;

        if(view == null) {
            item_view = new NormalAdapterItem(context);
            item_view.createView();
            item_view.setTextSize(mTextSize);
            item_view.setTextColor(mTextColor);
            item_view.setBackGroundColor(mBackGroundColor);
        }
        else {
            item_view = (NormalAdapterItem)view;
        }

        item_view.setData(data);
        item_view.updateView();

        return item_view;
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
