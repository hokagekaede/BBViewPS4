package hokage.kaede.gmail.com.BBViewLib.Java;

import java.net.URLConnection;
import java.util.ArrayList;

import hokage.kaede.gmail.com.StandardLib.Java.KeyValueStore;
import hokage.kaede.gmail.com.StandardLib.Java.NetAccess;

/**
 * BB.NETのページを解析するクラス。
 */
public class BBNetPageParser {

	private static final String LOGIN_URL = "https://pc.borderbreak.net/bb_p/login.do";
	private static final String BASE_URL  = "https://pc.borderbreak.net/bb_p/";
	private static final String ENCODE = "Shift_JIS";
	
	private static String sProgressMessage = "";
	private static String sErrorMessage = "";
	private static String sPageString = "";

	public static String getProgressMessage() {
		return sProgressMessage;
	}
	
	public static String getErrorMessage() {
		return sErrorMessage;
	}
	
	public static String getPageString() {
		return sPageString;
	}
	
	/**
	 * カード名の一覧を取得する
	 * @param uid
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static boolean getCardList(String uid, String password) throws Exception {

		BBNetDatabase database = BBNetDatabase.getInstance();
		KeyValueStore sCardList = new KeyValueStore();
		
		// Cookieを有効にする
		sProgressMessage = "Cookie初期化";
		NetAccess.initCookie();
		
		// カード情報を取得する。
		sCardList = readTopPage(uid, password);
		if(sCardList == null) {
			return false;
		}

		int size = sCardList.size();
		for(int i=0; i<BBNetDatabase.CARD_MAX; i++) {
			String card_name = BBNetDatabase.NO_CARD_DATA;
			if(i < size) {
				String card_name_buf = sCardList.getKey(i);
				
				if(!card_name.equals("カード登録")) {
					card_name = card_name_buf;
				}
			}

			database.setCardNumber(i);
			database.setCardName(sCardList.getKey(i));
		}

		return true;
	}
	
	/**
	 * 指定カードの情報を取得する
	 * @param uid
	 * @param password
	 * @param card_idx
	 * @return
	 * @throws Exception
	 */
	public static boolean parse(String uid, String password, int card_idx) throws Exception {
		BBNetDatabase database = BBNetDatabase.getInstance();
		KeyValueStore sCardList = new KeyValueStore();
		
		// Cookieを有効にする
		sProgressMessage = "Cookie初期化";
		NetAccess.initCookie();
		
		// カード情報を取得する。
		sCardList = readTopPage(uid, password);
		if(sCardList == null) {
			return false;
		}
		
		// カード名を保存する。
		int size = sCardList.size();
		for(int i=0; i<size; i++) {
			String card_name = sCardList.getKey(i);
			if(card_name.equals("カード登録")) {
				break;
			}
			
			database.setCardNumber(i);
			database.setCardName(sCardList.getKey(i));
		}

		database.setCardNumber(card_idx);
		database.clear(card_idx);
		
		String card_name = sCardList.getKey(card_idx);
		loadData(sCardList.get(card_idx), card_name);
		
		database.save();
		
		return true;
	}
	
