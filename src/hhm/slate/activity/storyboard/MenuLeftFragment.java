package hhm.slate.activity.storyboard;

import hhm.slate.R;
import hhm.slate.activity.storyboard.leftmenu.AddFilmActivity;
import hhm.slate.activity.storyboard.leftmenu.DIYSlateActivity;
import hhm.slate.activity.storyboard.leftmenu.FunctionIntroActivity;
import hhm.slate.activity.storyboard.leftmenu.WorkVersionSelector;
import hhm.slate.db.entity.Film;
import hhm.slate.db.entity.Scene;
import hhm.slate.db.impl.FilmImpl;
import hhm.slate.db.impl.SceneImpl;
import hhm.slate.db.impl.ShotImpl;
import hhm.slate.util.ExcelUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class MenuLeftFragment extends Fragment {
	private static final int FILE_SELECT_CODE = 0;

	private int film_id;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_left, null);
		final ListView list = (ListView) view
				.findViewById(R.id.LV_FunctionMenu_Left);

		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ItemImage", R.drawable.left_menu_addsb);
		map1.put("ItemTitle",
				getResources().getString(R.string.left_memu_text_1));
		listItem.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ItemImage", R.drawable.left_menu_excel);
		map2.put("ItemTitle",
				getResources().getString(R.string.left_memu_text_2));
		listItem.add(map2);

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("ItemImage", R.drawable.left_menu_update);
		map3.put("ItemTitle",
				getResources().getString(R.string.left_memu_text_3));
		listItem.add(map3);

		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("ItemImage", R.drawable.left_menu_version);
		map4.put("ItemTitle",
				getResources().getString(R.string.left_memu_text_4));
		listItem.add(map4);

		HashMap<String, Object> map5 = new HashMap<String, Object>();
		map5.put("ItemImage", R.drawable.left_menu_gallery);
		map5.put("ItemTitle",
				getResources().getString(R.string.left_memu_text_5));
		listItem.add(map5);

		HashMap<String, Object> map6 = new HashMap<String, Object>();
		map6.put("ItemImage", R.drawable.left_menu_aboutme);
		map6.put("ItemTitle",
				getResources().getString(R.string.left_memu_text_6));
		listItem.add(map6);

		HashMap<String, Object> map7 = new HashMap<String, Object>();
		map7.put("ItemImage", R.drawable.left_menu_slate);
		map7.put("ItemTitle",
				getResources().getString(R.string.left_memu_text_7));
		listItem.add(map7);

		HashMap<String, Object> map8 = new HashMap<String, Object>();
		map8.put("ItemImage", R.drawable.left_menu_cancel);
		map8.put("ItemTitle",
				getResources().getString(R.string.left_memu_text_8));
		listItem.add(map8);

		SimpleAdapter listItemAdapter = new SimpleAdapter(this.getActivity(),
				listItem,

				R.layout.listview_menu_left, new String[] { "ItemImage",
						"ItemTitle" }, new int[] { R.id.ItemImage,
						R.id.ItemTitle });

		list.setAdapter(listItemAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {

				// 调转到设置页面
				if (index == 0) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), AddFilmActivity.class);
					startActivity(intent);
				} else if (index == 1) {
					// 弹出对话框，要求选择
					final AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setTitle("请选择导入以下哪个剧本");
					// (R.drawable.ic_launcher);
					FilmImpl filmImpl = new FilmImpl(getActivity());
					Film film = filmImpl.queryArr();
					final int arr_id[] = film.getFilmid();

					builder.setItems(film.getFilmname(),
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface arg0,
										int index) {

									film_id = arr_id[index];
									showFileChooser();

								}

							});

					builder.show();

				} else if (index == 2) {
					Toast.makeText(getActivity(), "亲，请到应用市场关注最新版本哦", 3000)
							.show();
				} else if (index == 3) {

					Intent intent = new Intent();
					intent.setClass(getActivity(), WorkVersionSelector.class);

					startActivity(intent);
				} else if (index == 4) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), FunctionIntroActivity.class);

					startActivity(intent);
				} else if (index == 5) {

					OpenAboutMeDialog();

				} else if (index == 6) {

					Intent intent = new Intent();
					intent.setClass(getActivity(), DIYSlateActivity.class);
					startActivity(intent);

				} else if (index == 7) {
					exit();

				}
			}

		});

		return view;

	}

	private void OpenAboutMeDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		alert.setTitle("亲，感谢你使用该产品");
		alert.setMessage("如果该软件未能达到你的要求，以及bug的反馈，请您多多提提意见，我的邮箱是1585685035@qq.com");
		alert.show();
	}

	private void exit() {
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		alert.setTitle("提示");
		alert.setMessage("你要退出?");

		alert.setPositiveButton("退出", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				System.exit(0);
			}

		});

		alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}

		});

		alert.show();
	}

	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		try {
			startActivityForResult(
					Intent.createChooser(intent, "Select a File to Upload"),
					FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getActivity(), "Please install a File Manager.",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case FILE_SELECT_CODE:
			if (resultCode == getActivity().RESULT_OK) {
				Uri uri = data.getData();

				String file_path = FileUtils.getPath(getActivity(), uri);

				ExcelUtil eu = new ExcelUtil(getActivity());
				List<Scene> scene_list = eu.Input(file_path);

				for (int i = 0; i < scene_list.size(); i++) {

					scene_list.get(i).setFilm_id(film_id);
					SceneImpl sceneImpl = new SceneImpl(getActivity());
					sceneImpl.save(scene_list.get(i));

					ShotImpl shotImpl = new ShotImpl(getActivity());
					shotImpl.save_Input(scene_list.get(i).getShotList());

				}

			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static class FileUtils {
		public static String getPath(Context context, Uri uri) {

			if ("content".equalsIgnoreCase(uri.getScheme())) {
				String[] projection = { "_data" };
				Cursor cursor = null;

				try {
					cursor = context.getContentResolver().query(uri,
							projection, null, null, null);
					int column_index = cursor.getColumnIndexOrThrow("_data");
					if (cursor.moveToFirst()) {
						return cursor.getString(column_index);
					}
				} catch (Exception e) {
					// Eat it
				}
			}

			else if ("file".equalsIgnoreCase(uri.getScheme())) {
				return uri.getPath();
			}

			return null;
		}
	}

}
