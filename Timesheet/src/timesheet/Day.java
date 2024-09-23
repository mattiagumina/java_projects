package timesheet;

public class Day {
	
	protected int dayOfYear;
	protected boolean holiday;
	protected int dayOfWeek;
	
	public Day(int dayOfYear) {
		this.dayOfYear = dayOfYear;
		this.holiday = false;
		this.dayOfWeek = dayOfYear % 7;
	}

	public int getDayOfYear() {
		return dayOfYear;
	}

	public boolean isHoliday() {
		return holiday;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}	
	
	public void setDayHoliday() {
		this.holiday = true;
	}
}
