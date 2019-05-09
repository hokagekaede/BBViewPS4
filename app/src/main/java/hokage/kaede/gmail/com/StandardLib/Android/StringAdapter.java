package hokage.kaede.gmail.com.StandardLib.Android;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 一般的な項目一覧を生成するためのクラス。
 */
public class StringAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> mList;
	private int mTextSize;
	private int mTextColor;
	private int mBackGroundColor;
	private int mMode;
	
	public static final int MODE_DEFAULT = 0;
	public static final int MODE_SPINNER = 1;

	private static final int NOTHING_COLOR = -1;
	
	
	/**
	 * 初期化処理を行う。空リストを生成する。
	 * @param context リストを表示する画面
	 */
	public StringAdapter(Context context) {
		init(context);
	}
	
	/**
	 * 初期化処理を行う。引数の配列でリストを生成する。
	 * @param context リストを表示する画面
	 * @param values リストの中身になる配列データ
	 */
	public StringAdapter(Context context, String[] values) {
		init(context);
		
		int size = values.length;
		for(int i=0; i<size; i++) {
			mList.add(values[i]);
		}
	}
	
	/**
	 * 初期化処理を行う。
	 * @param context リストを表示する画面
	 */
	private void init(Context context) {
		mContext = context;
		mList = new ArrayList<String>();
		mTextSize = SettingManager.FLAG_TEXTSIZE_NORMAL;
		mTextColor = NOTHING_COLOR;
		mBackGroundColor = NOTHING_COLOR;
		mMode = MODE_DEFAULT;
	}

	/**
	 * リストの登録数を返す。
	 * @return リストの登録数
	 */
	@Override
	public int getCount() {
		return mList.size();
	}

	/**
	 * リストのデータを取得する。
	 * @param position 取得するデータの位置
	 * @return リストのデータ
	 */
	@Override
	public String getItem(int position) {
		if(position < 0 || position >= mList.size()) {
			return null;
		}
		
		return mList.get(position);
	}
	
	/**
	 * リストの指定データを削除する。
	 * @param value 削除するデータ
	 */
	public void removeItem(String value) {
		int idx = mList.indexOf(value);
		
		if(idx >= 0) {
			mList.remove(idx);
		}
	}
	
	/**
	 * リストの指定データを置き換える。
	 * @param from_value 置き換え前のデータ
	 * @param to_value 置き換え後のデータ
	 */
	public void replaceItem(String from_value, String to_value) {
		int idx = mList.indexOf(from_value);

		if(idx >= 0) {
			mList.set(idx, to_value);
		}
	}

	/**
	 * IDを取得する。
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * ビューを取得する。
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(mMode == MODE_SPINNER) {
			return updateViewSpinner(position, convertView, parent);
		}
		
		return updateViewDefault(position, convertView);
	}
	
	/**
	 * ビューを更新する。(通常)
	 * @param position データの位置
	 * @param convertView 更新対象のビュー
	 * @return ビューのインスタンス
	 */
	private View updateViewDefault(int position, View convertView) {
		TextView item_view;

		if(convertView == null) {
			item_view = new TextView(mContext);
			item_view.setText(mList.get(position));
			item_view.setPadding(10, 10, 10, 10);
			item_view.setTextSize(SettingManager.getTextSize(mContext, mTextSize));
			
			if(mTextColor != NOTHING_COLOR) {
				item_view.setTextColor(mTextColor);
			}

			if(mBackGroundColor != NOTHING_COLOR) {
				item_view.setBackgroundColor(mBackGroundColor);
			}
		}
		else {
			item_view = (TextView)convertView;
			item_view.setText(mList.get(position));
		}

		return item_view;
	}

	/**
	 * ビューを更新する。(Spinner向け)
	 * @param position データの位置
	 * @param convertView 更新対象のビュー
	 * @return ビューのインスタンス
	 */
	private View updateViewSpinner(int position, View convertView, ViewGroup parent) {
		TextView item_view;

		if(convertView == null) {
			Context context = parent.getContext();
			item_view = new TextView(context);
			item_view.setText(mList.get(position));
			item_view.setPadding(10, 10, 10, 10);
			item_view.setTextSize(SettingManager.getTextSize(context, mTextSize));

			if(mTextColor != NOTHING_COLOR) {
				item_view.setTextColor(mTextColor);
			}
			
			if(mBackGroundColor != NOTHING_COLOR) {
				item_view.setBackgroundColor(mBackGroundColor);
			}
		}
		else {
			item_view = (TextView)convertView;
			item_view.setText(mList.get(position));
		}

		return item_view;
	}
	
	/**
	 * データを追加する
	 * @param value 追加するデータ
	 */
	public void add(String value) {
		mList.add(value);
	}
	
	/**
	 * 配列データを追加する。
	 * @param values 追加するデータの配列
	 */
	public void addArrays(String[] values) {
		int size = values.length;
		
		for(int i=0; i<size; i++) {
			mList.add(values[i]);
		}
	}
	
	/**
	 * テキストサイズを設定する。
	 * @param text_size テキストサイズ
	 */
	public void setTextSize(int text_size) {
		mTextSize = text_size;
	}
	
	/**
	 * テキストカラーを設定する。
	 * @param color カラー値
	 */
	public void setTextColor(int color) {
		mTextColor = color;
	}
	
	/**
	 * 背景色を設定する。
	 * @param color カラー値
	 */
	public void setBackGroundColor(int color) {
		mBackGroundColor = color;
	}
	
	/**
	 * 表示モードを設定する。
	 * @param mode モード
	 */
	public void setMode(int mode) {
		mMode = mode;
	}
}
