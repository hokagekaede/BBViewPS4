package hokage.kaede.gmail.com.BBViewLib.Android.CustomLib;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BBDataAdapterItemProperty;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BBDataCheckExpandableAdapter;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.FavoritePanel;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomData;
import hokage.kaede.gmail.com.BBViewLib.Java.FavoriteManager;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;

/**
 * 「チップ」画面のカテゴリ表示の一覧を生成するクラス。
 */
public class SelectChipExpandableAdapter extends BBDataCheckExpandableAdapter {

    private CustomData mCustomData;

	private OnChangedChipSetBaseListener mChangedChipSetListener;
	
	private static final int SKILL_CHIP_CATEGORY_INDEX = 0;
	private static final int POWERUP_CHIP_CATEGORY_INDEX = 1;
	private static final int ACTION_CHIP_CATEGORY_INDEX = 2;
	private static final int FAVORITE_CHIP_CATEGORY_INDEX = 3;
	
	// 現在選択中のチップ位置
	private int mSelectedGroupPosition = 0;
	private int mSelectedChildPosition = 0;
	
	/**
	 * コンストラクタ。リストを初期化する。
	 */
	public SelectChipExpandableAdapter(CustomData custom_data, BBDataAdapterItemProperty property) {
		super(property);
		mCustomData = custom_data;

        mCategoryTextColor = SettingManager.getColorWhite();
        mCategoryBackGroundColor = SettingManager.getColorBlue();

		init();
	}
	
	/**
	 * グループリストを初期化する。
	 */
	private void init() {
		super.clear();
		mSelectedGroupPosition = 0;
		mSelectedChildPosition = 0;
		
		super.addGroup(BBDataManager.SKILL_CHIP_STR);
		super.addGroup(BBDataManager.POWERUP_CHIP_STR);
		super.addGroup(BBDataManager.ACTION_CHIP_STR);
		super.addGroup(FavoriteManager.FAVORITE_CATEGORY_NAME);
	}

	/**
	 * データのビューを取得する。
	 * @param groupPosition グループの位置
	 * @param childPosition データの位置
	 * @param isLastChild
	 * @param convertView データのビュー
	 * @param parent グループのビュー
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		BBData chipdata = super.getChild(groupPosition, childPosition);
		boolean is_select = isSelected(groupPosition, childPosition);
		ChipAdapterItem item_view;
		
		if(convertView == null) {
            item_view = new ChipAdapterItem(parent.getContext(), getProperty());
            item_view.createView();
			item_view.setFavoriteList(FavoriteManager.sChipStore);
		}
		else {
            item_view = (ChipAdapterItem)convertView;
        }

		item_view.setOnClickFavListener(new FavoritePanel.OnClickFavListener(this, FavoriteManager.sChipStore, chipdata));
        item_view.setCheckBoxID((int)getChildId(groupPosition, childPosition));
        item_view.setData(chipdata);
        item_view.updateView();
        item_view.setOnCheckedChangeListener(null);
        item_view.setChecked(is_select);
        item_view.setOnCheckedChangeListener(new OnCheckedFlagListener());
		
		return item_view;
	}
	
	/**
	 * チップのチェックボックスを変更した場合の処理を行うリスナー
	 */
	private class OnCheckedFlagListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			mSelectedGroupPosition = 0;
			mSelectedChildPosition = 0;
			
			int id = buttonView.getId();
			int group_position = getGroupPositionFromID(id);
			int child_position = getChildPositionFromID(id);
			
			// フラグ状態を設定する
			setSelect(group_position, child_position, isChecked);

			BBData target_item = getChild(group_position, child_position);

			if(isChecked) {
				mCustomData.addChip(target_item);
			}
			else {
				mCustomData.removeChip(target_item);
			}
			
			mChangedChipSetListener.changed();
			
