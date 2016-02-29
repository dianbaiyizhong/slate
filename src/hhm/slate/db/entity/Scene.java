package hhm.slate.db.entity;

import java.util.List;

public class Scene {

	public int getScene_id() {
		return scene_id;
	}

	public void setScene_id(int scene_id) {
		this.scene_id = scene_id;
	}

	public String getScene_name() {
		return scene_name;
	}

	public void setScene_name(String scene_name) {
		this.scene_name = scene_name;
	}

	public int getFilm_id() {
		return film_id;
	}

	public void setFilm_id(int film_id) {
		this.film_id = film_id;
	}

	public int getScene_number() {
		return scene_number;
	}

	public void setScene_number(int scene_number) {
		this.scene_number = scene_number;
	}

	public Scene(int scene_number, int film_id, String scene_name,
			String scene_pos) {
		super();
		this.scene_number = scene_number;
		this.film_id = film_id;
		this.scene_name = scene_name;
		this.scene_pos = scene_pos;
	}

	public Scene(int scene_number, int film_id, String scene_name) {
		super();
		this.scene_number = scene_number;
		this.film_id = film_id;
		this.scene_name = scene_name;
	}

	public Scene() {

	}

	private int scene_number;
	private int film_id;
	private int scene_id;

	@Override
	public String toString() {
		return scene_name;
	}

	private String scene_name;
	private String scene_pos;

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	private String distance;

	public String getScene_pos() {
		return scene_pos;
	}

	public void setScene_pos(String scene_pos) {
		this.scene_pos = scene_pos;
	}

	public Shot getShot() {
		return shot;
	}

	public void setShot(Shot shot) {
		this.shot = shot;
	}

	private Shot shot;

	public List<Shot> getShotList() {
		return shotList;
	}

	public void setShotList(List<Shot> shotList) {
		this.shotList = shotList;
	}

	private List<Shot> shotList;

}
