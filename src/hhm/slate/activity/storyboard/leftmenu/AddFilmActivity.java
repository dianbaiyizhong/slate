package hhm.slate.activity.storyboard.leftmenu;

import hhm.slate.R;
import hhm.slate.activity.storyboard.ActivityBase;
import hhm.slate.activity.storyboard.leftmenu.adapter.PhoneAdapter;
import hhm.slate.activity.storyboard.leftmenu.entity.Contact;
import hhm.slate.db.entity.Film;
import hhm.slate.db.entity.Staff;
import hhm.slate.db.impl.FilmImpl;
import hhm.slate.util.Call;
import hhm.slate.util.VerifyUtil;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AddFilmActivity extends Activity implements ActivityBase {
	Call call = new Call();

	Button btn_ss_more_info;
	LinearLayout et_ll_ss_more_info;

	Button btn_addShootingScript;

	EditText et_add_film_name;
	AutoCompleteTextView et_add_director;
	AutoCompleteTextView et_add_assistant_director;
	AutoCompleteTextView et_add_camera;
	AutoCompleteTextView et_add_lighting;
	AutoCompleteTextView et_add_production;

	boolean currentStatu = false;
	LinearLayout ll_ss_addform;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addfilm);

		// 下面两句代码是为了解决.9图片后，控件溢出的问题
		ll_ss_addform = (LinearLayout) findViewById(R.id.ll_ss_addform);
		ll_ss_addform.setPadding(0, 0, 0, 0);

		LoadWidget();

		LoadWidgetlistener();

	}

	private void saveFilm() {

		// 验证
		if (!verify()) {
			return;
		}

		FilmImpl filmImpl = new FilmImpl(this);
		String film_name = et_add_film_name.getText().toString();
		String director = et_add_director.getText().toString();
		String assistant_director = et_add_assistant_director.getText()
				.toString();
		String camera = et_add_camera.getText().toString();
		String lighting = et_add_lighting.getText().toString();
		String production = et_add_production.getText().toString();
		// 存入数据库
		Film film = new Film();
		film.setFilm_name(film_name);
		Staff staff = new Staff();
		staff.setDirector(director);
		staff.setAssistant_director(assistant_director);
		staff.setCamera(camera);
		staff.setLighting(lighting);
		staff.setProduction(production);

		film.setStaff(staff);
		filmImpl.save(film);

		// 添加后，调回那个主页
		finish();

	}

	private boolean verify() {

		List<EditText> list = new ArrayList<EditText>();
		list.add(et_add_film_name);

		boolean bool = new VerifyUtil().verifyIsNull(getApplicationContext(),
				list);

		if (bool) {
			return true;

		} else {
			return false;
		}

	}

	@Override
	public void LoadWidget() {
		btn_ss_more_info = (Button) findViewById(R.id.btn_ss_more_info);
		et_ll_ss_more_info = (LinearLayout) findViewById(R.id.et_ll_ss_more_info);
		btn_addShootingScript = (Button) findViewById(R.id.btn_addShootingScript);
		et_add_film_name = (EditText) findViewById(R.id.et_add_film_name);
		et_add_director = (AutoCompleteTextView) findViewById(R.id.et_add_director);
		et_add_assistant_director = (AutoCompleteTextView) findViewById(R.id.et_add_assistant_director);
		et_add_camera = (AutoCompleteTextView) findViewById(R.id.et_add_camera);
		et_add_lighting = (AutoCompleteTextView) findViewById(R.id.et_add_lighting);
		et_add_production = (AutoCompleteTextView) findViewById(R.id.et_add_production);
	}

	class Listener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View view, boolean bool) {
			if (ContactList == null) {

				buildContactData();
			}
			if (bool) {
				FindContactData((AutoCompleteTextView) view);
			}

		}

	}

	@Override
	public void LoadWidgetValue() {

	}

	@Override
	public void LoadWidgetlistener() {

		et_add_director.setOnFocusChangeListener(new Listener());
		et_add_assistant_director.setOnFocusChangeListener(new Listener());
		et_add_camera.setOnFocusChangeListener(new Listener());
		et_add_lighting.setOnFocusChangeListener(new Listener());
		et_add_production.setOnFocusChangeListener(new Listener());

		btn_ss_more_info.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// 在这里写点击显示隐藏
				if (currentStatu == true) {
					// 那就说明，状态已经展开，这个时候要隐藏
					et_ll_ss_more_info.setVisibility(View.INVISIBLE);
					currentStatu = false;

				} else {
					et_ll_ss_more_info.setVisibility(View.VISIBLE);
					currentStatu = true;
				}

			}
		});

		btn_addShootingScript.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				saveFilm();

			}

		});

	}

	List<Contact> ContactList;

	private void buildContactData() {
		List<Contact> cantactlist = call
				.getPhoneContacts(getApplicationContext());
		String[] phonenumber = new String[cantactlist.size()];
		for (int i = 0; i < cantactlist.size(); i++) {
			phonenumber[i] = cantactlist.get(i).getmContactsNumber();
		}

		ContactList = new ArrayList<Contact>();
		for (int i = 0; i < phonenumber.length; i++) {
			Contact contact = new Contact();
			contact.setmContactsName(cantactlist.get(i).getmContactsName());
			contact.setmContactsNumber(cantactlist.get(i).getmContactsNumber());
			contact.setmContactsPhonto(cantactlist.get(i).getmContactsPhonto());
			ContactList.add(contact);
		}

	}

	private void FindContactData(AutoCompleteTextView actv) {
		PhoneAdapter mAdapter = new PhoneAdapter(ContactList,
				getApplicationContext());
		actv.setAdapter(mAdapter);
		actv.setThreshold(1); // 设置输入一个字符 提示，默认为2

		actv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {

			}
		});

	}

	public void finish(View view) {
		finish();
	}

}
