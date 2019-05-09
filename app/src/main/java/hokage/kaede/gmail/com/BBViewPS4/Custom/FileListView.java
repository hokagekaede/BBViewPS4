package hokage.kaede.gmail.com.BBViewPS4.Custom;

import hokage.kaede.gmail.com.BBViewLib.Java.CustomFileManager;
import hokage.kaede.gmail.com.StandardLib.Android.SelectionAdapter;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 「データ」画面を表示するクラス。
 */
public class FileListView extends LinearLayout {

	// レイアウト定義
	private static final int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
	private static final int FP = LinearLayout.LayoutParams.FILL_PARENT;

	private static final String NEWLINE = System.getProperty("line.separator");

	// ファイル一覧データ
	private CustomFileManager mCustomFileManager;
	private SelectionAdapter<String> mFileListAdapter;
	private String mSelectName;
	private String mNewName;
	
	// ファイル選択後の動作
	private static final String CMD_TITLE = "動作を選択";
	private static final String[] CMD_ITEMS = { "読み込む", "上書きする", "名称を変更する", "削除する", "アセン比較する" };
	private static final int CMD_READ    = 0;
	private static final int CMD_UPDATE  = 1;
	private static final int CMD_CHANGE  = 2;
	private static final int CMD_DELETE  = 3;
	private static final int CMD_COMPARE = 4;

	// ボタンテキスト
	private static final String BTN_NEW_FILE = "新規";
	private static final String BTN_MENU = "メニュー";
	private static final String BTN_MOVE_UP = "▲";
	private static final String BTN_MOVE_DOWN = "▼";

	/**
	 * 初期化を行う。
	 * @param context コンテキスト
	 */
	public FileListView(Context context) {
		super(context);

		String file_dir = context.getFilesDir().toString();
		mCustomFileManager = CustomFileManager.getInstance(file_dir);
		ArrayList<String> tag_list = mCustomFileManager.getTagList();

		// リストビューの生成
		mFileListAdapter = new SelectionAdapter<String>();
		mFileListAdapter.setList(tag_list);

		createView();
	}

	/**
	 * 画面を生成する。
	 */
	private void createView() {
		Context context = getContext();

		// 画面レイアウトの生成
		setLayoutParams(new LinearLayout.LayoutParams(FP, FP));
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.LEFT | Gravity.TOP);

		ListView listview = new ListView(context);
		listview.setAdapter(mFileListAdapter);
		listview.setOnItemClickListener(new OnClickFileListener());
		listview.setLayoutParams(new LinearLayout.LayoutParams(FP, WC, 1));

		// 画面下部のボタンレイアウトを生成する
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setGravity(Gravity.CENTER);
		layout.setBackgroundColor(SettingManager.getColorGray());

		// 新規作成ボタンの生成
		Button new_file_btn = new Button(context);
		new_file_btn.setText(BTN_NEW_FILE);
		new_file_btn.setOnClickListener(new OnClickCreateFileButtonListener());

		// メニューボタンの生成
		Button menu_btn = new Button(context);
		menu_btn.setText(BTN_MENU);
		menu_btn.setOnClickListener(new OnClickMenuButtonListener());

		// 上移動ボタンの生成
		Button move_up_btn = new Button(context);
		move_up_btn.setText(BTN_MOVE_UP);
		move_up_btn.setOnClickListener(new OnClickMoveUpButtonListener());

		// 下移動ボタンの生成
		Button move_down_btn = new Button(context);
		move_down_btn.setText(BTN_MOVE_DOWN);
		move_down_btn.setOnClickListener(new OnClickMoveDownButtonListener());

		layout.addView(new_file_btn);
		layout.addView(menu_btn);
		layout.addView(move_up_btn);
		layout.addView(move_down_btn);

