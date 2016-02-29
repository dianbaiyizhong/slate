package hhm.slate.activity.storyboard.function;

import hhm.slate.R;
import hhm.slate.db.entity.Roll;
import hhm.slate.db.impl.RollImpl;
import hhm.slate.util.VerifyUtil;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class AddRollActivity extends Activity {

	EditText et_add_roll_roll_name;
	Button btn_add_roll_add;

	public void finish(View view) {
		finish();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addroll);

		et_add_roll_roll_name = (EditText) findViewById(R.id.et_add_roll_roll_name);
		btn_add_roll_add = (Button) findViewById(R.id.btn_add_roll_add);
		btn_add_roll_add.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// 验证
				if (!verify()) {
					return;
				}

				String roll_name = et_add_roll_roll_name.getText().toString();

				RollImpl rollImpl = new RollImpl(getApplicationContext());
				Roll roll = new Roll(roll_name);
				rollImpl.save(roll);

				finish();
			}
		});
	}

	private boolean verify() {

		List<EditText> list = new ArrayList<EditText>();
		list.add(et_add_roll_roll_name);

		boolean bool = new VerifyUtil().verifyIsNull(getApplicationContext(),
				list);

		if (bool) {
			return true;

		} else {
			return false;
		}
	}

}
