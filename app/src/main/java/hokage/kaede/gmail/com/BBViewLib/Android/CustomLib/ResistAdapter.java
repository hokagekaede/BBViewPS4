package hokage.kaede.gmail.com.BBViewLib.Android.CustomLib;

import java.util.ArrayList;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomData;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 「耐性」画面の武器一覧を表示するクラス。
 */
public class ResistAdapter extends BaseAdapter {
	private Context mContext;
	private CustomData mCustomData;
	private ArrayList<BBData> mList;
	private int mPatternMode = ResistAdapterItem.MODE_SHOT;

	// 武器スペックをタイプB表示にするかどうか
	private boolean mIsShowTypeB = false;	

	public ResistAdapter(Context context, CustomData custom_data, ArrayList<BBData> list) {
		mContext = context;
		mCustomData = custom_data;
		mList = list;
	}
	
	public void setList(ArrayList<BBData> list) {
		mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public BBData getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	
	/**
	 * スイッチ武器のスペックをタイプB表示をするかどうか。
	 * @param is_show_typeb タイプBにする場合はtrueを設定し、しない場合はfalseを設定する。
	 */
	public void setShowTypeB(boolean is_show_typeb) {
		mIsShowTypeB = is_show_typeb;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ResistAdapterItem item_view = (ResistAdapterItem)convertView;
		
		BBData item = mList.get(position);
		
		// インスタンスがnullの場合または表示モードが異なる場合は、インスタンスを再生成する
		if(item_view == null || item_view.getMode() != mPatternMode) {
			item_view = new ResistAdapterItem(mContext, mPatternMode);
		}

		item_view.update(mCustomData, item, mIsShowTypeB);
		
		return item_view;
	}
	
	public void setMode(int mode) {
		mPatternMode = mode;
	}
}
