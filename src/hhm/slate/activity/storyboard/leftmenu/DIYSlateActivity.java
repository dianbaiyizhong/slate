package hhm.slate.activity.storyboard.leftmenu;

import hhm.slate.R;
import hhm.slate.util.TimeUtil;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DIYSlateActivity extends Activity {

	TextView tv_diy_nowtime;
	Button btn_diy_action;
	ImageView iv_diy_slate_head;
	static TimeUtil tu = new TimeUtil();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diy_slate);

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
				mp = MediaPlayer.create(DIYSlateActivity.this, R.raw.m);
				mp.start();
			}
		});
		tv_diy_nowtime = (TextView) findViewById(R.id.tv_diy_nowtime);
		btn_diy_action = (Button) findViewById(R.id.btn_diy_action);
		iv_diy_slate_head = (ImageView) findViewById(R.id.iv_diy_slate_head);
		btn_diy_action.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				iv_diy_slate_head.startAnimation(animation);

			}
		});
		timerTask(); // 定时执行
	}

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				tv_diy_nowtime.setText(tu.getTime());
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

	public void finish(View view) {
		finish();
	}

}
