package com.solidbrain.russads.configuration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by PLADMUC on 2016-05-11.
 */
@Entity
public class ConfigurationProperty {
	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String key;

	@Column
	private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
