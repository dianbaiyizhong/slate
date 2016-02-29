package hhm.slate.activity.storyboard.dialog;

import hhm.slate.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class AlertDialog extends Dialog {
	TextView tv_dialog_alert_info;

	private String info;

	public AlertDialog(Context context, int theme, String info) {
		super(context, theme);
		this.info = info;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_alert);
		tv_dialog_alert_info = (TextView) findViewById(R.id.tv_dialog_alert_info);
		tv_dialog_alert_info.setText(info);
	}
}
