package hokage.kaede.gmail.com.BBViewLib.Android.CustomLib;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.SpecValues;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.ViewBuilder;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * パーツの比較ダイアログを表示するクラス。
 */
public class CmpPartsTableBuilder {

	private Activity mActivity;
	
	/**
	 * 初期化を行う。
	 * @param activity
	 */
	public CmpPartsTableBuilder(Activity activity) {
		this.mActivity = activity;
	}
	
	/**
	 * パーツの比較ビューを生成する
	 * @param from_data 比較元のデータ
	 * @param to_data 比較先のデータ
	 * @return 比較結果を示すビュー
	 */
	public void showDialog(BBData from_data, BBData to_data) {
		TableLayout table = new TableLayout(mActivity);
		ArrayList<TableRow> rows;
		
		rows = createCmpPartsRows(from_data, to_data);
		
		int size = rows.size();
		for(int i=0; i<size; i++) {
			table.addView(rows.get(i));
		}
		
		// ダイアログを表示
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("パーツ性能比較");
		builder.setView(table);
		builder.setPositiveButton("閉じる", null);

		Dialog dialog = builder.create();
		dialog.setOwnerActivity(mActivity);
		dialog.show();
	}
	
	/**
	 * パーツの比較行のリストを生成する
	 * @param from_data 比較元のデータ
	 * @param to_data 比較先のデータ
	 * @return 比較結果を示すビュー
	 */
	private ArrayList<TableRow> createCmpPartsRows(BBData from_data, BBData to_data) {
		ArrayList<TableRow> rows = new ArrayList<TableRow>();
		
		String[] cmp_target = BBDataManager.getCmpTarget(from_data);
		int size = cmp_target.length;
		
		rows.add(ViewBuilder.createTableRow(mActivity, SettingManager.getColorYellow(), "名称", from_data.get("名称"), to_data.get("名称")));
		
		for(int i=0; i<size; i++) {
			String target_key = cmp_target[i];
			int[] colors = ViewBuilder.getColors(from_data, to_data, target_key);
			
			String from_point = from_data.get(target_key);
			String to_point = to_data.get(target_key);
			
			if(from_point.equals(BBData.STR_VALUE_NOTHING) || to_point.equals(BBData.STR_VALUE_NOTHING)) {
				continue;
			}

			String from_str = SpecValues.getSpecUnit(from_data, target_key);
			String to_str = SpecValues.getSpecUnit(to_data, target_key);
			rows.add(ViewBuilder.createTableRow(mActivity, colors, target_key, from_str, to_str));
		}
		
		return rows;
	}
}
