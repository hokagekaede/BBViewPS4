package hokage.kaede.gmail.com.StandardLib.Java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * ArrayListのデータをファイルに入出力するクラス。
 * ArrayListのシリアライズは保証できないので、継承は行わず必要なメソッドだけ個別に定義する。
 */
public class FileArrayList {
	public static final String CACHE_NAME = "cache.dat";
	public static final String FILELIST_DAT = "filelist.dat";
	
	private static final String FILESEPARATOR = System.getProperty("file.separator");
	private static final String NEWLINE = System.getProperty("line.separator");
	private static final String STR_ENCODE = "Shift_JIS";

	private ArrayList<String> mList = new ArrayList<String>();
	
	private String mFilePath;
	private String mFileName;
	private String mTargetDir;
	private String mEncode;
	
	/**
	 * コンストラクタ
	 * @param dirname
	 */
	public FileArrayList(String dirname) {
		super();
		this.mTargetDir = dirname + FILESEPARATOR;
		this.mFilePath = dirname + FILESEPARATOR + CACHE_NAME;
		this.mFileName = CACHE_NAME;
		this.mEncode = STR_ENCODE;
	}
	
	/**
	 * コンストラクタ
	 * @param dirname
	 * @param filename
	 */
	public FileArrayList(String dirname, String filename) {
		super();
		this.mTargetDir = dirname + FILESEPARATOR;
		this.mFilePath = dirname + FILESEPARATOR + filename;
		this.mFileName = filename;
		this.mEncode = STR_ENCODE;
	}
	
	public void setEncode(String encode) {
		mEncode = encode;
	}

	/**
	 * キャッシュの保存
	 * @param filename
	 */
	public void save(String filename) {
		FileOutputStream fos = null;
		OutputStreamWriter osr = null;
		BufferedWriter bw = null;
		
		String data = "";
		String filepath = this.mTargetDir + filename;
		
		// キャッシュするデータの取得
		
		int len = mList.size();
		for(int i=0; i<len; i++) {
			data = data + mList.get(i) + NEWLINE;
		}
		
		// ファイルにキャッシュする
		try { 
			fos = new FileOutputStream(filepath, false);
			osr = new OutputStreamWriter(fos, mEncode);
			bw  = new BufferedWriter(osr);
			
			bw.write(data);
			bw.flush();
		
		} catch(Exception e) {
			e.printStackTrace();
		
		} finally {
			try { bw.close();  } catch (Exception e2) { }
			try { osr.close(); } catch (Exception e2) { }
			try { fos.close(); } catch (Exception e2) { }
		}
	}
	
	/**
	 * キャッシュの保存
	 */
	public void save() {
		save(mFileName);
	}
	
	/**
	 * ファイルを読み込む。
	 * @return
	 */
	public boolean load() {
		return load(mFilePath);
	}
	
	/**
	 * ファイルを読み込む。
	 * @param filename
	 * @return
	 */
	public boolean load(String filename) {
		boolean ret = true;
	
		FileInputStream fis = null;

		try { 
			fis = new FileInputStream(filename);
			ret = load(fis);
		
		} catch(Exception e) {
			ret = false;
			e.printStackTrace();
		
		} finally {
			try { fis.close(); } catch (Exception e2) { }
		}
		
		return ret;
	}
	
	/**
	 * ファイルを読み込む。
	 * @param is
	 * @return
	 */
	public boolean load(InputStream is) {
		boolean ret = true;
		
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		// リストを初期化する
		mList.clear();
		
		try { 
			isr = new InputStreamReader(is, mEncode);
			br  = new BufferedReader(isr);
			
			String line;
			while((line = br.readLine()) != null) {
				mList.add(line);
			}
		
		} catch(Exception e) {
			ret = false;
			e.printStackTrace();
		
		} finally {
			try { br.close();  } catch (Exception e2) { }
			try { isr.close(); } catch (Exception e2) { }
		}
		
		return ret;
	}
	
	/**
	 * ファイルの有無取得
	 * @return
	 */
	public boolean exists() {
		return new File(mFilePath).exists();
	}
	
	/**
	 * ファイルの削除
	 */
	public void delete() {
		File file = null;
		
		try {
			file = new File(mFilePath);
			file.delete();
			
		} catch(Exception e) {
			e.printStackTrace();
			
		} finally {
			file = null;
		}
	}
	
	/**
	 * データを追加する。
	 * @param item 追加する文字列
	 */
	public void add(String item) {
		mList.add(item);
	}
	
	/**
	 * データの有無を取得する。
	 * @param item チェック対象の文字列
	 * @return 存在する場合はtrueを返し、存在しない場合はfalseを返す。
	 */
	public boolean exist(String item) {
		if(mList.indexOf(item) > -1) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * データの位置を取得する。
	 * @param item 対象の文字列
	 * @return データの位置
	 */
	public int indexOf(String item) {
		return mList.indexOf(item);
	}
	
	/**
	 * データを削除する。
	 * @param index 削除するデータの位置
	 */
	public void remove(int index) {
		mList.remove(index);
	}

	/**
	 * データをクリアする。
	 */
	public void clear() {
		mList.clear();
	}
	
	/**
	 * データの数を取得する。
	 * @return
	 */
	public int size() {
		return mList.size();
	}
}
