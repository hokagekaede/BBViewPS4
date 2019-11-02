package hokage.kaede.gmail.com.BBViewPS4.Custom;

import hokage.kaede.gmail.com.BBViewPS4.Item.InfoActivity;
import hokage.kaede.gmail.com.BBViewLib.Java.BBData;
import hokage.kaede.gmail.com.BBViewLib.Java.BBDataManager;
import hokage.kaede.gmail.com.BBViewLib.Java.BBViewSetting;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomData;
import hokage.kaede.gmail.com.BBViewLib.Java.CustomFileManager;
import hokage.kaede.gmail.com.BBViewLib.Java.SpecValues;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.CustomAdapter;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.CustomAdapterItemCategory;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.CustomAdapterItemParts;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.CustomAdapterItemReqArm;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.CustomAdapterItemWeapon;
import hokage.kaede.gmail.com.BBViewLib.Android.CustomLib.CustomAdapter.CustomAdapterBaseItem;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.IntentManager;
import hokage.kaede.gmail.com.BBViewLib.Android.CommonLib.ViewBuilder;
import hokage.kaede.gmail.com.StandardLib.Android.SettingManager;
import hokage.kaede.gmail.com.StandardLib.Java.FileIO;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * 「アセン」画面を表示するクラス。
 */
public class CustomView extends FrameLayout implements android.widget.AdapterView.OnItemClickListener {

	private static final int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
	private static final int FP = LinearLayout.LayoutParams.FILL_PARENT;
	
	private static int sLastPosition = -1;
	private static int sLastListTop = -1;

