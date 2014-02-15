package pablosaraiva.gotobed.model;

import java.util.Date;

import pablosaraiva.gotobed.annotations.DoNotSave;
import pablosaraiva.gotobed.annotations.Sleeper;
import pablosaraiva.gotobed.annotations.SleeperId;

@Sleeper
public class Kid {
	@SleeperId
	private long myid;
	private String name;
	private Date birthdate;

	@DoNotSave
	private String nickname;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

}
