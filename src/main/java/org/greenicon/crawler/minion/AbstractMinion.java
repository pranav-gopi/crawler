package org.greenicon.crawler.minion;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 
 * @author Pranav
 *
 */
@MappedSuperclass
public abstract class AbstractMinion implements IMinion, Runnable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column
	private String id;
	
	@Column
	private String name;

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "AbstractMinion [id=" + id + ", name=" + name + "]";
	}

	@Override
	public void run() {
		doSomething();
	}
	
}
