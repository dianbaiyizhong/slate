package hhm.slate.activity.storyboard.function;

import hhm.slate.R;
import hhm.slate.activity.storyboard.function.adatpter.ResultAdapter;
import hhm.slate.activity.storyboard.function.entity.ResultInfo;
import hhm.slate.util.DataUtil;
import hhm.slate.util.VerifyUtil;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

/**
 * 演示poi搜索功能
 */
public class MapSearchActivity extends FragmentActivity implements
		OnGetPoiSearchResultListener, OnGetSuggestionResultListener {
	// 定义这个变量来保存经纬度的信息
	private String[] scene_pos = new String[3];
	private String returnValue;
	private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;
	private BaiduMap mBaiduMap = null;
	public LocationClient locationClient = null;
	boolean isFirstLoc = true;

	/**
	 * 搜索关键字输入窗口
	 */
	private AutoCompleteTextView actv_searchkey = null;
	EditText et_map_lal;
	EditText et_map_long;
	EditText et_map_pos;
	private ResultAdapter sugAdapter;
	private int load_Index = 0;

	private Button btn_pos_selected;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SDKInitializer.initialize(getApplicationContext());
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_mapsearch);

		et_map_lal = (EditText) findViewById(R.id.et_map_lal);
		et_map_long = (EditText) findViewById(R.id.et_map_long);
		et_map_pos = (EditText) findViewById(R.id.et_map_pos);

		sugAdapter = new ResultAdapter(this);

		// 获取返回参数
		Bundle bundle = this.getIntent().getExtras();

		// 获取secne_id
		this.returnValue = bundle.getString("returnValue");

		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		actv_searchkey = (AutoCompleteTextView) findViewById(R.id.actv_searchkey);
		actv_searchkey.setAdapter(sugAdapter);

		actv_searchkey.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg1, View view, int arg2,
					long arg3) {

				scene_pos[0] = ((TextView) view
						.findViewById(R.id.actv_mapsearch_key)).getText()
						.toString();

				String pt[] = ((TextView) view
						.findViewById(R.id.actv_mapsearch_pt)).getText()
						.toString().split("_");

				scene_pos[1] = pt[0];
				scene_pos[2] = pt[1];

				setToET();

			}

		});

		mBaiduMap = ((SupportMapFragment) (getSupportFragmentManager()
				.findFragmentById(R.id.map))).getBaiduMap();

		mBaiduMap.setMyLocationEnabled(true);

		locationClient = new LocationClient(getApplicationContext());
		locationClient.registerLocationListener(myListener);
		this.setLocationOption();
		locationClient.start();

		/**
		 * 当输入关键字变化时，动态更新建议列表
		 */
		actv_searchkey.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable arg0) {

			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {

				// 这个的作用就是，在用户选择自己再次输入地址的时候，就不要再使用自动定位填写了。。。此段代码可以去除从而查看负面影响
				if (locationClient.isStarted()) {
					locationClient.stop();
				}

				if (cs.length() <= 0) {
					return;
				}

				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city(""));
			}
		});

		LoadSelectedPosBtn();

	}

	private void setToET() {
		et_map_lal.setText("纬度:" + du.getM2(scene_pos[1]));
		et_map_long.setText("经度:" + du.getM2(scene_pos[2]));

		if (scene_pos[0] == null) {
			et_map_pos.setText("");
		} else {
			et_map_pos.setText("选择的地点为:" + scene_pos[0]);

		}
	}

	private boolean verify() {

		List<EditText> list = new ArrayList<EditText>();
		list.add(et_map_pos);

		boolean bool = new VerifyUtil().verifyIsNull(getApplicationContext(),
				list);

		if (bool) {
			return true;

		} else {
			return false;
		}

	}

	private void LoadSelectedPosBtn() {
		btn_pos_selected = (Button) findViewById(R.id.btn_pos_selected);
		btn_pos_selected.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				// 验证判断

				if (!verify()) {
					return;
				}
				// 由于这个类要被 添加 和修改 都调用 ，所以这里要有一个返回判断
				Intent intent = null;
				if (returnValue.equals("add")) {
					intent = new Intent(MapSearchActivity.this,
							AddSceneActivity.class);

				} else if (returnValue.equals("edit")) {
					intent = new Intent(MapSearchActivity.this,
							EditSceneActivity.class);
				}

				intent.putExtra("scene_pos", scene_pos);

				setResult(RESULT_OK, intent);

				finish();

			}
		});
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onDestroy() {
		mPoiSearch.destroy();
		mSuggestionSearch.destroy();
		super.onDestroy();
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * 影响搜索按钮点击事件
	 * 
	 * @param v
	 */
	public void searchButtonProcess(View v) {
		EditText actv_searchkey = (EditText) findViewById(R.id.actv_searchkey);
		mPoiSearch.searchInCity((new PoiCitySearchOption()).city("")
				.keyword(actv_searchkey.getText().toString())
				.pageNum(load_Index));
	}

	public void goToNextPage(View v) {
		load_Index++;
		searchButtonProcess(null);
	}

	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(MapSearchActivity.this, "未找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			mBaiduMap.clear();
			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
			String strInfo = "在";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "找到结果";
			Toast.makeText(MapSearchActivity.this, strInfo, Toast.LENGTH_LONG)
					.show();
		}
	}

	static DataUtil du = new DataUtil();

	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MapSearchActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		} else {

			// 通过在地图上点击,获取结果
			Toast.makeText(MapSearchActivity.this,
					result.getName() + ": " + result.getAddress(),
					Toast.LENGTH_SHORT).show();

			// 在编辑框中显示
			actv_searchkey.setText(result.getAddress());
			scene_pos[0] = result.getAddress();
			scene_pos[1] = String.valueOf(result.getLocation().latitude);
			scene_pos[2] = String.valueOf(result.getLocation().longitude);
			setToET();

		}
	}

	public void onGetSuggestionResult(SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		sugAdapter.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			if (info.key != null && info.pt != null) {

				ResultInfo resultInfo = new ResultInfo();
				resultInfo.setKey(info.key);
				resultInfo.setLatitude(String.valueOf(info.pt.latitude));
				resultInfo.setLongitude(String.valueOf(info.pt.longitude));
				sugAdapter.add(resultInfo);

			}

		}

		sugAdapter.notifyDataSetChanged();

	}

	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
					.poiUid(poi.uid));

			return true;
		}
	}

	public BDLocationListener myListener = new BDLocationListener() {

		public void onReceiveLocation(BDLocation location) {
			if (location == null || mBaiduMap == null)
				return;

			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius()).direction(100)
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();

			try {
				mBaiduMap.setMyLocationData(locData);

			} catch (Exception e) {
				return;

			}

			// 设置默认值
			actv_searchkey.setText(location.getAddrStr());
			scene_pos[0] = location.getAddrStr();
			scene_pos[1] = String.valueOf(location.getLatitude());
			scene_pos[2] = String.valueOf(location.getLongitude());
			setToET();

			if (isFirstLoc) {
				isFirstLoc = false;

				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory
						.newLatLngZoom(ll, 16);
				mBaiduMap.animateMapStatus(u);
			}
		}
	};

	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(5000);
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(true);

		locationClient.setLocOption(option);
	}

	public void finish(View view) {
		finish();
	}

}
