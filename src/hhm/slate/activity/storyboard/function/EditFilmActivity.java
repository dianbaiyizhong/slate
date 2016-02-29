package hhm.slate.activity.storyboard.function;

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

public class EditFilmActivity extends Activity implements ActivityBase {
	Call call = new Call();
	Button btn_editShootingScript;
	EditText et_edit_film_name;
	AutoCompleteTextView et_edit_director;
	AutoCompleteTextView et_edit_assistant_director;
	AutoCompleteTextView et_edit_camera;
	AutoCompleteTextView et_edit_lighting;
	AutoCompleteTextView et_edit_production;
	List<Contact> ContactList;

	private int film_id;
	public void finish(View view) {
		finish();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editfilm);

		LoadWidget();
		LoadWidgetlistener();
		Bundle bundle = this.getIntent().getExtras();
		String film_id = bundle.getString("film_id");
		this.film_id = Integer.parseInt(film_id);

		FilmImpl filmImpl = new FilmImpl(getApplicationContext());
		Film film = filmImpl.queryByid(film_id);

		et_edit_film_name.setText(film.getFilm_name());
		et_edit_director.setText(film.getStaff().getDirector());
		et_edit_assistant_director.setText(film.getStaff()
				.getAssistant_director());
		et_edit_camera.setText(film.getStaff().getCamera());
		et_edit_lighting.setText(film.getStaff().getLighting());
		et_edit_production.setText(film.getStaff().getProduction());

	}

	@Override
	public void LoadWidget() {

		btn_editShootingScript = (Button) findViewById(R.id.btn_editShootingScript);
		et_edit_film_name = (EditText) findViewById(R.id.et_edit_film_name);
		et_edit_director = (AutoCompleteTextView) findViewById(R.id.et_edit_director);
		et_edit_assistant_director = (AutoCompleteTextView) findViewById(R.id.et_edit_assistant_director);
		et_edit_camera = (AutoCompleteTextView) findViewById(R.id.et_edit_camera);
		et_edit_lighting = (AutoCompleteTextView) findViewById(R.id.et_edit_lighting);
		et_edit_production = (AutoCompleteTextView) findViewById(R.id.et_edit_production);

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

		et_edit_director.setOnFocusChangeListener(new Listener());
		et_edit_assistant_director.setOnFocusChangeListener(new Listener());
		et_edit_camera.setOnFocusChangeListener(new Listener());
		et_edit_lighting.setOnFocusChangeListener(new Listener());
		et_edit_production.setOnFocusChangeListener(new Listener());

		btn_editShootingScript.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// 验证
				if (!verify()) {
					return;
				}

				FilmImpl filmImpl = new FilmImpl(getApplicationContext());
				String film_name = et_edit_film_name.getText().toString();
				String director = et_edit_director.getText().toString();
				String assistant_director = et_edit_assistant_director
						.getText().toString();
				String camera = et_edit_camera.getText().toString();
				String lighting = et_edit_lighting.getText().toString();
				String production = et_edit_production.getText().toString();
				// 存入数据库
				Film film = new Film();
				film.setFilm_id(film_id);
				film.setFilm_name(film_name);
				Staff staff = new Staff();
				staff.setDirector(director);
				staff.setAssistant_director(assistant_director);
				staff.setCamera(camera);
				staff.setLighting(lighting);
				staff.setProduction(production);

				film.setStaff(staff);
				filmImpl.update(film);

				// 添加后，调回那个主页
				finish();

			}
		});
	}

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

	private boolean verify() {

		List<EditText> list = new ArrayList<EditText>();
		list.add(et_edit_film_name);

		boolean bool = new VerifyUtil().verifyIsNull(getApplicationContext(),
				list);

		if (bool) {
			return true;

		} else {
			return false;
		}

	}

}
