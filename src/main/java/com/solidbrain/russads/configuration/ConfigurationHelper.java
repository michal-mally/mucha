package com.solidbrain.russads.configuration;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.solidbrain.russads.configuration.model.ConfigurationProperty;

public class ConfigurationHelper {

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	public void doSf(){
		ConfigurationProperty configurationProperty = new ConfigurationProperty();
		configurationProperty.setKey("test");
		configurationProperty.setValue("12134dasf");
		entityManager.persist(configurationProperty);

		List<ConfigurationProperty> list =
		entityManager.createQuery("Select c from ConfigurationProperty c", ConfigurationProperty.class).getResultList();

		System.out.println(list);
	}

}
