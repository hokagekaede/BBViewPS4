package hokage.kaede.gmail.com.StandardLib.Android;

/**
 * 選択式のリストのインターフェース
 */
public interface ISelectionAdapter<T extends Object> {

    /**
     * 指定のアイテムを選択状態にする。
     * @param position 選択するアイテムの位置
     */
    void select(int position);

    /**
     * 選択状態を解除する。
     */
    void unselect();

    /**
     * 指定の位置が選択されているかどうかを取得する。
     * @param position 指定の位置
     * @return 選択されている場合はtrueを返し、選択されていない場合はfalseを返す。
     */
    boolean isSelected(int position);

    /**
     * 選択状態のアイテムを取得する。
     * @return 選択状態のアイテム
     */
    T getSelectedItem();

    /**
     * 選択状態の位置を取得する。
     * @return 選択状態の位置
     */
    int getSelectedPosition();
}
