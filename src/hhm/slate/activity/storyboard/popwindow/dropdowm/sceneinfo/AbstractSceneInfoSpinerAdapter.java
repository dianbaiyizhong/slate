package hhm.slate.activity.storyboard.popwindow.dropdowm.sceneinfo;

import hhm.slate.R;
import hhm.slate.db.entity.Scene;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class AbstractSceneInfoSpinerAdapter<T> extends BaseAdapter {

	public static interface ISceneInfoOnItemSelectListener {
		public void onSceneInfoItemClick(int pos);
	};

	private Context mContext;
	private List<Scene> mObjects = new ArrayList<Scene>();
	private int mSelectItem = 0;

	private LayoutInflater mInflater;

	public AbstractSceneInfoSpinerAdapter(Context context) {
		init(context);
	}

	public void refreshData(List<Scene> objects, int selIndex) {
		mObjects = objects;
		if (selIndex < 0) {
			selIndex = 0;
		}
		if (selIndex >= mObjects.size()) {
			selIndex = mObjects.size() - 1;
		}

		mSelectItem = selIndex;
	}

	private void init(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return mObjects.size();
	}

	@Override
	public Object getItem(int pos) {
		return mObjects.get(pos).toString();
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder;

		Scene scene = (Scene) mObjects.get(pos);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_dropdowm_addshot,
					null);
			viewHolder = new ViewHolder();
			viewHolder.tv_scene_name = (TextView) convertView
					.findViewById(R.id.sp_sceneinfo_scene_name);
			viewHolder.tv_scene_id = (TextView) convertView
					.findViewById(R.id.sp_sceneinfo_scene_id);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tv_scene_name.setText(scene.getScene_name());
		viewHolder.tv_scene_id.setText(String.valueOf(scene.getScene_id()));
		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_scene_name;
		public TextView tv_scene_id;

	}

}
