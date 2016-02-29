package hhm.slate.activity.storyboard;

import java.io.File;

import hhm.slate.db.ConfigImpl;
import hhm.slate.prefer.PreferencesService;
import hhm.slate.util.FileUtil;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class InputDatabaseActivity extends Activity {
	private PreferencesService preferencesService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Uri uri = (Uri) intent.getData();
		String path = uri.getPath();

		File file = new File(path);

		// 开始把文件复制到两个地方,然后设置数据库
		FileUtil fu = new FileUtil();
		fu.synchronizeDB(path, file.getName());

		ConfigImpl.setNowDBName(file.getName());
		// 设置偏好
		preferencesService = new PreferencesService(getApplicationContext());
		preferencesService.saveNowDBName(file.getName());

		Intent intent1 = new Intent();
		intent1.setClass(getApplicationContext(), StoryBoardActivity.class);
		startActivity(intent1);

	}
}
