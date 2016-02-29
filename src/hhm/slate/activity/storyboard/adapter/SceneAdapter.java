package hhm.slate.activity.storyboard.adapter;

import hhm.slate.R;
import hhm.slate.activity.storyboard.view.util.DisRecorder;
import hhm.slate.db.entity.Scene;
import hhm.slate.db.impl.SceneImpl;
import hhm.slate.util.DataUtil;

import java.util.ArrayList;
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

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

public class SceneAdapter extends ArrayAdapter<Scene> {

	private Context mContext;
	private List<Scene> mListData;
	private List<Scene> BackupListData;

	private int selectedPos = -1;
	private int selectedTag = -1;
	private int normalDrawbleId;
	private Drawable selectedDrawble;
	private OnClickListener onClickListener;
	private OnLongClickListener onLongClickListener;
	private OnLongItemClickListener mOnLongItemClickListener;

	private OnItemClickListener mOnItemClickListener;
	private LayoutInflater mInflater;

	public void Search(String value) {
		mListData.clear();
		if (value.equals("")) {
			// 如果为空，那就还原
			for (int i = 0; i < BackupListData.size(); i++) {
				mListData.add(BackupListData.get(i));
			}
			notifyDataSetChanged();
			return;
		}

		for (int i = 0; i < BackupListData.size(); i++) {
			if (BackupListData.get(i).getScene_name().contains(value)) {
				mListData.add(BackupListData.get(i));

			}

		}

		notifyDataSetChanged();

	}

	// 同步数据
	public void syncBackupData(List<Scene> listData) {
		BackupListData = new ArrayList<Scene>();
		BackupListData.clear();
		for (int i = 0; i < listData.size(); i++) {
			BackupListData.add(listData.get(i));

		}
	}

	public SceneAdapter(Context context, List<Scene> listData,
			int selected_scene_pos) {
		super(context, R.string.no_data, listData);

		this.mInflater = LayoutInflater.from(context);
		mContext = context;

		this.mListData = listData;

		// 同步数据
		syncBackupData(listData);

		selectedDrawble = mContext.getResources().getDrawable(
				R.drawable.choose_item);
		normalDrawbleId = R.drawable.choose_eara_item_selector;

		init();
		if (selected_scene_pos != 0) {

			int pos = getPosById(selected_scene_pos);

			setSelectedPosition(pos);
		}
	}

	/**
	 * 加载距离
	 */
	public void LoadDistance(Context ct, Double current_latitude,
			Double current_longitude) {
		LatLng current_latlng = new LatLng(current_latitude, current_longitude);
		DisRecorder dr = new DisRecorder(ct, mListData);
		dr.save(current_latitude, current_longitude);

		// 不要忘了还要在本地内存刷新数据，所以要循环再刷新一次
		for (int i = 0; i < mListData.size(); i++) {
			String pos = mListData.get(i).getScene_pos();

			if (pos != null) {
				String[] arr_pos = pos.split("#");
				double lat = Double.valueOf(arr_pos[1]);
				double lng = Double.valueOf(arr_pos[2]);
				LatLng latlng = new LatLng(lat, lng);
				double distance = DistanceUtil.getDistance(current_latlng,
						latlng);
				mListData.get(i).setDistance(String.valueOf(distance));
			}

		}

		notifyDataSetChanged();

	}

	public void remove(int scene_id) {

		// 通过scene_id来获取位置，进而删除数据
		mListData.remove(getRemovePos(mListData, scene_id));

		// 这里同步

		BackupListData.remove(getRemovePos(BackupListData, scene_id));
		// 记得刷新
		notifyDataSetChanged();

	}

