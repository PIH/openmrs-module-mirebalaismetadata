/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.mirebalaismetadata.deploy.bundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.OrderType;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.packageFile;

/**
 * Radiology metadata bundle
 */
public abstract class MirebalaisMetadataBundle extends AbstractMetadataBundle {

	public static final String SYSTEM_PROPERTY_SKIP_METADATA_SHARING_PACKAGE_REFRESH = "skipMetadataSharingPackageRefresh";

	protected Log log = LogFactory.getLog(getClass());

	@Autowired
	PlatformTransactionManager platformTransactionManager;

	/**
	 * Setting multiple GPs is much faster in a single transaction
	 */
	protected void setGlobalProperties(final Map<String, String> properties) {
		TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				for (Map.Entry<String, String> entry : properties.entrySet()) {
					setGlobalProperty(entry.getKey(), entry.getValue());
				}
			}
		});
	}

	/**
	 * Update the global property with the given name to the given value, creating it if it doesn't exist
	 */
	protected void setGlobalProperty(String propertyName, String propertyValue) {
		AdministrationService administrationService = Context.getAdministrationService();
		GlobalProperty gp = administrationService.getGlobalPropertyObject(propertyName);
		if (gp == null) {
			gp = new GlobalProperty(propertyName);
		}
		gp.setPropertyValue(propertyValue);
		administrationService.saveGlobalProperty(gp);
	}

	/**
	 * Utility method that verifies that a particular concept mapping is set up correctly
	 * @param conceptCode
	 * @param conceptSource
	 */
	protected void verifyConceptPresent(String conceptCode, String conceptSource) {
		if (Context.getConceptService().getConceptByMapping(conceptCode, conceptSource) == null) {
			throw new RuntimeException("No concept tagged with code " + conceptCode + " from source " + conceptSource);
		}
	}

	/**
	 * Utility method to install an MDS package if it hasn't been disabled by a system property
	 * @param filename
	 * @param groupUuid
	 * @return true if the package was installed
	 */
	protected boolean installMetadataSharingPackage(String filename, String groupUuid) {
		String systemProperty = System.getProperty(SYSTEM_PROPERTY_SKIP_METADATA_SHARING_PACKAGE_REFRESH, "false");
		if (Boolean.parseBoolean(systemProperty)) {
			log.warn("Skipping refresh of MDS package: " + filename);
			return false;
		}
		log.warn("Installing Metadata Sharing package: " + filename);
		install(packageFile(filename, null, groupUuid));
		return true;
	}

	/**
	 * Constructs an order type
	 * @param name the name
	 * @param description the description
	 * @param uuid the UUID
	 * @return the transient object
	 */
	@SuppressWarnings("deprecation")
	protected static OrderType orderType(String name, String description, String uuid) {
		OrderType obj = new OrderType();
		obj.setName(name);
		obj.setDescription(description);
		obj.setUuid(uuid);
		return obj;
	}
}