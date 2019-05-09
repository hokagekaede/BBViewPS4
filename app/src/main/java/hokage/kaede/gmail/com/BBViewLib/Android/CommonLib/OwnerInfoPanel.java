package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.BBItemDatabase;
import hokage.kaede.gmail.com.BBViewLib.Java.BBNetDatabase;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;

/**
 * 所有情報を示すパネル
 */
public class OwnerInfoPanel extends LinearLayout {

    // 所有情報を示すテキストビュー
    private TextView mExistTextView;

    /**
     * 初期化する。
     * @param context コンテキスト
     */
    public OwnerInfoPanel(Context context) {
        super(context);
    }

    /**
     * ビューを生成する。
     */
    public void createView() {
        Context context = getContext();

        mExistTextView = new TextView(context);
        mExistTextView.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
        mExistTextView.setGravity(Gravity.RIGHT | Gravity.CENTER);
        mExistTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));

        this.addView(mExistTextView);
    }

    /**
     * ビューの更新する。
     * @param target_item 表示対象のデータ
     */
    public void updateView(BBData target_item) {
        update(target_item);
    }

    /**
     * レベルを更新する。
     */
    public void changeLevel(BBData target_item) {
        changeNext(target_item);
        update(target_item);
    }

    /**
     * ビューの更新をする。
     */
    private void update(BBData target_item) {
        String text = createExistText(target_item);
        mExistTextView.setText(text);
    }

    /**
     * 購入した項目かどうかの文字列を生成する。
     * @param target_item 表示対象のデータ
     * @return 購入した武器(開発済みのチップ)かどうかを示す文字列
     */
    private String createExistText(BBData target_item) {
        String item_name = "";

        BBItemDatabase database = BBItemDatabase.getInstance();
        if(BBDataManager.isParts(target_item)) {
            item_name = database.getPartsLevelText(target_item);
        }
        else if(BBDataManager.isWeapon(target_item)) {
            item_name = database.getWeaponLevelText(target_item);
        }
        else if(target_item.existCategory(BBDataManager.CHIP_STR)) {
            item_name = database.getChipInfoText(target_item);
        }

        return item_name;
    }

    /**
     * 購入した項目かどうかの文字列を生成する。
     * @param target_item 表示対象のデータ
     * @return 購入した武器(開発済みのチップ)かどうかを示す文字列
     */
    private void changeNext(BBData target_item) {

        BBItemDatabase database = BBItemDatabase.getInstance();
        if(BBDataManager.isParts(target_item)) {
            int level = database.getPartsLevel(target_item);

            if(level == BBItemDatabase.ITEM_LEVEL3) {
                database.setPartsLevel(target_item, BBItemDatabase.ITEM_NOT_HAVING);
            }
            else if(level == BBItemDatabase.ITEM_NOT_HAVING) {
                database.setPartsLevel(target_item, BBItemDatabase.ITEM_LEVEL0);
            }
            else {
                database.setPartsLevel(target_item, level + 1);
            }
        }
        else if(BBDataManager.isWeapon(target_item)) {
            int level = database.getWeaponLevel(target_item);

            if(level == BBItemDatabase.ITEM_LEVEL3) {
                database.setWeaponLevel(target_item, BBItemDatabase.ITEM_NOT_HAVING);
            }
            else if(level == BBItemDatabase.ITEM_NOT_HAVING) {
                database.setWeaponLevel(target_item, BBItemDatabase.ITEM_LEVEL0);
            }
            else {
                database.setWeaponLevel(target_item, level + 1);
            }
        }
        else if(target_item.existCategory(BBDataManager.CHIP_STR)) {
            int is_having = database.getChipInfo(target_item);

            if(is_having == BBItemDatabase.ITEM_HAVING) {
                database.setChipInfo(target_item, BBItemDatabase.ITEM_NOT_HAVING);
            }
            else {
                database.setChipInfo(target_item, BBItemDatabase.ITEM_HAVING);
            }
        }

    }
}
