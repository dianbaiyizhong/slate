package hhm.slate.activity.storyboard.popwindow.dropdowm.shots;

import hhm.slate.R;
import hhm.slate.db.entity.Scene;
import hhm.slate.db.entity.Shots;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class AbstractShotsSpinerAdapter<T> extends BaseAdapter {

	public static interface IShotsOnItemSelectListener {
		public void onShotsItemClick(int pos);
	};

	private Context mContext;
	private List<Shots> mObjects = new ArrayList<Shots>();
	private int mSelectItem = 0;

	private LayoutInflater mInflater;

	public AbstractShotsSpinerAdapter(Context context) {
		init(context);
	}

	public void refreshData(List<Shots> objects, int selIndex) {
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

		Shots shots = mObjects.get(pos);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_dropdowm_shots,
					null);
			viewHolder = new ViewHolder();
			viewHolder.tv_shots = (TextView) convertView
					.findViewById(R.id.dropdowm_shots);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tv_shots.setText(shots.getShots_name());
		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_shots;

	}

}
