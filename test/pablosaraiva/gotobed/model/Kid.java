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
	
	public long getId() {
		return myid;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (myid ^ (myid >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Kid other = (Kid) obj;
		if (myid != other.myid)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	

}
