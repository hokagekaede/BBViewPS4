package hokage.kaede.gmail.com.StandardLib.Java;

import java.util.ArrayList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

/**
 * KeyValueStoreの機能に加え、ファイルの入出力の処理を有するクラス。
 */
public class FileKeyValueStore extends KeyValueStore {	
	public static final String CACHE_NAME = "cache.dat";
	public static final String FILELIST_DAT = "filelist.dat";
	
	private static final String FILESEPARATOR = System.getProperty("file.separator");
	private static final String NEWLINE = System.getProperty("line.separator");
	private static final String STR_ENCODE = "Shift_JIS";

	private String mFilePath;
	private String mFileName;
	private String mTargetDir;
	private String mEncode;
	
	/**
	 * コンストラクタ
	 * @param dirname
	 */
	public FileKeyValueStore(String dirname) {
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
	public FileKeyValueStore(String dirname, String filename) {
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
		ArrayList<String> keys   = getKeys();
		ArrayList<String> values = getValues();
		
		int len = keys.size();
		for(int i=0; i<len; i++) {
			data = data + keys.get(i) + "=" + values.get(i) + NEWLINE;
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
		clear();
		
		try { 
			isr = new InputStreamReader(is, mEncode);
			br  = new BufferedReader(isr);
			
			String line;
			while((line = br.readLine()) != null) {
				String[] tmp_line = line.split("=");
				
				if(tmp_line.length == 2) {
					set(tmp_line[0], tmp_line[1]);
				}
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
}
