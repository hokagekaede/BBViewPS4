package hokage.kaede.gmail.com.BBViewLib.Android.CustomLib;

import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BBDataAdapterItemProperty;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.FavoritePanel;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.OwnerInfoPanel;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.StandardLib.Android.NormalAdapterItem;
import hokage.kaede.gmail.com.StandardLib.Java.FileArrayList;

import android.content.Context;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 「チップ」画面のチップ一覧の中の表示内容を生成するクラス。
 */
public class ChipAdapterItem extends NormalAdapterItem<BBData> {

	private BBDataAdapterItemProperty mProperty;

	private CheckBox mCheckBox;
	private OwnerInfoPanel mOwnerInfoPanel;
	private FavoritePanel mFavoritePanel;

	/**
	 * 初期化を行う。
	 * @param context リストを表示する画面
	 * @param property 表示のためのデータと設定値
	 */
	public ChipAdapterItem(Context context, BBDataAdapterItemProperty property) {
		super(context);
		mProperty = property;
	}

	/**
	 * 表示するビューを生成する。
	 */
	@Override
	public void createView() {
		Context context = getContext();

		mCheckBox = new CheckBox(context);
		mCheckBox.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
		mCheckBox.setGravity(Gravity.LEFT | Gravity.CENTER);
		mCheckBox.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));

		this.addView(mCheckBox);

		mOwnerInfoPanel = new OwnerInfoPanel(context);
		mOwnerInfoPanel.createView();
		this.addView(mOwnerInfoPanel);

		if(mProperty.isShowFavorite()) {
			mFavoritePanel = new FavoritePanel(context);
			mFavoritePanel.createView();
			this.addView(mFavoritePanel);
		}
	}

	/**
	 * 表示するビューを生成する。
	 */
	@Override
	public void updateView() {
		BBData target_item = getData();
		mCheckBox.setText(target_item.get("名称") + " [" + target_item.get("コスト") + "]");

		mOwnerInfoPanel.updateView(target_item);

		if(mProperty.isShowFavorite()) {
			mFavoritePanel.updateView(target_item);
		}
	}

	/**
	 * チェックボックスを更新する。
	 * @param checked 設定するチェック状態
	 */
	public void setChecked(boolean checked) {
		mCheckBox.setChecked(checked);
	}

	/**
	 * チェックボックスのIDを設定する。
	 * @param id ID値
	 */
	public void setCheckBoxID(int id) {
		mCheckBox.setId(id);
	}

	/**
	 * チェックボックスが変更された場合のリスナーを設定する。
	 * @param listener 対象のリスナー
	 */
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mCheckBox.setOnCheckedChangeListener(listener);
	}

	/**
	 * お気に入りリストのデータストアを設定する。
	 * @param store ストア
	 */
	public void setFavoriteList(FileArrayList store) {
		if(mProperty.isShowFavorite()) {
			mFavoritePanel.setFavoriteStore(store);
		}
	}

	/**
	 * Favoriteボタンがクリックされた場合のリスナーを設定する。
	 * @param listener 対象のリスナー
	 */
	public void setOnClickFavListener(OnClickListener listener) {
		if(mProperty.isShowFavorite()) {
			mFavoritePanel.setOnClickListener(listener);
		}
	}
}