	private int getRemovePos(List<Scene> list, int scene_id) {

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getScene_id() == scene_id) {

				return i;
			}

		}

		return 0;

	}

	/**
	 * 通过id得到位置，辅助删除列表功能
	 * 
	 * @param tag
	 * @return
	 */
	private int getPosById(int tag) {
		for (int i = 0; i < mListData.size(); i++) {
			if (mListData.get(i).getScene_id() == tag) {
				return i;
			}

		}
		return 0;

	}

	private void init() {
		onClickListener = new OnClickListener() {

			public void onClick(View view) {
				View convertView = view;
				ViewHolder viewHolder = (ViewHolder) convertView.getTag();
				int pos = getPosById(Integer.parseInt(viewHolder.scene_id
						.getText().toString()));

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

				int pos = getPosById(Integer.parseInt(viewHolder.scene_id
						.getText().toString()));
				setSelectedPosition(pos);
				if (mOnLongItemClickListener != null) {
					mOnLongItemClickListener.onLongItemClick(view, selectedPos);
				}
				return false;
			}
		};
	}

	/**
	 * 设置选中的position,并通知列表刷新
	 */

	public void setSelectedPosition(int pos) {
		if (mListData != null) {
			selectedPos = pos;
			selectedTag = getIdByPos(pos);
			notifyDataSetChanged();
		}

	}

	private int getIdByPos(int pos) {

		return mListData.get(pos).getScene_id();

	}

	/**
	 * 设置选中的position,但不通知刷新
	 */

	public void setSelectedPositionNoNotify(int pos) {
		selectedPos = pos;
		if (mListData != null && pos <= mListData.size()) {

			selectedTag = getIdByPos(pos);

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

	static DataUtil dataUtil = new DataUtil();

	private SceneImpl sceneImpl;

	public View getView(int position, View convertView, ViewGroup parent) {

		if (mListData.size() - 1 > position) {

		}

		// 这里设置一个懒加载才行，否则太耗了
		if (sceneImpl == null) {
			sceneImpl = new SceneImpl(mContext);
		}

		ViewHolder holder;
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.listview_storyboard_scene,
					null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			// resetViewHolder(holder);
		}

		String scene_number = String.valueOf(mListData.get(position)
				.getScene_number());
		String scene_name = mListData.get(position).getScene_name().toString();
		String scene_pos = mListData.get(position).getScene_pos() + "";
		String scene_id = String.valueOf(mListData.get(position).getScene_id());
		String distance = String.valueOf(mListData.get(position).getDistance());

		holder.scene_number.setText(scene_number);
		holder.scene_name.setText("名称:" + scene_name);
		String s_distance = dataUtil.getM2(distance);

		if (s_distance.equals("")) {
			holder.scene_distance.setVisibility(View.GONE);

		} else {
			holder.scene_distance.setText("（" + s_distance + "米" + ")");
			holder.scene_distance.setVisibility(View.VISIBLE);

		}

		if (scene_pos.contains("null")) {
			holder.scene_pos.setText("拍摄地点:" + "未设置");

		} else {
			holder.scene_pos.setText("拍摄地点:" + scene_pos.split("#")[0]);

		}

		// 通过scene_id，获取未完成和已经完成的数据
		int uncompCount = sceneImpl.getUncompCount(Integer.parseInt(scene_id));

		int Count = sceneImpl.getCountById(Integer.parseInt(scene_id));
		holder.uncomp_count.setText("该场完成进度:" + uncompCount + "/" + Count);
		holder.scene_id.setText(scene_id);

		if (selectedTag != -1 && selectedTag == Integer.parseInt(scene_id)) {

			convertView.setBackgroundDrawable(selectedDrawble);// 设置选中的背景图片
		} else {

			convertView.setBackgroundDrawable(mContext.getResources()
					.getDrawable(normalDrawbleId));// 设置未选中状态背景图片
		}
		convertView.setPadding(20, 0, 0, 0);
		convertView.setOnClickListener(onClickListener);

		// 监听长按
		convertView.setOnLongClickListener(onLongClickListener);
		return convertView;
	}

	class ViewHolder {
		TextView scene_number;
		TextView scene_name;
		TextView scene_pos;
		TextView scene_distance;
		TextView uncomp_count;
		TextView scene_id;

		public ViewHolder(View view) {
			scene_number = (TextView) view
					.findViewById(R.id.tv_sb_scene_number);
			scene_name = (TextView) view.findViewById(R.id.tv_sb_scene_name);
			scene_pos = (TextView) view.findViewById(R.id.tv_sb_scene_pos);
			scene_distance = (TextView) view
					.findViewById(R.id.tv_sb_scene_distance);
			uncomp_count = (TextView) view
					.findViewById(R.id.tv_sb_uncomp_count);
			scene_id = (TextView) view.findViewById(R.id.tv_sb_scene_id);

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

	public interface OnLongItemClickListener {
		public void onLongItemClick(View view, int position);
	}

}
