package hhm.slate.activity.storyboard.function;

import hhm.slate.R;
import hhm.slate.db.entity.Shots;
import hhm.slate.db.impl.ShotsImpl;
import hhm.slate.util.VerifyUtil;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class AddShotsActivity extends Activity {
	EditText et_add_shots_shots_name;
	Button btn_add_shots_add;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_addshots);

		et_add_shots_shots_name = (EditText) findViewById(R.id.et_add_shots_shots_name);
		btn_add_shots_add = (Button) findViewById(R.id.btn_add_shots_add);
		btn_add_shots_add.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				// 验证
				if (!verify()) {
					return;
				}
				String shots_name = et_add_shots_shots_name.getText()
						.toString();

				ShotsImpl shotsImpl = new ShotsImpl(getApplicationContext());
				Shots shots = new Shots(shots_name);
				shotsImpl.save(shots);

				finish();

			}
		});
	}

	private boolean verify() {

		List<EditText> list = new ArrayList<EditText>();
		list.add(et_add_shots_shots_name);

		boolean bool = new VerifyUtil().verifyIsNull(getApplicationContext(),
				list);

		if (bool) {
			return true;

		} else {
			return false;
		}

	}

	public void finish(View view) {
		finish();
	}

}
