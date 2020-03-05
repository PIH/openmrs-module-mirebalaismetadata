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
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.emrapi.EmrApiProperties;
import org.openmrs.module.pihcore.config.Config;
import org.openmrs.module.pihcore.config.ConfigDescriptor;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class MirebalaisMetadataActivator extends BaseModuleActivator {

    protected Log log = LogFactory.getLog(getClass());

    private ConceptService conceptService;

    private Config config;

    /**
     * @see ModuleActivator#contextRefreshed()
     */
    public void contextRefreshed() {
        log.info("Mirebalais Metadata module refreshed");
    }

    /**
     * @see ModuleActivator#started()
     */
    public void started() {

        if (config == null) {  // hack to allow injecting a mock config for testing
            config = Context.getRegisteredComponents(Config.class).get(0); // currently only one of these
        }
        if (conceptService == null) {
            conceptService = Context.getConceptService();
        }
        try {
            if (config.getCountry().equals(ConfigDescriptor.Country.HAITI)) {
                retireOldConcepts();
            }
        }
		catch (Exception e) {
            Module mod = ModuleFactory.getModuleById("mirebalaismetadata");
            ModuleFactory.stopModule(mod, true, true);
            throw new RuntimeException("Failed to start the mirebalaismetadata module", e);
        }
        log.info("Mirebalais Metadata module started");
    }

    private EmrApiProperties getEmrApiProperties() {
        return Context.getRegisteredComponents(EmrApiProperties.class).iterator().next();
    }

    private void retireOldConcepts() {
        // we need to retire this old concept if it exists because another concept we will install later has the same French name
        // #UHM-1789 - Removed concept "cerebellar infarction”from HUM ED set, and added “cerebral infarction"
        Concept concept = conceptService.getConceptByUuid("145906AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        if (concept != null) {
            conceptService.retireConcept(concept, "replaced with by 155479AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        }
    }

    // hack to allow us to inject a mock config during testing
    public void setConfig(Config config) {
        this.config = config;
    }
}
