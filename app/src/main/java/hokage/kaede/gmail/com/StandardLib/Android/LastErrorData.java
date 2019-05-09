package hokage.kaede.gmail.com.StandardLib.Android;

import android.os.Build;

/**
 * 最後にエラーが発生した時の情報を管理するクラス。
 */
public class LastErrorData {
	private static String sErrorMessage = "";
	private static boolean sIsDebugMode = false;
	private static String sPageString = "";
	
	private static String ANDROID_VERSION = "バージョン：" + String.valueOf(Build.VERSION.SDK_INT);
	private static String MODEL_NAME = "機種名：" + Build.MODEL;
	
	/**
	 * デバッグモードを設定する。
	 * @param is_debug_mode デバッグモード
	 */
	public static void setDebugMode(boolean is_debug_mode) {
		sIsDebugMode = is_debug_mode;
	}
	
	/**
	 * デバッグモードが有効かどうかを判定する。
	 * @return デバッグモードが有効の場合はtrueを返し、向こうの場合はfalseを返す。
	 */
	public static boolean isDebugMode() {
		return sIsDebugMode;
	}
	
	/**
	 * エラー情報を設定する。
	 * @param e エラーデータ
	 */
	public static void setException(Exception e) {
		sErrorMessage = e.toString() + "\n";
		
		StackTraceElement[] traces = e.getStackTrace();
		int size = traces.length;
		
		for(int i=0; i<size; i++) {
			StackTraceElement trace = traces[i];
			sErrorMessage = sErrorMessage + "at " + trace.getClassName() + "." + trace.getMethodName() + "(" + trace.getFileName() + ":" + trace.getLineNumber() + ")\n";
		}
	}
	
	/**
	 * ページ情報を保持する
	 * @param page_string
	 */
	public static void setPageString(String page_string) {
		sPageString = page_string;
	}
	
	/**
	 * エラーメッセージを取得する。
	 * @return エラーメッセージ
	 */
	public static String getErrorMessage() {
		return MODEL_NAME + "\n" + ANDROID_VERSION + "\n" + sErrorMessage + "\n\n" + sPageString;
	}
}
