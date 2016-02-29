package hhm.slate.db.entity;

public class Shots {


	
	public Shots(String shots_name) {
		super();
		this.shots_name = shots_name;
	}
	
	
	public Shots(){
		
	}
	private int shots_id;
	public int getShots_id() {
		return shots_id;
	}
	public void setShots_id(int shots_id) {
		this.shots_id = shots_id;
	}
	public String getShots_name() {
		return shots_name;
	}
	public void setShots_name(String shots_name) {
		this.shots_name = shots_name;
	}
	private String shots_name;
	
	

}
