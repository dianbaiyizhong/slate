package hhm.slate.activity.storyboard.function;

import hhm.slate.R;
import hhm.slate.db.entity.Scene;
import hhm.slate.db.impl.FilmImpl;
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

public class AddSceneActivity extends Activity {
	EditText et_add_scene_scene_name;
	EditText et_add_scene_scene_number;
	EditText et_add_scene_scene_pos;
	Button btn_add_scene;
	private String film_id;
	ImageView iv_add_scene_mapsearch;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addscene);

		btn_add_scene = (Button) findViewById(R.id.btn_add_scene);
		iv_add_scene_mapsearch = (ImageView) findViewById(R.id.iv_add_scene_mapsearch);
		et_add_scene_scene_pos = (EditText) findViewById(R.id.et_add_scene_scene_pos);
		et_add_scene_scene_name = (EditText) findViewById(R.id.et_add_scene_scene_name);
		et_add_scene_scene_number = (EditText) findViewById(R.id.et_add_scene_scene_number);
		Bundle bundle = this.getIntent().getExtras();

		String film_id = bundle.getString("film_id");
		this.film_id = film_id;

		// 通过film_id获取最新的一个场景号
		FilmImpl filmImpl = new FilmImpl(getApplicationContext());

		int scene_number = filmImpl.queryRecentSceneById(film_id);
		// 为其赋值
		et_add_scene_scene_number.setText(String.valueOf(scene_number));
		btn_add_scene.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				Add();

			}

		});

		iv_add_scene_mapsearch.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				Intent intent = new Intent(getApplicationContext(),
						MapSearchActivity.class);

				intent.putExtra("returnValue", "add");
				startActivityForResult(intent, 0);

			}

		});

	}

	private void Add() {

		// 验证
		if (!verify()) {
			return;
		}

		String scene_name = et_add_scene_scene_name.getText().toString();
		String scene_number = et_add_scene_scene_number.getText().toString();
		Scene scene = null;
		if (scene_pos != null) {
			scene = new Scene(Integer.parseInt(scene_number),
					Integer.parseInt(film_id), scene_name, scene_pos[0] + "#"
							+ scene_pos[1] + "#" + scene_pos[2]);
		} else {
			scene = new Scene(Integer.parseInt(scene_number),
					Integer.parseInt(film_id), scene_name);
		}

		SceneImpl sceneImpl = new SceneImpl(getApplicationContext());
		sceneImpl.save(scene);

		// 跳到指示页面

		this.finish();

	}

	private boolean verify() {

		List<EditText> list = new ArrayList<EditText>();
		list.add(et_add_scene_scene_number);
		list.add(et_add_scene_scene_name);

		boolean bool = new VerifyUtil().verifyIsNull(getApplicationContext(),
				list);

		if (bool) {
			return true;

		} else {
			return false;
		}
	}

	String scene_pos[];

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			scene_pos = data.getExtras().getStringArray("scene_pos");

			et_add_scene_scene_pos.setText(scene_pos[0]);
			break;
		default:
			break;
		}

	}

	public void finish(View view) {
		finish();
	}

}
