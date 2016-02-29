package hhm.slate.activity.storyboard;

import hhm.slate.R;
import hhm.slate.activity.storyboard.dialog.AddShotDialog;
import hhm.slate.activity.storyboard.dialog.AlertDialog;
import hhm.slate.activity.storyboard.function.AddSceneActivity;
import hhm.slate.activity.storyboard.function.RollActivity;
import hhm.slate.activity.storyboard.function.ShotsActivity;
import hhm.slate.activity.storyboard.function.StaffActivity;
import hhm.slate.activity.storyboard.view.SceneMenuFragment;
import hhm.slate.activity.storyboard.view.ShotAndTakeMenuFragment;
import hhm.slate.activity.storyboard.view.util.DisRecorder;
import hhm.slate.activity.storyboard.view.util.PosRecorder;
import hhm.slate.activity.storyboard.view.util.StringRecorder;
import hhm.slate.db.entity.Scene;
import hhm.slate.db.impl.FilmImpl;
import hhm.slate.db.impl.SceneImpl;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.ext.SatelliteMenu;
import android.view.ext.SatelliteMenu.SateliteClickedListener;
import android.view.ext.SatelliteMenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

@SuppressLint("NewApi")
public class StoryBoardActivity extends FragmentActivity {

	LinearLayout ll_storyboard_shooting;
	Button btn_startShooting;
	TextView tv_view_scene_current_pos;
	private String film_id;
	ImageView iv_sb_return;
	private SceneMenuFragment sceneMenuFragment;
	private ShotAndTakeMenuFragment shotAndTakeMenuFragment;

