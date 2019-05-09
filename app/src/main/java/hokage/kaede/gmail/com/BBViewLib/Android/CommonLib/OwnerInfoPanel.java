package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
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

        // BBPS4は所持情報を非表示にする。
        this.setVisibility(View.GONE);
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
        String data_name = target_item.get("名称");

        BBNetDatabase net_database = BBNetDatabase.getInstance();
        if(!net_database.getCardName().equals(BBNetDatabase.NO_CARD_DATA)) {

            if(BBDataManager.isParts(target_item)) {
                if(net_database.existParts(target_item)) {
                    item_name = "(所持)";
                }
                else {
                    item_name = "(未購入)";
                }
            }
            else if(BBDataManager.isWeapon(target_item)) {
                if(net_database.existWeapon(data_name)) {
                    item_name = "(所持)";
                }
                else {
                    item_name = "(未購入)";
                }
            }
            else if(target_item.existCategory(BBDataManager.CHIP_STR)) {
                if(net_database.existChip(data_name)) {
                    item_name = "(所持)";
                }
                else {
                    item_name = "(未開発)";
                }
            }
            else if(target_item.existCategory(BBDataManager.MATERIAL_STR)) {
                String value = net_database.getMaterials().get(data_name);
                if(value.equals("null")) {
                    item_name = "(情報なし)";
                }
                else {
                    item_name = value + "個";
                }
            }
            else if(target_item.existCategory(BBDataManager.MEDAL_STR)) {
                String value = net_database.getMedals().get(data_name);
                if(value.equals("null")) {
                    item_name = "(情報なし)";
                }
                else {
                    item_name = value + "個";
                }
            }
            else if(target_item.existCategory(BBDataManager.SEED_STR)) {
                String value = net_database.getSeeds().get(data_name);
                if(value.equals("null")) {
                    item_name = "(情報なし)";
                }
                else {
                    item_name = value + "個";
                }
            }
        }

        return item_name;
    }
}
