package hokage.kaede.gmail.com.BBViewPS4.Item;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.ViewBuilder;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataLvl;
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
        layout_table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorYellow(), "項目", "強化無し", "強化1", "強化2", "強化3"));

        ArrayList<String> keys = data.getKeys();
        int size = keys.size();

        // 一般情報を表示
        for(int i=0; i<size; i++) {
            String target_key = keys.get(i);

            if(target_key.equals("名称")) {
                continue;
            }

            TableRow row = createItemInfoRow(data, target_key, SettingManager.getColorWhite());

            if(row != null) {
                layout_table.addView(row);
            }
        }

        // 追加情報の表示
        size = CALC_KEYS.length;
        for(int i=0; i<size ; i++) {
            String target_key = CALC_KEYS[i];
            TableRow row = createItemInfoRow(data, target_key, SettingManager.getColorCyan());

            if(row != null) {
                layout_table.addView(row);
            }
        }

        return layout_table;
    }

    /**
     * アイテム詳細情報の行データを生成する。
     * @param data アイテム情報
     * @param target_key アイテムのキー
     * @param color 表示する文字色
     * @return アイテム詳細情報の行
     */
    private TableRow createItemInfoRow(BBData data, String target_key, int color) {
        Context context = getContext();

        String[] values = new String[BBDataLvl.MAX_LEVEL + 2];  // キーと強化無しを入れて、合計5
        values[0] = target_key;

        for(int j = 0; j<= BBDataLvl.MAX_LEVEL; j++) {
            String buf = getSpecValue(data, target_key, j);

            if(buf.equals(BBData.STR_VALUE_NOTHING)) {
                return null;
            }
            else {
                values[j + 1] = buf;
            }
        }
        return ViewBuilder.createTableRow(context, color, values);
    }

    /**
     * 一般情報の文字列を取得する。
     * @param data データ
     * @param target_key ターゲットのキー
     * @param level 強化段階
     * @return 一般情報の文字列
     */
    private String getSpecValue(BBData data, String target_key, int level) {
        String ret = "";
        String value = data.get(target_key, level);

        if(value.equals(BBData.STR_VALUE_NOTHING)) {
            return BBData.STR_VALUE_NOTHING;
        }

        if(BBDataComparator.isPointKey(target_key)) {
            String point = SpecValues.getPoint(target_key, value, false);

            // 評価値算出に失敗した場合は対象の文字が評価値そのもの
            if(point.equals(SpecValues.NOTHING_STR)) {
                String data_str = SpecValues.getSpecUnit(data, target_key);
                ret = value + " (" + data_str + ")";
            }
            else {
                double value_num = SpecValues.changeDouble(value);
                String data_str = SpecValues.getSpecUnit(value_num, target_key);
                ret = point + " (" + data_str + ")";
            }
        }
        else {
            double value_num = SpecValues.getSpecValue(value, target_key, "");

            if(value_num != SpecValues.ERROR_VALUE) {
                ret = SpecValues.getSpecUnit(value_num, target_key);
            }
            else {
                ret = value;
            }
        }

        return ret;
    }
}
