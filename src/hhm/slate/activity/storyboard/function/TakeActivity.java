package hhm.slate.activity.storyboard.function;

import hhm.slate.R;
import hhm.slate.activity.storyboard.dialog.AddTakeDialog;
import hhm.slate.db.entity.Roll;
import hhm.slate.db.entity.Shot;
import hhm.slate.db.entity.Shots;
import hhm.slate.db.entity.Take;
import hhm.slate.db.impl.RollImpl;
import hhm.slate.db.impl.ShotImpl;
import hhm.slate.db.impl.ShotsImpl;
import hhm.slate.util.PathUtil;
import hhm.slate.util.TimeUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class TakeActivity extends Activity {
	TextView tv_storyboard_shooting_info;
	TextView tv_storyboard_shooting_scene_number_value;
	TextView tv_storyboard_shooting_shot_number_value;
	TextView tv_storyboard_shooting_take_number_value;
	static TimeUtil tu = new TimeUtil();

	TextView tv_storyboard_shooting_nowtime;
	Spinner sp_storyboard_shooting_shots;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 200;
	ImageView iv_storyboard_shooting_image;
	Spinner sp_storyboard_shooting_roll;
	public static int take_number;
	Button btn_sb_showAddTake_Dialog;
	ImageView iv_slate_head;
	Button btn_ac_shotting_action;

	private int shot_id;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_addtake);

		Bundle bundle = this.getIntent().getExtras();
		this.shot_id = bundle.getInt("shot_id");

		// 加载控件
		tv_storyboard_shooting_info = (TextView) findViewById(R.id.tv_storyboard_shooting_info);
		tv_storyboard_shooting_scene_number_value = (TextView) findViewById(R.id.tv_storyboard_shooting_scene_number_value);
		tv_storyboard_shooting_shot_number_value = (TextView) findViewById(R.id.tv_storyboard_shooting_shot_number_value);
		tv_storyboard_shooting_take_number_value = (TextView) findViewById(R.id.tv_storyboard_shooting_take_number_value);
		tv_storyboard_shooting_nowtime = (TextView) findViewById(R.id.tv_storyboard_shooting_nowtime);
		iv_storyboard_shooting_image = (ImageView) findViewById(R.id.iv_storyboard_shooting_image);
		timerTask(); // 定时执行

		// 根据shot_id，获取数据 分别为 场 镜 次
		ShotImpl shotImpl = new ShotImpl(getApplicationContext());
		Shot shot = shotImpl.queryRecentTakeById(shot_id);

		if (shot.getShot_number() == null || shot.getShot_number() == 0) {
			// 如果这个为空，就证明，在没有任何次的情况下，是最新拍的
			// 这里要做处理的，别忘了
			shot = shotImpl.querySceneById(shot_id);
			take_number = 1;
		} else {
			take_number = shot.getTake().getTake_number();

		}

		// 为控件赋值
		tv_storyboard_shooting_info.setText(shot.getScene().getScene_name()
				+ " " + shot.getShot_name() + "");
		tv_storyboard_shooting_scene_number_value.setText(shot.getScene()
				.getScene_number() + "");
		tv_storyboard_shooting_shot_number_value.setText(shot.getShot_number()
				+ "");
		tv_storyboard_shooting_take_number_value.setText(take_number + "");

		// 加载spiner
		LoadShotsSpinner();

		// 加载拍照按钮
		LoadOpenCameraButton();

		LoadRollSpinner();

		// 加载 提交按钮
		LoadAddTakeBtn();

		// 加载拍板动画按钮
		LoadActionBtn();

	}

	private ArrayAdapter roll_adapter;

	private void LoadRollSpinner() {

		sp_storyboard_shooting_roll = (Spinner) findViewById(R.id.sp_storyboard_shooting_roll);

		RollImpl rollImpl = new RollImpl(getApplicationContext());
		List<Roll> list = rollImpl.query();
		String[] arr_roll = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr_roll[i] = list.get(i).getRoll_name();
		}
		ArrayAdapter roll_adapter = new ArrayAdapter(getApplicationContext(),
				R.layout.spinner_slate, arr_roll);
		// 设置下拉列表的风格
		roll_adapter.setDropDownViewResource(R.layout.spinner_slate_selector);
		sp_storyboard_shooting_roll.setAdapter(roll_adapter);

	}

	/**
	 * 加载景别spinner
	 */
	private ArrayAdapter shots_adapter;

	private void LoadShotsSpinner() {
		sp_storyboard_shooting_shots = (Spinner) findViewById(R.id.sp_storyboard_shooting_shots);

		ShotsImpl shotsImpl = new ShotsImpl(getApplicationContext());
		List<Shots> list = new ArrayList<Shots>();
		list = shotsImpl.query();
		list.add(0, new Shots("不填"));
		String[] arr_shots = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr_shots[i] = list.get(i).getShots_name();
		}

		shots_adapter = new ArrayAdapter(getApplicationContext(),
				R.layout.spinner_slate, arr_shots);

		// 设置下拉列表的风格
		shots_adapter.setDropDownViewResource(R.layout.spinner_slate_selector);
		sp_storyboard_shooting_shots.setAdapter(shots_adapter);

	}

	/**
	 * 加载打开相机的按钮
	 */
	private void LoadOpenCameraButton() {

		// 点击图片拍照
		iv_storyboard_shooting_image
				.setOnClickListener(new View.OnClickListener() {

					public void onClick(View arg0) {
						Opencamera();

					}
				});

	}

	private void LoadActionBtn() {
		btn_ac_shotting_action = (Button) findViewById(R.id.btn_ac_shotting_action);
		iv_slate_head = (ImageView) findViewById(R.id.iv_slate_head);
		final RotateAnimation animation = new RotateAnimation(0f, -30f,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1f);
		animation.setDuration(500);// 设置动画持续时间
		animation.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation arg0) {

			}

			public void onAnimationRepeat(Animation arg0) {

			}

			public void onAnimationEnd(Animation arg0) {
				// 在这里输出声音
				MediaPlayer mp = new MediaPlayer();
				mp = MediaPlayer.create(TakeActivity.this, R.raw.m);
				mp.start();
			}
		});
		btn_ac_shotting_action.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				iv_slate_head.startAnimation(animation);

			}

		});
	}

	private void LoadAddTakeBtn() {
		btn_sb_showAddTake_Dialog = (Button) findViewById(R.id.btn_sb_showAddTake_Dialog);
		btn_sb_showAddTake_Dialog
				.setOnClickListener(new View.OnClickListener() {

					public void onClick(View arg0) {

						// 弹出框
						Take take = new Take();
						take.setShot_id(shot_id);
						String filename = "default.jpg";

						if (imageName != null) {
							filename = imageName;
						}
						take.setTake_image(filename);
						take.setTake_number(take_number);
						String roll_name = sp_storyboard_shooting_roll
								.getSelectedItem().toString();
						take.setRoll_name(roll_name);

						// 使用系统自带的弹出框

						Dialog dialog = new AddTakeDialog(TakeActivity.this,
								R.style.dialog_addtake, take, TakeActivity.this);

						dialog.show();

					}
				});

	}

	/**
	 * 打开摄像机
	 */
	Uri imageFileUri;

	String imageFilePath;
	String imageName;

	public void Opencamera() {

		// 先为照片起好名字
		imageName = new TimeUtil().getLocalTime() + ".jpg";
		String imageFilePath = new PathUtil().getStagePhotoPath() + imageName;

		File imageFile = new File(imageFilePath);
		imageFileUri = Uri.fromFile(imageFile);

		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
		startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	// 重写相机结束的响应事件

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {

			return;
		}
		switch (requestCode) {
		case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			Intent intent = new Intent("com.android.camera.action.CROP"); // 剪裁
			intent.setDataAndType(imageFileUri, "image/");
			intent.putExtra("scale", true);
			// 设置宽高比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// 设置裁剪图片宽高
			intent.putExtra("outputX", 340);
			intent.putExtra("outputY", 340);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);

			// 广播刷新相册
			Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			intentBc.setData(imageFileUri);
			this.sendBroadcast(intentBc);
			startActivityForResult(intent, CROP_IMAGE_ACTIVITY_REQUEST_CODE); // 设置裁剪参数显示图片至ImageView
			break;
		case CROP_IMAGE_ACTIVITY_REQUEST_CODE:
			try {
				iv_storyboard_shooting_image.setVisibility(View.VISIBLE);

				// 图片解析成Bitmap对象
				Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(imageFileUri));

				iv_storyboard_shooting_image.setImageBitmap(bitmap); // 将剪裁后照片显示出来

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}

	}

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				tv_storyboard_shooting_nowtime.setText(tu.getTime());
				break;
			case 2:
				mTimer.cancel();//
				mTimer = null;
			}
			super.handleMessage(msg);
		}
	};

	public void timerTask() {
		// 创建定时线程执行更新任务
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {

				mHandler.sendEmptyMessage(1);// 向Handler发送消息

			}
		}, 0, 1000);// 定时任务
	}

	public Timer mTimer = new Timer();// 定时器

	@Override
	protected void onStop() {
		mTimer.cancel();// 程序退出时cancel timer
		super.onStop();
	}

}
