package hhm.slate.activity.storyboard.function.adatpter;

import hhm.slate.R;
import hhm.slate.activity.storyboard.function.entity.ResultInfo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class ResultAdapter extends BaseAdapter implements Filterable {
	private List<ResultInfo> mList;
	private Context context;
	private ArrayFilter mFilter;
	private ArrayList<ResultInfo> mUnfilteredData;

	public ResultAdapter(Context context) {

		this.context = context;
	}

	public void add(ResultInfo obj) {
		if (mList == null) {
			mList = new ArrayList<ResultInfo>();
		}
		if (obj.getKey() != null) {
			mList.add(obj);

		}

	}

	public void clear() {
		if (mList != null) {
			mList.clear();

		}
	}

	public int getCount() {

		return mList == null ? 0 : mList.size();
	}

	public Object getItem(int position) {
		return mList.get(position).getKey();
	}

	public long getItemId(int position) {

		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = View.inflate(context, R.layout.actv_mapsearch, null);

			holder = new ViewHolder();
			holder.key = (TextView) view.findViewById(R.id.actv_mapsearch_key);
			holder.pt = (TextView) view.findViewById(R.id.actv_mapsearch_pt);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		ResultInfo pc = mList.get(position);

		holder.key.setText(pc.getKey());
		holder.pt.setText(pc.getLatitude() + "_" + pc.getLongitude());

		return view;
	}

	static class ViewHolder {
		public TextView key;
		public TextView pt;
	}

	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	private class ArrayFilter extends Filter {

		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mUnfilteredData == null) {
				mUnfilteredData = new ArrayList<ResultInfo>(mList);
			}

			return results;
		}

		protected void publishResults(CharSequence arg0, FilterResults results) {
			mList = (List<ResultInfo>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}

	}

}
