package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.StandardLib.Android.NormalExpandableAdapter;
import hokage.kaede.gmail.com.StandardLib.Java.FileArrayList;

/**
 * カテゴリ表示用のリストのクラス
 */
public class BBDataExpandableAdapter extends NormalExpandableAdapter<BBData> {

	private BBDataAdapterItemProperty mProperty;

	private FileArrayList mFavStore;
	private ControlPanelBuilder mControlPanelBuilder;

	/**
	 * 初期化を行う。
	 * @param property 表示のためのデータと設定値
	 */
	public BBDataExpandableAdapter(BBDataAdapterItemProperty property) {
		mProperty = property;
	}

	/**
	 * プロパティを取得する。
	 * @return プロパティ
	 */
	public BBDataAdapterItemProperty getProperty() {
		return mProperty;
	}

	/**
	 * お気に入りリストのストアを設定する。
	 * @param store
	 */
	public void setFavStore(FileArrayList store) {
		mFavStore = store;
	}

	/**
	 * コントロールパネルのビルダーを設定する。
	 * @param builder コントロールパネルの生成設定がされているビルダー
	 */
	public void setBuilder(ControlPanelBuilder builder) {
		mControlPanelBuilder = builder;
	}

	/**
	 * データを追加する。
	 * @param groupPosition グループの位置
	 * @param item 追加するデータ
	 */
	@Override
	public void addChild(int groupPosition, BBData item) {
		super.addChild(groupPosition, item);

		int size = getGroupCount();
		if(groupPosition == size - 1) {
			return;
		}

		// お気に入りリストに格納されているアイテムを追加する。
		String name = item.get("名称");
		if(mFavStore != null && mFavStore.exist(name)) {
			super.addChild(size - 1, item);
		}
	}

	/**
	 * アイテムのビューを取得する。
	 * @param groupPosition グループの位置
	 * @param childPosition アイテムの位置
	 * @param isExpanded 開いている状態かどうか
	 * @param convertView 元となるビュー
	 * @param parent 親のView
	 * @return アイテムのビュー
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		Context context = parent.getContext();
		BBData data = getChild(groupPosition, childPosition);
		BBDataAdapterItem item_view;
		ControlPanel control_panel;

		if(convertView == null) {
			item_view = new BBDataAdapterItem(context, mProperty);
			item_view.createView();
			item_view.setTextSize(mTextSize);
			item_view.setTextColor(mTextColor);
			item_view.setBackGroundColor(mBackGroundColor);
			item_view.setFavoriteList(mFavStore);

			control_panel = mControlPanelBuilder.createControlPanel(context);
			item_view.addView(control_panel, 0); // 先頭(一番左)に置く
		}
		else {
			item_view = (BBDataAdapterItem)convertView;
			control_panel = (ControlPanel)item_view.getChildAt(0);
		}

		item_view.setOnClickFavListener(new FavoritePanel.OnClickFavListener(this, mFavStore, data));
		item_view.setData(data);
		item_view.updateView();
		control_panel.updateView(data);

		return item_view;
	}

}
