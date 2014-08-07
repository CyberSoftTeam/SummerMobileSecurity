package vn.cybersoft.summerms.model;

import java.sql.Date;

public class DateTraffic {
	private String date ;
	private long startupLoad;
	private long startdownLoad;
	public DateTraffic(String date2, long tx, long rx) {
		super();
		this.date = date2;
		this.startupLoad = tx;
		this.startdownLoad = rx;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public long getStartupLoad() {
		return startupLoad;
	}
	public void setStartupLoad(long startupLoad) {
		this.startupLoad = startupLoad;
	}
	public long getStartdownLoad() {
		return startdownLoad;
	}
	public void setStartdownLoad(long startdownLoad) {
		this.startdownLoad = startdownLoad;
	}
	public DateTraffic() {
		// TODO Auto-generated constructor stub
	}
}
