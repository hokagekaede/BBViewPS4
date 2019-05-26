package hokage.kaede.gmail.com.BBViewPS4.Custom;

import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.CustomAdapter;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.CustomAdapterItemCategory;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.CustomAdapterItemChip;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomData;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 「チップ」画面を表示するクラス。
 */
public class ChipView extends FrameLayout implements android.widget.AdapterView.OnItemClickListener {

	private static final int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
	private static final int FP = LinearLayout.LayoutParams.FILL_PARENT;

	private static int sLastPosition = -1;
	private static int sLastListTop = -1;

	public ChipView(Context context, CustomData custom_data) {
		super(context);

		this.setLayoutParams(new FrameLayout.LayoutParams(FP, FP));

		createViewColOne(context, custom_data);
	}

	/**
	 * 1列設定時の画面設定を行う。
	 * @param context
	 * @param custom_data
	 */
	private void createViewColOne(Context context, CustomData custom_data) {

		ListView list_view = new ListView(context);
		CustomAdapter adapter = new CustomAdapter(context);

		list_view.setAdapter(adapter);
		list_view.setOnItemClickListener(this);

		adapter.addItem(new CustomAdapterItemCategory(context, "頭部チップ"));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_HEAD, 0));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_HEAD, 1));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_HEAD, 2));

		adapter.addItem(new CustomAdapterItemCategory(context, "胴部チップ"));
        adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_BODY, 0));
        adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_BODY, 1));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_BODY, 2));

		adapter.addItem(new CustomAdapterItemCategory(context, "腕部チップ"));
        adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_ARMS, 0));
        adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_ARMS, 1));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_ARMS, 2));

		adapter.addItem(new CustomAdapterItemCategory(context, "脚部チップ"));
        adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_LEGS, 0));
        adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_LEGS, 1));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_LEGS, 2));

		adapter.addItem(new CustomAdapterItemCategory(context, "サポートチップ"));
        adapter.addItem(createItem(context, custom_data, "サポートチップ", 0));
        adapter.addItem(createItem(context, custom_data, "サポートチップ", 1));
		adapter.addItem(createItem(context, custom_data, "サポートチップ", 2));

		if(sLastPosition >= 0) {
			list_view.setSelectionFromTop(sLastPosition, sLastListTop);
		}

		this.addView(list_view);
	}

	private static CustomAdapter.CustomAdapterBaseItem createItem(Context context, CustomData custom_data, String type, int index) {
		BBData data = custom_data.getChip(type, index);
		String summary = type + String.format("チップ%02d", index);

		return new CustomAdapterItemChip(context, data, summary, type, index);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		sLastPosition = arg0.getFirstVisiblePosition();
		sLastListTop = arg0.getChildAt(0).getTop();

		CustomAdapter adapter = (CustomAdapter)arg0.getAdapter();
		CustomAdapter.CustomAdapterBaseItem base_item = adapter.getItem(position);
		base_item.click();
	}
}
