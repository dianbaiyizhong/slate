package hhm.slate.db.entity;

public class Staff {

	public int getStaff_id() {
		return staff_id;
	}

	public void setStaff_id(int staff_id) {
		this.staff_id = staff_id;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getAdditional_camera() {
		return additional_camera;
	}

	public void setAdditional_camera(String additional_camera) {
		this.additional_camera = additional_camera;
	}

	public String getArmorer() {
		return armorer;
	}

	public void setArmorer(String armorer) {
		this.armorer = armorer;
	}

	public String getArt_director() {
		return art_director;
	}

	public void setArt_director(String art_director) {
		this.art_director = art_director;
	}

	public String getAssistant_camera() {
		return assistant_camera;
	}

	public void setAssistant_camera(String assistant_camera) {
		this.assistant_camera = assistant_camera;
	}

	public String getAssistant_director() {
		return assistant_director;
	}

	public void setAssistant_director(String assistant_director) {
		this.assistant_director = assistant_director;
	}

	public String getGaffer() {
		return gaffer;
	}

	public void setGaffer(String gaffer) {
		this.gaffer = gaffer;
	}

	public String getMakeup() {
		return makeup;
	}

	public void setMakeup(String makeup) {
		this.makeup = makeup;
	}

	public String getProperty_master() {
		return property_master;
	}

	public void setProperty_master(String property_master) {
		this.property_master = property_master;
	}

	public String getScreenwriter() {
		return screenwriter;
	}

	public void setScreenwriter(String screenwriter) {
		this.screenwriter = screenwriter;
	}

	public String getCamera_operator() {
		return camera_operator;
	}

	public void setCamera_operator(String camera_operator) {
		this.camera_operator = camera_operator;
	}

	public int getFilm_id() {
		return film_id;
	}

	public void setFilm_id(int film_id) {
		this.film_id = film_id;
	}



	private int staff_id;
	@Override
	public String toString() {
		return "Staff [staff_id=" + staff_id + ", director=" + director
				+ ", additional_camera=" + additional_camera + ", armorer="
				+ armorer + ", art_director=" + art_director
				+ ", assistant_camera=" + assistant_camera
				+ ", assistant_director=" + assistant_director + ", gaffer="
				+ gaffer + ", makeup=" + makeup + ", property_master="
				+ property_master + ", screenwriter=" + screenwriter
				+ ", camera_operator=" + camera_operator + ", film_id="
				+ film_id + ", camera=" + camera + ", lighting=" + lighting
				+ ", production=" + production + "]";
	}

	private String director;
	private String additional_camera;
	private String armorer;
	private String art_director;
	private String assistant_camera;
	private String assistant_director;
	private String gaffer;
	private String makeup;
	private String property_master;
	private String screenwriter;
	private String camera_operator;
	private int film_id;

	private String camera;
	private String lighting;
	private String production;

	public String getCamera() {
		return camera;
	}

	public void setCamera(String camera) {
		this.camera = camera;
	}

	public String getLighting() {
		return lighting;
	}

	public void setLighting(String lighting) {
		this.lighting = lighting;
	}

	public String getProduction() {
		return production;
	}

	public void setProduction(String production) {
		this.production = production;
	}

}
