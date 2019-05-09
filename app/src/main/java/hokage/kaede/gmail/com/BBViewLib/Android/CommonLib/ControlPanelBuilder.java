package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import android.content.Context;
import android.view.View;

/**
 * コントロールパネルを生成するクラス
 */
public class ControlPanelBuilder {

	private String[] mCommandList;
	private boolean[] mIsHiddenButton;
	private boolean mIsHiddenPanel;
	private ControlPanel.OnExecuteListenerInterface mListener;

	/**
	 * 初期化を行う。
	 * @param cmd_str コマンド名の文字列配列
	 */
	public ControlPanelBuilder(String[] cmd_str, ControlPanel.OnExecuteListenerInterface listener) {
		mCommandList = cmd_str;
		mListener = listener;
		mIsHiddenPanel = false;

		int count = mCommandList.length;
		mIsHiddenButton = new boolean[count];
		for(int i=0; i<count; i++) {
			mIsHiddenButton[i] = false;
		}
	}

	/**
	 * パネル自体を非表示にするかどうか設定する。
	 * @param is_hidden 非表示にする場合はtrueを設定する。
	 */
	public void setHiddenPanel(boolean is_hidden) {
		mIsHiddenPanel = is_hidden;
	}

	/**
	 * 非表示にするボタンを設定する。
	 * @param position ボタンの位置
	 */
	public void setHiddenButton(int position) {
		if(position < mIsHiddenButton.length) {
			mIsHiddenButton[position] = true;
		}
	}

	/**
	 * ボタンのビューを生成する。
	 * @param context 対象のContext
	 * @return ボタンのビュー
	 */
	public ControlPanel createControlPanel(Context context) {
		ControlPanel panel =  new ControlPanel(context, mCommandList, mIsHiddenButton);
		panel.createView();
		panel.setOnExecuteListener(mListener);

		if(mIsHiddenPanel) {
			panel.setVisibility(View.GONE);
		}

		return panel;
	}
}
