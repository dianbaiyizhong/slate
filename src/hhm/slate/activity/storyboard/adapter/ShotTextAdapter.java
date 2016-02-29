package hhm.slate.activity.storyboard.adapter;

import hhm.slate.R;
import hhm.slate.activity.storyboard.adapter.TakeTextAdapter.OnLongItemClickListener;
import hhm.slate.activity.storyboard.view.entity.IndexParam;
import hhm.slate.db.entity.Scene;
import hhm.slate.db.entity.Shot;
import hhm.slate.db.util.ListSortUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ShotTextAdapter extends ArrayAdapter<Shot> {

	private Context mContext;
	private List<Shot> mListData;
	private List<Shot> BackupListData;

	private int selectedPos = -1;
	private int normalDrawbleId;
	private Drawable selectedDrawble;
	private OnClickListener onClickListener;
	private OnItemClickListener mOnItemClickListener;
	private OnLongItemClickListener mOnLongItemClickListener;
	private OnLongClickListener onLongClickListener;
	private LayoutInflater mInflater;
	private int selectedTag = -1;

	public ShotTextAdapter(Context context, List<Shot> listData,
			int selected_shot_pos) {
		super(context, R.string.no_data, listData);
		mContext = context;
		mListData = listData;
		// 同步数据
		syncBackupData(listData);
		selectedDrawble = mContext.getResources().getDrawable(
				R.drawable.choose_item_selected);
		normalDrawbleId = R.drawable.choose_eara_item_selector;
		this.mInflater = LayoutInflater.from(context);

		init();

		if (selected_shot_pos != 0) {

			int pos = getPosById(selected_shot_pos);

			setSelectedPosition(pos);
		}

	}

	public void StartIndex(IndexParam indexParam) {
		mListData.clear();
		for (int i = 0; i < BackupListData.size(); i++) {

			// 1.判断是否含有搜索关键词
			if (!indexParam.getSearch_keyword().equals("")) {
				if (!BackupListData.get(i).getShot_name()
						.contains(indexParam.getSearch_keyword())) {
					continue;
				}
			}

			// 2.判断状态是“完成”还是“未完成”

			if (indexParam.getComplete_status() != 0) {
				if (BackupListData.get(i).getStatu() != indexParam
						.getComplete_status()) {
					continue;

				}
			}

			// 3.判断景别

			if (!indexParam.getShots().equals("")) {
				if (!BackupListData.get(i).getShots()
						.equals(indexParam.getShots())) {
					continue;
				}
			}

			// 4.判断关键词
			if (!indexParam.getShot_keyword().equals("")) {
				if (!BackupListData.get(i).getShot_keyword()
						.equals(indexParam.getShot_keyword())) {
					continue;
				}
			}

			mListData.add(BackupListData.get(i));
		}
		notifyDataSetChanged();
	}

	// public void SortByKeyword(String keyword) {
	//
	// mListData.clear();
	// if (keyword.equals("")) {
	// // 如果为空，那就还原
	// for (int i = 0; i < BackupListData.size(); i++) {
	// mListData.add(BackupListData.get(i));
	// }
	// notifyDataSetChanged();
	// return;
	// }
	//
	// for (int i = 0; i < BackupListData.size(); i++) {
	// if (BackupListData.get(i).getShot_keyword().equals(keyword)) {
	// mListData.add(BackupListData.get(i));
	//
	// }
	//
	// }
	//
	// notifyDataSetChanged();
	//
	// }

	// public void SortByShots(String shots) {
	// ListSortUtil<Shot> lsu = new ListSortUtil<Shot>();
	// lsu.sort(mListData, "shots", "desc");
	// // 把特定的镜头提前
	// for (int i = 0; i < mListData.size(); i++) {
	// if (mListData.get(i).getShots().equals(shots)) {
	// Shot shot = new Shot();
	// shot = mListData.get(i);
	// mListData.remove(i);
	// mListData.add(0, shot);
	// }
	//
	// }
	// notifyDataSetChanged();
	//
	// }

	// public void SortByStatus(int value) {
	// ListSortUtil<Shot> lsu = new ListSortUtil<Shot>();
	//
	// if (value == -1) {
	//
	// lsu.sort(mListData, "statu", "desc");
	//
	// } else if (value == 1) {
	// lsu.sort(mListData, "statu", "");
	//
	// } else if (value == 0) {
	// lsu.sort(mListData, "shot_number", "");
	//
	// }
	//
	// notifyDataSetChanged();
	//
	// }

	// public void Search(String value) {
	// mListData.clear();
	// if (value.equals("")) {
	// // 如果为空，那就还原
	// for (int i = 0; i < BackupListData.size(); i++) {
	// mListData.add(BackupListData.get(i));
	// }
	// notifyDataSetChanged();
	// return;
	// }
	//
	// for (int i = 0; i < BackupListData.size(); i++) {
	// if (BackupListData.get(i).getShot_name().contains(value)) {
	// mListData.add(BackupListData.get(i));
	//
	// }
	//
	// }
	//
	// notifyDataSetChanged();
	//
	// }

	// 同步数据
	public void syncBackupData(List<Shot> listData) {
		BackupListData = new ArrayList<Shot>();
		BackupListData.clear();
		for (int i = 0; i < listData.size(); i++) {
			BackupListData.add(listData.get(i));

		}
	}

	private void init() {
		onClickListener = new OnClickListener() {

			public void onClick(View view) {

				View convertView = view;
				ViewHolder viewHolder = (ViewHolder) convertView.getTag();
				int id = Integer.parseInt(viewHolder.sb_shot_id.getText()
						.toString());

				int pos = getPosById(id);

				setSelectedPosition(pos);
				if (mOnItemClickListener != null) {
					mOnItemClickListener.onItemClick(view, selectedPos);
				}
			}
		};

		onLongClickListener = new OnLongClickListener() {

			public boolean onLongClick(View view) {

				View convertView = view;
				ViewHolder viewHolder = (ViewHolder) convertView.getTag();
				int pos = getPosById(Integer.parseInt(viewHolder.sb_shot_id
						.getText().toString()));

				setSelectedPosition(pos);
				if (mOnLongItemClickListener != null) {
					mOnLongItemClickListener.onLongItemClick(view, selectedPos);
				}
				return false;
			}
		};
	}

	private int getPosById(int tag) {
		for (int i = 0; i < mListData.size(); i++) {
			if (mListData.get(i).getShot_id() == tag) {
				return i;
			}

		}
		return 0;

	}

	/**
	 * 设置选中的position,并通知列表刷新
	 */
	public void setSelectedPosition(int pos) {
		if (mListData != null && pos <= mListData.size()) {
			selectedPos = pos;
			selectedTag = mListData.get(pos).getShot_id();

			notifyDataSetChanged();
		}

	}

	/**
	 * 设置选中的position,但不通知刷新
	 */
	public void setSelectedPositionNoNotify(int pos) {
		selectedPos = pos;
		if (mListData != null && pos < mListData.size()) {
			selectedTag = mListData.get(pos).getShot_id();
		}
	}

	/**
	 * 获取选中的position
	 */
	public int getSelectedPosition() {

		if (mListData != null && selectedPos < mListData.size()) {
			return selectedPos;
		}

		return -1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.listview_storyboard_shot,
					null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			// resetViewHolder(holder);
		}

		String shot_name = mListData.get(position).getShot_name();
		String shot_number = mListData.get(position).getShot_number()
				.toString();

		String shot_id = mListData.get(position).getShot_id().toString();

		String shots = mListData.get(position).getShots();
		int status = mListData.get(position).getStatu();
		holder.sb_shot_id.setText(shot_id + "");

		holder.sb_shot_name.setText(shot_name);
		holder.sb_shot_number_str.setText("第" + shot_number + "镜");
		holder.sb_shot_number.setText(shot_number);

		if (status == -1) {
			holder.sb_shot_status.setText("状态:" + "未完成");

		} else if (status == 1) {
			holder.sb_shot_status.setText("状态:" + "完成");

		}

		holder.sb_shot_shots.setText("(景别:" + shots + ")");
		if (selectedTag != -1 && selectedTag == Integer.parseInt(shot_id)) {

			convertView.setBackgroundDrawable(selectedDrawble);// 设置选中的背景图片
		} else {
			convertView.setBackgroundDrawable(mContext.getResources()
					.getDrawable(normalDrawbleId));// 设置未选中状态背景图片
		}
		convertView.setPadding(20, 0, 0, 0);
		convertView.setOnClickListener(onClickListener);
		convertView.setOnLongClickListener(onLongClickListener);
		return convertView;
	}

	class ViewHolder {
		TextView sb_shot_id;
		TextView sb_shot_name;

		TextView sb_shot_number;
		TextView sb_shot_shots;
		TextView sb_shot_number_str;
		TextView sb_shot_status;

		public ViewHolder(View view) {
			sb_shot_id = (TextView) view.findViewById(R.id.sb_shot_id);
			sb_shot_name = (TextView) view.findViewById(R.id.sb_shot_name);
			sb_shot_number = (TextView) view.findViewById(R.id.sb_shot_number);
			sb_shot_shots = (TextView) view.findViewById(R.id.sb_shot_shots);
			sb_shot_number_str = (TextView) view
					.findViewById(R.id.sb_shot_number_str);
			sb_shot_status = (TextView) view.findViewById(R.id.sb_shot_status);

		}
	}

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public void setOnLongItemClickListener(OnLongItemClickListener l) {
		mOnLongItemClickListener = l;
	}

	/**
	 * 重新定义菜单选项单击接口
	 */
	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}

	public void remove(int shot_id) {

		mListData.remove(getRemovePos(mListData, shot_id));
		// 这里同步

		BackupListData.remove(getRemovePos(BackupListData, shot_id));

		// 记得刷新
		notifyDataSetChanged();

	}

	private int getRemovePos(List<Shot> list, int shot_id) {

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getShot_id() == shot_id) {

				return i;
			}

		}

		return 0;

	}

}
