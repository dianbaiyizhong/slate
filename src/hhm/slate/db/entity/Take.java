package hhm.slate.db.entity;

import java.sql.Timestamp;

public class Take {

	public Integer getTake_id() {
		return take_id;
	}

	public void setTake_id(Integer take_id) {
		this.take_id = take_id;
	}

	public Integer getTake_number() {
		return take_number;
	}

	public void setTake_number(Integer take_number) {
		this.take_number = take_number;
	}

	public Integer getIs_available() {
		return is_available;
	}

	public void setIs_available(Integer is_available) {
		this.is_available = is_available;
	}

	public String getNot_avaliable_season() {
		return not_avaliable_season;
	}

	public void setNot_avaliable_season(String not_avaliable_season) {
		this.not_avaliable_season = not_avaliable_season;
	}

	public Timestamp getTake_time() {
		return take_time;
	}

	public void setTake_time(Timestamp take_time) {
		this.take_time = take_time;
	}

	@Override
	public String toString() {
		return "Take [take_id=" + take_id + ", take_number=" + take_number
				+ ", take_time=" + take_time + ", is_available=" + is_available
				+ ", not_avaliable_season=" + not_avaliable_season + "]";
	}

	public String getRoll_name() {
		return roll_name;
	}

	public void setRoll_name(String roll_name) {
		this.roll_name = roll_name;
	}

	private String roll_name;
	public Integer getShot_id() {
		return shot_id;
	}

	public void setShot_id(Integer shot_id) {
		this.shot_id = shot_id;
	}

	private Integer shot_id;


	public String getTake_image() {
		return take_image;
	}

	public void setTake_image(String take_image) {
		this.take_image = take_image;
	}

	private Integer take_id;
	private Integer take_number;
	private Timestamp take_time;
	private Integer is_available;
	private String take_image;
	private String not_avaliable_season;
	public String getVideo_number() {
		return video_number;
	}

	public void setVideo_number(String video_number) {
		this.video_number = video_number;
	}

	public String getAudio_number() {
		return audio_number;
	}

	public void setAudio_number(String audio_number) {
		this.audio_number = audio_number;
	}

	private String video_number;
	private String audio_number;
	
	
	



}
