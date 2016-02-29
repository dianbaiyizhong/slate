package hhm.slate.activity.storyboard.function;

import hhm.slate.R;
import hhm.slate.db.entity.Staff;
import hhm.slate.db.impl.StaffImpl;
import hhm.slate.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class StaffActivity extends Activity {

	static StringUtil stringUtil = new StringUtil();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_staff);

		Bundle bundle = this.getIntent().getExtras();
		String film_id = bundle.getString("film_id");

		StaffImpl staffImpl = new StaffImpl(StaffActivity.this);
		Staff staff = staffImpl.queryById(film_id);
		ListView list = (ListView) findViewById(R.id.LV_staff_info);

		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("staff_call", "导演");
		map1.put("staff_name", stringUtil.getName(staff.getDirector()));
		map1.put("staff_phonenumber",
				stringUtil.getPhoneNum(staff.getDirector()));

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("staff_call", "副导演");
		map2.put("staff_name",
				stringUtil.getName(staff.getAssistant_director()));
		map2.put("staff_phonenumber",
				stringUtil.getPhoneNum(staff.getAssistant_director()));
		
		listItem.add(map1);
		listItem.add(map2);

		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,
				R.layout.listview_staff_info, new String[] { "staff_call",
						"staff_name", "staff_phonenumber" }, new int[] {
						R.id.tv_staff_call, R.id.tv_staff_name,
						R.id.tv_staff_phonenumber });

		list.setAdapter(listItemAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> lv, View view, int index,
					long arg3) {
				TextView phonenum = (TextView) view
						.findViewById(R.id.tv_staff_phonenumber);

				final String phone = phonenum.getText().toString();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						StaffActivity.this);
				builder.setTitle("操作");
				builder.setIcon(R.drawable.ic_launcher);
				builder.setItems(new String[] { "发短信", "打电话", "取消" },
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int index) {

								if (index == 1) {
									Intent phoneIntent = new Intent(
											"android.intent.action.CALL", Uri
													.parse("tel:" + phone));

									startActivity(phoneIntent);
								} else if (index == 0) {
									Intent intent = new Intent(
											Intent.ACTION_SENDTO, Uri
													.parse("smsto:" + phone));
									startActivity(intent);
								}

							}
						});

				builder.show();
			}

		});

	}

	public void finish(View view) {
		finish();
	}
}
