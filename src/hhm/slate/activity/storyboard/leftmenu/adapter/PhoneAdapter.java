package hhm.slate.activity.storyboard.leftmenu.adapter;

import hhm.slate.R;
import hhm.slate.activity.storyboard.leftmenu.entity.Contact;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class PhoneAdapter extends BaseAdapter implements Filterable {
	private ArrayFilter mFilter;
	private List<Contact> mList;
	private Context context;
	private ArrayList<Contact> mUnfilteredData;

	public PhoneAdapter(List<Contact> mList, Context context) {
		this.mList = mList;
		this.context = context;
	}

	@Override
	public int getCount() {

		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position).getmContactsName() + "<"
				+ mList.get(position).getmContactsNumber() + ">";
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = View.inflate(context, R.layout.actv_phonenumber, null);

			holder = new ViewHolder();
			holder.tv_name = (TextView) view.findViewById(R.id.contactname);
			holder.tv_phone = (TextView) view.findViewById(R.id.contactnumber);
			holder.iv_photh = (ImageView) view.findViewById(R.id.contactphoto);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		Contact pc = mList.get(position);

		holder.tv_name.setText("姓名：" + pc.getmContactsName());
		holder.tv_phone.setText("电话：" + pc.getmContactsNumber());
		holder.iv_photh.setImageBitmap(pc.getmContactsPhonto());

		return view;
	}

	static class ViewHolder {
		public TextView tv_name;
		public TextView tv_phone;
		public ImageView iv_photh;
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	private class ArrayFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mUnfilteredData == null) {
				mUnfilteredData = new ArrayList<Contact>(mList);
			}

			if (prefix == null || prefix.length() == 0) {
				ArrayList<Contact> list = mUnfilteredData;
				results.values = list;
				results.count = list.size();
			} else {
				String prefixString = prefix.toString().toLowerCase();

				ArrayList<Contact> unfilteredValues = mUnfilteredData;
				int count = unfilteredValues.size();

				ArrayList<Contact> newValues = new ArrayList<Contact>(count);

				for (int i = 0; i < count; i++) {
					Contact pc = unfilteredValues.get(i);
					if (pc != null) {

						if (pc.getmContactsName() != null
								&& pc.getmContactsName().startsWith(
										prefixString)) {

							newValues.add(pc);
						} else if (pc.getmContactsNumber() != null
								&& pc.getmContactsNumber().startsWith(
										prefixString)) {

							newValues.add(pc);
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			mList = (List<Contact>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}

	}
}