	private static void loadData(String start_url, String card_name) throws Exception {
		String buf_url_str;
		String my_items_url_str = getUrlFromNextPage(start_url, "状態/所持品確認");
		
		BBNetDatabase database = BBNetDatabase.getInstance();
		
		sProgressMessage = card_name + "の頭部パーツデータ取得";
		buf_url_str = getUrlFromNextPage(my_items_url_str, "機体");
		getPartsList(buf_url_str, database.getHeadParts(), "HEAD");

		sProgressMessage = card_name + "の胴部パーツデータ取得";
		buf_url_str = getUrlFromNextPage(my_items_url_str, "機体");
		getPartsList(buf_url_str, database.getBodyParts(), "BODY");

		sProgressMessage = card_name + "の腕部パーツデータ取得";
		buf_url_str = getUrlFromNextPage(my_items_url_str, "機体");
		getPartsList(buf_url_str, database.getArmsParts(), "ARM");

		sProgressMessage = card_name + "の脚部パーツデータ取得";
		buf_url_str = getUrlFromNextPage(my_items_url_str, "機体");
		getPartsList(buf_url_str, database.getLegsParts(), "LEG");

		sProgressMessage = card_name + "の強襲兵装の武器データ取得";
		buf_url_str = getUrlFromNextPage(my_items_url_str, "機体");
		getWeaponList(buf_url_str, database.getWeapons(), "強襲(");

		sProgressMessage = card_name + "の重火力兵装の武器データ取得";
		buf_url_str = getUrlFromNextPage(my_items_url_str, "機体");
		getWeaponList(buf_url_str, database.getWeapons(), "重火力(");

		sProgressMessage = card_name + "の遊撃兵装の武器データ取得";
		buf_url_str = getUrlFromNextPage(my_items_url_str, "機体");
		getWeaponList(buf_url_str, database.getWeapons(), "遊撃(");

		sProgressMessage = card_name + "の支援兵装の武器データ取得";
		buf_url_str = getUrlFromNextPage(my_items_url_str, "機体");
		getWeaponList(buf_url_str, database.getWeapons(), "支援(");

		sProgressMessage = card_name + "のチップデータ取得";
		buf_url_str = getUrlFromNextPage(my_items_url_str, "チップ");
		getChipList(buf_url_str, database.getChips());

		sProgressMessage = card_name + "の素材データ取得";
		buf_url_str = getUrlFromNextPage(my_items_url_str, "素材");
		getMaterialList(buf_url_str, database.getMaterials());

		sProgressMessage = card_name + "の勲章データ取得";
		buf_url_str = getUrlFromNextPage(my_items_url_str, "勲章");
		getMedalList(buf_url_str, database.getMedals());
		
		sProgressMessage = card_name + "のシードデータ取得";
		buf_url_str = getUrlFromNextPage(start_url, "シード");
		getSeedList(buf_url_str, database.getSeeds());
	}
	
	/**
	 * BB.NETにログインし、カード名とURLを取得する。
	 * @param uid
	 * @param password
	 * @return
	 */
	public static KeyValueStore readTopPage(String uid, String password) throws Exception {
		
		// BB.NETにログインし、更新通知画面またはトップメニューのページを取得する。
		URLConnection uc = NetAccess.getConnection(LOGIN_URL);

		sProgressMessage = "ログイン";
		try {
			NetAccess.outputPassword(uc, uid, password);
		} catch(Exception e) {
			// IDおよびパスワード書き込みは通信ができればエラーは発生しない。
			sErrorMessage = "通信障害が発生している可能性があります。";
			throw e;
		}
		
		String page_str = readPage(uc);
		if(page_str.equals("")) {
			return null;
		}
		
		sProgressMessage = "ログインページの解析";
		String url = BBNetPageParser.getUrl(page_str, "トップメニュー", 1);

		// ログイン後の画面が更新通知画面の場合(トップメニューへのリンクがある場合)
		// トップメニューのページデータを取得する。
		if(!url.equals("")) {
			sProgressMessage = "トップページの解析";
			uc = NetAccess.getConnection(BASE_URL + url);
			page_str = readPage(uc);
			if(page_str.equals("")) {
				return null;
			}
		}

		// トップメニューからカード情報を取得する。
		page_str = page_str.substring(page_str.indexOf("▼カード情報/登録"), page_str.indexOf("▼アーケード連動"));
		return BBNetPageParser.getUrlList(page_str);
	}
	
	/**
	 * 指定のURLのページデータから指定の文字列のリンクを取得する。
	 * @param url_str
	 * @param link_name
	 * @return
	 */
	public static String getUrlFromNextPage(String url_str, String link_name) throws Exception {
		URLConnection uc = NetAccess.getConnection(BASE_URL + url_str);
		String page_str = readPage(uc);
		return BBNetPageParser.getUrl(page_str, link_name, 1);
	}

	/**
	 * 機体パーツデータを取得する。
	 * 一旦、格納済みのデータを消去し、取得したデータを上書きする。
	 * @param url_str 機体データのページ
	 * @param output 機体データのストア
	 * @param parts_type 機体パーツの種類
	 * @return
	 */
	public static void getPartsList(String url_str, KeyValueStore output, String parts_type) throws Exception {
		URLConnection uc = NetAccess.getConnection(BASE_URL + url_str);
		String page_str = readPage(uc);
		
		readPartsList(getUrlStartsWith(page_str, parts_type, 1), output);
	}

