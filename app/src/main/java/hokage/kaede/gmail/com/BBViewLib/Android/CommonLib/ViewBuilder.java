package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataComparator;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.SpecArray.SpecRow;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;
import android.content.Context;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 基本的なビューを生成するクラス
 */
public class ViewBuilder {
	
	/**
	 * テーブルビューの行を生成する
	 * @param context 生成する画面
	 * @param col データ行
	 * @return テーブルビューの行
	 */
	public static TableRow createTableRow(Context context, SpecRow col) {
		return createTableRow(context, col.getColors(), col.getValues());
	}
	
	/**
	 * テーブルビューの行を生成する
	 * @param context 生成する画面
	 * @param args 表示する文字列
	 * @return テーブルビューの行
	 */
	public static TableRow createTableRow(Context context, int color, String... args) {
		TableRow row = new TableRow(context);
		
		int len = args.length;
		for(int i=0; i<len; i++) {
			TextView text = new TextView(context);
			text.setText(args[i]);
			text.setPadding(5, 0, 5, 0);
			text.setTextColor(color);
			text.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_SMALL));
			row.addView(text);
		}
		
		return row;
	}

	/**
	 * テーブルビューの行を生成する
	 * @param context 生成する画面
	 * @param size フォンサイズ
	 * @param args 表示する文字列
	 * @return テーブルビューの行
	 */
	public static TableRow createTableRow(Context context, int color, float size, String... args) {
		TableRow row = new TableRow(context);
		
		int len = args.length;
		for(int i=0; i<len; i++) {
			TextView text = new TextView(context);
			text.setText(args[i]);
			text.setPadding(5, 0, 5, 0);
			text.setTextColor(color);
			text.setTextSize(size);
			row.addView(text);
		}
		
		return row;
	}

	/**
	 * テーブルビューの行を生成する。文字列の配列数と色番号の配列数を合わせること。
	 * @param context 現在表示中の画面
	 * @param colors 各列の色
	 * @param args 表示する文字列
	 * @return テーブルビューの行
	 */
	public static TableRow createTableRow(Context context, int[] colors, String... args) {
		TableRow row = new TableRow(context);

		int len = args.length;
		for(int i=0; i<len; i++) {
			TextView text = new TextView(context);
			text.setText(args[i]);
			text.setPadding(5, 0, 5, 0);
			text.setTextColor(colors[i]);
			text.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_SMALL));
			row.addView(text);
		}
		
		return row;
	}

	/**
	 * テーブルビューの行の色を取得する。
	 * @param from_data 比較元のデータ
	 * @param to_data 比較先のデータ
	 * @param target_key 対象のスペック項目
	 * @return 色データ(配列3)
	 */
	public static int[] getColors(BBData from_data, BBData to_data, String target_key) {
		int[] ret = new int[3];
		
		BBDataComparator cmp_data = new BBDataComparator(target_key, true, true);
		int cmp = cmp_data.compare(from_data, to_data);
		
		if(cmp > 0) {
			ret[0] = SettingManager.getColorWhite();
			ret[1] = SettingManager.getColorCyan();
			ret[2] = SettingManager.getColorMazenta();
		}
		else if(cmp < 0) {
			ret[0] = SettingManager.getColorWhite();
			ret[1] = SettingManager.getColorMazenta();
			ret[2] = SettingManager.getColorCyan();
		}
		else {
			ret[0] = SettingManager.getColorWhite();
			ret[1] = SettingManager.getColorWhite();
			ret[2] = SettingManager.getColorWhite();
		}
		
		return ret;
	}
	
	/**
	 * テーブルビューの行の色を取得する。
	 * @param from_value 比較元のデータ値
	 * @param to_value 比較先のデータ値
	 * @param target_key 対象のスペック項目
	 * @return 色データ(配列3)
	 */
	public static int[] getColors(int[] colors, double from_value, double to_value, String target_key) {
		
		BBDataComparator cmp_data = new BBDataComparator(target_key, true, true);
		int cmp = cmp_data.compareValue(from_value, to_value);
		
		if(cmp > 0) {
			colors[0] = SettingManager.getColorWhite();
			colors[1] = SettingManager.getColorCyan();
			colors[2] = SettingManager.getColorMazenta();
		}
		else if(cmp < 0) {
			colors[0] = SettingManager.getColorWhite();
			colors[1] = SettingManager.getColorMazenta();
			colors[2] = SettingManager.getColorCyan();
		}
		else {
			colors[0] = SettingManager.getColorWhite();
			colors[1] = SettingManager.getColorWhite();
			colors[2] = SettingManager.getColorWhite();
		}
		
		return colors;
	}

	/**
	 * テーブルビューの行の色を取得する。
	 * @param from_value 比較元のデータ値
	 * @param to_value 比較先のデータ値
	 * @param target_key 対象のスペック項目
	 * @return 色データ(配列3)
	 */
	public static int[] getColors(double from_value, double to_value, String target_key) {
		int[] ret = new int[3];
		return getColors(ret, from_value, to_value, target_key);
	}
	
	/**
	 * テキストビューを生成する。
	 * @param context 現在表示中の画面
	 * @param text テキスト
	 * @param flag テキストサイズの設定
	 * @param color 文字色
	 * @param bg_color 背景色
	 * @return テキストビュー
	 */
	public static TextView createTextView(Context context, String text, int flag, int color, int bg_color) {
		TextView text_view = new TextView(context);
		text_view.setText(text);
		text_view.setTextSize(BBViewSetting.getTextSize(context, flag));
		text_view.setTextColor(color);
		text_view.setBackgroundColor(bg_color);
		
		return text_view;
	}

	/**
	 * テキストビューを生成する。
	 * @param context 現在表示中の画面
	 * @param text テキスト
	 * @param flag テキストサイズの設定
	 * @param color 文字色
	 * @return テキストビュー
	 */
	public static TextView createTextView(Context context, String text, int flag, int color) {
		TextView text_view = new TextView(context);
		text_view.setText(text);
		text_view.setTextSize(BBViewSetting.getTextSize(context, flag));
		text_view.setTextColor(color);
		
		return text_view;
	}
	
	/**
	 * テキストビューを生成する
	 * @param context 現在表示中の画面
	 * @param text テキスト
	 * @param flag テキストサイズの設定
	 * @return テキストビュー
	 */
	public static TextView createTextView(Context context, String text, int flag) {
		return createTextView(context, text, flag, SettingManager.getColorWhite());
	}
}
