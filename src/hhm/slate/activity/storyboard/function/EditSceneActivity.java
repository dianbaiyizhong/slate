package hhm.slate.activity.storyboard.function;

import hhm.slate.R;
import hhm.slate.db.entity.Scene;
import hhm.slate.db.impl.SceneImpl;
import hhm.slate.util.VerifyUtil;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EditSceneActivity extends Activity {

	EditText et_edit_scene_scene_name;
	EditText et_edit_scene_scene_pos;
	Button btn_edit_scene_edit;

	EditText et_edit_scene_scene_number;
	ImageView iv_edit_scene_mapsearch;

	private int scene_id;
	public void finish(View view) {
		finish();
	}
	// 记录好film_id,可以返回
	private String film_id;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_editscene);
	

		et_edit_scene_scene_name = (EditText) findViewById(R.id.et_edit_scene_scene_name);
		et_edit_scene_scene_pos = (EditText) findViewById(R.id.et_edit_scene_scene_pos);
		et_edit_scene_scene_number = (EditText) findViewById(R.id.et_edit_scene_scene_number);
		iv_edit_scene_mapsearch = (ImageView) findViewById(R.id.iv_edit_scene_mapsearch);
		btn_edit_scene_edit = (Button) findViewById(R.id.btn_edit_scene_edit);

		Bundle bundle = this.getIntent().getExtras();

		// 获取secne_id
		this.scene_id = bundle.getInt("scene_id");

		InitValue();

		btn_edit_scene_edit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// 验证
				if (!verify()) {
					return;
				}

				SceneImpl sceneImpl = new SceneImpl(getApplicationContext());
				Scene scene = new Scene();
				scene.setScene_id(scene_id);
				scene.setScene_name(et_edit_scene_scene_name.getText()
						.toString());
				scene.setScene_number(Integer
						.parseInt(et_edit_scene_scene_number.getText()
								.toString()));

				if (!scene_pos[0].equals("")) {
					scene.setScene_pos(scene_pos[0] + "#" + scene_pos[1] + "#"
							+ scene_pos[2]);
				}

				sceneImpl.update(scene);

				finish();

			}
		});

		iv_edit_scene_mapsearch.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("returnValue", "edit");
				intent.setClass(getApplicationContext(),
						MapSearchActivity.class);
				startActivityForResult(intent, 0);

			}
		});

	}

	private void InitValue() {
		SceneImpl sceneImpl = new SceneImpl(getApplicationContext());

		Scene scene = new Scene();
		scene = sceneImpl.queryById(scene_id);
		et_edit_scene_scene_name.setText(scene.getScene_name());
		String pos = scene.getScene_pos();
		String[] scene_pos1 = new String[3];
		try {
			scene_pos1 = pos.split("#");

		} catch (Exception e) {
			scene_pos1[0] = "";
			scene_pos1[1] = "";
			scene_pos1[2] = "";

		}
		et_edit_scene_scene_pos.setText(scene_pos1[0]);

		scene_pos = new String[3];
		scene_pos[0] = scene_pos1[0];
		scene_pos[1] = scene_pos1[1];
		scene_pos[2] = scene_pos1[2];
		et_edit_scene_scene_number.setText(String.valueOf(scene
				.getScene_number()));
		this.film_id = String.valueOf(scene.getFilm_id());
	}

	String scene_pos[];

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			scene_pos = data.getExtras().getStringArray("scene_pos");

			et_edit_scene_scene_pos.setText(scene_pos[0]);

			break;
		default:
			break;
		}

	}

	private boolean verify() {

		List<EditText> list = new ArrayList<EditText>();
		list.add(et_edit_scene_scene_name);
		list.add(et_edit_scene_scene_number);

		boolean bool = new VerifyUtil().verifyIsNull(getApplicationContext(),
				list);

		if (bool) {
			return true;

		} else {
			return false;
		}

	}
}
