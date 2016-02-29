package hhm.slate.activity.storyboard.function;

import hhm.slate.R;
import hhm.slate.db.entity.Film;
import hhm.slate.db.entity.Roll;
import hhm.slate.db.entity.Shot;
import hhm.slate.db.entity.Shots;
import hhm.slate.db.entity.Take;
import hhm.slate.db.impl.FilmImpl;
import hhm.slate.db.impl.RollImpl;
import hhm.slate.db.impl.ShotsImpl;
import hhm.slate.db.impl.TakeImpl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class EditTakeActivity extends Activity {

	TextView tv_storyboard_shooted_info;
	TextView tv_storyboard_shooted_scene_number_value;
	TextView tv_storyboard_shooted_shot_number_value;
	TextView tv_storyboard_shooted_take_number_value;
	Spinner sp_storyboard_shooted_shots;
	Spinner sp_storyboard_shooted_roll;
	Spinner sp_storyboard_shooted_is_available;
	ImageView iv_storyboard_shooted_image;

	TextView tv_storyboard_shooted_nowtime;

	Button btn_sb_shooted_edit;

	EditText et_storyboard_shooted_video_number;
	EditText et_storyboard_shooted_audio_number;
	EditText et_storyboard_shooted_not_available_season;

	public void finish(View view) {
		finish();
	}

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edittake);

		Bundle bundle = this.getIntent().getExtras();
		int take_id = bundle.getInt("take_id");

		FilmImpl filmImpl = new FilmImpl(getApplicationContext());
		Film film = filmImpl.queryByTask_id(String.valueOf(take_id));

		LoadBtn(take_id);
		LoadData(film);
	}

	private void LoadBtn(final int take_id) {
		btn_sb_shooted_edit = (Button) findViewById(R.id.btn_sb_shooted_edit);

		btn_sb_shooted_edit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				// 这个地方不需要验证，都能空
				Shot shot = new Shot();
				String shots = sp_storyboard_shooted_shots.getSelectedItem()
						.toString();
				String roll_name = sp_storyboard_shooted_roll.getSelectedItem()
						.toString();
				String not_availble_season = et_storyboard_shooted_not_available_season
						.getText().toString();
				String is_available = sp_storyboard_shooted_is_available
						.getSelectedItem().toString();

				int i_is_available = -1;
				if (is_available.equals("可用")) {
					i_is_available = 1;
				} else if (is_available.equals("保")) {
					i_is_available = 0;
				}

				String video_number = et_storyboard_shooted_video_number
						.getText().toString();
				String audio_number = et_storyboard_shooted_audio_number
						.getText().toString();

				Take take = new Take();
				take.setAudio_number(audio_number);
				take.setIs_available(i_is_available);
				take.setNot_avaliable_season(not_availble_season);
				take.setRoll_name(roll_name);
				take.setTake_id(take_id);
				take.setVideo_number(video_number);

				TakeImpl takeImpl = new TakeImpl(getApplicationContext());
				takeImpl.update(take);

				finish();

			}
		});
	}

	private void LoadData(Film film) {

		tv_storyboard_shooted_info = (TextView) findViewById(R.id.tv_storyboard_shooted_info);
		tv_storyboard_shooted_scene_number_value = (TextView) findViewById(R.id.tv_storyboard_shooted_scene_number_value);
		tv_storyboard_shooted_shot_number_value = (TextView) findViewById(R.id.tv_storyboard_shooted_shot_number_value);

		tv_storyboard_shooted_take_number_value = (TextView) findViewById(R.id.tv_storyboard_shooted_take_number_value);

		iv_storyboard_shooted_image = (ImageView) findViewById(R.id.iv_storyboard_shooted_image);

		tv_storyboard_shooted_nowtime = (TextView) findViewById(R.id.tv_storyboard_shooted_nowtime);

		et_storyboard_shooted_video_number = (EditText) findViewById(R.id.et_storyboard_shooted_video_number);
		et_storyboard_shooted_audio_number = (EditText) findViewById(R.id.et_storyboard_shooted_audio_number);
		et_storyboard_shooted_not_available_season = (EditText) findViewById(R.id.et_storyboard_shooted_not_available_season);
		tv_storyboard_shooted_nowtime.setText(film.getScene().getScene_name()
				+ " " + film.getTake().getTake_time());
		tv_storyboard_shooted_info.setText(film.getScene().getScene_name()
				+ " " + film.getShot().getShot_name() + "");
		tv_storyboard_shooted_scene_number_value.setText(film.getScene()
				.getScene_number() + "");
		tv_storyboard_shooted_shot_number_value.setText(film.getShot()
				.getShot_number() + "");
		tv_storyboard_shooted_take_number_value.setText(film.getTake()
				.getTake_number() + "");

		et_storyboard_shooted_video_number.setText(film.getTake()
				.getVideo_number() + "");
		et_storyboard_shooted_audio_number.setText(film.getTake()
				.getAudio_number() + "");
		et_storyboard_shooted_not_available_season.setText(film.getTake()
				.getNot_avaliable_season() + "");

		LoadShotsSpinner(film.getShot().getShots());

		LoadRollSpinner(film.getTake().getRoll_name());

		LoadIsAvalibleSpinner(film.getTake().getIs_available().toString());

	}

	private ArrayAdapter is_available_adapter;

	private void LoadIsAvalibleSpinner(String value) {
		sp_storyboard_shooted_is_available = (Spinner) findViewById(R.id.sp_storyboard_shooted_is_available);

		String arr[] = { "-1", "0", "1" };

		for (int i = 0; i < arr.length; i++) {

			if (value.equals(arr[i])) {
				String temp = arr[i];
				arr[i] = arr[0];
				arr[0] = temp;
			}

		}

		String arr1[] = new String[3];
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals("-1")) {
				arr1[i] = "不可用";
			} else if (arr[i].equals("0")) {
				arr1[i] = "保";

			} else if (arr[i].equals("1")) {
				arr1[i] = "可用";

			}

		}

		ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
				R.layout.spinner_slate, arr1);
		// 设置下拉列表的风格
		adapter.setDropDownViewResource(R.layout.spinner_slate_selector);
		sp_storyboard_shooted_is_available.setAdapter(adapter);
	}

	private void LoadRollSpinner(String roll_name) {

		sp_storyboard_shooted_roll = (Spinner) findViewById(R.id.sp_storyboard_shooted_roll);

		RollImpl rollImpl = new RollImpl(getApplicationContext());
		List<Roll> list = rollImpl.query();
		String[] arr_roll = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr_roll[i] = list.get(i).getRoll_name();
			if (arr_roll[i].equals(roll_name)) {
				String temp = arr_roll[0];
				arr_roll[0] = arr_roll[i];
				arr_roll[i] = temp;
			}
		}
		ArrayAdapter roll_adapter = new ArrayAdapter(getApplicationContext(),
				R.layout.spinner_slate, arr_roll);
		// 设置下拉列表的风格
		roll_adapter.setDropDownViewResource(R.layout.spinner_slate_selector);
		sp_storyboard_shooted_roll.setAdapter(roll_adapter);

	}

	private void LoadShotsSpinner(String shots) {

		sp_storyboard_shooted_shots = (Spinner) findViewById(R.id.sp_storyboard_shooted_shots);

		ShotsImpl shotsImpl = new ShotsImpl(getApplicationContext());
		List<Shots> list = new ArrayList<Shots>();
		list = shotsImpl.query();
		String[] arr_shots = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr_shots[i] = list.get(i).getShots_name();
			if (arr_shots[i].equals(shots)) {
				String temp = arr_shots[0];
				arr_shots[0] = arr_shots[i];
				arr_shots[i] = temp;
			}

		}

		ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
				R.layout.spinner_slate, arr_shots);

		// 设置下拉列表的风格
		adapter.setDropDownViewResource(R.layout.spinner_slate_selector);
		sp_storyboard_shooted_shots.setAdapter(adapter);

	}

}
