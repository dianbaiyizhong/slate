package hhm.slate.util;

import java.io.File;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;

public class ShareFileThread extends Thread {

	private String filename;

	private Activity activity;

	private String app;

	public ShareFileThread(Activity activity, String filename, String app) {

		this.activity = activity;

		this.filename = filename;

		this.app = app;
	}

	@Override
	public void run() {
		ShareQQFile();
	}

	public void ShareQQFile() {

		File file = new File(new PathUtil().getExcelDefaultPath() + filename
				+ ".xls");

		Intent share = new Intent(Intent.ACTION_SEND);
		ComponentName component = null;
		if (app.equals("QQ")) {
			new ComponentName("com.tencent.mobileqq",
					"com.tencent.mobileqq.activity.JumpActivity");
		} else if (app.equals("")) {

		}

		share.setComponent(component);

		share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		share.setType("*/*");
		activity.startActivity(Intent.createChooser(share, "发送"));
	}

}
