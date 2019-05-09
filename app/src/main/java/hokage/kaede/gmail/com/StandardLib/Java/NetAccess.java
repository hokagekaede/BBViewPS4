package hokage.kaede.gmail.com.StandardLib.Java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Webデータへの入出力機能を備えたクラス。
 */
public class NetAccess {
	
	private static final String REQ_CODE_GET = "GET";

	/**
	 * Webデータを文字列データとして読み込む
	 * @param path
	 * @return
	 */
	public static String readString(String path, String encode) {
		HttpURLConnection connection = null;
		String ret = "";
		
		try {
			URL uri = new URL(path);
			connection = (HttpURLConnection)(uri.openConnection());
			connection.setRequestMethod(REQ_CODE_GET);
			connection.connect();
			ret = readString(connection, encode);
			
		} catch(Exception e) {
			ret = "";

		} finally {
			try { connection.disconnect(); } catch(Exception e) { };
		}
		
		return ret;
	}
	
	/**
	 * Webページを文字列データとして読み込む
	 * @param connection
	 * @param encode
	 * @return
	 */
	public static String readString(URLConnection connection, String encode) {
		String ret = "";
		InputStream is = null;
		InputStreamReader ir = null;
		BufferedReader br = null;
		
		try {
			is = connection.getInputStream();
			ir = new InputStreamReader(is, encode);
			br = new BufferedReader(ir);

			String buffer;
			while((buffer = br.readLine()) != null) {
				ret = ret + buffer;
			}
			
		} catch(Exception e) {
			ret = "";

		} finally {
			try { if(br != null) { br.close(); } } catch(Exception e) { };
			try { if(ir != null) { ir.close(); } } catch(Exception e) { };
			try { if(is != null) { is.close(); } } catch(Exception e) { };
		}
		
		return ret;
	}
	
	/**
	 * ユーザIDとパスワードを設定する。
	 * @param uc
	 * @param uid
	 * @param pass
	 */
	public static boolean outputPassword(URLConnection uc, String uid, String pass) throws IOException {
		boolean is_success = false;
		OutputStream os = null;
		
		uc.setDoOutput(true);

		try {
			os = uc.getOutputStream();
			PrintStream ps = new PrintStream(os);
			ps.print("uid=" + uid + "&password=" + pass);
			ps.flush();
			ps.close();
			os.close();
			is_success = true;

		} finally {
			try { if(os != null) { os.close(); }} catch(IOException e2) { }
		}
		
		return is_success;
	}
	
	public static URLConnection getConnection(String path) throws Exception {
		URL url = new URL(path);
		return url.openConnection();
	}
	
	/**
	 * Cookieの使用を有効にする。
	 */
	public static void initCookie() {
		CookieManager manager = new CookieManager();
		CookieHandler.setDefault(manager);
	}
}