		addView(listview);
		addView(layout);
	}

	/**
	 * リストのファイル名をクリックしたときの処理を行うリスナー。
	 */
	private class OnClickFileListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
			if(mFileListAdapter.isSelected(position)) {
				mFileListAdapter.unselect();
			}
			else {
				mFileListAdapter.select(position);
			}

			mFileListAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 新規作成ボタンを押下した時の処理を行うリスナー。
	 */
	private class OnClickCreateFileButtonListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			saveNewFileDiaglog();
		}
	}

	/**
	 * ファイルの新規保存ダイアログを表示する。
	 */
	private void saveNewFileDiaglog() {
		Context context = getContext();

		EditText mDialogText = new EditText(context);
		mDialogText.setInputType(InputType.TYPE_CLASS_TEXT);
		mDialogText.addTextChangedListener(new OnUpdateFileNameTextListener());

		AlertDialog.Builder alt_dialog = new AlertDialog.Builder(context);
		alt_dialog.setTitle("データの新規保存");
		alt_dialog.setView(mDialogText);
		alt_dialog.setPositiveButton("OK", new OnInputNewFileNameListener());
		alt_dialog.show();
	}

	/**
	 * ファイルの新規作成ダイアログで名前を入力した後に処理を行うリスナー。
	 */
	private class OnInputNewFileNameListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialogInterface, int i) {
			createFile();
		}
	}

	/**
	 * ファイルを新規保存する。
	 */
	private void createFile() {

		// ファイルが既に存在する場合は、処理を終了する
		if(!mCustomFileManager.createFile(mNewName)) {
			String warning_str = "既にデータが存在しています。" + NEWLINE + "別の名前を入力してください。";
			Toast.makeText(getContext(), warning_str, Toast.LENGTH_LONG).show();
			return;
		}

		// リストに追加し、再描画する
		mFileListAdapter.add(mNewName);
		mFileListAdapter.notifyDataSetChanged();
	}

	/**
	 * メニューボタンを押下した時の処理を行うリスナー。
	 */
	private class OnClickMenuButtonListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			// 選択されたファイルタイトルからファイル名を取得
			mSelectName = mFileListAdapter.getSelectedItem();

			if(mSelectName == null) {
				String warning_str = "ファイルを選択してください。";
				Toast.makeText(getContext(), warning_str, Toast.LENGTH_LONG).show();
				return;
			}

			// ダイアログの表示
			AlertDialog.Builder cmd_dialog = new AlertDialog.Builder(getContext());
			cmd_dialog.setIcon(android.R.drawable.ic_menu_more);
			cmd_dialog.setTitle(CMD_TITLE);
			cmd_dialog.setItems(CMD_ITEMS, new OnSelectCommandListener());
			cmd_dialog.show();
		}
	}

	/**
	 * ファイルに対する操作を選択した時の処理を行うリスナー。
	 */
	private class OnSelectCommandListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialogInterface, int which) {
			if(which == CMD_READ) {
				showReadDialog();
			}
			else if(which == CMD_UPDATE) {
				showUpdateFileDialog();
			}
			else if(which == CMD_CHANGE) {
				showChangeDialog();
			}
			else if(which == CMD_DELETE) {
				deleteFile();
			}
			else if(which == CMD_COMPARE) {
				compareCustomData();
			}
		}
	}

	/**
	 * ファイルの読み込みを確認するダイアログを表示する。
	 */
	private void showReadDialog() {

		// データ変更時のみダイアログを表示する
		if(mCustomFileManager.isCacheChanged()) {
			AlertDialog.Builder read_dialog = new AlertDialog.Builder(getContext());
			read_dialog.setTitle("データの読み込み");
			read_dialog.setPositiveButton("OK", new OnClickReadListener());
			read_dialog.setNegativeButton("Cancel", null);
			read_dialog.setMessage("編集中のデータを保存せずに読み込みしますか？");
			read_dialog.show();
		}
		else {
			readFile();
		}
	}

	/**
	 * ファイルの読み込みを実行するリスナー
	 */
	private class OnClickReadListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			readFile();
		}
	}
	
	/**
	 * ファイルの読み込み処理を行う。
	 */
	private void readFile() {
		mCustomFileManager.readFile(mSelectName);
	}

	/**
	 * ファイルを上書き保存する。
	 */
	private void showUpdateFileDialog() {
		AlertDialog.Builder upd_dialog = new AlertDialog.Builder(getContext());
		upd_dialog.setTitle("データの上書き");
		upd_dialog.setPositiveButton("OK", new OnClickUpdateListener());
		upd_dialog.setNegativeButton("Cancel", null);
		upd_dialog.setMessage("データを上書きしますか？");
		upd_dialog.show();
	}
	
	/**
	 * ファイルの上書き保存を実行するリスナー
	 */
	private class OnClickUpdateListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			mCustomFileManager.updateFile(mSelectName);
		}
	}

	/**
	 * ファイル名変更ダイアログを表示する。
	 */
	private void showChangeDialog() {
		Context context = getContext();

		EditText mDialogText = new EditText(context);
		mDialogText.setInputType(InputType.TYPE_CLASS_TEXT);
		mDialogText.addTextChangedListener(new OnUpdateFileNameTextListener());

		AlertDialog.Builder alt_dialog = new AlertDialog.Builder(context);
		alt_dialog.setTitle("データ名の変更");
		alt_dialog.setView(mDialogText);
		alt_dialog.setPositiveButton("OK", new OnInputAfterFileNameListener());
		alt_dialog.show();
	}

	/**
	 * ファイル名変更における変更後のファイル名を入力した後に処理を行うリスナー。
	 */
	private class OnInputAfterFileNameListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialogInterface, int i) {
			changeFile();
		}
	}

	/**
	 * ファイル名を変更する。
	 */
	private void changeFile() {
		// 存在する場合、エラーダイアログを表示し、処理を終了する
		if(!mCustomFileManager.change(mSelectName, mNewName)) {
			String warning_str = "既にデータが存在しています。" + NEWLINE + "別の名前を入力してください。";
			Toast.makeText(getContext(), warning_str, Toast.LENGTH_LONG).show();
			return;
		}
		
		// リストを再描画する
		mFileListAdapter.replace(mSelectName, mNewName);
		mFileListAdapter.notifyDataSetChanged();
	}
	
	/**
	 * ファイルを削除する。
	 */
	private void deleteFile() {
		AlertDialog.Builder del_dialog = new AlertDialog.Builder(getContext());
		del_dialog.setTitle("データの削除");
		del_dialog.setPositiveButton("OK", new OnClickDeleteListener());
		del_dialog.setNegativeButton("Cancel", null);
		del_dialog.setMessage("データを削除しますか？");
		del_dialog.show();
	}

	/**
	 * ファイルの削除を実行するリスナー
	 */
	private class OnClickDeleteListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// ファイルを削除する
			mCustomFileManager.delete(mSelectName);

			// リストを再描画する
			mFileListAdapter.remove(mSelectName);
			mFileListAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * アセンの比較画面に移動する。
	 */
	private void compareCustomData() {
		Context context = getContext();
		Intent intent = new Intent(context, CompareActivity.class);
		intent.putExtra(CompareActivity.INTENTKEY_CMPTO_FILENAME, mSelectName);

		context.startActivity(intent);
	}

	/**
	 * 上移動ボタンを押下した時の処理を行うリスナー。
	 */
	private class OnClickMoveUpButtonListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			moveUpFile();
		}
	}

	/**
	 * 選択中のファイルを上のファイルと入れ替える。
	 */
	private void moveUpFile() {
		int position = mFileListAdapter.getSelectedPosition();

		if(position == SelectionAdapter.UNSELECTED_POSITION) {
			String warning_str = "ファイルを選択してください。";
			Toast.makeText(getContext(), warning_str, Toast.LENGTH_LONG).show();
			return;
		}

		if(position <= 0) {
			return;
		}

		mCustomFileManager.swapFile(position, position - 1);
		mFileListAdapter.swap(position, position - 1);
		mFileListAdapter.select(position - 1);
		mFileListAdapter.notifyDataSetChanged();
	}

	/**
	 * 下移動ボタンを押下した時の処理を行うリスナー。
	 */
	private class OnClickMoveDownButtonListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			moveDownFile();
		}
	}

	/**
	 * 選択中のファイルを下のファイルと入れ替える。
	 */
	private void moveDownFile() {
		int position = mFileListAdapter.getSelectedPosition();

		if(position == SelectionAdapter.UNSELECTED_POSITION) {
			String warning_str = "ファイルを選択してください。";
			Toast.makeText(getContext(), warning_str, Toast.LENGTH_LONG).show();
			return;
		}

		// アダプタの方がサイズ小さいことが保証されている (ファイル数のデータがあるため)
		if(position >= mFileListAdapter.getCount() - 1) {
			return;
		}

		mCustomFileManager.swapFile(position, position + 1);
		mFileListAdapter.swap(position, position + 1);
		mFileListAdapter.select(position + 1);
		mFileListAdapter.notifyDataSetChanged();
	}

	/**
	 * EditTextの入力文字列を更新するリスナー。
	 */
	private class OnUpdateFileNameTextListener implements TextWatcher {
		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			// Do Nothing
		}

		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			mNewName = charSequence.toString();
		}

		@Override
		public void afterTextChanged(Editable editable) {
			// Do Nothing
		}
	}
}
