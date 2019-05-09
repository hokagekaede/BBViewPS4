package hokage.kaede.gmail.com.StandardLib.Android;

import java.util.ArrayList;

/**
 * 複数選択式のリストのインターフェース
 */
public interface IMultiSelectionAdapter {

    /**
     * 指定のアイテムを選択状態にする。
     * @param position 選択するアイテムの位置
     * @param is_select 選択状態かどうか
     */
    void setSelect(int position, boolean is_select);

    /**
     * 指定位置のアイテムが選択中かどうかを取得する。
     * @param position 指定位置
     * @return 選択中の場合はtrueを返し、非選択の場合はfalseを返す。
     */
    boolean isSelected(int position);

    /**
     * 選択状態のアイテムを取得する。
     * 外部処理により、選択状態を示すリストとデータ数が不一致の場合、全て未選択状態として扱う。
     * @return 選択状態のアイテム
     */
    ArrayList getSelectedList();
}
