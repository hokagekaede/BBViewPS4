package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import android.content.Intent;

/**
 * インテント制御を行うクラス
 */
public class IntentManager {

	private static final String INTENTKYE_SELECTED_DATAID = "INTENTKYE_SELECTED_DATAID";
	
	/**
	 * インテントからデータを取得する。
	 * @param intent インテント
	 * @return 取得したデータ
	 */
	public static BBData getSelectedData(Intent intent) {
		return BBDataManager.getInstance().getData(intent.getIntExtra(INTENTKYE_SELECTED_DATAID, 0));
	}
	
	/**
	 * インテントから複数のデータを取得する。
	 * @param intent
	 * @return
	 */
	public static BBData[] getSelectedDataArray(Intent intent) {
		BBDataManager manager = BBDataManager.getInstance();
		
		int[] ids = intent.getIntArrayExtra(INTENTKYE_SELECTED_DATAID);
		int size = ids.length;
		BBData[] data_array = new BBData[size];
		
		for(int i=0; i<size; i++) {
			data_array[i] = manager.getData(ids[i]);
		}
		
		return data_array;
	}
	
	/**
	 * インテントにデータを追加する。ただし実際に追加するのはID値とする。
	 * @param intent インテント
	 * @param data 追加するデータ
	 */
	public static void setSelectedData(Intent intent, BBData data) {
		intent.putExtra(INTENTKYE_SELECTED_DATAID, data.id);
	}

	/**
	 * インテントにデータを追加する。ただし実際に追加するのはID値とする。
	 * @param intent インテント
	 * @param data 追加するデータ
	 */
	public static void setSelectedDataArray(Intent intent, BBData... data) {
		int size = data.length;
		int[] ids = new int[size];
		
		for(int i=0; i<size; i++) {
			ids[i] = data[i].id;
		}
		
		intent.putExtra(INTENTKYE_SELECTED_DATAID, ids);
	}
}
