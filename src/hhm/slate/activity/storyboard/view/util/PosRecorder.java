package hhm.slate.activity.storyboard.view.util;

public class PosRecorder {

	public static int getScene_pos_id() {
		return scene_pos_id;
	}

	public static void setScene_pos_id(int value) {
		scene_pos_id = value;
	}

	public static int getShot_pos_id() {
		return shot_pos_id;
	}

	public static void setShot_pos_id(int value) {
		shot_pos_id = value;
	}

	public static int getTake_pos_id() {
		return take_pos_id;
	}

	public static void setTake_pos_id(int value) {
		take_pos_id = value;
	}

	public static void reset() {
		shot_pos_id = 0;
		take_pos_id = 0;
	}

	public static void resetAll() {
		shot_pos_id = 0;
		take_pos_id = 0;
		scene_pos_id = 0;
	}

	public static int scene_pos_id = 0;
	public static int shot_pos_id = 0;
	public static int take_pos_id = 0;

	public static String getCurrent_pos() {
		return current_pos;
	}

	public static void setCurrent_pos(String value) {
		current_pos = value;
	}

	public static String current_pos = null;
}
