package hhm.slate.util.tencent;

import com.tencent.tauth.Tencent;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ShareThread extends Thread {

	private Activity activity;

	private Tencent tencent;
	private Bundle bundle;

	public ShareThread(Tencent tencent, Activity activity, Bundle bundle) {

		this.activity = activity;

		this.tencent = tencent;

		this.bundle = bundle;
	}

	@Override
	public void run() {
		QQShare qqShare = new QQShare();
		qqShare.doShareToQQ(activity, tencent, bundle);
		Message msg = shareHandler.obtainMessage();

		// 将Message对象加入到消息队列当中
		shareHandler.sendMessage(msg);

	}

	Handler shareHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
		}
	};

}
