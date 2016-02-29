package hhm.slate.activity.storyboard.function.adatpter;

import hhm.slate.R;
import hhm.slate.db.entity.Scene;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SceneInfoAdapter extends ArrayAdapter<Scene> {

	private int resource;

	private List<Scene> objects;

	private LayoutInflater inflater;

	public SceneInfoAdapter(Context context, int resource, List<Scene> objects) {
		super(context, resource, objects);
		this.resource = resource;
		this.objects = objects;

		this.inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	public Scene getItem(int position) {

		return objects.get(position);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		

		View view = inflater.inflate(R.layout.dropdown_sceneinfo, null);
		TextView label = (TextView) view
				.findViewById(R.id.sp_sceneinfo_scene_id);

		label.setText(objects.get(position).toString());
		// if (gradeSpinner.getSelectedItemPosition() == position) {
		// view.setBackgroundColor(getResources().getColor(
		// R.color.spinner_green));
		// check.setImageResource(R.drawable.check_selected);
		// } else {
		// view.setBackgroundColor(getResources().getColor(
		// R.color.spinner_light_green));
		// check.setImageResource(R.drawable.check_unselect);
		// }

		return view;
	}

//	public View getView(int position, View convertView, ViewGroup parent) {
//		LinearLayout ll;
//		Scene scene = objects.get(position);
//		int scene_id = scene.getScene_id();
//		String scene_name = scene.getScene_name();
//		if (convertView == null) {
//			ll = new LinearLayout(getContext());
//			LayoutInflater inflater = (LayoutInflater) getContext()
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			inflater.inflate(R.layout.sp_sceneinfo, ll, true);
//		} else {
//			ll = (LinearLayout) convertView;
//		}
//
//		// 获取控件,填充数据
//		TextView tv1 = (TextView) ll.findViewById(R.id.sp_sceneinfo_scene_id);
//		tv1.setText(String.valueOf(scene_id));
//		TextView tv2 = (TextView) ll.findViewById(R.id.sp_sceneinfo_film_name);
//		tv2.setText(scene_name);
//
//		return ll;
//
//	}

}
