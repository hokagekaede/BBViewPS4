package hokage.kaede.gmail.com.BBViewPS4.Item;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.ViewBuilder;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataComparator;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataLvl;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Java.SpecValues;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;

public class PartsSeriesView extends LinearLayout  {

    private static final int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
    private static final int FP = LinearLayout.LayoutParams.FILL_PARENT;

    public PartsSeriesView(Context context, BBData target) {
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
        ArrayList<BBData> datalist = getTargetList(data);
        ArrayList<String> keylist = data.getKeys();

        // タイトルとアイテム詳細情報を表示する
        Context context = getContext();
        this.removeAllViews();
        this.addView(ViewBuilder.createTextView(context, "同シリーズ情報", BBViewSetting.FLAG_TEXTSIZE_LARGE));
        this.addView(createItemInfoTable(datalist, keylist));
    }

    /**
     * 対象パーツを取り出す
     * @param data データ
     * @return 同じシリーズのデータ
     */
    public ArrayList<BBData> getTargetList(BBData data) {

        // 対象パーツを取り出す
        String series_name = SpecValues.getSeries(data);

        BBDataManager manager = BBDataManager.getInstance();
        manager.setSortKey("");
        ArrayList<BBData> item_list = manager.getList();
        ArrayList<BBData> target_list = new ArrayList<BBData>();
        int item_list_count = item_list.size();

        for(int i=0; i<item_list_count; i++) {
            BBData buf_item = item_list.get(i);
            String buf_name = buf_item.get("名称");

            if(buf_name.contains(series_name) && BBDataManager.cmpPartsType(data, buf_item)) {
                target_list.add(buf_item);
            }
        }

        return target_list;
    }

    private static final String[] CALC_KEYS = {
            BBData.REAL_LIFE_KEY,
            BBData.DEF_RECORVER_TIME_KEY,
            BBData.CARRY_KEY,
            BBData.FLAIGHT_TIME_KEY,
            BBData.SEARCH_SPACE_KEY,
            BBData.SEARCH_SPACE_START_KEY,
            BBData.SEARCH_SPACE_MAX_KEY,
            BBData.SEARCH_SPACE_TIME_KEY
    };

    /**
     * アイテム詳細情報のテーブルを生成する
     * @param datalist アイテム情報リスト
     * @param keylist 対象のスペックリスト
     * @return アイテム詳細情報のテーブル
     */
    public TableLayout createItemInfoTable(ArrayList<BBData> datalist, ArrayList<String> keylist) {
        Context context = getContext();
        TableLayout layout_table = new TableLayout(context);
        layout_table.setLayoutParams(new LinearLayout.LayoutParams(FP, WC));
        layout_table.setColumnShrinkable(1, true);    // 右端は表を折り返す

        int size = keylist.size();

        // 一般情報を表示
        for(int i=0; i<size; i++) {
            String target_key = keylist.get(i);

            TableRow row = createItemInfoRow(datalist, target_key);

            if(row != null) {
                layout_table.addView(row);
            }
        }

        // 追加情報の表示
        /*
        size = CALC_KEYS.length;
        for(int i=0; i<size ; i++) {
            String key = CALC_KEYS[i];
            double num = data.getCalcValue(key);
            String value_str = SpecValues.getSpecUnit(num, key);

            if(num > BBData.NUM_VALUE_NOTHING) {
                layout_table.addView(ViewBuilder.createTableRow(context, SettingManager.getColorCyan(), key, value_str));
            }
        }
        */

        return layout_table;
    }

    /**
     * アイテム詳細情報の行データを生成する。
     * @param datalist アイテム情報のリスト
     * @param target_key アイテムのキー
     * @return アイテム詳細情報の行
     */
    private TableRow createItemInfoRow(ArrayList<BBData> datalist, String target_key) {
        Context context = getContext();

        String[] values = new String[datalist.size() + 1];
        values[0] = target_key;

        int data_count = datalist.size();
        for(int j=0; j<data_count; j++) {
            String buf = SpecValues.getSpecUnit(datalist.get(j), target_key);

            if(buf.equals(BBData.STR_VALUE_NOTHING)) {
                return null;
            }

            values[j + 1] = buf;
        }

        int color = SettingManager.getColorWhite();
        if(target_key.equals("名称")) {
            color = SettingManager.getColorYellow();
        }

        return ViewBuilder.createTableRow(context, color, values);
    }

    /**
     * 一般情報の文字列を取得する。
     * @param data データ
     * @param target_key ターゲットのキー
     * @return 一般情報の文字列
     */
    private String getSpecValue(BBData data, String target_key) {
        String ret = "";
        String point = data.get(target_key);
        String data_str = SpecValues.getSpecUnit(data, target_key);

        if(point.equals(BBData.STR_VALUE_NOTHING)) {
            return BBData.STR_VALUE_NOTHING;
        }

        if(BBDataComparator.isPointKey(target_key)) {
            ret = point + " (" + data_str + ")";
        }
        else {
            ret = data_str;
        }

        return ret;
    }
}
