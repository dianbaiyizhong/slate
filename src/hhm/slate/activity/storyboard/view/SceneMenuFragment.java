package hhm.slate.activity.storyboard.view;

import hhm.slate.activity.storyboard.StoryBoardActivity;
import hhm.slate.activity.storyboard.view.util.StringRecorder;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SceneMenuFragment extends Fragment {

	public SceneMenuView sceneMenuView;

	private Activity activity;
	private String film_id;
	// 提供给外的接口
	private static SceneMenuFragment instance = null;

	// 单例模式
	public static SceneMenuFragment getInstance() {
		if (instance == null) {
			instance = new SceneMenuFragment();
		}
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 实例化级联菜单

		sceneMenuView = new SceneMenuView(getActivity(), activity, film_id);
		sceneMenuView.setOnSelectListener(new SceneMenuView.OnSelectListener() {

			public void getValue() {
				StoryBoardActivity.btn_scene.setText(StringRecorder
						.getScene_ShowString());

			}
		});
	}

	public void setFilm(String film_id) {
		this.film_id = film_id;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public void LoadDistance(Context context, Double latitude, Double longitude) {

		sceneMenuView.LoadDistance(context, latitude, longitude);

	}

	public void reload() {

		sceneMenuView.reload();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return sceneMenuView;
	}

}