	public static Button btn_shotAndTake;
	public static Button btn_scene;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_storyboard);

		// SDKInitializer.initialize(getApplicationContext());
		Bundle bundle = this.getIntent().getExtras();
		String film_id = bundle.getString("film_id");
		// 获取了从剧本选择界面传来的film_id之后，记录下来
		this.film_id = film_id;
		// 加载好一些必须的控件
		LoadWidgets();
		// 加载卫星菜单插件
		LoadSateLlite();
		// 加载拍板的控件
		// 重置位置信息
		PosRecorder.resetAll();

		StringRecorder.resetAll();

		if (PosRecorder.getCurrent_pos() != null) {

			tv_view_scene_current_pos.setText(PosRecorder.getCurrent_pos());
		} else {

			// 初始化所有的距离设置为空值
			FilmImpl filmImpl = new FilmImpl(getApplicationContext());
			Scene scene = new Scene();
			scene.setFilm_id(Integer.parseInt(film_id));
			scene.setDistance("");
			filmImpl.updateNullAllDistance(scene);
		}

		LoadSceneView();

		LoadShotAndTakeView();

	}

	private void LoadSceneView() {

		sceneMenuFragment = SceneMenuFragment.getInstance();
		sceneMenuFragment.setFilm(film_id);
		sceneMenuFragment.setActivity(StoryBoardActivity.this);
	}

	private void LoadShotAndTakeView() {
		shotAndTakeMenuFragment = shotAndTakeMenuFragment.getInstance();

	}

	private void LoadWidgets() {
		btn_shotAndTake = (Button) findViewById(R.id.btn_shotAndTake);

		btn_scene = (Button) findViewById(R.id.btn_scene);

		tv_view_scene_current_pos = (TextView) findViewById(R.id.tv_view_scene_current_pos);

		iv_sb_return = (ImageView) findViewById(R.id.iv_sb_return);
		iv_sb_return.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});

		btn_scene.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showFragmentMenu("1");
			}
		});

		btn_scene.setText("请选择 场");

		btn_shotAndTake.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showFragmentMenu("2");
			}

		});
		btn_shotAndTake.setText("请选择镜 次");

	}

	/**
	 * 加载卫星菜单
	 */
	private void LoadSateLlite() {
		SatelliteMenu menu = (SatelliteMenu) findViewById(R.id.menu_satellite);

		List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
		items.add(new SatelliteMenuItem(6, R.drawable.icon_sate_06));
		items.add(new SatelliteMenuItem(5, R.drawable.icon_sate_05));
		items.add(new SatelliteMenuItem(4, R.drawable.icon_sate_04));
		items.add(new SatelliteMenuItem(3, R.drawable.icon_sate_03));
		items.add(new SatelliteMenuItem(2, R.drawable.icon_sate_02));
		items.add(new SatelliteMenuItem(1, R.drawable.icon_sate_01));
		menu.addItems(items);

		menu.setOnItemClickedListener(new SateliteClickedListener() {

			public void eventOccured(int id) {

				if (id == 1) {
					// 添加 场
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							AddSceneActivity.class);
					intent.putExtra("film_id", film_id);
					startActivity(intent);

				} else if (id == 2) {

					SceneImpl sceneImpl = new SceneImpl(getApplicationContext());

					// 判断是否存在有镜头
					if (sceneImpl.isExist(film_id)) {

						Dialog dialog = new AddShotDialog(
								StoryBoardActivity.this, R.style.dialog,
								PosRecorder.getScene_pos_id(), Integer
										.parseInt(film_id),
								shotAndTakeMenuFragment);
						dialog.show();
					} else {
						AlertDialog ad = new AlertDialog(
								StoryBoardActivity.this, R.style.dialog,
								"请添加 场");

						ad.show();
					}

				} else if (id == 3) {

					// 添加景别
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							ShotsActivity.class);
					startActivity(intent);

				} else if (id == 4) {
					// 打开添加一个相机槽
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), RollActivity.class);
					startActivity(intent);
				} else if (id == 5) {
					// 这个功能吊啊，他可以获取当前位置及刷新所有场次位置的对比信息

					// 获取当前的地理位置信息
					getLocation();

				} else if (id == 6) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							StaffActivity.class);
					intent.putExtra("film_id", film_id);
					startActivity(intent);
				}
			}

		});
	}

	static Boolean sceneview_open = false;
	static Boolean shotAndtakeview_open = false;

	public void showFragmentMenu(String value) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.short_menu_pop_in,
				R.anim.short_menu_pop_out);

		if (value.equals("1")) {

			if (sceneview_open) {
				fragmentTransaction.remove(sceneMenuFragment);
				sceneview_open = false;
				shotAndtakeview_open = false;
			} else {
				fragmentTransaction.replace(R.id.liner, sceneMenuFragment);
				sceneview_open = true;
				shotAndtakeview_open = false;

			}

		} else if (value.equals("2")) {
			if (shotAndtakeview_open) {
				fragmentTransaction.remove(shotAndTakeMenuFragment);
				shotAndtakeview_open = false;
				sceneview_open = false;

			} else {
				fragmentTransaction
						.replace(R.id.liner, shotAndTakeMenuFragment);
				shotAndtakeview_open = true;
				sceneview_open = false;

			}
		}
		fragmentTransaction.commit();

	}

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
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
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
			// 由于没有办法在别的地方调用到经纬度的值，所以只能在这里执行这个方法
			// 这个捕获异常的作用，你可以去掉再试试效果。避免刚进入界面时，点击进入其他界面再返回时报错
			PosRecorder.setCurrent_pos(address);
			tv_view_scene_current_pos.setText(address);
			try {
				sceneMenuFragment.LoadDistance(getApplicationContext(),
						latitude, longitude);

			} catch (Exception e) {

				// 这个的意思就是用户还没打开视图初始化操作，此时只要修改数据库的地点就好
				SceneImpl sceneImpl = new SceneImpl(getApplicationContext());
				List<Scene> list = new ArrayList<Scene>();
				list = sceneImpl.queryPos(film_id);
				DisRecorder dr = new DisRecorder(getApplicationContext(), list);
				dr.save(latitude, longitude);

			}
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		// 记得销毁的操作

		PosRecorder.setScene_pos_id(0);

	}

	protected void onRestart() {
		super.onRestart();
		// 回到这里时，刷新一下scene的adapter

		// 这个捕获异常的作用，你可以去掉再试试效果。避免刚进入界面时，点击进入其他界面再返回时报错
		try {
			if (sceneMenuFragment != null) {
				sceneMenuFragment.reload();

			}

			if (shotAndTakeMenuFragment != null
					&& PosRecorder.getScene_pos_id() != 0) {
				shotAndTakeMenuFragment.reloadButNoReset();

			}

		} catch (Exception e) {

			LoadSceneView();
			LoadShotAndTakeView();

		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

}
