package hhm.slate.activity.storyboard.popwindow.dropdowm.keyword;

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

public abstract class AbstractKeywordSpinerAdapter<T> extends BaseAdapter {

	public static interface IKeywordOnItemSelectListener {
		public void onKeywordItemClick(int pos);
	};

	private Context mContext;
	private List<String> mObjects = new ArrayList<String>();
	private int mSelectItem = 0;

	private LayoutInflater mInflater;

	public AbstractKeywordSpinerAdapter(Context context) {
		init(context);
	}

	public void refreshData(List<String> objects, int selIndex) {
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

		String keyword = mObjects.get(pos);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_dropdowm_shot_keyword,
					null);
			viewHolder = new ViewHolder();
			viewHolder.tv_shots = (TextView) convertView
					.findViewById(R.id.dropdowm_shot_keyword);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tv_shots.setText(keyword);
		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_shots;

	}

}