	/**
	 * 機体パーツデータを取得する。
	 * @param url_str
	 * @param output
	 * @return
	 */
	public static void readPartsList(String url_str, KeyValueStore output) throws Exception {
		URLConnection uc = NetAccess.getConnection(BASE_URL + url_str);
		String page_str = readPage(uc);
		KeyValueStore input = BBNetPageParser.getUrlList(page_str);

		BBDataManager manager = BBDataManager.getInstance();
		
		ArrayList<String> list = input.getKeys();
		int size = list.size();
		for(int i=0; i<size; i++) {
			String link_data = list.get(i);
			
			if(manager.isParts(link_data)) {
				output.set(link_data, "-");
			}
		}
		
		// 「次へ」のページがある場合は、次のページについても同様の処理を繰り返し行う。
		String next_url = getUrl(page_str, "次へ[#]", 1);
		if(!next_url.equals("")) {
			readPartsList(next_url, output);
		}
	}

	/**
	 * 武器データを取得する。
	 * @param url_str
	 * @param output
	 * @param blust_type
	 * @return
	 */
	public static void getWeaponList(String url_str, KeyValueStore output, String blust_type) throws Exception {
		URLConnection uc = NetAccess.getConnection(BASE_URL + url_str);
		String page_str = readPage(uc);
		
		// 状態/所持品確認 機体 ページを開く
		String main_page = getUrlStartsWith(page_str, blust_type, 1);
		
		// 各種兵装のページのデータを取得する
		uc = NetAccess.getConnection(BASE_URL + main_page);
		page_str = readPage(uc);

		String[] base_urls = {
			getUrlStartsWith(page_str, "MAIN", 1),
			getUrlStartsWith(page_str, "SUB", 1),
			getUrlStartsWith(page_str, "EXTRA", 1),
			getUrlStartsWith(page_str, "SPECIAL", 1),
		};
		
		int size = base_urls.length;
		for(int i=0; i<size; i++) {
			readWeaponList(base_urls[i], output);
		}
	}

	/**
	 * 武器データを取得する。
	 * @param url_str
	 * @param output
	 * @return
	 */
	public static void readWeaponList(String url_str, KeyValueStore output) throws Exception {
		URLConnection uc = NetAccess.getConnection(BASE_URL + url_str);
		String page_str = readPage(uc);
		KeyValueStore input = BBNetPageParser.getUrlList(page_str);

		BBDataManager manager = BBDataManager.getInstance();
		
		ArrayList<String> list = input.getKeys();
		int size = list.size();
		for(int i=0; i<size; i++) {
			String link_data = list.get(i);
			
			if(manager.isWeapon(link_data)) {
				output.set(link_data, "-");
			}
		}
		
		// 「次へ」のページがある場合は、次のページについても同様の処理を繰り返し行う。
		String next_url = getUrl(page_str, "次へ[#]", 1);
		if(!next_url.equals("")) {
			readWeaponList(next_url, output);
		}
	}
	
	/**
	 * チップのデータを取得する。
	 * @param url_str
	 * @param output
	 * @throws Exception
	 */
	public static void getChipList(String url_str, KeyValueStore output) throws Exception {
		URLConnection uc = NetAccess.getConnection(BASE_URL + url_str);
		String page_str = readPage(uc);
		KeyValueStore input = BBNetPageParser.getUrlList(page_str);
		
		BBDataManager manager = BBDataManager.getInstance();
		
		ArrayList<String> list = input.getKeys();
		int size = list.size();
		for(int i=0; i<size; i++) {
			String link_data = list.get(i);
			
			if(manager.isChip(link_data)) {
				output.set(link_data, "-");
			}
		}
		
		// 「次へ」のページがある場合は、次のページについても同様の処理を繰り返し行う。
		String next_url = getUrl(page_str, "次へ[#]", 1);
		if(!next_url.equals("")) {
			getChipList(next_url, output);
		}
	}

