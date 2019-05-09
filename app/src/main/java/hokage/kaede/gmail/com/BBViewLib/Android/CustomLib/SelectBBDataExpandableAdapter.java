package hokage.kaede.gmail.com.BBViewLib.Android.CustomLib;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BBDataAdapterItemProperty;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.BBDataExpandableAdapter;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.ControlPanel;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.ControlPanelBuilder;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;

/**
 * 「パーツ武器選択」画面のカテゴリ表示の一覧を生成するクラス。
 */
public abstract class SelectBBDataExpandableAdapter extends BBDataExpandableAdapter {

	/**
	 * 初期化を行う。
	 * @param property 表示のためのデータと設定値
	 */
	public SelectBBDataExpandableAdapter(BBDataAdapterItemProperty property) {
		super(property);

		mCategoryTextColor = SettingManager.getColorWhite();
		mCategoryBackGroundColor = SettingManager.getColorBlue();
	}

	/**
	 * データのリストを追加する。
	 * @param itemlist チップのリスト
	 */
	abstract public void addChildren(ArrayList<BBData> itemlist);

}
