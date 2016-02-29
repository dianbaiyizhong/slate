package hhm.slate.activity.storyboard.view;

import hhm.slate.R;
import hhm.slate.activity.storyboard.adapter.SceneAdapter;
import hhm.slate.activity.storyboard.function.EditSceneActivity;
import hhm.slate.activity.storyboard.view.util.PosRecorder;
import hhm.slate.activity.storyboard.view.util.StringRecorder;
import hhm.slate.db.entity.Scene;
import hhm.slate.db.impl.SceneImpl;
import hhm.slate.util.tencent.ShareThread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.tencent.tauth.Tencent;

public class SceneMenuView extends LinearLayout {
	private Tencent mTencent;
	private ListView mListView;
	private OnSelectListener mOnSelectListener;
	private Context mContext;
	private String film_id;
	private SceneAdapter adapter;
	private Map<Integer, Scene> sceneMap = new HashMap<Integer, Scene>();
	private Activity activity;
	EditText et_scene_search;

	
	public SceneMenuView(Context context, Activity activity, String film_id) {
		super(context);

		this.film_id = film_id;
		this.mContext = context;
		this.activity = activity;
		this.mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_scene, this, true);
		mListView = (ListView) findViewById(R.id.sb_listView_scene);

		et_scene_search = (EditText) findViewById(R.id.et_scene_search);

		reload();
	}

	/**
	 * 加载数据
	 */
	private void loadData() {
		sceneMap = new HashMap<Integer, Scene>();

		// 从数据库获取要显示的值
		SceneImpl sceneImpl = new SceneImpl(mContext);
		final List<Scene> list = sceneImpl.query(film_id);
		for (int i = 0; i < list.size(); i++) {
			sceneMap.put(list.get(i).getScene_id(), list.get(i));
		}

		adapter = new SceneAdapter(mContext, list,
				PosRecorder.getScene_pos_id());
		mListView.setAdapter(adapter);
	}

	public void reload() {

		et_scene_search.setText("");
		loadData();
		initView();
	}

	@SuppressLint("NewApi")
	private void initView() {

		et_scene_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				String value = arg0.toString();
				adapter.Search(value);
			}

		});

		adapter.setOnItemClickListener(new SceneAdapter.OnItemClickListener() {

			public void onItemClick(View view, int position) {

				if (mOnSelectListener != null) {

					TextView tv_id = (TextView) view
							.findViewById(R.id.tv_sb_scene_id);

					TextView tv_number = (TextView) view
							.findViewById(R.id.tv_sb_scene_number);
					String showText = tv_number.getText().toString() + "场";

					PosRecorder.setScene_pos_id(Integer.parseInt(tv_id
							.getText().toString()));
					// 每次点击场，所有位置都要重置
					PosRecorder.reset();

					StringRecorder.setScene_ShowString(showText);
					StringRecorder.reset();

					mOnSelectListener.getValue();

				}
			}
		});

		adapter.setOnLongItemClickListener(new SceneAdapter.OnLongItemClickListener() {

			public void onLongItemClick(View view, int position) {

				TextView tv_number = (TextView) view
						.findViewById(R.id.tv_sb_scene_number);
				TextView tv = (TextView) view.findViewById(R.id.tv_sb_scene_id);
				final int scene_id = Integer.parseInt(tv.getText().toString());
				String showText = tv_number.getText().toString() + "场";

				PosRecorder.setScene_pos_id(scene_id);
				StringRecorder.setScene_ShowString(showText);
				StringRecorder.reset();
				mOnSelectListener.getValue();
				final AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setTitle("操作");
				builder.setIcon(R.drawable.ic_launcher);

				builder.setItems(new String[] { "编辑", "删除", "分享", "取消" },
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int index) {

								if (index == 0) {
									Intent intent = new Intent();
									intent.setClass(getContext(),
											EditSceneActivity.class);
									intent.putExtra("scene_id", scene_id);

									mContext.startActivity(intent);

								} else if (index == 1) {

									delete(scene_id);

								} else if (index == 2) {

									Share();
								} else if (index == 3) {

								}

							}

						});

				builder.show();

			}
		});

	}

	private void Share() {

		getLocation();

	}

	public Bundle getShareBundle() {

		SceneImpl sceneImpl = new SceneImpl(getContext());
		String scenePos[] = sceneImpl.queryPos(PosRecorder.getScene_pos_id());

		Bundle bundle = new Bundle();

		bundle.putString("title", "片场助手");

		bundle.putString(
				"imageUrl",
				"http://a3.qpic.cn/psb?/V12cqEz545kKsP/Fnv*TubP73zYjfLHwOKrIh46owNPj95i4Q6Lq6IQ15s!/b/dGsAAAAAAAAA&bo=AAIAAgACAAIDCSw!&rf=viewer_4");

		bundle.putString("targetUrl",
				"http://www.com179.com/path/cms/downloads/client/");
		bundle.putString("summary", "今天在<" + scenePos[0] + ">" + "拍摄了电影");
		bundle.putString("site", "2222");
		bundle.putString("appName", "片场助手");
		return bundle;
	}

	public String shareConditional() {

		if (address == null) {

			return "获取地址失败,请检查网络";
		}

		SceneImpl sceneImpl = new SceneImpl(getContext());
		String scenePos[];
		if (sceneImpl.queryPos(PosRecorder.getScene_pos_id()) == null) {
			return "你还没设置拍摄位置";
		} else {
			scenePos = sceneImpl.queryPos(PosRecorder.getScene_pos_id());
		}

		LatLng latlng1 = new LatLng(Double.valueOf(scenePos[1]),
				Double.valueOf(scenePos[2]));
		LatLng latlng2 = new LatLng(latitude, longitude);

		double distance = DistanceUtil.getDistance(latlng1, latlng2);

		if (distance > 20000) {
			return "你当前处在的地点离拍摄地过远，且为" + distance + "米" + "不符合发送条件";
		}

		return "ok";

	}

	private void delete(final int scene_id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("删除");
		builder.setIcon(R.drawable.ic_launcher);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				SceneImpl sceneImpl = new SceneImpl(getContext());
				adapter.remove(scene_id);
				sceneImpl.delete(scene_id);
				// 删除后，要重置位置
				PosRecorder.resetAll();

			}
		});
		builder.setNegativeButton("取消", null);

		builder.show();

	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		public void getValue();
	}

	public void LoadDistance(Context context, Double latitude, Double longitude) {
		adapter.LoadDistance(context, latitude, longitude);

	}

	/**************************************
 * 
 */

	/**
	 * 
	 * 
	 * 
	 * 百度地图的类
	 * 
	 * 
	 */
	public BDLocationListener myListener = new MyLocationListener();

	public LocationClient mLocationClient = null;

	public double latitude;
	public double longitude;
	public String address;

	public void getLocation() {
		mLocationClient = new LocationClient(getContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		mLocationClient.requestLocation();

	}

	class MyLocationListener/* 内部类 */implements BDLocationListener {

		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			address = location.getAddrStr();
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			try {
				mLocationClient.stop();
			} catch (Exception e) {
				e.printStackTrace();

			}

			// 先获取地理位置是否普符合规则
			String value = shareConditional();
			if (value.equals("ok")) {
				// 传口关闭才调用分享，否则会报错
				mTencent = Tencent.createInstance("1104814903", getContext());
				ShareThread st = new ShareThread(mTencent, activity,
						getShareBundle());
				st.start();

			} else {

				Toast.makeText(getContext(), value, 5000).show();
			}

		}
	}

}
