package hhm.slate.activity.storyboard.function;

import hhm.slate.R;
import hhm.slate.db.entity.Shots;
import hhm.slate.db.impl.ShotsImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ShotsActivity extends Activity {
	Button btn_shots_add;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_shots);

		InitData();

	}

	private void InitData() {
		btn_shots_add = (Button) findViewById(R.id.btn_shots_add);

		btn_shots_add.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), AddShotsActivity.class);
				startActivity(intent);

			}
		});

		ShotsImpl shotsImpl = new ShotsImpl(getApplicationContext());
		List<Shots> shotslist = shotsImpl.query();

		final ListView list = (ListView) findViewById(R.id.LV_shots_info);

		final ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

		if (shotslist.size() != 0) {

			for (int i = 0; i < shotslist.size(); i++) {

				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("shots_name", shotslist.get(i).getShots_name());
				map.put("shots_id", shotslist.get(i).getShots_id());

				listItem.add(map);

			}
		}

		final SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,

		R.layout.listview_shots_info,
				new String[] { "shots_name", "shots_id" }, new int[] {
						R.id.tv_shots_name, R.id.tv_shots_id });

		list.setAdapter(listItemAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> lv, View view, int index,
					long arg3) {

			}

		});

		// 长按

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {

				TextView tv_shots_id = (TextView) view
						.findViewById(R.id.tv_shots_id);
				final int shots_id = Integer.parseInt(tv_shots_id.getText()
						.toString());
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ShotsActivity.this);
				builder.setTitle("删除");
				builder.setIcon(R.drawable.ic_launcher);

				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								listItem.remove(position);
								listItemAdapter.notifyDataSetChanged();

								ShotsImpl shotsImpl = new ShotsImpl(
										getApplicationContext());

								// 记得，要从位置中找到id。两者不一定对应

								shotsImpl.delete(shots_id);

							}
						});
				builder.setNegativeButton("取消", null);

				builder.show();

				return false;
			}

		});

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		InitData();
	}

	public void finish(View view) {
		finish();
	}

}
