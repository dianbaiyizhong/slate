package hhm.slate.activity.storyboard.view.util;

public class StringRecorder {

	public static String getScene_ShowString() {
		return scene_ShowString;
	}

	public static void setScene_ShowString(String value) {
		scene_ShowString = value;
	}

	public static String scene_ShowString = "请选择 场";

	public static String shot_ShowString = "";

	public static String getShot_ShowString() {
		return shot_ShowString;
	}

	public static void setShot_ShowString(String value) {
		shot_ShowString = value;
	}

	public static String getTake_ShowString() {
		return take_ShowString;
	}

	public static void setTake_ShowString(String value) {
		take_ShowString = value;
	}

	public static String take_ShowString = "";

	public static String getShot_or_take_ShowString() {
		String result = shot_ShowString + take_ShowString;
		if (result.equals("")) {
			return "请选择 镜次";
		}
		return shot_ShowString + take_ShowString;
	}

	public static void setShot_or_take_ShowString(String value) {
		shot_or_take_ShowString = value;
	}

	public static String shot_or_take_ShowString = "请选择 镜次";
	public static void reset() {
		shot_or_take_ShowString = "请选择 镜次";
		take_ShowString = "";
		shot_ShowString = "";
	}
	public static void resetAll() {
		scene_ShowString = "请选择 场";
		shot_or_take_ShowString = "请选择 镜次";
		take_ShowString = "";
		shot_ShowString = "";
	}
}
