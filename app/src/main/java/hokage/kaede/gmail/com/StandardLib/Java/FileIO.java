package hokage.kaede.gmail.com.StandardLib.Java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

/**
 * ファイルの入出力を管理するクラス。
 */
public class FileIO {

	public static final String ENCODE_UTF8 = "UTF-8";
	public static final String ENCODE_SJIS = "Shift_JIS";
	
	public static final String FILESEPARATOR = System.getProperty("file.separator");
	public static final String NEWLINE = System.getProperty("line.separator");
	
	/**
	 * コピー処理
	 * @param copy_fm_str
	 * @param copy_to_str
	 * @return
	 */
	public static boolean copy(String copy_fm_str, String copy_to_str)
	{
		boolean ret = false;
		File copy_fm = new File(copy_fm_str);
		File copy_to = new File(copy_to_str);
		
		if(copy_fm.isDirectory()) {
			ret = copyDirecory(copy_fm, copy_to);
		}
		else if(copy_fm.isFile()) {
			ret = copyFile(copy_fm, copy_to);
		}
		
		return ret;
	}

	/**
	 * ディレクトリのコピー処理
	 * @param copy_fm
	 * @param copy_to
	 * @return
	 */
	public static boolean copyDirecory(File copy_fm, File copy_to)
	{
		boolean ret = true;
		
		// コピー元のディレクトリが存在しない場合、処理終了
		if(!copy_fm.exists()) {
			return false;
		}
		
		// コピー先のディレクトリが存在しない場合、ディレクトリを生成する
		if(!copy_to.exists()) {
			try {
				copy_to.mkdirs();
			} catch (Exception e) {
				// e.printStackTrace();
				return false;
			}
		}
		
		// ファイル一覧を取得する
		File[] files = copy_fm.listFiles();
		int file_count = files.length;
		
		// ファイルを1つずつコピーする
		for(int i=0; i<file_count; i++) {
			File copy_fm_file = files[i];
			File copy_to_file = new File(copy_to.getPath() + File.separator + copy_fm_file.getName());
			
			boolean tmp_ret = copyFile(copy_fm_file, copy_to_file);
			if(!tmp_ret) {
				ret = false;
			}
		}
		
		return ret;
	}
	
	/**
	 * ファイルのコピー処理
	 * @param in
	 * @param out
	 * @return
	 */
	public static boolean copyFile(File in, File out)
	{
		boolean ret = true;
		FileChannel sourceChannel = null;
		FileChannel destinationChannel = null;
		
		try {
			sourceChannel = new FileInputStream(in).getChannel();
			destinationChannel = new FileOutputStream(out).getChannel();
			
			sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
			sourceChannel.close();
			destinationChannel.close();
			
		} catch (Exception e) {
			ret = false;
			// e.printStackTrace();
			
		} finally {
			try { sourceChannel.close(); } catch (Exception e) { };
			try { destinationChannel.close(); } catch (Exception e) { };
		}
		
		return ret;
	}
	
	private static final int READ_MAX_BUFFER = 2048 * 4;
	
	/**
	 * 指定のストリームから文字データを読み込む
	 * @param is 入力元のストリーム
	 * @param encode 文字のエンコード
	 * @return 読み込んだ文字列
	 */
	public static String readInputStream(InputStream is, String encode)
	{
		String ret = "";
		
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		try {
			isr = new InputStreamReader(is, encode);
			br  = new BufferedReader(isr);
			
			int read_size = 0;
			char[] buf = new char[READ_MAX_BUFFER];
			
			while((read_size = br.read(buf)) > 0) {
				
				if(read_size == READ_MAX_BUFFER) {
					ret = ret + String.valueOf(buf);
				}
				else {
					ret = ret + String.valueOf(buf).substring(0, read_size);
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		
		} finally {
			try { br.close();  } catch (Exception e2) { }
			try { isr.close(); } catch (Exception e2) { }
		}
		
		return ret;
	}
	
	/**
	 * 指定のパスにテキストを書き込む
	 * @param filepath ファイルパス
	 * @param data 書き込むデータ
	 * @param encode エンコード
	 */
	public static void write(String filepath, String data, String encode) {
		FileOutputStream fos = null;
		OutputStreamWriter osr = null;
		BufferedWriter bw = null;
		
		// ファイルにキャッシュする
		try { 
			fos = new FileOutputStream(filepath, false);
			osr = new OutputStreamWriter(fos, encode);
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
}
