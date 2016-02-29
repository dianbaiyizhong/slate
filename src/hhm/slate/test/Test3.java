package hhm.slate.test;

import hhm.slate.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class Test3 extends Activity {
	ImageView iv_slate_head;
	Button btn_ac_shotting_action;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addtake);

		btn_ac_shotting_action = (Button) findViewById(R.id.btn_ac_shotting_action);
		iv_slate_head = (ImageView) findViewById(R.id.iv_slate_head);

		/** 设置旋转动画 */

		/** 常用方法 */
		final RotateAnimation animation = new RotateAnimation(0f, -30f,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1f);
		animation.setDuration(1000);// 设置动画持续时间

		btn_ac_shotting_action.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				iv_slate_head.setAnimation(animation);

				/** 开始动画 */
				iv_slate_head.startAnimation(animation);
			}

		});

	}

}
