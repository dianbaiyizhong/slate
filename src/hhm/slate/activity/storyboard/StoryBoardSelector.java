package hhm.slate.activity.storyboard;

import hhm.slate.R;
import hhm.slate.activity.storyboard.function.EditFilmActivity;
import hhm.slate.db.ConfigImpl;
import hhm.slate.db.entity.Film;
import hhm.slate.db.impl.FilmImpl;
import hhm.slate.init.Init;
import hhm.slate.prefer.PreferencesService;
import hhm.slate.util.ExcelUtil;
import hhm.slate.util.FileUtil;
import hhm.slate.util.ShareFileThread;
import hhm.slate.util.StringUtil;
import hhm.slate.util.TimeUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class StoryBoardSelector extends SlidingFragmentActivity {

	RelativeLayout ll_sb_selector;
	private PreferencesService preferencesService;
	private SimpleAdapter listItemAdapter;
	static StringUtil stringUtil = new StringUtil();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storyboard_selector);

		// 设置是否能够使用ActionBar来滑动
		setSlidingActionBarEnabled(true);
		// 初始化滑动菜单
		initSlidingMenu(savedInstanceState);

		// 加载数据
		InitData();

	}

	private void InitData() {

		// 先取出用户是使用哪个数据库(工作版本)的数据
		preferencesService = new PreferencesService(this);
		Map<String, String> dbname_params = preferencesService
				.getNowDBNamePerferences();

		String dbname = dbname_params.get("nowDBName");

		if (dbname == null || dbname.equals("")) {

			ConfigImpl.setNowDBName("default.slate");
		} else {
			ConfigImpl.setNowDBName(dbname);

		}

		// 初始化
		Init init = new Init();
		init.InitUserDBFile(this);
		init.InitDB(this);
		init.InitImageFile();
		init.InitExcelFile();

		LoadData();

	}

	private void LoadData() {
		FilmImpl filmImpl = new FilmImpl(this);
		List<Film> filmList = filmImpl.query();

		final ListView list = (ListView) findViewById(R.id.LV_storyboard_selector);

		final ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

		if (filmList.size() != 0) {

			for (int i = 0; i < filmList.size(); i++) {

				Timestamp createTime = filmList.get(i).getFilm_createtime();
				Timestamp wraptime = filmList.get(i).getFilm_wraptime();

				String s_createTime = new TimeUtil().getTime(createTime);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("film_name", filmList.get(i).getFilm_name());

				String director = filmList.get(i).getStaff().getDirector();

				if (director == null) {
					map.put("director", "导演:未填写");

				} else {
					if (director.equals("")) {
						map.put("director", "导演:未填写");

					} else {
						map.put("director",
								"导演:" + stringUtil.getName(director));

					}

				}
				map.put("film_createtime", s_createTime);
				if (wraptime == null) {

					map.put("film_wraptime", "仍未杀青");
				} else {
					String s_wrapTime = new TimeUtil().getTime(wraptime);
					map.put("film_wraptime", s_wrapTime);
				}
				map.put("film_id", filmList.get(i).getFilm_id());
				listItem.add(map);
			}
		}
		listItemAdapter = new SimpleAdapter(this, listItem,

		R.layout.listview_storyboard_selector, new String[] { "film_name",
				"director", "film_createtime", "film_wraptime", "film_id" },
				new int[] { R.id.sb_film_name, R.id.sb_director,
						R.id.sb_film_createtime, R.id.sb_film_wraptime,
						R.id.sb_id });

		list.setAdapter(listItemAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> lv, View view, int index,
					long arg3) {

				TextView tv = (TextView) lv.getChildAt(index).findViewById(
						R.id.sb_id);

				String film_id = tv.getText().toString();

				Intent intent = new Intent(StoryBoardSelector.this,
						StoryBoardActivity.class);

				intent.putExtra("film_id", film_id);

				startActivity(intent);
			}

		});

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> lv, View arg1,
					int index, long arg3) {
				final TextView tv = (TextView) lv.getChildAt(index)
						.findViewById(R.id.sb_id);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						StoryBoardSelector.this);
				builder.setTitle("操作");
				builder.setIcon(R.drawable.ic_launcher);
				builder.setItems(
						new String[] { "导出", "删除", "编辑", "是否杀青", "取消" },
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int index) {

								if (index == 0) {

									// 这里是导出时的操作

									String film_id = tv.getText().toString();

									Output(film_id);

								} else if (index == 1) {
									String film_id = tv.getText().toString();

									Delete(film_id);

								} else if (index == 2) {
									String film_id = tv.getText().toString();

									Edit(film_id);

								} else if (index == 3) {
									Toast.makeText(getApplicationContext(),
											"该功能暂未开发", 3000).show();
									// Wrap();
								} else if (index == 4) {

								}

							}

						});
				builder.show();

				return false;
			}

		});

	}

	private void Wrap() {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				StoryBoardSelector.this);
		builder.setTitle("操作");
		builder.setIcon(R.drawable.ic_launcher);
		builder.setPositiveButton("已杀青", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				FilmImpl filmImpl = new FilmImpl(getApplicationContext());

			}
		});

		builder.setNegativeButton("未杀青", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

	}

	private void Edit(String film_id) {

		Intent intent = new Intent(StoryBoardSelector.this,
				EditFilmActivity.class);

		intent.putExtra("film_id", film_id);

		startActivity(intent);

	}

	private void Delete(final String film_id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				StoryBoardSelector.this);
		builder.setTitle("操作");
		builder.setMessage("删除后会把有关联的场，镜，次的数据清空!");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				FilmImpl filmImpl = new FilmImpl(getApplicationContext());
				filmImpl.delete(Integer.parseInt(film_id));
				LoadData();
				listItemAdapter.notifyDataSetChanged();
			}

		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		builder.show();

	}

	/**
	 * 导出
	 */
	private void Output(String film_id) {
		ExcelUtil excelUtil = new ExcelUtil(getApplicationContext());

		try {
			final String filename = excelUtil.Output(film_id);

			// 导出成功之后，弹出框
			AlertDialog.Builder builder = new AlertDialog.Builder(
					StoryBoardSelector.this);
			builder.setTitle("导出成功");
			builder.setIcon(R.drawable.ic_launcher);

			builder.setItems(new String[] { "分享", "打开查看", "取消" },
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int index) {

							if (index == 0) {

								ShareFileThread st = new ShareFileThread(
										StoryBoardSelector.this, filename, "");
								st.start();

							} else if (index == 1) {

								try {
									FileUtil fileUtil = new FileUtil();
									fileUtil.OpenExcel(filename + ".xls",
											StoryBoardSelector.this);
								} catch (Exception e) {
									System.out.println(e.getMessage());
								}

							}

						}

					});

			builder.show();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * 初始化滑动菜单
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {

		// 设置滑动菜单的视图
		// 这里设置一个没有任何关系的布局，原因未知
		setBehindContentView(R.layout.left_menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.left_menu_frame, new MenuLeftFragment()).commit();
		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动阴影的图像资源
		sm.setShadowDrawable(R.drawable.shadow);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		InitData();
	}

}
