package hhm.slate.activity.storyboard.view.util;

import hhm.slate.db.entity.Scene;
import hhm.slate.db.impl.SceneImpl;

import java.util.List;

import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

public class DisRecorder {

	private Context context;

	private List<Scene> list;

	public DisRecorder(Context context, List<Scene> list) {
		this.context = context;
		this.list = list;
	}

	public void save(Double current_latitude, Double current_longitude) {
		SDKInitializer.initialize(context);
		LatLng current_latlng = new LatLng(current_latitude, current_longitude);
		SceneImpl sceneImpl = new SceneImpl(context);

		for (int i = 0; i < list.size(); i++) {
			String pos = list.get(i).getScene_pos();

			if (pos != null) {

				String[] arr_pos = pos.split("#");
				double lat = Double.valueOf(arr_pos[1]);
				double lng = Double.valueOf(arr_pos[2]);
				LatLng latlng = new LatLng(lat, lng);

				double distance = DistanceUtil.getDistance(current_latlng,
						latlng);

				Scene scene = new Scene();
				scene.setScene_id(list.get(i).getScene_id());
				scene.setDistance(String.valueOf(distance));
				sceneImpl.updateAllDistance(scene);

			}
		}

	}

}
