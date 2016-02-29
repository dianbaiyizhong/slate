package hhm.slate.activity.storyboard.view.entity;

public class IndexParam {
	public String getShot_keyword() {
		return shot_keyword;
	}

	public void setShot_keyword(String shot_keyword) {
		this.shot_keyword = shot_keyword;
	}

	public String getShots() {
		return shots;
	}

	public void setShots(String shots) {
		this.shots = shots;
	}

	public String getSearch_keyword() {
		return search_keyword;
	}

	public void setSearch_keyword(String search_keyword) {
		this.search_keyword = search_keyword;
	}

	public int getComplete_status() {
		return complete_status;
	}

	public void setComplete_status(int complete_status) {
		this.complete_status = complete_status;
	}

	// 初始值是0表示默认，完成和未完成都统计进来
	private int complete_status = 0;

	@Override
	public String toString() {
		return "IndexParam [complete_status=" + complete_status
				+ ", search_keyword=" + search_keyword + ", shot_keyword="
				+ shot_keyword + ", shots=" + shots + "]";
	}

	private String search_keyword = "";
	private String shot_keyword = "";
	private String shots = "";
}
