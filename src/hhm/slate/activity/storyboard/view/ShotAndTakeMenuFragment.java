package hhm.slate.activity.storyboard.view;

import hhm.slate.R;
import hhm.slate.activity.storyboard.StoryBoardActivity;
import hhm.slate.activity.storyboard.dialog.AlertDialog;
import hhm.slate.activity.storyboard.function.TakeActivity;
import hhm.slate.activity.storyboard.view.util.PosRecorder;
import hhm.slate.activity.storyboard.view.util.StringRecorder;
import hhm.slate.db.impl.ShotImpl;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ShotAndTakeMenuFragment extends Fragment {

	
	
	private ShotAndTakeMenuView shotAndTakeMenuView;

	// 提供给外的接口
	private static ShotAndTakeMenuFragment instance = null;

	Button btn_startShooting;

	// 单例模式
	public static ShotAndTakeMenuFragment getInstance() {
		if (instance == null) {
			instance = new ShotAndTakeMenuFragment();
		}
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 实例化级联菜单
		shotAndTakeMenuView = new ShotAndTakeMenuView(getActivity());
		shotAndTakeMenuView
				.setOnSelectListener(new ShotAndTakeMenuView.OnSelectListener() {

					public void getValue() {
						StoryBoardActivity.btn_shotAndTake
								.setText(StringRecorder
										.getShot_or_take_ShowString());
					}

				});

		LoadStardShootingBtn();

	}

	
	
	
	
	private void LoadStardShootingBtn() {
		btn_startShooting = (Button) shotAndTakeMenuView
				.findViewById(R.id.btn_startShooting);
		btn_startShooting.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				// 点击按钮的时候要来个预处理，再次确认该shot_id存不存在
				ShotImpl shotImpl = new ShotImpl(getActivity());

				if (shotImpl.isExist(PosRecorder.getShot_pos_id())) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), TakeActivity.class);
					intent.putExtra("shot_id", PosRecorder.getShot_pos_id());
					startActivity(intent);
				} else {
					AlertDialog ad = new AlertDialog(getActivity(),
							R.style.dialog, "请添加镜头");

					ad.show();
				}
			}
		});
	}

	public void reload(int scene_id) {
		shotAndTakeMenuView.reload(scene_id);
	}

	public void reloadButNoReset() {
		shotAndTakeMenuView.reloadButNoReset();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return shotAndTakeMenuView;
	}

}
