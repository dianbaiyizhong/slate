package hhm.slate.activity.storyboard.view;

import hhm.slate.R;
import hhm.slate.activity.storyboard.adapter.ShotTextAdapter;
import hhm.slate.activity.storyboard.adapter.TakeTextAdapter;
import hhm.slate.activity.storyboard.adapter.TakeTextAdapter.OnLongItemClickListener;
import hhm.slate.activity.storyboard.function.EditShotActivity;
import hhm.slate.activity.storyboard.function.EditTakeActivity;
import hhm.slate.activity.storyboard.popwindow.dropdowm.keyword.AbstractKeywordSpinerAdapter;

import hhm.slate.activity.storyboard.popwindow.dropdowm.keyword.SpinerKeywordPopWindow;
import hhm.slate.activity.storyboard.popwindow.dropdowm.shots.AbstractShotsSpinerAdapter;
import hhm.slate.activity.storyboard.popwindow.dropdowm.shots.SpinerShotsPopWindow;
import hhm.slate.activity.storyboard.view.entity.IndexParam;
import hhm.slate.activity.storyboard.view.util.PosRecorder;
import hhm.slate.activity.storyboard.view.util.StringRecorder;
import hhm.slate.db.entity.Shot;
import hhm.slate.db.entity.Shots;
import hhm.slate.db.entity.Take;
import hhm.slate.db.impl.ShotImpl;
import hhm.slate.db.impl.ShotsImpl;
import hhm.slate.db.impl.TakeImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShotAndTakeMenuView extends LinearLayout implements
		AbstractKeywordSpinerAdapter.IKeywordOnItemSelectListener,
		AbstractShotsSpinerAdapter.IShotsOnItemSelectListener {
	EditText et_shot_and_take_search;
	private List<Shots> shots_list = new ArrayList<Shots>();
	private SpinerShotsPopWindow mSpinerPopWindow_shots;
	private SpinerKeywordPopWindow mSpinerPopWindow_keyword;
	CheckBox cb_statu_1;
	CheckBox cb_statu_2;
	private TextView tv_dropdowm_shot_and_take_shots;

	private TextView tv_dropdowm_shot_and_take_keyword;

	private ListView shotListView;
	private ListView takeListView;
	private List<Shot> shotList = new ArrayList<Shot>();
	private List<Take> takeList = new ArrayList<Take>();
	private Set<String> keyword_set_list = new HashSet<String>();
	private List<String> keyword_list;
	private Map<Integer, List<Take>> takeMap = new HashMap<Integer, List<Take>>();
	private ShotTextAdapter shotListViewAdapter;
	private TakeTextAdapter takeListViewAdapter;
	private OnSelectListener mOnSelectListener;

	private RelativeLayout rl_dropdowm_shot_and_take_shots;

	private RelativeLayout rl_dropdowm_shot_and_take_keyword;
	private Context mContext;

	private static IndexParam indexParam = new IndexParam();

	private final String default_chinese = "默认";

	public ShotAndTakeMenuView(Context context) {
		super(context);

		mContext = context;
		// 初始化的时候只做一次，否则会出现页面循环嵌套的情况
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_shot_and_take, this, true);

		// 初始化数据
		shotListView = (ListView) findViewById(R.id.sb_shot_listview);
		takeListView = (ListView) findViewById(R.id.sb_shot_listview_child);
		et_shot_and_take_search = (EditText) findViewById(R.id.et_shot_and_take_search);

		LoadSortFun();
		LoadDropDowmBtnShots();
		initData(context);
		// 这个要在数据加载结束后才调用,否则没有数据
		LoadDropDowmBtnKeyword();

	}

	private void LoadDropDowmBtnKeyword() {

		tv_dropdowm_shot_and_take_keyword = (TextView) findViewById(R.id.tv_dropdowm_shot_and_take_keyword);
		rl_dropdowm_shot_and_take_keyword = (RelativeLayout) findViewById(R.id.rl_dropdowm_shot_and_take_keyword);
		rl_dropdowm_shot_and_take_keyword
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						showKeywordSpinWindow(tv_dropdowm_shot_and_take_keyword);

					}
				});

		// 加载关键词

		keyword_list = new ArrayList<String>();
		keyword_list.add(default_chinese);

		Iterator<String> it = keyword_set_list.iterator();

		while (it.hasNext()) {
			String str = it.next();

			keyword_list.add(str);

		}

		tv_dropdowm_shot_and_take_keyword.setText(default_chinese);
		mSpinerPopWindow_keyword = new SpinerKeywordPopWindow(getContext());
		mSpinerPopWindow_keyword.refreshData(keyword_list, 0);
		mSpinerPopWindow_keyword.setItemListener(this);

	}

	private void LoadDropDowmBtnShots() {

		rl_dropdowm_shot_and_take_shots = (RelativeLayout) findViewById(R.id.rl_dropdowm_shot_and_take_shots);
		tv_dropdowm_shot_and_take_shots = (TextView) findViewById(R.id.tv_dropdowm_shot_and_take_shots);
		rl_dropdowm_shot_and_take_shots
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						showShotsSpinWindow(tv_dropdowm_shot_and_take_shots);

					}
				});

		ShotsImpl shotsImpl = new ShotsImpl(getContext());
		shots_list = shotsImpl.query();

		shots_list.add(new Shots(default_chinese));
		tv_dropdowm_shot_and_take_shots.setText(default_chinese);
		mSpinerPopWindow_shots = new SpinerShotsPopWindow(getContext());

		mSpinerPopWindow_shots.refreshData(shots_list, 0);
		mSpinerPopWindow_shots.setItemListener(this);
	}

	private void showShotsSpinWindow(TextView tv) {
		mSpinerPopWindow_shots.setWidth(tv.getWidth());
		mSpinerPopWindow_shots.showAsDropDown(tv);
	}

	private void showKeywordSpinWindow(TextView tv) {
		mSpinerPopWindow_keyword.setWidth(tv.getWidth());
		mSpinerPopWindow_keyword.showAsDropDown(tv);
	}

	// 重新载入数据

	private void LoadSortFun() {
		cb_statu_1 = (CheckBox) findViewById(R.id.cb_statu_1);
		cb_statu_2 = (CheckBox) findViewById(R.id.cb_statu_2);

		cb_statu_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean bol) {

				if (bol) {
					indexParam.setComplete_status(-1);
					cb_statu_2.setChecked(!bol);
				}
				StartIndex();

			}
		});

		cb_statu_2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean bol) {

				if (bol) {
					indexParam.setComplete_status(1);
					cb_statu_1.setChecked(!bol);
				}
				StartIndex();

			}
		});

	}

	public void reload(int scene_id) {
		et_shot_and_take_search.setText("");

		// 初始化数据之前，把上次数据清除掉
		takeList = new ArrayList<Take>();
		takeMap = new HashMap<Integer, List<Take>>();

		// 要把那个什么顶部信息设置回原来的样子“请选择 镜次”
		StringRecorder.reset();
		mOnSelectListener.getValue();

		initData(mContext);

	}

	public void reloadButNoReset() {

		// 初始化数据之前，把上次数据清除掉
		takeList = new ArrayList<Take>();
		takeMap = new HashMap<Integer, List<Take>>();

		mOnSelectListener.getValue();

		initData(mContext);

	}

	LinearLayout ll_storyboard_shooting;

	private int getShotIdByPos(int shot_id) {
		for (int i = 0; i < shotList.size(); i++) {
			if (shot_id == i) {
				return shotList.get(i).getShot_id();
			}
		}
		return 0;
	}

	private void LoadTakeById(int position, int shot_id) {
		takeList.clear();
		takeList.addAll(takeMap.get(shot_id));
		takeListViewAdapter.notifyDataSetChanged();

		/**
		 * 选择好镜头后，所执行的内容在这里写
		 */

	}

	private void initData(Context context) {

		et_shot_and_take_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				String value = arg0.toString();
				indexParam.setSearch_keyword(value);
				StartIndex();
			}

		});

		ShotImpl shotImpl = new ShotImpl(context);
		TakeImpl takeImpl = new TakeImpl(context);
		// 获取某一指定场景的所有镜头

		shotList = shotImpl
				.query(String.valueOf(PosRecorder.getScene_pos_id()));

		for (int i = 0; i < shotList.size(); i++) {

			// 在这里加载好所有的关键词进入到set队列
			if (!shotList.get(i).getShot_keyword().equals("")) {
				keyword_set_list.add(shotList.get(i).getShot_keyword());

			}

			List<Take> takelist = new ArrayList<Take>();

			// 通过shot_id查询所有次数信息
			takelist = takeImpl.query(shotList.get(i).getShot_id().toString());

			// 以shot_id作为map的键值
			takeMap.put(shotList.get(i).getShot_id(), takelist);
			shotList.get(i).setStatu(-1);

			// 这是关键，镜头是否已经成为完成状态，需要 次的决定，这个时候在这里判断
			for (int j = 0; j < takelist.size(); j++) {
				int value = takelist.get(j).getIs_available();

				if (value == 1) {

					shotList.get(i).setStatu(1);

					continue;
				}

			}

		}

		// 装载shot数据
		shotListViewAdapter = new ShotTextAdapter(context, shotList,
				PosRecorder.getShot_pos_id());

		shotListView.setAdapter(shotListViewAdapter);
		shotListViewAdapter
				.setOnItemClickListener(new ShotTextAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, final int position) {
						if (position < takeMap.size()) {

							// 获取shot_id并且记录好shot_id
							TextView tv_shot_id = (TextView) view
									.findViewById(R.id.sb_shot_id);
							TextView tv_shot_number = (TextView) view
									.findViewById(R.id.sb_shot_number);
							// 每次点击结束后，都要记录下来位置
							int shot_id = Integer.parseInt(tv_shot_id.getText()
									.toString());

							PosRecorder.setShot_pos_id(shot_id);

							String shot_number = tv_shot_number.getText()
									.toString();
							// 记录选择的镜号
							StringRecorder
									.setShot_ShowString(shot_number + "镜");

							LoadTakeById(position, PosRecorder.getShot_pos_id());

						}
					}

				});

		// 加载长按事件
		shotListViewAdapter
				.setOnLongItemClickListener(new OnLongItemClickListener() {

					@Override
					public void onLongItemClick(View view, final int position) {

						// 获取shot_id并且记录好shot_id
						TextView tv_shot_id = (TextView) view
								.findViewById(R.id.sb_shot_id);
						TextView tv_shot_number = (TextView) view
								.findViewById(R.id.sb_shot_number);
						int shot_id = Integer.parseInt(tv_shot_id.getText()
								.toString());

						// 每次点击结束后，都要记录下来位置
						PosRecorder.setShot_pos_id(shot_id);

						String shot_number = tv_shot_number.getText()
								.toString();
						// 记录选择的镜号
						StringRecorder.setShot_ShowString(shot_number + "镜");

						final AlertDialog.Builder builder = new AlertDialog.Builder(
								mContext);
						builder.setTitle("操作");
						// builder.setIcon(R.drawable.ic_launcher);

						builder.setItems(new String[] { "编辑", "删除", "取消" },
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int index) {

										if (index == 0) {
											Intent intent = new Intent();
											intent.setClass(getContext(),
													EditShotActivity.class);
											intent.putExtra("shot_id",
													PosRecorder
															.getShot_pos_id());
											mContext.startActivity(intent);

										} else if (index == 1) {

											deleteShot(PosRecorder
													.getShot_pos_id());

										} else if (index == 2) {

										}

									}

								});

						builder.show();

					}
				});
		// 这个代表首次加载，当然显示的是第一个镜头的所有的次的数据咯
		if (shotList.size() != 0) {
			StringRecorder.setShot_ShowString(shotList.get(0).getShot_number()
					+ "镜");
			if (PosRecorder.getShot_pos_id() != 0) {
				// 证明已经选择过的记录
				takeList.addAll(takeMap.get(PosRecorder.getShot_pos_id()));

			}

		}
		takeListViewAdapter = new TakeTextAdapter(context, takeList,
				PosRecorder.getTake_pos_id());
		takeListView.setAdapter(takeListViewAdapter);
		takeListViewAdapter
				.setOnItemClickListener(new TakeTextAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, final int position) {

						// 把镜头名称以及次数显示在栏里

						if (mOnSelectListener != null) {

							/**
							 * 在这里执行点击后的事情
							 */
							// 存储后选择的shot_id

							// 获取选中项中隐藏的take_id
							TextView tv = (TextView) view
									.findViewById(R.id.sb_take_id);

							// 记录take_id
							int take_id = Integer.parseInt(tv.getText()
									.toString());
							// 在这里设置要显示在栏 里地信息

							StringRecorder.setTake_ShowString(takeList.get(
									position).getTake_number()
									+ "次");
							// 一定要执行前面的，在最后执行这句话，只要下面的执行，StoryBoardActivity才开始监听

							mOnSelectListener.getValue();

							// 记录位置
							PosRecorder.setTake_pos_id(take_id);

						}

					}
				});

		takeListViewAdapter
				.setOnLongItemClickListener(new TakeTextAdapter.OnLongItemClickListener() {

					@Override
					public void onLongItemClick(final View view, int position) {

						final TextView tv_take_id = (TextView) view
								.findViewById(R.id.sb_take_id);
						final AlertDialog.Builder builder = new AlertDialog.Builder(
								mContext);
						builder.setTitle("操作");
						// builder.setIcon(R.drawable.ic_launcher);
						// 获取选中项中隐藏的take_id
						TextView tv = (TextView) view
								.findViewById(R.id.sb_take_id);

						// 记录take_id
						int take_id = Integer.parseInt(tv.getText().toString());
						// 记录位置
						PosRecorder.setTake_pos_id(take_id);

						StringRecorder.setTake_ShowString(takeList
								.get(position).getTake_number() + "次");
						builder.setItems(new String[] { "编辑", "删除", "取消" },
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int index) {
										if (index == 1) {
											deleteTake(Integer
													.parseInt(tv_take_id
															.getText()
															.toString()));
										} else if (index == 0) {
											editTake(Integer
													.parseInt(tv_take_id
															.getText()
															.toString()));

										}

									}

								});

						builder.show();

					}

				});

	}

	private void editTake(int id) {
		Intent intent = new Intent();
		intent.setClass(getContext(), EditTakeActivity.class);

		intent.putExtra("take_id", id);
		getContext().startActivity(intent);
	}

	private void deleteTake(final int id) {

		// 长按之后弹出的是一个是否删除
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("删除");
		// builder.setIcon(R.drawable.ic_launcher);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				TakeImpl takeImpl = new TakeImpl(getContext());

				takeListViewAdapter.remove(id);
				takeImpl.delete(id);

			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}

		});

		builder.show();
	}

	private void deleteShot(final int shot_id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("删除");
		// builder.setIcon(R.drawable.ic_launcher);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				ShotImpl shotImpl = new ShotImpl(getContext());
				shotListViewAdapter.remove(shot_id);

				shotImpl.delete(shot_id);
				PosRecorder.reset();
				reload(PosRecorder.getScene_pos_id());

			}
		});
		builder.setNegativeButton("取消", null);

		builder.show();
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		public void getValue();
	}

	@Override
	public void onShotsItemClick(int pos) {

		String shots = shots_list.get(pos).getShots_name();
		tv_dropdowm_shot_and_take_shots.setText(shots);

		// 如果点击了“默认”，那要把搜索索引的条件设置为空
		if (shots.equals(default_chinese)) {
			indexParam.setShots("");

		} else {
			indexParam.setShots(shots);

		}

		StartIndex();

	}

	@Override
	public void onKeywordItemClick(int pos) {

		String keyword = (String) keyword_list.get(pos);
		tv_dropdowm_shot_and_take_keyword.setText(keyword);

		// 如果点击了“默认”，那要把搜索索引的条件设置为空
		if (keyword.equals(default_chinese)) {
			indexParam.setShot_keyword("");

		} else {
			indexParam.setShot_keyword(keyword);

		}

		StartIndex();

	}

	/**
	 * 无论点击哪些搜索请求，都要执行这个，类似提交表单
	 */
	private void StartIndex() {

		// 还要判断spinner是否在已经点击的状态下，用户点击取消
		if (!cb_statu_1.isChecked() && !cb_statu_2.isChecked()) {
			indexParam.setComplete_status(0);
		}

		shotListViewAdapter.StartIndex(indexParam);

	}

}
