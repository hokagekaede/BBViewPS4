package hokage.kaede.gmail.com.BBViewPS4.Item;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import java.util.ArrayList;

import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.ViewBuilder;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataComparator;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Java.SpecValues;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;

/**
 * パーツまたは武器の詳細情報を表示するクラス
 */
public class InfoView extends LinearLayout {

    private static final int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
    private static final int FP = LinearLayout.LayoutParams.FILL_PARENT;

    public InfoView(Context context, BBData target) {
        super(context);

        createView(target);
    }

    /**
     * 画面に表示するビューを生成する。
     * @param data 対象のパーツ、または武器
     */
    private void createView(BBData data) {
        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.LEFT | Gravity.TOP);

        update(data);
    }

    /**
     * 画面の表示を更新する。
     * @param data 対象のパーツ、または武器
     */
    public void update(BBData data) {

        String title = data.get("名称");

        String category_str = "";
        ArrayList<String> categorys = data.getCategorys();
        int cate_len = categorys.size();
        for(int i=0; i<cate_len; i++) {
            String buf = categorys.get(i);
            if(buf != null) {
                category_str = category_str + " - " + buf + "\n";
            }
        }

        // タイトルとアイテム詳細情報を表示する
        Context context = getContext();
        this.removeAllViews();
        this.addView(ViewBuilder.createTextView(context, title, BBViewSetting.FLAG_TEXTSIZE_LARGE));
        this.addView(ViewBuilder.createTextView(context, category_str, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
        this.addView(createItemInfoTable(data));
    }

    private static final String[] CALC_KEYS = {
            BBData.REAL_LIFE_KEY,
            BBData.DEF_RECORVER_TIME_KEY,
            BBData.FULL_POWER_KEY,
            BBData.MAGAZINE_POWER_KEY,
            BBData.SEC_POWER_KEY,
            BBData.BATTLE_POWER_KEY,
            BBData.OH_POWER_KEY,
            BBData.BULLET_SUM_KEY,
            BBData.CARRY_KEY,
            BBData.FLAIGHT_TIME_KEY,
            BBData.SEARCH_SPACE_KEY,
            BBData.SEARCH_SPACE_START_KEY,
            BBData.SEARCH_SPACE_MAX_KEY,
            BBData.SEARCH_SPACE_TIME_KEY
    };

    /**
     * アイテム詳細情報のテーブルを生成する
     * @param data アイテム情報
     * @return アイテム詳細情報のテーブル
     */
    public TableLayout createItemInfoTable(BBData data) {
        Context context = getContext();
        TableLayout layout_table = new TableLayout(context);
        layout_table.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));
        layout_table.setColumnShrinkable(1, true);    // 右端は表を折り返す

        // 表のタイトルを記載する
        layout_table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), "項目", "説明"));

        ArrayList<String> keys = data.getKeys();
        int size = keys.size();

        // 一般情報を表示
        for(int i=0; i<size; i++) {
            String target_key = keys.get(i);
            String point = data.get(target_key);

            if(target_key.equals("名称")) {
                continue;
            }
            else if(point == null) {
                continue;
            }

            String data_str = SpecValues.getSpecUnit(data, target_key, BBViewSetting.IS_KM_PER_HOUR);

            if(BBDataComparator.isPointKey(target_key)) {
                layout_table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorWhite(), target_key, point + " (" + data_str + ")"));
            }
            else {
                layout_table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorWhite(), target_key, data_str));
            }
        }

        // 追加情報の表示
        size = CALC_KEYS.length;
        for(int i=0; i<size ; i++) {
            String key = CALC_KEYS[i];
            double num = data.getCalcValue(key);
            String value_str = SpecValues.getSpecUnit(num, key, BBViewSetting.IS_KM_PER_HOUR);

            if(num > BBData.NUM_VALUE_NOTHING) {
                layout_table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorCyan(), key, value_str));
            }
        }

        return layout_table;
    }
}
