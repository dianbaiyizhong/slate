package hhm.slate.activity.storyboard.popwindow.dropdowm.sceneinfo;

import hhm.slate.R;
import hhm.slate.activity.storyboard.popwindow.dropdowm.sceneinfo.AbstractSceneInfoSpinerAdapter.ISceneInfoOnItemSelectListener;
import hhm.slate.db.entity.Scene;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

public class SpinerSceneInfoPopWindow extends PopupWindow implements
		OnItemClickListener {

	private Context mContext;
	private ListView mListView;
	private NormalSceneInfoSpinerAdapter mAdapter;
	private ISceneInfoOnItemSelectListener mItemSelectListener;

	public SpinerSceneInfoPopWindow(Context context) {
		super(context);

		mContext = context;
		init();
	}

	public void setItemListener(ISceneInfoOnItemSelectListener listener) {
		mItemSelectListener = listener;
	}

	private void init() {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.dropdown_sceneinfo, null);
		setContentView(view);
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);

		setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00);
		setBackgroundDrawable(dw);

		mListView = (ListView) view.findViewById(R.id.LV_addshot_scene_name);

		mAdapter = new NormalSceneInfoSpinerAdapter(mContext);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	public void refreshData(List<Scene> list, int selIndex) {
		if (list != null && selIndex != -1) {
			mAdapter.refreshData(list, selIndex);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
		dismiss();
		if (mItemSelectListener != null) {
			mItemSelectListener.onSceneInfoItemClick(pos);
		}
	}

}
