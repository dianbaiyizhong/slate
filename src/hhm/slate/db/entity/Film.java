package hhm.slate.db.entity;

import java.sql.Timestamp;

public class Film {
	public Integer getFilm_id() {
		return film_id;
	}

	public void setFilm_id(Integer film_id) {
		this.film_id = film_id;
	}

	public String getFilm_name() {
		return film_name;
	}

	public void setFilm_name(String film_name) {
		this.film_name = film_name;
	}

	public Timestamp getFilm_createtime() {
		return film_createtime;
	}

	public void setFilm_createtime(Timestamp film_createtime) {
		this.film_createtime = film_createtime;
	}

	public Timestamp getFilm_wraptime() {
		return film_wraptime;
	}

	public void setFilm_wraptime(Timestamp film_wraptime) {
		this.film_wraptime = film_wraptime;
	}

	public Integer getStaff_id() {
		return staff_id;
	}

	public void setStaff_id(Integer staff_id) {
		this.staff_id = staff_id;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Shot getShot() {
		return shot;
	}

	public void setShot(Shot shot) {
		this.shot = shot;
	}

	public Take getTake() {
		return take;
	}

	public void setTake(Take take) {
		this.take = take;
	}

	@Override
	public String toString() {
		return "Film [scene=" + scene + ", shot=" + shot + ", take=" + take
				+ ", film_id=" + film_id + ", film_name=" + film_name
				+ ", film_createtime=" + film_createtime + ", film_wraptime="
				+ film_wraptime + ", staff_id=" + staff_id + ", staff=" + staff
				+ "]";
	}

	private Integer film_id;
	private String film_name;
	private Timestamp film_createtime;
	private Timestamp film_wraptime;
	private Integer staff_id;
	private Staff staff;
	private Scene scene;
	private Shot shot;
	private Take take;

	public int[] getFilmid() {
		return filmid;
	}

	public void setFilmid(int[] filmid) {
		this.filmid = filmid;
	}

	public String[] getFilmname() {
		return filmname;
	}

	public void setFilmname(String[] filmname) {
		this.filmname = filmname;
	}

	private int[] filmid;
	private String[] filmname;

}
