package hhm.slate.prefer;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesService {
	private Context context;

	public PreferencesService(Context context) {
		this.context = context;
	}

	public void saveNowDBName(String name) {
		// 获得SharedPreferences对象
		SharedPreferences preferences = context.getSharedPreferences(
				"nowDBName", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("nowDBName", name);
		editor.commit();
	}

	/**
	 * 获取各项参数
	 * 
	 * @return
	 */
	public Map<String, String> getNowDBNamePerferences() {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences(
				"nowDBName", Context.MODE_PRIVATE);
		params.put("nowDBName", preferences.getString("nowDBName", ""));

		return params;
	}

}
