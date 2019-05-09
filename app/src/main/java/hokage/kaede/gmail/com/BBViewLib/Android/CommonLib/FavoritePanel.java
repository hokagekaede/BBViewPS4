package hokage.kaede.gmail.com.BBViewLib.Android.CommonLib;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Java.FavoriteManager;
import hokage.kaede.gmail.com.StandardLib.Android.NormalExpandableAdapter;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;
import hokage.kaede.gmail.com.StandardLib.Java.FileArrayList;

/**
 * お気に入りを表示するパネル
 */
public class FavoritePanel extends LinearLayout {

    // お気に入りを表示するテキストビュー
    private TextView mFavoriteTextView;

    // お気に入りのデータ
    private FileArrayList mFavoriteStore;

    /**
     * 初期化する。
     * @param context コンストラクタ
     */
    public FavoritePanel(Context context) {
        super(context);
    }

    /**
     * ビューを生成する。
     */
    public void createView() {
        Context context = getContext();

        mFavoriteTextView = new TextView(context);
        mFavoriteTextView.setTextSize(BBViewSetting.getTextSize(context, BBViewSetting.FLAG_TEXTSIZE_NORMAL));
        mFavoriteTextView.setGravity(Gravity.RIGHT | Gravity.CENTER);
        mFavoriteTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
        mFavoriteTextView.setPadding(10, 0, 10, 0);

        this.addView(mFavoriteTextView);
    }

    /**
     * お気に入りリストのデータストアを設定する。
     * @param store ストア
     */
    public void setFavoriteStore(FileArrayList store) {
        mFavoriteStore = store;
    }

    /**
     * お気に入りデータに応じてビューの表示を更新する。
     */
    public void updateView(BBData target_data) {
        if(mFavoriteStore == null) {
            return;
        }

        if(mFavoriteStore.exist(target_data.get("名称"))) {
            mFavoriteTextView.setTextColor(SettingManager.getColorYellow());
            mFavoriteTextView.setText("[Fav:ON]");
        }
        else {
            mFavoriteTextView.setTextColor(SettingManager.getColorCyan());
            mFavoriteTextView.setText("[Fav:OFF]");
        }
    }

    /**
     * Favoriteボタンを押下した場合の処理を行う。
     * お気に入りの設定状況に応じて、リストへの追加/削除を行う。
     * また、お気に入りリストのファイルも更新する。
     */
    public static class OnClickFavListener implements View.OnClickListener {
        private BBData mTarget;
        private NormalExpandableAdapter mAdapter;
        private FileArrayList mFavoriteStore;

        public OnClickFavListener(NormalExpandableAdapter adapter, FileArrayList store, BBData data) {
            mTarget = data;
            mFavoriteStore = store;
            mAdapter = adapter;
        }

        @Override
        public void onClick(View view) {
            String name = mTarget.get("名称");

            if(mFavoriteStore == null) {
                return;
            }

            int index = mFavoriteStore.indexOf(name);

            int fav_group_idx = mAdapter.indexOfGroup(FavoriteManager.FAVORITE_CATEGORY_NAME);

            if(index > -1) {
                mFavoriteStore.remove(index);

                int fav_index = mAdapter.indexOfChild(fav_group_idx, mTarget);
                mAdapter.removeChild(fav_group_idx, fav_index);
            }
            else {
                mFavoriteStore.add(name);

                mAdapter.addChild(fav_group_idx, mTarget);
            }

            mFavoriteStore.save();

            mAdapter.notifyDataSetChanged();
        }
    }
}