	public CustomView(Context context, CustomData custom_data) {
		super(context);

		this.setLayoutParams(new FrameLayout.LayoutParams(FP, FP));

		String file_dir = context.getFilesDir().toString();
		CustomFileManager custom_mng = CustomFileManager.getInstance(file_dir);

		if(BBViewSetting.IS_SHOW_COLUMN2) {
			createViewColTwo(context, custom_data);
		}
		else {
			createViewColOne(context, custom_data);
		}
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

		String base_spec_str = String.format("機体 (装甲：%.1f (%%))", custom_data.getArmorAve());
		
		adapter.addItem(new CustomAdapterItemCategory(context, base_spec_str));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_HEAD));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_BODY));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_ARMS));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_LEGS));
		
		String assult_spec_str = String.format("%s (積載：%.1f / 初速：%.2f)",
				BBDataManager.BLUST_TYPE_ASSALT, 
				custom_data.getSpaceWeight(BBDataManager.BLUST_TYPE_ASSALT), 
				custom_data.getStartDush(BBDataManager.BLUST_TYPE_ASSALT));
		
		adapter.addItem(new CustomAdapterItemCategory(context, assult_spec_str));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_ASSALT, BBDataManager.WEAPON_TYPE_MAIN));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_ASSALT, BBDataManager.WEAPON_TYPE_SUB));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_ASSALT, BBDataManager.WEAPON_TYPE_SUPPORT));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_ASSALT, BBDataManager.WEAPON_TYPE_SPECIAL));

		String heavy_spec_str = String.format("%s (積載：%.1f / 初速：%.2f)",
				BBDataManager.BLUST_TYPE_HEAVY, 
				custom_data.getSpaceWeight(BBDataManager.BLUST_TYPE_HEAVY), 
				custom_data.getStartDush(BBDataManager.BLUST_TYPE_HEAVY));
		
		adapter.addItem(new CustomAdapterItemCategory(context, heavy_spec_str));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_HEAVY, BBDataManager.WEAPON_TYPE_MAIN));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_HEAVY, BBDataManager.WEAPON_TYPE_SUB));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_HEAVY, BBDataManager.WEAPON_TYPE_SUPPORT));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_HEAVY, BBDataManager.WEAPON_TYPE_SPECIAL));
	
		String sniper_spec_str = String.format("%s (積載：%.1f / 初速：%.2f)",
				BBDataManager.BLUST_TYPE_SNIPER, 
				custom_data.getSpaceWeight(BBDataManager.BLUST_TYPE_SNIPER), 
				custom_data.getStartDush(BBDataManager.BLUST_TYPE_SNIPER));
		
		adapter.addItem(new CustomAdapterItemCategory(context, sniper_spec_str));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_SNIPER, BBDataManager.WEAPON_TYPE_MAIN));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_SNIPER, BBDataManager.WEAPON_TYPE_SUB));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_SNIPER, BBDataManager.WEAPON_TYPE_SUPPORT));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_SNIPER, BBDataManager.WEAPON_TYPE_SPECIAL));

		String support_spec_str = String.format("%s (積載：%.1f / 初速：%.2f)",
				BBDataManager.BLUST_TYPE_SUPPORT, 
				custom_data.getSpaceWeight(BBDataManager.BLUST_TYPE_SUPPORT), 
				custom_data.getStartDush(BBDataManager.BLUST_TYPE_SUPPORT));
		
		adapter.addItem(new CustomAdapterItemCategory(context, support_spec_str));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_SUPPORT, BBDataManager.WEAPON_TYPE_MAIN));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_SUPPORT, BBDataManager.WEAPON_TYPE_SUB));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_SUPPORT, BBDataManager.WEAPON_TYPE_SUPPORT));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_SUPPORT, BBDataManager.WEAPON_TYPE_SPECIAL));

		adapter.addItem(new CustomAdapterItemCategory(context, "その他"));
		adapter.addItem(new CustomAdapterItemReqArm(context, custom_data.getReqArm()));

		if(sLastPosition >= 0) {
			list_view.setSelectionFromTop(sLastPosition, sLastListTop);
		}

		this.addView(list_view);
	}

	/**
	 * 2列設定時の画面設定を行う。
	 * @param context
	 * @param custom_data
	 */
	private void createViewColTwo(Context context, CustomData custom_data) {

		LinearLayout base_layout = new LinearLayout(context);
		base_layout.setOrientation(LinearLayout.VERTICAL);
		base_layout.setLayoutParams(new LinearLayout.LayoutParams(FP, FP));
		
		ListView list_view = new ListView(context);
		CustomAdapter adapter = new CustomAdapter(context);
		
		list_view.setAdapter(adapter);
		list_view.setOnItemClickListener(this);

		String base_spec_str = String.format("機体 (装甲：%.1f (%%))", custom_data.getArmorAve());
		
		adapter.addItem(new CustomAdapterItemCategory(context, base_spec_str));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_HEAD));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_BODY));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_ARMS));
		adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_PARTS_LEGS));

		adapter.addItem(new CustomAdapterItemCategory(context, "その他"));
		adapter.addItem(new CustomAdapterItemReqArm(context, custom_data.getReqArm()));
		
		// 2段組みのリスト
		GridView grid_view = new GridView(context);
		CustomAdapter grid_adapter = new CustomAdapter(context);
		
		grid_view.setNumColumns(2);
		grid_view.setAdapter(grid_adapter);
		grid_view.setLayoutParams(new TableLayout.LayoutParams(FP, WC, 1));
		grid_view.setOnItemClickListener(this);
		grid_view.setOnItemLongClickListener(new OnWeaponLongClickListener());
		
		String assult_spec_str = String.format("%s (積載：%.1f / 初速：%.2f)",
				BBDataManager.BLUST_TYPE_ASSALT, 
				custom_data.getSpaceWeight(BBDataManager.BLUST_TYPE_ASSALT), 
				custom_data.getStartDush(BBDataManager.BLUST_TYPE_ASSALT));

		String heavy_spec_str = String.format("%s (積載：%.1f / 初速：%.2f)",
				BBDataManager.BLUST_TYPE_HEAVY, 
				custom_data.getSpaceWeight(BBDataManager.BLUST_TYPE_HEAVY), 
				custom_data.getStartDush(BBDataManager.BLUST_TYPE_HEAVY));

		String sniper_spec_str = String.format("%s (積載：%.1f / 初速：%.2f)",
				BBDataManager.BLUST_TYPE_SNIPER, 
				custom_data.getSpaceWeight(BBDataManager.BLUST_TYPE_SNIPER), 
				custom_data.getStartDush(BBDataManager.BLUST_TYPE_SNIPER));

		String support_spec_str = String.format("%s (積載：%.1f / 初速：%.2f)",
				BBDataManager.BLUST_TYPE_SUPPORT, 
				custom_data.getSpaceWeight(BBDataManager.BLUST_TYPE_SUPPORT), 
				custom_data.getStartDush(BBDataManager.BLUST_TYPE_SUPPORT));
		
		grid_adapter.addItem(new CustomAdapterItemCategory(context, assult_spec_str));
		grid_adapter.addItem(new CustomAdapterItemCategory(context, sniper_spec_str));
		
		grid_adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_ASSALT, BBDataManager.WEAPON_TYPE_MAIN));
		grid_adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_SNIPER, BBDataManager.WEAPON_TYPE_MAIN));
		
		grid_adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_ASSALT, BBDataManager.WEAPON_TYPE_SUB));
		grid_adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_SNIPER, BBDataManager.WEAPON_TYPE_SUB));
		
		grid_adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_ASSALT, BBDataManager.WEAPON_TYPE_SUPPORT));
		grid_adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_SNIPER, BBDataManager.WEAPON_TYPE_SUPPORT));
		
		grid_adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_ASSALT, BBDataManager.WEAPON_TYPE_SPECIAL));
		grid_adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_SNIPER, BBDataManager.WEAPON_TYPE_SPECIAL));

		grid_adapter.addItem(new CustomAdapterItemCategory(context, heavy_spec_str));
		grid_adapter.addItem(new CustomAdapterItemCategory(context, support_spec_str));
		
		grid_adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_HEAVY, BBDataManager.WEAPON_TYPE_MAIN));
		grid_adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_SUPPORT, BBDataManager.WEAPON_TYPE_MAIN));
		
		grid_adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_HEAVY, BBDataManager.WEAPON_TYPE_SUB));
		grid_adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_SUPPORT, BBDataManager.WEAPON_TYPE_SUB));
		
		grid_adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_HEAVY, BBDataManager.WEAPON_TYPE_SUPPORT));
		grid_adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_SUPPORT, BBDataManager.WEAPON_TYPE_SUPPORT));
		
		grid_adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_HEAVY, BBDataManager.WEAPON_TYPE_SPECIAL));
		grid_adapter.addItem(createItem(context, custom_data, BBDataManager.BLUST_TYPE_SUPPORT, BBDataManager.WEAPON_TYPE_SPECIAL));
		
		base_layout.addView(list_view);
		base_layout.addView(grid_view);
		
		this.addView(base_layout);
	}
	
	private static CustomAdapterBaseItem createItem(Context context, CustomData custom_data, String type) {
		BBData data = custom_data.getParts(type);
		String title = data.get("名称");
		String summary = type;
	
		if(type.equals(BBDataManager.BLUST_PARTS_HEAD)) {
			title = title + " (頭部)";
			summary = String.format("装：%s / 射：%s / 索：%s / ロ：%s / 回：%s",
					getPoint(data, "装甲"),
					getPoint(data, "射撃補正"),
					getPoint(data, "索敵"),
					getPoint(data, "ロックオン"),
					getPoint(data, "DEF回復"));
		}
		else if(type.equals(BBDataManager.BLUST_PARTS_BODY)) {
			title = title + " (胴部)";
			summary = String.format("装：%s / ブ：%s / SP：%s / エ：%s / 耐：%s",
					getPoint(data, "装甲"),
					getPoint(data, "ブースター"),
					getPoint(data, "SP供給"),
					getPoint(data, "エリア移動"),
					getPoint(data, "DEF耐久"));
		}
		else if(type.equals(BBDataManager.BLUST_PARTS_ARMS)) {
			title = title + " (腕部)";
			summary = String.format("装：%s / 反：%s / リ：%s / 武：%s / 弾：%s",
					getPoint(data, "装甲"),
					getPoint(data, "反動吸収"),
					getPoint(data, "リロード"),
					getPoint(data, "武器変更"),
					getPoint(data, "予備弾数"));
		}
		else if(type.equals(BBDataManager.BLUST_PARTS_LEGS)) {
			title = title + " (脚部)";
			summary = String.format("装：%s / 歩：%s / ダ：%s / 重：%s / 巡：%s",
					getPoint(data, "装甲"),
					getPoint(data, "歩行"),
					getPoint(data, "ダッシュ"),
					getPoint(data, "重量耐性"),
					getPoint(data, "巡航"));
		}
		
		return new CustomAdapterItemParts(context, data, summary, type);
	}

	private static String getPoint(BBData data, String key) {
		String name = data.get("名称");
		String spec = data.get(key);
		double value = SpecValues.getSpecValue(spec, key, name);
		return SpecValues.getPoint(key, value);
	}

	private static CustomAdapterBaseItem createItem(Context context, CustomData custom_data, String blust_type, String weapon_type) {
		BBData data = custom_data.getWeapon(blust_type, weapon_type);
		return new CustomAdapterItemWeapon(context, data, blust_type, weapon_type);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		sLastPosition = arg0.getFirstVisiblePosition();
		sLastListTop = arg0.getChildAt(0).getTop();
		
		CustomAdapter adapter = (CustomAdapter)arg0.getAdapter();
		CustomAdapterBaseItem base_item = adapter.getItem(position);
		base_item.click();
	}
	
	/**
	 * 武器を長タップした際の処理を行う。
	 */
	private class OnWeaponLongClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			CustomAdapter adapter = (CustomAdapter)parent.getAdapter();
			CustomAdapterBaseItem item_view = adapter.getItem(position);
			
			moveInfoActivity(item_view.getItem());
			return false;
		}
	}

	/**
	 * 詳細画面へ移動する。
	 * @param to_item 詳細画面で表示するデータ
	 */
	private void moveInfoActivity(BBData to_item) {
		Context context = this.getContext();
		Intent intent = new Intent(context, InfoActivity.class);
		IntentManager.setSelectedData(intent, to_item);
		context.startActivity(intent);
	}
}
