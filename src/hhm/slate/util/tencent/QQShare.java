package hhm.slate.util.tencent;

import android.app.Activity;
import android.os.Bundle;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQShare {

	public void doShareToQQ(Activity activity, Tencent tencent, Bundle bundle) {

		tencent.shareToQQ(activity, bundle, new IUiListener() {

			public void onCancel() {

			}

			public void onComplete(Object arg0) {

			}

			public void onError(UiError arg0) {

			}

		});
	}

}
