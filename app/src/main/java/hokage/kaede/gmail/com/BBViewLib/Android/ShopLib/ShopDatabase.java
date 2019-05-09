package hokage.kaede.gmail.com.BBViewLib.Android.ShopLib;

import hokage.kaede.gmail.com.StandardLib.Java.NetAccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * 店舗情報のデータを管理するクラス。
 */
public class ShopDatabase {

	// 地域情報1
	public static final String[] AREA_LIST = {
		"北海道", "青森県", "岩手県", "秋田県", "宮城県", 
		"山形県", "福島県", "群馬県", "栃木県", "茨城県", 
		"埼玉県", "千葉県", "東京都", "神奈川県", "山梨県", 
		"新潟県", "長野県", "富山県", "石川県", "福井県", 
		"静岡県", "愛知県", "岐阜県", "三重県", "滋賀県", 
		"京都府", "奈良県", "大阪府", "和歌山県", "兵庫県", 
		"鳥取県", "岡山県", "広島県", "島根県", "山口県", 
		"徳島県", "香川県", "愛媛県", "高知県", "福岡県", 
		"大分県", "佐賀県", "長崎県", "宮崎県", "熊本県", 
		"鹿児島県", "沖縄県"
	};
	
	// 店舗情報が記載されたスクリプトファイルのURL
	private static final String PLACE_URL = "http://borderbreak.com/json/stores.json";

	// 店舗データ
	private String mShopDataString;
	
	// 店舗データjson
	private JSONObject mShopDataJSON;

	// 店舗名データ一覧
	private ArrayList<ShopData> mShopDataList;
	
	// キーワードに合致する店舗名の一覧
	private ArrayList<ShopData> mTargetList;
	
	// キーワード設定の有無
	private boolean mIsKeyWord;
	
	private static ShopDatabase mShopDatabase = new ShopDatabase();
	
	/**
	 * 地域データ管理オブジェクトを取得する。
	 * @return 地域データ管理オブジェクト
	 */
	public static ShopDatabase getShopDatabase() {
		return mShopDatabase;
	}
	
	/**
	 * 初期化処理を行う
	 */
	private ShopDatabase() {
		mIsKeyWord = false;
		mShopDataList = new ArrayList<ShopData>();
		mTargetList = new ArrayList<ShopData>();
	}
	
	/**
	 * リストをクリアする。
	 */
	public void clear() {
		mIsKeyWord = false;
		mShopDataList.clear();
		mTargetList.clear();
	}
	
	/**
	 * 公式サイトから店舗情報を取得する
	 * @return 読み込みに成功した場合はtrueを返し、失敗した場合はfalseを返す。
	 */
	public boolean readWebData() {
		if(mShopDataString == null) {
			mShopDataString = NetAccess.readString(PLACE_URL, "UTF-8");
			
			if(mShopDataString == null) {
				return false;
			}

			try {
				mShopDataJSON = new JSONObject(mShopDataString);
			} catch (JSONException e) {
				mShopDataString = "";
				return false;
			}
		}
		
		return true;
	}

	/**
	 * 地域ごとの店舗情報を取得する
	 * @param location_name 地域名
	 */
	public void readLocationData(String location_name) {
		if(mShopDataString == null) {
			return;
		}
		else if(mShopDataString.equals("")) {
			return;
		}
		
		// 店舗リストをクリアする
		mShopDataList.clear();
		
		// 店舗データのルート要素を取得
		JSONArray shop_list;
		try {
			shop_list = mShopDataJSON.getJSONArray("stores");
		} catch (JSONException e) {
			return;
		}

		// 店舗名と住所を取得する
		for (int i = 0; i < shop_list.length(); i++) {
			try {
				JSONObject shop = shop_list.getJSONObject(i);
				if(shop.getString("prefecture").equals(location_name)) {
					ShopData tmp_data = new ShopData();
					tmp_data.name = shop.getString("name");
					tmp_data.address = shop.getString("address");
					mShopDataList.add(tmp_data);
				}
			} catch (JSONException e) {
				break;
			}
		}
	}

	/**
	 * 店舗数を取得する
	 * @return 店舗数
	 */
	public int getCount() {
		if(mIsKeyWord) {
			return mTargetList.size();
		}
		
		return mShopDataList.size();
	}
	
	/**
	 * 店舗データを取得する
	 * @param index 店舗番号
	 * @return 店舗データ
	 */
	public ShopData getItem(int index) {
		if(mIsKeyWord) {
			if(index < 0 || index >= mTargetList.size()) {
				return null;
			}
			
			return mTargetList.get(index);
		}
		
		if(index < 0 || index >= mShopDataList.size()) {
			return null;
		}
		
		return mShopDataList.get(index);
	}
	
	/**
	 * キーワードを設定し、リストを更新する
	 * @param keyword 設定するキーワード。nullを設定した場合は解除する。
	 */
	public void setKeyword(String keyword) {
		if(keyword == null || keyword.length() == 0) {
			mIsKeyWord = false;
			mTargetList.clear();
		}
		else {
			mIsKeyWord = true;
			mTargetList.clear();
			
			int size = mShopDataList.size();
			for(int i=0; i<size; i++) {
				ShopData tmp_data = mShopDataList.get(i);
				
				if(tmp_data.name.contains(keyword) || tmp_data.address.contains(keyword)) {
					mTargetList.add(tmp_data);
				}
			}
		}
	}
	
	/**
	 * 建物・地名からの検索処理の進捗更新時の処理を行うリスナー。
	 */
	public interface UpdateProgressListener {
		public void Update(int size, int finish);
	}
	
	private UpdateProgressListener mDistanceListener;
	
	/**
	 * 建物・地名からの検索処理の進捗更新時の処理を行うリスナーを設定する。
	 * @param listener リスナー
	 */
	public void setOnUpdateProgressListener(UpdateProgressListener listener) {
		mDistanceListener = listener;
	}
	
	/**
	 * 指定の住所情報から店舗までの距離を設定し、リストを更新する。
	 * @param context
	 * @param recent_address
	 * @param distance
	 * @return
	 */
	public boolean setDistance(Context context, Address recent_address, float distance) {
		boolean ret = false;
		float[] ret_distance = new float[3];

		if(recent_address != null) {
			mIsKeyWord = true;
			mTargetList.clear();
			
			int size = mShopDataList.size();
			mDistanceListener.Update(size, 0);
			
			for(int i=0; i<size; i++) {
				ShopData shopdata = mShopDataList.get(i);
				Address target_address = getAddress(context, shopdata.address);
				if(target_address != null) {
					Location.distanceBetween(recent_address.getLatitude(), recent_address.getLongitude(),
							target_address.getLatitude(), target_address.getLongitude(), ret_distance);
					
					if(distance > ret_distance[0]) {
						shopdata.distance = ret_distance[0];
						mTargetList.add(shopdata);
					}
				}
				
				mDistanceListener.Update(size, i);
			}
			
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 地名などから住所情報を取得する。
	 * @param context
	 * @param name
	 * @return
	 */
	public static Address getAddress(Context context, String name) {
		Geocoder coder = new Geocoder(context);
		Address recent_address = null;

		try {
			List<Address> address_list = coder.getFromLocationName(name, 1);
			
			if(address_list.size() > 0) {
				recent_address = address_list.get(0);
			}
			
		} catch (IOException e) {
			recent_address = null;
		}
		
		return recent_address;
	}
}
