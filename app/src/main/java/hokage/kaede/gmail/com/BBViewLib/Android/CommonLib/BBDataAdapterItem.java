package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.StandardLib.Android.NormalAdapterItem;
import hokage.kaede.gmail.com.StandardLib.Java.FileArrayList;

import android.content.Context;

/**
 * 一般的なBBDataリストの各項目のアイテムクラス
 */
public class BBDataAdapterItem extends NormalAdapterItem<BBData> {

	private BBDataAdapterItemProperty mProperty;

	private SpecInfoPanel mSpecInfoPanel;
	private OwnerInfoPanel mOwnerInfoPanel;
	private FavoritePanel mFavoritePanel;

	/**
	 * 初期化処理を行う。
	 * LinearLayoutのコンストラクタをコールし、TextViewのオブジェクトを生成する。
	 * @param context リストを表示する画面
	 * @param property 表示のためのデータと設定値
	 */
	public BBDataAdapterItem(Context context, BBDataAdapterItemProperty property) {
		super(context);
		mProperty = property;
	}

	/**
	 * ビューを生成する。
	 */
	@Override
	public void createView() {
		Context context = getContext();

		mSpecInfoPanel = new SpecInfoPanel(context, mProperty);
		mSpecInfoPanel.createView();
		mSpecInfoPanel.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		this.addView(mSpecInfoPanel);

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
	 * ビューの更新する。
	 */
	@Override
	public void updateView() {
		BBData target_item = getData();

		mSpecInfoPanel.updateView(target_item);
		mOwnerInfoPanel.updateView(target_item);

		if(mProperty.isShowFavorite()) {
			mFavoritePanel.updateView(target_item);
		}
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
