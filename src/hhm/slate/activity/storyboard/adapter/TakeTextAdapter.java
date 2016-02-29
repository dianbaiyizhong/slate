package hhm.slate.activity.storyboard.adapter;

import hhm.slate.R;
import hhm.slate.db.entity.Take;
import hhm.slate.util.PathUtil;
import hhm.slate.util.TimeUtil;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TakeTextAdapter extends ArrayAdapter<Take> {

	private Context mContext;
	private List<Take> mListData;
	private int selectedPos = -1;
	private int selectedTag = -1;
	private int normalDrawbleId;
	private Drawable selectedDrawble;
	private float textSize;
	private OnClickListener onClickListener;
	private OnItemClickListener mOnItemClickListener;

	private OnLongClickListener onLongClickListener;
	private OnLongItemClickListener mOnLongItemClickListener;
	private LayoutInflater mInflater;

	public TakeTextAdapter(Context context, List<Take> listData,
			int selected_take_pos) {

		super(context, R.string.no_data, listData);
		this.mInflater = LayoutInflater.from(context);
		mContext = context;
		this.mListData = listData;
		selectedDrawble = mContext.getResources().getDrawable(
				R.drawable.choose_item);
		normalDrawbleId = R.drawable.choose_plate_item_selector;
		init();
		if (selected_take_pos != 0) {
			int pos = getPosById(selected_take_pos);

			setSelectedPosition(pos);
		}

	}

	public void remove(int take_id) {

		// 通过scene_id来获取位置，进而删除数据
		mListData.remove(getRemovePos(take_id));

		// 记得刷新
		notifyDataSetChanged();

	}

	private int getRemovePos(int take_id) {

		for (int i = 0; i < mListData.size(); i++) {
			if (mListData.get(i).getTake_id() == take_id) {

				System.out.println("返回的位置的序号为" + i);
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

				// 从点击的take_id获取位置先
				int pos = getPosById(Integer.parseInt(viewHolder.tv_take_id
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
				int pos = getPosById(Integer.parseInt(viewHolder.tv_take_id
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
	 * 通过id得到位置，辅助删除列表功能
	 * 
	 * @param tag
	 * @return
	 */
	private int getPosById(int tag) {
		for (int i = 0; i < mListData.size(); i++) {
			if (mListData.get(i).getTake_id() == tag) {
				return i;
			}

		}
		return 0;

	}

	private int getIdByPos(int pos) {

		return mListData.get(pos).getTake_id();

	}

	/**
	 * 设置选中的position,并通知列表刷新
	 */

	public void setSelectedPosition(int pos) {
		if (mListData != null && pos <= mListData.size()) {
			selectedPos = pos;
			selectedTag = getIdByPos(pos);
			notifyDataSetChanged();
		}

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

	/**
	 * 设置列表字体大小
	 */
	public void setTextSize(float tSize) {
		textSize = tSize;
	}

	static TimeUtil timeUtil = new TimeUtil();

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.listview_storyboard_take,
					null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			// resetViewHolder(holder);
		}

		String take_number = mListData.get(position).getTake_number()
				.toString();

		String take_time = timeUtil.getTime(mListData.get(position)
				.getTake_time());
		String take_id = mListData.get(position).getTake_id().toString();
		String take_image = mListData.get(position).getTake_image().toString();
		holder.tv_take_number.setText("第" + take_number + "次");

		holder.tv_take_time.setText(take_time);

		holder.tv_take_id.setText(take_id);

		if (!take_image.equals("default.jpg")) {
			holder.iv_take_image.setImageBitmap(BitmapFactory
					.decodeFile(new PathUtil().getStagePhotoPath()
							+ mListData.get(position).getTake_image()
									.toString()));
		} else {
			holder.iv_take_image.setImageResource(R.drawable.default_no_image);
		}

		if (selectedTag != -1 && selectedTag == Integer.parseInt(take_id)) {
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
		TextView tv_take_number;
		TextView tv_take_time;
		TextView tv_take_id;
		ImageView iv_take_image;

		public ViewHolder(View view) {
			tv_take_number = (TextView) view.findViewById(R.id.sb_take_number);
			tv_take_time = (TextView) view.findViewById(R.id.sb_take_time);
			tv_take_id = (TextView) view.findViewById(R.id.sb_take_id);
			iv_take_image = (ImageView) view.findViewById(R.id.sb_take_image);

		}
	}

	protected void resetViewHolder(ViewHolder viewHolder) {
		viewHolder.tv_take_number.setText(null);
		viewHolder.tv_take_time.setText(null);
		viewHolder.tv_take_id.setText(null);
		viewHolder.iv_take_image.setImageDrawable(null);
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
