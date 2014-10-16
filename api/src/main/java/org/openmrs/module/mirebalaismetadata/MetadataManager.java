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

package org.openmrs.module.mirebalaismetadata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.metadatadeploy.api.MetadataDeployService;
import org.openmrs.module.metadatadeploy.bundle.MetadataBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Metadata package manager
 */
@Component
public class MetadataManager {

	protected static final Log log = LogFactory.getLog(MetadataManager.class);

	public static final String SYSTEM_PROPERTY_SKIP_REFRESH = "skipMetadataRefresh";

	@Autowired
	private MetadataDeployService deployService;

	/**
	 * Inspired from kenyacore and kenyaemr, this loads metadata bundles.
	 * TODO: Extract out classes fron kenyaemr into common classes that can be used by broader implementations
	 */
	public synchronized void refresh() {
		// Allow skipping of metadata refresh - useful for developers
		if (Boolean.parseBoolean(System.getProperty(SYSTEM_PROPERTY_SKIP_REFRESH))) {
			log.warn("Skipping metadata refresh");
			return;
		}

		// Install bundle components
		deployService.installBundles(Context.getRegisteredComponents(MetadataBundle.class));
	}
}