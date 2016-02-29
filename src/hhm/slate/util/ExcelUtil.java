package hhm.slate.util;

import hhm.slate.db.entity.Scene;
import hhm.slate.db.entity.Shot;
import hhm.slate.db.entity.Take;
import hhm.slate.db.impl.SceneImpl;
import hhm.slate.db.impl.ShotImpl;
import hhm.slate.db.impl.TakeImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.content.Context;

public class ExcelUtil {

	// scene所在的位置
	private int scene_pos = 0;
	// shot所在的位置
	private int shot_pos = 1;
	// take所在的位置
	private int take_pos = 2;

	private int video_number_pos = 3;
	private int audio_number_pos = 4;
	private int take_time_pos = 5;
	private int is_available_pos = 6;
	private int not_avaliable_season_pos = 7;
	private int roll_name_pos = 8;

	private int initRowPos = 1;

	private Context mContent;

	public ExcelUtil(Context content) {

		this.mContent = content;

	}

	public List<Scene> Input(String path) {
		// 创建一个list 用来存储读取的内容
		List list = new ArrayList();
		Workbook rwb = null;
		Cell cell = null;

		// 创建输入流
		InputStream stream = null;
		try {
			stream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// 获取Excel文件对象
		try {
			rwb = Workbook.getWorkbook(stream);
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 获取文件的指定工作表 默认的第一个
		Sheet sheet = rwb.getSheet(0);

		List<Scene> scene_list = new ArrayList<Scene>();
		List<Shot> shot_list = new ArrayList<Shot>();

		for (int i = 1; i < sheet.getRows(); i++) {

			// 这里将会获取scene_name
			String scene_name = sheet.getCell(0, i).getContents();
			Scene scene = new Scene();

			String shot_name = sheet.getCell(1, i).getContents();
			String shots = sheet.getCell(2, i).getContents();
			Shot shot = new Shot();
			shot.setShot_name(shot_name);
			shot.setShots(shots);
			// 如果不等于空，证明是新的场景
			if (!scene_name.equals("")) {
				shot_list = new ArrayList<Shot>();

				shot.setShot_number(shot_list.size() + 1);
				shot_list.add(shot);

				scene.setScene_name(scene_name);
				scene.setScene_number(scene_list.size() + 1);

				scene.setShotList(shot_list);
				scene_list.add(scene);

			} else {
				shot.setShot_number(shot_list.size() + 1);
				shot_list.add(shot);

				scene_list.get(scene_list.size() - 1).setShotList(shot_list);
			}

		}

		return scene_list;

	}

	public String Output(String film_id) {
		WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 12,
				WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
				Colour.RED);
		PathUtil pu = new PathUtil();

		WritableWorkbook writeBook = null;
		String filename = null;
		try {

			filename = new TimeUtil().getLocalTime();
			writeBook = Workbook.createWorkbook(new File(pu
					.getExcelDefaultPath() + filename + ".xls"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		WritableCellFormat wcfF = new WritableCellFormat(font);
		WritableSheet sheet = writeBook.createSheet(film_id, 1);// 第一个参数为工作簿的名称，第二个参数为页数
		// 通过它可以指定单元格的各种属性
		// 添加边框：
		try {
			wcfF.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);
			// 添加背景色：
			wcfF.setBackground(Colour.GRAY_25);
			// 把水平对齐方式指定为居中
			wcfF.setAlignment(Alignment.CENTRE);
			// 把垂直对齐方式指定为居中
			wcfF.setVerticalAlignment(VerticalAlignment.CENTRE);
			// 设置自动换行
			wcfF.setWrap(true);
		} catch (WriteException e) {
			e.printStackTrace();
		}

		// 这个9指的是列数
		String[] colName = { "场", "镜", "次", "视频编号", "音频编号", "拍摄时间", "是否可用",
				"不可用原因", "内存卡(卷)" };
		for (int i = 0; i < 9; i++) {
			Label label_colName = new Label(i, 0, colName[i], wcfF);

			try {
				sheet.addCell(label_colName);
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}

		SceneImpl sceneImpl = new SceneImpl(mContent);
		ShotImpl shotImpl = new ShotImpl(mContent);
		TakeImpl takeImpl = new TakeImpl(mContent);
		List<Scene> scene_list = new ArrayList<Scene>();
		// 首先传入一个film_id。
		scene_list = sceneImpl.query(film_id);

		// 开始循环
		// 这里定义了一个指针，指到哪里就在哪里写
		int point_take = initRowPos;
		int point_shot = initRowPos;
		int point_scene = initRowPos;
		for (int i = 0; i < scene_list.size(); i++) {
			// 获取一个场
			int scene_id = scene_list.get(i).getScene_id();
			List<Shot> shot_list = shotImpl.query(String.valueOf(scene_id));

			// 获取次 的数量，可以知道合并多少个单元格
			int count_take = 0;

			// 如果只有场 连镜都没有,z这个情况要判断一下
			if (shot_list.size() != 0) {

				for (int j = 0; j < shot_list.size(); j++) {

					// 这里为什么要添加一个for循环呢，因为存在一种情况就是，当有镜头 ，但是却没有拍摄次数的时候，会出现问题

					int count = shotImpl.queryCountById(shot_list.get(j)
							.getShot_id());
					if (count == 0) {
						// 如果数量为0，证明有镜头，但没有拍摄，因此也要为单元格合并
						count_take++;
					} else {
						count_take = count_take + count;

					}
				}

			} else {
				count_take = 1;

				// 添加一个空镜，说明，有场景，但是没镜头
				Shot shot = new Shot();
				shot.setShot_id(0);
				shot.setShot_name("未添加任何镜");
				shot_list.add(shot);
			}

			try {
				sheet.mergeCells(scene_pos, point_scene, scene_pos, point_scene
						+ count_take - 1);
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}

			Label label_scene_name = new Label(scene_pos, point_scene,
					scene_list.get(i).getScene_name().toString(), wcfF);

			point_scene = point_scene + count_take;

			try {
				sheet.addCell(label_scene_name);
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}

			for (int j = 0; j < shot_list.size(); j++) {

				int shot_id = shot_list.get(j).getShot_id();

				List<Take> take_list = takeImpl.query(String.valueOf(shot_id));
				// 获取数量，确定合并的单元格的数量
				int count = take_list.size();

				// 在这里再查询一次shot_id
				int count_1 = shotImpl.queryCountById(shot_id);
				if (count_1 == 0) {
					count = 1;

					// 记得补充 有 镜 但是没有 次的情况
					Take take = new Take();
					take.setTake_id(0);
					take.setTake_number(0);
					take_list.add(take);

				}

				try {
					sheet.mergeCells(shot_pos, point_shot, shot_pos, point_shot
							+ count - 1);
				} catch (RowsExceededException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				}

				Label label_shot_name = new Label(shot_pos, point_shot,
						shot_list.get(j).getShot_name().toString(), wcfF);

				point_shot = point_shot + count;

				try {
					sheet.addCell(label_shot_name);
				} catch (RowsExceededException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				}

				for (int k = 0; k < take_list.size(); k++) {

					Label label_take_number = new Label(take_pos, point_take,
							"未添加任何次", wcfF);
					;

					Label label_3 = new Label(video_number_pos, point_take, "",
							wcfF);
					;
					Label label_4 = new Label(audio_number_pos, point_take, "",
							wcfF);
					;
					Label label_5 = new Label(take_time_pos, point_take, "",
							wcfF);
					;
					Label label_6 = new Label(is_available_pos, point_take, "",
							wcfF);
					;
					Label label_7 = new Label(not_avaliable_season_pos,
							point_take, "", wcfF);
					;
					Label label_8 = new Label(roll_name_pos, point_take, "",
							wcfF);
					;

					if (take_list.get(k).getTake_number() == 0) {

					} else {
						label_take_number = new Label(take_pos, point_take,
								take_list.get(k).getTake_number().toString(),
								wcfF);
						label_3 = new Label(video_number_pos, point_take,
								take_list.get(k).getVideo_number().toString(),
								wcfF);
						label_4 = new Label(audio_number_pos, point_take,
								take_list.get(k).getAudio_number().toString(),
								wcfF);
						label_5 = new Label(take_time_pos, point_take,
								take_list.get(k).getTake_time().toString(),
								wcfF);

						int is = take_list.get(k).getIs_available();
						if (is == 0) {
							label_6 = new Label(is_available_pos, point_take,
									"保", wcfF);
						} else if (is == -1) {
							label_6 = new Label(is_available_pos, point_take,
									"不可用", wcfF);
						} else if (is == 1) {
							label_6 = new Label(is_available_pos, point_take,
									"可用", wcfF);
						}

						label_7 = new Label(not_avaliable_season_pos,
								point_take, take_list.get(k)
										.getNot_avaliable_season().toString(),
								wcfF);
						label_8 = new Label(roll_name_pos, point_take,
								take_list.get(k).getRoll_name().toString(),
								wcfF);

					}

					point_take++;
					try {

						sheet.addCell(label_take_number);
						sheet.addCell(label_3);
						sheet.addCell(label_4);
						sheet.addCell(label_5);
						sheet.addCell(label_6);
						sheet.addCell(label_7);
						sheet.addCell(label_8);

					} catch (RowsExceededException e) {
						e.printStackTrace();
					} catch (WriteException e) {
						e.printStackTrace();
					}

				}

			}

		}
		// 4、打开流，开始写文件
		try {
			writeBook.write();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 5、关闭流
		try {
			writeBook.close();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return filename;
	}
}
