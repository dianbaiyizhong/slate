package hhm.slate.db.entity;

public class Shot {

	public Integer getShot_id() {
		return shot_id;
	}

	public void setShot_id(Integer shot_id) {
		this.shot_id = shot_id;
	}

	public String getShot_name() {
		return shot_name;
	}

	public void setShot_name(String shot_name) {
		this.shot_name = shot_name;
	}

	public String getShots() {
		return shots;
	}

	public void setShots(String shots) {
		this.shots = shots;
	}

	public Integer getScene_id() {
		return scene_id;
	}

	public void setScene_id(Integer scene_id) {
		this.scene_id = scene_id;
	}

	public Integer getShot_number() {
		return shot_number;
	}

	public void setShot_number(Integer shot_number) {
		this.shot_number = shot_number;
	}

	@Override
	public String toString() {
		return "Shot [shot_id=" + shot_id + ", shot_name=" + shot_name
				+ ", shots=" + shots + ", scene_id=" + scene_id
				+ ", shot_number=" + shot_number + "]";
	}

	public Shot(Integer shot_id, String shot_name, String shots,
			Integer shot_number) {
		super();
		this.shot_id = shot_id;
		this.shot_name = shot_name;
		this.shots = shots;
		this.shot_number = shot_number;
	}

	public Shot() {

	}

	private Integer shot_id;
	private String shot_name;
	private String shots;
	private Integer shot_number;

	private Integer scene_id;



	private String shot_keyword;

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	private Scene scene;

	public Take getTake() {
		return take;
	}

	public void setTake(Take take) {
		this.take = take;
	}

	private Take take;



	public String getShot_keyword() {
		return shot_keyword;
	}

	public void setShot_keyword(String shot_keyword) {
		this.shot_keyword = shot_keyword;
	}
	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	private int statu;

}
