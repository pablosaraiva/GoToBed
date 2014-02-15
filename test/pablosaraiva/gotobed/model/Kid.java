package pablosaraiva.gotobed.model;

import java.util.Date;

import pablosaraiva.gotobed.annotations.DoNotSave;
import pablosaraiva.gotobed.annotations.Sleeper;
import pablosaraiva.gotobed.annotations.SleeperId;

@Sleeper
public class Kid {
	@SleeperId
	private long id;
	private String name;
	private int age;
	private Date birthdate;

	@DoNotSave
	private String nickname;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

}
