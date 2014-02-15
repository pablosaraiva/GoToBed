package pablosaraiva.gotobed.model;

import pablosaraiva.gotobed.annotations.DoNotSave;
import pablosaraiva.gotobed.annotations.Id;
import pablosaraiva.gotobed.annotations.Sleeper;

@Sleeper
public class Kid {
	@Id
	private long Id;
	private String name;
	@DoNotSave
	private String nickname;
	
	private int age;
	
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

}
