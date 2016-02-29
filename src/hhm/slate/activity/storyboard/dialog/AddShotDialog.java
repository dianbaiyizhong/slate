package hhm.slate.activity.storyboard.dialog;

import hhm.slate.R;
import hhm.slate.activity.storyboard.ActivityBase;
import hhm.slate.activity.storyboard.popwindow.dropdowm.sceneinfo.AbstractSceneInfoSpinerAdapter;
import hhm.slate.activity.storyboard.popwindow.dropdowm.sceneinfo.SpinerSceneInfoPopWindow;
import hhm.slate.activity.storyboard.popwindow.dropdowm.shots.AbstractShotsSpinerAdapter;
import hhm.slate.activity.storyboard.popwindow.dropdowm.shots.SpinerShotsPopWindow;
import hhm.slate.activity.storyboard.view.ShotAndTakeMenuFragment;
import hhm.slate.db.entity.Scene;
import hhm.slate.db.entity.Shot;
import hhm.slate.db.entity.Shots;
import hhm.slate.db.impl.SceneImpl;
import hhm.slate.db.impl.ShotImpl;
import hhm.slate.db.impl.ShotsImpl;
import hhm.slate.util.VerifyUtil;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddShotDialog extends Dialog implements
		AbstractSceneInfoSpinerAdapter.ISceneInfoOnItemSelectListener,
		AbstractShotsSpinerAdapter.IShotsOnItemSelectListener, ActivityBase {
	private ShotAndTakeMenuFragment shotAndTakeMenuFragment;
	TextView tv_dropdowm_addshot_scene_name;
	TextView tv_dropdowm_addshot_scene_id;
	TextView tv_dropdowm_shots;

	EditText et_shot_name;
	EditText et_shot_number;
	EditText et_shot_keyword;
	private SpinerSceneInfoPopWindow mSpinerPopWindow_scene_info;
	private SpinerShotsPopWindow mSpinerPopWindow_shots;

	private List<Scene> scene_list = new ArrayList<Scene>();
	private List<Shots> shots_list = new ArrayList<Shots>();

	Button btn_addshot;
	private ImageButton ib_addshot_dropdown_sceneinfo;
	private ImageButton ib_addshot_dropdown_shots;

	private int scene_id;
	private int film_id;
	private Context context;

	public AddShotDialog(Context context, int theme, int scene_id, int film_id,
			ShotAndTakeMenuFragment shotAndTakeMenuFragment) {
		super(context, theme);
		this.scene_id = scene_id;
		this.shotAndTakeMenuFragment = shotAndTakeMenuFragment;
		this.film_id = film_id;
		this.context = context;

	}

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_addshot);

		// 加载控件
		LoadWidget();

		// 加载下拉列表
		LoadDropDowmBtnSceneInfo();

		LoadDropDowmBtnShots();
		// 加载提交按钮
		LoadBtnAddShot();
		// 控件赋值
		LoadWidgetValue();
		// 设置窗口大小
		setWindow();

	}

	private void setWindow() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		lp.width = (int) (d.widthPixels * 0.8);
		lp.height = (int) (d.heightPixels * 0.7);
		dialogWindow.setAttributes(lp);

	}

	private void LoadDropDowmBtnShots() {

		ib_addshot_dropdown_shots = (ImageButton) findViewById(R.id.ib_addshot_dropdown_shots);

		ib_addshot_dropdown_shots
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						showShotsSpinWindow(tv_dropdowm_shots);

					}
				});

		ShotsImpl shotsImpl = new ShotsImpl(getContext());
		shots_list = shotsImpl.query();
		shots_list.add(0, new Shots("不填"));
		tv_dropdowm_shots.setText(shots_list.get(0).getShots_name());
		mSpinerPopWindow_shots = new SpinerShotsPopWindow(getContext());
		mSpinerPopWindow_shots.refreshData(shots_list, 0);
		mSpinerPopWindow_shots.setItemListener(this);
	}

	private void LoadDropDowmBtnSceneInfo() {

		ib_addshot_dropdown_sceneinfo = (ImageButton) findViewById(R.id.ib_addshot_dropdown_sceneinfo);

		ib_addshot_dropdown_sceneinfo
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						showSceneInfoSpinWindow(tv_dropdowm_addshot_scene_name);

					}
				});
		// 通过scene_id获取相关的信息
		SceneImpl sceneImpl = new SceneImpl(getContext());
		scene_list = sceneImpl.query(String.valueOf(film_id));
		// 这里对scene_list进行排序，可以设置默认选项
		if (scene_id != 0) {
			for (int i = 0; i < scene_list.size(); i++) {

				if (scene_list.get(i).getScene_id() == scene_id) {

					Scene Scene_first = scene_list.get(0);
					scene_list.set(0, scene_list.get(i));
					scene_list.set(i, Scene_first);
				}

			}
		}
		// 设置下拉列表的第一个值
		tv_dropdowm_addshot_scene_name.setText(scene_list.get(0)
				.getScene_name());
		tv_dropdowm_addshot_scene_id.setText(String.valueOf(scene_list.get(0)
				.getScene_id()));
		mSpinerPopWindow_scene_info = new SpinerSceneInfoPopWindow(getContext());
		mSpinerPopWindow_scene_info.refreshData(scene_list, 0);
		mSpinerPopWindow_scene_info.setItemListener(this);
	}

	private void LoadBtnAddShot() {
		btn_addshot = (Button) findViewById(R.id.btn_addshot);
		btn_addshot.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				saveShot();

			}

		});
	}

	private void saveShot() {

		// 验证
		if (!verify()) {
			return;
		}
		String shot_name = et_shot_name.getText().toString();
		String shot_number = et_shot_number.getText().toString();
		String shots = tv_dropdowm_shots.getText().toString();

		String keyword = et_shot_keyword.getText().toString() ;
		Shot shot = new Shot();
		shot.setShots(shots);

		shot.setScene_id(Integer.parseInt(tv_dropdowm_addshot_scene_id
				.getText().toString()));

		shot.setShot_name(shot_name);
		shot.setShot_number(Integer.parseInt(shot_number));
		shot.setShot_keyword(keyword);
		ShotImpl shotImpl = new ShotImpl(getContext());

		shotImpl.save(shot);

		// shotAndTakeMenuFragment.reload(scene_id);
		cancel();

	}

	private void showSceneInfoSpinWindow(TextView tv) {
		mSpinerPopWindow_scene_info.setWidth(tv.getWidth());
		mSpinerPopWindow_scene_info.showAsDropDown(tv);
	}

	private void showShotsSpinWindow(TextView tv) {
		mSpinerPopWindow_shots.setWidth(tv.getWidth());
		mSpinerPopWindow_shots.showAsDropDown(tv);
	}

	@Override
	public void onSceneInfoItemClick(int pos) {

		tv_dropdowm_addshot_scene_name.setText(scene_list.get(pos)
				.getScene_name());
		tv_dropdowm_addshot_scene_id.setText(String.valueOf(scene_list.get(pos)
				.getScene_id()));

		Load_et_shot_number();
	}

	@Override
	public void onShotsItemClick(int pos) {

		tv_dropdowm_shots.setText(shots_list.get(pos).getShots_name());
	}

	@Override
	public void LoadWidget() {
		et_shot_name = (EditText) findViewById(R.id.et_shot_name);

		et_shot_keyword = (EditText) findViewById(R.id.et_shot_keyword);
		et_shot_number = (EditText) findViewById(R.id.et_shot_number);
		tv_dropdowm_addshot_scene_name = (TextView) findViewById(R.id.tv_dropdowm_addshot_scene_name);
		tv_dropdowm_addshot_scene_id = (TextView) findViewById(R.id.tv_dropdowm_addshot_scene_id);

		tv_dropdowm_shots = (TextView) findViewById(R.id.tv_dropdowm_shots);
	}

	@Override
	public void LoadWidgetValue() {

		Load_et_shot_number();

	}

	private void Load_et_shot_number() {
		ShotImpl shotImpl = new ShotImpl(getContext());

		int count = shotImpl.queryRecentShotById(tv_dropdowm_addshot_scene_id
				.getText().toString());

		et_shot_number.setText(String.valueOf(count));

	}

	@Override
	public void LoadWidgetlistener() {

	}

	private boolean verify() {

		List<EditText> list = new ArrayList<EditText>();
		list.add(et_shot_name);
		list.add(et_shot_number);
		// 关键词可以为空，所以不填
		boolean bool = new VerifyUtil().verifyIsNull(getContext(), list);

		if (bool) {
			return true;

		} else {
			return false;
		}

	}

}
