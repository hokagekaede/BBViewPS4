package hokage.kaede.gmail.com.StandardLib.Java;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ArrayList<String>とString[]を相互に変換する関数を備えたクラス。
 */
public class ListConverter {
	
	/**
	 * ArrayList<String>のデータをStringの配列に変換する。
	 * @param list 変換元のデータ
	 * @return 変換後のデータ
	 */
	public static final String[] convert(ArrayList<String> list) {
		return list.toArray(new String[0]);
	}
	
	/**
	 * Stringの配列をArrayList<String>のデータに変換する。
	 * @param array 変換元のデータ
	 * @return 変換後のデータ
	 */
	public static final ArrayList<String> convert(String[] array) {
		return new ArrayList<String>(Arrays.asList(array));
	}

}
