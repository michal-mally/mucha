package com.solidbrain.russads.configuration;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.solidbrain.russads.configuration.model.ConfigurationProperty;

public class ConfigurationHelper {

	public static final String AUTO_SALES_REP_KEY = "autoSalesRep";

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	public void fakeFlayway() {
		ConfigurationProperty configurationProperty = new ConfigurationProperty();
		configurationProperty.setKey(AUTO_SALES_REP_KEY);
		configurationProperty.setValue("920274");
		entityManager.persist(configurationProperty);

		List<ConfigurationProperty> list =
				entityManager.createQuery("Select c from ConfigurationProperty c", ConfigurationProperty.class)
						.getResultList();

		System.out.println(list);
	}



}
