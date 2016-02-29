package hhm.slate.db.entity;

public class Roll {

	public int getRoll_id() {
		return roll_id;
	}

	public void setRoll_id(int roll_id) {
		this.roll_id = roll_id;
	}

	public String getRoll_name() {
		return roll_name;
	}

	public void setRoll_name(String roll_name) {
		this.roll_name = roll_name;
	}

	@Override
	public String toString() {
		return "Roll [roll_id=" + roll_id + ", roll_name=" + roll_name + "]";
	}



	public Roll() {

	}

	public Roll(String roll_name) {
		super();
		this.roll_name = roll_name;
	}

	private int roll_id;
	private String roll_name;

}