	/**
	 * 素材データを取得する。
	 * @param url_str
	 * @param output
	 * @return
	 */
	public static void getMaterialList(String url_str, KeyValueStore output) throws Exception {
		URLConnection uc = NetAccess.getConnection(BASE_URL + url_str);
		String page_str = readPage(uc);
		KeyValueStore input = BBNetPageParser.getUrlList(page_str);

		BBDataManager manager = BBDataManager.getInstance();
		
		ArrayList<String> list = input.getKeys();
		int size = list.size();
		for(int i=0; i<size; i++) {
			String link_data = list.get(i);

			int num_head_idx = link_data.indexOf("(");
			int num_tail_idx = link_data.indexOf(")");
			
			if(num_head_idx < 0 || num_tail_idx < 0) {
				continue;
			}

			String name = link_data.substring(0, num_head_idx);
			String value = link_data.substring(num_head_idx + 1, num_tail_idx);

			if(manager.isMaterial(name)) {
				output.set(name, value);
			}
		}
	}

	/**
	 * 勲章データを取得する。
	 * @param url_str
	 * @param output
	 * @return
	 */
	public static void getMedalList(String url_str, KeyValueStore output) throws Exception {
		URLConnection uc = NetAccess.getConnection(BASE_URL + url_str);
		String page_str = readPage(uc);
		
		String[] base_urls = {
			getUrl(page_str, "攻撃系", 1),
			getUrl(page_str, "支援系", 1),
			getUrl(page_str, "特別系", 1),
			getUrl(page_str, "攻撃系", 2),
			getUrl(page_str, "支援系", 2),
			getUrl(page_str, "特別系", 2)
		};
		
		int size = base_urls.length;
		for(int i=0; i<size; i++) {
			readMedalList(base_urls[i], output);
		}
	}
	
	/**
	 * 勲章のデータを取得する。
	 * @param url_str
	 * @param output
	 * @throws Exception
	 */
	private static void readMedalList(String url_str, KeyValueStore output) throws Exception {
		URLConnection uc = NetAccess.getConnection(BASE_URL + url_str);
		String page_str = readPage(uc);
		KeyValueStore input = BBNetPageParser.getUrlList(page_str);

		BBDataManager manager = BBDataManager.getInstance();
		
		ArrayList<String> list = input.getKeys();
		int size = list.size();
		for(int i=0; i<size; i++) {
			String link_data = list.get(i);
			int num_head_idx = link_data.indexOf("(");
			int num_tail_idx = link_data.indexOf(")");
			
			if(num_head_idx < 0 || num_tail_idx < 0) {
				continue;
			}
			
			String name = link_data.substring(0, num_head_idx);
			String value = link_data.substring(num_head_idx + 1, num_tail_idx);
		
			if(manager.isMedal(name)) {
				output.set(name, value);
			}
		}
		
		// 「次へ」のページがある場合は、次のページについても同様の処理を繰り返し行う。
		String next_url = getUrl(page_str, "次へ[#]", 1);
		if(!next_url.equals("")) {
			readMedalList(next_url, output);
		}
	}

	/**
	 * シードデータを取得する。
	 * @param url_str
	 * @param output
	 * @return
	 */
	public static void getSeedList(String url_str, KeyValueStore output) throws Exception {
		URLConnection uc = NetAccess.getConnection(BASE_URL + url_str);
		String page_str = readPage(uc);
		
		String[] seed_names = {
			"<span>ピュアシード</span>：",
			"<span>ディープシード</span>：",
			"<span>ナチュラルシード</span>：",
			"<span>ライトシード</span>：",
			"<span>コアシード</span>："
		};
		
		// 所持数上限時の赤文字タグを削除する
		page_str = page_str.replace("<font color=\"red\">", "");
		page_str = page_str.replace("</font>", "");
		
		int size = seed_names.length;
		for(int i=0; i<size; i++) {
			int head_idx = page_str.indexOf(seed_names[i]);
			int tail_idx = page_str.indexOf("<br>", head_idx);
			String seed_count = page_str.substring(head_idx + seed_names[i].length(), tail_idx);

			String name = seed_names[i].replace("<span>", "").replace("</span>：", "");
			output.set(name, seed_count);
		}
	}
	
