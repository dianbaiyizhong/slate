package hhm.slate.activity.storyboard.leftmenu.entity;

import android.graphics.Bitmap;

public class Contact {
	public String getmContactsName() {
		return mContactsName;
	}

	public void setmContactsName(String mContactsName) {
		this.mContactsName = mContactsName;
	}

	public String getmContactsNumber() {
		return mContactsNumber;
	}

	public void setmContactsNumber(String mContactsNumber) {
		this.mContactsNumber = mContactsNumber;
	}

	public Bitmap getmContactsPhonto() {
		return mContactsPhonto;
	}

	public void setmContactsPhonto(Bitmap mContactsPhonto) {
		this.mContactsPhonto = mContactsPhonto;
	}

	private String mContactsName;
	private String mContactsNumber;
	private Bitmap mContactsPhonto;

}
