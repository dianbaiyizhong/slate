package hhm.slate.activity.storyboard.leftmenu;

import hhm.slate.R;
import hhm.slate.activity.storyboard.ActivityBase;
import hhm.slate.db.ConfigImpl;
import hhm.slate.prefer.PreferencesService;
import hhm.slate.util.FileUtil;
import hhm.slate.util.PathUtil;
import hhm.slate.util.ShareFileThread;
import hhm.slate.util.VerifyUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class WorkVersionSelector extends Activity implements ActivityBase {
	private PreferencesService preferencesService;

	Button btn_work_version_save;

	TextView tv_ac_work_version_now;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_work_version);

		InitData();

	}

	private void InitData() {
		LoadWidget();
		LoadWidgetValue();
		LoadWidgetlistener();

		String path = new PathUtil().getUserDBPath();

		File file = new File(path);
		String arr_file[] = file.list();

		final ListView list = (ListView) findViewById(R.id.LV_work_version_info);

		final ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

		if (arr_file.length != 0) {

			for (int i = 0; i < arr_file.length; i++) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				String filename = arr_file[i];
				if (filename.contains(".")) {
					filename = filename.substring(0, filename.indexOf("."));
				}
				map.put("version_name", filename);

				listItem.add(map);

			}
		}

		final SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,

		R.layout.listview_work_version_info, new String[] { "version_name" },
				new int[] { R.id.tv_version_name });

		list.setAdapter(listItemAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> lv, View view, int index,
					long arg3) {
				TextView tv = (TextView) view
						.findViewById(R.id.tv_version_name);
				final String name = tv.getText().toString();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						WorkVersionSelector.this);
				builder.setTitle("操作");
				builder.setIcon(R.drawable.ic_launcher);
				builder.setItems(new String[] { "使用该工作台", "分享", "删除", "取消" },
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int index) {

								if (index == 0) {
									// 复制过去
									PathUtil pu = new PathUtil();
									String oldPath = pu.getUserDBPath() + name
											+ ".slate";
									String newPath = pu.getDB_PATH() + name
											+ ".slate";

									// 设置单例全局
									new ConfigImpl().setNowDBName(name);

									// 设置偏好
									preferencesService = new PreferencesService(
											getApplicationContext());
									preferencesService.saveNowDBName(name
											+ ".slate");

									finish();
								} else if (index == 1) {

									ShareFileThread st = new ShareFileThread(
											WorkVersionSelector.this, name, "");
									st.start();
								} else if (index == 2) {

									Delete(name);
								}
							}

						});

				builder.show();

			}

		});

	}

	private void Delete(String name) {
		if (name.equals("default")) {

			hhm.slate.activity.storyboard.dialog.AlertDialog ad = new hhm.slate.activity.storyboard.dialog.AlertDialog(
					WorkVersionSelector.this, R.style.dialog, "该版本为系统默认版本,不能删除");
			ad.show();
			return;
		}
		new FileUtil().delete(name + ".slate");
		InitData();
	}

	@Override
	public void LoadWidget() {
		btn_work_version_save = (Button) findViewById(R.id.btn_work_version_save);
		tv_ac_work_version_now = (TextView) findViewById(R.id.tv_ac_work_version_now);
	}

	@Override
	public void LoadWidgetValue() {
		String name = ConfigImpl.getNowDBName();
		tv_ac_work_version_now.setText("当前使用的工作台名称为:"
				+ name.substring(0, name.indexOf(".")));

	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void LoadWidgetlistener() {

		btn_work_version_save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						WorkVersionSelector.this);
				builder.setTitle("请输入一个备份的名字?");
				builder.setIcon(R.drawable.ic_launcher);
				final EditText et_name = new EditText(getApplicationContext());
				et_name.setTextColor(R.color.black);
				builder.setView(et_name);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {

								// 开始复制当前数据库，先取得路径
								String oldPath = new PathUtil().getDB_PATH()
										+ new ConfigImpl().getNowDBName();
								String name = et_name.getText().toString();

								// 备份两份
								FileUtil fu = new FileUtil();

								if (fu.synchronizeDB(oldPath, name + ".slate")
										|| new VerifyUtil()
												.isValidFileName(name)) {

								} else {
									hhm.slate.activity.storyboard.dialog.AlertDialog ad = new hhm.slate.activity.storyboard.dialog.AlertDialog(
											WorkVersionSelector.this,
											R.style.dialog,
											"已经存在同名的备份文件!或者文件名不符合要求");
									ad.show();

								}

								InitData();

							}
						});

				builder.setNegativeButton("取消", null);

				builder.show();

			}
		});

	}

	public void finish(View view) {
		finish();
	}

}