	/**
	 * ページソースからURLの一覧を取得する。
	 * @param web_data
	 * @return
	 */
	private static KeyValueStore getUrlList(String web_data) {
		KeyValueStore data = new KeyValueStore();
		
		String link_tag_head = "<a href=\"";
		String link_tag_tail = "</a>";
		
		int head_len = link_tag_head.length();
		int index = 0;
		int end_pointer = 0;
		while((index = web_data.indexOf(link_tag_head, index)) != -1) {
			String url, title;
			
			end_pointer = web_data.indexOf("\"", index + head_len);
			url = web_data.substring(index + head_len, end_pointer);
			
			index = web_data.indexOf(">", index);
			end_pointer = web_data.indexOf(link_tag_tail, index);
			title = web_data.substring(index + 1, end_pointer);
			
			data.set(title, url);
			
			index = end_pointer;
		}
		
		return data;
	}

	/**
	 * ページソースからURLを取得する。
	 * @param web_data
	 * @param keyword
	 * @param number
	 * @return
	 */
	private static String getUrl(String web_data, String keyword, int number) {
		String link_tag_head = "<a href=\"";
		String link_tag_tail = "</a>";
		
		String ret = "";
		String url = "";
		String title = "";
		
		int head_len = link_tag_head.length();
		int index = 0;
		int end_pointer = 0;
		int count = 0;
		
		while((index = web_data.indexOf(link_tag_head, index)) != -1) {
			
			// URL文字列を取得する
			end_pointer = web_data.indexOf("\"", index + head_len);
			url = web_data.substring(index + head_len, end_pointer);
			
			// <a href="">xxx</a>のxxxの部分を取得する
			index = web_data.indexOf(">", index);
			end_pointer = web_data.indexOf(link_tag_tail, index);
			title = web_data.substring(index + 1, end_pointer);

			// 指定のキーワードと同じ場合、URLを返す
			if(keyword.equals(title)) {
				count++;
				
				if(count >= number) {
					ret = url;
					break;
				}
			}
			
			index = end_pointer;
		}
		
		return ret;
	}

	/**
	 * ページソースから先頭文字列が等しいURLを取得する。
	 * @param web_data
	 * @param keyword
	 * @param number
	 * @return
	 */
	private static String getUrlStartsWith(String web_data, String keyword, int number) {
		String link_tag_head = "<a href=\"";
		String link_tag_tail = "</a>";
		
		String ret = "";
		String url = "";
		String title = "";
		
		int head_len = link_tag_head.length();
		int index = 0;
		int end_pointer = 0;
		int count = 0;
		
		while((index = web_data.indexOf(link_tag_head, index)) != -1) {
			
			// URL文字列を取得する
			end_pointer = web_data.indexOf("\"", index + head_len);
			url = web_data.substring(index + head_len, end_pointer);
			
			// <a href="">xxx</a>のxxxの部分を取得する
			index = web_data.indexOf(">", index);
			end_pointer = web_data.indexOf(link_tag_tail, index);
			title = web_data.substring(index + 1, end_pointer);
			
			// 指定のキーワードと同じ場合、URLを返す
			if(title.startsWith(keyword)) {
				count++;
				
				if(count >= number) {
					ret = url;
					break;
				}
			}
			
			index = end_pointer;
		}

		return ret;
	}
	
	private static String readPage(URLConnection uc) {
		String page_str = NetAccess.readString(uc, ENCODE);
		
		// デバッグ用にページデータを格納する。
		sPageString = page_str;
		
		if(page_str.equals("")) {
			sErrorMessage = "通信障害が発生している可能性があります。";
			return "";
		}
		else if(page_str.contains("ログインできませんでした")) {
			sErrorMessage = "SEGA IDまたはパスワードが誤っている可能性があります。";
			return "";
			
		}
		else if(page_str.contains("BB.NETでは共有されている複数の端末/ブラウザでの同時操作は行なえません。")) {
			sErrorMessage = "他アプリ/他ブラウザでアクセス中の可能性があります。";
			return "";
			
		}
		else if(page_str.contains("不正なアクセスを検出/記録しました。")) {
			sErrorMessage = "アクセス不正が発生しました。";
			return "";
		}
		
		return page_str;
	}
	
}