			// 通常のリストとFavoriteリストのチェック状態を同期させるために、アダプタを更新する。
			SelectChipExpandableAdapter.this.notifyDataSetChanged();
		}
	}
	
	/**
	 * チップの設定状態が更新された際の処理を行うリスナー。
	 */
	public interface OnChangedChipSetBaseListener {
		void changed();
	}
	
	/**
	 * チップの設定状態が更新された際の処理を行うリスナーを設定する。
	 * @param listener リスナーのインスタンス
	 */
	public void setOnChengedChipSetListener(OnChangedChipSetBaseListener listener) {
		mChangedChipSetListener = listener;
	}

	/**
	 * 選択フラグを設定する。
	 * 
	 * お気に入りリストからの変更がある都合により、
	 * 指定位置のチェックを変更する方法ではチェック状況が同期できない。
	 * そのため、ポジション値は完全に無視することとし、
	 * 全リストの中身を確認し、対象のチップを探し出してチェックを変更する方式を採用する。
	 * 
	 * @param changed_chip 対象のチップ
	 * @param flag 設定するフラグ値
	 */
	public void setFlag(BBData changed_chip, boolean flag) {
		int groupCount = super.getGroupCount();
		
		for(int i=0; i<groupCount; i++) {
			int childrenCount = super.getChildrenCount(i);
			
			for(int j=0; j<childrenCount; j++) {
				BBData buf_chip = super.getChild(i, j);

				String tmp_name1 = buf_chip.get("名称");
				String tmp_name2 = changed_chip.get("名称");

				if(tmp_name1.equals(tmp_name2)) {
                    setSelect(i, j, flag);
					break;
				}
			}
		}
	}

	/**
	 * カスタマイズデータを読み込み、フラグに反映する。
	 */
	public void loadCustomData() {
		ArrayList<BBData> recent_list = mCustomData.getChips();
		
		int size = recent_list.size();
		for(int i=0; i<size; i++) {
			setFlag(recent_list.get(i), true);
		}
	}
	
	/**
	 * データを追加する。
	 * @param chip 追加するデータ
	 */
	public void addChild(BBData chip) {
		BBDataManager manager = BBDataManager.getInstance();
		
		if(chip.existCategory(BBDataManager.SKILL_CHIP_STR)) {
			addChild(SKILL_CHIP_CATEGORY_INDEX, chip);
		}
		else if(chip.existCategory(BBDataManager.POWERUP_CHIP_STR)) {
			addChild(POWERUP_CHIP_CATEGORY_INDEX, chip);
		}
		else if(manager.isActionChip(chip)) {
			addChild(ACTION_CHIP_CATEGORY_INDEX, chip);
		}
		
		// お気に入りリストに格納されているチップを追加する
		if(FavoriteManager.sChipStore.exist(chip.get("名称"))) {
            addChild(FAVORITE_CHIP_CATEGORY_INDEX, chip);
        }
	}
	
	/**
	 * データのリストを追加する。
	 * @param chiplist チップのリスト
	 */
	public void addChildren(ArrayList<BBData> chiplist) {
		int size = chiplist.size();
		for(int i=0; i<size; i++) {
			addChild(chiplist.get(i));
		}
	}

	/**
	 * 選択中のチップを次の項目に移動させる。
	 * 次の項目がない場合(現在選択中が最後の場合)は現在位置のままにする。
	 */
	public void selectNext() {
		int gourp_count = getGroupCount();
		for(int i=mSelectedGroupPosition; i<gourp_count; i++) {

			int start_pos;
			if(i == mSelectedGroupPosition) {
				start_pos = mSelectedChildPosition + 1;
			}
			else {
				start_pos = 0;
			}
			
			int children_count = getChildrenCount(i);
			for(int j=start_pos; j<children_count; j++) {
				if(isSelected(i, j)) {
					mSelectedGroupPosition = i;
					mSelectedChildPosition = j;
					return;
				}
			}
		}
	}

	/**
	 * 選択中のチップを前の項目に移動させる。
	 * 前の項目がない場合(現在選択中が最初の場合)は現在位置のままにする。
	 */
	public void selectLast() {
		for(int i=mSelectedGroupPosition; i>=0; i--) {

			int start_pos;
			if(i == mSelectedGroupPosition) {
				start_pos = mSelectedChildPosition - 1;
			}
			else {
				start_pos = getGroupCount() - 1;
			}

            int children_count = getChildrenCount(i);
			for(int j=start_pos; j>=0; j--) {
                if(isSelected(i, j)) {
					mSelectedGroupPosition = i;
					mSelectedChildPosition = j;
					return;
				}
			}
			
		}
	}
	
	/**
	 * 選択中のチップのグループ位置を返す。
	 * @return グループ位置
	 */
	public int getSelectedGroupPosition() {
		return mSelectedGroupPosition;
	}
	
	/**
	 * 選択中のチップの子の位置を返す。
	 * @return 子の位置
	 */
	public int getSelectedChildPosition() {
		return mSelectedChildPosition;
	}
}
