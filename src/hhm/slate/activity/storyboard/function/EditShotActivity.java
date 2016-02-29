package hhm.slate.activity.storyboard.function;

import hhm.slate.R;
import hhm.slate.activity.storyboard.ActivityBase;
import hhm.slate.db.entity.Shot;
import hhm.slate.db.entity.Shots;
import hhm.slate.db.impl.ShotImpl;
import hhm.slate.db.impl.ShotsImpl;
import hhm.slate.util.VerifyUtil;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EditShotActivity extends Activity implements ActivityBase {
	Spinner sp_edit_shot_shots;
	EditText et_edit_shot_shot_name;

	EditText et_edit_shot_shot_number;

	EditText et_edit_shot_shot_keyword;
	Button btn_edit_shot_edit;
	private int shot_id;

	public void finish(View view) {
		finish();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_editshot);

		Bundle bundle = this.getIntent().getExtras();

		// 获取secne_id
		this.shot_id = bundle.getInt("shot_id");
		LoadWidget();

		LoadWidgetValue();

	}

	@Override
	public void LoadWidget() {
		et_edit_shot_shot_name = (EditText) findViewById(R.id.et_edit_shot_shot_name);
		sp_edit_shot_shots = (Spinner) findViewById(R.id.sp_edit_shot_shots);
		et_edit_shot_shot_number = (EditText) findViewById(R.id.et_edit_shot_shot_number);
		et_edit_shot_shot_keyword = (EditText) findViewById(R.id.et_edit_shot_shot_keyword);
		btn_edit_shot_edit = (Button) findViewById(R.id.btn_edit_shot_edit);
		btn_edit_shot_edit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// 验证
				if (!verify()) {
					return;
				}
				String shot_name = et_edit_shot_shot_name.getText().toString();
				String shot_number = et_edit_shot_shot_number.getText()
						.toString();
				String keyword = et_edit_shot_shot_keyword.getText().toString();

				String shots = sp_edit_shot_shots.getSelectedItem().toString();

				Shot shot = new Shot(shot_id, shot_name, shots, Integer
						.parseInt(shot_number));
				shot.setShot_keyword(keyword);
				ShotImpl shotImpl = new ShotImpl(getApplicationContext());

				shotImpl.update(shot);

				finish();

			}
		});
	}

	@Override
	public void LoadWidgetValue() {
		ShotImpl shotImpl = new ShotImpl(getApplicationContext());
		Shot shot = new Shot();

		shot = shotImpl.queryById(shot_id);

		et_edit_shot_shot_name.setText(shot.getShot_name());

		et_edit_shot_shot_number.setText(String.valueOf(shot.getShot_number()));
		et_edit_shot_shot_keyword.setText(shot.getShot_keyword());
		LoadShotsSpinner(shot.getShots());
		this.shot_id = shot.getShot_id();

	}

	private void LoadShotsSpinner(String shots) {

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
				R.layout.simple_spinner_item, arr_shots);

		// 设置下拉列表的风格
		adapter.setDropDownViewResource(R.layout.spinner_slate_selector);
		sp_edit_shot_shots.setAdapter(adapter);

	}

	@Override
	public void LoadWidgetlistener() {

	}

	private boolean verify() {

		List<EditText> list = new ArrayList<EditText>();
		list.add(et_edit_shot_shot_name);
		list.add(et_edit_shot_shot_number);

		boolean bool = new VerifyUtil().verifyIsNull(getApplicationContext(),
				list);

		if (bool) {
			return true;

		} else {
			return false;
		}

	}
}
