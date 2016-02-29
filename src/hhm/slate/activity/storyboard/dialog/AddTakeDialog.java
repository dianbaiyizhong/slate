package hhm.slate.activity.storyboard.dialog;

import hhm.slate.R;
import hhm.slate.db.entity.Take;
import hhm.slate.db.impl.TakeImpl;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AddTakeDialog extends Dialog {
	EditText et_storyboard_shooting_is_avalible_season;
	EditText et_storyboard_shooting_video_number;
	EditText et_storyboard_shooting_audio_number;

	Button btn_sb_addTake;
	RadioGroup rg_add_take_is_avai;
	RadioButton rb_add_take_is_avai;
	private Take take;

	private Activity activity;

	private String is_avalible;

	public AddTakeDialog(Context context, int theme, Take take,
			Activity activity) {
		super(context, theme);

		this.take = take;

		this.activity = activity;
	}

	private void LoadIsAvailableSeasonET() {
		et_storyboard_shooting_is_avalible_season = (EditText) findViewById(R.id.et_storyboard_shooting_is_avalible_season);
		et_storyboard_shooting_video_number = (EditText) findViewById(R.id.et_storyboard_shooting_video_number);
		et_storyboard_shooting_audio_number = (EditText) findViewById(R.id.et_storyboard_shooting_audio_number);

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_add_take);
		// 加载spiner
		LoadIsAvailableRadioButton();
		// 加载 舍弃原因
		LoadIsAvailableSeasonET();

		// 加载确定控件
		LoadAddTakeBtn();

		setWindow();
	}

	private void setWindow() {
		Window dialogWindow = getWindow();

		// 对话框弹出后，背景变暗为0.9

		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		lp.width = (int) (d.widthPixels * 0.8);
		lp.height = (int) (d.heightPixels * 0.7);
		lp.dimAmount = 0.8f;
		dialogWindow.setAttributes(lp);
		dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

	}

	private void LoadAddTakeBtn() {
		btn_sb_addTake = (Button) findViewById(R.id.btn_sb_addTake);
		btn_sb_addTake.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				String is_available = is_avalible;
				String not_avaliable_season = et_storyboard_shooting_is_avalible_season
						.getText().toString();

				int i_is_available = -1;
				if (is_available.equals("可用")) {
					i_is_available = 1;
				} else if (is_available.equals("保")) {
					i_is_available = 0;
				}
				take.setIs_available(i_is_available);
				take.setNot_avaliable_season(not_avaliable_season);

				take.setAudio_number(et_storyboard_shooting_audio_number
						.getText().toString() + "");
				take.setVideo_number(et_storyboard_shooting_video_number
						.getText().toString() + "");

				TakeImpl takeImpl = new TakeImpl(getContext());
				takeImpl.save(take);

				// 提交结束后，重新刷新数据

				activity.finish();
			}
		});
	}

	private void LoadIsAvailableRadioButton() {

		rg_add_take_is_avai = (RadioGroup) findViewById(R.id.rg_add_take_is_avai);
		rg_add_take_is_avai.check(R.id.rb_add_take);
		is_avalible = "可用";
		/* 设置事件监听 */
		rg_add_take_is_avai
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						rb_add_take_is_avai = (RadioButton) findViewById(checkedId);
						is_avalible = rb_add_take_is_avai.getText().toString();
					}
				});

	}
}
