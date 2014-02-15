package pablosaraiva.gotobed.model;

import java.util.Date;

import pablosaraiva.gotobed.annotations.DoNotSave;
import pablosaraiva.gotobed.annotations.SleeperId;
import pablosaraiva.gotobed.annotations.Sleeper;

@Sleeper
public class Kid {
	@SleeperId
	private
	long id;
	String name;
	int age;
	Date birthdate;

	@DoNotSave
	private String nickname;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
