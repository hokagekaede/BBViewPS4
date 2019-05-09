package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataComparator;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Java.SpecValues;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;

/**
 * パーツや武器のスペックを表示するパネル
 */
public class SpecInfoPanel extends LinearLayout {

    private TextView mNameTextView;
    private TextView mSubTextView;

    private BBDataAdapterItemProperty mProperty;

    // テキストビューに設定するタグ
    private static final String COLOR_TAG_END    = "</font>";
    private static final String TAG_BR = "<BR>";

    public SpecInfoPanel(Context context, BBDataAdapterItemProperty property) {
        super(context);
        mProperty = property;
    }

    /**
     * ビューを生成する。
     */
    public void createView() {
        Context context = getContext();

        mNameTextView = new TextView(context);
        mNameTextView.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));

        mSubTextView = new TextView(context);
        mSubTextView.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_SMALL));

        LinearLayout top_layout = new LinearLayout(context);
        top_layout.setOrientation(LinearLayout.VERTICAL);
        top_layout.setGravity(Gravity.LEFT | Gravity.CENTER_HORIZONTAL);
        top_layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));

        top_layout.addView(mNameTextView);
        top_layout.addView(mSubTextView);

        this.addView(top_layout);
    }

    /**
     * ビューの更新する。
     * @param target_item 表示対象のデータ
     */
    public void updateView(BBData target_item) {
        BBData base_item = mProperty.getBaseItem();

        String name_text = createNameText(target_item);
        String sub_text = createSubText(target_item);

        mNameTextView.setText(name_text);
        mSubTextView.setText(Html.fromHtml(sub_text));

        // テキストを更新する
        if(sub_text.equals("")) {
            mSubTextView.setVisibility(View.GONE);
        }
        else {
            mSubTextView.setVisibility(View.VISIBLE);
        }

        // 選択中のアイテムの場合は文字色を黄色に変更する
        if(base_item != null && BBDataManager.equalData(target_item, base_item)) {
            mNameTextView.setTextColor(SettingManager.getColorYellow());
        }
        else {
            mNameTextView.setTextColor(SettingManager.getColorWhite());
        }
    }

    /**
     * データの名前を生成する
     * @param target_item 表示対象のデータ
     * @return 項目の名前を生成する。パーツの場合はどの部位のパーツかの情報も追記する。
     */
    private String createNameText(BBData target_item) {
        String item_name = "";
        String data_name = target_item.get("名称");

        if(BBDataManager.isParts(target_item)) {
            String part_type = BBDataManager.getPartsType(target_item).substring(0, 1);
            item_name = data_name + " (" + part_type + ")";
        }
        else {
            item_name = data_name;
        }

        // スイッチ武器の場合はタイプ情報を追加表示する
        if(mProperty.isShowSwitch() && target_item.getTypeB() != null) {
            if(mProperty.isShowTypeB()) {
                item_name = item_name + " (タイプB)";
            }
            else {
                item_name = item_name + " (タイプA)";
            }
        }

        return item_name;
    }

    /**
     * 追加表示する文字列を生成する
     * @param target_item 表示対象のデータ
     * @return 追加表示する文字列
     */
    private String createSubText(BBData target_item) {
        String ret = "";
        BBData base_item = mProperty.getBaseItem();
        ArrayList<String> shown_keys = mProperty.getShownKeys();

        if(base_item == null || shown_keys == null) {
            return ret;
        }

        // 対象のデータを決定 (スイッチ武器)
        BBData from_item = base_item;
        BBData to_item = target_item;

        if(mProperty.isShowSwitch() && mProperty.isShowTypeB()) {
            if(from_item.getTypeB() != null) {
                from_item = from_item.getTypeB();
            }

            if(to_item.getTypeB() != null) {
                to_item = to_item.getTypeB();
            }
        }

        // 表示文字列生成
        int len = shown_keys.size();
        for(int i=0; i<len; i++) {
            String shown_key = shown_keys.get(i);

            // 現在選択中のパーツとの性能比較を行い、表示色を決定する。
            String color_stag = "";
            String color_etag = "";
            String cmp_str = "";
            if(from_item != null) {
                BBDataComparator cmp_data = new BBDataComparator(shown_key, true);
                int comp_ret = cmp_data.compare(from_item, to_item);
                double cmp_value = cmp_data.getCmpValue();

                if(cmp_data.isCmpOK()) {
                    if(comp_ret < 0) {
                        color_stag = SettingManager.getCodeCyan();
                        color_etag = COLOR_TAG_END;
                        cmp_str = " (" + SpecValues.getSpecUnitCmpArmor(Math.abs(cmp_value), shown_key) + "↑)";
                    }
                    else if(comp_ret > 0) {
                        color_stag = SettingManager.getCodeMagenta();
                        color_etag = COLOR_TAG_END;
                        cmp_str = " (" + SpecValues.getSpecUnitCmpArmor(Math.abs(cmp_value), shown_key) + "↓)";
                    }
                }
            }

            // 表示する値の文字列を取得する
            String value_str = "";
            if(BBDataComparator.isPointKey(shown_key)) {
                value_str = SpecValues.getSpecUnit(to_item, shown_key);
            }
            else if(shown_key.equals(BBData.ARMOR_BREAK_KEY)) {
                value_str = createArmorBreakString(BBData.ARMOR_BREAK_KEY, to_item);
            }
            else if(shown_key.equals(BBData.ARMOR_DOWN_KEY)) {
                value_str = createArmorBreakString(BBData.ARMOR_DOWN_KEY, to_item);
            }
            else if(shown_key.equals(BBData.ARMOR_KB_KEY)) {
                value_str = createArmorBreakString(BBData.ARMOR_KB_KEY, to_item);
            }
            else if(shown_key.equals(BBData.BULLET_SUM_KEY)) {
                value_str = to_item.get("総弾数") + "=" + SpecValues.getShowValue(to_item, shown_key);
            }
            else {
                value_str = SpecValues.getShowValue(to_item, shown_key);
            }

            // 文字を結合する
            ret = ret + shown_key + "：" + color_stag + value_str + cmp_str + color_etag + TAG_BR;
        }

        // 末尾の文字を削除
        if(!ret.equals("")) {
            ret = ret.substring(0, ret.length() - TAG_BR.length());
        }

        return ret;
    }

    /**
     * 大破判定、転倒判定、KB判定の文字列を生成する。
     * @param key
     * @param target_data
     * @return
     */
    private String createArmorBreakString(String key, BBData target_data) {
        String ret = "";
        String point = "";

        double min_value = SpecValues.getSpecValue("E-", "装甲", "");
        double value = target_data.getCalcValue(key);

        if(min_value == SpecValues.ERROR_VALUE) {
            min_value = Double.MIN_VALUE;
            point = SpecValues.NOTHING_STR;
        }
        else {
            if(value >= min_value) {
                point = SpecValues.getPoint("装甲", value, false);
            }
            else {
                point = SpecValues.NOTHING_STR;
            }
        }

        if(point.equals(SpecValues.NOTHING_STR)) {
            ret = "BS:対象無し";
        }
        else {
            ret = String.format("BS:%s(%s)以下", point, SpecValues.getSpecUnit(value, "装甲"));
        }

        // CS時の情報を追加
        if(target_data.isShotWeapon()) {
            String cs_key = "";
            if(key.equals(BBData.ARMOR_BREAK_KEY)) {
                cs_key = BBData.ARMOR_CS_BREAK_KEY;
            }
            else if(key.equals(BBData.ARMOR_DOWN_KEY)) {
                cs_key = BBData.ARMOR_CS_DOWN_KEY;
            }
            else if(key.equals(BBData.ARMOR_KB_KEY)) {
                cs_key = BBData.ARMOR_CS_KB_KEY;
            }
            else {
                return ret;
            }

            double cs_value = target_data.getCalcValue(cs_key);
            if(cs_value >= min_value) {
                point = SpecValues.getPoint("装甲", cs_value, false);
            }
            else {
                point = SpecValues.NOTHING_STR;
            }

            if(point.equals(SpecValues.NOTHING_STR)) {
                ret = ret + " - CS:対象無し";
            }
            else {
                ret = String.format("%s - CS:%s(%s)以下",
                        ret,
                        SpecValues.getPoint("装甲", cs_value, false),
                        SpecValues.getSpecUnit(cs_value, "装甲"));
            }
        }

        return ret;
    }
}
