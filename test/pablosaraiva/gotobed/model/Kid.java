package pablosaraiva.gotobed.model;

import java.util.Date;

import pablosaraiva.gotobed.annotations.DoNotSave;
import pablosaraiva.gotobed.annotations.Id;
import pablosaraiva.gotobed.annotations.Sleeper;

@Sleeper
public class Kid {
	@Id
	long Id;
	String name;
	int age;
	Date birthdate;

	@DoNotSave
	private String nickname;

}
