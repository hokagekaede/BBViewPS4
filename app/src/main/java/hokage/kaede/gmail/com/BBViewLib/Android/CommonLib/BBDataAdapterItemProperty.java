package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import java.util.ArrayList;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;

/**
 * BBDataAdapterItemで使用するデータと設定値を保持するクラス
 */
public class BBDataAdapterItemProperty {

    private BBData mBaseItem;
    private boolean mIsShowSwitch = false;
    private boolean mIsShowTypeB = false;
    private ArrayList<String> mShownKeys;

    private boolean mIsShowFavorite = false;

    /**
     * 比較対象のデータを設定する。
     * @param base_item 比較対象のデータ
     */
    public void setBaseItem(BBData base_item) {
        mBaseItem = base_item;
    }

    /**
     * 比較対象のデータを取得する。
     * @return 比較対象のデータ
     */
    public BBData getBaseItem() {
        return mBaseItem;
    }

    /**
     * スイッチ武器のタイプ情報を表示するかどうかの設定を行う。
     * @param is_show_switch スイッチ武器のタイプ表示をする場合はtrueを設定する。
     */
    public void setShowSwitch(boolean is_show_switch) {
        mIsShowSwitch = is_show_switch;
    }

    /**
     * スイッチ武器のタイプを表示するかどうかを取得する。
     * @return タイプ表示をする場合はtrueを返し、表示しない場合はfalseを返す。
     */
    public boolean isShowSwitch() {
        return mIsShowTypeB;
    }

    /**
     * タイプBの情報を表示するかどうかの設定を行う。
     * @param is_show_typeb trueを設定した場合は表示し、falseを設定した場合はタイプAを表示する。
     */
    public void setShowTypeB(boolean is_show_typeb) {
        mIsShowTypeB = is_show_typeb;
    }

    /**
     * タイプBの情報を表示するかどうかを取得する。
     * @return タイプBを表示する場合はtrueを返し、表示しない場合はfalseを返す。
     */
    public boolean isShowTypeB() {
        return mIsShowTypeB;
    }

    /**
     * 表示項目の対象スペックのリストを設定する。
     * @param keys 表示項目の対象スペックのリスト
     */
    public void setShownKeys(ArrayList<String> keys) {
        mShownKeys = keys;
    }

    /**
     * 表示項目の対象スペックのリストを取得する。
     * @return 表示項目の対象スペックのリスト
     */
    public ArrayList<String> getShownKeys() {
        return mShownKeys;
    }

    /**
     * お気に入りボタンを表示有無を設定する。
     * @param is_show 表示する場合はtrueを設定し、表示しない場合はfalseを設定する。
     */
    public void setShowFavorite(boolean is_show) {
        mIsShowFavorite = is_show;
    }

    /**
     * お気に入りボタンの表示有無を取得する。
     * @return 表示する場合はtrueを返し、表示しない場合はfalseを返す。
     */
    public boolean isShowFavorite() {
        return mIsShowFavorite;
    }

}
