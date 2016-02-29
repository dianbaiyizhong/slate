package hhm.slate.activity.storyboard.function;

import hhm.slate.R;
import hhm.slate.db.entity.Roll;
import hhm.slate.db.impl.RollImpl;

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

public class RollActivity extends Activity {

	Button btn_roll_add;

	public void finish(View view) {
		finish();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_roll);

		InitData();

	}

	private void InitData() {
		btn_roll_add = (Button) findViewById(R.id.btn_roll_add);

		btn_roll_add.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), AddRollActivity.class);
				startActivity(intent);

			}
		});

		RollImpl rollImpl = new RollImpl(getApplicationContext());
		List<Roll> rolllist = rollImpl.query();

		final ListView list = (ListView) findViewById(R.id.LV_roll_info);

		final ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

		if (rolllist.size() != 0) {

			for (int i = 0; i < rolllist.size(); i++) {

				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("roll_name", rolllist.get(i).getRoll_name());
				map.put("roll_id", rolllist.get(i).getRoll_id());

				listItem.add(map);

			}
		}

		final SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,

		R.layout.listview_roll_info, new String[] { "roll_name", "roll_id" },
				new int[] { R.id.tv_roll_name, R.id.tv_roll_id });

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

				TextView tv_roll_id = (TextView) view
						.findViewById(R.id.tv_roll_id);
				final int roll_id = Integer.parseInt(tv_roll_id.getText()
						.toString());
				AlertDialog.Builder builder = new AlertDialog.Builder(
						RollActivity.this);
				builder.setTitle("删除");
				builder.setIcon(R.drawable.ic_launcher);

				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								listItem.remove(position);
								listItemAdapter.notifyDataSetChanged();

								RollImpl rollImpl = new RollImpl(
										getApplicationContext());

								// 记得，要从位置中找到id。两者不一定对应

								rollImpl.delete(roll_id);

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
}
