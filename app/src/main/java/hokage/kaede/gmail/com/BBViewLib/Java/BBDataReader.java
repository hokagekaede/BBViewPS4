package hokage.kaede.gmail.com.BBViewLib.Java;

import hokage.kaede.gmail.com.StandardLib.Java.FileIO;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * アプリ起動時にパーツ、武器のスペック情報を読み込むクラス。
 */
public class BBDataReader {
	
	private static final String NEWLINE = System.getProperty("line.separator");
	
	private static final String CATEGORY_STR = "■";
	private static final String SUB_CATEGORY_STR = "◆";
	private static final String WEAPON_TYPE_CATEGORY_STR = "●";
	private static final String TITLE_STR = "○";
	private static final String SPLIT_STR = "\t";
	
	/**
	 * 各種データを読み込む。
	 * パーツ、武器、チップ、マップ、素材、勲章のデータを読み込む。
	 * 
	 * 多重ループが多くなることで処理時間が極端に長くなるため、
	 * スイッチ武器のタイプBデータの読み込みにおいて、
	 * 共通関数を使用して既に入力済みのスイッチ武器を検索する手法での実装を断念。
	 * 本関数でタイプAのバッファを保持し、そこからデータを探し出す手法で実装する。
	 * 
	 * @param is 読み込む入力ストリーム。
	 * @return 
	 */
	public static final void read(InputStream is) {
		BBDataManager data_mng = BBDataManager.getInstance();
		
		String bb_data_str = FileIO.readInputStream(is, FileIO.ENCODE_UTF8);
		String[] bb_data_list = bb_data_str.split(NEWLINE);

		int size = bb_data_list.length;

		String buf = null;
		String category1 = null;
		String category2 = null;
		String category3 = null;
		String[] keys = null;
		String[] values = null;
		
		boolean isTypeA = false;
		boolean isTypeB = false;
		ArrayList<BBData> switch_weapon_list = new ArrayList<BBData>();
		ArrayList<String> weapon_series_list = null;
		
		int count = 0;

		for(int i=0; i<size; i++) {
			buf = bb_data_list[i];
			
			if(buf.startsWith(CATEGORY_STR)) {
				category1 = buf.substring(1);
				category2 = null;
				category3 = null;

				isTypeA = false;
				isTypeB = false;
			}
			else if(buf.startsWith(SUB_CATEGORY_STR)) {
				category2 = buf.substring(1);
				category3 = null;

				isTypeA = false;
				isTypeB = false;
				
				// 武器の系統名を格納するリストを取得する
				weapon_series_list = SpecValues.getWeaponSeiresList(category1, category2);
			}
			else if(buf.startsWith(WEAPON_TYPE_CATEGORY_STR)) {
				category3 = buf.substring(1);

				if(category3.contains("タイプA")) {
					isTypeA = true;
					isTypeB = false;
					
					// 武器の系統名を格納する
					weapon_series_list.add(category3.replace("(タイプA)", ""));
				}
				else if(category3.contains("タイプB")) {
					isTypeA = false;
					isTypeB = true;
				}
				else {
					isTypeA = false;
					isTypeB = false;
					switch_weapon_list.clear();

					// 武器の系統名を格納する
					weapon_series_list.add(category3);
				}
			}
			else if(buf.startsWith(TITLE_STR)) {
				buf = buf.substring(1);
				keys = buf.split(SPLIT_STR);
			}
			else if(buf.indexOf(SPLIT_STR) > 0) {
				values = buf.split(SPLIT_STR);
				
				BBData data_buf = new BBData();
				if(category1 != null) { data_buf.addCategory(category1); }
				if(category2 != null) { data_buf.addCategory(category2); }
				if(category3 != null) { data_buf.addCategory(category3); }
				data_buf.setList(keys, values);
				
				if(isTypeB) {
					BBData typeA_data = null;
					int typeA_size = switch_weapon_list.size();
					for(int j=0; j<typeA_size; j++) {
						BBData typeA_buf = switch_weapon_list.get(j);
						if(typeA_buf.get("名称").equals(data_buf.get("名称"))) {
							typeA_data = typeA_buf;
							break;
						}
					}

					typeA_data.setTypeB(data_buf);
				}
				else {
					data_buf.id = count;
					count++;
					
					data_mng.add(data_buf);
					
					if(isTypeA) {
						switch_weapon_list.add(data_buf);
					}
				}
			}
			else {
				// DO NOTHING
			}
		}
	}
}
