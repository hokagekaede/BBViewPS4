package hokage.kaede.gmail.com.StandardLib.Java;

import java.io.File;
import java.util.ArrayList;

/**
 * ファイル名と実際のファイル名の管理機能と変換処理を有するクラス。
 *
 * 日本語のファイルタグ(key)と実際に保存するファイル名(value)で管理する。
 * 本クラスを使うことで、日本語のファイル名のみ意識してファイル管理できるようにする。
 */
public class FileManager {
	
	// ファイル数を管理するキー
	private static final String FILE_COUNT_KEY = "ファイル数";
	
	private String mDirPath;
	private String mFileNameHead;
	private String mFileNameType;
	
	// ファイルリスト管理ストア
	private FileKeyValueStore mFileStore;
	
	/**
	 * 初期化処理を行う。
	 * @param dirpath ファイルが格納されたディレクトリ
	 * @param file_name_head ファイル名
	 * @param file_name_type 拡張子
	 */
	public FileManager(String dirpath, String file_name_head, String file_name_type) {
		mDirPath = dirpath;
		mFileNameHead = file_name_head;
		mFileNameType = file_name_type;
		
		mFileStore = new FileKeyValueStore(dirpath, file_name_head + "list" + file_name_type);
		mFileStore.load();
		
		// ファイル数のキーが無かった場合、初期化する。
		if(!mFileStore.existKey(FILE_COUNT_KEY)) {
			mFileStore.clear();
			mFileStore.set(FILE_COUNT_KEY, "0");
		}
	}

	/**
	 * ファイル一覧の読み込みを行う。
	 */
	public void load() {
		mFileStore.load();
	}

	/**
	 * 管理中のファイルの数を取得する。
	 * @return
	 */
	public int getCount() {
		int ret = -1;
		
		try {
			ret = Integer.valueOf(mFileStore.get(FILE_COUNT_KEY));
			
		} catch(NumberFormatException e) {
			ret = -1;
		}
		
		return ret;
	}
	
	/**
	 * ファイルの格納対象のフォルダパスを取得する。
	 * @return
	 */
	public String getDirectory() {
		return mDirPath;
	}
	
	/**
	 * 管理中のファイルタグを取得する。
	 * @return
	 */
	public ArrayList<String> getTagList() {
		ArrayList<String> tag_list = new ArrayList<String>(mFileStore.getKeys());
		tag_list.remove(FILE_COUNT_KEY);
		return tag_list;
	}
	
	/**
	 * 実際のファイル名を取得する。
	 * @param tag
	 * @return
	 */
	public String getFileName(String tag) {
		return mFileStore.get(tag);
	}
	
	/**
	 * ファイルタグに応じたファイルを作成する。
	 * @param tag
	 * @return
	 */
	public boolean create(String tag) {
		int count = getCount();
		String filename = "";
		
		// ファイルタグが存在する場合、処理を中止する。
		if(mFileStore.existKey(tag)) {
			return false;
		}

		// ファイルの有無を確認する。存在する場合は前の番号を検索して使用する。
		for(int i=count; i>=0; i--) {
			filename = mFileNameHead + String.valueOf(i) + mFileNameType;
			File tmp_file = new File(mDirPath, filename);
			
			if(!tmp_file.exists()) {
				break;
			}
		}

		// ファイル数とファイルタグを更新する。
		mFileStore.set(FILE_COUNT_KEY, String.valueOf(count + 1));
		mFileStore.set(tag, filename);
		mFileStore.save();
		
		return true;
	}
	
	/**
	 * ファイルタグを変更する。
	 * @param from_tag
	 * @param to_tag
	 * @return
	 */
	public boolean change(String from_tag, String to_tag) {

		// ファイルタグが存在する場合、処理を中止する。
		if(mFileStore.existKey(to_tag)) {
			return false;
		}

		/*
		// ファイル名を取得し、当該ファイル名のキーと値を削除する。
		String filename = mFileStore.get(from_tag);
		mFileStore.remove(from_tag);
		
		// 変更後のファイルタグでファイル名を登録する。
		mFileStore.set(to_tag, filename);
		mFileStore.save();
		*/
		mFileStore.changeKey(from_tag, to_tag);

		return true;
	}
	
	/**
	 * ファイルタグに合致したファイルを削除する。
	 * @param tag
	 * @return
	 */
	public boolean delete(String tag) {

		// ファイルを読み込み、削除する。
		String select_file_name = mFileStore.get(tag);
		FileKeyValueStore select_file = new FileKeyValueStore(mDirPath, select_file_name);
		select_file.delete();
		
		// ファイルリストからファイルタグを削除し、ファイル数を1つ減らす。
		mFileStore.remove(tag);
		int count = getCount();
		mFileStore.set(FILE_COUNT_KEY, String.valueOf(count - 1));
		mFileStore.save();
		
		return true;
	}
	
	/**
	 * ファイルタグの有無を確認する。
	 * @param tag
	 * @return
	 */
	public boolean exist(String tag) {
		return mFileStore.existKey(tag);
	}

	/**
	 * ファイルの位置を入れ替える。
	 * @param from_position 移動元の位置
	 * @param to_position 移動先の位置
	 */
	public void swapFile(int from_position, int to_position) {

		// ファイル数のキーが先頭にあるので、指定位置に対して1加算して処理する。
		mFileStore.swap(from_position + 1, to_position + 1);
		mFileStore.save();
	}
}
